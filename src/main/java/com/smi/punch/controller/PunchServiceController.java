package com.smi.punch.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.smi.punch.IContent;
import com.smi.punch.service.OGServiceClient;
import com.smi.punch.service.PunchServiceClient;
import com.smi.punch.vo.DoorLogVO;
import com.smi.punch.vo.EhrEmpVO;
import com.smi.punch.vo.PunchRecordVO;

@RestController
@RequestMapping("/api/")
public class PunchServiceController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private OGServiceClient ogClient;

	@Autowired
	private PunchServiceClient punchService;
	
	@GetMapping(path = "v1/punchCard/delRecord/{id}", produces = "application/json;charset=utf-8")
	public Map<String,Object> delRecord(HttpSession session,@PathVariable Integer id) throws Exception {
		EhrEmpVO empVO = (EhrEmpVO)session.getAttribute("_USER");
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("RETCODE", "999");
		result.put("RET", false);
		boolean sqlResult = punchService.delRecord(id, empVO.getEmpNo());
		if(sqlResult) {
			result.put("RETCODE", "000");
			result.put("RET", true);
		}
		result.put("MSG", new ArrayList<String>());
		return result;
	}
	@GetMapping(path = "v2/doorLogs/get/{dateStr}", produces = "application/json;charset=utf-8")
	public Map<String,String> getDeptMemVO2(HttpSession session,@PathVariable String dateStr) throws Exception {
		EhrEmpVO empVO = (EhrEmpVO)session.getAttribute("_USER");
		
		Map<String,String> doorLogs = new HashMap<String,String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeSdf = new SimpleDateFormat("HH:mm");
		Date today = sdf.parse(dateStr);
		session.setAttribute(IContent._session_date_req, dateStr);
		Gson gson = new Gson();
		List<LinkedTreeMap> doorLogDatas = ogClient.getDoorLogs(empVO.getEmpNo(), sdf.format(today));
		PunchRecordVO mPunchInVO = punchService.getPunchInRecord(empVO.getEmpNo(), sdf.format(today));
		PunchRecordVO mPunchOutVO = punchService.getPunchOutRecord(empVO.getEmpNo(), sdf.format(today));
		
		doorLogs.put("FIRST","N/A");
		doorLogs.put("SECOND","N/A");
		
		if(doorLogDatas.isEmpty()) {
			if(null!=mPunchInVO) {
				doorLogs.put("FIRST",timeSdf.format(new Date(mPunchInVO.getPunchDateTime()))+"(M)");
			}
			if(null!=mPunchOutVO) {
				doorLogs.put("SECOND",timeSdf.format(new Date(mPunchOutVO.getPunchDateTime()))+"(M)");
			}
		}else {
			if(null!=mPunchInVO) {
				doorLogs.put("FIRST",timeSdf.format(new Date(mPunchInVO.getPunchDateTime()))+"(M)");
			}else {
				DoorLogVO doorVO_F = gson.fromJson(gson.toJson(doorLogDatas.get(0)), DoorLogVO.class);
				doorLogs.put("FIRST",timeSdf.format(new Date(doorVO_F.getTime()))+"(D)");
			}
			
			if(null!=mPunchOutVO) {
				doorLogs.put("SECOND",timeSdf.format(new Date(mPunchOutVO.getPunchDateTime()))+"(M)");
			}else {
				DoorLogVO doorVO_L = gson.fromJson(gson.toJson(doorLogDatas.get(1)), DoorLogVO.class);
				doorLogs.put("SECOND",timeSdf.format(new Date(doorVO_L.getTime()))+"(D)");
			}
		}
		
		logger.info("Date:{}, EmpNo:{},DoorLogs: {}",today, empVO.getEmpNo(), doorLogs);
		return doorLogs;
	}
	
	@GetMapping(path = "v1/doorLogs/get/{dateStr}", produces = "application/json;charset=utf-8")
	public Map<String,String> getDeptMemVO1(HttpSession session,@PathVariable String dateStr) throws Exception {
		EhrEmpVO empVO = (EhrEmpVO)session.getAttribute("_USER");
		
		Map<String,String> doorLogs = new HashMap<String,String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeSdf = new SimpleDateFormat("HH:mm");
		if(null==dateStr || "".contentEquals(dateStr)) {
			dateStr = sdf.format(Calendar.getInstance().getTime());
		}
		Date today = sdf.parse(dateStr);
		
	
		Gson gson = new Gson();
		List<LinkedTreeMap> doorLogDatas = ogClient.getDoorLogs(empVO.getEmpNo(), sdf.format(today));
		List<PunchRecordVO> punchRecords = punchService.getTargetDateRecords(empVO.getEmpNo(), sdf.format(today));
		doorLogs.put("FIRST","N/A");
		doorLogs.put("SECOND","N/A");
		if(!punchRecords.isEmpty()) {
			if(!doorLogDatas.isEmpty()) {
				DoorLogVO doorVO_F = gson.fromJson(gson.toJson(doorLogDatas.get(0)), DoorLogVO.class);
				DoorLogVO doorVO_L = gson.fromJson(gson.toJson(doorLogDatas.get(1)), DoorLogVO.class);
				PunchRecordVO punchVO_F = punchRecords.get(0);
				PunchRecordVO punchVO_L = punchRecords.get(1);
				if(doorVO_F.getTime()<=punchVO_F.getPunchDateTime()) {
					doorLogs.put("FIRST",timeSdf.format(new Date(doorVO_F.getTime()))+"(Door)");
				}else {
					doorLogs.put("FIRST",timeSdf.format(new Date(punchVO_F.getPunchDateTime()))+"(Mobile)");
				}
				if(doorVO_L.getTime()>=punchVO_L.getPunchDateTime()) {
					doorLogs.put("SECOND",timeSdf.format(new Date(doorVO_L.getTime()))+"(Door)");
				}else {
					doorLogs.put("SECOND",timeSdf.format(new Date(punchVO_L.getPunchDateTime()))+"(Mobile)");
				}
			}else {
				PunchRecordVO punchVO_F = punchRecords.get(0);
				PunchRecordVO punchVO_L = punchRecords.get(1);
				doorLogs.put("FIRST",timeSdf.format(new Date(punchVO_F.getPunchDateTime()))+"(Mobile)");
				doorLogs.put("SECOND",timeSdf.format(new Date(punchVO_L.getPunchDateTime()))+"(Mobile)");
			}
		}else {
			if(!doorLogDatas.isEmpty()) {
				DoorLogVO doorVO1 = gson.fromJson(gson.toJson(doorLogDatas.get(0)), DoorLogVO.class);
				doorLogs.put("FIRST",timeSdf.format(new Date(doorVO1.getTime()))+"(Door)");
				DoorLogVO doorVO2 = gson.fromJson(gson.toJson(doorLogDatas.get(1)), DoorLogVO.class);
				doorLogs.put("SECOND",timeSdf.format(new Date(doorVO2.getTime()))+"(Door)");
				
			}
		}
		
		logger.info("Date:{}, EmpNo:{},DoorLogs: {}",today, empVO.getEmpNo(), doorLogs);
		return doorLogs;

	}
}

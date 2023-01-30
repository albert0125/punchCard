package com.smi.punch.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smi.punch.IContent;
import com.smi.punch.dao.PunchDAO;
import com.smi.punch.vo.PunchRecordVO;

public class PunchService {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	/*

	private PunchDAO punchDAO;
	

	public boolean addPunchData(PunchRecordVO vo) {
		boolean result = punchDAO.insert(vo);
		return result;
	}
	public List<PunchRecordVO> hasOtherRecordsAtSameDate(String empNo, String calDate, String newRecordType){
		List<PunchRecordVO> records = punchDAO.selectDuplicateRecord(empNo, calDate);
		List<PunchRecordVO> result = new ArrayList<PunchRecordVO>();
		if(newRecordType.contentEquals(IContent._act_punch_in) || newRecordType.contentEquals(IContent._act_punch_out)) {
			for(PunchRecordVO vo : records) {
				if(!vo.getPunchType().contentEquals(IContent._act_punch_in) && !vo.getPunchType().contentEquals(IContent._act_punch_out)) {
					result.add(vo);
				}
			}
		}else {
			for(PunchRecordVO vo : records) {
				if(vo.getPunchType().contentEquals(IContent._act_punch_in) || vo.getPunchType().contentEquals(IContent._act_punch_out)) {
					result.add(vo);
				}
			}
		}
		return result;
	}
	public List<PunchRecordVO> hasDepuiclateRecords(String empNo, String calDate, long newDateTime){
		List<PunchRecordVO> records = punchDAO.selectDuplicateRecord(empNo, calDate);
		List<PunchRecordVO> result = new ArrayList<PunchRecordVO>();
		for(PunchRecordVO vo : records) {
			if(vo.getPunchDateTime()==newDateTime) {
				result.add(vo);
			}
		}
		return result;
	}
	public List<PunchRecordVO> listRecord(String empNo, String sDate, String eDate){
		List<PunchRecordVO> vos = punchDAO.select(empNo, sDate, eDate);
		return vos;
	}
	public PunchRecordVO getPunchInRecord(String empNo, String targetDateStr) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat tdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date targetDate = sdf.parse(targetDateStr);
		Calendar targetCal = Calendar.getInstance();
		targetCal.setTime(targetDate);
		targetCal.set(Calendar.HOUR_OF_DAY, 5);
		targetCal.set(Calendar.MINUTE,0);
		targetCal.set(Calendar.SECOND,0);
		String startDateStr = tdf.format(targetCal.getTime());
		targetCal.add(Calendar.HOUR, 24);
		targetCal.add(Calendar.MINUTE, -1);
		String endDateStr = tdf.format(targetCal.getTime());
		
		PunchRecordVO sqlResult = punchDAO.selectLastRecord(empNo, startDateStr, endDateStr, IContent._act_punch_in);
		logger.info("[getPunchInRecord] empNo:{}, startDateStr:{}, endDateStr:{}, result={}", empNo, startDateStr, endDateStr, sqlResult);
		return sqlResult;
	}
	public PunchRecordVO getPunchOutRecord(String empNo, String targetDateStr) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat tdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date targetDate = sdf.parse(targetDateStr);
		Calendar targetCal = Calendar.getInstance();
		targetCal.setTime(targetDate);
		targetCal.set(Calendar.HOUR_OF_DAY, 5);
		targetCal.set(Calendar.MINUTE,0);
		targetCal.set(Calendar.SECOND,0);
		String startDateStr = tdf.format(targetCal.getTime());
		targetCal.add(Calendar.HOUR, 24);
		targetCal.add(Calendar.MINUTE, -1);
		String endDateStr = tdf.format(targetCal.getTime());
		
		PunchRecordVO sqlResult = punchDAO.selectLastRecord(empNo, startDateStr, endDateStr, IContent._act_punch_out);
		logger.info("[getPunchOutRecord] empNo:{}, startDateStr:{}, endDateStr:{}, result={}", empNo, startDateStr, endDateStr, sqlResult);
		return sqlResult;
	}
	public List<PunchRecordVO> getTargetDateRecords(String empNo, String targetDateStr) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat tdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date targetDate = sdf.parse(targetDateStr);
		Calendar targetCal = Calendar.getInstance();
		targetCal.setTime(targetDate);
		targetCal.set(Calendar.HOUR_OF_DAY, 5);
		targetCal.set(Calendar.MINUTE,0);
		targetCal.set(Calendar.SECOND,0);
		String startDateStr = tdf.format(targetCal.getTime());
		targetCal.add(Calendar.HOUR, 24);
		targetCal.add(Calendar.MINUTE, -1);
		String endDateStr = tdf.format(targetCal.getTime());
		
		List<PunchRecordVO> sqlResult = punchDAO.selectPunchTime(empNo, startDateStr, endDateStr);
		List<PunchRecordVO> result = new ArrayList<PunchRecordVO>();
		if(!sqlResult.isEmpty()) {
			result.add(sqlResult.get(sqlResult.size()-1));
			result.add(sqlResult.get(0));
		}
		logger.info("empNo:{}, startDateStr:{}, endDateStr:{}, result={}", empNo, startDateStr, endDateStr,result);
		return result;
	}
	
	public boolean delRecord(Integer id, String empNo) {
		logger.info("DELETE: emp_no:{}, id:{}", StringUtils.replaceChars(empNo, "[\r\n]",""), StringUtils.replaceChars(String.valueOf(id), "[\r\n]",""));
		return punchDAO.delete(id, empNo);
	}
	*/
}

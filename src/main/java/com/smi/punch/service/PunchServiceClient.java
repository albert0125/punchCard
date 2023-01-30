package com.smi.punch.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.smi.punch.utils.MyUtils;
import com.smi.punch.vo.PunchRecordVO;
import com.smi.punch.vo.ServiceRespVO;

@Service
public class PunchServiceClient {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${myserver.PunchServiceHost}")
	private String serviceHost;
	
	@Autowired
	RestTemplate restTemplate;
	
	public boolean addPunchData(PunchRecordVO vo) {
		Gson gson = new Gson();
		/*
		Map<String,Object> dataMap = new HashMap<String,Object>();
		dataMap.put("deptNo", vo.getDeptNo());
		dataMap.put("deptName", vo.getDeptName());
		dataMap.put("empNo", vo.getEmpNo());
		dataMap.put("name", vo.getName());
		dataMap.put("punchDateTime", new Long(vo.getPunchDateTime()));
		dataMap.put("punchType", vo.getPunchType());
		dataMap.put("otherDesc", vo.getOtherDesc());
		*/
		logger.info(gson.toJson(vo).toString());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> req = new HttpEntity<String>(gson.toJson(vo).toString(),headers);

		ResponseEntity<String> entity = restTemplate.postForEntity(serviceHost + "/addPunchData", req, String.class);
		if (entity.getStatusCode().is2xxSuccessful()) {
			logger.info("[addPunchData] res:{}", entity.getBody());
			ServiceRespVO resp = gson.fromJson(entity.getBody(), ServiceRespVO.class);
			if ("000".equals(resp.getRETCODE())) {
				String ret =  String.valueOf(resp.getRET());	
				return new Boolean(ret);
			}else {
				logger.error("[addPunchData] 新增記錄失敗!MSG={}", resp.getMSG());
			}
		} else {
			logger.error("[addPunchData] res:{}", entity.getBody());
		}
		return false;
	}
	public boolean hasOtherRecordsAtSameDate(String empNo, String calDate, String newRecordType) throws UnsupportedEncodingException{
		calDate = StringUtils.replace(calDate, "/", "-");
		calDate = URLEncoder.encode(calDate, "utf-8");
		newRecordType = URLEncoder.encode(newRecordType, "utf-8");
		String url = serviceHost + "/hasOtherRecordsAtSameDate/"+empNo+"/"+calDate+"/"+newRecordType;
		logger.info("[URL] = {}",MyUtils.filterCRLF(url));
		ResponseEntity<String> entity = restTemplate.getForEntity(url,String.class);
		Gson gson = new Gson();
		if (entity.getStatusCode().is2xxSuccessful()) {
			ServiceRespVO resp = gson.fromJson(entity.getBody(), ServiceRespVO.class);
			if ("000".equals(resp.getRETCODE())) {
				return (Boolean)resp.getRET();
			}else {
				logger.error("[hasOtherRecordsAtSameDate] RETCODE={}, MSG={}", resp.getRETCODE(), resp.getMSG());
			}
		}
		return true;
	}
	public boolean hasDepuiclateRecords(String empNo, String calDate, Long newDateTime) throws UnsupportedEncodingException{
		calDate = StringUtils.replace(calDate, "/", "-");
		calDate = URLEncoder.encode(calDate, "utf-8");
		ResponseEntity<String> entity = restTemplate.getForEntity(serviceHost + "/hasDepuiclateRecords/"+empNo+"/"+calDate+"/"+newDateTime.toString(),String.class);
		Gson gson = new Gson();
		if (entity.getStatusCode().is2xxSuccessful()) {
			ServiceRespVO resp = gson.fromJson(entity.getBody(), ServiceRespVO.class);
			if ("000".equals(resp.getRETCODE())) {
				return (Boolean) resp.getRET();
			}else {
				logger.error("[hasDepuiclateRecords] RETCODE={}, MSG={}", resp.getRETCODE(), resp.getMSG());
			}
		}
		return true;
	}
	public List<PunchRecordVO> listRecord(String empNo, String sDate, String eDate) throws UnsupportedEncodingException{
		List<PunchRecordVO> result = new ArrayList<PunchRecordVO>();
		sDate = StringUtils.replace(sDate, "/", "-");
		eDate = StringUtils.replace(eDate, "/", "-");
		sDate = URLEncoder.encode(sDate, "utf-8");
		eDate = URLEncoder.encode(eDate, "utf-8");
		ResponseEntity<String> entity = restTemplate.getForEntity(serviceHost + "/listRecord/"+empNo+"/"+sDate+"/"+eDate,String.class);
		if (entity.getStatusCode().is2xxSuccessful()) {
			JsonObject jobject =  new JsonParser().parseString(entity.getBody()).getAsJsonObject();
			if ("000".equals(jobject.get("RETCODE").getAsString())) {
				JsonArray jarray = jobject.get("RET").getAsJsonArray();
				for (final JsonElement elem : jarray) {
					result.add(new Gson().fromJson(elem, PunchRecordVO.class));
		        }
				return result;
			}
		}
		return new ArrayList<PunchRecordVO>();
	}
	public PunchRecordVO getPunchInRecord(String empNo, String targetDateStr) throws ParseException {
		Gson gson = new Gson();
		ResponseEntity<String> entity = restTemplate.getForEntity(serviceHost + "/getPunchInRecord/"+empNo+"/"+targetDateStr,String.class);
		if (entity.getStatusCode().is2xxSuccessful()) {
			ServiceRespVO resp = gson.fromJson(entity.getBody(), ServiceRespVO.class);
			if ("000".equals(resp.getRETCODE())) {
				return (PunchRecordVO) resp.getRET();
			}
		}
		return null;
	}
	public PunchRecordVO getPunchOutRecord(String empNo, String targetDateStr) throws ParseException {
		Gson gson = new Gson();
		ResponseEntity<String> entity = restTemplate.getForEntity(serviceHost + "/getPunchOutRecord/"+empNo+"/"+targetDateStr,String.class);
		if (entity.getStatusCode().is2xxSuccessful()) {
			ServiceRespVO resp = gson.fromJson(entity.getBody(), ServiceRespVO.class);
			if ("000".equals(resp.getRETCODE())) {
				return (PunchRecordVO) resp.getRET();
			}
		}
		return null;
	}
	public List<PunchRecordVO> getTargetDateRecords(String empNo, String targetDateStr) throws ParseException{
		List<PunchRecordVO> result = new ArrayList<PunchRecordVO>();
		targetDateStr = StringUtils.replaceChars(targetDateStr, "/", "-");
		ResponseEntity<String> entity = restTemplate.getForEntity(serviceHost + "/getTargetDateRecords/"+empNo+"/"+targetDateStr, String.class);
		if (entity.getStatusCode().is2xxSuccessful()) {
			JsonObject jobject =  new JsonParser().parseString(entity.getBody()).getAsJsonObject();
			if ("000".equals(jobject.get("RETCODE").getAsString())) {
				JsonArray jarray = jobject.get("RET").getAsJsonArray();
				for (final JsonElement elem : jarray) {
					result.add(new Gson().fromJson(elem, PunchRecordVO.class));
		        }
				return result;
			}
		}
		return new ArrayList<PunchRecordVO>();
	}
	
	public boolean delRecord(Integer id, String empNo) {
		Gson gson = new Gson();
		ResponseEntity<String> entity = restTemplate.postForEntity(serviceHost + "/delRecord/"+id+"/"+empNo, null , String.class);
		if (entity.getStatusCode().is2xxSuccessful()) {
			logger.info("[delRecord] res:{}", entity.getBody());
			ServiceRespVO resp = gson.fromJson(entity.getBody(), ServiceRespVO.class);
			if ("000".equals(resp.getRETCODE())) {
				String ret =  String.valueOf(resp.getRET());	
				return new Boolean(ret);
			}
		} else {
			logger.error("[delRecord] res:{}", entity.getBody());
		}
		return false;
	}
}

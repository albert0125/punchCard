package com.smi.punch.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.smi.punch.vo.DoorLogVO;
import com.smi.punch.vo.EhrEmpVO;
import com.smi.punch.vo.ReqResultVO;

@Service
public class OGServiceClient {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${myserver.OGServiceHost}")
	private String ogServiceHost;
	@Autowired
	RestTemplate restTemplate;
	
	public List getDoorLogs(String empNo, String targetDateStr) throws JsonProcessingException{
		targetDateStr = StringUtils.replace(targetDateStr, "/", "-");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> params = new HashMap<String, String>();
		params.put("empNo", empNo);
        params.put("targetDate", targetDateStr);
		String value = mapper.writeValueAsString(params);
		HttpEntity<String> requestEntity = new HttpEntity<String>(value, headers);

		ResponseEntity entity = restTemplate.postForEntity(ogServiceHost + "/quertDoorLogs", requestEntity, String.class);
		if (entity.getStatusCode().is2xxSuccessful()) {
			Gson gson = new Gson();
			ReqResultVO result = gson.fromJson((String) entity.getBody(), ReqResultVO.class);
			if ("000".contentEquals(result.getRetCode())) {
				List vos = gson.fromJson(gson.toJson(result.getRet()), List.class);
				return vos;
			} else {
				for (String m : result.getMsg()) {
					logger.error("[getDoorLogs]{}", m);
				}
			}
		}
		return null;
	}
	
}

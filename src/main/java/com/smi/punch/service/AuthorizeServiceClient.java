package com.smi.punch.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.smi.punch.vo.EhrEmpVO;
import com.smi.punch.vo.ReqResultVO;

@Component(value = "authServiceClient")
public class AuthorizeServiceClient {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${myserver.ADServiceHost}")
	private String adServerHost;
	@Autowired
	RestTemplate restTemplate;
	
	public EhrEmpVO getEmpByNo(String empNo) {
		ResponseEntity<String> entity = restTemplate.getForEntity(adServerHost + "/getEmpByNo/" + empNo, String.class);
		if (entity.getStatusCode().is2xxSuccessful()) {
			Gson gson = new Gson();
			ReqResultVO result = gson.fromJson((String) entity.getBody(), ReqResultVO.class);
			if ("000".contentEquals(result.getRetCode())) {
				System.out.println(result.getRet());
				
				EhrEmpVO vo = gson.fromJson(gson.toJson(result.getRet()), EhrEmpVO.class);
				return vo;
			} else {
				for (String m : result.getMsg()) {
					logger.error("[getEmpVO]{}", m);
				}
			}
		}
		return null;
	}
	public EhrEmpVO getEmpVO(String username) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> entity = restTemplate.getForEntity(adServerHost + "/getEmp/" + username, String.class);
		if (entity.getStatusCode().is2xxSuccessful()) {
			Gson gson = new Gson();
			ReqResultVO result = gson.fromJson((String) entity.getBody(), ReqResultVO.class);
			if ("000".contentEquals(result.getRetCode())) {
				System.out.println(result.getRet());
				
				EhrEmpVO vo = gson.fromJson(gson.toJson(result.getRet()), EhrEmpVO.class);
				return vo;
			} else {
				for (String m : result.getMsg()) {
					logger.error("[getEmpVO]{}", m);
				}
			}
		}
		return null;
	}

	public EhrEmpVO doAuthorize(String username, String password) throws JsonProcessingException {
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", username);
		params.put("password", password);
		String value = mapper.writeValueAsString(params);
		HttpEntity<String> requestEntity = new HttpEntity<String>(value, headers);
	
		ResponseEntity entity = restTemplate.postForEntity(adServerHost + "/authorize", requestEntity, String.class);
		if (entity.getStatusCode().is2xxSuccessful()) {
			Gson gson = new Gson();
			ReqResultVO result = gson.fromJson((String) entity.getBody(), ReqResultVO.class);
			if ("000".contentEquals(result.getRetCode())) {
				EhrEmpVO vo = gson.fromJson(gson.toJson(result.getRet()), EhrEmpVO.class);
				return vo;
			} else {
				for (String m : result.getMsg()) {
					logger.error("[doAuthorize]{}", m);
				}
			}
		}
		return null;
	}
}
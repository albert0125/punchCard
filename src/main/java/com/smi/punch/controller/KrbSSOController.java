package com.smi.punch.controller;

import java.text.ParseException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
@Controller
public class KrbSSOController {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value = {"/SSOLogin.html" } ,method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?>  index(HttpSession session, HttpServletRequest req,HttpServletResponse res) throws JsonProcessingException, ParseException {
		HttpHeaders headers = new HttpHeaders();
		String contextPath = req.getContextPath();
		if(SecurityContextHolder.getContext().getAuthentication()!=null) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			logger.info("[principal] principal={}",principal);
			if("anonymousUser".equals(principal)) {
				 return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
			}else {
				
				headers.add("Location", contextPath+"/index.html");  
				return new ResponseEntity<String>(headers,HttpStatus.FOUND);
			}
		}
		Enumeration<String> headerNames = req.getHeaderNames();

	    if (headerNames != null) {
	            while (headerNames.hasMoreElements()) {
	                    System.out.println("Header: " + req.getHeader(headerNames.nextElement()));
	            }
	    }
		return new ResponseEntity<String>(headers,HttpStatus.UNAUTHORIZED);
		
	}
}

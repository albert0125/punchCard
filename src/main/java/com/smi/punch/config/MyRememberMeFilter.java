package com.smi.punch.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import com.smi.punch.utils.MyCookieManager;

public class MyRememberMeFilter extends GenericFilterBean{
	private String _form_remenberMe = "remenberMe";
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		if("true".equals(request.getParameter(_form_remenberMe))) {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			if(StringUtils.isNotBlank(username) && StringUtils.isNotBlank("response")) {
				MyCookieManager mcm = new MyCookieManager(request, response);
				mcm.setupLoginCookie(username, password);
			}
			
		}
		
		chain.doFilter(request,response);
	}


}

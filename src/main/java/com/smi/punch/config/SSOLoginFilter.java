package com.smi.punch.config;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.smi.punch.utils.SecurityURL;

public class SSOLoginFilter extends AbstractAuthenticationProcessingFilter {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "k";
	public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";
	private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;
	private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;
	private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =new AntPathRequestMatcher("/sso", "GET");

	public SSOLoginFilter() {
		super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
	}
	public SSOLoginFilter(AuthenticationManager authenticationManager) {
		super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
		setAuthenticationManager(authenticationManager);

	}
	@Nullable
	protected String obtainPassword(HttpServletRequest request) {
		return request.getParameter(passwordParameter);
	}
	@Nullable
	protected String obtainUsername(HttpServletRequest request) {
		return request.getParameter(usernameParameter);
	}
	protected void setDetails(HttpServletRequest request,
			UsernamePasswordAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		String decKey = "";
		try {
			String k = obtainUsername(request);
			logger.info("k:{}",StringUtils.replaceChars(k, "[\r\n]", ""));
			decKey = SecurityURL.Base64URLDecode(k);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		logger.info("decKey:{}",decKey);
		
		

		setDetails(request, authRequest);

		return this.getAuthenticationManager().authenticate(authRequest);
	}

}

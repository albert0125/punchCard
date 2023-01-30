package com.smi.punch.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.kerberos.authentication.KerberosAuthenticationProvider;
import org.springframework.security.kerberos.authentication.KerberosServiceAuthenticationProvider;
import org.springframework.security.kerberos.authentication.sun.SunJaasKerberosClient;
import org.springframework.security.kerberos.authentication.sun.SunJaasKerberosTicketValidator;
import org.springframework.security.kerberos.web.authentication.SpnegoAuthenticationProcessingFilter;
import org.springframework.security.kerberos.web.authentication.SpnegoEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.smi.punch.IContent;
import com.smi.punch.service.AuthorizeServiceClient;
import com.smi.punch.service.DummyUserDetailsService;
import com.smi.punch.service.SMIADAuthenticationProvider;
import com.smi.punch.service.SMIADAuthenticationService;

@Configuration
public class MyWebSecurityConfig extends WebSecurityConfigurerAdapter {
	 Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${myserver.krb.keytab.path}")
	private String keytabPath;
	@Value("${myserver.krb.spn}")
	private String spn;
	
	@Autowired
	AuthorizeServiceClient authServiceClient;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
	    http.cors().and().csrf().disable();
	    http
	    	.exceptionHandling().authenticationEntryPoint(spnegoEntryPoint())
	    	.and()
			.authorizeRequests()
	        .antMatchers("/login_page.html", "/logout_page.html", "/perform_login.html","/sso.html", "/perform_logout.html","/assets/**","/images/**").permitAll()
	        .anyRequest().authenticated()
	        .and()
	        .formLogin()
	            .loginPage("/login_page.html")
	            .loginProcessingUrl("/perform_login.html")
	            .usernameParameter("username")
	            .passwordParameter("password")
	            .failureUrl("/login_page.html?error")
	            .defaultSuccessUrl("/index.html", true)
	        .and()	        
	        .logout()
	            .logoutUrl("/perform_logout.html")
	            .logoutSuccessUrl("/login_page.html?logout")
	        .and()
	        .addFilterBefore(new MyRememberMeFilter(), UsernamePasswordAuthenticationFilter.class)
	        .addFilterBefore(mobileSSOLoginFilter() , BasicAuthenticationFilter.class)
	        .addFilterBefore(spnegoAuthenticationProcessingFilter(), BasicAuthenticationFilter.class);
	            
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.authenticationProvider(kerberosAuthenticationProvider())
			.authenticationProvider(kerberosServiceAuthenticationProvider())
			.authenticationProvider(smiADAuthenticationProvider());

	}
	
	@Bean
    public SpnegoEntryPoint spnegoEntryPoint() {
        return new SpnegoEntryPoint("/login_page.html");
		
    }

	@Bean
	public MobileSSOLoginFilter mobileSSOLoginFilter() {
		MobileSSOLoginFilter filter = new MobileSSOLoginFilter();
		try {
			AuthenticationManager authenticationManager = new SMIADAuthenticationService();
			filter.setAuthenticationManager(authenticationManager);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filter;
	}
	
	@Bean
	public SpnegoAuthenticationProcessingFilter spnegoAuthenticationProcessingFilter() {		
		SpnegoAuthenticationProcessingFilter filter = new SpnegoAuthenticationProcessingFilter();
		try {
			AuthenticationManager authenticationManager = authenticationManagerBean();			
			filter.setAuthenticationManager(authenticationManager);
			filter.setFailureHandler(new AuthenticationFailureHandler() {
				Logger logger = LoggerFactory.getLogger(this.getClass());
				@Override
				public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
						AuthenticationException exception) throws IOException, ServletException {
					String contextPath = request.getContextPath();
					logger.info("login page:{}",contextPath+"/login_page.html");
					response.sendRedirect(contextPath+"/login_page.html");
				}});
			
			filter.setSuccessHandler(new AuthenticationSuccessHandler() {

				@Override
				public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
						Authentication authentication) throws IOException, ServletException {
					request.getSession().setAttribute(IContent._login_type, "KERBEROS");
					
				}});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filter;
	}

	@Bean
	public SMIADAuthenticationProvider smiADAuthenticationProvider() {
		logger.info("==============[smiADAuthenticationProvider]");
		SMIADAuthenticationProvider provider = new SMIADAuthenticationProvider();
		provider.setAuthorizeServiceClient(authServiceClient);
		provider.setUserDetailsService(dummyUserDetailsService());
		return provider;
	}

	@Bean
	public KerberosAuthenticationProvider kerberosAuthenticationProvider() {
		logger.info("==============[kerberosAuthenticationProvider]");
		KerberosAuthenticationProvider provider = new KerberosAuthenticationProvider();
		SunJaasKerberosClient client = new SunJaasKerberosClient();
		client.setDebug(true);
		provider.setKerberosClient(client);
		provider.setUserDetailsService(dummyUserDetailsService());
		
		
		return provider;
	}

	@Bean
	public KerberosServiceAuthenticationProvider kerberosServiceAuthenticationProvider() {
		logger.info("==============[kerberosServiceAuthenticationProvider]");
		KerberosServiceAuthenticationProvider provider = new KerberosServiceAuthenticationProvider();
		provider.setTicketValidator(sunJaasKerberosTicketValidator());
		provider.setUserDetailsService(dummyUserDetailsService());
		
		return provider;
	}

	@Bean
	public SunJaasKerberosTicketValidator sunJaasKerberosTicketValidator() {

		logger.info("==============[sunJaasKerberosTicketValidator]");
		SunJaasKerberosTicketValidator ticketValidator = new SunJaasKerberosTicketValidator();
		ticketValidator.setServicePrincipal(spn);
		ticketValidator.setKeyTabLocation(new FileSystemResource(keytabPath));
		
		ticketValidator.setDebug(true);
		return ticketValidator;
	}

	
	@Bean
	public DummyUserDetailsService dummyUserDetailsService() {
		return new DummyUserDetailsService();
	}

}

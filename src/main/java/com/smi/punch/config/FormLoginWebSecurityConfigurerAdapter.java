package com.smi.punch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.kerberos.authentication.KerberosAuthenticationProvider;
import org.springframework.security.kerberos.authentication.KerberosServiceAuthenticationProvider;
import org.springframework.security.kerberos.authentication.sun.SunJaasKerberosClient;
import org.springframework.security.kerberos.authentication.sun.SunJaasKerberosTicketValidator;
import org.springframework.security.kerberos.web.authentication.SpnegoAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.smi.punch.service.AuthorizeServiceClient;
import com.smi.punch.service.DummyUserDetailsService;
import com.smi.punch.service.SMIADAuthenticationProvider;
import com.smi.punch.service.SMIADAuthenticationService;


public class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	AuthorizeServiceClient authServiceClient;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(smiADAuthenticationProvider())
				.authenticationProvider(kerberosAuthenticationProvider())
				.authenticationProvider(kerberosServiceAuthenticationProvider());

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable();
		http.authorizeRequests()
				.antMatchers("/login_page.html", "/logout_page.html", "/perform_login.html", "/sso.html",
						"/perform_logout.html", "/assets/**", "/images/**")
				.permitAll().antMatchers("/**").authenticated().and()
				.addFilterBefore(new MyRememberMeFilter(), UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(mobileSSOLoginFilter(), BasicAuthenticationFilter.class)
				.addFilterBefore(spnegoAuthenticationProcessingFilter(), BasicAuthenticationFilter.class).formLogin()
				.loginPage("/login_page.html").loginProcessingUrl("/perform_login.html").usernameParameter("username")
				.passwordParameter("password").failureUrl("/login_page.html?error")
				.defaultSuccessUrl("/index.html", true).and()
				.logout().logoutUrl("/perform_logout.html").logoutSuccessUrl("/login_page.html?logout")
				.and().anonymous().disable()
		        .exceptionHandling()
				.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

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
	public SMIADAuthenticationProvider smiADAuthenticationProvider() {
		logger.info("==============[smiADAuthenticationProvider]");
		SMIADAuthenticationProvider provider = new SMIADAuthenticationProvider();
		provider.setAuthorizeServiceClient(authServiceClient);
		provider.setUserDetailsService(new DummyUserDetailsService());
		return provider;
	}

	@Bean
	public KerberosAuthenticationProvider kerberosAuthenticationProvider() {
		KerberosAuthenticationProvider provider = new KerberosAuthenticationProvider();
		SunJaasKerberosClient client = new SunJaasKerberosClient();
		client.setDebug(true);
		provider.setKerberosClient(client);
		provider.setUserDetailsService(new DummyUserDetailsService());
		return provider;
	}

	@Bean
	public SpnegoAuthenticationProcessingFilter spnegoAuthenticationProcessingFilter() {
		logger.info("==============[spnegoAuthenticationProcessingFilter]");
		SpnegoAuthenticationProcessingFilter filter = new SpnegoAuthenticationProcessingFilter();
		try {
			AuthenticationManager authenticationManager = authenticationManagerBean();
			logger.info("==============[authenticationManager]={}", authenticationManager.getClass().getName());
			filter.setAuthenticationManager(authenticationManager);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filter;
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

		ticketValidator.setDebug(true);
		return ticketValidator;
	}

	@Bean
	public DummyUserDetailsService dummyUserDetailsService() {
		return new DummyUserDetailsService();
	}
}
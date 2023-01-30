package com.smi.punch.config;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.GzipResourceResolver;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.VersionResourceResolver;


public class MvcConfig implements WebMvcConfigurer {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("assets/**").addResourceLocations("classpath:static/assets/")
				.setCacheControl(CacheControl.maxAge(5, TimeUnit.DAYS)).resourceChain(true)
				.addResolver(new GzipResourceResolver()).addResolver(new PathResourceResolver());
		registry.addResourceHandler("images/**").addResourceLocations("classpath:static/images/")
				.setCacheControl(CacheControl.maxAge(5, TimeUnit.DAYS)).resourceChain(true)
				.addResolver(new GzipResourceResolver()).addResolver(new PathResourceResolver());
		;
	}

}
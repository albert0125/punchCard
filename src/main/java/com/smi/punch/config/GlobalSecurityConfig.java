package com.smi.punch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.kerberos.authentication.sun.GlobalSunJaasKerberosConfig;

@Configuration
public class GlobalSecurityConfig {
	@Value("${myserver.krb.config}")
	private String config;
	@Bean
	public GlobalSunJaasKerberosConfig globalSunJaasKerberosConfig() {
		GlobalSunJaasKerberosConfig globalConfig = new GlobalSunJaasKerberosConfig();
		globalConfig.setDebug(true);
		globalConfig.setKrbConfLocation(config);
		return globalConfig;
	}
}

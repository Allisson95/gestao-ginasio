package br.com.gestaoginasio.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SpringWebSecurityInitializer extends AbstractSecurityWebApplicationInitializer {

	public SpringWebSecurityInitializer() {
		super(SecurityConfig.class);
	}

}

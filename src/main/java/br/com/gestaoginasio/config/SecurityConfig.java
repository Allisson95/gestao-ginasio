package br.com.gestaoginasio.config;

import javax.enterprise.inject.spi.CDI;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.gestaoginasio.auth.DetalhesUsuarioService;
import br.com.gestaoginasio.model.TipoPermissao;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private DetalhesUsuarioService detalhesUsuarioService;
	
	public SecurityConfig() {
		this.detalhesUsuarioService = CDI.current().select(DetalhesUsuarioService.class).get();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.exceptionHandling()
				.accessDeniedPage("/ErrorPages/403.jsf")
			.and()
			.authorizeRequests()
				.antMatchers("/javax.faces.resource/**").permitAll()
				.antMatchers("/Pages/Administrador/**").hasAuthority(TipoPermissao.ADMIN.getDescricao())
				.antMatchers("/Pages/Professor/**").hasAuthority(TipoPermissao.PROFESSOR.getDescricao())
				.antMatchers("/Pages/Aluno/**").hasAuthority(TipoPermissao.ALUNO.getDescricao())
				.antMatchers("/Pages/Home.jsf").hasAnyAuthority(TipoPermissao.ADMIN.getDescricao(), TipoPermissao.PROFESSOR.getDescricao(), TipoPermissao.ALUNO.getDescricao())
				.antMatchers("/Pages/Perfil.jsf").hasAnyAuthority(TipoPermissao.ADMIN.getDescricao(), TipoPermissao.PROFESSOR.getDescricao(), TipoPermissao.ALUNO.getDescricao())
				.antMatchers("/ErrorPages/**").permitAll()
				.anyRequest()
				.authenticated()
			.and()
				.formLogin()
					.loginPage("/Login.jsf")
						.successForwardUrl("/Pages/Home.jsf")
						.failureUrl("/Login.jsf?error=true")
						.permitAll()
			.and()
				.logout()
					.logoutSuccessUrl("/Login.jsf?logout=true")
					.permitAll()
			.and()
				.rememberMe()
					.rememberMeParameter("remember_input")
			.and()
				.csrf().disable();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(this.detalhesUsuarioService).passwordEncoder(getPasswordEncoder());
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**");
	}
	
	private PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}

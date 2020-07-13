package com.viniciusmello.springrest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.viniciusmello.springrest.service.ImplementacaoUserDetailsService;

// 			AUTORIZA OU BLOQUEIA ACESSOS A URL

@Configuration @EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private ImplementacaoUserDetailsService userDetailsService;
	
//			CONFIGURA AS SOLICITAÇÕES DE ACESSO POR HTTP
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		 
// 			ATIVANDO A PROTEÇÃO CONTRA USUÁRIOS QUE NÃO ESTÃO VALIDADOS POR TOKEN
		 
		 http
		 	.csrf()
		 	.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		 
//			DESABILITANDO AS CONFIGURAÇÕES PADRÃO E ATIVANDO A NOVA RESTRIÇÃO URL

		 	.disable()
		 	.authorizeRequests()
		 	.antMatchers("/")
		 	.permitAll()
		 	.antMatchers("/index.html")
		 	.permitAll()
		 
//			URL DE LOGOUT - REDIRECIONA APÓS O USER DESLOGAR DO SISTEMA.
		 
		 	.anyRequest()
		 	.authenticated()
		 	.and()
		 	.logout()
		 	.logoutSuccessUrl("/index")
		 	
//			MAPEIA URL DE LOGOUT E INVALIDA O USUARIO
		 	
		 	.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		 
// 			FILTRA QUE CONFIGURA AUTHENTICATIONMANAGER, URL, VALIDA ACESSO DE USUARIO E GERA TOKEN.
		 
		 	.and()
		 	.addFilterBefore(new JWTLoginFilter("/login", authenticationManager()),
		 			UsernamePasswordAuthenticationFilter.class)
		 
//			FILTRO QUE VERIFICA SE TOKEN PASSADO NA REQUISIÇÃO É VALIDO
		 
		 	.addFilterBefore(new JWTApiAutenticacaoFilter(), UsernamePasswordAuthenticationFilter.class);
		 
	 }
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

// 			VAI CONSULTAR O NOSSO USUARIO NO BANCO DE DADOS

		auth
			.userDetailsService(userDetailsService)
			.passwordEncoder(new BCryptPasswordEncoder()); //Define a nossa criptografia
		
	}
	
}

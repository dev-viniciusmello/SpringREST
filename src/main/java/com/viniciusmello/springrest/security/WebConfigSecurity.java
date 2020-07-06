package com.viniciusmello.springrest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.viniciusmello.springrest.service.ImplementacaoUserDetailsService;

// URL, ENDERECO, AUTORIZA OU BLOQUEIA ACESSOS A URL
// VAI FAZER AS CONFIGURAÇÕES QUE INDICARMOS
@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private ImplementacaoUserDetailsService userDetailsService;
	
	// Configura as solicitações de acesso por HTTP
	 @Override
	protected void configure(HttpSecurity http) throws Exception {
		 // Ativando a proteção contra usuarios que não estão validados por Token
		 http
		 	.csrf()
		 	.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		 
		 //Desabilitando configurações padrão e ativando a restrição da URL
		 	//.antMatchers("/").permitAll(). -> Permitindo todos com o /
		 	.disable().authorizeRequests().antMatchers("/").permitAll().antMatchers("/index.html").permitAll()
		 
		 //URL de logout - Redireciona após o user deslogar do sistema.
		 .anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")
		 
		 //Mapeia URL de logout e invalida o usuário
		 .logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
		 
		 // Filtra requisições de login para autenticação
		 
		 // Filtra as demais requisições para verificar TOKEN JWT  NO HEADER HTTp
		 
		 
		 
		 
		 
		 ;
		 
	 }
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Ira consultar usuario no banco de dados
		auth
			.userDetailsService(userDetailsService)
			.passwordEncoder(new BCryptPasswordEncoder()); //Define a nossa criptografia
		
	}
	
	
}

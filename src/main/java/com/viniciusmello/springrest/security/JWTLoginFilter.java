package com.viniciusmello.springrest.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viniciusmello.springrest.model.Usuario;

// 		ESTABELECE O NOSSO GERENCIADOR DE TOKEN

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

	protected JWTLoginFilter(String url, AuthenticationManager authManager) {

//		CONFIGURA O CONSTRUTOR DE ABSTRACT AUTHENTICATION PROCESSING FILTER
//		OBRIGA A AUTENTICAR A URL

		super(new AntPathRequestMatcher(url));

//		GERENCIADOR DE AUTENTICAÇÃO

		setAuthenticationManager(authManager);

	}

//		RETORNA O USUÁRIO AO PROCESSAR A AUTENTICAÇÃO

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {

//		ESTA PEGANDO O TOKEN PARA VALIDAR

		ObjectMapper objectMapper = new ObjectMapper();

		Usuario usuario = objectMapper.readValue(request.getInputStream(), Usuario.class);

		return getAuthenticationManager()
				.authenticate(new UsernamePasswordAuthenticationToken(usuario.getLogin(), usuario.getSenha()));
	}

	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
			
			try {
			
				JWTTokenAutenticacaoService.addAuthentication(response, authResult.getName());
		
			} 
			
			catch (Exception e) {
	
				e.printStackTrace();
			
			}
	
	}

}

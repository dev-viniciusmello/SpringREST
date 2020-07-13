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

//		CONFIGURA URL E AUTHENTICATIONMANAGER
	
	protected JWTLoginFilter(String url, AuthenticationManager authManager) {

//		CONFIGURA O CONSTRUTOR DE ABSTRACT AUTHENTICATION PROCESSING FILTER
		
//		OBRIGA A AUTENTICAR A URL

		super(new AntPathRequestMatcher(url));

//		GERENCIADOR DE AUTENTICAÇÃO. É POR AQUI QUE ELE VAI VERIFICAR SE O NOSSO USUARIO ESTÁ CADASTRADO OU NÃO.

		setAuthenticationManager(authManager);

	}

//		VALIDA USUARIO PASSADO NO LOGIN 

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {

//		OBJECT MAPPER É UTILIZADO PARA LER E ESCREVER JSON

		ObjectMapper objectMapper = new ObjectMapper();

//		AQUI NÓS ESTAMOS PEGANDO E RETORNANDO UM USUARIO DO STREAM DA REQUISIÇÃO
		
		Usuario usuario = objectMapper.readValue(request.getInputStream(), Usuario.class);

		return getAuthenticationManager()
				.authenticate(new UsernamePasswordAuthenticationToken(usuario.getLogin(), usuario.getSenha()));
	}

//		GERA TOKEN E DA UMA REPOSTA
	
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

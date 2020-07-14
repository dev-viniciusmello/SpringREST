package com.viniciusmello.springrest.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

//			FILTRO ONDE TODAS AS REQUISIÇÕES SERÃO CAPTURADAS PRA AUTENTICAR

public class JWTApiAutenticacaoFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

//			ESTABELECE A AUTENTICAÇÃO DA REQUISIÇÃO
		
		Authentication authentication = 
				JWTTokenAutenticacaoService.getAuthentication((HttpServletRequest) request, (HttpServletResponse) response);
		
//			COLOCA O PROCESSO DE AUTENTICAÇÃO NO SPRING SECURITY
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
//			PROCESSA A REQUISIÇÃO
		
		chain.doFilter(request, response);
		
	}

	
}

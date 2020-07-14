package com.viniciusmello.springrest.security;

import java.io.IOException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.viniciusmello.springrest.ApplicationContextLoad;
import com.viniciusmello.springrest.model.Usuario;
import com.viniciusmello.springrest.repository.UsuarioRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JWTTokenAutenticacaoService {
	
	private static final long EXPIRATION_TIME = 172800000l;

	private static final String SECRET = "SenhaExtremamenteSecreta";

	private static final String TOKEN_PREFIX = "Bearer";

	private static final String HEADER_STRING = "Authorization";

	public static void addAuthentication(HttpServletResponse response, String username) throws Exception { // GERA TOKEN DE
																									// AUTENTICAÇÃO
		String JWT = 
				
				Jwts
					.builder()
					.setSubject(username)
					.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
					.signWith(SignatureAlgorithm.HS512, SECRET)
					.compact();

		String token = TOKEN_PREFIX + " " + JWT;

		response.addHeader(HEADER_STRING, token);

		inserirTokenBanco(username, JWT);
		
		liberacaoCors(response);
		
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
	}

	
	public static Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
		
		String token = request.getHeader(HEADER_STRING);
		
		try {
			
			if (isStringValida(token)) {
				String user = getUserLoginByToken(token);
				
				Usuario usuario = getUsuarioByContextLogin(user, token.replace(TOKEN_PREFIX, "").trim());
							
				return usuario != null ? new UsernamePasswordAuthenticationToken(usuario.getLogin(), usuario.getSenha(),
						usuario.getAuthorities()) : null; 
			}
		} 
		catch (ExpiredJwtException e) {
			try {
				response.getOutputStream().println("Seu token esta expirado faça o login ou informe um novo token para a autenticacao");
			}
			catch (IOException e1) {

				e1.printStackTrace();
			}
		}
		
		liberacaoCors(response);
		return null;
	}

	
	private static void liberacaoCors(HttpServletResponse response) {
		
		if (response.getHeader("Access-Control-Allow-Origin") == null) response.addHeader("Access-Control-Allow-Origin", "*");
		
		if (response.getHeader("Access-Control-Allow-Headers") == null) response.addHeader("Access-Control-Allow-Headers", "*");
		
		if (response.getHeader("Access-Control-Request-Headers") == null) response.addHeader("Access-Control-Request-Headers", "*");
		
		if (response.getHeader("Access-Control-Allow-Methods") == null) response.addHeader("Access-Control-Allow-Methods", "*");
		
	}


	public static void inserirTokenBanco(String username, String jwt) {
		
		Usuario usuario = usuarioRepository().findUsuarioByLogin(username);
		
		usuario.setToken(jwt);
		
		usuarioRepository().save(usuario);
		
	}
	
	private static  UsuarioRepository usuarioRepository() {
		
		return ApplicationContextLoad
				.getApplicationContext()
				.getBean(UsuarioRepository.class);
		
	}

	private static boolean isStringValida(String string) {
		if (string == null || string.isEmpty()) {
			return false;
		}
		return true;
	}

	
	private static String getUserLoginByToken(String token) {
		
		String user = 
				
				Jwts
					.parser()
					.setSigningKey(SECRET)
					.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
					.getBody()
					.getSubject();

		return user;
	}
	
	

	private static Usuario getUsuarioByContextLogin(String user, String token) {
		
		if (isStringValida(user)) {
			
			Usuario usuario = usuarioRepository().findUsuarioByLogin(user);
			
			if (usuario != null) 
				if (token.equals(usuario.getToken())) 
					return usuario;

		}
		
		return null;
	}

	

}

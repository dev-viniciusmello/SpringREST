package com.viniciusmello.springrest.security;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.viniciusmello.springrest.ApplicationContextLoad;
import com.viniciusmello.springrest.model.Usuario;
import com.viniciusmello.springrest.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

//			CLASSE UTILIZADA TANTO PARA AUTENTICAR TOKENS QUANTO VALIDAR TOKENS VINDOS DE UMA REQUISIÇÃO

@Service
public class JWTTokenAutenticacaoService {
	
//			ATRIBUTO EXPIRATION_TIME EQUIVALE A 2 DIAS
	
	private static final long EXPIRATION_TIME = 172800000l;

	private static final String SECRET = "SenhaExtremamenteSecreta";

	private static final String TOKEN_PREFIX = "Bearer";

	private static final String HEADER_STRING = "Authorization";

	public static void addAuthentication(HttpServletResponse response, String username) throws Exception { // GERA TOKEN DE
																									// AUTENTICAÇÃO
		String JWT = 
				
//				DEFININDO O NOME DO USUARIO NO TOKEN
				
				Jwts
				
//				RETORNA UMA INSTÂNCIA DO OBJETO DEFAULTJWTBUILDER PRA EU PODER CONFIGURAR DEPOIS
					
					.builder()
					
//				DEFINE O NOME DE USUARIO DO MEU TOKEN 
					
					.setSubject(username)
				
//				DEFININFO PRAZO DE VALIDADE NO TOKEN
				
					.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				
//				DEFINE ALGORITMO DE CODIFICAÇÃO JUNTO COM A SENHA SECRETA
				
					.signWith(SignatureAlgorithm.HS512, SECRET)
				
//				ESSE MÉTODO CRIA A STRING EM JWT DE ACORDO COM TODAS AS CONFIGURAÇÕES QUE FORAM FEITAS
				
					.compact();

//				ADICIONA A PALAVRA BEARER QUE SIGINIFICA PORTADOR NO TOKEN
		
		String token = TOKEN_PREFIX + " " + JWT;

//				DEFINE O CABEÇALHO NA RESPOSTA COM O NOME AUTHORIZATION CONTENDO O TOKEN GERADO
		
		response.addHeader(HEADER_STRING, token);

		
//				INSERE TOKEN NO BANCO DE DADOS
		
		inserirTokenBanco(username, JWT);
		
//				ENVIA UMA RESPOSTA PADRÃO NO CABEÇALHO COM O JSON { AUTHORIZATION : BEARER TOKEN.TOKEN.TOKEN }
		
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
	}


//			VALIDA USUARIO DO TOKEN PASSADO NA REQUISIÇÃO
	
	public static Authentication getAuthentication(HttpServletRequest request) {
		
		String token = request.getHeader(HEADER_STRING);
		
		if (isStringValida(token)) {
			
			String user = getUserLoginByToken(token);
			
			Usuario usuario = getUsuarioByContextLogin(user, token.replace(TOKEN_PREFIX, ""));
			
//			USUARIO AUTENTICADO
			
			return usuario != null ? new UsernamePasswordAuthenticationToken(usuario.getLogin(), usuario.getSenha(),
					usuario.getAuthorities()) : null; 
			
		}
		
//			USUARIO NÃO AUTENTICADO			
		
		return null;
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

	
//			FAZ A VALIDAÇÃOD O TOKEN DO USUARIO NA REQUISIÇÃO
	
	private static String getUserLoginByToken(String token) {
		
//			USER É O NOME DO USUARIO
		
		String user = 

//			CRIA UMA INSTÂNCIA DE DEFAULTJWTPARSER
				
				Jwts.parser()

//			CONFIGURA A NOSSA CHAVE DE ASSINATURA
					.setSigningKey(SECRET)
					
//			DECODIFICA O NOSSO JWTT
					
					.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
					
//			OBTEM O NOME DO USUARIO QUE VEIO NA REQUISIÇÃO
					
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

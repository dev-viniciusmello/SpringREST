package com.viniciusmello.springrest.model;

import java.io.Serializable;

//			SERVE PARA ESCONDER OS ARTRIBUTOS PARA QUESTÃO DE SEGURANCA

public class UsuarioDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String userNome;
	private String userLogin;
	private String userCpf;
	
	public UsuarioDTO(Usuario usuario) {
		
		this.userNome = usuario.getNome();
		this.userLogin = usuario.getLogin();
		this.userCpf = usuario.getToken();
		
	}

	public String getUserNome() {
		return userNome;
	}

	public void setUserNome(String userNome) {
		this.userNome = userNome;
	}



	public String getUserLogin() {
		return userLogin;
	}



	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}



	public String getUserCpf() {
		return userCpf;
	}



	public void setUserCpf(String userCpf) {
		this.userCpf = userCpf;
	}
	
	

}

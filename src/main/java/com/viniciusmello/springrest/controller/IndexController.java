package com.viniciusmello.springrest.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viniciusmello.springrest.model.Usuario;

@RestController 
@RequestMapping(value = "/usuario")
public class IndexController {

	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<List<Usuario>>init() {

		List<Usuario> usuarios = new ArrayList<Usuario>();
		
		for (Long i = 0l; i < 30 ; i++) {
			Usuario usuario = new Usuario();
			usuario.setId(i);
			usuario.setLogin("dev.viniciusmello@gmail.com");
			usuario.setNome("Marcus Vinicius");
			usuario.setSenha("1121212323232");
			usuarios.add(usuario);
		}
		
		return new ResponseEntity<List<Usuario>>(usuarios, HttpStatus.OK);
	
	}
	
	
}

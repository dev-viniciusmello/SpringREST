package com.viniciusmello.springrest.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viniciusmello.springrest.model.Usuario;

@RestController 
@RequestMapping(value = "/usuario")
public class IndexController {

	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> init() {
		Usuario usuario = new Usuario();
		usuario.setLogin("dev.viniciusmello@gmail.com");
		usuario.setNome("Marcus Vinicius");
		usuario.setSenha("1121212323232");		
		return ResponseEntity.ok(usuario);
	
	}
	
	
}

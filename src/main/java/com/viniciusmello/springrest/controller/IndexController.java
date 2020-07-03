package com.viniciusmello.springrest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viniciusmello.springrest.model.Usuario;
import com.viniciusmello.springrest.repository.UsuarioRepository;

@RestController 
@RequestMapping(value = "/usuario")
public class IndexController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@GetMapping(value = "/{id}/codigodavenda/{venda}", produces = "application/json")
	public ResponseEntity<Usuario> relatorio(@PathVariable("id") Long id, 
			@PathVariable(value = "venda") Long venda) {
		System.out.println(id);
		System.out.println(venda);
		return new ResponseEntity<Usuario>(usuarioRepository.findById(id).get(), HttpStatus.OK);	
	}
	
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<List<Usuario>> usuarios() {
		return new ResponseEntity<List<Usuario>>((List<Usuario>) usuarioRepository.findAll(), HttpStatus.OK);
	}
	
}

package com.viniciusmello.springrest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viniciusmello.springrest.model.Usuario;
import com.viniciusmello.springrest.repository.UsuarioRepository;

@RestController 
@RequestMapping(value = "/usuario")
public class IndexController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Usuario> init(@PathVariable("id") Long id) {
		Usuario usuario = usuarioRepository.findById(id).get();
		usuario.getTelefones().forEach(t -> System.out.println(t));
		return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
		
	}
	
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<List<Usuario>> usuarios() {
		return new ResponseEntity<List<Usuario>>((List<Usuario>) usuarioRepository.findAll(), HttpStatus.OK);
	}
	
	
	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> cadastrarVenda(@RequestBody Usuario usuario) {
		System.out.println("Entrou");
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK); 
	}
	
	
	@PutMapping(value = "/") // Atualizar
	public ResponseEntity<Usuario> atualizar(@RequestBody Usuario usuario) {
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK); 
		
	}
	
	@DeleteMapping(value = "/{id}")
	public String delete(@PathVariable("id") Long id) {
		usuarioRepository.deleteById(id);
		return "ok";
	}
	
	
	
	
}

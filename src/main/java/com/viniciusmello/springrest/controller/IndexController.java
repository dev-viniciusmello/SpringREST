package com.viniciusmello.springrest.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.viniciusmello.springrest.model.Telefone;
import com.viniciusmello.springrest.model.Usuario;
import com.viniciusmello.springrest.repository.UsuarioRepository;

@CrossOrigin(origins = "*") 
@RestController 
public class IndexController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Usuario> init(@PathVariable("id") Long id) {
		Usuario usuario = usuarioRepository.findById(id).get();
		return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
	}
	
	@GetMapping(value = "/", produces = "application/json", headers = "X-API-VERSION=v1")
	public ResponseEntity<List<Usuario>> usuarios() {
		return new ResponseEntity<List<Usuario>>((List<Usuario>) usuarioRepository.findAll(), HttpStatus.OK);
	}
	
	
	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) {
		
		associaTelefonesPessoa(usuario);
		usuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		return new ResponseEntity<Usuario>(usuarioSalvo , HttpStatus.OK); 
	}
	
	
	@PutMapping(value = "/") 
	public ResponseEntity<Usuario> atualizar(@RequestBody Usuario usuario) {
		
		associaTelefonesPessoa(usuario);
		
		Usuario aux = usuarioRepository.findById(usuario.getId()).get();
		
		if (!aux.getSenha().equals(usuario.getSenha()) && aux != null) {
			
			usuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));
		
		}
		
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		
		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK); 
		
	}
	
	@DeleteMapping(value = "/{id}")
	public String delete(@PathVariable("id") Long id) {
		usuarioRepository.deleteById(id);
		return "ok";
	}
	
	private void associaTelefonesPessoa(Usuario usuario) {
		for (Telefone t : usuario.getTelefones()) {
			t.setUsuario(usuario);
		}
	}
}

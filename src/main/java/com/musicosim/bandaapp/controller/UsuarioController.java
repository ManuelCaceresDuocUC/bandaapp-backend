package com.musicosim.bandaapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musicosim.bandaapp.dto.LoginRequest;
import com.musicosim.bandaapp.model.Usuario;
import com.musicosim.bandaapp.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    Optional<Usuario> optionalUsuario = usuarioRepository.findByNPI(loginRequest.getNpi());

    if (optionalUsuario.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
    }

    Usuario usuario = optionalUsuario.get();

    if (!usuario.getContrasena().equals(loginRequest.getPassword())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta");
    }

    return ResponseEntity.ok(usuario);
}
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        return usuarioRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/buscar-por-npi/{npi}")
    public ResponseEntity<Usuario> buscarPorNpi(@PathVariable String npi) {
        return usuarioRepository.findByNPI(npi)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}

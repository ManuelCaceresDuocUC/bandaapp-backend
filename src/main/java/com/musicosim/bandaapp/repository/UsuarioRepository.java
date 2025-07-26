package com.musicosim.bandaapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.musicosim.bandaapp.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEmail(String email);
}


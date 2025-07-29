package com.musicosim.bandaapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.musicosim.bandaapp.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEmail(String email);
        Optional<Usuario> findByNpi(String npi);

}

package com.musicosim.bandaapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.musicosim.bandaapp.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
Optional<Usuario> findByEmail(String email);
    
        Optional<Usuario> findByNpi(String npi);
        List<Usuario> findByBandaIdAndIdNotIn(Long bandaId, List<Long> ids);
        List<Usuario> findByBandaId(Long bandaId);



}

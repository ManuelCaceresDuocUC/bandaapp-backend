package com.musicosim.bandaapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.musicosim.bandaapp.model.EstadoVigente;

public interface EstadoVigenteRepository extends JpaRepository<EstadoVigente, Long> {

    Optional<EstadoVigente> findByUsuarioId(Long usuarioId);

    void deleteByUsuarioId(Long usuarioId);
}

package com.musicosim.bandaapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.musicosim.bandaapp.model.Actividad;

public interface ActividadRepository extends JpaRepository<Actividad, Long> {
    List<Actividad> findByBandaId(Long bandaId);
}

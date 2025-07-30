package com.musicosim.bandaapp.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.musicosim.bandaapp.model.Asistencia;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    List<Asistencia> findByUsuarioId(Long usuarioId);
    List<Asistencia> findByFechaAndUsuario_Banda_Id(LocalDate fecha, Long bandaId);
    
    
}

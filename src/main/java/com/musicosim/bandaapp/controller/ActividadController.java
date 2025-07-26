package com.musicosim.bandaapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.musicosim.bandaapp.model.Actividad;
import com.musicosim.bandaapp.repository.ActividadRepository;

@RestController
@RequestMapping("/api/actividades")
public class ActividadController {

    @Autowired
    private ActividadRepository actividadRepository;

    @GetMapping
    public List<Actividad> listar(@RequestParam(required = false) Long bandaId) {
        if (bandaId != null) {
            return actividadRepository.findByBandaId(bandaId);
        }
        return actividadRepository.findAll();
    }

    @PostMapping
    public Actividad crear(@RequestBody Actividad actividad) {
        return actividadRepository.save(actividad);
    }
   @DeleteMapping("/{id}")
    public void eliminarActividad(@PathVariable Long id) {
        actividadRepository.deleteById(id);
    }
}

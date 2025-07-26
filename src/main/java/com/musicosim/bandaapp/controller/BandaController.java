package com.musicosim.bandaapp.controller;

import com.musicosim.bandaapp.model.Banda;
import com.musicosim.bandaapp.repository.BandaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bandas")
public class BandaController {

    @Autowired
    private BandaRepository bandaRepository;

    @GetMapping
    public List<Banda> listarBandas() {
        return bandaRepository.findAll();
    }

    @PostMapping
    public Banda crearBanda(@RequestBody Banda banda) {
        return bandaRepository.save(banda);
    }
}

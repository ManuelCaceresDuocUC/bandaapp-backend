package com.musicosim.bandaapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.musicosim.bandaapp.model.Banda;

public interface BandaRepository extends JpaRepository<Banda, Long> {
}

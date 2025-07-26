package com.musicosim.bandaapp.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "asistencias")
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @ManyToOne
@JoinColumn(name = "usuario_id")
@JsonIgnoreProperties("asistencias") // Previene bucles
private Usuario usuario;

    private LocalDate fecha;

    @Column(nullable = false)
    private String estado; // ASISTE, FALTA, LICENCIA, COMISION

    private String observaciones;
    private static final double CUARTEL_LAT = -71.620738;
    private static final double CUARTEL_LNG = -33.055116;
    private static final double RADIO_METROS = 200; // Radio permitido en metros



    // Getters y setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}

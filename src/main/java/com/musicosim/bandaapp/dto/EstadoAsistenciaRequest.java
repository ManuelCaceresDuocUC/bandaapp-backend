package com.musicosim.bandaapp.dto;

public class EstadoAsistenciaRequest {

    private String nuevoEstado;
    private String observaciones; // opcional, si deseas permitir editar también esto

    public String getNuevoEstado() {
        return nuevoEstado;
    }

    public void setNuevoEstado(String nuevoEstado) {
        this.nuevoEstado = nuevoEstado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}

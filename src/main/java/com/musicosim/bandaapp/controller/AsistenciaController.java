package com.musicosim.bandaapp.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.musicosim.bandaapp.dto.RegistroAsistenciaRequest;
import com.musicosim.bandaapp.model.Asistencia;
import com.musicosim.bandaapp.model.EstadoVigente;
import com.musicosim.bandaapp.model.Usuario;
import com.musicosim.bandaapp.repository.AsistenciaRepository;
import com.musicosim.bandaapp.repository.EstadoVigenteRepository;
import com.musicosim.bandaapp.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/asistencias")
public class AsistenciaController {

    @Autowired
    private AsistenciaRepository asistenciaRepository;
    private static final double CUARTEL_LAT = -33.020705;
private static final double CUARTEL_LNG = -71.638160;
private static final List<String> ESTADOS_PERSISTENTES = List.of("PERMISO", "CATEGORIA", "LICENCIA");


    @Autowired
    private UsuarioRepository usuarioRepository;


@PostMapping
public ResponseEntity<?> registrar(@RequestBody Asistencia asistencia) {
    try {
        if (asistencia.getUsuario() == null || asistencia.getUsuario().getId() == null) {
            return ResponseEntity.badRequest().body("Falta el usuario.");
        }
        if (asistencia.getFecha() == null) {
            return ResponseEntity.badRequest().body("Falta la fecha.");
        }

        // Cargar el usuario desde la base de datos para evitar problemas de mapeo
        Usuario usuario = usuarioRepository.findById(asistencia.getUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        asistencia.setUsuario(usuario);

        boolean yaExiste = asistenciaRepository
            .findByUsuarioId(usuario.getId())
            .stream()
            .anyMatch(a -> a.getFecha().equals(asistencia.getFecha()));

        if (yaExiste) {
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Ya existe un registro de asistencia para este usuario en esa fecha.");
        }

        Asistencia nueva = asistenciaRepository.save(asistencia);
        return ResponseEntity.ok(nueva);

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error interno: " + e.getMessage());
    }
}




    @Autowired
private EstadoVigenteRepository estadoVigenteRepository;

@PostMapping("/registrar")
public ResponseEntity<?> registrarAsistencia(@RequestBody RegistroAsistenciaRequest request) {
    double distancia = calcularDistanciaMetros(
        request.getLatitud(), request.getLongitud(),
        CUARTEL_LAT, CUARTEL_LNG
    );

    Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    LocalDate hoy = LocalDate.now();

    boolean yaExisteHoy = asistenciaRepository
        .findByUsuarioId(usuario.getId())
        .stream()
        .anyMatch(a -> a.getFecha().equals(hoy));

    if (yaExisteHoy) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body("Ya existe un registro de asistencia para hoy.");
    }

    String estado = request.getEstado() != null ? request.getEstado().toUpperCase() : null;

    Asistencia nueva = new Asistencia();
    nueva.setUsuario(usuario);
    nueva.setFecha(hoy);
    nueva.setObservaciones(request.getObservaciones());

    // 📌 Caso 1: Registro manual con estado explícito
    if (estado != null) {
        nueva.setEstado(estado);
        asistenciaRepository.save(nueva);

        if (ESTADOS_PERSISTENTES.contains(estado)) {
            EstadoVigente vigente = estadoVigenteRepository.findByUsuarioId(usuario.getId())
                    .orElse(new EstadoVigente());
            vigente.setUsuario(usuario);
            vigente.setEstado(estado);
            vigente.setDesde(hoy);
            vigente.setObservaciones(request.getObservaciones());
            estadoVigenteRepository.save(vigente);
        } else {
            estadoVigenteRepository.deleteByUsuarioId(usuario.getId());
        }

        return ResponseEntity.ok("Asistencia manual registrada correctamente.");
    }

    // 📌 Caso 2: Registro automático con geolocalización
    if (distancia <= 200) {
        nueva.setEstado("A BORDO");
        nueva.setObservaciones("Registrado presencialmente por geolocalización");
        estadoVigenteRepository.deleteByUsuarioId(usuario.getId());
    } else {
        // Sin geolocalización válida → verificar estado persistente
        var vigenteOpt = estadoVigenteRepository.findByUsuarioId(usuario.getId());
        if (vigenteOpt.isPresent()) {
            var vigente = vigenteOpt.get();
            nueva.setEstado(vigente.getEstado());
            nueva.setObservaciones("Estado replicado automáticamente desde el " + vigente.getDesde());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Fuera de rango permitido para registrar asistencia.");
        }
    }

    asistenciaRepository.save(nueva);
    return ResponseEntity.ok("Asistencia registrada correctamente.");
}



    @GetMapping("/usuario/{id}")
    public List<Asistencia> porUsuario(@PathVariable Long id) {
        return asistenciaRepository.findByUsuarioId(id);
    }

    @GetMapping("/asistencia/faltantes")
public ResponseEntity<List<Usuario>> obtenerUsuariosSinAsistencia(
        @RequestParam Long bandaId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

    List<Asistencia> asistencias = asistenciaRepository.findByFechaAndUsuario_Banda_Id(fecha, bandaId);

    List<Long> idsConAsistencia = asistencias.stream()
            .map(a -> a.getUsuario().getId())
            .toList();

    List<Usuario> faltantes;
    if (idsConAsistencia.isEmpty()) {
        // Si nadie ha registrado asistencia, traer todos los usuarios de la banda
        faltantes = usuarioRepository.findByBandaId(bandaId);
    } else {
        faltantes = usuarioRepository.findByBandaIdAndIdNotIn(bandaId, idsConAsistencia);
    }

    return ResponseEntity.ok(faltantes);
}

    @GetMapping
    public List<Asistencia> porFechaYBanda(@RequestParam LocalDate fecha, @RequestParam Long bandaId) {
        return asistenciaRepository.findByFechaAndUsuario_Banda_Id(fecha, bandaId);
    }
    private double calcularDistanciaMetros(double lat1, double lon1, double lat2, double lon2) {
    final int R = 6371000; // Radio de la Tierra en metros

    double lat1Rad = Math.toRadians(lat1);
    double lat2Rad = Math.toRadians(lat2);
    double deltaLat = Math.toRadians(lat2 - lat1);
    double deltaLon = Math.toRadians(lon2 - lon1);

    double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
               Math.cos(lat1Rad) * Math.cos(lat2Rad) *
               Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return R * c;
    
}

}


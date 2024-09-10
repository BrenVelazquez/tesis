package com.sistexperto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sistexperto.dto.PacienteRequest;
import com.sistexperto.dto.PacienteResponse;
import com.sistexperto.model.Paciente;
import com.sistexperto.service.PacienteService;

@RestController
public class PacienteController {
    private final PacienteService pacienteService;

    @Autowired
    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    private static final Logger logger = LoggerFactory.getLogger(PacienteController.class);

    @PostMapping("/diagnosticar")
    public ResponseEntity<Object> getPaciente(@RequestBody PacienteRequest pacienteRequest) {
        try {
            logger.info("Recibiendo solicitud con pacienteRequest: {}", pacienteRequest);
            PacienteResponse pacienteResponse = pacienteService.pacienteResponse(pacienteRequest);
            logger.info("Saliendo de Recibir solicitud");
            return new ResponseEntity<>(pacienteResponse, HttpStatus.OK);
        } catch (Exception ex) {

            logger.error("Error al procesar la solicitud: {}", ex.getMessage(), ex);
            return new ResponseEntity<>("Ocurrió un error al procesar la solicitud.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/subirImagen")
    public ResponseEntity<Object> subirImagen(
            @RequestParam("codigoPaciente") String codigoPaciente,
            @RequestPart("file") MultipartFile file) {
        try {
            // armar logica
            return ResponseEntity.ok("Imagen subida y guardada con éxito.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error al subir la imagen.");
        }
    }

    @PostMapping("/ingresarPaciente")
    public ResponseEntity<Object> ingresarPaciente(@RequestBody Paciente paciente) {

        logger.info("Recibiendo solicitud con Paciente PARA GUARDAR: {}", paciente);

        pacienteService.guardarPaciente(paciente);
        return ResponseEntity.ok("Paciente ingresado con éxito.");
    }

}

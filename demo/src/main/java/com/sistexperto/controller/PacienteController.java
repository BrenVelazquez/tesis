package com.sistexperto.controller;

import javax.imageio.IIOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.sistexperto.service.PacienteService;

public class PacienteController {
    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    // aca van a ir todas las apis que usemos

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
}

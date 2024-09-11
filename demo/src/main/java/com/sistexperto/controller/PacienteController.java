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

import java.util.List;

import javax.imageio.IIOException;

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
            @RequestPart("file") MultipartFile file,
            @RequestParam("estado") String estado,
            @RequestParam("diagnostico") String diagnostico) {
        try {
            // armar logica
            pacienteService.validateFile(file);

            String originalFileName = cleanFileName(file.getOriginalFilename());
            logger.info("Estado", estado);
            pacienteService.guardarImagen(codigoPaciente, originalFileName, file, estado, diagnostico);

            return ResponseEntity.ok("Imagen subida y guardada con éxito.");
        } catch (IIOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error al subir la imagen.");
        }
    }

    public String cleanFileName(String fileName) {
        // Remover caracteres especiales 
        // mantener solo letras, números, guiones y puntos
        String cleanedFileName = fileName.replaceAll("[^a-zA-Z0-9.-]", "_");

        // Asegurarse de que el nombre de archivo no comience con un guion o un punto
        cleanedFileName = cleanedFileName.replaceAll("^[.-]", "_");

        return cleanedFileName;
    }

    @PostMapping("/ingresarPaciente")
    public ResponseEntity<Object> ingresarPaciente(@RequestBody Paciente paciente) {

        logger.info("Recibiendo solicitud con Paciente PARA GUARDAR: {}", paciente);

        pacienteService.guardarPaciente(paciente);
        return ResponseEntity.ok("Paciente ingresado con éxito.");
    }

}

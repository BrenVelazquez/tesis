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
import com.sistexperto.model.Medico;
import com.sistexperto.service.MedicoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.IIOException;

@RestController
public class MedicoController {
    private final MedicoService medicoService;

    @Autowired
    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    private static final Logger logger = LoggerFactory.getLogger(MedicoController.class);


    /*@PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody String mail, String contraseña) {
        
        Boolean exito = medicoService.login(mail, contraseña);
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("exito", exito);
        if (exito) {
            respuesta.put("mensaje", "Usuario y contraseña correcto.");
        } else {
            respuesta.put("mensaje", "Médico no encontrado.");
        }

        return ResponseEntity.ok(respuesta);

    }*/
}

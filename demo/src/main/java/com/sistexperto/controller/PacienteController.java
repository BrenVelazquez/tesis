package com.sistexperto.controller;

import com.sistexperto.service.PacienteService;

public class PacienteController {
    private final PacienteService pacienteService;
    
    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    // aca van a ir todas las apis que usemos
}

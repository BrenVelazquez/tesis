package com.sistexperto.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.util.Arrays;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sistexperto.database.database;
import com.sistexperto.dto.PacienteRequest;
import com.sistexperto.dto.PacienteResponse;
import com.sistexperto.model.Medico;
import org.slf4j.Logger;

@Service
public class MedicoService {

    private final KieContainer kieContainer;
    private static final Logger logger = LoggerFactory.getLogger(PacienteService.class);

    public MedicoService(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }
}

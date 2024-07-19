package com.sistexperto.service;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sistexperto.dto.PacienteRequest;
import com.sistexperto.dto.PacienteResponse;

@Service
public class PacienteService {
    
    private final KieContainer kieContainer;
    
    public PacienteService(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }

    public PacienteResponse pacienteResponse(PacienteRequest pacienteRequest) {
        PacienteResponse pacienteResponse = new PacienteResponse();

        // Crear y configurar una nueva sesión de Drools
        // Es el bjeto que contiene las reglas
        KieSession kieSession = kieContainer.newKieSession();

        // Estos son los resultados que vamos a obtener
        Boolean noPosible = Boolean.FALSE;
        Boolean posible = Boolean.FALSE;
        Boolean posibleTemporal = Boolean.FALSE;

        kieSession.setGlobal("noPosible", noPosible);
        kieSession.setGlobal("posible", posible);
        kieSession.setGlobal("posibleTemporal", posibleTemporal);
        kieSession.setGlobal("pacienteResponse", pacienteResponse);

        // este es el paciente al que le estoy aplicando a las reglas (los datos vienen del front)
        kieSession.insert(pacienteRequest); 
        //Corre todas las reglas al mismo tiempo. (se puede hacer que se ejecuten reglas especificas y no todo junto pero hay que ver como y si sirve de algo)
        kieSession.fireAllRules(); 
        kieSession.dispose();

        // Devolver la respuesta
        return pacienteResponse;
    }
    

    // si vamos a cargar archivos o imagenes de los estudios del paciente la logica va aca


    // TODO: guardar datos del paciente en la base

    // guardar diagnostico
}

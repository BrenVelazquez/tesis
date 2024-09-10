package com.sistexperto.service;

import java.util.Arrays;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sistexperto.dto.PacienteRequest;
import com.sistexperto.dto.PacienteResponse;
import com.sistexperto.model.Paciente;

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
        Boolean noPosibleEsquizofrenia = Boolean.FALSE;
        Boolean posibleEsquizofrenia = Boolean.FALSE;
        Boolean posibleTemporal = Boolean.FALSE;

        kieSession.setGlobal("noPosibleEsquizofrenia", noPosibleEsquizofrenia);
        kieSession.setGlobal("posibleEsquizofrenia", posibleEsquizofrenia);
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
    

    // region Subir imagen

    public void validateFile(MultipartFile file) throws Exception {
        if (!isFileExtensionValid(file)) {
            throw new Exception("La extensión del archivo no es válida.");
        }

        if (!isFileSizeValid(file, 1048576)) {
            throw new Exception("El archivo es demasiado grande.");
        }

        if (!isFileMimeTypeValid(file)) {
            throw new Exception("El tipo de archivo no es válido.");
        }
    }

    private boolean isFileExtensionValid(MultipartFile file) {
        // Cambia esta implementación según las extensiones de archivo válidas que querramos permitir, ejemplo pdf
        String[] allowedExtensions = { "jpg", "png" };
        String fileExtension = getFileExtension(file.getOriginalFilename());
        return Arrays.stream(allowedExtensions).anyMatch(ext -> ext.equalsIgnoreCase(fileExtension));
    }

    private boolean isFileSizeValid(MultipartFile file, long maxSize) {
        return file.getSize() <= maxSize;
    }

    private boolean isFileMimeTypeValid(MultipartFile file) {
        // Tipos MIME válidos: JPG y PNG
        String[] allowedMimeTypes = { "image/jpeg", "image/png" };
        return Arrays.asList(allowedMimeTypes).contains(file.getContentType());
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex != -1) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }

    //endregion Subir imagen

    // TODO: guardar datos del paciente en la base
 public void guardarPaciente(Paciente pacienteCompleto) {
        // Crear un paciente completo con datos de ejemplo
        Paciente paciente = new Paciente();
        paciente.setSexo(pacienteCompleto.getSexo());
        paciente.setEdad(pacienteCompleto.getEdad());
        // paciente.setNivelRiesgo(pacienteCompleto.getNivelRiesgo());
        // paciente.setJustificacion(pacienteCompleto.getJustificacion());
        // paciente.setRecomendacion(pacienteCompleto.getRecomendacion());
        // paciente.setComentariosMedicos(pacienteCompleto.getComentariosMedicos());
        // paciente.setEstado(pacienteCompleto.getEstado());
        // paciente.setCodigoPaciente(pacienteCompleto.getCodigoPaciente());
        // paciente.setJustificacion_rechazo(pacienteCompleto.getJustificacion_rechazo());

        // database.guardarPaciente(paciente);
    }


    // guardar diagnostico
}

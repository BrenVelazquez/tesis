package com.sistexperto.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sistexperto.dto.PacienteRequest;
import com.sistexperto.dto.PacienteResponse;
import com.sistexperto.model.Paciente;
import org.slf4j.Logger;

@Service
public class PacienteService {

    private final KieContainer kieContainer;
    private static final Logger logger = LoggerFactory.getLogger(PacienteService.class);

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

        // este es el paciente al que le estoy aplicando a las reglas (los datos vienen
        // del front)
        kieSession.insert(pacienteRequest);
        // kieSession.insert(pacienteResponse);
        
        // Corre todas las reglas al mismo tiempo. (se puede hacer que se ejecuten
        // reglas especificas y no todo junto pero hay que ver como y si sirve de algo)
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
        // Cambia esta implementación según las extensiones de archivo válidas que
        // querramos permitir, ejemplo pdf
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


    public void guardarImagen(String codigoPaciente, String originalFileName, MultipartFile file, String estado,
            String riesgo) throws IOException {
    //     // Directorio donde se guardarán las imágenes dentro del proyecto (en este caso,
    //     // dentro de src/img)
    //     String uploadDir = "src/main/resources/static";
    //     String uploadDirPath = "";

    //     if (estado.equals("Confirmado")) {
    //         if (riesgo.equals("Riesgo Bajo")) {
    //             uploadDirPath = "/imagenes/benignos";
    //             uploadDir += uploadDirPath;

    //         } else {
    //             uploadDirPath = "/imagenes/malignos";
    //             uploadDir += uploadDirPath;

    //         }
    //     } else if (estado.equals("Rechazado")) {
    //         uploadDirPath = "/imagenes/rechazdos";
    //         uploadDir += uploadDirPath;
    //     }

    //     // Crear el directorio si no existe
    //     Path uploadPath = Path.of(uploadDir);
    //     if (!Files.exists(uploadPath)) {
    //         Files.createDirectories(uploadPath);
    //     }

    //     // Construir el nombre del archivo completo (código del paciente + extensión del
    //     // archivo original)
    //     String fileName = codigoPaciente + '.' + getFileExtension(originalFileName);
    //     logger.info("String:", fileName);
    //     // Ruta completa del archivo en el servidor (dentro del proyecto)
    //     Path filePath = uploadPath.resolve(fileName);
    //     logger.info("filePath:", filePath);

    //     // Copiar el archivo al servidor
    //     try {
    //         Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    //         ImagenesEntity imagen = new ImagenesEntity();
    //         imagen.setNombre_imagen(fileName);
    //         imagen.setRuta_imagen(uploadDirPath + '/' + fileName);

    //         database.guardarImagen(imagen);
    //     } catch (IOException e) {
    //         throw new IOException("No se pudo guardar el archivo: " + e.getMessage());
    //     }
    }

    // endregion Subir imagen

    // region guardar paciente
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

    // endregion guardar paciente

    // guardar diagnostico
}

package com.sistexperto.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
<<<<<<< HEAD
import com.sistexperto.model.Medico;
=======
import java.util.StringJoiner;
>>>>>>> 1168b328f742551a8e2ce14a3fe8614c860716d3

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sistexperto.controller.PacienteDTO;
import com.sistexperto.database.database;
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
        Boolean esquizofrenia = Boolean.FALSE;
        Boolean posibleTemporal = Boolean.FALSE;

        kieSession.setGlobal("noPosibleEsquizofrenia", noPosibleEsquizofrenia);
        kieSession.setGlobal("esquizofrenia", esquizofrenia);
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
        // // Directorio donde se guardarán las imágenes dentro del proyecto (en este
        // caso,
        // // dentro de src/img)
        // String uploadDir = "src/main/resources/static";
        // String uploadDirPath = "";

        // if (estado.equals("Confirmado")) {
        // if (riesgo.equals("Riesgo Bajo")) {
        // uploadDirPath = "/imagenes/benignos";
        // uploadDir += uploadDirPath;

        // } else {
        // uploadDirPath = "/imagenes/malignos";
        // uploadDir += uploadDirPath;

        // }
        // } else if (estado.equals("Rechazado")) {
        // uploadDirPath = "/imagenes/rechazdos";
        // uploadDir += uploadDirPath;
        // }

        // // Crear el directorio si no existe
        // Path uploadPath = Path.of(uploadDir);
        // if (!Files.exists(uploadPath)) {
        // Files.createDirectories(uploadPath);
        // }

        // // Construir el nombre del archivo completo (código del paciente + extensión
        // del
        // // archivo original)
        // String fileName = codigoPaciente + '.' + getFileExtension(originalFileName);
        // logger.info("String:", fileName);
        // // Ruta completa del archivo en el servidor (dentro del proyecto)
        // Path filePath = uploadPath.resolve(fileName);
        // logger.info("filePath:", filePath);

        // // Copiar el archivo al servidor
        // try {
        // Files.copy(file.getInputStream(), filePath,
        // StandardCopyOption.REPLACE_EXISTING);
        // ImagenesEntity imagen = new ImagenesEntity();
        // imagen.setNombre_imagen(fileName);
        // imagen.setRuta_imagen(uploadDirPath + '/' + fileName);

        // database.guardarImagen(imagen);
        // } catch (IOException e) {
        // throw new IOException("No se pudo guardar el archivo: " + e.getMessage());
        // }
    }

    // endregion Subir imagen

    // region guardar paciente
    public boolean ingresarNuevoPaciente(Paciente pacienteCompleto) {
        // Crear un paciente completo con datos de ejemplo
        Paciente paciente = new Paciente();

        // PACIENTE
        paciente.setNombre(pacienteCompleto.getNombre());
        paciente.setSexo(pacienteCompleto.getSexo());
        paciente.setEdad(pacienteCompleto.getEdad());

        // ESTUDIOS
        paciente.setEstudios(pacienteCompleto.getEstudios());
        paciente.setEstudioCausaNatural(pacienteCompleto.getEstudioCausaNatural());
        paciente.setEstudioComentario(pacienteCompleto.getEstudioComentario());
        // paciente.setIdImagen(pacienteCompleto.getIdImagen());

        // HISTORIAS_CLINICAS
        paciente.setTrastornoAutista(pacienteCompleto.getTrastornoAutista());
        paciente.setTrastornoComunicacion(pacienteCompleto.getTrastornoComunicacion());
        paciente.setTrastornoEsquizoafectivo(pacienteCompleto.getTrastornoEsquizoafectivo());
        paciente.setTrastornoDepresivo(pacienteCompleto.getTrastornoDepresivo());
        paciente.setTrastornoBipolar(pacienteCompleto.getTrastornoBipolar());
        paciente.setAntecedentesFamiliares(pacienteCompleto.getAntecedentesFamiliares());
        paciente.setSintomasPositivosDuracion(pacienteCompleto.getSintomasPositivosDuracion());
        paciente.setSustancias(pacienteCompleto.getSustancias());

        // SINTOMAS_POSITIVOS
        paciente.setSintomasPositivosTipoRitmoPensamiento(pacienteCompleto.getSintomasPositivosTipoRitmoPensamiento());

        // SINTOMA_ALUCINACIONES
        paciente.setSintomasPositivosTipoAlucinaciones(pacienteCompleto.getSintomasPositivosTipoAlucinaciones());
        paciente.setSintomasPositivosAlucinaciones(pacienteCompleto.getSintomasPositivosAlucinaciones());

        // SINTOMA_LENGUAJES
        paciente.setSintomasPositivosTipoLenguaje(pacienteCompleto.getSintomasPositivosTipoLenguaje());

        // SINTOMA_PENSAMIENTOS
        paciente.setSintomasPositivosTipoPensamiento(pacienteCompleto.getSintomasPositivosTipoPensamiento());

        // SINTOMA_CONTENIDOS_PENSAMIENTOS
        paciente.setSintomasPositivosTipoContenidoPensamiento(
                pacienteCompleto.getSintomasPositivosTipoContenidoPensamiento());

        // SINTOMAS_NEGATIVOS
        paciente.setSintomasNegativosDuracion(pacienteCompleto.getSintomasNegativosDuracion());
        paciente.setSintomasNegativosAtencion(pacienteCompleto.getSintomasNegativosAtencion());
        paciente.setSintomasNegativosActividad(pacienteCompleto.getSintomasNegativosActividad());
        paciente.setSintomasNegativosBajoFuncionamiento(pacienteCompleto.getSintomasNegativosBajoFuncionamiento());
        paciente.setSintomasNegativosBajoFuncionamientoComentario(
                pacienteCompleto.getSintomasNegativosBajoFuncionamientoComentario());

        // SINTOMA_ASPECTOS
        paciente.setSintomasNegativosAspecto(pacienteCompleto.getSintomasNegativosAspecto());

        // SINTOMA_AFECTIVIDADES
        paciente.setSintomasNegativosAfectividad(pacienteCompleto.getSintomasNegativosAfectividad());

        // paciente.setCodigoPaciente(pacienteCompleto.getCodigoPaciente());
        paciente.setDiagnostico(pacienteCompleto.getDiagnostico());
        paciente.setJustificacion(pacienteCompleto.getJustificacion());
        paciente.setReglas(pacienteCompleto.getReglas());
        paciente.setRecomendacion(pacienteCompleto.getRecomendacion());
        paciente.setComentarioMedico(pacienteCompleto.getComentarioMedico());
        paciente.setJustificacionRechazo(pacienteCompleto.getJustificacionRechazo());
        paciente.setEstado(pacienteCompleto.getEstado());
        paciente.setPuntaje(pacienteCompleto.getPuntaje());

        // logger.info("##### PACIENTE: ", paciente, " #####");

        Boolean exito = database.ingresarNuevoPaciente(paciente);
        return exito;
    }
    // endregion guardar paciente

    // region guardar diagnostico
    // endregion guardar diagnostico

<<<<<<< HEAD
    public String login(String mail, String password) {
=======
    // region obtenerTodosLosPacientes
    public List<PacienteDTO> obtenerTodosLosPacientes() {
        return database.obtenerPacientes();
    }
    // endregion obtenerTodosLosPacientes

    // region obtenerPacientePorId
    public Paciente obtenerPacientePorId(int idPaciente) {
        Paciente paciente = database.obtenerPacientePorId(idPaciente);

        if(paciente != null){
            String sexo = paciente.getSexo();
            if (sexo != null) {
                paciente.setSexo(formatearTextoSintomas(sexo));
            }
            String ritmoPensamiento = paciente.getSintomasPositivosTipoRitmoPensamiento();
            if (ritmoPensamiento != null) {
                paciente.setSintomasPositivosTipoRitmoPensamiento(formatearTextoSintomas(ritmoPensamiento));
            }
            String alucinaciones = convertirSintomasAString(database.obtenerAlucinacionesPorSintoma(idPaciente));
            paciente.setSintomasPositivosTipoAlucinaciones(formatearTextoSintomas(alucinaciones));
            String lenguajes = convertirSintomasAString(database.obtenerLenguajesPorPacienteId(idPaciente));
            paciente.setSintomasPositivosTipoLenguaje(formatearTextoSintomas(lenguajes));
            String pensamientos = convertirSintomasAString(database.obtenerPensamientosPorPacienteId(idPaciente));
            paciente.setSintomasPositivosTipoPensamiento(formatearTextoSintomas(pensamientos));
            String contenidosPensamientos = convertirSintomasAString(
                    database.obtenerContenidosPensamientosPorPacienteId(idPaciente));
            paciente.setSintomasPositivosTipoContenidoPensamiento(formatearTextoSintomas(contenidosPensamientos));
    
            String atenciones = paciente.getSintomasNegativosAtencion();
            if (atenciones != null) {
                paciente.setSintomasNegativosAtencion(formatearTextoSintomas(atenciones));
            }
            String actividades = paciente.getSintomasNegativosActividad();
            if (actividades != null) {
                paciente.setSintomasNegativosActividad(formatearTextoSintomas(actividades));
            }
            String aspectos = convertirSintomasAString(database.obtenerAspectosPorPacienteId(idPaciente));
            paciente.setSintomasNegativosAspecto(formatearTextoSintomas(aspectos));
            String afectividades = convertirSintomasAString(database.obtenerAfectividadesPorPacienteId(idPaciente));
            paciente.setSintomasNegativosAfectividad(formatearTextoSintomas(afectividades));
    
            paciente.setSintomasPositivosTipoLenguaje(formatearTextoSintomas(lenguajes));
            if (paciente != null && paciente.getFechaConsulta() != null) {
                String fechaFormateada = formatearFecha(paciente.getFechaConsulta());
                paciente.setFechaConsulta(fechaFormateada);
            }
        }
        return paciente;
    }
    // endregion obtenerPacientePorId

    // region login
    public boolean login(String mail, String password) {
>>>>>>> 1168b328f742551a8e2ce14a3fe8614c860716d3
        System.err.println("service");
        String medico = database.login(mail, password);
        return medico;
    }
    // endregion login

    // region formateo
    private String formatearFecha(String fechaConsulta) {
        LocalDate fecha = LocalDate.parse(fechaConsulta);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return fecha.format(formatter);
    }

    public String formatearTextoSintomas(String sintomas) {
        String[] sintomasArray = sintomas.split(", ");
        StringBuilder resultado = new StringBuilder();
        for (String sintoma : sintomasArray) {
            String sintomaFormateado = sintoma.replace("_", " ");
            sintomaFormateado = sintomaFormateado.toLowerCase();
            sintomaFormateado = sintomaFormateado.substring(0, 1).toUpperCase() + sintomaFormateado.substring(1);
            if (resultado.length() > 0) {
                resultado.append(", ");
            }
            resultado.append(sintomaFormateado);
        }
        return resultado.toString();
    }

    public String convertirSintomasAString(List<String> sintomas) {
        if (sintomas == null || sintomas.isEmpty()) {
            return "";
        }
        StringJoiner sintomasString = new StringJoiner(", ");
        for (String sintoma : sintomas) {
            sintomasString.add(sintoma);
        }
        return sintomasString.toString();
    }
    // endregion formateo

}

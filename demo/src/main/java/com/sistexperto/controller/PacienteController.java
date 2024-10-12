package com.sistexperto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sistexperto.model.Medico;
import com.sistexperto.dto.PacienteRequest;
import com.sistexperto.dto.PacienteResponse;
import com.sistexperto.model.Paciente;
import com.sistexperto.service.PacienteService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.IIOException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
public class PacienteController {
    private final PacienteService pacienteService;

    @Autowired
    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    private static final Logger logger = LoggerFactory.getLogger(PacienteController.class);

    // region diagnosticar
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
    // endregion diagnosticar

    //region subirImagen
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
        // mantener solo letras, numeros, guiones y puntos
        String cleanedFileName = fileName.replaceAll("[^a-zA-Z0-9.-]", "_");

        // Asegurarse de que el nombre de archivo no comience con un guion o un punto
        cleanedFileName = cleanedFileName.replaceAll("^[.-]", "_");

        return cleanedFileName;
    }
    //endregion subitImagen

    // region ingresarPaciente
    @PostMapping("/ingresarPaciente")
    public ResponseEntity<Map<String, Object>> ingresarPaciente(@RequestBody Paciente paciente) {

        logger.info("Recibiendo solicitud con Paciente PARA GUARDAR: {}", paciente);

        Boolean exito = pacienteService.ingresarNuevoPaciente(paciente);
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("exito", exito);
        if (exito) {
            respuesta.put("mensaje", "Paciente ingresado con éxito.");
        } else {
            respuesta.put("mensaje", "Ocurrió un error al ingresar al paciente.");
        }

        return ResponseEntity.ok(respuesta);
    }
    //endregion ingresarPaciente

    //region obtenerPacientes
    @GetMapping("/obtenerPacientes")
    public ResponseEntity<List<Map<String, Object>>> obtenerPacientes() {
        try {
            List<PacienteDTO> listaPacientes = pacienteService.obtenerTodosLosPacientes();
            List<Map<String, Object>> respuesta = new ArrayList<>();

            for (PacienteDTO paciente : listaPacientes) {
                Map<String, Object> datosPaciente = new HashMap<>();
                datosPaciente.put("idPaciente", paciente.getIdPaciente());
                datosPaciente.put("nombre", paciente.getNombre());
                datosPaciente.put("diagnostico", paciente.getDiagnostico());
                datosPaciente.put("estado", paciente.getEstado());
                datosPaciente.put("fecha", paciente.getFecha());
                respuesta.add(datosPaciente);
            }
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    //endregion obtenerPacientes

    // region obtenerDetallesPAciente
    @GetMapping("/obtenerDetallesPaciente/{idPaciente}")
    public ResponseEntity<Map<String, Object>> obtenerDetallesPaciente(@PathVariable Integer idPaciente) {
        try {
            Paciente paciente = pacienteService.obtenerPacientePorId(idPaciente);

            if (paciente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Map<String, Object> datosPaciente = new HashMap<>();
            datosPaciente.put("idPaciente", paciente.getIdPaciente());
            datosPaciente.put("nombre", paciente.getNombre());
            datosPaciente.put("edad", paciente.getEdad());
            datosPaciente.put("sexo", paciente.getSexo());
            datosPaciente.put("trastornoAutista", paciente.getTrastornoAutista());
            datosPaciente.put("trastornoComunicacion", paciente.getTrastornoComunicacion());
            datosPaciente.put("trastornoEsquizoafectivo", paciente.getTrastornoEsquizoafectivo());
            datosPaciente.put("trastornoDepresivo", paciente.getTrastornoDepresivo());
            datosPaciente.put("trastornoBipolar", paciente.getTrastornoBipolar());
            datosPaciente.put("antecedentesFamiliares", paciente.getAntecedentesFamiliares());
            datosPaciente.put("sustancias", paciente.getSustancias());
            datosPaciente.put("estudioCausaNatural", paciente.getEstudioCausaNatural());
            datosPaciente.put("estudioComentario", paciente.getEstudioComentario());
            // datosPaciente.put("idImagen", paciente.getIdImagen());
            // TODO: AGREGAR IMAGEN

            datosPaciente.put("sintomasPositivosDuracion", paciente.getSintomasPositivosDuracion());
            datosPaciente.put("sintomasPositivosTipoRitmoPensamiento",
                    paciente.getSintomasPositivosTipoRitmoPensamiento());
            datosPaciente.put("sintomasPositivosTipoAlucinaciones", paciente.getSintomasPositivosTipoAlucinaciones());
            datosPaciente.put("sintomasPositivosTipoLenguaje", paciente.getSintomasPositivosTipoLenguaje());
            datosPaciente.put("sintomasPositivosTipoPensamiento", paciente.getSintomasPositivosTipoPensamiento());
            datosPaciente.put("sintomasPositivosTipoContenidoPensamiento",
                    paciente.getSintomasPositivosTipoContenidoPensamiento());

            datosPaciente.put("sintomasNegativosDuracion", paciente.getSintomasNegativosDuracion());
            datosPaciente.put("sintomasNegativosBajoFuncionamiento", paciente.getSintomasNegativosBajoFuncionamiento());
            datosPaciente.put("sintomasNegativosBajoFuncionamientoComentario",
                    paciente.getSintomasNegativosBajoFuncionamientoComentario());
            datosPaciente.put("sintomasNegativosAspecto", paciente.getSintomasNegativosAspecto());
            datosPaciente.put("sintomasNegativosAtencion", paciente.getSintomasNegativosAtencion());
            datosPaciente.put("sintomasNegativosActividad", paciente.getSintomasNegativosActividad());
            datosPaciente.put("sintomasNegativosAfectividad", paciente.getSintomasNegativosAfectividad());

            datosPaciente.put("diagnostico", paciente.getDiagnostico());
            datosPaciente.put("justificacion", paciente.getJustificacion());
            // datosPaciente.put("reglas", paciente.getReglas());
            datosPaciente.put("recomendacion", paciente.getRecomendacion());
            datosPaciente.put("comentarioMedico", paciente.getComentarioMedico());
            datosPaciente.put("justificacionRechazo", paciente.getJustificacionRechazo());
            datosPaciente.put("estado", paciente.getEstado());
            datosPaciente.put("fechaConsulta", paciente.getFechaConsulta());
            // datosPaciente.put("puntaje", paciente.getPuntaje());
            datosPaciente.put("nombreMedico", paciente.getNombreMedico());
            datosPaciente.put("apellidoMedico", paciente.getApellidoMedico());

            return ResponseEntity.ok(datosPaciente);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    //endregion obtenerDetallesPaciente

    //region login
    @PostMapping("/login")
    // public ResponseEntity<Map<String, Object>> login(@RequestBody String mail,
    // String contraseña) {
    public ResponseEntity<Object> login(@RequestBody Map<String, String> loginRequest) {
        String mail = loginRequest.get("mail");
        String contraseña = loginRequest.get("contraseña");
        System.out.println(mail);
        String medico = pacienteService.login(mail, contraseña);
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("medico", medico);
        if (medico!="") {
            respuesta.put("mensaje", "Usuario y contraseña correcto.");
        } else {
            respuesta.put("mensaje", "Médico no encontrado.");
        }
        return ResponseEntity.ok(respuesta);
    }
    // endregion login

    //region descargarExcel
    @GetMapping("/descargarExcel")
    public ResponseEntity<byte[]> descargarExcel() {
        try {
            // 1. Llama al método del servicio para exportar los datos a Excel o CSV
            byte[] excelOrCsvData = pacienteService.descargarExcel();

            if (excelOrCsvData != null) {
                // 2. Configura el encabezado HTTP para la descarga del archivo
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment", "dataset.xlsx");

                // 3. Devuelve el archivo como respuesta HTTP
                return new ResponseEntity<>(excelOrCsvData, headers, HttpStatus.OK);
            } else {
                // Manejar el caso de error o datos no disponibles
                 return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Manejar excepciones en caso de error
            e.printStackTrace();
             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    //endregion decargarExcel

}

package com.sistexperto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sistexperto.model.Consulta;
import com.sistexperto.model.Medico;
import com.sistexperto.dto.PacienteRequest;
import com.sistexperto.dto.PacienteResponse;
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

    // region subirImagen
    @PostMapping("/subirImagen")
    public ResponseEntity<Object> subirImagen(
            @RequestParam("codigoPaciente") String codigoPaciente,
            @RequestPart("file") MultipartFile file,
            @RequestParam("estado") String estado,
            @RequestParam("diagnostico") String diagnostico) {
        try {
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
        String cleanedFileName = fileName.replaceAll("[^a-zA-Z0-9.-]", "_");
        cleanedFileName = cleanedFileName.replaceAll("^[.-]", "_");

        return cleanedFileName;
    }
    // endregion subitImagen

    // region ingresarPaciente
    @PostMapping("/ingresarPaciente")
    public ResponseEntity<Map<String, Object>> ingresarPaciente(@RequestBody Consulta request) {
        /*
         * Paciente paciente = request.getPaciente();
         * //AGREGO CLASES
         * logger.info("ANTECEDENTES FAMILIARES:",paciente.getHistoriaClinica().
         * getAntecedentesFamiliares());
         * Medico medico=request.getMedico();
         * Diagnostico diagnostico=request.getDiagnostico();
         * Consulta consulta=new Consulta();
         * consulta.setDiagnostico(diagnostico);
         * consulta.setFechaConsulta(null);
         * consulta.setMedico(medico);
         * consulta.setPaciente(paciente);
         * //FIN AGREGO CLASES
         * int idMedico = request.getMedico().getId();
         * logger.info("Recibiendo solicitud con Paciente PARA GUARDAR: {}",
         * consulta.getPaciente().getNombre());
         * logger.info("IMAGEN PATH",
         * consulta.getPaciente().getHistoriaClinica().getEstudio().getImagen());
         */

        Boolean exito = pacienteService.ingresarNuevoPaciente(request);
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("exito", exito);
        if (exito) {
            respuesta.put("mensaje", "Paciente ingresado con éxito.");
        } else {
            respuesta.put("mensaje", "Ocurrió un error al ingresar al paciente.");
        }

        return ResponseEntity.ok(respuesta);
    }
    // endregion ingresarPaciente

    // region obtenerPacientes
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
                datosPaciente.put("nombreMedico", paciente.getNombreMedico());
                datosPaciente.put("fecha", paciente.getFecha());
                respuesta.add(datosPaciente);
            }
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    // endregion obtenerPacientes

    // region obtenerDetallesPAciente
    @GetMapping("/obtenerDetallesPaciente/{idPaciente}")
    public ResponseEntity<Map<String, Object>> obtenerDetallesPaciente(@PathVariable Integer idPaciente) {
        try {
            Consulta consulta = pacienteService.obtenerConsultaPorPaciente(idPaciente);

            if (consulta == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Map<String, Object> datosPaciente = new HashMap<>();
            datosPaciente.put("idPaciente", consulta.getPaciente().getIdPaciente());
            datosPaciente.put("nombre", consulta.getPaciente().getNombre());
            datosPaciente.put("edad", consulta.getPaciente().getEdad());
            datosPaciente.put("sexo", consulta.getPaciente().getSexo());
            datosPaciente.put("trastornoAutista", consulta.getPaciente().getHistoriaClinica().getTrastornoAutista());
            datosPaciente.put("trastornoComunicacion",
                    consulta.getPaciente().getHistoriaClinica().getTrastornoComunicacion());
            datosPaciente.put("trastornoEsquizoafectivo",
                    consulta.getPaciente().getHistoriaClinica().getTrastornoEsquizoafectivo());
            datosPaciente.put("trastornoDepresivo",
                    consulta.getPaciente().getHistoriaClinica().getTrastornoDepresivo());
            datosPaciente.put("trastornoBipolar", consulta.getPaciente().getHistoriaClinica().getTrastornoBipolar());
            datosPaciente.put("antecedentesFamiliares",
                    consulta.getPaciente().getHistoriaClinica().getAntecedentesFamiliares());
            datosPaciente.put("sustancias", consulta.getPaciente().getHistoriaClinica().getSustancias());
            datosPaciente.put("estudioCausaNatural",
                    consulta.getPaciente().getHistoriaClinica().getEstudio().getEstudioCausaNatural());
            datosPaciente.put("estudioComentario",
                    consulta.getPaciente().getHistoriaClinica().getEstudio().getEstudioComentario());
            datosPaciente.put("imagen", consulta.getPaciente().getHistoriaClinica().getEstudio().getImagen());
            datosPaciente.put("sintomasPositivosDuracion",
                    consulta.getPaciente().getSintomasPositivos().getSintomasPositivosDuracion());
            datosPaciente.put("sintomasPositivosTipoRitmoPensamiento",
                    consulta.getPaciente().getSintomasPositivos().getSintomasPositivosTipoRitmoPensamiento());
            datosPaciente.put("sintomasPositivosTipoAlucinaciones",
                    consulta.getPaciente().getSintomasPositivos().getSintomasPositivosAlucinaciones());
            datosPaciente.put("sintomasPositivosTipoLenguaje",
                    consulta.getPaciente().getSintomasPositivos().getSintomasPositivosTipoLenguaje());
            datosPaciente.put("sintomasPositivosTipoPensamiento",
                    consulta.getPaciente().getSintomasPositivos().getSintomasPositivosTipoPensamiento());
            datosPaciente.put("sintomasPositivosTipoContenidoPensamiento",
                    consulta.getPaciente().getSintomasPositivos().getSintomasPositivosTipoContenidoPensamiento());

            datosPaciente.put("sintomasNegativosDuracion",
                    consulta.getPaciente().getSintomasNegativos().getSintomasNegativosDuracion());
            datosPaciente.put("sintomasNegativosBajoFuncionamiento",
                    consulta.getPaciente().getSintomasNegativos().getSintomasNegativosBajoFuncionamiento());
            datosPaciente.put("sintomasNegativosBajoFuncionamientoComentario",
                    consulta.getPaciente().getSintomasNegativos().getSintomasNegativosBajoFuncionamientoComentario());
            datosPaciente.put("sintomasNegativosAspecto",
                    consulta.getPaciente().getSintomasNegativos().getSintomasNegativosAspecto());
            datosPaciente.put("sintomasNegativosAtencion",
                    consulta.getPaciente().getSintomasNegativos().getSintomasNegativosAtencion());
            datosPaciente.put("sintomasNegativosActividad",
                    consulta.getPaciente().getSintomasNegativos().getSintomasNegativosActividad());
            datosPaciente.put("sintomasNegativosAfectividad",
                    consulta.getPaciente().getSintomasNegativos().getSintomasNegativosAfectividad());

            datosPaciente.put("diagnostico", consulta.getDiagnostico().getDiagnostico());
            datosPaciente.put("justificacion", consulta.getDiagnostico().getJustificacion());
            // datosPaciente.put("reglas", paciente.getReglas());
            datosPaciente.put("recomendacion", consulta.getDiagnostico().getRecomendacion());
            datosPaciente.put("comentarioMedico", consulta.getDiagnostico().getComentarioMedico());
            datosPaciente.put("justificacionRechazo", consulta.getDiagnostico().getJustificacionRechazo());
            datosPaciente.put("estado", consulta.getDiagnostico().getEstado());
            datosPaciente.put("fechaConsulta", consulta.getFechaConsulta());
            // datosPaciente.put("puntaje", paciente.getPuntaje());
            datosPaciente.put("nombreMedico", consulta.getMedico().getNombreMedico());
            datosPaciente.put("apellidoMedico", consulta.getMedico().getApellidoMedico());

            return ResponseEntity.ok(datosPaciente);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    // endregion obtenerDetallesPaciente

    // region login
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Map<String, String> loginRequest) {
        String mail = loginRequest.get("mail");
        String contraseña = loginRequest.get("contraseña");
        Medico medico = pacienteService.login(mail, contraseña);
        Map<String, Object> respuesta = new HashMap<>();

        if (medico != null) {
            respuesta.put("medico", medico);
            respuesta.put("mensaje", "Usuario y contraseña correcto.");
        } else {
            respuesta.put("mensaje", "Médico no encontrado.");
        }
        return ResponseEntity.ok(respuesta);
    }
    // endregion login

    // region descargarExcel
    @GetMapping("/descargarExcel")
    public ResponseEntity<?> descargarExcel() {
        try {
            byte[] excelOrCsvData = pacienteService.descargarExcel();

            if (excelOrCsvData == null || excelOrCsvData.length == 0) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "No hay datos disponibles para exportar.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "dataset.xlsx");

            return new ResponseEntity<>(excelOrCsvData, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    // endregion decargarExcel

}

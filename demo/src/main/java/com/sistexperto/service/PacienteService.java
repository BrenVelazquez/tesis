package com.sistexperto.service;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import com.sistexperto.model.Medico;
import java.util.StringJoiner;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import java.io.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import java.awt.Color;

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

        Boolean exito = database.ingresarNuevoPaciente(paciente);
        return exito;
    }
    // endregion guardar paciente

    // // region guardar diagnostico
    // // endregion guardar diagnostico

    // region obtenerTodosLosPacientes
    public List<PacienteDTO> obtenerTodosLosPacientes() {
        return database.obtenerPacientes();
    }
    // endregion obtenerTodosLosPacientes

    // region obtenerTodosLosPacientesConDetalles
    public List<Paciente> obtenerTodosLosPacientesConDetalles() {
        List<Paciente> pacientes = database.obtenerTodosLosPacientesConDetalles();

        for (Paciente paciente : pacientes) {
            if (paciente != null) {
                paciente = formatAndGetSintomas(paciente, paciente.getIdPaciente());
            }
        }

        return pacientes;
    }
    // endregion obtenerTodosLosPacientesConDetalles

    // region obtenerPacientePorId
    public Paciente obtenerPacientePorId(int idPaciente) {
        Paciente paciente = database.obtenerPacientePorId(idPaciente);

        if (paciente != null) {
            paciente = formatAndGetSintomas(paciente, idPaciente);
        }
        return paciente;
    }
    // endregion obtenerPacientePorId

    // region login
    public Medico login(String mail, String password) {
        System.err.println("service");
        Medico medico = database.login(mail, password);
        return medico;
    }
    // endregion login

    // region descargarExcel
    public byte[] descargarExcel() {
        try {
            // 1. Obtener los datos de la base de datos
            List<Paciente> pacientes = obtenerTodosLosPacientesConDetalles();

            // 2. Procesar los datos y generar un archivo Excel o CSV
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            writeToExcelOrCSVFile(pacientes, outputStream);

            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void writeToExcelOrCSVFile(List<Paciente> pacientes, OutputStream outputStream) {
        try {
            // Crear un nuevo libro de Excel o archivo CSV
            try (Workbook workbook = new XSSFWorkbook()) {
                // Crear una hoja dentro del libro
                Sheet sheet = workbook.createSheet("Datos");

                CellStyle headerStyle = workbook.createCellStyle();
                byte[] rgbColor = new byte[] { (byte) 0x00, (byte) 0x95, (byte) 0x87 };
                XSSFColor myColor = new XSSFColor(rgbColor, null);
                ((XSSFCellStyle) headerStyle).setFillForegroundColor(myColor);
                headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerFont.setColor(IndexedColors.WHITE.getIndex());
                headerStyle.setFont(headerFont);

                // Crear una fila para los encabezados
                Row headerRow = sheet.createRow(0);

                String[] columnHeaders = { "NOMBRE DEL PACIENTE", "EDAD", "SEXO", "DIAGNOSTICO", "ESTADO",
                        "FECHA DE CONSULTA", "JUSTIFICACION", "RECOMENDACION", "COMENTARIOS ADICIONALES DEL MÉDICO",
                        "JUSTIFICACIÓN DE RECHAZO", "TRASTORNO AUTISTA", "TRASTORNO DE LA COMUNICACIÓN EN LA INFANCIA",
                        "TRASTORNO ESQUIZOAFECTIVO", "TRASTORNO DEPRESIVO",
                        "TRASTORNO BIPOLAR CON CARACTERÍSTICAS PSICÓTICAS", "ANTECEDENTES FAMILIARES",
                        "BAJO SUSTANCIAS", "DURACIÓN DE LOS SINTOMAS POSITIVOS", "ALUCINACIONES", "LENGUAJE",
                        "PENSAMIENTO", "CONTENIDO DEL PENSAMIENTO", "RITMO DEL PENSAMIENTO",
                        "DURACIÓN DE LOS SINTOMAS NEGATIVOS", "ASPECTO", "ATENCIÓN", "ACTIVIDAD", "AFECTIVIDAD",
                        "BAJO FUNCIONAMIENTO EN LOS ÁMBITOS PRINCIPALES",
                        "COMENTARIO SOBRE EL BAJO FUNCIONAMIENTO EN LOS ÁMBITOS PRINCIPALES", "POSEE ESTUDIOS",
                        "SE DESCARTA CAUSA ORGANICA EN LOS ESTUDIOS", "COMENTARIOS DE LOS ESTUDIOS" };
                for (int i = 0; i < columnHeaders.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columnHeaders[i]);
                    cell.setCellStyle(headerStyle);
                }

                int rowNum = 1; // Comienza desde la segunda fila después de los encabezados

                for (Paciente paciente : pacientes) {
                    Row row = sheet.createRow(rowNum++);
                    // Llena las celdas con los datos de pacientes
                    // row.createCell(0).setCellValue(paciente.getIdPaciente());
                    row.createCell(0).setCellValue(paciente.getNombre());
                    row.createCell(1).setCellValue(paciente.getEdad());
                    row.createCell(2).setCellValue(paciente.getSexo());
                    row.createCell(3).setCellValue(paciente.getDiagnostico());
                    row.createCell(4).setCellValue("1".equals(paciente.getEstado()) ? "Confirmado" : "Rechazado");
                    row.createCell(5).setCellValue(paciente.getFechaConsulta());
                    row.createCell(6).setCellValue(paciente.getJustificacion());
                    // row.createCell(0).setCellValue(paciente.getReglas());
                    row.createCell(7).setCellValue(paciente.getRecomendacion());
                    row.createCell(8)
                            .setCellValue(
                                    !paciente.getComentarioMedico().isEmpty() ? paciente.getComentarioMedico() : "-");
                    row.createCell(9).setCellValue(
                            !paciente.getJustificacionRechazo().isEmpty() ? paciente.getJustificacionRechazo() : "-");
                    row.createCell(10).setCellValue(convertirSiNo(paciente.getTrastornoAutista()));
                    row.createCell(11).setCellValue(convertirSiNo(paciente.getTrastornoComunicacion()));
                    row.createCell(12).setCellValue(convertirSiNo(paciente.getTrastornoEsquizoafectivo()));
                    row.createCell(13).setCellValue(convertirSiNo(paciente.getTrastornoDepresivo()));
                    row.createCell(14).setCellValue(convertirSiNo(paciente.getTrastornoBipolar()));
                    row.createCell(15).setCellValue(convertirSiNo(paciente.getAntecedentesFamiliares()));
                    row.createCell(16).setCellValue(convertirSiNo(paciente.getSustancias()));
                    row.createCell(17).setCellValue(paciente.getSintomasPositivosDuracion());
                    row.createCell(18).setCellValue(paciente.getSintomasPositivosTipoAlucinaciones());
                    row.createCell(19).setCellValue(paciente.getSintomasPositivosTipoLenguaje());
                    row.createCell(20).setCellValue(paciente.getSintomasPositivosTipoPensamiento());
                    row.createCell(21).setCellValue(paciente.getSintomasPositivosTipoContenidoPensamiento());
                    row.createCell(22).setCellValue(paciente.getSintomasPositivosTipoRitmoPensamiento());
                    row.createCell(23).setCellValue(paciente.getSintomasNegativosDuracion());
                    row.createCell(24).setCellValue(paciente.getSintomasNegativosAspecto());
                    row.createCell(25).setCellValue(paciente.getSintomasNegativosAtencion());
                    row.createCell(26).setCellValue(paciente.getSintomasNegativosActividad());
                    row.createCell(27).setCellValue(paciente.getSintomasNegativosAfectividad());
                    row.createCell(28).setCellValue(convertirSiNo(paciente.getSintomasNegativosBajoFuncionamiento()));
                    row.createCell(29)
                            .setCellValue(!paciente.getSintomasNegativosBajoFuncionamientoComentario().isEmpty()
                                    ? paciente.getSintomasNegativosBajoFuncionamientoComentario()
                                    : "-");

                    String estudioCausaOrganica = paciente.getEstudioCausaNatural();
                    row.createCell(30).setCellValue(estudioCausaOrganica != null ? "No" : "Sí");
                    estudioCausaOrganica = "estudio-causa-natural-no".equals(estudioCausaOrganica) ? "No"
                            : "estudio-causa-natural-si".equals(estudioCausaOrganica) ? "Si" : "Inconcluso";
                    row.createCell(31).setCellValue(estudioCausaOrganica);
                    row.createCell(32).setCellValue(
                            paciente.getEstudioComentario() != null ? paciente.getEstudioComentario() : "-");
                    // TODO: FALTA IMAGEN
                }

                // Escribir el libro en el flujo de salida
                workbook.write(outputStream);
            }
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // endregion descargarExcel

    // region formatAndGetSintomas
    public Paciente formatAndGetSintomas(Paciente paciente, int idPaciente) {
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
        return paciente;
    }
    // endregion formatAndGetSintomas

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

    public static String convertirSiNo(String estado) {
        return "1".equals(estado) ? "Sí" : "No";
    }
    // endregion formateo

}

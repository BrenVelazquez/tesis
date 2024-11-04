package com.sistexperto.service;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
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
import com.sistexperto.model.RitmoPensamiento;
import com.sistexperto.model.SintomaNegativo;
import com.sistexperto.model.SintomaPositivo;
import com.sistexperto.model.Consulta;
import com.sistexperto.model.HistoriaClinica;
import com.sistexperto.model.Estudio;
import com.sistexperto.model.Diagnostico;
import com.sistexperto.model.Afectividad;
import com.sistexperto.model.Alucinacion;
import com.sistexperto.model.Lenguaje;
import com.sistexperto.model.ContenidoPensamiento;
import com.sistexperto.model.Pensamiento;
import com.sistexperto.model.Actividad;
import com.sistexperto.model.Aspecto;
import com.sistexperto.model.Atencion;
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
        // Es el objeto que contiene las reglas
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
    public boolean ingresarNuevoPaciente(/* Paciente pacienteCompleto, int idMedico */ Consulta c) {
        // Paciente paciente = new Paciente(); COMENTO PARA PROBAR CAMBIOS
        Consulta consulta = new Consulta();
        Paciente paciente = new Paciente();
        Diagnostico diagnostico = new Diagnostico();
        Medico medico = new Medico();
        HistoriaClinica historia = new HistoriaClinica();
        Estudio estudio = new Estudio();
        SintomaPositivo positivos = new SintomaPositivo();
        SintomaNegativo negativos = new SintomaNegativo();

        paciente.setNombre(c.getPaciente().getNombre());
        paciente.setSexo(c.getPaciente().getSexo());
        paciente.setEdad(c.getPaciente().getEdad());

        System.out.println(
                "ESTUDIO CAUSA NATURAL:" + c.getPaciente().getHistoriaClinica().getEstudio().getEstudioCausaNatural());
        estudio.setEstudioCausaNatural(c.getPaciente().getHistoriaClinica().getEstudio().getEstudioCausaNatural());
        estudio.setEstudioComentario(c.getPaciente().getHistoriaClinica().getEstudio().getEstudioComentario());
        estudio.setImagen(c.getPaciente().getHistoriaClinica().getEstudio().getImagen());

        historia.setTrastornoAutista(c.getPaciente().getHistoriaClinica().getTrastornoAutista());
        historia.setTrastornoComunicacion(c.getPaciente().getHistoriaClinica().getTrastornoComunicacion());
        historia.setTrastornoEsquizoafectivo(c.getPaciente().getHistoriaClinica().getTrastornoEsquizoafectivo());
        historia.setTrastornoDepresivo(c.getPaciente().getHistoriaClinica().getTrastornoDepresivo());
        historia.setTrastornoBipolar(c.getPaciente().getHistoriaClinica().getTrastornoBipolar());
        historia.setAntecedentesFamiliares(c.getPaciente().getHistoriaClinica().getAntecedentesFamiliares());
        historia.setSustancias(c.getPaciente().getHistoriaClinica().getSustancias());
        historia.setEstudio(estudio);
        positivos.setSintomasPositivosAlucinaciones(
                c.getPaciente().getSintomasPositivos().getSintomasPositivosAlucinaciones());

        positivos.setSintomasPositivosDuracion(c.getPaciente().getSintomasPositivos().getSintomasPositivosDuracion());
        positivos.setSintomasPositivosTipoContenidoPensamiento(
                c.getPaciente().getSintomasPositivos().getSintomasPositivosTipoContenidoPensamiento());
        positivos.setSintomasPositivosTipoLenguaje(
                c.getPaciente().getSintomasPositivos().getSintomasPositivosTipoLenguaje());
        positivos.setSintomasPositivosTipoPensamiento(
                c.getPaciente().getSintomasPositivos().getSintomasPositivosTipoPensamiento());
        positivos.setSintomasPositivosTipoRitmoPensamiento(
                c.getPaciente().getSintomasPositivos().getSintomasPositivosTipoRitmoPensamiento());

        negativos.setSintomasNegativosBajoFuncionamientoComentario(
                c.getPaciente().getSintomasNegativos().getSintomasNegativosBajoFuncionamiento());
        negativos.setSintomasNegativosBajoFuncionamientoComentario(
                c.getPaciente().getSintomasNegativos().getSintomasNegativosBajoFuncionamientoComentario());
        negativos.setSintomasNegativosActividad(c.getPaciente().getSintomasNegativos().getSintomasNegativosActividad());
        negativos.setSintomasNegativosAfectividad(
                c.getPaciente().getSintomasNegativos().getSintomasNegativosAfectividad());
        negativos.setSintomasNegativosAspecto(c.getPaciente().getSintomasNegativos().getSintomasNegativosAspecto());
        System.out.println("ATENCION" + c.getPaciente().getSintomasNegativos().getSintomasNegativosAtencion());
        negativos.setSintomasNegativosAtencion(c.getPaciente().getSintomasNegativos().getSintomasNegativosAtencion());
        negativos.setSintomasNegativosBajoFuncionamiento(
                c.getPaciente().getSintomasNegativos().getSintomasNegativosBajoFuncionamiento());
        negativos.setSintomasNegativosBajoFuncionamientoComentario(
                c.getPaciente().getSintomasNegativos().getSintomasNegativosBajoFuncionamientoComentario());
        negativos.setSintomasNegativosDuracion(c.getPaciente().getSintomasNegativos().getSintomasNegativosDuracion());

        paciente.setHistoriaClinica(historia);
        paciente.setSintomasNegativos(negativos);
        paciente.setSintomasPositivos(positivos);

        medico.setApellidoMedico(c.getMedico().getApellidoMedico());
        medico.setContraseña(c.getMedico().getContraseña());
        medico.setNombreMedico(c.getMedico().getNombreMedico());
        medico.setDni(c.getMedico().getDni());
        medico.setEmail(c.getMedico().getEmail());
        medico.setId(medico.getId());

        diagnostico.setDiagnostico(c.getDiagnostico().getDiagnostico());
        diagnostico.setComentarioMedico(c.getDiagnostico().getComentarioMedico());
        diagnostico.setEstado(c.getDiagnostico().getEstado());
        diagnostico.setJustificacion(c.getDiagnostico().getJustificacion());
        diagnostico.setJustificacionRechazo(c.getDiagnostico().getJustificacionRechazo());
        diagnostico.setPuntaje(c.getDiagnostico().getPuntaje());
        diagnostico.setRecomendacion(c.getDiagnostico().getRecomendacion());
        diagnostico.setReglas(c.getDiagnostico().getReglas());

        consulta.setDiagnostico(diagnostico);
        consulta.setMedico(medico);
        consulta.setPaciente(paciente);

        Boolean exito = database.ingresarNuevoPaciente(consulta);
        return exito;
    }
    // endregion guardar paciente

    // region guardar diagnostico
    // endregion guardar diagnostico

    // region obtenerTodosLosPacientes
    public List<PacienteDTO> obtenerTodosLosPacientes() {
        return database.obtenerPacientes();
    }

    // region obtenerTodasLasConsultasConDetalles
    public List<Consulta> obtenerTodasLasConsultasConDetalles() {
        List<Consulta> consultas = database.obtenerTodasLasConsultasConDetalles();

        for (Consulta consulta : consultas) {
            if (consulta != null) {
                Paciente p = formatAndGetSintomas(consulta.getPaciente(), consulta.getPaciente().getIdPaciente());
                consulta.setPaciente(p);
            }
            if (consulta != null && consulta.getFechaConsulta() != null) {
                String fechaFormateada = formatearFecha(consulta.getFechaConsulta());
                consulta.setFechaConsulta(fechaFormateada);
            }
        }

        return consultas;
    }
    // endregion obtenerTodasLasConsultasConDetalles

    // region obtenerPacientePorId
    public Consulta obtenerConsultaPorPaciente(int idPaciente) {
        Consulta consulta = database.obtenerConsultaPorPaciente(idPaciente);
        System.out.println(consulta.getPaciente().getHistoriaClinica().getEstudio());
        if (consulta != null) {
            Paciente p = formatAndGetSintomas(consulta.getPaciente(), idPaciente);
            consulta.setPaciente(p);
            System.out.println(consulta.getPaciente().getHistoriaClinica().getEstudio());
        }
        return consulta;
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
            List<Consulta> consultas = obtenerTodasLasConsultasConDetalles();

            // 2. Procesar los datos y generar un archivo Excel o CSV
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            System.out.println(consultas);
            writeToExcelOrCSVFile(consultas, outputStream);

            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void writeToExcelOrCSVFile(List<Consulta> consultas, OutputStream outputStream) {
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
                        "SE DESCARTA CAUSA ORGANICA EN LOS ESTUDIOS", "COMENTARIOS DE LOS ESTUDIOS"/* , "IMAGEN" */ };
                for (int i = 0; i < columnHeaders.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columnHeaders[i]);
                    cell.setCellStyle(headerStyle);
                }

                int rowNum = 1; // Comienza desde la segunda fila después de los encabezados

                for (Consulta consulta : consultas) {
                    Row row = sheet.createRow(rowNum++);
                    // Llena las celdas con los datos de pacientes
                    // row.createCell(0).setCellValue(paciente.getIdPaciente());
                    row.createCell(0).setCellValue(consulta.getPaciente().getNombre());
                    row.createCell(1).setCellValue(consulta.getPaciente().getEdad());
                    row.createCell(2).setCellValue(consulta.getPaciente().getSexo());
                    row.createCell(3).setCellValue(consulta.getDiagnostico().getDiagnostico());
                    row.createCell(4).setCellValue(
                            "1".equals(consulta.getDiagnostico().getEstado()) ? "Confirmado" : "Rechazado");
                    row.createCell(5).setCellValue(consulta.getFechaConsulta());
                    row.createCell(6).setCellValue(consulta.getDiagnostico().getJustificacion());
                    // row.createCell(0).setCellValue(paciente.getReglas());
                    row.createCell(7).setCellValue(consulta.getDiagnostico().getRecomendacion());
                    row.createCell(8)
                            .setCellValue(
                                    !consulta.getDiagnostico().getComentarioMedico().isEmpty()
                                            ? consulta.getDiagnostico().getComentarioMedico()
                                            : "-");
                    row.createCell(9).setCellValue(
                            !consulta.getDiagnostico().getJustificacionRechazo().isEmpty()
                                    ? consulta.getDiagnostico().getJustificacionRechazo()
                                    : "-");
                    row.createCell(10).setCellValue(
                            convertirSiNo(consulta.getPaciente().getHistoriaClinica().getTrastornoAutista()));
                    row.createCell(11).setCellValue(
                            convertirSiNo(consulta.getPaciente().getHistoriaClinica().getTrastornoComunicacion()));
                    row.createCell(12).setCellValue(
                            convertirSiNo(consulta.getPaciente().getHistoriaClinica().getTrastornoEsquizoafectivo()));
                    row.createCell(13).setCellValue(
                            convertirSiNo(consulta.getPaciente().getHistoriaClinica().getTrastornoDepresivo()));
                    row.createCell(14).setCellValue(
                            convertirSiNo(consulta.getPaciente().getHistoriaClinica().getTrastornoBipolar()));
                    row.createCell(15).setCellValue(
                            convertirSiNo(consulta.getPaciente().getHistoriaClinica().getAntecedentesFamiliares()));
                    row.createCell(16)
                            .setCellValue(convertirSiNo(consulta.getPaciente().getHistoriaClinica().getSustancias()));
                    row.createCell(17)
                            .setCellValue(consulta.getPaciente().getSintomasPositivos().getSintomasPositivosDuracion());
                    StringBuilder al = new StringBuilder();
                    Alucinacion[] alucinaciones = consulta.getPaciente().getSintomasPositivos()
                            .getSintomasPositivosAlucinaciones();
                    for (int i = 0; i < alucinaciones.length; i++) {
                        al.append(alucinaciones[i].getNombre());
                        if (i < alucinaciones.length - 1) {
                            al.append(", "); // Agregar coma entre elementos
                        }
                    }
                    row.createCell(18).setCellValue(al.toString());
                    StringBuilder len = new StringBuilder();
                    Lenguaje[] lengaujes = consulta.getPaciente().getSintomasPositivos()
                            .getSintomasPositivosTipoLenguaje();
                    for (int i = 0; i < lengaujes.length; i++) {
                        len.append(lengaujes[i].getNombre());
                        if (i < lengaujes.length - 1) {
                            len.append(", "); // Agregar coma entre elementos
                        }
                    }
                    row.createCell(19).setCellValue(len.toString());
                    StringBuilder tp = new StringBuilder();
                    Pensamiento[] pensamientos = consulta.getPaciente().getSintomasPositivos()
                            .getSintomasPositivosTipoPensamiento();
                    for (int i = 0; i < pensamientos.length; i++) {
                        tp.append(pensamientos[i].getNombre());
                        if (i < pensamientos.length - 1) {
                            tp.append(", "); // Agregar coma entre elementos
                        }
                    }
                    row.createCell(20).setCellValue(tp.toString());
                    StringBuilder cp = new StringBuilder();
                    ContenidoPensamiento[] contenidos = consulta.getPaciente().getSintomasPositivos()
                            .getSintomasPositivosTipoContenidoPensamiento();
                    for (int i = 0; i < contenidos.length; i++) {
                        cp.append(contenidos[i].getNombre());
                        if (i < contenidos.length - 1) {
                            cp.append(", "); // Agregar coma entre elementos
                        }
                    }
                    row.createCell(21).setCellValue(cp.toString());
                    row.createCell(22).setCellValue(consulta.getPaciente().getSintomasPositivos()
                            .getSintomasPositivosTipoRitmoPensamiento().getNombre());
                    // row.createCell(23).setCellValue(paciente.getSintomasNegativosDuracion());
                    row.createCell(23)
                            .setCellValue(consulta.getPaciente().getSintomasNegativos().getSintomasNegativosDuracion());
                    // row.createCell(24).setCellValue(paciente.getSintomasNegativosAspecto());
                    StringBuilder as = new StringBuilder();
                    Aspecto[] aspectos = consulta.getPaciente().getSintomasNegativos().getSintomasNegativosAspecto();
                    for (int i = 0; i < aspectos.length; i++) {
                        as.append(aspectos[i].getNombre());
                        if (i < aspectos.length - 1) {
                            as.append(", "); // Agregar coma entre elementos
                        }
                    }
                    row.createCell(24).setCellValue(as.toString());

                    row.createCell(25).setCellValue(
                            consulta.getPaciente().getSintomasNegativos().getSintomasNegativosAtencion().getNombre());
                    row.createCell(26).setCellValue(
                            consulta.getPaciente().getSintomasNegativos().getSintomasNegativosActividad().getNombre());
                    StringBuilder af = new StringBuilder();
                    Afectividad[] afectividades = consulta.getPaciente().getSintomasNegativos()
                            .getSintomasNegativosAfectividad();
                    for (int i = 0; i < afectividades.length; i++) {
                        af.append(afectividades[i].getNombre());
                        if (i < afectividades.length - 1) {
                            af.append(", "); // Agregar coma entre elementos
                        }
                    }
                    row.createCell(27).setCellValue(af.toString());
                    row.createCell(28).setCellValue(convertirSiNo(
                            consulta.getPaciente().getSintomasNegativos().getSintomasNegativosBajoFuncionamiento()));
                    row.createCell(29)
                            .setCellValue(!consulta.getPaciente().getSintomasNegativos()
                                    .getSintomasNegativosBajoFuncionamientoComentario().isEmpty()
                                            ? consulta.getPaciente().getSintomasNegativos()
                                                    .getSintomasNegativosBajoFuncionamientoComentario()
                                            : "-");

                    String estudioCausaOrganica = consulta.getPaciente().getHistoriaClinica().getEstudio()
                            .getEstudioCausaNatural();
                    row.createCell(30).setCellValue(estudioCausaOrganica != null ? "No" : "Sí");
                    estudioCausaOrganica = "estudio-causa-natural-no".equals(estudioCausaOrganica) ? "No"
                            : "estudio-causa-natural-si".equals(estudioCausaOrganica) ? "Si" : "Inconcluso";
                    row.createCell(31).setCellValue(estudioCausaOrganica);
                    row.createCell(32).setCellValue(
                            consulta.getPaciente().getHistoriaClinica().getEstudio().getEstudioComentario() != null
                                    ? consulta.getPaciente().getHistoriaClinica().getEstudio().getEstudioComentario()
                                    : "-");
                    /*
                     * row.createCell(33).setCellValue(
                     * paciente.getImagen() != null ? paciente.getImagen() : " ");
                     */
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
        RitmoPensamiento ritmoPensamiento = paciente.getSintomasPositivos().getSintomasPositivosTipoRitmoPensamiento();
        if (ritmoPensamiento != null) {
            paciente.getSintomasPositivos().getSintomasPositivosTipoRitmoPensamiento()
                    .setNombre(formatearTextoSintomas(ritmoPensamiento.getNombre()));
        }
        Alucinacion[] alucinaciones = database.obtenerAlucinacionesPorSintoma(idPaciente);
        // String alucinaciones =
        // convertirSintomasAString(database.obtenerAlucinacionesPorSintoma(idPaciente));
        // paciente.setSintomasPositivosTipoAlucinaciones(formatearTextoSintomas(alucinaciones));
        for (Alucinacion alucinacion : alucinaciones) {
            String nombreFormateado = formatearTextoSintomas(alucinacion.getNombre());
            alucinacion.setNombre(nombreFormateado);
        }
        paciente.getSintomasPositivos().setSintomasPositivosAlucinaciones(alucinaciones);
        // String lenguajes =
        // convertirSintomasAString(database.obtenerLenguajesPorPacienteId(idPaciente));
        // paciente.setSintomasPositivosTipoLenguaje(formatearTextoSintomas(lenguajes));
        Lenguaje[] lenguajes = database.obtenerLenguajesPorPacienteId(idPaciente);
        for (Lenguaje lenguaje : lenguajes) {
            String nombreFormateado = formatearTextoSintomas(lenguaje.getNombre());
            lenguaje.setNombre(nombreFormateado);
        }
        paciente.getSintomasPositivos().setSintomasPositivosTipoLenguaje(lenguajes);
        // String pensamientos =
        // convertirSintomasAString(database.obtenerPensamientosPorPacienteId(idPaciente));
        // paciente.setSintomasPositivosTipoPensamiento(formatearTextoSintomas(pensamientos));
        Pensamiento[] pensamientos = database.obtenerPensamientosPorPacienteId(idPaciente);
        for (Pensamiento pensamiento : pensamientos) {
            String nombreFormateado = formatearTextoSintomas(pensamiento.getNombre());
            pensamiento.setNombre(nombreFormateado);
        }
        paciente.getSintomasPositivos().setSintomasPositivosTipoPensamiento(pensamientos);
        // String contenidosPensamientos = convertirSintomasAString(
        // database.obtenerContenidosPensamientosPorPacienteId(idPaciente));
        // paciente.setSintomasPositivosTipoContenidoPensamiento(formatearTextoSintomas(contenidosPensamientos));
        ContenidoPensamiento[] contenidos = database.obtenerContenidosPensamientosPorPacienteId(idPaciente);
        for (ContenidoPensamiento contenido : contenidos) {
            String nombreFormateado = formatearTextoSintomas(contenido.getNombre());
            contenido.setNombre(nombreFormateado);
        }
        paciente.getSintomasPositivos().setSintomasPositivosTipoContenidoPensamiento(contenidos);
        // String atenciones = paciente.getSintomasNegativosAtencion();
        // if (atenciones != null) {
        // paciente.setSintomasNegativosAtencion(formatearTextoSintomas(atenciones));
        // }
        Atencion atencion = paciente.getSintomasNegativos().getSintomasNegativosAtencion();
        if (atencion != null) {
            paciente.getSintomasNegativos().getSintomasNegativosAtencion()
                    .setNombre(formatearTextoSintomas(atencion.getNombre()));
        }
        // String actividades = paciente.getSintomasNegativosActividad();
        // if (actividades != null) {
        // paciente.setSintomasNegativosActividad(formatearTextoSintomas(actividades));
        // }
        Actividad actividad = paciente.getSintomasNegativos().getSintomasNegativosActividad();
        if (actividad != null) {
            paciente.getSintomasNegativos().getSintomasNegativosActividad()
                    .setNombre(formatearTextoSintomas(actividad.getNombre()));
        }
        // String aspectos =
        // convertirSintomasAString(database.obtenerAspectosPorPacienteId(idPaciente));
        // paciente.setSintomasNegativosAspecto(formatearTextoSintomas(aspectos));
        Aspecto[] aspectos = database.obtenerAspectosPorPacienteId(idPaciente);
        for (Aspecto aspecto : aspectos) {
            String nombreFormateado = formatearTextoSintomas(aspecto.getNombre());
            aspecto.setNombre(nombreFormateado);
        }
        paciente.getSintomasNegativos().setSintomasNegativosAspecto(aspectos);
        // String afectividades =
        // convertirSintomasAString(database.obtenerAfectividadesPorPacienteId(idPaciente));
        // paciente.setSintomasNegativosAfectividad(formatearTextoSintomas(afectividades));
        Afectividad[] afectividades = database.obtenerAfectividadesPorPacienteId(idPaciente);
        for (Afectividad afectividad : afectividades) {
            String nombreFormateado = formatearTextoSintomas(afectividad.getNombre());
            afectividad.setNombre(nombreFormateado);
        }
        paciente.getSintomasNegativos().setSintomasNegativosAfectividad(afectividades);
        // paciente.setSintomasPositivosTipoLenguaje(formatearTextoSintomas(lenguajes));

        /*
         * if (paciente != null && paciente.getFechaConsulta() != null) {
         * String fechaFormateada = formatearFecha(paciente.getFechaConsulta());
         * paciente.setFechaConsulta(fechaFormateada);
         * }
         */
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

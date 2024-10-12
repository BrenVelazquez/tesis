package com.sistexperto.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sistexperto.controller.PacienteDTO;
import com.sistexperto.model.Estudio;
import com.sistexperto.model.HistoriaClinica;
import com.sistexperto.model.ImagenesEntity;
import com.sistexperto.model.Medico;
import com.sistexperto.model.Paciente;
import com.sistexperto.model.SintomaAlucinacion;
import com.sistexperto.model.SintomaPositivo;

public class database {

    private static final String JDBC_URL = "jdbc:sqlserver://localhost:1433;databaseName=tesis;";
    private static final String JDBC_USER = "admin_tesis";
    private static final String JDBC_PASSWORD = "tesis2024";

    private static String sql;
    private static int idPaciente = -1;
    private static Boolean tieneEstudios = false;
    private static int idSintomaPositivo = -1;
    private static int idSintomaNegativo = -1;
    private static int idDiagnostico = -1;

    public static Boolean ingresarNuevoPaciente(Paciente paciente) {
        if (!insertPaciente(paciente))
            return false;
        if (!insertEstudios(paciente))
            return false;
        if (!insertHistoriaClinica(paciente))
            return false;
        if (!insertSintomasPositivos(paciente))
            return false;
        if (!insertSintomaAlucinaciones(paciente))
            return false;
        if (!insertSintomaLenguajes(paciente))
            return false;
        if (!insertSintomaPensamientos(paciente))
            return false;
        if (!insertSintomaContenidoPensamientos(paciente))
            return false;
        if (!insertSintomasNegativos(paciente))
            return false;
        if (!insertSintomaAspectos(paciente))
            return false;
        if (!insertSintomaAfectividades(paciente))
            return false;
        if (!insertDiagnostico(paciente))
            return false;
        if (!insertConsulta(paciente))
            return false;

        return true;
    }

    // region insert PACIENTES
    public static Boolean insertPaciente(Paciente paciente) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "INSERT INTO PACIENTES (NOMBRE, SEXO, EDAD) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, paciente.getNombre());
                preparedStatement.setString(2, convertirGuiones(paciente.getSexo()));
                preparedStatement.setInt(3, paciente.getEdad());
                preparedStatement.executeUpdate();
                idPaciente = obtenerUltimoId("PACIENTES", "ID_PACIENTE");
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // endregion insert PACIENTES

    // region LOGIN
    public static String login(String mail, String password) {

        System.out.println("database");
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "SELECT * FROM MEDICOS WHERE EMAIL= ? AND CONTRASEÑA = ?";
            System.out.println(mail);
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, mail);
                preparedStatement.setString(2, password);
                // preparedStatement.executeUpdate();
                ResultSet resultSet = preparedStatement.executeQuery();
                // ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    Medico medico = new Medico();
                    medico.setEmail(mail);
                    medico.setContraseña(password);
                    medico.setDni(resultSet.getInt("DNI"));
                    medico.setNombre(resultSet.getString("NOMBRE"));
                    medico.setApellido(resultSet.getString("APELLIDO"));
                    System.out.println("devuelve medico");
                    return medico.getNombre() + " " + medico.getApellido();
                } else {
                    return "";
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }
    // endregion LOGIN

    // region obtenerPacientes
    public static List<PacienteDTO> obtenerPacientes() {
        List<PacienteDTO> listaPacientes = new ArrayList<>();
        String sql = "SELECT p.ID_PACIENTE, p.NOMBRE, d.DIAGNOSTICO, d.ESTADO, c.FECHA " +
                "FROM PACIENTES p " +
                "JOIN CONSULTAS c ON p.ID_PACIENTE = c.ID_PACIENTE " +
                "JOIN DIAGNOSTICOS d ON c.ID_DIAGNOSTICO = d.ID_DIAGNOSTICO;";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                PacienteDTO paciente = new PacienteDTO();
                paciente.setIdPaciente(resultSet.getInt("ID_PACIENTE"));
                paciente.setNombre(resultSet.getString("nombre"));
                paciente.setDiagnostico(resultSet.getString("diagnostico"));
                paciente.setEstado(resultSet.getString("estado"));
                paciente.setFecha(resultSet.getDate("fecha").toString());
                listaPacientes.add(paciente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaPacientes;
    }
    // endregion obtenerPacientes

    // region obtenerPacientePorId
    public static Paciente obtenerPacientePorId(Integer idPaciente) {
        String sql = "SELECT p.ID_PACIENTE, p.NOMBRE, p.EDAD, p.SEXO, " +
                "h.TRASTORNO_AUTISTA, h.TRASTORNO_COMUNICACION, h.TRASTORNO_ESQUIZOAFECTIVO, " +
                "h.TRASTORNO_DEPRESIVO, h.BIPOLAR_CARAC_PSICOTICAS, h.ANTECEDENTES_FAMILIARES, h.SUSTANCIAS, " +
                "e.CAUSA_ORGANICA AS ESTUDIO_CAUSA_NATURAL, " +
                "e.COMENTARIO AS ESTUDIO_COMENTARIO, " +
                "sp.DURACION_POSITIVOS AS SINTOMAS_POSITIVOS_DURACION, " +
                "spr.NOMBRE AS RITMO_PENSAMIENTO, " +
                "sn.DURACION_NEGATIVOS AS SINTOMAS_NEGATIVOS_DURACION, " +
                "sn.BAJO_FUNCIONAMIENTO AS SINTOMAS_NEGATIVOS_BAJO_FUNCIONAMIENTO,  " +
                "sn.COMENTARIO_FUNCIONAMIENTO AS SINTOMAS_NEGATIVOS_BAJO_FUNCIONAMIENTO_COMENTARIO, " +
                "snac.NOMBRE AS ACTIVIDAD, " +
                "snat.NOMBRE AS ATENCION, " +
                "c.FECHA AS FECHA_CONSULTA, " +
                "d.DIAGNOSTICO, d.JUSTIFICACION, d.REGLAS, d.RECOMENDACION, " +
                "d.COMENTARIOS_MEDICOS, d.COMENTARIOS_RECHAZO, d.ESTADO, d.PUNTAJE, " +
                "m.NOMBRE AS NOMBRE_MEDICO, m.APELLIDO AS APELLIDO_MEDICO " +
                "FROM PACIENTES p  " +
                "LEFT JOIN HISTORIAS_CLINICAS h ON p.ID_PACIENTE = h.ID_PACIENTE " +
                "LEFT JOIN ESTUDIOS e ON h.ID_ESTUDIO = e.ID_ESTUDIO " +
                "LEFT JOIN SINTOMAS_POSITIVOS sp ON p.ID_PACIENTE = sp.ID_PACIENTE " +
                "LEFT JOIN RITMOS_PENSAMIENTOS spr ON sp.ID_RITMO_PENSAMIENTO = spr.ID_RITMO_PENSAMIENTO " +
                "LEFT JOIN SINTOMAS_NEGATIVOS sn ON p.ID_PACIENTE = sn.ID_PACIENTE " +
                "LEFT JOIN ACTIVIDADES snac ON sn.ID_ACTIVIDAD = snac.ID_ACTIVIDAD " +
                "LEFT JOIN ATENCIONES snat ON sN.ID_ATENCION = snat.ID_ATENCION " +
                "LEFT JOIN CONSULTAS c ON p.ID_PACIENTE = c.ID_PACIENTE " +
                "LEFT JOIN DIAGNOSTICOS d ON c.ID_DIAGNOSTICO = d.ID_DIAGNOSTICO " +
                "LEFT JOIN MEDICOS m ON c.ID_MEDICO = m.ID_MEDICO " +
                "WHERE p.ID_PACIENTE = ?";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idPaciente);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Paciente paciente = new Paciente();
                paciente.setIdPaciente(idPaciente);
                paciente.setNombre(resultSet.getString("NOMBRE"));
                paciente.setEdad(resultSet.getInt("EDAD"));
                paciente.setSexo(resultSet.getString("SEXO"));
                paciente.setTrastornoAutista(resultSet.getString("TRASTORNO_AUTISTA"));
                paciente.setTrastornoComunicacion(resultSet.getString("TRASTORNO_COMUNICACION"));
                paciente.setTrastornoEsquizoafectivo(resultSet.getString("TRASTORNO_ESQUIZOAFECTIVO"));
                paciente.setTrastornoDepresivo(resultSet.getString("TRASTORNO_DEPRESIVO"));
                paciente.setTrastornoBipolar(resultSet.getString("BIPOLAR_CARAC_PSICOTICAS"));
                paciente.setAntecedentesFamiliares(resultSet.getString("ANTECEDENTES_FAMILIARES"));
                paciente.setSustancias(resultSet.getString("SUSTANCIAS"));
                paciente.setEstudioCausaNatural(resultSet.getString("ESTUDIO_CAUSA_NATURAL"));
                paciente.setEstudioComentario(resultSet.getString("ESTUDIO_COMENTARIO"));
                paciente.setSintomasPositivosDuracion(resultSet.getString("SINTOMAS_POSITIVOS_DURACION"));
                paciente.setSintomasPositivosTipoRitmoPensamiento(resultSet.getString("RITMO_PENSAMIENTO"));
                paciente.setSintomasNegativosDuracion(resultSet.getString("SINTOMAS_NEGATIVOS_DURACION"));
                paciente.setSintomasNegativosAtencion(resultSet.getString("ATENCION"));
                paciente.setSintomasNegativosActividad(resultSet.getString("ACTIVIDAD"));
                paciente.setSintomasNegativosBajoFuncionamiento(
                        resultSet.getString("SINTOMAS_NEGATIVOS_BAJO_FUNCIONAMIENTO"));
                paciente.setSintomasNegativosBajoFuncionamientoComentario(
                        resultSet.getString("SINTOMAS_NEGATIVOS_BAJO_FUNCIONAMIENTO_COMENTARIO"));
                paciente.setDiagnostico(resultSet.getString("DIAGNOSTICO"));
                paciente.setJustificacion(resultSet.getString("JUSTIFICACION"));
                paciente.setReglas(resultSet.getString("REGLAS"));
                paciente.setRecomendacion(resultSet.getString("RECOMENDACION"));
                paciente.setComentarioMedico(resultSet.getString("COMENTARIOS_MEDICOS"));
                paciente.setJustificacionRechazo(resultSet.getString("COMENTARIOS_RECHAZO"));
                paciente.setEstado(resultSet.getString("ESTADO"));
                paciente.setPuntaje(resultSet.getInt("PUNTAJE"));
                paciente.setFechaConsulta(resultSet.getDate("FECHA_CONSULTA").toString());
                paciente.setNombreMedico(resultSet.getString("NOMBRE_MEDICO"));
                paciente.setApellidoMedico(resultSet.getString("APELLIDO_MEDICO"));
                return paciente;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    // endregion obtenerPacientePorId

    // region obtenerTodosLosPacientesConDetalles
    public static List<Paciente> obtenerTodosLosPacientesConDetalles() {
        List<Paciente> listaPacientes = new ArrayList<>();

        String sql = "SELECT p.ID_PACIENTE, p.NOMBRE, p.EDAD, p.SEXO, " +
                "h.TRASTORNO_AUTISTA, h.TRASTORNO_COMUNICACION, h.TRASTORNO_ESQUIZOAFECTIVO, " +
                "h.TRASTORNO_DEPRESIVO, h.BIPOLAR_CARAC_PSICOTICAS, h.ANTECEDENTES_FAMILIARES, h.SUSTANCIAS, " +
                "e.CAUSA_ORGANICA AS ESTUDIO_CAUSA_NATURAL, " +
                "e.COMENTARIO AS ESTUDIO_COMENTARIO, " +
                "sp.DURACION_POSITIVOS AS SINTOMAS_POSITIVOS_DURACION, " +
                "spr.NOMBRE AS RITMO_PENSAMIENTO, " +
                "sn.DURACION_NEGATIVOS AS SINTOMAS_NEGATIVOS_DURACION, " +
                "sn.BAJO_FUNCIONAMIENTO AS SINTOMAS_NEGATIVOS_BAJO_FUNCIONAMIENTO,  " +
                "sn.COMENTARIO_FUNCIONAMIENTO AS SINTOMAS_NEGATIVOS_BAJO_FUNCIONAMIENTO_COMENTARIO, " +
                "snac.NOMBRE AS ACTIVIDAD, " +
                "snat.NOMBRE AS ATENCION, " +
                "c.FECHA AS FECHA_CONSULTA, " +
                "d.DIAGNOSTICO, d.JUSTIFICACION, d.REGLAS, d.RECOMENDACION, " +
                "d.COMENTARIOS_MEDICOS, d.COMENTARIOS_RECHAZO, d.ESTADO, d.PUNTAJE, " +
                "m.NOMBRE AS NOMBRE_MEDICO, m.APELLIDO AS APELLIDO_MEDICO " +
                "FROM PACIENTES p  " +
                "LEFT JOIN HISTORIAS_CLINICAS h ON p.ID_PACIENTE = h.ID_PACIENTE " +
                "LEFT JOIN ESTUDIOS e ON h.ID_ESTUDIO = e.ID_ESTUDIO " +
                "LEFT JOIN SINTOMAS_POSITIVOS sp ON p.ID_PACIENTE = sp.ID_PACIENTE " +
                "LEFT JOIN RITMOS_PENSAMIENTOS spr ON sp.ID_RITMO_PENSAMIENTO = spr.ID_RITMO_PENSAMIENTO " +
                "LEFT JOIN SINTOMAS_NEGATIVOS sn ON p.ID_PACIENTE = sn.ID_PACIENTE " +
                "LEFT JOIN ACTIVIDADES snac ON sn.ID_ACTIVIDAD = snac.ID_ACTIVIDAD " +
                "LEFT JOIN ATENCIONES snat ON sN.ID_ATENCION = snat.ID_ATENCION " +
                "LEFT JOIN CONSULTAS c ON p.ID_PACIENTE = c.ID_PACIENTE " +
                "LEFT JOIN DIAGNOSTICOS d ON c.ID_DIAGNOSTICO = d.ID_DIAGNOSTICO " +
                "LEFT JOIN MEDICOS m ON c.ID_MEDICO = m.ID_MEDICO";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Paciente paciente = new Paciente();
                paciente.setIdPaciente(resultSet.getInt("ID_PACIENTE"));
                paciente.setNombre(resultSet.getString("NOMBRE"));
                paciente.setEdad(resultSet.getInt("EDAD"));
                paciente.setSexo(resultSet.getString("SEXO"));
                paciente.setTrastornoAutista(resultSet.getString("TRASTORNO_AUTISTA"));
                paciente.setTrastornoComunicacion(resultSet.getString("TRASTORNO_COMUNICACION"));
                paciente.setTrastornoEsquizoafectivo(resultSet.getString("TRASTORNO_ESQUIZOAFECTIVO"));
                paciente.setTrastornoDepresivo(resultSet.getString("TRASTORNO_DEPRESIVO"));
                paciente.setTrastornoBipolar(resultSet.getString("BIPOLAR_CARAC_PSICOTICAS"));
                paciente.setAntecedentesFamiliares(resultSet.getString("ANTECEDENTES_FAMILIARES"));
                paciente.setSustancias(resultSet.getString("SUSTANCIAS"));
                paciente.setEstudioCausaNatural(resultSet.getString("ESTUDIO_CAUSA_NATURAL"));
                paciente.setEstudioComentario(resultSet.getString("ESTUDIO_COMENTARIO"));
                paciente.setSintomasPositivosDuracion(resultSet.getString("SINTOMAS_POSITIVOS_DURACION"));
                paciente.setSintomasPositivosTipoRitmoPensamiento(resultSet.getString("RITMO_PENSAMIENTO"));
                paciente.setSintomasNegativosDuracion(resultSet.getString("SINTOMAS_NEGATIVOS_DURACION"));
                paciente.setSintomasNegativosAtencion(resultSet.getString("ATENCION"));
                paciente.setSintomasNegativosActividad(resultSet.getString("ACTIVIDAD"));
                paciente.setSintomasNegativosBajoFuncionamiento(
                        resultSet.getString("SINTOMAS_NEGATIVOS_BAJO_FUNCIONAMIENTO"));
                paciente.setSintomasNegativosBajoFuncionamientoComentario(
                        resultSet.getString("SINTOMAS_NEGATIVOS_BAJO_FUNCIONAMIENTO_COMENTARIO"));
                paciente.setDiagnostico(resultSet.getString("DIAGNOSTICO"));
                paciente.setJustificacion(resultSet.getString("JUSTIFICACION"));
                paciente.setReglas(resultSet.getString("REGLAS"));
                paciente.setRecomendacion(resultSet.getString("RECOMENDACION"));
                paciente.setComentarioMedico(resultSet.getString("COMENTARIOS_MEDICOS"));
                paciente.setJustificacionRechazo(resultSet.getString("COMENTARIOS_RECHAZO"));
                paciente.setEstado(resultSet.getString("ESTADO"));
                paciente.setPuntaje(resultSet.getInt("PUNTAJE"));
                paciente.setFechaConsulta(resultSet.getDate("FECHA_CONSULTA").toString());
                paciente.setNombreMedico(resultSet.getString("NOMBRE_MEDICO"));
                paciente.setApellidoMedico(resultSet.getString("APELLIDO_MEDICO"));

                listaPacientes.add(paciente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaPacientes;
    }
    // endregion obtenerTodosLosPacientesConDetalles

    // region insert ESTUDIOS
    public static Boolean insertEstudios(Paciente paciente) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {

            tieneEstudios = "Si".equals(paciente.getEstudios());
            if (tieneEstudios) {
                // TODO: AGREGAR IMAGEN
                // sql = "INSERT INTO ESTUDIOS (CAUSA_ORGANICA, COMENTARIO, IMAGEN_PATH)" +
                String sql = "INSERT INTO ESTUDIOS (CAUSA_ORGANICA, COMENTARIO) VALUES (?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, paciente.getEstudioCausaNatural());
                    preparedStatement.setNString(2, paciente.getEstudioComentario());
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // endregion insert ESTUDIOS

    // region insert HISTORIAS_CLINICAS
    public static Boolean insertHistoriaClinica(Paciente paciente) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            if (tieneEstudios) {
                sql = "INSERT INTO HISTORIAS_CLINICAS (TRASTORNO_AUTISTA, TRASTORNO_COMUNICACION, TRASTORNO_ESQUIZOAFECTIVO, "
                        +
                        "BIPOLAR_CARAC_PSICOTICAS, TRASTORNO_DEPRESIVO, ANTECEDENTES_FAMILIARES, SUSTANCIAS, ID_PACIENTE, ID_ESTUDIO) "
                        +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            } else {
                sql = "INSERT INTO HISTORIAS_CLINICAS (TRASTORNO_AUTISTA, TRASTORNO_COMUNICACION, TRASTORNO_ESQUIZOAFECTIVO, "
                        +
                        "BIPOLAR_CARAC_PSICOTICAS, TRASTORNO_DEPRESIVO, ANTECEDENTES_FAMILIARES, SUSTANCIAS, ID_PACIENTE) "
                        +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, convertirABit(paciente.getTrastornoAutista()));
                preparedStatement.setInt(2, convertirABit(paciente.getTrastornoComunicacion()));
                preparedStatement.setInt(3, convertirABit(paciente.getTrastornoEsquizoafectivo()));
                preparedStatement.setInt(4, convertirABit(paciente.getTrastornoBipolar()));
                preparedStatement.setInt(5, convertirABit(paciente.getTrastornoDepresivo()));
                preparedStatement.setInt(6, convertirABit(paciente.getAntecedentesFamiliares()));
                preparedStatement.setInt(7, convertirABit(paciente.getSustancias()));
                preparedStatement.setInt(8, idPaciente);
                if (tieneEstudios) {
                    int idEstudio = obtenerUltimoId("ESTUDIOS", "ID_ESTUDIO");
                    preparedStatement.setInt(9, idEstudio);
                }
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // endregion insert HISTORIAS_CLINICAS

    // region insert SINTOMAS_POSITIVOS
    public static Boolean insertSintomasPositivos(Paciente paciente) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String ritmoPensamiento = paciente.getSintomasPositivosTipoRitmoPensamiento();
            int idRitmoPensamiento = obtenerIdPorNombre("RITMOS_PENSAMIENTOS", convertirGuiones(ritmoPensamiento),
                    "ID_RITMO_PENSAMIENTO");
            sql = "INSERT INTO SINTOMAS_POSITIVOS (ID_PACIENTE, ID_RITMO_PENSAMIENTO, DURACION_POSITIVOS) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, idPaciente);
                preparedStatement.setInt(2, idRitmoPensamiento);
                preparedStatement.setNString(3, paciente.getSintomasPositivosDuracion());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            idSintomaPositivo = obtenerUltimoId("SINTOMAS_POSITIVOS", "ID_SINTOMA_POSITIVO");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // endregion insert SINTOMAS_POSITIVOS

    // region insert SINTOMA_ALUCINACIONES
    public static Boolean insertSintomaAlucinaciones(Paciente paciente) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String alucinaciones = paciente.getSintomasPositivosAlucinaciones();
            Boolean tieneAlucinaciones = "Si".equals(alucinaciones);
            Boolean noTieneAlucinaciones = "No".equals(alucinaciones);
            sql = "INSERT INTO SINTOMA_ALUCINACIONES (ID_ALUCINACION, ID_SINTOMA_POSITIVO) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                if (tieneAlucinaciones) {
                    String tiposAlucinaciones = paciente.getSintomasPositivosTipoAlucinaciones();
                    String[] tiposAlucinacionesArray = tiposAlucinaciones.split(",");
                    for (String tipoAlucinacion : tiposAlucinacionesArray) {
                        int idAlucinacion = obtenerIdPorNombre("ALUCINACIONES",
                                convertirGuiones(tipoAlucinacion.trim()),
                                "ID_ALUCINACION");
                        preparedStatement.setInt(1, idAlucinacion);
                        preparedStatement.setInt(2, idSintomaPositivo);
                        preparedStatement.executeUpdate();
                    }
                } else {
                    int idAlucinacion = -1;
                    if (noTieneAlucinaciones) {
                        idAlucinacion = obtenerIdPorNombre("ALUCINACIONES", "NO_PRESENTA",
                                "ID_ALUCINACION");
                    } else {
                        // NO_SE_DESCARTA
                        idAlucinacion = obtenerIdPorNombre("ALUCINACIONES",
                                convertirGuiones(alucinaciones),
                                "ID_ALUCINACION");
                    }
                    preparedStatement.setInt(1, idAlucinacion);
                    preparedStatement.setInt(2, idSintomaPositivo);
                    preparedStatement.executeUpdate();

                }
                return true;

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // endregion insert SINTOMA_ALUCINACIONES

    // region insert SINTOMA_LENGUAJES
    public static Boolean insertSintomaLenguajes(Paciente paciente) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            sql = "INSERT INTO SINTOMA_LENGUAJES (ID_LENGUAJE, ID_SINTOMA_POSITIVO) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                String tiposLenguajes = paciente.getSintomasPositivosTipoLenguaje();
                String[] tiposLenguajesArray = tiposLenguajes.split(",");
                for (String tipoLenguaje : tiposLenguajesArray) {
                    int idLenguaje = obtenerIdPorNombre("LENGUAJES", convertirGuiones(tipoLenguaje.trim()),
                            "ID_LENGUAJE");
                    preparedStatement.setInt(1, idLenguaje);
                    preparedStatement.setInt(2, idSintomaPositivo);
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // endregion insert SINTOMA_LENGUAJES

    // region insert SINTOMA_PENSAMIENTOS
    public static Boolean insertSintomaPensamientos(Paciente paciente) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            sql = "INSERT INTO SINTOMA_PENSAMIENTOS (ID_PENSAMIENTO, ID_SINTOMA_POSITIVO) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                String tiposPensamientos = paciente.getSintomasPositivosTipoPensamiento();
                String[] tiposPensamientosArray = tiposPensamientos.split(",");
                for (String tipoPensamiento : tiposPensamientosArray) {
                    int idPensamiento = obtenerIdPorNombre("PENSAMIENTOS",
                            convertirGuiones(tipoPensamiento.trim()),
                            "ID_PENSAMIENTO");
                    preparedStatement.setInt(1, idPensamiento);
                    preparedStatement.setInt(2, idSintomaPositivo);
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // endregion insert SINTOMA_PENSAMIENTOS

    // region insert SINTOMA_CONTENIDO_PENSAMIENTOS
    public static Boolean insertSintomaContenidoPensamientos(Paciente paciente) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            sql = "INSERT INTO SINTOMA_CONTENIDO_PENSAMIENTOS (ID_CONTENIDO_PENSAMIENTO, ID_SINTOMA_POSITIVO) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                String tiposContenidosPensamientos = paciente.getSintomasPositivosTipoContenidoPensamiento();
                String[] tiposContenidosPensamientosArray = tiposContenidosPensamientos.split(",");
                for (String tipoContenidoPensamiento : tiposContenidosPensamientosArray) {
                    int idContenidoPensamiento = obtenerIdPorNombre("CONTENIDO_PENSAMIENTOS",
                            convertirGuiones(tipoContenidoPensamiento.trim()),
                            "ID_CONTENIDO_PENSAMIENTO");
                    preparedStatement.setInt(1, idContenidoPensamiento);
                    preparedStatement.setInt(2, idSintomaPositivo);
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // endregion insert SINTOMA_CONTENIDO_PENSAMIENTOS

    // region insert SINTOMAS_NEGATIVOS
    public static Boolean insertSintomasNegativos(Paciente paciente) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String atencion = paciente.getSintomasNegativosAtencion();
            int idAtencion = obtenerIdPorNombre("ATENCIONES", convertirGuiones(atencion), "ID_ATENCION");
            String actividad = paciente.getSintomasNegativosActividad();
            int idActividad = obtenerIdPorNombre("ACTIVIDADES", convertirGuiones(actividad), "ID_ACTIVIDAD");
            sql = "INSERT INTO SINTOMAS_NEGATIVOS (ID_PACIENTE, ID_ATENCION, ID_ACTIVIDAD, BAJO_FUNCIONAMIENTO, " +
                    "COMENTARIO_FUNCIONAMIENTO, DURACION_NEGATIVOS) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, idPaciente);
                preparedStatement.setInt(2, idAtencion);
                preparedStatement.setInt(3, idActividad);
                preparedStatement.setInt(4, convertirABit(paciente.getSintomasNegativosBajoFuncionamiento()));
                preparedStatement.setNString(5, paciente.getSintomasNegativosBajoFuncionamientoComentario());
                preparedStatement.setNString(6, paciente.getSintomasNegativosDuracion());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            idSintomaNegativo = obtenerUltimoId("SINTOMAS_NEGATIVOS", "ID_SINTOMA_NEGATIVO");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // endregion insert SINTOMAS_NEGATIVOS

    // region insert SINTOMA_ASPECTOS
    public static Boolean insertSintomaAspectos(Paciente paciente) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            sql = "INSERT INTO SINTOMA_ASPECTOS (ID_ASPECTO, ID_SINTOMA_NEGATIVO) VALUES(?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                String tiposAspectos = paciente.getSintomasNegativosAspecto();
                String[] tiposAspectosArray = tiposAspectos.split(",");
                for (String tipoAspecto : tiposAspectosArray) {
                    int idAspecto = obtenerIdPorNombre("ASPECTOS", convertirGuiones(tipoAspecto.trim()), "ID_ASPECTO");
                    preparedStatement.setInt(1, idAspecto);
                    preparedStatement.setInt(2, idSintomaNegativo);
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // endregion insert SINTOMA_ASPECTOS

    // region insert SINTOMA_AFECTIVIDADES
    public static Boolean insertSintomaAfectividades(Paciente paciente) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            sql = "INSERT INTO SINTOMA_AFECTIVIDADES (ID_AFECTIVIDAD,             ID_SINTOMA_NEGATIVO) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                String tiposAfectividades = paciente.getSintomasNegativosAfectividad();
                String[] tiposAfectividadesArray = tiposAfectividades.split(",");
                for (String tipoAfectividad : tiposAfectividadesArray) {
                    int idAfectividad = obtenerIdPorNombre("AFECTIVIDADES", convertirGuiones(tipoAfectividad.trim()),
                            "ID_AFECTIVIDAD");
                    preparedStatement.setInt(1, idAfectividad);
                    preparedStatement.setInt(2, idSintomaNegativo);
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // endregion insert SINTOMA_AFECTIVIDADES

    // region insert DIAGNOSTICOS
    public static Boolean insertDiagnostico(Paciente paciente) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "INSERT INTO DIAGNOSTICOS (DIAGNOSTICO, ESTADO, COMENTARIOS_RECHAZO, JUSTIFICACION, "
                    + "REGLAS, RECOMENDACION, PUNTAJE, COMENTARIOS_MEDICOS) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            int estado = paciente.getEstado().equalsIgnoreCase("Confirmado") ? 1 : 0;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setNString(1, paciente.getDiagnostico());
                preparedStatement.setInt(2, estado);
                preparedStatement.setNString(3, paciente.getJustificacionRechazo());
                preparedStatement.setNString(4, paciente.getJustificacion());
                preparedStatement.setNString(5, paciente.getReglas());
                preparedStatement.setNString(6, paciente.getRecomendacion());
                preparedStatement.setInt(7, paciente.getPuntaje());
                preparedStatement.setNString(8, paciente.getComentarioMedico());
                preparedStatement.executeUpdate();
                idDiagnostico = obtenerUltimoId("DIAGNOSTICOS", "ID_DIAGNOSTICO");
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // endregion insert DIAGNOSTICOS

    // region insert CONSULTAS
    public static Boolean insertConsulta(Paciente paciente) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "INSERT INTO CONSULTAS (ID_MEDICO, ID_PACIENTE, FECHA, ID_DIAGNOSTICO) VALUES (?, ?, ?, ?)";
            Date todayDate = new Date(Calendar.getInstance().getTimeInMillis());
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, 1);
                preparedStatement.setInt(2, idPaciente);
                preparedStatement.setDate(3, todayDate);
                preparedStatement.setInt(4, idDiagnostico);
                preparedStatement.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // endregion insert CONSULTAS

    // region select ESTUDIOS
    public static Estudio getEstudios(Integer idEstudio) {
        Estudio estudios = new Estudio();
        String sql = "SELECT * FROM ESTUDIOS";
        if (idEstudio != null) {
            sql += " WHERE ID_ESTUDIO = ?";
        }
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            if (idEstudio != null) {
                preparedStatement.setInt(1, idEstudio);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            Estudio estudio = new Estudio();
            estudio.setIdEstudio(resultSet.getInt("ID_ESTUDIO"));
            estudio.setEstudioCausaNatural(resultSet.getString("CAUSA_ORGANICA"));
            estudio.setEstudioComentario(resultSet.getString("COMENTARIO"));
            // TODO: FALTA AGREGAR LA IMAGEN
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estudios;
    }
    // endregion select ESTUDIOS

    // region select HISTORIAS_CLINICAS
    public static List<HistoriaClinica> getHistoriasClinicas(Integer idPaciente) {
        List<HistoriaClinica> historias = new ArrayList<>();
        String sql = "SELECT * FROM HISTORIAS_CLINICAS";
        if (idPaciente != null) {
            sql += " WHERE ID_PACIENTE = ?";
        }
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            if (idPaciente != null) {
                preparedStatement.setInt(1, idPaciente);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                HistoriaClinica historia = new HistoriaClinica();
                historia.setIdHistoriaClinica(resultSet.getInt("ID_HISTORIA_CLINICA"));
                historia.setTrastornoAutista(convertBooleanToString(resultSet.getBoolean("TRASTORNO_AUTISTA")));
                historia.setTrastornoComunicacion(
                        convertBooleanToString(resultSet.getBoolean("TRASTORNO_COMUNICACION")));
                historia.setTrastornoEsquizoafectivo(
                        convertBooleanToString(resultSet.getBoolean("TRASTORNO_ESQUIZOAFECTIVO")));
                historia.setTrastornoBipolar(convertBooleanToString(resultSet.getBoolean("BIPOLAR_CARAC_PSICOTICAS")));
                historia.setTrastornoDepresivo(convertBooleanToString(resultSet.getBoolean("TRASTORNO_DEPRESIVO")));
                historia.setAntecedentesFamiliares(
                        convertBooleanToString(resultSet.getBoolean("ANTECEDENTES_FAMILIARES")));
                historia.setSustancias(convertBooleanToString(resultSet.getBoolean("SUSTANCIAS")));
                int idEstudio = resultSet.getInt("ID_ESTUDIO");
                Estudio estudio = getEstudios(idEstudio);
                historia.setEstudio(estudio);
                historias.add(historia);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historias;
    }
    // endregion select HISTORIAS_CLINICAS

    // region select SINTOMAS_POSITIVOS
    public static List<SintomaPositivo> getSintomasPositivos(Integer idPaciente) {
        List<SintomaPositivo> sintomasPositivos = new ArrayList<>();
        String sql = "SELECT * FROM SINTOMAS_POSITIVOS";
        if (idPaciente != null) {
            sql += " WHERE ID_PACIENTE = ?";
        }
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            if (idPaciente != null) {
                preparedStatement.setInt(1, idPaciente);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                SintomaPositivo sintoma = new SintomaPositivo();
                sintoma.setIdSintomaPositivo(resultSet.getInt("ID_SINTOMA_POSITIVO"));
                sintoma.setSintomasPositivosTipoRitmoPensamiento(
                        obtenerNombrePorId("RITMOS_PENSAMIENTOS", resultSet.getInt("ID_RITMO_PENSAMIENTO")));
                sintoma.setSintomasPositivosDuracion(resultSet.getString("DURACION_POSITIVOS"));
                sintomasPositivos.add(sintoma);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sintomasPositivos;
    }
    // endregion select SINTOMAS_POSITIVOS

    // region select SINTOMA_ALUCINACIONES
    public static List<SintomaAlucinacion> getSintomaAlucinaciones(Integer idSintomaPositivo) {
        List<SintomaAlucinacion> alucinaciones = new ArrayList<>();
        String sql = "SELECT * FROM SINTOMA_ALUCINACIONES";
        if (idSintomaPositivo != null) {
            sql += " WHERE ID_SINTOMA_POSITIVO = ?";
        }
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            if (idSintomaPositivo != null) {
                preparedStatement.setInt(1, idSintomaPositivo);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            Map<Integer, List<String>> sintomasAlucinacionesMap = new HashMap<>();
            while (resultSet.next()) {
                SintomaAlucinacion alucinacion = new SintomaAlucinacion();
                int idAlucinacion = resultSet.getInt("ID_ALUCINACION");
                alucinacion.setIdSintomaPositivo(resultSet.getInt("ID_SINTOMA_POSITIVO"));
                alucinacion.setIdAlucinacion(idAlucinacion);
                String nombreAlucinacion = obtenerNombrePorId("ALUCINACIONES", idAlucinacion);
                sintomasAlucinacionesMap.get(idSintomaPositivo).add(nombreAlucinacion);
            }
            for (Map.Entry<Integer, List<String>> entry : sintomasAlucinacionesMap.entrySet()) {
                SintomaAlucinacion alucinacion = new SintomaAlucinacion();
                alucinacion.setIdSintomaPositivo(entry.getKey());
                alucinacion.setTipoAlucinaciones(entry.getValue());
                alucinaciones.add(alucinacion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alucinaciones;
    }
    // endregion select SINTOMA_ALUCINACIONES

    // region select ALUCINACIONES
    public static List<String> obtenerAlucinacionesPorSintoma(int idPaciente) {
        String sql = "SELECT a.NOMBRE AS TIPOS_ALUCINACIONES " +
                "FROM PACIENTES p " +
                "LEFT JOIN SINTOMAS_POSITIVOS sp ON p.ID_PACIENTE = sp.ID_PACIENTE " +
                "LEFT JOIN SINTOMA_ALUCINACIONES sa ON sp.ID_SINTOMA_POSITIVO = sa.ID_SINTOMA_POSITIVO " +
                "LEFT JOIN ALUCINACIONES a ON sa.ID_ALUCINACION = a.ID_ALUCINACION " +
                "WHERE p.ID_PACIENTE = ?";
        List<String> alucinaciones = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idPaciente);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                alucinaciones.add(resultSet.getString("TIPOS_ALUCINACIONES"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alucinaciones;
    }
    // endregion select ALUCINACIONES

    // region select LENGUAJES
    public static List<String> obtenerLenguajesPorPacienteId(int idPaciente) {
        String sql = "SELECT l.NOMBRE AS TIPOS_LENGUAJES " +
                "FROM PACIENTES p " +
                "LEFT JOIN SINTOMAS_POSITIVOS sp ON p.ID_PACIENTE = sp.ID_PACIENTE " +
                "LEFT JOIN SINTOMA_LENGUAJES sl ON sp.ID_SINTOMA_POSITIVO = sl.ID_SINTOMA_POSITIVO " +
                "LEFT JOIN LENGUAJES l ON sl.ID_LENGUAJE = l.ID_LENGUAJE " +
                "WHERE p.ID_PACIENTE = ?";
        List<String> lenguajes = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idPaciente);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                lenguajes.add(resultSet.getString("TIPOS_LENGUAJES"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lenguajes;
    }
    // endregion select LENGUAJES

    // region select PENSAMIENTOS
    public static List<String> obtenerPensamientosPorPacienteId(int idPaciente) {
        String sql = "SELECT pe.NOMBRE AS TIPOS_PENSAMIENTOS " +
                "FROM PACIENTES p " +
                "LEFT JOIN SINTOMAS_POSITIVOS sp ON p.ID_PACIENTE = sp.ID_PACIENTE " +
                "LEFT JOIN SINTOMA_PENSAMIENTOS sl ON sp.ID_SINTOMA_POSITIVO = sl.ID_SINTOMA_POSITIVO " +
                "LEFT JOIN PENSAMIENTOS pe ON sl.ID_PENSAMIENTO = pe.ID_PENSAMIENTO " +
                "WHERE p.ID_PACIENTE = ?";
        List<String> pensamientos = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idPaciente);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                pensamientos.add(resultSet.getString("TIPOS_PENSAMIENTOS"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pensamientos;
    }
    // endregion select PENSAMIENTOS

    // region select CONTENIDO_PENSAMIENTOS
    public static List<String> obtenerContenidosPensamientosPorPacienteId(int idPaciente) {
        String sql = "SELECT cp.NOMBRE AS TIPOS_CONTENIDOS_PENSAMIENTOS " +
                "FROM PACIENTES p " +
                "LEFT JOIN SINTOMAS_POSITIVOS sp ON p.ID_PACIENTE = sp.ID_PACIENTE " +
                "LEFT JOIN SINTOMA_CONTENIDO_PENSAMIENTOS sl ON sp.ID_SINTOMA_POSITIVO = sl.ID_SINTOMA_POSITIVO " +
                "LEFT JOIN CONTENIDO_PENSAMIENTOS cp ON sl.ID_CONTENIDO_PENSAMIENTO = cp.ID_CONTENIDO_PENSAMIENTO " +
                "WHERE p.ID_PACIENTE = ?";
        List<String> contenidosPensamientos = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idPaciente);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                contenidosPensamientos.add(resultSet.getString("TIPOS_CONTENIDOS_PENSAMIENTOS"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contenidosPensamientos;
    }
    // endregion select CONTENIDO_PENSAMIENTOS

    // region select ASPECTOS
    public static List<String> obtenerAspectosPorPacienteId(int idPaciente) {
        String sql = "SELECT a.NOMBRE AS TIPOS_ASPECTOS " +
                "FROM PACIENTES p " +
                "LEFT JOIN SINTOMAS_NEGATIVOS sp ON p.ID_PACIENTE = sp.ID_PACIENTE " +
                "LEFT JOIN SINTOMA_ASPECTOS sl ON sp.ID_SINTOMA_NEGATIVO = sl.ID_SINTOMA_NEGATIVO " +
                "LEFT JOIN ASPECTOS a ON sl.ID_ASPECTO = a.ID_ASPECTO " +
                "WHERE p.ID_PACIENTE = ?";
        List<String> aspectos = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idPaciente);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                aspectos.add(resultSet.getString("TIPOS_ASPECTOS"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aspectos;
    }
    // endregion select ASPECTOS

    // region select AFECTIVIDADES
    public static List<String> obtenerAfectividadesPorPacienteId(int idPaciente) {
        String sql = "SELECT a.NOMBRE AS TIPOS_AFECTIVIDADES " +
                "FROM PACIENTES p " +
                "LEFT JOIN SINTOMAS_NEGATIVOS sp ON p.ID_PACIENTE = sp.ID_PACIENTE  " +
                "LEFT JOIN SINTOMA_AFECTIVIDADES sl ON sp.ID_SINTOMA_NEGATIVO = sl.ID_SINTOMA_NEGATIVO " +
                "LEFT JOIN AFECTIVIDADES a ON sl.ID_AFECTIVIDAD = a.ID_AFECTIVIDAD " +
                "WHERE p.ID_PACIENTE = ?";
        List<String> afectividades = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idPaciente);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                afectividades.add(resultSet.getString("TIPOS_AFECTIVIDADES"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return afectividades;
    }
    // endregion select AFECTIVIDADES

    // region obtenerIdPorNombre
    private static int obtenerIdPorNombre(String tabla, String nombre, String columnaID) {
        int id = -1;
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "SELECT " + columnaID + " FROM " + tabla + " WHERE NOMBRE = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, nombre);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    id = resultSet.getInt(columnaID);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
    // endregion obtenerIdPorNombre

    // region obtenerNombrePorId
    private static String obtenerNombrePorId(String tabla, int columnaID) {
        String nombre = null;
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String columnaIDString = String.valueOf(columnaID);
            String sql = "SELECT NOMBRE FROM " + tabla + " WHERE " + columnaIDString + " = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, columnaID);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    nombre = resultSet.getString("NOMBRE");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombre;
    }
    // endregion obtenerNombrePorId

    // region obtenerUltimoId
    public static int obtenerUltimoId(String nombreTabla, String nombreCampoId) {
        int ultimoId = -1;
        String sql = "SELECT TOP 1 " + nombreCampoId + " FROM " + nombreTabla + " ORDER BY " + nombreCampoId + " DESC";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                ultimoId = resultSet.getInt(nombreCampoId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ultimoId;
    }
    // endregion obtenerUltimoId

    // region converciones
    public static String convertirGuiones(String texto) {
        if (texto == null) {
            return null;
        }
        return texto.replace("-", "_").replace(" ", "_").toUpperCase();
    }

    private static int convertirABit(String valor) {
        return valor.equalsIgnoreCase("Si") ? 1 : 0;
    }

    public static String convertBooleanToString(Boolean yesNo) {
        return yesNo ? "Sí" : "No";
    }
    // endregion converciones

}

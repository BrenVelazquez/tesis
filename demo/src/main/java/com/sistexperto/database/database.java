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
import com.sistexperto.model.Consulta;
import com.sistexperto.model.ImagenesEntity;
import com.sistexperto.model.Actividad;
import com.sistexperto.model.Alucinacion;
import com.sistexperto.model.Atencion;
import com.sistexperto.model.ContenidoPensamiento;
import com.sistexperto.model.Pensamiento;
import com.sistexperto.model.Lenguaje;
import com.sistexperto.model.Medico;
import com.sistexperto.model.Paciente;
import com.sistexperto.model.RitmoPensamiento;
import com.sistexperto.model.Estudio;
import com.sistexperto.model.Aspecto;
import com.sistexperto.model.Diagnostico;
import com.sistexperto.model.Afectividad;
import com.sistexperto.model.HistoriaClinica;
import com.sistexperto.model.SintomaPositivo;
import com.sistexperto.model.SintomaNegativo;

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

    public static Boolean ingresarNuevoPaciente(/* Paciente paciente, int idMedico */ Consulta consulta) {
        if (!insertPaciente(consulta.getPaciente()))
            return false;
        if (!insertEstudios(consulta.getPaciente().getHistoriaClinica().getEstudio()))

            return false;
        if (!insertHistoriaClinica(consulta.getPaciente().getHistoriaClinica()))
            return false;
        if (!insertSintomasPositivos(consulta.getPaciente().getSintomasPositivos()))
            return false;
        if (!insertSintomaAlucinaciones(
                consulta.getPaciente().getSintomasPositivos().getSintomasPositivosAlucinaciones()))
            return false;
        if (!insertSintomaLenguajes(consulta.getPaciente().getSintomasPositivos().getSintomasPositivosTipoLenguaje()))
            return false;
        if (!insertSintomaPensamientos(
                consulta.getPaciente().getSintomasPositivos().getSintomasPositivosTipoPensamiento()))
            return false;
        if (!insertSintomaContenidoPensamientos(
                consulta.getPaciente().getSintomasPositivos().getSintomasPositivosTipoContenidoPensamiento()))
            return false;
        if (!insertSintomasNegativos(consulta.getPaciente().getSintomasNegativos()))
            return false;
        if (!insertSintomaAspectos(consulta.getPaciente().getSintomasNegativos().getSintomasNegativosAspecto()))
            return false;
        if (!insertSintomaAfectividades(
                consulta.getPaciente().getSintomasNegativos().getSintomasNegativosAfectividad()))
            return false;
        if (!insertDiagnostico(consulta.getDiagnostico()))
            return false;
        if (!insertConsulta(consulta))
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
    public static Medico login(String mail, String password) {

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "SELECT * FROM MEDICOS WHERE EMAIL= ? AND CONTRASEÑA = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, mail);
                preparedStatement.setString(2, password);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    Medico medico = new Medico();
                    medico.setId(resultSet.getInt("ID_MEDICO"));
                    medico.setEmail(mail);
                    medico.setContraseña(password);
                    medico.setDni(resultSet.getInt("DNI"));
                    medico.setNombreMedico(resultSet.getString("NOMBRE"));
                    medico.setApellidoMedico(resultSet.getString("APELLIDO"));
                    return medico;
                } else {
                    return null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    // endregion LOGIN

    // region obtenerPacientes
    public static List<PacienteDTO> obtenerPacientes() {
        List<PacienteDTO> listaPacientes = new ArrayList<>();
        String sql = "SELECT p.ID_PACIENTE, p.NOMBRE, d.DIAGNOSTICO, d.ESTADO, c.FECHA, m.NOMBRE AS NOMBRE_MEDICO, " +
                "m.APELLIDO AS APELLIDO_MEDICO " +
                "FROM PACIENTES p " +
                "JOIN CONSULTAS c ON p.ID_PACIENTE = c.ID_PACIENTE " +
                "JOIN DIAGNOSTICOS d ON c.ID_DIAGNOSTICO = d.ID_DIAGNOSTICO " +
                "JOIN MEDICOS m ON c.ID_MEDICO = m.ID_MEDICO " +
                "ORDER BY p.ID_PACIENTE DESC;";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                PacienteDTO paciente = new PacienteDTO();
                paciente.setIdPaciente(resultSet.getInt("ID_PACIENTE"));
                paciente.setNombre(resultSet.getString("NOMBRE"));
                paciente.setDiagnostico(resultSet.getString("DIAGNOSTICO"));
                paciente.setEstado(resultSet.getString("ESTADO"));
                paciente.setNombreMedico(
                        resultSet.getString("NOMBRE_MEDICO") + " " + resultSet.getString("APELLIDO_MEDICO"));
                paciente.setFecha(resultSet.getDate("FECHA").toString());
                listaPacientes.add(paciente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaPacientes;
    }
    // endregion obtenerPacientes

    // region obtenerPacientePorId
    public static Consulta obtenerConsultaPorPaciente(Integer idPaciente) {
        String sql = "SELECT p.ID_PACIENTE, p.NOMBRE, p.EDAD, p.SEXO, " +
                "h.TRASTORNO_AUTISTA, h.TRASTORNO_COMUNICACION, h.TRASTORNO_ESQUIZOAFECTIVO, " +
                "h.TRASTORNO_DEPRESIVO, h.BIPOLAR_CARAC_PSICOTICAS, h.ANTECEDENTES_FAMILIARES, h.SUSTANCIAS, " +
                "e.CAUSA_ORGANICA AS ESTUDIO_CAUSA_NATURAL, " +
                "e.COMENTARIO AS ESTUDIO_COMENTARIO, " +
                "e.IMAGEN_PATH AS IMAGEN, " +
                "sp.DURACION_POSITIVOS AS SINTOMAS_POSITIVOS_DURACION, " +
                "spr.NOMBRE AS RITMO_PENSAMIENTO, " +
                "spr.ID_RITMO_PENSAMIENTO, " +
                "sn.DURACION_NEGATIVOS AS SINTOMAS_NEGATIVOS_DURACION, " +
                "sn.BAJO_FUNCIONAMIENTO AS SINTOMAS_NEGATIVOS_BAJO_FUNCIONAMIENTO,  " +
                "sn.COMENTARIO_FUNCIONAMIENTO AS SINTOMAS_NEGATIVOS_BAJO_FUNCIONAMIENTO_COMENTARIO, " +
                "snac.NOMBRE AS ACTIVIDAD, " +
                "snac.ID_ACTIVIDAD, " +
                "snat.NOMBRE AS ATENCION, " +
                "snat.ID_ATENCION , " +
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
                HistoriaClinica historia = new HistoriaClinica();
                Estudio estudio = new Estudio();
                SintomaPositivo positivos = new SintomaPositivo();
                SintomaNegativo negativos = new SintomaNegativo();
                RitmoPensamiento ritmo = new RitmoPensamiento();
                Atencion atencion = new Atencion();
                Actividad actividad = new Actividad();
                Diagnostico diagnostico = new Diagnostico();
                Consulta consulta = new Consulta();
                Medico medico = new Medico();
                paciente.setIdPaciente(idPaciente);
                paciente.setNombre(resultSet.getString("NOMBRE"));
                paciente.setEdad(resultSet.getInt("EDAD"));
                paciente.setSexo(resultSet.getString("SEXO"));
                historia.setTrastornoAutista(resultSet.getString("TRASTORNO_AUTISTA"));
                historia.setTrastornoComunicacion(resultSet.getString("TRASTORNO_COMUNICACION"));
                historia.setTrastornoEsquizoafectivo(resultSet.getString("TRASTORNO_ESQUIZOAFECTIVO"));
                historia.setTrastornoDepresivo(resultSet.getString("TRASTORNO_DEPRESIVO"));
                historia.setTrastornoBipolar(resultSet.getString("BIPOLAR_CARAC_PSICOTICAS"));
                historia.setAntecedentesFamiliares(resultSet.getString("ANTECEDENTES_FAMILIARES"));
                historia.setSustancias(resultSet.getString("SUSTANCIAS"));
                estudio.setEstudioCausaNatural(resultSet.getString("ESTUDIO_CAUSA_NATURAL"));
                System.out.println("CAUSA NATURAL: " + resultSet.getString("ESTUDIO_CAUSA_NATURAL"));
                estudio.setEstudioComentario(resultSet.getString("ESTUDIO_COMENTARIO"));
                estudio.setImagen(resultSet.getString("IMAGEN"));
                historia.setEstudio(estudio);
                paciente.setHistoriaClinica(historia);
                positivos.setSintomasPositivosDuracion(resultSet.getString("SINTOMAS_POSITIVOS_DURACION"));
                ritmo.setNombre(resultSet.getString("RITMO_PENSAMIENTO"));
                ritmo.setId(resultSet.getInt("ID_RITMO_PENSAMIENTO"));
                // positivos.setSintomasPositivosTipoRitmoPensamiento(resultSet.getString("RITMO_PENSAMIENTO"));
                positivos.setSintomasPositivosTipoRitmoPensamiento(ritmo);
                negativos.setSintomasNegativosDuracion(resultSet.getString("SINTOMAS_NEGATIVOS_DURACION"));
                atencion.setNombre(resultSet.getString("ATENCION"));
                atencion.setId(resultSet.getInt("ID_ATENCION"));
                // negativos.setSintomasNegativosAtencion(resultSet.getString("ATENCION"));
                negativos.setSintomasNegativosAtencion(atencion);
                actividad.setNombre(resultSet.getString("ACTIVIDAD"));
                actividad.setId(resultSet.getInt("ID_ACTIVIDAD"));
                // paciente.setSintomasNegativosActividad(resultSet.getString("ACTIVIDAD"));
                negativos.setSintomasNegativosActividad(actividad);
                negativos.setSintomasNegativosBajoFuncionamiento(
                        resultSet.getString("SINTOMAS_NEGATIVOS_BAJO_FUNCIONAMIENTO"));
                negativos.setSintomasNegativosBajoFuncionamientoComentario(
                        resultSet.getString("SINTOMAS_NEGATIVOS_BAJO_FUNCIONAMIENTO_COMENTARIO"));
                paciente.setSintomasPositivos(positivos);
                paciente.setSintomasNegativos(negativos);
                diagnostico.setDiagnostico(resultSet.getString("DIAGNOSTICO"));
                diagnostico.setJustificacion(resultSet.getString("JUSTIFICACION"));
                diagnostico.setReglas(resultSet.getString("REGLAS"));
                diagnostico.setRecomendacion(resultSet.getString("RECOMENDACION"));
                diagnostico.setComentarioMedico(resultSet.getString("COMENTARIOS_MEDICOS"));
                diagnostico.setJustificacionRechazo(resultSet.getString("COMENTARIOS_RECHAZO"));
                diagnostico.setEstado(resultSet.getString("ESTADO"));
                diagnostico.setPuntaje(resultSet.getInt("PUNTAJE"));
                consulta.setFechaConsulta(resultSet.getDate("FECHA_CONSULTA").toString());
                medico.setNombreMedico(resultSet.getString("NOMBRE_MEDICO"));
                medico.setApellidoMedico(resultSet.getString("APELLIDO_MEDICO"));
                consulta.setDiagnostico(diagnostico);
                consulta.setMedico(medico);
                consulta.setPaciente(paciente);
                return consulta;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    // endregion obtenerPacientePorId

    // region obtenerTodasLasConsultasConDetalles
    public static List<Consulta> obtenerTodasLasConsultasConDetalles() {
        List<Consulta> listaConsultas = new ArrayList<>();

        String sql = "SELECT p.ID_PACIENTE, p.NOMBRE, p.EDAD, p.SEXO, " +
                "h.TRASTORNO_AUTISTA, h.TRASTORNO_COMUNICACION, h.TRASTORNO_ESQUIZOAFECTIVO, " +
                "h.TRASTORNO_DEPRESIVO, h.BIPOLAR_CARAC_PSICOTICAS, h.ANTECEDENTES_FAMILIARES, h.SUSTANCIAS, " +
                "e.CAUSA_ORGANICA AS ESTUDIO_CAUSA_NATURAL, " +
                "e.COMENTARIO AS ESTUDIO_COMENTARIO, " +
                "e.IMAGEN_PATH AS IMAGEN, " +
                "sp.DURACION_POSITIVOS AS SINTOMAS_POSITIVOS_DURACION, " +
                "spr.NOMBRE AS RITMO_PENSAMIENTO, " +
                "spr.ID_RITMO_PENSAMIENTO, " +
                "sn.DURACION_NEGATIVOS AS SINTOMAS_NEGATIVOS_DURACION, " +
                "sn.BAJO_FUNCIONAMIENTO AS SINTOMAS_NEGATIVOS_BAJO_FUNCIONAMIENTO,  " +
                "sn.COMENTARIO_FUNCIONAMIENTO AS SINTOMAS_NEGATIVOS_BAJO_FUNCIONAMIENTO_COMENTARIO, " +
                "snac.NOMBRE AS ACTIVIDAD, " +
                "snac.ID_ACTIVIDAD, " +
                "snat.NOMBRE AS ATENCION, " +
                "snat.ID_ATENCION , " +
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
                "LEFT JOIN MEDICOS m ON c.ID_MEDICO = m.ID_MEDICO ";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Paciente paciente = new Paciente();
                HistoriaClinica historia = new HistoriaClinica();
                Estudio estudio = new Estudio();
                SintomaPositivo positivos = new SintomaPositivo();
                SintomaNegativo negativos = new SintomaNegativo();
                RitmoPensamiento ritmo = new RitmoPensamiento();
                Atencion atencion = new Atencion();
                Actividad actividad = new Actividad();
                Diagnostico diagnostico = new Diagnostico();
                Consulta consulta = new Consulta();
                Medico medico = new Medico();
                paciente.setIdPaciente(resultSet.getInt("ID_PACIENTE"));
                paciente.setNombre(resultSet.getString("NOMBRE"));
                paciente.setEdad(resultSet.getInt("EDAD"));
                paciente.setSexo(resultSet.getString("SEXO"));
                historia.setTrastornoAutista(resultSet.getString("TRASTORNO_AUTISTA"));
                historia.setTrastornoComunicacion(resultSet.getString("TRASTORNO_COMUNICACION"));
                historia.setTrastornoEsquizoafectivo(resultSet.getString("TRASTORNO_ESQUIZOAFECTIVO"));
                historia.setTrastornoDepresivo(resultSet.getString("TRASTORNO_DEPRESIVO"));
                historia.setTrastornoBipolar(resultSet.getString("BIPOLAR_CARAC_PSICOTICAS"));
                historia.setAntecedentesFamiliares(resultSet.getString("ANTECEDENTES_FAMILIARES"));
                historia.setSustancias(resultSet.getString("SUSTANCIAS"));
                estudio.setEstudioCausaNatural(resultSet.getString("ESTUDIO_CAUSA_NATURAL"));
                estudio.setEstudioComentario(resultSet.getString("ESTUDIO_COMENTARIO"));
                estudio.setImagen(resultSet.getString("IMAGEN"));
                historia.setEstudio(estudio);
                paciente.setHistoriaClinica(historia);
                positivos.setSintomasPositivosDuracion(resultSet.getString("SINTOMAS_POSITIVOS_DURACION"));
                ritmo.setNombre(resultSet.getString("RITMO_PENSAMIENTO"));
                ritmo.setId(resultSet.getInt("ID_RITMO_PENSAMIENTO"));
                // positivos.setSintomasPositivosTipoRitmoPensamiento(resultSet.getString("RITMO_PENSAMIENTO"));
                positivos.setSintomasPositivosTipoRitmoPensamiento(ritmo);
                negativos.setSintomasNegativosDuracion(resultSet.getString("SINTOMAS_NEGATIVOS_DURACION"));
                atencion.setNombre(resultSet.getString("ATENCION"));
                atencion.setId(resultSet.getInt("ID_ATENCION"));
                // negativos.setSintomasNegativosAtencion(resultSet.getString("ATENCION"));
                negativos.setSintomasNegativosAtencion(atencion);
                actividad.setNombre(resultSet.getString("ACTIVIDAD"));
                actividad.setId(resultSet.getInt("ID_ACTIVIDAD"));
                // paciente.setSintomasNegativosActividad(resultSet.getString("ACTIVIDAD"));
                negativos.setSintomasNegativosActividad(actividad);
                negativos.setSintomasNegativosBajoFuncionamiento(
                        resultSet.getString("SINTOMAS_NEGATIVOS_BAJO_FUNCIONAMIENTO"));
                negativos.setSintomasNegativosBajoFuncionamientoComentario(
                        resultSet.getString("SINTOMAS_NEGATIVOS_BAJO_FUNCIONAMIENTO_COMENTARIO"));
                paciente.setSintomasPositivos(positivos);
                paciente.setSintomasNegativos(negativos);
                diagnostico.setDiagnostico(resultSet.getString("DIAGNOSTICO"));
                diagnostico.setJustificacion(resultSet.getString("JUSTIFICACION"));
                diagnostico.setReglas(resultSet.getString("REGLAS"));
                diagnostico.setRecomendacion(resultSet.getString("RECOMENDACION"));
                diagnostico.setComentarioMedico(resultSet.getString("COMENTARIOS_MEDICOS"));
                diagnostico.setJustificacionRechazo(resultSet.getString("COMENTARIOS_RECHAZO"));
                diagnostico.setEstado(resultSet.getString("ESTADO"));
                diagnostico.setPuntaje(resultSet.getInt("PUNTAJE"));
                consulta.setFechaConsulta(resultSet.getDate("FECHA_CONSULTA").toString());
                medico.setNombreMedico(resultSet.getString("NOMBRE_MEDICO"));
                medico.setApellidoMedico(resultSet.getString("APELLIDO_MEDICO"));
                consulta.setDiagnostico(diagnostico);
                consulta.setMedico(medico);
                consulta.setPaciente(paciente);
                listaConsultas.add(consulta);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaConsultas;
    }
    // endregion obtenerTodasLasConsultasConDetalles

    // region insert ESTUDIOS
    public static Boolean insertEstudios(/* Paciente paciente */ Estudio estudio) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {

            System.out.println("ESTUDIO CAUSA NATURAL SI:" + estudio.getEstudioCausaNatural());
            // tieneEstudios = "sI".equals(estudio.getEstudioCausaNatural());
            if (estudio != null) {
                tieneEstudios = true;
                String sql = "INSERT INTO ESTUDIOS (CAUSA_ORGANICA, COMENTARIO, IMAGEN_PATH) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, estudio.getEstudioCausaNatural());
                    preparedStatement.setNString(2, estudio.getEstudioComentario());
                    preparedStatement.setString(3, estudio.getImagen());
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
    public static Boolean insertHistoriaClinica(HistoriaClinica historia) {
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
                preparedStatement.setInt(1, convertirABit(historia.getTrastornoAutista()));
                preparedStatement.setInt(2, convertirABit(historia.getTrastornoComunicacion()));
                preparedStatement.setInt(3, convertirABit(historia.getTrastornoEsquizoafectivo()));
                preparedStatement.setInt(4, convertirABit(historia.getTrastornoBipolar()));
                preparedStatement.setInt(5, convertirABit(historia.getTrastornoDepresivo()));
                preparedStatement.setInt(6, convertirABit(historia.getAntecedentesFamiliares()));
                preparedStatement.setInt(7, convertirABit(historia.getSustancias()));
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
    public static Boolean insertSintomasPositivos(SintomaPositivo positivos) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            RitmoPensamiento ritmoPensamiento = positivos.getSintomasPositivosTipoRitmoPensamiento();
            int idRitmoPensamiento = obtenerIdPorNombre("RITMOS_PENSAMIENTOS",
                    convertirGuiones(ritmoPensamiento.getNombre()),
                    "ID_RITMO_PENSAMIENTO");

            sql = "INSERT INTO SINTOMAS_POSITIVOS (ID_PACIENTE, ID_RITMO_PENSAMIENTO, DURACION_POSITIVOS) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, idPaciente);
                preparedStatement.setInt(2, idRitmoPensamiento);
                preparedStatement.setNString(3, positivos.getSintomasPositivosDuracion());
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
    public static Boolean insertSintomaAlucinaciones(Alucinacion[] alucinaciones) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            // Alucinacion[] alucinaciones= a;
            /*
             * Boolean tieneAlucinaciones = "Si".equals(alucinaciones);
             * Boolean noTieneAlucinaciones = "No".equals(alucinaciones);
             */
            sql = "INSERT INTO SINTOMA_ALUCINACIONES (ID_ALUCINACION, ID_SINTOMA_POSITIVO) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // if (tieneAlucinaciones) {
                // String tiposAlucinaciones = paciente.getSintomasPositivosTipoAlucinaciones();
                // String[] tiposAlucinacionesArray = tiposAlucinaciones.split(",");
                for (Alucinacion tipoAlucinacion : alucinaciones) {
                    int idAlucinacion = obtenerIdPorNombre("ALUCINACIONES",
                            convertirGuiones(tipoAlucinacion.getNombre().trim()),
                            "ID_ALUCINACION");
                    System.out.println("ALUCINACION nombre " + tipoAlucinacion.getNombre().trim());
                    System.out.println("ALUCINACION ID " + idAlucinacion);
                    preparedStatement.setInt(1, idAlucinacion);
                    preparedStatement.setInt(2, idSintomaPositivo);
                    preparedStatement.executeUpdate();

                } /*
                   * else {
                   * int idAlucinacion = -1;
                   * if (noTieneAlucinaciones) {
                   * idAlucinacion = obtenerIdPorNombre("ALUCINACIONES", "NO_PRESENTA",
                   * "ID_ALUCINACION");
                   * } else {
                   * // NO_SE_DESCARTA
                   * idAlucinacion = obtenerIdPorNombre("ALUCINACIONES",
                   * convertirGuiones(alucinaciones),
                   * "ID_ALUCINACION");
                   * }
                   * preparedStatement.setInt(1, idAlucinacion);
                   * preparedStatement.setInt(2, idSintomaPositivo);
                   * preparedStatement.executeUpdate();
                   * }
                   */

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
    public static Boolean insertSintomaLenguajes(Lenguaje[] lenguajes) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            sql = "INSERT INTO SINTOMA_LENGUAJES (ID_LENGUAJE, ID_SINTOMA_POSITIVO) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Lengauje tiposLenguajes = paciente.getSintomasPositivosTipoLenguaje();
                // Lenguaje[] lenguajes = l;
                for (Lenguaje lenguaje : lenguajes) {
                    int idLenguaje = obtenerIdPorNombre("LENGUAJES", convertirGuiones(lenguaje.getNombre().trim()),
                            "ID_LENGUAJE");

                    System.out.println("ID_LENGUAJE " + idLenguaje);
                    System.out.println("lenguaje.getNombre() " + lenguaje.getNombre());
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
    public static Boolean insertSintomaPensamientos(Pensamiento[] pensamientos) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            sql = "INSERT INTO SINTOMA_PENSAMIENTOS (ID_PENSAMIENTO, ID_SINTOMA_POSITIVO) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // String tiposPensamientos = paciente.getSintomasPositivosTipoPensamiento();
                // Pensamiento[] pensamientos = p;
                for (Pensamiento tipoPensamiento : pensamientos) {
                    int idPensamiento = obtenerIdPorNombre("PENSAMIENTOS",
                            convertirGuiones(tipoPensamiento.getNombre().trim()),
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
    public static Boolean insertSintomaContenidoPensamientos(ContenidoPensamiento[] contenidos) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            sql = "INSERT INTO SINTOMA_CONTENIDO_PENSAMIENTOS (ID_CONTENIDO_PENSAMIENTO, ID_SINTOMA_POSITIVO) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // String tiposContenidosPensamientos =
                // paciente.getSintomasPositivosTipoContenidoPensamiento();
                // ContenidoPensamiento[] contenidos = cp;
                for (ContenidoPensamiento tipoContenidoPensamiento : contenidos) {
                    int idContenidoPensamiento = obtenerIdPorNombre("CONTENIDO_PENSAMIENTOS",
                            convertirGuiones(tipoContenidoPensamiento.getNombre().trim()),
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
    public static Boolean insertSintomasNegativos(SintomaNegativo negativos) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            Atencion atencion = negativos.getSintomasNegativosAtencion();
            int idAtencion = obtenerIdPorNombre("ATENCIONES", convertirGuiones(atencion.getNombre()), "ID_ATENCION");
            Actividad actividad = negativos.getSintomasNegativosActividad();
            int idActividad = obtenerIdPorNombre("ACTIVIDADES", convertirGuiones(actividad.getNombre()),
                    "ID_ACTIVIDAD");
            sql = "INSERT INTO SINTOMAS_NEGATIVOS (ID_PACIENTE, ID_ATENCION, ID_ACTIVIDAD, BAJO_FUNCIONAMIENTO, " +
                    "COMENTARIO_FUNCIONAMIENTO, DURACION_NEGATIVOS) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                System.out.println("ATENCION: " + idAtencion);
                preparedStatement.setInt(1, idPaciente);
                preparedStatement.setInt(2, idAtencion);
                preparedStatement.setInt(3, idActividad);
                preparedStatement.setInt(4, convertirABit(negativos.getSintomasNegativosBajoFuncionamiento()));
                preparedStatement.setNString(5, negativos.getSintomasNegativosBajoFuncionamientoComentario());
                preparedStatement.setNString(6, negativos.getSintomasNegativosDuracion());
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
    public static Boolean insertSintomaAspectos(Aspecto[] aspectos) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            sql = "INSERT INTO SINTOMA_ASPECTOS (ID_ASPECTO, ID_SINTOMA_NEGATIVO) VALUES(?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // String tiposAspectos = paciente.getSintomasNegativosAspecto();
                // String[] tiposAspectosArray = tiposAspectos.split(",");
                for (Aspecto tipoAspecto : aspectos) {
                    int idAspecto = obtenerIdPorNombre("ASPECTOS", convertirGuiones(tipoAspecto.getNombre().trim()),
                            "ID_ASPECTO");
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
    public static Boolean insertSintomaAfectividades(Afectividad[] afectividades) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            sql = "INSERT INTO SINTOMA_AFECTIVIDADES (ID_AFECTIVIDAD,             ID_SINTOMA_NEGATIVO) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // String tiposAfectividades = paciente.getSintomasNegativosAfectividad();
                // String[] tiposAfectividadesArray = tiposAfectividades.split(",");
                for (Afectividad tipoAfectividad : afectividades) {
                    int idAfectividad = obtenerIdPorNombre("AFECTIVIDADES",
                            convertirGuiones(tipoAfectividad.getNombre().trim()),
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
    public static Boolean insertDiagnostico(Diagnostico diagnostico) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "INSERT INTO DIAGNOSTICOS (DIAGNOSTICO, ESTADO, COMENTARIOS_RECHAZO, JUSTIFICACION, "
                    + "REGLAS, RECOMENDACION, PUNTAJE, COMENTARIOS_MEDICOS) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            int estado = diagnostico.getEstado().equalsIgnoreCase("Confirmado") ? 1 : 0;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setNString(1, diagnostico.getDiagnostico());
                preparedStatement.setInt(2, estado);
                preparedStatement.setNString(3, diagnostico.getJustificacionRechazo());
                preparedStatement.setNString(4, diagnostico.getJustificacion());
                preparedStatement.setNString(5, diagnostico.getReglas());
                preparedStatement.setNString(6, diagnostico.getRecomendacion());
                preparedStatement.setInt(7, diagnostico.getPuntaje());
                preparedStatement.setNString(8, diagnostico.getComentarioMedico());
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
    public static Boolean insertConsulta(Consulta consulta) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            int idMedico = obtenerIdPorMail("MEDICOS", consulta.getMedico().getEmail(),
                    "ID_MEDICO");
            System.out.println("MEDICO" + idMedico);
            String sql = "INSERT INTO CONSULTAS (ID_MEDICO, ID_PACIENTE, FECHA, ID_DIAGNOSTICO) VALUES (?, ?, ?, ?)";
            Date todayDate = new Date(Calendar.getInstance().getTimeInMillis());
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, idMedico);
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

    // region select ALUCINACIONES
    public static Alucinacion[] obtenerAlucinacionesPorSintoma(int idPaciente) {
        String sql = "SELECT a.NOMBRE AS TIPOS_ALUCINACIONES ," +
                "a.ID_ALUCINACION " +
                "FROM PACIENTES p " +
                "LEFT JOIN SINTOMAS_POSITIVOS sp ON p.ID_PACIENTE = sp.ID_PACIENTE " +
                "LEFT JOIN SINTOMA_ALUCINACIONES sa ON sp.ID_SINTOMA_POSITIVO = sa.ID_SINTOMA_POSITIVO " +
                "LEFT JOIN ALUCINACIONES a ON sa.ID_ALUCINACION = a.ID_ALUCINACION " +
                "WHERE p.ID_PACIENTE = ?";
        List<Alucinacion> alucinaciones = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, idPaciente);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String nombre = resultSet.getString("TIPOS_ALUCINACIONES");
                int idAlucinacion = resultSet.getInt("ID_ALUCINACION");
                Alucinacion alucinacion = new Alucinacion(idAlucinacion, nombre);
                alucinaciones.add(alucinacion);
            }
            System.out.println("ALUCINACIONES ARRAY:" + alucinaciones);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alucinaciones.toArray(new Alucinacion[0]);
    }
    // endregion select ALUCINACIONES

    // region select LENGUAJES
    public static Lenguaje[] obtenerLenguajesPorPacienteId(int idPaciente) {
        String sql = "SELECT l.NOMBRE AS TIPOS_LENGUAJES, " +
                "l.ID_LENGUAJE " +
                "FROM PACIENTES p " +
                "LEFT JOIN SINTOMAS_POSITIVOS sp ON p.ID_PACIENTE = sp.ID_PACIENTE " +
                "LEFT JOIN SINTOMA_LENGUAJES sl ON sp.ID_SINTOMA_POSITIVO = sl.ID_SINTOMA_POSITIVO " +
                "LEFT JOIN LENGUAJES l ON sl.ID_LENGUAJE = l.ID_LENGUAJE " +
                "WHERE p.ID_PACIENTE = ?";
        System.out.println("LENGUAJES:" + idPaciente);
        List<Lenguaje> lenguajes = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idPaciente);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String nombre = resultSet.getString("TIPOS_LENGUAJES");
                int idLenguaje = resultSet.getInt("ID_LENGUAJE");
                Lenguaje lenguaje = new Lenguaje(idLenguaje, nombre);
                lenguajes.add(lenguaje);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lenguajes.toArray(new Lenguaje[0]);
    }
    // endregion select LENGUAJES

    // region select PENSAMIENTOS
    public static Pensamiento[] obtenerPensamientosPorPacienteId(int idPaciente) {
        String sql = "SELECT pe.NOMBRE AS TIPOS_PENSAMIENTOS ," +
                " pe.ID_PENSAMIENTO " +
                "FROM PACIENTES p " +
                "LEFT JOIN SINTOMAS_POSITIVOS sp ON p.ID_PACIENTE = sp.ID_PACIENTE " +
                "LEFT JOIN SINTOMA_PENSAMIENTOS spe ON sp.ID_SINTOMA_POSITIVO = spe.ID_SINTOMA_POSITIVO " +
                "LEFT JOIN PENSAMIENTOS pe ON spe.ID_PENSAMIENTO = pe.ID_PENSAMIENTO " +
                "WHERE p.ID_PACIENTE = ?";
        List<Pensamiento> pensamientos = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idPaciente);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String nombre = resultSet.getString("TIPOS_PENSAMIENTOS");
                int idPensamiento = resultSet.getInt("ID_PENSAMIENTO");
                Pensamiento pensamiento = new Pensamiento(idPensamiento, nombre);
                pensamientos.add(pensamiento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pensamientos.toArray(new Pensamiento[0]);
    }
    // endregion select PENSAMIENTOS

    // region select CONTENIDO_PENSAMIENTOS
    public static ContenidoPensamiento[] obtenerContenidosPensamientosPorPacienteId(int idPaciente) {
        String sql = "SELECT cp.NOMBRE AS TIPOS_CONTENIDOS_PENSAMIENTOS ," +
                " cp.ID_CONTENIDO_PENSAMIENTO " +
                "FROM PACIENTES p " +
                "LEFT JOIN SINTOMAS_POSITIVOS sp ON p.ID_PACIENTE = sp.ID_PACIENTE " +
                "LEFT JOIN SINTOMA_CONTENIDO_PENSAMIENTOS scp ON sp.ID_SINTOMA_POSITIVO = scp.ID_SINTOMA_POSITIVO " +
                "LEFT JOIN CONTENIDO_PENSAMIENTOS cp ON scp.ID_CONTENIDO_PENSAMIENTO = cp.ID_CONTENIDO_PENSAMIENTO " +
                "WHERE p.ID_PACIENTE = ?";
        List<ContenidoPensamiento> contenidosPensamientos = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idPaciente);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String nombre = resultSet.getString("TIPOS_CONTENIDOS_PENSAMIENTOS");
                int idContenidoPensamiento = resultSet.getInt("ID_CONTENIDO_PENSAMIENTO");
                ContenidoPensamiento contenidoPensamiento = new ContenidoPensamiento(idContenidoPensamiento, nombre);
                contenidosPensamientos.add(contenidoPensamiento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (contenidosPensamientos.toArray(new ContenidoPensamiento[0]));
    }
    // endregion select CONTENIDO_PENSAMIENTOS

    // region select ASPECTOS
    public static Aspecto[] obtenerAspectosPorPacienteId(int idPaciente) {
        String sql = "SELECT a.NOMBRE AS TIPOS_ASPECTOS ," +
                " a.ID_ASPECTO " +
                "FROM PACIENTES p " +
                "LEFT JOIN SINTOMAS_NEGATIVOS sp ON p.ID_PACIENTE = sp.ID_PACIENTE " +
                "LEFT JOIN SINTOMA_ASPECTOS sa ON sp.ID_SINTOMA_NEGATIVO = sa.ID_SINTOMA_NEGATIVO " +
                "LEFT JOIN ASPECTOS a ON sa.ID_ASPECTO = a.ID_ASPECTO " +
                "WHERE p.ID_PACIENTE = ?";
        List<Aspecto> aspectos = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idPaciente);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String nombre = resultSet.getString("TIPOS_ASPECTOS");
                int idAspecto = resultSet.getInt("ID_ASPECTO");
                Aspecto aspecto = new Aspecto(idAspecto, nombre);
                aspectos.add(aspecto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (aspectos.toArray(new Aspecto[0]));
    }
    // endregion select ASPECTOS

    // region select AFECTIVIDADES
    public static Afectividad[] obtenerAfectividadesPorPacienteId(int idPaciente) {
        String sql = "SELECT a.NOMBRE AS TIPOS_AFECTIVIDADES ," +
                " a.ID_AFECTIVIDAD " +
                "FROM PACIENTES p " +
                "LEFT JOIN SINTOMAS_NEGATIVOS sp ON p.ID_PACIENTE = sp.ID_PACIENTE  " +
                "LEFT JOIN SINTOMA_AFECTIVIDADES sa ON sp.ID_SINTOMA_NEGATIVO = sa.ID_SINTOMA_NEGATIVO " +
                "LEFT JOIN AFECTIVIDADES a ON sa.ID_AFECTIVIDAD = a.ID_AFECTIVIDAD " +
                "WHERE p.ID_PACIENTE = ?";
        List<Afectividad> afectividades = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idPaciente);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String nombre = resultSet.getString("TIPOS_AFECTIVIDADES");
                int idAfectividad = resultSet.getInt("ID_AFECTIVIDAD");
                Afectividad afectividad = new Afectividad(idAfectividad, nombre);
                afectividades.add(afectividad);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (afectividades.toArray(new Afectividad[0]));
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

    // region obtenerIdPorMail
    private static int obtenerIdPorMail(String tabla, String nombre, String columnaID) {
        int id = -1;
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "SELECT " + columnaID + " FROM " + tabla + " WHERE EMAIL = ?";
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
    // endregion obtenerIdPorMail

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
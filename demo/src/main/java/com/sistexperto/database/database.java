package com.sistexperto.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.sistexperto.model.ImagenesEntity;
import com.sistexperto.model.Medico;
import com.sistexperto.model.Paciente;

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
                preparedStatement.setString(2, paciente.getSexo());
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

<<<<<<< HEAD
    // region login
    public static Boolean login(String mail, String password) {
        System.out.println("------");
        System.out.println(mail);
        System.out.println("------");
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "SELECT * FROM MEDICOS WHERE EMAIL= ? AND CONTRASEÑA = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1,"admin@admin.com");
                preparedStatement.setString(2,password);
                //preparedStatement.executeUpdate();
                ResultSet resultSet=preparedStatement.executeQuery();
                //ResultSet resultSet = preparedStatement.getGeneratedKeys();
                System.out.println("After Change:" + preparedStatement);
                System.out.println(resultSet);
                System.out.println(mail);
                System.out.println(password);
                if(resultSet.next()){
                    Medico medico = new Medico();
                    medico.setEmail(mail);
                    medico.setContraseña(password);
                    medico.setDni(resultSet.getInt("DNI"));
                    medico.setNombre(resultSet.getString("NOMBRE"));
                    medico.setApellido(resultSet.getString("APELLIDO"));
                    return true;
                }else{
                    System.out.println("asdrgh");
                    return false;
                }
=======
    // region insert ESTUDIOS
    public static Boolean insertEstudios(Paciente paciente) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {

            tieneEstudios = "Si".equals(paciente.getEstudios());
            if (tieneEstudios) {
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
>>>>>>> d764eb9c33fea130fa8783caa2b55073c96a26f1
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
<<<<<<< HEAD
    // endregion insert PACIENTES
=======
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

    // region convertirGuiones
    public static String convertirGuiones(String texto) {
        if (texto == null) {
            return null;
        }
        return texto.replace("-", "_").replace(" ", "_").toUpperCase();
    }
    // endregion convertirGuiones

    // region convertirABit
    private static int convertirABit(String valor) {
        return valor.equalsIgnoreCase("Si") ? 1 : 0;
    }
    // endregion convertirABit
>>>>>>> d764eb9c33fea130fa8783caa2b55073c96a26f1

}

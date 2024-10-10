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
    
    public static Boolean ingresarNuevoPaciente(Paciente paciente) {
        if (!insertPaciente(paciente))
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

}

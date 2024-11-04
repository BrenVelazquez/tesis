package com.sistexperto.dto;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PacienteResponse {

    @JsonProperty("diagnostico")
    private String diagnostico;

    @JsonProperty("reglas")
    private int reglas;

    @JsonProperty("reglas_ejecutadas")
    private String reglasEjecutadas;

    @JsonProperty("recomendacion")
    private String recomendacion;

    @JsonProperty("puntaje")
    private int puntaje = 0;

    @JsonProperty("justificacion")
    private String justificacion;

    @JsonProperty("exito")
    private Boolean exito;

    public void calcularDiagnostico(Boolean temporal) {
        String recomendacion = "";
        if (puntaje < 36) {
            this.setDiagnostico("Esquizofrenia no posible");
            recomendacion = "Se recomienda evaluar otros diagnósticos como Depresión y Trastorno Bipolar.";
            this.actualizarReglasEjecutadas("R60");
        } else {
            if (puntaje >= 36 && temporal) {
                this.setDiagnostico("Evaluar esquizofrenia temporal");
                recomendacion = "Se recomienda evaluar esquizofrenia temporal.";

            } else {
                this.setDiagnostico("Esquizofrenia");
                recomendacion = "Se recomienda iniciar tratamiento.";
                this.actualizarReglasEjecutadas("R59");
            }
        }

        if (recomendacion == "") {
            this.actualizarRecomendacion(recomendacion, 1);

        } else {
            this.actualizarRecomendacion(recomendacion, 1);
        }
    }

    public void actualizarJustificacion(String texto, int opcion) {
        String utf8Texto = new String(texto.getBytes(), StandardCharsets.UTF_8);
        System.out.println("Nueva Justificacion: " + utf8Texto);
        if (opcion == 1) {
            if (justificacion == null || justificacion.isEmpty()) {
                justificacion = utf8Texto;

            } else {
                justificacion += ", \n" + utf8Texto;
            }
        } else if (opcion == 2) {
            justificacion = texto;
        }
        System.out.println("- Puntaje: " + puntaje);
        setJustificacion(justificacion);

    }

    public void actualizarRecomendacion(String texto, int opcion) {
        String utf8Texto = new String(texto.getBytes(), StandardCharsets.UTF_8);
        System.out.println(utf8Texto);
        if (opcion == 1) {
            if (recomendacion == null || recomendacion.isEmpty()) {
                recomendacion = utf8Texto;

            } else {
                recomendacion += ",\n" + utf8Texto;
            }
        } else if (opcion == 2) {
            recomendacion = texto;
        }
        System.out.println("Recomendacion: " + recomendacion);
        setRecomendacion(recomendacion);
    }

    public void actualizarReglasEjecutadas(String texto) {
        String utf8Texto = new String(texto.getBytes(), StandardCharsets.UTF_8);
        System.out.println("Nueva Regla: " + utf8Texto);
        if (reglas == 0) {
            reglasEjecutadas = utf8Texto;
            reglas++;

        } else {
            reglasEjecutadas += ", " + utf8Texto;
            reglas++;
        }
        System.out.println("- reglas: " + reglas);
        setReglasEjecutadas(reglasEjecutadas);
        setReglas(reglas);

    }
}

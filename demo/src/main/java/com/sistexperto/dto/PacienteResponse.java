package com.sistexperto.dto;

import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PacienteResponse {
    // Esto es lo que nos va a devolver de las reglas y lo que vamos a mostrar como
    // resultado por pantalla

    @JsonProperty("posibilidad")
    private String posibilidad;

    @JsonProperty("reglas")
    private int reglas;

    // Para los casos en que se recomienda evaluar esquizofrenia temporal
    @JsonProperty("recomendacion")
    private String recomendacion;

    @JsonProperty("puntaje")
    private int puntaje;

    // Tenemos que justificar el resultado que damos
    @JsonProperty("justificacion")
    private String justificacion;

    public void calcularDiagnostico() {  //yo evaluo los puntajes para dar el diagnostico en las reglas, agrego lo que había hecho (que se ve que lo borre ) para que no rompan las reglas y después definimos como lo hacemos
        if (puntaje < 51) {
            this.setPosibilidad("No es posible que tenga esquizofrenia");
            if (recomendacion == null) {
                this.setRecomendacion("Se recomienda evaluar otras enfermedades como Depresión y Transtorno Bipolar.");
            }
        } else if (puntaje <= 20) {  // la temporal solo se puede saber con el tiempo, no con el puntaje
            this.setPosibilidad("Posible Esquizofrenia Temporal");
            if (recomendacion == null) {
                this.setRecomendacion("Se recomienda evaluar esquizofrenia temporal.");
            }
        } else {
            this.setPosibilidad("Posible esquizofrenia");
            if (recomendacion == null) {
                this.setRecomendacion("Se recomienda iniciar tratamiento.");
            }
        }
    }

    // opción de calcular diagnóstico en reglas 

    public void posibleEsquizofrenia() { 
        this.setPosibilidad("Posible esquizofrenia");
            if (recomendacion == null) {
                this.setRecomendacion("Se recomienda iniciar tratamiento.");
            }
    }

    public void noPosibleEsquizofrenia() { 
        this.setPosibilidad("No es posible que tenga esquizofrenia");
            if (recomendacion == null) {
                this.setRecomendacion("Se recomienda evaluar otros diagnósticos como Depresión y Transtorno Bipolar.");
            }
    }

    public void posibleTemporal() {   //en la regla evaluo que tenga el puntaje para esquizofrenia pero la duración sea menor a un mes
        this.setPosibilidad("Posible Esquizofrenia Temporal");
            if (recomendacion == null) {
                this.setRecomendacion("Se recomienda evaluar esquizofrenia temporal.");
            }
    }

    // Logica de Mati
    public void actualizarJustificacion(String texto, int opcion) {
        String utf8Texto = new String(texto.getBytes(), StandardCharsets.UTF_8);
        System.out.println(utf8Texto);
        if (opcion == 1) {
            // Opción 1: Concatenar el texto con una coma
            if (justificacion == null || justificacion.isEmpty()) {
                justificacion = utf8Texto;

            } else {
                justificacion += ",\n " + utf8Texto;
            }
        } else if (opcion == 2) {
            // Opción 2: Reemplazar el contenido existente
            justificacion = texto;
        }
        System.out.println(justificacion);
        setJustificacion(justificacion);
    }

    public void actualizarRecomendacion(String texto, int opcion) {
        String utf8Texto = new String(texto.getBytes(), StandardCharsets.UTF_8);
        System.out.println(utf8Texto);
        if (opcion == 1) {
            // Opción 1: Concatenar el texto con una coma
            if (recomendacion == null || recomendacion.isEmpty()) {
                recomendacion = utf8Texto;

            } else {
                recomendacion += ",\n " + utf8Texto;
            }
        } else if (opcion == 2) {
            // Opción 2: Reemplazar el contenido existente
            recomendacion = texto;
        }
        System.out.println(recomendacion);
        setRecomendacion(recomendacion);
    }
}

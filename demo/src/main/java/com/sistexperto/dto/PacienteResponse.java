package com.sistexperto.dto;

import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PacienteResponse {
    //Esto es lo que nos va a devolver de las reglas y lo que vamos a mostrar como resultado por pantalla


    @JsonProperty("posibilidad")
    private String posibilidad;

    @JsonProperty("reglas")
    private int reglas;

    //Para los casos en que se recomienda evaluar esquizofrenia temporal
    @JsonProperty("recomendacion")
    private String recomendacion;

    @JsonProperty("puntaje")
    private int puntaje;

    // Tenemos que justificar el resultado que damos
    @JsonProperty("justificacion")
    private String justificacion;

    public void calcularRiesgo() {
        // REVISAR PUNTAJES (PUSE NUMERO AL AZAR PARA ARMAR ESTO NOMAS) 
        if (puntaje < 10) {
            this.setPosibilidad("No es posible que tenga esquizofrenia");
        } 
        else if(puntaje <= 20){
            this.setPosibilidad("Posible Esquizofrenia Temporal");
            this.setRecomendacion("Se recomienda evaluar exquisofrenia temporal.");
        } else {
            this.setPosibilidad("Posible esquizofrenia");
        }
    }

    // Logica de Mati
    public void actualizarJustificacion(String texto, int opcion) 
    {
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
}

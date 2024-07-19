package com.sistexperto.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PacienteRequest {
    // Estos son todos los datos que vamos a recolectar y enviar a las reglas (todas los valores de la tabla CAV)


    @JsonProperty("edad")
    private int edad;

    @JsonProperty("sexo")
    private String sexo;
    
}

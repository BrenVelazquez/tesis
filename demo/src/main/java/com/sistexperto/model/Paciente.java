package com.sistexperto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

    @JsonProperty("edad")
    private int edad;

    @JsonProperty("sexo")
    private String sexo;
    
    @JsonProperty("nombre")
    private String nombre;
    
    @JsonProperty("id_imagen")
    private String id_imagen;
    
}

package com.sistexperto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medico {

    @JsonProperty("dni")
    private int dni;

    @JsonProperty("email")
    private String email;
    
    @JsonProperty("contraseña")
    private String contraseña;
    
    @JsonProperty("nombre")
    private String nombre;
    
    @JsonProperty("apellido")
    private String apellido;
}
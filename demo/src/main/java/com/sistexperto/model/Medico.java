package com.sistexperto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medico {

    @JsonProperty("id")
    private int id;

    @JsonProperty("dni")
    private int dni;

    @JsonProperty("email")
    private String email;

    @JsonProperty("contraseña")
    private String contraseña;

    @JsonProperty("nombre_medico")
    private String nombreMedico;

    @JsonProperty("apellido_medico")
    private String apellidoMedico;

    public String getNombreCompletoMedico() {
        return nombreMedico + " " + apellidoMedico;
    }
}
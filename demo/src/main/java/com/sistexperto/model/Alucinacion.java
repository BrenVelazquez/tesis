package com.sistexperto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alucinacion {

    @JsonProperty("id")
    private int id;

    @JsonProperty("nombre")
    private String nombre;

}
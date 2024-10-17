package com.sistexperto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BajoFuncionamiento {

    @JsonProperty("id")
    private int id;

    @JsonProperty("bajoFuncionamiento")
    private boolean bajoFuncionamiento;

    @JsonProperty("comentarios")
    private String comentarios;

}
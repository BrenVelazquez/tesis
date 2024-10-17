package com.sistexperto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SintomaPositivo {

    @JsonProperty("alucinaciones")
    private List<Alucinacion> alucinaciones;

    @JsonProperty("lenguajes")
    private List<Lenguaje> lenguajes;

    @JsonProperty("pensamientos")
    private List<Pensamiento> pensamientos;

    @JsonProperty("contenidos")
    private List<ContenidoPensamiento> contenidos;

    @JsonProperty("ritmo")
    private RitmoPensamiento ritmo;

    @JsonProperty("duracionPositivos")
    private String duracionPositivos;

}
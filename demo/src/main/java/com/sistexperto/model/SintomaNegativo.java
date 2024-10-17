package com.sistexperto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SintomaNegativo {

    @JsonProperty("actividad")
    private Actividad actividad;

    @JsonProperty("aspecto")
    private List<Aspecto> aspectos;

    @JsonProperty("atencion")
    private Atencion atencion;
    
    @JsonProperty("afectividad")
    private List<Afectividad> afectividades;

    @JsonProperty("bajoF")
    private BajoFuncionamiento bajoF;

    @JsonProperty("duracionPositivos")
    private String duracionPositivos;

}
package com.sistexperto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Estudio {
    

    @JsonProperty("estudio_causa_natural")
    private String estudioCausaNatural;

    @JsonProperty("estudio_comentario")
    private String estudioComentario;

    @JsonProperty("imagen")
    private String imagen;

}
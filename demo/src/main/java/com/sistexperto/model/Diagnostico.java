package com.sistexperto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Diagnostico {

    @JsonProperty("id")
    private int id;

    @JsonProperty("diagnostico")
    private String diagnostico;

    @JsonProperty("estado")
    private String estado;

    @JsonProperty("comentario_medico")
    private String comentarioMedico;

    @JsonProperty("justificacion")
    private String justificacion;

    @JsonProperty("justificacion_rechazo")
    private String justificacionRechazo;

    @JsonProperty("recomendacion")
    private String recomendacion;

    @JsonProperty("reglas")
    private String reglas;

    @JsonProperty("puntaje")
    private int puntaje;

}
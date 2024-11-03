package com.sistexperto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoriaClinica {

    @JsonProperty("trastorno_autista")
    private String trastornoAutista;

    @JsonProperty("trastorno_comunicacion")
    private String trastornoComunicacion;

    @JsonProperty("trastorno_esquizoafectivo")
    private String trastornoEsquizoafectivo;

    @JsonProperty("trastorno_depresivo")
    private String trastornoDepresivo;

    @JsonProperty("trastorno_bipolar")
    private String trastornoBipolar;

    @JsonProperty("antecedentes_familiares")
    private String antecedentesFamiliares;

    @JsonProperty("sustancias")
    private String sustancias;

    @JsonProperty("estudio")
    private Estudio estudio;

}
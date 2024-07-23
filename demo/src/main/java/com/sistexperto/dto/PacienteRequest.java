package com.sistexperto.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PacienteRequest {
    // Estos son todos los datos que vamos a recolectar y enviar a las reglas (todas los valores de la tabla CAV)


    @JsonProperty("edad")
    private int edad;

    @JsonProperty("sexo")
    private String sexo;
    
    @JsonProperty("delirio")
    private boolean delirio;

    @JsonProperty("alucinaciones")
    private String alucinaciones;

    @JsonProperty("disgregacion")
    private boolean disgregacion;

    @JsonProperty("incoherencia")
    private boolean incoherencia;

    @JsonProperty("positivos")
    private int positivos;

    @JsonProperty("negativos")
    private int negativos;

    @JsonProperty("bizarro")
    private boolean bizarro;

    @JsonProperty("extravagante")
    private boolean extravagante;

    @JsonProperty("inadecuado")
    private boolean inadecuado;

    @JsonProperty("adelgazado")
    private boolean adelgazado;

    @JsonProperty("hipomimico")
    private boolean hipomimico;

    @JsonProperty("hiprosexia")
    private boolean hiprosexia;

    @JsonProperty("paraprosexia")
    private boolean paraprosexia;

    @JsonProperty("abulia")
    private boolean abulia;

    @JsonProperty("hipobulia")
    private boolean hipobulia;

    @JsonProperty("impulsiones")
    private boolean impulsiones;

    @JsonProperty("compulsiones")
    private boolean compulsiones;

    @JsonProperty("apraxia")
    private boolean apraxia;

    @JsonProperty("ataxia")
    private boolean ataxia;

    @JsonProperty("exopraxia")
    private boolean exopraxia;

    @JsonProperty("manierismo")
    private boolean manierismo;

    @JsonProperty("esterotipia")
    private boolean esterotipia;

    @JsonProperty("negativismo")
    private boolean negativismo;

    @JsonProperty("flexibilidadCerea")
    private boolean flexibilidadCerea;

    @JsonProperty("aplanamientoAfectivo")
    private boolean aplanamientoAfectivo;

    @JsonProperty("labilidad")
    private boolean labilidad;

    @JsonProperty("irritabilidad")
    private boolean irritabilidad;

    @JsonProperty("expansivo")
    private boolean expansivo;

    @JsonProperty("inapropiado")
    private boolean inapropiado;

    @JsonProperty("anhedonia")
    private boolean anhedonia;

    @JsonProperty("autismo")
    private boolean autismo;

    @JsonProperty("trastornoComunicacion")
    private boolean trastornoComunicacion;

    @JsonProperty("esquizoafectivo")
    private boolean esquizoafectivo;

    @JsonProperty("bipolarCaracPsicoticas")
    private boolean bipolarCaracPsicoticas;

    @JsonProperty("sustancias")
    private boolean sustancias;

    @JsonProperty("causaOrganica")
    private boolean causaOrganica;

    @JsonProperty("pensamientoSP") //pensamiento sin particularidades, lo pongo como boolean porque si es true es normal
    private boolean pensamientoSP;

    @JsonProperty("Bradipsiquia") 
    private boolean Bradipsiquia;

    @JsonProperty("Taquipsiquia") 
    private boolean Taquipsiquia;

    @JsonProperty("contenidoPensSP") //contenido de pensamiento sin particularidades, lo pongo como boolean porque si es true es normal
    private boolean contenidoPensSP;
}

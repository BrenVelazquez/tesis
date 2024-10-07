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

    @JsonProperty("trastorno_autista")
    private String trastorno_autista;

    @JsonProperty("trastorno_comunicacion")
    private String trastorno_comunicacion;

    @JsonProperty("trastorno_esquizoafectivo")
    private String trastorno_esquizoafectivo;

    @JsonProperty("trastorno_depresivo")
    private String trastorno_depresivo;

    @JsonProperty("trastorno_bipolar")
    private String trastorno_bipolar;

    @JsonProperty("antecedentes_familiares")
    private String antecedentes_familiares;

    @JsonProperty("sintomas_positivos_duracion")
    private String sintomas_positivos_duracion;

    @JsonProperty("sintomas_positivos_alucinaciones")
    private String sintomas_positivos_alucinaciones;

    @JsonProperty("sintomas_positivos_tipo_lenguaje")
    private String sintomas_positivos_tipo_lenguaje;

    @JsonProperty("sintomas_positivos_tipo_alucinaciones")
    private String sintomas_positivos_tipo_alucinaciones;

    @JsonProperty("sintomas_positivos_tipo_pensamiento")
    private String sintomas_positivos_tipo_pensamiento;

    @JsonProperty("sintomas_positivos_tipo_ritmo_pensamiento")
    private String sintomas_positivos_tipo_ritmo_pensamiento;

    @JsonProperty("sintomas_positivos_tipo_contenido_pensamiento")
    private String sintomas_positivos_tipo_contenido_pensamiento;


    @JsonProperty("sintomas_negativos_duracion")
    private String sintomas_negativos_duracion;

    @JsonProperty("sintomas_negativos_bajo_funcionamiento")
    private String sintomas_negativos_bajo_funcionamiento;

    @JsonProperty("sintomas_negativos_bajo_funcionamiento_comentario")
    private String sintomas_negativos_bajo_funcionamiento_comentario;

    @JsonProperty("sintomas_negativos_aspecto")
    private String sintomas_negativos_aspecto;

    @JsonProperty("sintomas_negativos_atencion")
    private String sintomas_negativos_atencion;

    @JsonProperty("sintomas_negativos_actividad")
    private String sintomas_negativos_actividad;

    @JsonProperty("sintomas_negativos_afectividad")
    private String sintomas_negativos_afectividad;

    @JsonProperty("sustancias")
    private String sustancias;

    @JsonProperty("estudios")
    private String estudios;

    @JsonProperty("estudio_causa_natural")
    private String estudio_causa_natural;
} 

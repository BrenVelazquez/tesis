package com.sistexperto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

    @JsonProperty("id_paciente")
    private int idPaciente;

    @JsonProperty("edad")
    private int edad;

    @JsonProperty("sexo")
    private String sexo;

    @JsonProperty("nombre")
    private String nombre;

    // HISTORIAS_CLINICAS
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

    // SINTOMAS_POSITIVOS
    @JsonProperty("sintomas_positivos_duracion")
    private String sintomasPositivosDuracion;

    @JsonProperty("sintomas_positivos_alucinaciones")
    private String sintomasPositivosAlucinaciones;

    @JsonProperty("sintomas_positivos_tipo_alucinaciones")
    private String sintomasPositivosTipoAlucinaciones;

    @JsonProperty("sintomas_positivos_tipo_lenguaje")
    private String sintomasPositivosTipoLenguaje;

    @JsonProperty("sintomas_positivos_tipo_pensamiento")
    private String sintomasPositivosTipoPensamiento;

    @JsonProperty("sintomas_positivos_tipo_ritmo_pensamiento")
    private String sintomasPositivosTipoRitmoPensamiento;

    @JsonProperty("sintomas_positivos_tipo_contenido_pensamiento")
    private String sintomasPositivosTipoContenidoPensamiento;

    // SINTOMAS_NEGATIVOS
    @JsonProperty("sintomas_negativos_duracion")
    private String sintomasNegativosDuracion;

    @JsonProperty("sintomas_negativos_aspecto")
    private String sintomasNegativosAspecto;

    @JsonProperty("sintomas_negativos_atencion")
    private String sintomasNegativosAtencion;

    @JsonProperty("sintomas_negativos_actividad")
    private String sintomasNegativosActividad;

    @JsonProperty("sintomas_negativos_afectividad")
    private String sintomasNegativosAfectividad;

    @JsonProperty("sintomas_negativos_bajo_funcionamiento")
    private String sintomasNegativosBajoFuncionamiento;

    @JsonProperty("sintomas_negativos_bajo_funcionamiento_comentario")
    private String sintomasNegativosBajoFuncionamientoComentario;

    // ESTUDIOS
    @JsonProperty("estudios")
    private String estudios;

    @JsonProperty("estudio_causa_natural")
    private String estudioCausaNatural;

    @JsonProperty("estudio_comentario")
    private String estudioComentario;

    // @JsonProperty("id_imagen")
    // private String idImagen;

    // CONSULTAS
    @JsonProperty("fecha_consulta")
    private String fechaConsulta;

    // DIAGNOSTICO
    @JsonProperty("diagnostico")
    private String diagnostico;

    @JsonProperty("justificacion")
    private String justificacion;

    @JsonProperty("reglas")
    private String reglas;

    @JsonProperty("recomendacion")
    private String recomendacion;

    @JsonProperty("comentario_medico")
    private String comentarioMedico;

    @JsonProperty("justificacion_rechazo")
    private String justificacionRechazo;

    @JsonProperty("estado")
    private String estado;

    @JsonProperty("puntaje")
    private int puntaje;
    
    // MEDICO
    @JsonProperty("nombre_medico")
    private String nombreMedico;

    @JsonProperty("apellido_medico")
    private String apellidoMedico;
}

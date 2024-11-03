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

    @JsonProperty("sintomas_negativos_actividad")
    private Actividad sintomasNegativosActividad;

    @JsonProperty("sintomas_negativos_aspecto")
    private Aspecto[] sintomasNegativosAspecto;

    @JsonProperty("sintomas_negativos_atencion")
    private Atencion sintomasNegativosAtencion;
    
    @JsonProperty("sintomas_negativos_afectividad")
    private Afectividad[] sintomasNegativosAfectividad;


    @JsonProperty("sintomas_negativos_bajo_funcionamiento")
    private String sintomasNegativosBajoFuncionamiento;

    @JsonProperty("sintomas_negativos_bajo_funcionamiento_comentario")
    private String sintomasNegativosBajoFuncionamientoComentario;

    @JsonProperty("sintomas_negativos_duracion")
    private String sintomasNegativosDuracion;
}

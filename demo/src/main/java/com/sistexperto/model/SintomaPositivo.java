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

    @JsonProperty("sintomas_positivos_alucinaciones")
    private Alucinacion[] sintomasPositivosAlucinaciones;

    @JsonProperty("sintomas_positivos_tipo_lenguaje")
    private Lenguaje[] sintomasPositivosTipoLenguaje;

    @JsonProperty("sintomas_positivos_tipo_pensamiento")
    private Pensamiento[] sintomasPositivosTipoPensamiento;

    @JsonProperty("sintomas_positivos_tipo_contenido_pensamiento")
    private  ContenidoPensamiento[] sintomasPositivosTipoContenidoPensamiento;

    @JsonProperty("sintomas_positivos_tipo_ritmo_pensamiento")
    private RitmoPensamiento sintomasPositivosTipoRitmoPensamiento;

    @JsonProperty("sintomas_positivos_duracion")
    private String sintomasPositivosDuracion;

}
package com.sistexperto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consulta {

    @JsonProperty("paciente")
    private Paciente paciente;

    @JsonProperty("medico")
    private Medico medico;

    
    @JsonProperty("diagnostico")
    private Diagnostico diagnostico;

    /*@JsonProperty("date")
    private Date date;*/
     

    

}

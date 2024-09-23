function volver() {
    window.location.href = "/";
}

// region completarYDeshabilitarCampos
function completarYDeshabilitarCampos(pacienteData) {
    // Completar campos con los datos del paciente
    $("#nombre").val(pacienteData.nombre);
    $("#edad").val(pacienteData.edad);
    $("#sexo").val(pacienteData.sexo);
    $("#fototipo").val(pacienteData.fototipo_piel);
    $("#ubicacion").val(pacienteData.ubicacion);
    $("#color").val(pacienteData.color);
    $("#diametro").val(pacienteData.diametro);

    // Completar campos booleanos (si/no)
    $("#trastorno-autista-si").toggleClass("selected", pacienteData.trastorno_autista === "Si");
    $("#trastorno-autista-no").toggleClass("selected", pacienteData.trastorno_autista === "No");

    $("#trastorno-comunicacion-si").toggleClass("selected", pacienteData.trastorno_comunicacion === "Si");
    $("#trastorno-comunicacion-no").toggleClass("selected", pacienteData.trastorno_comunicacion === "No");

    $("#trastorno-esquizoafectivo-si").toggleClass("selected", pacienteData.trastorno_esquizoafectivo === "Si");
    $("#trastorno-esquizoafectivo-no").toggleClass("selected", pacienteData.trastorno_esquizoafectivo === "No");

    $("#trastorno-depresivo-si").toggleClass("selected", pacienteData.trastorno_depresivo === "Si");
    $("#trastorno-depresivo-no").toggleClass("selected", pacienteData.trastorno_depresivo === "No");

    $("#trastorno-bipolar-si").toggleClass("selected", pacienteData.trastorno_bipolar === "Si");
    $("#trastorno-bipolar-no").toggleClass("selected", pacienteData.trastorno_bipolar === "No");

    $("#antecedentes-familiares-si").toggleClass("selected", pacienteData.antecedentes_familiares === "Si");
    $("#antecedentes-familiares-no").toggleClass("selected", pacienteData.antecedentes_familiares === "No");

    // sintomas positivos
    $("#sintomas-positivos-duracion").val(pacienteData.sintomas_positivos_duracion);
    $("#sintomas-positivos-alucinaciones-si").toggleClass("selected", pacienteData.sintomas_positivos_alucinaciones === "Si");
    $("#sintomas-positivos-alucinaciones-no").toggleClass("selected", pacienteData.sintomas_positivos_alucinaciones === "No");
    $("#sintomas-positivos-alucinaciones-no-descarta").toggleClass("selected", pacienteData.sintomas_positivos_alucinaciones === "No se descarta");
    //mostrar select y multiselect en un unico texto
    $("#alucinaciones").val(pacienteData.sintomas_positivos_tipo_alucinaciones);
    $("#lenguaje").val(pacienteData.sintomas_positivos_tipo_lenguaje);
    $("#pensamiento").val(pacienteData.sintomas_positivos_tipo_pensamiento);
    $("#ritmo-pensamiento").val(pacienteData.sintomas_positivos_tipo_ritmo_pensamiento);
    $("#contenido-pensamiento").val(pacienteData.sintomas_positivos_tipo_contenido_pensamiento);

    //sintomas negativos
    $("#sintomas-negativos-duracion").val(pacienteData.sintomas_negativos_duracion);
    $("#aspecto").val(pacienteData.sintomas_negativos_aspecto);
    $("#atencion").val(pacienteData.sintomas_negativos_atencion);
    $("#actividad").val(pacienteData.sintomas_negativos_actividad);
    $("#afectividad").val(pacienteData.sintomas_negativos_afectividad);
    $("#bajo-funcionamiento-si").toggleClass("selected", pacienteData.bajo_funcionamiento === "Si");
    $("#bajo-funcionamiento-no").toggleClass("selected", pacienteData.bajo_funcionamiento === "No");

    if (pacienteData.bajo_funcionamiento_comentario !== "" && pacienteData.bajo_funcionamiento_comentario != undefined) {
        $("#bajo-funcionamiento-comentario").removeClass("no-show-seccion");
        $("#bajo-funcionamiento-comentario-text").val(pacienteData.bajo_funcionamiento_comentario);
    }


    //complementarios
    $("#sustancias-si").toggleClass("selected", pacienteData.sustancias === "Si");
    $("#sustancias-no").toggleClass("selected", pacienteData.sustancias === "No");
    $("#estudios-si").toggleClass("selected", pacienteData.estudios === "Si");
    $("#estudios-no").toggleClass("selected", pacienteData.estudios === "No");
    if (pacienteData.estudio_causa_natural != "" && pacienteData.estudio_causa_natural != undefined) {
        $("#estudio-causa-natural-id").removeClass("no-show-seccion");
        $("#estudios-causa-organica-si").toggleClass("selected", pacienteData.estudio_causa_natural === "Si");
        $("#estudios-causa-organica-no").toggleClass("selected", pacienteData.estudio_causa_natural === "No");
        $("#estudios-causa-organica-inconcluso").toggleClass("selected", pacienteData.estudio_causa_natural === "Inconcluso");
    }
    if (pacienteData.estudio_comentario != "" && pacienteData.estudio_comentario != undefined) {
        $("#estudio-comentario-id").removeClass("no-show-seccion");
        $("#estudio-comentario-text").val(pacienteData.estudio_comentario);
    }
    if (pacienteData.id_imagen != "" && pacienteData.id_imagen != undefined) {
        $("#estudio-imagen-id").removeClass("no-show-seccion");
        $("#imagen-preview").attr("src", pacienteData.id_imagen);
    }

    //diagnostico
    if (pacienteData.diagnostico === "Posible esquizofrenia") {
        $("#diagnostico-esquizofrenia").removeClass("no-show-seccion");
    } else if (pacienteData.diagnostico === "Esquizofrenia no posible") {
        $("#diagnostico-no-esquizofrenia").removeClass("no-show-seccion");
    } else if (pacienteData.diagnostico === "Evaluar esquizofrenia temporal") {
        $("#diagnostico-esquizofrenia-temporal").removeClass("no-show-seccion");
    }
    $("#justificacion").val(pacienteData.justificacion.replace(/\n/g, ' '));
    $("#recomendacion").val(pacienteData.recomendacion.replace(/\n/g, ' '));
    if (pacienteData.comentarios != "") {
        $("#comentarios-medicos-container").removeClass("no-show-seccion");
        $("#comentarios-medicos").val(pacienteData.comentarios);
    }
    if (pacienteData.estado === "Aceptado") {
        $("#estado-aceptado").removeClass("no-show-seccion");
    } else {
        $("#estado-rechazado").removeClass("no-show-seccion");
        if (pacienteData.justificacion_rechazo != "") {
            $("#justificacion-rechazo-container").removeClass("no-show-seccion");
            $("#justificacion-rechazo").val(pacienteData.justificacion_rechazo);
        }
    }
}
// endregion completarYDeshabilitarCampos

function obtenerPacienteHarcodeado() {
    const data = {
        edad: "25",
        sexo: "Masculino",
        nombre: "Armando Quilo",
        trastorno_autista: "No",
        trastorno_comunicacion: "No",
        trastorno_esquizoafectivo: "No",
        trastorno_depresivo: "No",
        trastorno_bipolar: "No",
        antecedentes_familiares: "Si",
        sintomas_positivos_duracion: "Mayor o igual a un mes",
        sintomas_positivos_alucinaciones: "Si",
        sintomas_positivos_tipo_alucinaciones: "Alucinaciones auditivas simples, Alucinaciones cenestesicas",
        sintomas_positivos_tipo_lenguaje: "Soliloquio",
        sintomas_positivos_tipo_pensamiento: "Sin particularidades",
        sintomas_positivos_tipo_ritmo_pensamiento: "Sin particularidades",
        sintomas_positivos_tipo_contenido_pensamiento: "Ideacion delirante no sistematizada",
        sintomas_negativos_duracion: "Mayor o igual a un mes",
        sintomas_negativos_aspecto: "Facie, Alinio, Aseo",
        sintomas_negativos_atencion: "Euprosexia",
        sintomas_negativos_actividad: "Hipobulia",
        sintomas_negativos_afectividad: "Eutimia",
        bajo_funcionamiento: "Si",
        bajo_funcionamiento_comentario: "",
        sustancias: "No",
        estudios: "Si",
        estudio_causa_natural: "No",
        estudio_comentario: "Se realiza interconsulta con Traumatología y Reumatología quienes descartan causa orgánica de sus dolores musculares.",
        recomendacion: "Se recomienda evaluar esquizofrenia cenestésica,\nSe recomienda iniciar tratamiento.",
        diagnostico: "Posible esquizofrenia",
        estado: "Aceptado",
        comentarios: "",
        puntaje: 58,
        justificacion: "Dos (o más) de los síntomas principales, cada uno de ellos presente durante una parte significativa de tiempo durante un período de un mes. Al menos uno de ellos ha de ser delirios, alucinaciones, disgregación o incoherencia, \nLa duración de síntomas positivos y negativos es mayor a un mes, \nDurante una parte significativa de tiempo desde el inicio del trastorno, el nivel de funcionamiento en uno o más ámbitos principales está muy por debajo del nivel alcanzado antes del inicio, \nExisten antecedentes de un trastorno del espectro autista o de un trastorno de la comunicación de inicio en la infancia y presenta delirios o alucinaciones, \nSe han descartado el trastorno esquizoafectivo y el trastorno depresivo o bipolar con características psicóticas, \nEl trastorno no se puede atribuir a los efectos fisiológicos de una sustancia u otra afección médica, \nLos síntomas no se deben a una causa orgánica, \nPresenta alucinaciones, \nPresenta delirios, \nSe encuentra en la edad de la media de diagnóstico de esquizofrenia para hombres, \nNo presenta particularidades en el lenguaje, \nNo presenta particularidades en el pensamiento, \nNo presenta particularidades en el ritmo del pensamiento, \nPosee antecedentes familiares psiquiátricos, \nAspecto normal, \nAtención normal, \nPresenta particularidades en la actividad de las más comunes en esquizofrenia (abulia, hipobulia, compulsiones, ecopraxia, manierismo, estereotipia motora o negativismo), \nAfectividad normal"
    };

    completarYDeshabilitarCampos(data);
}

$(document).ready(function () {

    $("#detalle-complementarios-container").load("detalleComplementarios.html", function () { });

    $("#detalle-sintomas-positivos-container").load("detalleSintomasPositivos.html", function () { });

    $("#detalle-sintomas-negativos-container").load("detalleSintomasNegativos.html", function () {
        if (typeof obtenerPacienteHarcodeado === "function") {
            obtenerPacienteHarcodeado();
        }
    });

    $("#detalle-diagnostico-container").load("detalleDiagnostico.html", function () {
        if (typeof obtenerPacienteHarcodeado === "function") {
            obtenerPacienteHarcodeado();
        }
    });
});

document.addEventListener("DOMContentLoaded", function () {
    const urlParams = new URLSearchParams(window.location.search);
    const codigoPaciente = urlParams.get("codigo");
    console.log("Código del paciente: " + codigoPaciente);

    function obtenerPaciente() {
        $.ajax({
            url: "/obtener-paciente",
            method: "GET",
            data: { codigo_paciente: codigoPaciente },
            dataType: "json",
            success: function (data) {
                console.log("DATA: ", data);
                completarYDeshabilitarCampos(data);
            },
            error: function (xhr, status, error) {
                console.log("Error al buscar paciente: " + status);
                alert("Error al buscar paciente: " + status);
            }
        });
    }

    // Llama a esta función al cargar la página para obtener los datos del paciente
    // obtenerPaciente();
    obtenerPacienteHarcodeado();

})
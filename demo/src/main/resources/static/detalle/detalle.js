function volver() {
    window.location.href = "/";
}

$(document).ready(function () {

    $("#detalle-complementarios-container").load("detalleComplementarios.html", function () { });

    $("#detalle-sintomas-positivos-container").load("detalleSintomasPositivos.html", function () { });

    $("#detalle-sintomas-negativos-container").load("detalleSintomasNegativos.html", function () { });

    $("#detalle-diagnostico").load("detalleDiagnostico.html", function () { });
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
        $("#trastorno-depresivos-no").toggleClass("selected", pacienteData.trastorno_depresivo === "No");

        $("#trastorno-bipolar-si").toggleClass("selected", pacienteData.trastorno_bipolar === "Si");
        $("#trastorno-bipolar-no").toggleClass("selected", pacienteData.trastorno_bipolar === "No");

        $("#antecedentes-familiares-si").toggleClass("selected", pacienteData.antecedentes_familiares === "Si");
        $("#antecedentes-familiares-no").toggleClass("selected", pacienteData.antecedentes_familiares === "No");

        // sintomas positivos
        $("#sintomas-positivos-duracion").text(pacienteData.sintomas_positivos_duracion);
        $("#sintomas-positivos-alucinaciones-si").toggleClass("selected", pacienteData.sintomas_positivos_alucinaciones === "Si");
        $("#sintomas-positivos-alucinaciones-no").toggleClass("selected", pacienteData.sintomas_positivos_alucinaciones === "No");
        $("#sintomas-positivos-alucinaciones-no-descarta").toggleClass("selected", pacienteData.sintomas_positivos_alucinaciones === "No se descarta");
        //mostrar select y multiselect en un unico texto
        $("#alucinaciones").text(pacienteData.sintomas_positivos_tipo_alucinaciones);
        $("#lenguaje").text(pacienteData.sintomas_positivos_tipo_lenguaje);
        $("#pensamiento").text(pacienteData.sintomas_positivos_tipo_pensamiento);
        $("#ritmo-pensamiento").text(pacienteData.sintomas_positivos_tipo_ritmo_pensamiento);
        $("#contenido-pensamiento").text(pacienteData.sintomas_positivos_tipo_contenido_pensamiento);

        //sintomas negativos
        $("#sintomas-negativos-duracion").text(pacienteData.sintomas_negativos_duracion);
        $("#aspecto").text(pacienteData.sintomas_negativos_aspecto);
        $("#atencion").text(pacienteData.sintomas_negativos_atencion);
        $("#actividad").text(pacienteData.sintomas_negativos_actividad);
        $("#afectividad").text(pacienteData.sintomas_negativos_afectividad);

        //complementarios
        $("#sustancias-si").toggleClass("selected", pacienteData.sustancias === "Si");
        $("#sustancias-no").toggleClass("selected", pacienteData.sustancias === "No");
        $("#estudios-si").toggleClass("selected", pacienteData.estudios === "Si");
        $("#estudios-no").toggleClass("selected", pacienteData.estudios === "No");
        $("#estudio-causa-natural-opcion").text(pacienteData.estudio_causa_natural);
        $("#estudio-comentario").text(pacienteData.estudio_comentario);
        $("#imagen-preview").attr("src", pacienteData.id_imagen);

        //diagnostico
        if (pacienteData.diagnostico === "Esquizofrenia Posible") {
            $("#diagnostico-esquizofrenia").show();
        } else if (pacienteData.diagnostico === "Esquizofrenia no posible") {
            $("#diagnostico-no-esquizofrenia").show();
        } else if (pacienteData.diagnostico === "Evaluar esquizofrenia temporal") {
            $("#diagnostico-esquizofrenia-temporal").show();
        }
        // $("#diagnostico").text(pacienteData.diagnostico);
        $("#justificacion").text(pacienteData.justificacion);
        $("#recomendacion").text(pacienteData.recomendacion);
        $("#comentarios-medicos").val(pacienteData.comentarios);
        $("#estado").text(pacienteData.estado);
        $("#justificacion-rechazo").val(pacienteData.justificacion_rechazo);
    }
    // endregion completarYDeshabilitarCampos
})
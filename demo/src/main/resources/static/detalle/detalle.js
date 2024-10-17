function volver() {
    //window.location.href = "/home";
    window.history.back();
}

document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('btnCerrarSesion').addEventListener('click', function () {
        localStorage.removeItem('medico'); // Eliminar información del médico
        window.location.href = '/'; // Redirigir a la página de login
    });
});


function obtenerIdPacienteDeUrl() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('idPaciente');
}

$(document).ready(function () {

    $("#detalle-complementarios-container").load("detalleComplementarios.html", function () { });

    $("#detalle-sintomas-positivos-container").load("detalleSintomasPositivos.html", function () { });

    $("#detalle-sintomas-negativos-container").load("detalleSintomasNegativos.html", function () { });

    $("#detalle-diagnostico-container").load("detalleDiagnostico.html", function () { });

    const medicoData = JSON.parse(localStorage.getItem('medico')); // Recuperar y convertir de JSON
    if (medicoData) {
        document.getElementById('username').textContent = `Dr/Dra: ${medicoData.nombre + " " + medicoData.apellido}`;
    }
});

document.addEventListener('DOMContentLoaded', function () {
    const idPaciente = obtenerIdPacienteDeUrl();
    console.log("idPaciente: " + idPaciente);

    if (idPaciente) {
        obtenerDetallesPaciente(idPaciente);
    } else {
        alert('No se ha seleccionado ningún paciente.');
    }
});

async function obtenerDetallesPaciente(idPaciente) {
    try {
        const response = await $.ajax({
            type: "GET",
            url: `/obtenerDetallesPaciente/${idPaciente}`,
            contentType: "application/json"
        });
        if (response) {
            console.log("response: " + JSON.stringify(response));
            mostrarDetallesPaciente(response);
        } else {
            console.error('No se encontraron detalles para este paciente.');
        }
    } catch (error) {
        console.error('Error al obtener los detalles del paciente:', error);
    }
}

// region completarYDeshabilitarCampos
function mostrarDetallesPaciente(pacienteData) {
    $("#nombre").val(pacienteData.nombre);
    $("#edad").val(pacienteData.edad);
    $("#sexo").val(pacienteData.sexo);

    $("#trastorno-autista-si").toggleClass("selected", pacienteData.trastornoAutista == 1);
    $("#trastorno-autista-no").toggleClass("selected", pacienteData.trastornoAutista == 0);

    $("#trastorno-comunicacion-si").toggleClass("selected", pacienteData.trastornoComunicacion == 1);
    $("#trastorno-comunicacion-no").toggleClass("selected", pacienteData.trastornoComunicacion == 0);

    $("#trastorno-esquizoafectivo-si").toggleClass("selected", pacienteData.trastornoEsquizoafectivo == 1);
    $("#trastorno-esquizoafectivo-no").toggleClass("selected", pacienteData.trastornoEsquizoafectivo == 0);

    $("#trastorno-depresivo-si").toggleClass("selected", pacienteData.trastornoDepresivo == 1);
    $("#trastorno-depresivo-no").toggleClass("selected", pacienteData.trastornoDepresivo == 0);

    $("#trastorno-bipolar-si").toggleClass("selected", pacienteData.trastornoBipolar == 1);
    $("#trastorno-bipolar-no").toggleClass("selected", pacienteData.trastornoBipolar == 0);

    $("#antecedentes-familiares-si").toggleClass("selected", pacienteData.antecedentesFamiliares == 1);
    $("#antecedentes-familiares-no").toggleClass("selected", pacienteData.antecedentesFamiliares == 0);

    // sintomas positivos
    $("#sintomas-positivos-duracion").val(pacienteData.sintomasPositivosDuracion);
    // $("#sintomas-positivos-alucinaciones-si").toggleClass("selected", pacienteData.sintomasPositivosTipoAlucinaciones != null);
    // $("#sintomas-positivos-alucinaciones-no").toggleClass("selected", pacienteData.sintomas_positivos_alucinaciones === "No");
    // $("#sintomas-positivos-alucinaciones-no-descarta").toggleClass("selected", pacienteData.sintomas_positivos_alucinaciones === "No se descarta");

    //mostrar select y multiselect en un unico texto
    $("#alucinaciones").val(pacienteData.sintomasPositivosTipoAlucinaciones);
    $("#lenguaje").val(pacienteData.sintomasPositivosTipoLenguaje);
    $("#pensamiento").val(pacienteData.sintomasPositivosTipoPensamiento);
    $("#ritmo-pensamiento").val(pacienteData.sintomasPositivosTipoRitmoPensamiento);
    $("#contenido-pensamiento").val(pacienteData.sintomasPositivosTipoContenidoPensamiento);

    // sintomas negativos
    $("#sintomas-negativos-duracion").val(pacienteData.sintomasNegativosDuracion);
    $("#aspecto").val(pacienteData.sintomasNegativosAspecto);
    $("#atencion").val(pacienteData.sintomasNegativosAtencion);
    $("#actividad").val(pacienteData.sintomasNegativosActividad);
    $("#afectividad").val(pacienteData.sintomasNegativosAfectividad);
    $("#bajo-funcionamiento-si").toggleClass("selected", pacienteData.sintomasNegativosBajoFuncionamiento == 1);
    $("#bajo-funcionamiento-no").toggleClass("selected", pacienteData.sintomasNegativosBajoFuncionamiento == 0);
    if (pacienteData.sintomasNegativosBajoFuncionamiento == 1
        && pacienteData.sintomasNegativosBajoFuncionamientoComentario != undefined 
        && pacienteData.sintomasNegativosBajoFuncionamientoComentario != "") {
        $("#bajo-funcionamiento-comentario-id").removeClass("no-show-seccion");
        $("#bajo-funcionamiento-comentario").val(pacienteData.sintomasNegativosBajoFuncionamientoComentario);
    }

    // complementarios
    $("#sustancias-si").toggleClass("selected", pacienteData.sustancias == 1);
    $("#sustancias-no").toggleClass("selected", pacienteData.sustancias == 0);
    if (pacienteData.estudioCausaNatural != null) {
        $("#estudios-si").toggleClass("selected");
    }
    else {
        $("#estudios-no").toggleClass("selected");
    }
    if (pacienteData.estudioCausaNatural != null && pacienteData.estudioCausaNatural != undefined) {
        $("#estudio-causa-natural-id").removeClass("no-show-seccion");
        $("#estudios-causa-organica-si").toggleClass("selected", pacienteData.estudioCausaNatural === "estudio-causa-natural-si");
        $("#estudios-causa-organica-no").toggleClass("selected", pacienteData.estudioCausaNatural === "estudio-causa-natural-no");
        $("#estudios-causa-organica-inconcluso").toggleClass("selected", pacienteData.estudioCausaNatural === "estudio-causa-natural-inconcluso");
    }
    if (pacienteData.estudioComentario != "" && pacienteData.estudioComentario != undefined) {
        $("#estudio-comentario-id").removeClass("no-show-seccion");
        $("#estudio-comentario-text").val(pacienteData.estudioComentario);
    }
    if (pacienteData.idImagen != "" && pacienteData.idImagen != undefined) {
        $("#estudio-imagen-id").removeClass("no-show-seccion");
        $("#imagen-preview").attr("src", pacienteData.idImagen);
    }

    // Diagnostico
    if (pacienteData.diagnostico === "Esquizofrenia") {
        $("#diagnostico-esquizofrenia").removeClass("no-show-seccion");
    } else if (pacienteData.diagnostico === "Esquizofrenia no posible") {
        $("#diagnostico-no-esquizofrenia").removeClass("no-show-seccion");
    } else if (pacienteData.diagnostico === "Evaluar esquizofrenia temporal") {
        $("#diagnostico-esquizofrenia-temporal").removeClass("no-show-seccion");
    }
    $("#justificacion").val(pacienteData.justificacion.replace(/, \n/g, '. '));
    $("#recomendacion").val(pacienteData.recomendacion.replace(/,\n/g, '. '));
    if (pacienteData.comentarioMedico != "") {
        $("#comentarios-medicos-container").removeClass("no-show-seccion");
        $("#comentarios-medicos").val(pacienteData.comentarioMedico);
    }
    if (pacienteData.estado == 1) {
        $("#estado-confirmado").removeClass("no-show-seccion");
    } else {
        $("#estado-rechazado").removeClass("no-show-seccion");
        if (pacienteData.justificacionRechazo != "") {
            $("#justificacion-rechazo-container").removeClass("no-show-seccion");
            $("#justificacion-rechazo").val(pacienteData.justificacionRechazo);
        }
    }
    $("#fecha").text("Fecha de consulta: " + pacienteData.fechaConsulta)
    $("#medico").text("Médico a cargo: " + pacienteData.apellidoMedico + " " + pacienteData.nombreMedico)
}
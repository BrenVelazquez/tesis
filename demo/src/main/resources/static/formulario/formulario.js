let datosFormulario = {};

$(document).ready(function () {
    initializeButtons();

    $("#complementarios-container").load("complementarios.html", function () {
        initializeComplementaryButtons();
    });

    $("#sintomas-positivos-container").load("sintomasPositivos.html", function () {
        initializePositiveButtons();
    });

    $("#sintomas-negativos-container").load("sintomasNegativos.html", function () {
        // initializeNegativeButtons(); 
    });

    $("#diagnostico-popup").load("diagnostico.html", function () {
        // Inicializa el botón de close una vez cargado el contenido del popup
        $('#close-popup').on('click', function () {
            cerrarPopup();
        });
    });
});


document.addEventListener('DOMContentLoaded', function () { });

function mostrarPopup() {
    $("#popup").fadeIn(1000);
}

function mostrarJustificacion() {
    $("#justificacion-explicacion").fadeIn(1000);
}

function cerrarPopup() {
    $("#message-container").fadeIn();
    setTimeout(function () {
        $("#message-container").fadeOut();
        $("#popup").fadeOut();
        // Redirige al inicio (index.html) en localhost:8080
        // window.location.href = "http://192.168.0.13:8080/";
        // window.location.href = "http://127.0.0.1:8080/formulario/formulario.html";
    }, 1000); // Muestra el mensaje durante 1 segundo
}

function initializeButtons() {
    const buttonPairs = [
        { yes: 'transtorno-autista-si', no: 'transtorno-autista-no' },
        { yes: 'transtorno-comunicacion-si', no: 'transtorno-comunicacion-no' },
        { yes: 'transtorno-esquizoafectivo-si', no: 'transtorno-esquizoafectivo-no' },
        { yes: 'transtorno-depresivo-si', no: 'transtorno-depresivo-no' },
        { yes: 'transtorno-bipolar-si', no: 'transtorno-bipolar-no' },
        { yes: 'antecedentes-familiares-si', no: 'antecedentes-familiares-no' },
    ];

    buttonPairs.forEach(pair => {
        const btnYes = document.getElementById(pair.yes);
        const btnNo = document.getElementById(pair.no);

        if (btnYes && btnNo) {
            btnYes.addEventListener('click', function () {
                btnYes.classList.remove('selected');
                btnNo.classList.remove('selected');
                this.classList.add('selected');
            });

            btnNo.addEventListener('click', function () {
                btnYes.classList.remove('selected');
                btnNo.classList.remove('selected');
                this.classList.add('selected');
            });
        } else {
            console.error(`Botones no encontrados: ${pair.yes} o ${pair.no}`);
        }
    });
}


function initializeComplementaryButtons() {
    const buttonPairs = [
        { yes: 'estudios-si', no: 'estudios-no' },
        { yes: 'sustancias-si', no: 'sustancias-no' },
    ];

    buttonPairs.forEach(pair => {
        const btnYes = document.getElementById(pair.yes);
        const btnNo = document.getElementById(pair.no);

        if (btnYes && btnNo) {
            btnYes.addEventListener('click', function () {
                btnYes.classList.remove('selected');
                btnNo.classList.remove('selected');
                this.classList.add('selected');

                //Para que se desplieguen los campos de estudio
                if (this.id === 'estudios-si') {
                    $("#estudio-comentario").slideDown();
                    $("#estudio-causa-natural").slideDown();
                    $("#image-container").slideDown();
                }
            });

            btnNo.addEventListener('click', function () {
                btnYes.classList.remove('selected');
                btnNo.classList.remove('selected');
                this.classList.add('selected');

                //Para que se dejen de mostrar los campos de estudio
                if (this.id === 'estudios-no') {
                    $("#estudio-comentario").slideUp();
                    $("#estudio-causa-natural").slideUp();
                    $("#image-container").slideUp();
                }
            });
        } else {
            console.error(`Complementarios - Botones no encontrados: ${pair.yes} o ${pair.no}`);
        }
    });

}

function initializePositiveButtons() {
    const buttonPairs = [
        { yes: 'sintomas-positivos-delirios-presencia', no: 'sintomas-positivos-delirios-ausencia' },
    ];

    buttonPairs.forEach(pair => {
        const btnYes = document.getElementById(pair.yes);
        const btnNo = document.getElementById(pair.no);

        if (btnYes && btnNo) {
            btnYes.addEventListener('click', function () {
                btnYes.classList.remove('selected');
                btnNo.classList.remove('selected');
                this.classList.add('selected');
            });

            btnNo.addEventListener('click', function () {
                btnYes.classList.remove('selected');
                btnNo.classList.remove('selected');
                this.classList.add('selected');
            });
        } else {
            console.error(`Positivos - Botones no encontrados: ${pair.yes} o ${pair.no}`);
        }
    });

    /* Botones Alucinaciones*/
    const btnYesAlucinaciones = document.getElementById('sintomas-positivos-alucinaciones-si');
    const btnNoAlucinaciones = document.getElementById('sintomas-positivos-alucinaciones-no');
    const btnNoSeDescartaAlucinaciones = document.getElementById('sintomas-positivos-alucinaciones-no-descarta');

    if (btnYesAlucinaciones && btnNoAlucinaciones && btnNoSeDescartaAlucinaciones) {
        btnYesAlucinaciones.addEventListener('click', function () {
            btnYesAlucinaciones.classList.add('selected');
            btnNoAlucinaciones.classList.remove('selected');
            btnNoSeDescartaAlucinaciones.classList.remove('selected');
            $("#tipo-alucinaciones").slideDown();
        });

        btnNoAlucinaciones.addEventListener('click', function () {
            btnNoAlucinaciones.classList.add('selected');
            btnYesAlucinaciones.classList.remove('selected');
            btnNoSeDescartaAlucinaciones.classList.remove('selected');
            $("#tipo-alucinaciones").slideUp();
        });

        btnNoSeDescartaAlucinaciones.addEventListener('click', function () {
            btnNoSeDescartaAlucinaciones.classList.add('selected');
            btnYesAlucinaciones.classList.remove('selected');
            btnNoAlucinaciones.classList.remove('selected');
            $("#tipo-alucinaciones").slideUp();
        });
    } else {
        console.error('Uno o más botones de alucinaciones no encontrados.');
    }
    /*Fin Botones Alucinaciones*/

}

function validar_campos(datosFormulario) {
    console.log("INICIO VALIDAR CAMPOS");

    resetearErrores(); // Resetear mensajes de error y estilos previos
    let esValido = true;

    if (datosFormulario.nombre === "") {
        mostrarError(document.getElementById("nombre"), "Por favor, ingrese el nombre del paciente.");
        esValido = false;
    }

    if (datosFormulario.edad === "") {
        mostrarError(document.getElementById("edad"), "Por favor, ingrese la edad del paciente.");
        esValido = false;
    }

    if (datosFormulario.sexo === "-1") {
        mostrarError(document.getElementById("sexo"), "Por favor, seleccione una opción.");
        esValido = false;
    }

    console.log("FIN VALIDAR CAMPOS - esValido? = " + esValido);
    return esValido; // Retorna true si todo es válido, false si hay algún campo incompleto
}


function mostrarError(campo, mensaje) {
    campo.style.borderColor = "red"; // Resaltar el campo con borde rojo
    const errorDiv = document.createElement("div");
    errorDiv.className = "error-message";
    errorDiv.style.color = "red";
    errorDiv.textContent = mensaje;
    campo.parentNode.appendChild(errorDiv); // Mostrar mensaje de error debajo del campo
}

function resetearErrores() {
    const campos = document.querySelectorAll("input, select");
    campos.forEach(campo => {
        campo.style.borderColor = ""; // Resetear borde
        const errorMessage = campo.parentNode.querySelector(".error-message");
        if (errorMessage) {
            campo.parentNode.removeChild(errorMessage); // Eliminar mensaje de error
        }
    });
}

function determinar_diagnostico() {
    console.log("INICIO FUNCION DIAGNOSTICO()");

    // Obtener los valores de los campos del formulario
    const edad = document.getElementById("edad").value;
    const sexo = document.getElementById("sexo").value;
    const nombre = document.getElementById("nombre").value;

    const transtornoAutista =
        document.querySelector("#transtorno-autista-si.selected") ? "Si" :
            document.querySelector("#transtorno-autista-no.selected") ? "No" : "";
    const transtornoComunicacion =
        document.querySelector("#transtorno-comunicacion-si.selected") ? "Si" :
            document.querySelector("#transtorno-comunicacion-no.selected") ? "No" : "";
    const transtornoEsquizoafectivo =
        document.querySelector("#transtorno-esquizoafectivo-si.selected") ? "Si" :
            document.querySelector("#transtorno-esquizoafectivo-no.selected") ? "No" : "";
    const transtornoDepresivo =
        document.querySelector("#transtorno-depresivo-si.selected") ? "Si" :
            document.querySelector("#transtorno-depresivo-no.selected") ? "No" : "";
    const transtornoBipolar =
        document.querySelector("#transtorno-bipolar-si.selected") ? "Si" :
            document.querySelector("#transtorno-bipolar-no.selected") ? "No" : "";
    const antecedentesFamiliares =
        document.querySelector("#antecedentes-familiares-si.selected") ? "Si" :
            document.querySelector("#antecedentes-familiares-no.selected") ? "No" : "";

    // sintomas positivos            
    const selectedSPDuracion = document.getElementById("sintomas-positivos-duracion");
    const sintomasPositivosDuracion = selectedSPDuracion.options[selectedSPDuracion.selectedIndex].text;
    const sintomasPositivosAlucinaciones =
        document.querySelector("#sintomas-positivos-alucinaciones-si.selected") ? "Si" :
            document.querySelector("#sintomas-positivos-alucinaciones-no.selected") ? "No" :
                document.querySelector("#sintomas-positivos-alucinaciones-no-descarta.selected") ? "No se descarta" :
                    "";
    const sintomasPositivosTipoAlucinaciones = document.getElementById("alucinaciones").value;
    const sintomasPositivosTipoLenguaje = document.getElementById("lenguaje").value;
    const sintomasPositivosTipoPensamiento = document.getElementById("pensamiento").value;
    const sintomasPositivosTipoRitmoPensamiento = document.getElementById("ritmo-pensamiento").value;
    const sintomasPositivosTipoContenidoPensamiento = document.getElementById("contenido-pensamiento").value;
    const sintomasPositivosDelirios =
        document.querySelector("#sintomas-positivos-delirios-presencia.selected") ? "Presencia" :
            document.querySelector("#sintomas-positivos-delirios-ausencia.selected") ? "Ausencia" : "";

    // sintomas negativos
    const selectedSNDuracion = document.getElementById("sintomas-negativos-duracion");
    const sintomasNegativosDuracion = selectedSNDuracion.options[selectedSNDuracion.selectedIndex].text;
    const sintomasNegativosAspecto = document.getElementById("aspecto").value;
    const sintomasNegativosAtencion = document.getElementById("atencion").value;
    const sintomasNegativosActividad = document.getElementById("actividad").value;
    const sintomasNegativosAfectividad = document.getElementById("afectividad").value;

    //complementarios
    const sustancias =
        document.querySelector("#sustancias-si.selected") ? "Si" :
            document.querySelector("#sustancias-no.selected") ? "No" : "";
    const estudios =
        document.querySelector("#estudios-si.selected") ? "Si" :
            document.querySelector("#estudios-no.selected") ? "No" : "";
    const estudioCausaNatural = document.getElementById("estudio-causa-natural-opcion").value;

    const estudioComentario = document.getElementById("estudio-comentario").value;
    // Se crea un objeto con los valores recolectados
    console.log("datosFormulario: ", datosFormulario);
    datosFormulario = {
        edad: edad,
        sexo: sexo,
        nombre: nombre,
        transtorno_autista: transtornoAutista,
        transtorno_comunicacion: transtornoComunicacion,
        transtorno_esquizoafectivo: transtornoEsquizoafectivo,
        transtorno_depresivo: transtornoDepresivo,
        transtorno_bipolar: transtornoBipolar,
        antecedentes_familiares: antecedentesFamiliares,
        // sintomas positivos,
        sintomas_positivos_duracion: sintomasPositivosDuracion,
        sintomas_positivos_alucinaciones: sintomasPositivosAlucinaciones,
        sintomas_positivos_tipo_lenguaje: sintomasPositivosTipoLenguaje,
        sintomas_positivos_tipo_alucinaciones: sintomasPositivosTipoAlucinaciones,
        sintomas_positivos_tipo_pensamiento: sintomasPositivosTipoPensamiento,
        sintomas_positivos_tipo_ritmo_pensamiento: sintomasPositivosTipoRitmoPensamiento,
        sintomas_positivos_tipo_contenido_pensamiento: sintomasPositivosTipoContenidoPensamiento,
        sintomas_positivos_delirios: sintomasPositivosDelirios,
        // sintomas negativos,
        sintomas_negativos_duracion: sintomasNegativosDuracion,
        sintomas_negativos_aspecto: sintomasNegativosAspecto,
        sintomas_negativos_atencion: sintomasNegativosAtencion,
        sintomas_negativos_actividad: sintomasNegativosActividad,
        sintomas_negativos_afectividad: sintomasNegativosAfectividad,
        //complementarios
        sustancias: sustancias,
        estudios: estudios,
        estudio_causa_natural: estudioCausaNatural,
        //respuestas del diagnostico
        // posibilidad,
        recomendacion,
        // justificacion,
    };

    const jsonString = JSON.stringify(datosFormulario);
    console.log("Datos del formulario en formato JSON: ", jsonString);
    if (validar_campos(datosFormulario)) {

        $.ajax({
            type: "POST",
            url: "/diagnosticar", // La URL del Controller
            contentType: "application/json",
            data: jsonString,
            success: function (response) {
                console.log("Respuesta: ", response);
                // document.getElementById("codigo-paciente").textContent = response.codigo_paciente;
                let posibilidad = response.posibilidad;

                //Dato para harcodear en el front
                // posibilidad = "Posible esquizofrenia";

                // Mostrar el elemento correspondiente según el valor de "posibilidad"
                if (posibilidad === "Posible esquizofrenia") {
                    $("#posible-esquizofrenia").css("display", "block");
                }
                else if (posibilidad === "Posible Esquizofrenia Temporal") {
                    $("#evaluar-temporal").css("display", "block");
                }
                else if (posibilidad === "No es posible que tenga esquizofrenia") {
                    $("#no-posible-esquizofrenia").css("display", "block");
                }
                datosFormulario.posibilidad = response.posibilidad;

                // Actualizar el texto de "Recomendaciones" según la respuesta
                let recomendacion = response.recomendacion;
                // recomendacion = "Se recomienda iniciar tratamiento.";
                console.log("recomendacion: " + recomendacion);
                if (recomendacion != null) {
                    $("#recomendacion-title").css("display", "block");
                    $("#recomendacion").css("display", "block");
                    $("#recomendacion").text(recomendacion ? recomendacion : "-");
                }
                datosFormulario.recomendacion = recomendacion;

                // Actualizar el texto de "Justificacion" según la respuesta
                let justificacion = response.justificacion;
                if (justificacion != null) {
                    $("#posibles-causas").css("display", "block");
                    $("#posibles-causas").text(response.justificacion ? response.justificacion : "No disponible");
                }
                datosFormulario.justificacion = response.justificacion;

                // datosFormulario.codigo_paciente = response.codigo_paciente;
                console.log('DATOS DEL FORMULARIO ACTUALIZADOS', datosFormulario);
                mostrarPopup();
            },
            error: function (error) {
                console.error("Error en la solicitud de diagnósitco: " + JSON.stringify(error));
            }
        });

    }
    console.log("FIN FUNCION DIAGNOSTICO()");

}

function guardarRegistro(estado) {

    // Obtener el archivo seleccionado
    let fileInput = document.getElementById('imagen');
    let file = fileInput.files[0];

    // Crear un objeto FormData
    let formData = new FormData();
    formData.append('file', file);

    //Metodo de Subir Imagen
    // console.error('file:', file);
    // console.error('formData:', formData);
    // if(file){
    //     console.log("Se carga el archivo.");
    //     $.ajax({
    //         url: '/subirImagen', // La URL del Controller
    //         type: 'POST',
    //         data: formData,
    //         processData: false,
    //         contentType: false,
    //         success: function (data) {
    //             console.log('Imagen subida y guardada con éxito:', data);
    //         },
    //         error: function (xhr, status, error) {
    //             console.error('Error al subir la imagen:', error);
    //         }
    //     });
    // }


    // // Metodo de Cargar Paciente
    // $.ajax({
    //     type: "POST",
    //     url: "/ingresarPaciente", // La URL del controlador
    //     contentType: "application/json",
    //     data: jsonString,
    //     success: function (response) {
    //         console.log('Paciente ingresado con éxito:', data);
    //         cerrarPopup();
    //     },
    //     error: function (xhr, status, error) {
    //         console.error('Error al registrar al paciente:', error);
    //     }
    // });
}
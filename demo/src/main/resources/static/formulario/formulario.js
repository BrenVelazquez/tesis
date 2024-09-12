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
                    $("#estudio-causa-natural-id").slideDown();
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
                    $("#estudio-causa-natural-id").slideUp();
                    $("#image-container").slideUp();
                }
            });
        } else {
            console.error(`Complementarios - Botones no encontrados: ${pair.yes} o ${pair.no}`);
        }
    });

}

function initializePositiveButtons() {
    const buttonPairs = [];

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
            $("#tipo-alucinaciones-id").slideDown();
        });

        btnNoAlucinaciones.addEventListener('click', function () {
            btnNoAlucinaciones.classList.add('selected');
            btnYesAlucinaciones.classList.remove('selected');
            btnNoSeDescartaAlucinaciones.classList.remove('selected');
            $("#tipo-alucinaciones-id").slideUp();
        });

        btnNoSeDescartaAlucinaciones.addEventListener('click', function () {
            btnNoSeDescartaAlucinaciones.classList.add('selected');
            btnYesAlucinaciones.classList.remove('selected');
            btnNoAlucinaciones.classList.remove('selected');
            $("#tipo-alucinaciones-id").slideUp();
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
    const errorText = "Por favor, seleccione una opción.";

    if (datosFormulario.nombre === "") {
        mostrarError(document.getElementById("nombre"), "Por favor, ingrese el nombre del paciente.");
        esValido = false;
    }

    if (datosFormulario.edad === "") {
        mostrarError(document.getElementById("edad"), "Por favor, ingrese la edad del paciente.");
        esValido = false;
    }
    else if (datosFormulario.edad > 120 || datosFormulario.edad < 1) {
        mostrarError(document.getElementById("edad"), "Por favor, ingrese una edad valida.");
        esValido = false;
    }

    if (!datosFormulario.transtorno_autista) {
        document.getElementById("error-transtorno-autista").textContent = errorText;
        document.getElementById("error-transtorno-autista").style.display = "block";
        esValido = false;
    } else {
        document.getElementById("error-transtorno-autista").style.display = "none";
    }

    if (!datosFormulario.transtorno_comunicacion) {
        document.getElementById("error-transtorno-comunicacion").textContent = errorText;
        document.getElementById("error-transtorno-comunicacion").style.display = "block";
        esValido = false;
    } else {
        document.getElementById("error-transtorno-comunicacion").style.display = "none";
    }

    if (!datosFormulario.transtorno_esquizoafectivo) {
        document.getElementById("error-transtorno-esquizoafectivo").textContent = errorText;
        document.getElementById("error-transtorno-esquizoafectivo").style.display = "block";
        esValido = false;
    } else {
        document.getElementById("error-transtorno-esquizoafectivo").style.display = "none";
    }

    if (!datosFormulario.transtorno_depresivo) {
        document.getElementById("error-transtorno-depresivo").textContent = errorText;
        document.getElementById("error-transtorno-depresivo").style.display = "block";
        esValido = false;
    } else {
        document.getElementById("error-transtorno-depresivo").style.display = "none";
    }

    if (!datosFormulario.transtorno_bipolar) {
        document.getElementById("error-transtorno-bipolar").textContent = errorText;
        document.getElementById("error-transtorno-bipolar").style.display = "block";
        esValido = false;
    } else {
        document.getElementById("error-transtorno-bipolar").style.display = "none";
    }

    if (!datosFormulario.antecedentes_familiares) {
        document.getElementById("error-antecedentes-familiares").textContent = errorText;
        document.getElementById("error-antecedentes-familiares").style.display = "block";
        esValido = false;
    } else {
        document.getElementById("error-antecedentes-familiares").style.display = "none";
    }

    // SINTOMAS POSITIVOS
    if (datosFormulario.sintomas_positivos_duracion === "Seleccioná una opción") {
        mostrarError(document.getElementById("sintomas-positivos-duracion"), errorText);
        esValido = false;
    }

    if (!datosFormulario.sintomas_positivos_alucinaciones) {
        document.getElementById("error-sintomas-positivos-alucinaciones").textContent = errorText;
        document.getElementById("error-sintomas-positivos-alucinaciones").style.display = "block";
        esValido = false;
    } else {
        document.getElementById("error-sintomas-positivos-alucinaciones").style.display = "none";
        if (datosFormulario.sintomas_positivos_alucinaciones == "Si" && datosFormulario.sintomas_positivos_tipo_alucinaciones === "") {
            mostrarError(document.getElementById("alucinaciones"), errorText);
            esValido = false;
        }
    }

    if (datosFormulario.sintomas_positivos_tipo_lenguaje === "-1") {
        mostrarError(document.getElementById("lenguaje"), errorText);
        esValido = false;
    }

    if (datosFormulario.sintomas_positivos_tipo_pensamiento === "-1") {
        mostrarError(document.getElementById("pensamiento"), errorText);
        esValido = false;
    }

    if (datosFormulario.sintomas_positivos_tipo_ritmo_pensamiento === "-1") {
        mostrarError(document.getElementById("ritmo-pensamiento"), errorText);
        esValido = false;
    }

    if (datosFormulario.sintomas_positivos_tipo_contenido_pensamiento === "-1") {
        mostrarError(document.getElementById("contenido-pensamiento"), errorText);
        esValido = false;
    }

    // SINTOMAS NEGATIVOS
    if (datosFormulario.sintomas_negativos_duracion === "Seleccioná una opción") {
        mostrarError(document.getElementById("sintomas-negativos-duracion"), errorText);
        esValido = false;
    }

    if (datosFormulario.sintomas_negativos_aspecto === "-1") {
        mostrarError(document.getElementById("aspecto"), errorText);
        esValido = false;
    }

    if (datosFormulario.sintomas_negativos_atencion === "-1") {
        mostrarError(document.getElementById("atencion"), errorText);
        esValido = false;
    }

    if (datosFormulario.sintomas_negativos_actividad === "-1") {
        mostrarError(document.getElementById("actividad"), errorText);
        esValido = false;
    }

    if (datosFormulario.sintomas_negativos_afectividad === "-1") {
        mostrarError(document.getElementById("afectividad"), errorText);
        esValido = false;
    }

    // COMPLEMENTARIOS
    if (!datosFormulario.sustancias) {
        document.getElementById("error-sustancias").textContent = errorText;
        document.getElementById("error-sustancias").style.display = "block";
        esValido = false;
    } else {
        document.getElementById("error-sustancias").style.display = "none";
    }

    if (!datosFormulario.estudios) {
        document.getElementById("error-estudios").textContent = errorText;
        document.getElementById("error-estudios").style.display = "block";
        esValido = false;
    } else {
        document.getElementById("error-estudios").style.display = "none";
        if (datosFormulario.estudios == "Si" && datosFormulario.estudio_causa_natural === "-1") {
            mostrarError(document.getElementById("estudio-causa-natural-opcion"), errorText);
            esValido = false;
        }
    }

    console.log("FIN VALIDAR CAMPOS - esValido? = " + esValido);
    return esValido; // Retorna true si todo es válido, false si hay algún campo incompleto
}


function mostrarError(campo, mensaje) {
    campo.style.borderColor = "red"; // Resalta el campo con borde rojo
    const errorDiv = document.createElement("div");
    errorDiv.className = "error-message";
    errorDiv.style.color = "red";
    errorDiv.textContent = mensaje;
    campo.parentNode.appendChild(errorDiv); // Muestra el mensaje de error debajo del campo
}

function resetearErrores() {
    const campos = document.querySelectorAll("input, select");
    campos.forEach(campo => {
        campo.style.borderColor = ""; // Resetea borde
        const errorMessage = campo.parentNode.querySelector(".error-message");
        if (errorMessage) {
            campo.parentNode.removeChild(errorMessage); // Elimina el mensaje de error
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
    //const sintomasPositivosTipoAlucinaciones = document.getElementById("alucinaciones").value;
    var select1 = document.getElementById("alucinaciones");
    const alucinaciones = [];
    for (var i = 0; i < select1.length; i++) {
        if (select1.options[i].selected) alucinaciones.push(select1.options[i].value);
    }
    const sintomasPositivosTipoAlucinaciones=alucinaciones.toString();
    console.log(sintomasPositivosTipoAlucinaciones);
    
    //const sintomasPositivosTipoAlucinaciones = select1.map( s => Object.values(s)[0] );
  
    //const sintomasPositivosTipoLenguaje = document.getElementById("lenguaje").value;
    select1 = document.getElementById("lenguaje");
    const lenguaje = [];
    for (var i = 0; i < select1.length; i++) {
        if (select1.options[i].selected) lenguaje.push(select1.options[i].value);
    }
    const sintomasPositivosTipoLenguaje=lenguaje.toString();
    console.log(sintomasPositivosTipoLenguaje);
    
    //const sintomasPositivosTipoPensamiento = document.getElementById("pensamiento").value;

    select1 = document.getElementById("pensamiento");
    const pensamiento = [];
    for (var i = 0; i < select1.length; i++) {
        if (select1.options[i].selected) pensamiento.push(select1.options[i].value);
    }
    const sintomasPositivosTipoPensamiento=pensamiento.toString();
    console.log(sintomasPositivosTipoPensamiento);

    const sintomasPositivosTipoRitmoPensamiento = document.getElementById("ritmo-pensamiento").value;

    

    select1 = document.getElementById("contenido-pensamiento");
    const contenido = [];
    for (var i = 0; i < select1.length; i++) {
        if (select1.options[i].selected) contenido.push(select1.options[i].value);
    }
    const sintomasPositivosTipoContenidoPensamiento=contenido.toString();
    console.log(sintomasPositivosTipoContenidoPensamiento);
    //const sintomasPositivosTipoContenidoPensamiento = document.getElementById("contenido-pensamiento").value;

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

        $("#justificacion-title").click(function () {
            $("#justificacion").toggle(); // Alternar la visibilidad del contenido

            // Alternar la clase 'collapsed' para el título
            $(this).toggleClass('collapsed');

            // Cambiar el ícono según el estado de colapso
            if ($("#justificacion").is(":visible")) {
                $("#justificacion-icon").removeClass('fa-chevron-down').addClass('fa-chevron-up');
            } else {
                $("#justificacion-icon").removeClass('fa-chevron-up').addClass('fa-chevron-down');
            }
        });

        $("#recomendacion-title").click(function () {
            $("#recomendacion").toggle(); // Alternar la visibilidad del contenido

            // Alternar la clase 'collapsed' para el título
            $(this).toggleClass('collapsed');

            // Cambiar el ícono según el estado de colapso
            if ($("#recomendacion").is(":visible")) {
                $("#recomendacion-icon").removeClass('fa-chevron-down').addClass('fa-chevron-up');
            } else {
                $("#recomendacion-icon").removeClass('fa-chevron-up').addClass('fa-chevron-down');
            }
        });

        $.ajax({
            type: "POST",
            url: "/diagnosticar", // La URL del Controller
            contentType: "application/json",
            data: jsonString,
            success: function (response) {
                console.log("Respuesta: ", JSON.stringify(response));
                document.getElementById("nombre-paciente").textContent = datosFormulario.nombre;

                let posibilidad = response.posibilidad;
                // Mostrar el elemento correspondiente según el valor de "posibilidad"
                // Ocultar todas las secciones primero
                $(".posibilidad-seccion").css("display", "none");

                // Mostrar el elemento correspondiente según el valor de "posibilidad"
                switch (posibilidad) {
                    case "Posible esquizofrenia":
                        $("#posible-esquizofrenia").css("display", "block");
                        break;
                    case "Posible Esquizofrenia Temporal":
                        $("#evaluar-temporal").css("display", "block");
                        break;
                    case "No es posible que tenga esquizofrenia":
                        $("#no-posible-esquizofrenia").css("display", "block");
                        break;
                    default:
                        console.log("Opción de posibilidad desconocida");
                }
                datosFormulario.posibilidad = response.posibilidad;


                // Actualizar el texto de "Recomendaciones" según la respuesta
                let recomendacion = response.recomendacion;
                if (recomendacion != null) {
                    $("#recomendacion-title").css("display", "block");
                    $("#recomendacion").css("display", "block");
                    $("#recomendacion").text(recomendacion ? recomendacion : "-");
                }
                datosFormulario.recomendacion = recomendacion;

                // Actualizar el texto de "Justificacion" según la respuesta
                let justificacion = response.justificacion;
                if (justificacion != null) {
                    $("#justificacion").css("display", "block");
                    // $("#justificacion").text(response.justificacion ? response.justificacion : "No disponible");
                    // Convertir el texto en una lista
                    let justificacionArray = justificacion.split('\n');
                    let listItems = justificacionArray.map(line => `<li>${line}</li>`).join('');
                    $("#justificacion").html(`<ul>${listItems}</ul>`);
                } 
                else {
                    $("#justificacion").text("No disponible");
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
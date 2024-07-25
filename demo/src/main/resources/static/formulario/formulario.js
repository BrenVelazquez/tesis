let datosFormulario = {};

// const imagenInput = document.getElementById('imagen');
// const imagenPreview = document.getElementById('imagen-preview');
// const insertPhotoMessage = document.querySelector('.insert-photo-message');

// imagenInput.addEventListener('change', function () {
//     const file = this.files[0];
//     if (file) {
//         const reader = new FileReader();
//         reader.onload = function (e) {
//             imagenPreview.style.display = 'block';
//             imagenPreview.src = e.target.result;
//             insertPhotoMessage.style.display = 'none';
//         };
//         reader.readAsDataURL(file);
//     } else {
//         imagenPreview.style.display = 'none';
//         insertPhotoMessage.style.display = 'block';
//     }
// });

// insertPhotoMessage.addEventListener('DOMContentLoaded', function () {
//     imagenInput.click();
// });


$(document).ready(function () {
    initializeButtons();

    $("#complementarios-container").load("complementarios.html", function () {
        initializeComplementaryButtons();
    });

    $("#sintomas-positivos-container").load("sintomasPositivos.html", function () {
        initializePositiveButtons(); // Asegúrate de definir esta función en formulario.js
    });

    $("#sintomas-negativos-container").load("sintomasNegativos.html", function () {
        // initializeNegativeButtons(); // Descomenta si necesitas esta inicialización
    });
});


document.addEventListener('DOMContentLoaded', function () {
    // Configuración de la previsualización de la imagen
    const imagenInput = document.getElementById('imagen');
    const imagenPreview = document.getElementById('imagen-preview');
    const insertPhotoMessage = document.querySelector('.insert-photo-message');

    if (imagenInput && imagenPreview) {
        imagenInput.addEventListener('change', function () {
            const file = this.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    imagenPreview.src = e.target.result;
                    imagenPreview.style.display = 'block';
                    if (insertPhotoMessage) {
                        insertPhotoMessage.style.display = 'none';
                    }
                };
                reader.readAsDataURL(file);
            } else {
                imagenPreview.style.display = 'none';
                if (insertPhotoMessage) {
                    insertPhotoMessage.style.display = 'block';
                }
            }
        });
    }

    if (insertPhotoMessage && imagenInput) {
        insertPhotoMessage.addEventListener('click', function () {
            imagenInput.click();
        });
    } else {
        console.error('Elementos de imagen no encontrados.');
    }
});

function mostrarPopup() {
    $("#popup").fadeIn(1000);
}

function mostrarJustificacion() {
    $(".justificacion").fadeIn(1000);
}

function cerrarPopup() {
    $("#message-container").fadeIn();
    setTimeout(function () {
        $("#message-container").fadeOut();
        $("#popup").fadeOut();
        // Redirige al inicio (index.html) en localhost:8080
        window.location.href = "http://192.168.0.13:8080/";
    }, 3000); // Muestra el mensaje durante 3 segundos
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
    const sintomasPositivosDuracion = document.getElementById("sintomas-positivos-duracion").value;
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
    const sintomasNegativosDuracion = document.getElementById("sintomas-negativos-duracion").value;
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
    const estudioCausaNatural = document.getElementById("estudio-causa-natural").value;


    // Crear un objeto con los valores recolectados
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
        estudio_causa_natural: estudioCausaNatural
    };

    const jsonString = JSON.stringify(datosFormulario);
    console.log("Datos del formulario en formato JSON: ", jsonString);

    // Obtener el archivo seleccionado
    let fileInput = document.getElementById('imagen');
    let file = fileInput.files[0];

    // Crear un objeto FormData
    let formData = new FormData();
    formData.append('file', file);

    //Metodo de Subir Imagen
    $.ajax({
        url: '/subirImagen', // La URL del controlador
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function (data) {

            console.log('Imagen subida y guardada con éxito:', data);

            //Metodo de Cargar Paciente
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

        },
        error: function (xhr, status, error) {
            console.error('Error al subir la imagen:', error);
        }
    });
}
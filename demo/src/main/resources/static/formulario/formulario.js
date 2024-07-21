let datosFormulario = {};

const imagenInput = document.getElementById('imagen');
const imagenPreview = document.getElementById('imagen-preview');
const insertPhotoMessage = document.querySelector('.insert-photo-message');

imagenInput.addEventListener('change', function () {
    const file = this.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function (e) {
            imagenPreview.style.display = 'block';
            imagenPreview.src = e.target.result;
            insertPhotoMessage.style.display = 'none';
        };
        reader.readAsDataURL(file);
    } else {
        imagenPreview.style.display = 'none';
        insertPhotoMessage.style.display = 'block';
    }
});

insertPhotoMessage.addEventListener('click', function () {
    imagenInput.click();
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

function mostrarContenedorImagen() {
    console.log("mostrarContenedorImagen");
    const contenedorImagen = document.getElementById("image-container");
    contenedorImagen.style.display = "block !important"; // Mostrar el contenedor de imagen
    
    console.log("contenedorImagen.style.display: " + contenedorImagen.style.display);
}

function ocultarContenedorImagen() {
    const contenedorImagen = document.getElementById("image-container");
    contenedorImagen.style.display = "none"; // Ocultar el contenedor de imagen
}

function initializeButtons() {
    const buttonPairs = [
        { yes: 'transtorno-autista-si', no: 'transtorno-autista-no' },
        { yes: 'transtorno-comunicacion-si', no: 'transtorno-comunicacion-no' },
        { yes: 'transtorno-esquizoafectivo-si', no: 'transtorno-esquizoafectivo-no' },
        { yes: 'transtorno-depresivo-si', no: 'transtorno-depresivo-no' },
        { yes: 'transtorno-bipolar-si', no: 'transtorno-bipolar-no' },
        { yes: 'estudios-si', no: 'estudios-no' },
        { yes: 'sustancias-si', no: 'sustancias-no' },
        { yes: 'antecedentes-familiares-si', no: 'antecedentes-familiares-no' }
    ];

    buttonPairs.forEach(pair => {
        const btnYes = document.getElementById(pair.yes);
        const btnNo = document.getElementById(pair.no);
        const estudiosSiButton = document.getElementById("estudios-si");
        const estudiosNoButton = document.getElementById("estudios-no");
        estudiosSiButton.addEventListener("click", mostrarContenedorImagen);
        estudiosNoButton.addEventListener("click", ocultarContenedorImagen);

        if (btnYes && btnNo) {
            btnYes.addEventListener('click', function () {
                btnYes.classList.remove('selected');
                btnNo.classList.remove('selected');
                this.classList.add('selected');

                // console.log("btnYes: " + this.id);
                //Para que se desplieguen los campos de estudio
                if (this.id === 'estudios-si') {
                    // this.classList.add('selected');
                    $("#estudio-comentario").slideDown();
                    $("#estudio-causa-natural").slideDown();
                    // $("#image-container").css('display', 'block');
                    // $("#image-container").slideDown();
                }
            });

            btnNo.addEventListener('click', function () {
                btnYes.classList.remove('selected');
                btnNo.classList.remove('selected');
                this.classList.add('selected');

                //Para que se dejen de mostrar los campos de estudio
                if (this.id === 'estudios-no') {
                    // this.classList.add('selected');
                    $("#estudio-comentario").slideUp();
                    $("#estudio-causa-natural").slideUp();
                    // $("#image-container").css('display', 'none');
                    // $("#image-container").slideUp();
                }
            });
        } else {
            console.error(`Botones no encontrados: ${pair.yes} o ${pair.no}`);
        }
    });
}

function determinar_diagnostico() {

    console.log("INICIO FUNCION DIAGNOSTICO()");

    // Obtener los valores de los campos del formulario
    const edad = document.getElementById("edad").value;
    const sexo = document.getElementById("sexo").value;
    const nombre = document.getElementById("nombre").value;

    const transtornoAutista = document.querySelector("#transtorno-autista-si.selected") ? "Si" : document.querySelector("#transtorno-autista-no.selected") ? "No" : "";
    const transtornoComunicacion = document.querySelector("#transtorno-comunicacion-si.selected") ? "Si" : document.querySelector("#transtorno-comunicacion-no.selected") ? "No" : "";
    const transtornoEsquizoafectivo = document.querySelector("#transtorno-esquizoafectivo-si.selected") ? "Si" : document.querySelector("#transtorno-esquizoafectivo-no.selected") ? "No" : "";
    const transtornoDepresivo = document.querySelector("#transtorno-depresivo-si.selected") ? "Si" : document.querySelector("#transtorno-depresivo-no.selected") ? "No" : "";
    const transtornoBipolar = document.querySelector("#transtorno-bipolar-si.selected") ? "Si" : document.querySelector("#transtorno-bipolar-no.selected") ? "No" : "";
    const antecedentesFamiliares = document.querySelector("#antecedentes-familiares-si.selected") ? "Si" : document.querySelector("#antecedentes-familiares-no.selected") ? "No" : "";
    const estudios = document.querySelector("#estudios-si.selected") ? "Si" : document.querySelector("#estudios-no.selected") ? "No" : "";
    const sustancias = document.querySelector("#sustancias-si.selected") ? "Si" : document.querySelector("#sustancias-no.selected") ? "No" : "";
    // Obtener el valor de inmunodepresión
    // const transtornoAutista = document.querySelector("#transtorno-autista-si.selected")
    //     ? "Si"
    //     : document.querySelector("#transtorno-autista-no.selected")
    //         ? "No"
    //         : "";

    // // Obtener el valor de transtorno de comunicacion
    // const transtornoComunicacion = document.querySelector("#transtorno-comunicacion-si.selected")
    //     ? "Si"
    //     : document.querySelector("#transtorno-comunicacion-no.selected")
    //         ? "No"
    //         : "";

    // // Obtener el valor de transtorno esquizoafectivo
    // const transtornoEsquizoafectivo = document.querySelector("#transtorno-esquizoafectivo-si.selected")
    //     ? "Si"
    //     : document.querySelector("#transtorno-esquizoafectivo-no.selected")
    //         ? "No"
    //         : "";

    // // Obtener el valor de transtorno depresivo
    // const transtornoDepresivo = document.querySelector("#transtorno-depresivo-si.selected")
    //     ? "Si"
    //     : document.querySelector("#transtorno-depresivo-no.selected")
    //         ? "No"
    //         : "";

    // // Obtener el valor de transtorno bipolar
    // const transtornoBipolar = document.querySelector("#transtorno-bipolar-si.selected")
    //     ? "Si"
    //     : document.querySelector("#transtorno-bipolar-no.selected")
    //         ? "No"
    //         : "";
    // // Obtener el valor de antecedentes familiares
    // const antecedentesFamiliares = document.querySelector("#antecedentes-familiares-si.selected")
    //     ? "Si"
    //     : document.querySelector("#antecedentes-familiares-no.selected")
    //         ? "No"
    //         : "";

    // // Obtener el valor de estudios
    // const estudios = document.querySelector("#estudios-si.selected")
    //     ? "Si"
    //     : document.querySelector("#estudios-no.selected")
    //         ? "No"
    //         : "";

    // // Obtener el valor de sustancias
    // const sustancias = document.querySelector("#sustancias-si.selected")
    //     ? "Si"
    //     : document.querySelector("#sustancias-no.selected")
    //         ? "No"
    //         : "";



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
        estudios: estudios,
        sustancias: sustancias,
        antecedentes_familiares: antecedentesFamiliares,
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
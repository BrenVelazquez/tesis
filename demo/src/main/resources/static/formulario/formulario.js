let datosFormulario = {};
let nombreMedico;
let imagenPath;
$(document).ready(function () {
    initializeButtons();

    $("#complementarios-container").load("complementarios.html", function () {
        initializeComplementaryButtons();
        insertarImagen();
    });

    $("#sintomas-positivos-container").load("sintomasPositivos.html", function () {
        initializePositiveButtons();
    });

    $("#sintomas-negativos-container").load("sintomasNegativos.html", function () {
        initializeNegativeButtons();
    });

    $("#diagnostico-popup").load("diagnostico.html", function () {
        $('#close-popup').on('click', function () {
            cerrarPopup();
        });
    });

    const medicoData = JSON.parse(localStorage.getItem('medico'));
    if (medicoData) {
        document.getElementById('username').textContent = `Dr/Dra: ${medicoData.nombre_medico + " " + medicoData.apellido_medico}`;
    }
});

document.addEventListener('DOMContentLoaded', function () { });

// region imagen
function insertarImagen() {
    const imagenInput = document.getElementById('imagen');
    const imagenPreview = document.getElementById('imagen-preview');
    const insertPhotoMessage = document.getElementById('insert-photo-message');

    if (!imagenInput || !imagenPreview || !insertPhotoMessage) {
        console.error("Uno o más elementos no existen en el DOM.");
        return;
    }

    insertPhotoMessage.addEventListener('click', function () {
        imagenInput.click();
    })

    imagenInput.addEventListener('change', function () {
        const file = this.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function (e) {
                imagenPreview.src = e.target.result;
                imagenPreview.style.display = 'block';
                insertPhotoMessage.style.display = 'none';
                imagenPath = e.target.result;
                console.log('Path de la imagen:', imagenPath);
            };
            reader.readAsDataURL(file);
        } else {
            imagenPreview.style.display = 'none';
            insertPhotoMessage.style.display = 'block';
        }
    });
}
// endregion imagen

// region popup
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
    }, 1000);
}
// endregion popup

function initializeButtons() {
    const buttonPairs = [
        { yes: 'trastorno-autista-si', no: 'trastorno-autista-no' },
        { yes: 'trastorno-comunicacion-si', no: 'trastorno-comunicacion-no' },
        { yes: 'trastorno-esquizoafectivo-si', no: 'trastorno-esquizoafectivo-no' },
        { yes: 'trastorno-depresivo-si', no: 'trastorno-depresivo-no' },
        { yes: 'trastorno-bipolar-si', no: 'trastorno-bipolar-no' },
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

function initializeNegativeButtons() {
    const buttonPairs = [
        { yes: 'bajo-funcionamiento-si', no: 'bajo-funcionamiento-no' },
    ];

    buttonPairs.forEach(pair => {
        const btnYes = document.getElementById(pair.yes);
        const btnNo = document.getElementById(pair.no);

        if (btnYes && btnNo) {
            btnYes.addEventListener('click', function () {
                btnYes.classList.remove('selected');
                btnNo.classList.remove('selected');
                this.classList.add('selected');

                if (this.id === 'bajo-funcionamiento-si') {
                    $("#bajo-funcionamiento-comentario-id").slideDown();
                }
            });

            btnNo.addEventListener('click', function () {
                btnYes.classList.remove('selected');
                btnNo.classList.remove('selected');
                this.classList.add('selected');

                if (this.id === 'bajo-funcionamiento-no') {
                    $("#bajo-funcionamiento-comentario-id").slideUp();
                }
            });
        } else {
            console.error(`Nagativos - Botones no encontrados: ${pair.yes} o ${pair.no}`);
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

function volver() {
    window.location.href = "/home";
}

document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('btnCerrarSesion').addEventListener('click', function () {
        localStorage.removeItem('medico');
        window.location.href = '/';
    });
});

// region validar campos
function validar_campos(datosFormulario) {
    resetearErrores();
    let esValido = true;
    const errorText = "Por favor, seleccione una opción.";

    if (datosFormulario.nombre == "") {
        mostrarError(document.getElementById("nombre"), "Por favor, ingrese el nombre del paciente.");
        esValido = false;
    }

    if (datosFormulario.edad == "") {
        mostrarError(document.getElementById("edad"), "Por favor, ingrese la edad del paciente.");
        esValido = false;
    }
    else if (datosFormulario.edad > 120 || datosFormulario.edad < 1) {
        mostrarError(document.getElementById("edad"), "Por favor, ingrese una edad valida.");
        esValido = false;
    }

    if (datosFormulario.sexo == "-1") {
        mostrarError(document.getElementById("sexo"), "Por favor, ingrese el sexo del paciente.");
        esValido = false;
    }

    if (!datosFormulario.trastorno_autista) {
        document.getElementById("error-trastorno-autista").textContent = errorText;
        document.getElementById("error-trastorno-autista").style.display = "block";
        esValido = false;
    } else {
        document.getElementById("error-trastorno-autista").style.display = "none";
    }

    if (!datosFormulario.trastorno_comunicacion) {
        document.getElementById("error-trastorno-comunicacion").textContent = errorText;
        document.getElementById("error-trastorno-comunicacion").style.display = "block";
        esValido = false;
    } else {
        document.getElementById("error-trastorno-comunicacion").style.display = "none";
    }

    if (!datosFormulario.trastorno_esquizoafectivo) {
        document.getElementById("error-trastorno-esquizoafectivo").textContent = errorText;
        document.getElementById("error-trastorno-esquizoafectivo").style.display = "block";
        esValido = false;
    } else {
        document.getElementById("error-trastorno-esquizoafectivo").style.display = "none";
    }

    if (!datosFormulario.trastorno_depresivo) {
        document.getElementById("error-trastorno-depresivo").textContent = errorText;
        document.getElementById("error-trastorno-depresivo").style.display = "block";
        esValido = false;
    } else {
        document.getElementById("error-trastorno-depresivo").style.display = "none";
    }

    if (!datosFormulario.trastorno_bipolar) {
        document.getElementById("error-trastorno-bipolar").textContent = errorText;
        document.getElementById("error-trastorno-bipolar").style.display = "block";
        esValido = false;
    } else {
        document.getElementById("error-trastorno-bipolar").style.display = "none";
    }

    if (!datosFormulario.antecedentes_familiares) {
        document.getElementById("error-antecedentes-familiares").textContent = errorText;
        document.getElementById("error-antecedentes-familiares").style.display = "block";
        esValido = false;
    } else {
        document.getElementById("error-antecedentes-familiares").style.display = "none";
    }

    // SINTOMAS POSITIVOS
    if (datosFormulario.sintomas_positivos_duracion == "Seleccioná una opción") {
        mostrarError(document.getElementById("sintomas-positivos-duracion"), errorText);
        esValido = false;
    }

    if (!datosFormulario.sintomas_positivos_alucinaciones) {
        document.getElementById("error-sintomas-positivos-alucinaciones").textContent = errorText;
        document.getElementById("error-sintomas-positivos-alucinaciones").style.display = "block";
        esValido = false;
    } else {
        document.getElementById("error-sintomas-positivos-alucinaciones").style.display = "none";
        var cboxes = document.getElementsByName('alucinaciones[]');
        var len = cboxes.length;
        const alucinaciones = [];
        for (var i = 0; i < len; i++) {
            if (cboxes[i].checked) alucinaciones.push(cboxes[i].value);
        }
        const sintomasPositivosTipoAlucinaciones = alucinaciones.toString();


        if (datosFormulario.sintomas_positivos_alucinaciones == "Si" && sintomasPositivosTipoAlucinaciones == '') {
            mostrarError(document.getElementById("alucinaciones"), errorText);
            esValido = false;
        }
    }

    var cboxes = document.getElementsByName('lenguaje[]');
    var len = cboxes.length;
    const lenguaje = [];
    for (var i = 0; i < len; i++) {
        if (cboxes[i].checked) lenguaje.push(cboxes[i].value);
    }
    const sintomasPositivosTipoLenguaje = lenguaje.toString();


    if (sintomasPositivosTipoLenguaje == '') {

        mostrarError(document.getElementById("lenguaje"), errorText);
        esValido = false;
    }

    cboxes = document.getElementsByName('pensamiento[]');
    len = cboxes.length;
    const pensamiento = [];
    for (var i = 0; i < len; i++) {
        if (cboxes[i].checked) pensamiento.push(cboxes[i].value);
    }
    const sintomas_positivos_tipo_pensamiento = pensamiento.toString();

    if (sintomas_positivos_tipo_pensamiento == '') {
        mostrarError(document.getElementById("pensamiento"), errorText);
        esValido = false;
    }

    if (datosFormulario.sintomas_positivos_tipo_ritmo_pensamiento.nombre === "-1") {
        mostrarError(document.getElementById("ritmo-pensamiento"), errorText);
        esValido = false;
    }

    cboxes = document.getElementsByName('contenido-pensamiento[]');
    len = cboxes.length;
    const contenido = [];
    for (var i = 0; i < len; i++) {
        if (cboxes[i].checked) contenido.push(cboxes[i].value);
    }
    const sintomas_positivos_tipo_contenido_pensamiento = contenido.toString();


    if (sintomas_positivos_tipo_contenido_pensamiento == '') {
        mostrarError(document.getElementById("contenido-pensamiento"), errorText);
        esValido = false;
    }

    // SINTOMAS NEGATIVOS
    if (datosFormulario.sintomas_negativos_duracion == "Seleccioná una opción") {
        mostrarError(document.getElementById("sintomas-negativos-duracion"), errorText);
        esValido = false;
    }

    cboxes = document.getElementsByName('aspecto[]');
    len = cboxes.length;
    const aspecto = [];
    for (var i = 0; i < len; i++) {
        if (cboxes[i].checked) aspecto.push(cboxes[i].value);
    }
    const sintomas_negativos_aspecto = aspecto.toString();

    if (sintomas_negativos_aspecto == '') {
        mostrarError(document.getElementById("aspecto"), errorText);
        esValido = false;
    }

    if (datosFormulario.sintomas_negativos_atencion.nombre == "-1") {
        mostrarError(document.getElementById("atencion"), errorText);
        esValido = false;
    }

    if (datosFormulario.sintomas_negativos_actividad.nombre == "-1") {
        mostrarError(document.getElementById("actividad"), errorText);
        esValido = false;
    }

    cboxes = document.getElementsByName('afectividad[]');
    len = cboxes.length;
    const afectividad = [];
    for (var i = 0; i < len; i++) {
        if (cboxes[i].checked) afectividad.push(cboxes[i].value);
    }
    const sintomas_negativos_afectividad = afectividad.toString();

    if (sintomas_negativos_afectividad == '') {
        mostrarError(document.getElementById("afectividad"), errorText);
        esValido = false;
    }

    if (!datosFormulario.sintomas_negativos_bajo_funcionamiento) {
        document.getElementById("error-bajo-funcionamiento").textContent = errorText;
        document.getElementById("error-bajo-funcionamiento").style.display = "block";
        esValido = false;
    } else {
        document.getElementById("error-bajo-funcionamiento").style.display = "none";
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
    return esValido;
}


function mostrarError(campo, mensaje) {
    campo.style.borderColor = "red";
    const errorDiv = document.createElement("div");
    errorDiv.className = "error-message";
    errorDiv.style.color = "red";
    errorDiv.textContent = mensaje;
    campo.parentNode.appendChild(errorDiv);
}

function resetearErrores() {
    const errores = document.querySelectorAll(".error-message");
    errores.forEach(error => {
        const campo = error.parentNode.querySelector("input, select, .checkboxes");
        campo.style.borderColor = "";
        error.parentNode.removeChild(error);
    });
}
// endregion validar campos

// region obtenerDatosFormulario
function obtenerDatosFormulario() {
    const edad = document.getElementById("edad").value;
    const sexo = document.getElementById("sexo").value;
    const nombre = document.getElementById("nombre").value;

    const trastornoAutista =
        document.querySelector("#trastorno-autista-si.selected") ? "Si" :
            document.querySelector("#trastorno-autista-no.selected") ? "No" : "";
    const trastornoComunicacion =
        document.querySelector("#trastorno-comunicacion-si.selected") ? "Si" :
            document.querySelector("#trastorno-comunicacion-no.selected") ? "No" : "";
    const trastornoEsquizoafectivo =
        document.querySelector("#trastorno-esquizoafectivo-si.selected") ? "Si" :
            document.querySelector("#trastorno-esquizoafectivo-no.selected") ? "No" : "";
    const trastornoDepresivo =
        document.querySelector("#trastorno-depresivo-si.selected") ? "Si" :
            document.querySelector("#trastorno-depresivo-no.selected") ? "No" : "";
    const trastornoBipolar =
        document.querySelector("#trastorno-bipolar-si.selected") ? "Si" :
            document.querySelector("#trastorno-bipolar-no.selected") ? "No" : "";
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

    var cboxes = document.getElementsByName('alucinaciones[]');
    var len = cboxes.length;
    const alucinaciones = [];

    for (var i = 0; i < len; i++) {
        if (cboxes[i].checked) alucinaciones.push(cboxes[i].value);
    }

    const sintomasPositivosTipoAlucinaciones = alucinaciones.toString();

    var cboxes = document.getElementsByName('lenguaje[]');
    var len = cboxes.length;
    const lenguaje = [];
    for (var i = 0; i < len; i++) {
        if (cboxes[i].checked) lenguaje.push(cboxes[i].value);
    }
    const sintomasPositivosTipoLenguaje = lenguaje.toString();

    cboxes = document.getElementsByName('pensamiento[]');
    len = cboxes.length;
    const pensamiento = [];
    for (var i = 0; i < len; i++) {
        if (cboxes[i].checked) pensamiento.push(cboxes[i].value);
    }
    const sintomasPositivosTipoPensamiento = pensamiento.toString();
    const sintomasPositivosTipoRitmoPensamiento = document.getElementById("ritmo-pensamiento").value;

    cboxes = document.getElementsByName('contenido-pensamiento[]');
    len = cboxes.length;
    const contenido = [];
    for (var i = 0; i < len; i++) {
        if (cboxes[i].checked) contenido.push(cboxes[i].value);
    }
    const sintomasPositivosTipoContenidoPensamiento = contenido.toString();

    // sintomas negativos
    const selectedSNDuracion = document.getElementById("sintomas-negativos-duracion");
    const sintomasNegativosDuracion = selectedSNDuracion.options[selectedSNDuracion.selectedIndex].text;

    cboxes = document.getElementsByName('aspecto[]');
    len = cboxes.length;
    const aspecto = [];
    for (var i = 0; i < len; i++) {
        if (cboxes[i].checked) aspecto.push(cboxes[i].value);
    }
    const sintomasNegativosAspecto = aspecto.toString();

    const sintomasNegativosAtencion = document.getElementById("atencion").value;
    const sintomasNegativosActividad = document.getElementById("actividad").value;

    cboxes = document.getElementsByName('afectividad[]');
    len = cboxes.length;
    const afectividad = [];
    for (var i = 0; i < len; i++) {
        if (cboxes[i].checked) afectividad.push(cboxes[i].value);
    }
    const sintomasNegativosAfectividad = afectividad.toString();

    const bajoFuncionamiento =
        document.querySelector("#bajo-funcionamiento-si.selected") ? "Si" :
            document.querySelector("#bajo-funcionamiento-no.selected") ? "No" : "";

    const bajoFuncionamientoComentario = document.getElementById("bajo-funcionamiento-comentario").value;

    //complementarios
    const sustancias =
        document.querySelector("#sustancias-si.selected") ? "Si" :
            document.querySelector("#sustancias-no.selected") ? "No" : "";
    const estudios =
        document.querySelector("#estudios-si.selected") ? "Si" :
            document.querySelector("#estudios-no.selected") ? "No" : "";
    const estudioCausaNatural = document.getElementById("estudio-causa-natural-opcion").value;

    const estudioComentario = document.getElementById("estudio-comentario-text").value;
    const imagen = imagenPath;
    //FALTA IMAGEN

    //ARRAYS
    /*const listaAlucinaciones=[];
    if(sintomasPositivosTipoAlucinaciones!=''){
        listaAlucinaciones = sintomasPositivosTipoAlucinaciones.split(',');
    }else{
        listaAlucinaciones.push(sintomasPositivosAlucinaciones);
    }

    const lAlucinaciones = listaAlucinaciones.map(alucinacion => `new Alucionacion("${alucinacion}")`);*/

    let listaAlucinaciones = [];
    if (sintomasPositivosAlucinaciones == "Si") {
        listaAlucinaciones = sintomasPositivosTipoAlucinaciones.split(',').map((alucinacion, index) => ({
            id: 0,
            nombre: alucinacion.trim()
        }));
    } else {
        if (sintomasPositivosAlucinaciones == "No")
            listaAlucinaciones.push({
                id: 0,
                nombre: "NO_PRESENTA"
            });
        else {
            listaAlucinaciones.push({
                id: 0,
                nombre: "NO_SE_DESCARTA"
            });
        }
    }
    console.log("ALUCINACIONES", JSON.stringify(listaAlucinaciones, null, 2));
    console.log(sintomasPositivosAlucinaciones);
    /*
    const listaLenguaje = sintomasPositivosTipoLenguaje.split(',');
    const lLenguajes= listaLenguaje.map(lenguaje => `new Lenguaje("${lenguaje}")`);
*/
    let listaLenguajes = [];
    listaLenguajes = sintomasPositivosTipoLenguaje.split(',').map((lenguaje, index) => ({
        id: 0,
        nombre: lenguaje.trim()
    }));
    /*const listaPensamiento = sintomasPositivosTipoPensamiento.split(',');
    const lPensamientos= listaPensamiento.map(pensamiento => `new Pensamiento("${pensamiento}")`);*/
    let listaPensamientos = [];
    listaPensamientos = sintomasPositivosTipoPensamiento.split(',').map((pensamiento, index) => ({
        id: 0,
        nombre: pensamiento.trim()
    }));

    /*const listaContenido = sintomasPositivosTipoContenidoPensamiento.split(',');
    const lContenidos= listaContenido.map(contenido => `new Contenido("${contenido}")`);*/
    let listaContenidos = [];
    listaContenidos = sintomasPositivosTipoContenidoPensamiento.split(',').map((contenido, index) => ({
        id: 0,
        nombre: contenido.trim()
    }));

    /*const listaAspecto = sintomasNegativosAspecto.split(',');
    const lAspectos= listaAspecto.map(aspecto => `new Aspecto("${aspecto}")`);*/
    let listaAspectos = [];
    listaAspectos = sintomasNegativosAspecto.split(',').map((aspecto, index) => ({
        id: 0,
        nombre: aspecto.trim()
    }));

    /*const listaAfectividad = sintomasNegativosAfectividad.split(',');
    const lAfectividades= listaAfectividad.map(afectividad => `new Afectividad("${afectividad}")`);*/
    let listaAfectividades = [];
    listaAfectividades = sintomasNegativosAfectividad.split(',').map((afectividad, index) => ({
        id: 0,
        nombre: afectividad.trim()
    }));


    const ritmoPensamiento = {
        id: 0,
        nombre: sintomasPositivosTipoRitmoPensamiento,
    };
    const atencion = {
        id: 0,
        nombre: sintomasNegativosAtencion,
    };
    const actividad = {
        id: 0,
        nombre: sintomasNegativosActividad,
    };


    //LISTAS

    datosFormulario = {
        edad: edad,
        sexo: sexo,
        nombre: nombre,
        trastorno_autista: trastornoAutista,
        trastorno_comunicacion: trastornoComunicacion,
        trastorno_esquizoafectivo: trastornoEsquizoafectivo,
        trastorno_bipolar: trastornoBipolar,
        trastorno_depresivo: trastornoDepresivo,
        antecedentes_familiares: antecedentesFamiliares,

        sintomas_positivos_duracion: sintomasPositivosDuracion,
        sintomas_positivos_alucinaciones: sintomasPositivosAlucinaciones,
        sintomas_positivos_tipo_alucinaciones: listaAlucinaciones,
        sintomas_positivos_tipo_alucinaciones_String: sintomasPositivosTipoAlucinaciones,
        sintomas_positivos_tipo_lenguaje: listaLenguajes,
        sintomas_positivos_tipo_lenguaje_String: sintomasPositivosTipoLenguaje,
        sintomas_positivos_tipo_pensamiento: listaPensamientos,
        sintomas_positivos_tipo_pensamiento_String: sintomasPositivosTipoPensamiento,
        sintomas_positivos_tipo_ritmo_pensamiento: ritmoPensamiento,
        sintomas_positivos_tipo_ritmo_pensamiento_string: sintomasPositivosTipoRitmoPensamiento,
        sintomas_positivos_tipo_contenido_pensamiento: listaContenidos,
        sintomas_positivos_tipo_contenido_pensamiento_String: sintomasPositivosTipoContenidoPensamiento,
        // sintomas negativos,
        sintomas_negativos_duracion: sintomasNegativosDuracion,
        sintomas_negativos_aspecto: listaAspectos,
        sintomas_negativos_tipo_aspecto_String: sintomasNegativosAspecto,
        sintomas_negativos_atencion: atencion,
        sintomas_negativos_actividad: actividad,
        sintomas_negativos_atencion_string: sintomasNegativosAtencion,
        sintomas_negativos_actividad_string: sintomasNegativosActividad,
        sintomas_negativos_afectividad: listaAfectividades,
        sintomas_negativos_tipo_afectividad_String: sintomasNegativosAfectividad,
        sintomas_negativos_bajo_funcionamiento: bajoFuncionamiento,
        sintomas_negativos_bajo_funcionamiento_comentario: bajoFuncionamientoComentario,
        // complementarios
        sustancias: sustancias,
        estudios: estudios,
        estudio_causa_natural: estudioCausaNatural,
        estudio_comentario: estudioComentario,
        imagen: imagen,

        // FALTA IMAGEN
    };

    return datosFormulario
}
// endregion obtenerDatosFormulario

// region determinar_diagnostico
function determinar_diagnostico() {
    console.log("INICIO FUNCION DIAGNOSTICO()");

    const datosFormulario = obtenerDatosFormulario();
    let datosReglas = {
        nombre: datosFormulario.nombre,
        edad: datosFormulario.edad,
        sexo: datosFormulario.sexo,
        antecedentes_familiares: datosFormulario.antecedentes_familiares,
        trastorno_autista: datosFormulario.trastorno_autista,
        trastorno_comunicacion: datosFormulario.trastorno_Comunicacion,
        trastorno_esquizoafectivo: datosFormulario.trastorno_esquizoafectivo,
        trastorno_bipolar: datosFormulario.trastorno_bipolar,
        trastorno_depresivo: datosFormulario.trastorno_depresivo,
        sustancias: datosFormulario.sustancias,
        estudio_causa_natural: datosFormulario.estudio_causa_natural,
        sintomas_positivos_duracion: datosFormulario.sintomas_positivos_duracion,
        sintomas_positivos_tipo_alucinaciones: datosFormulario.sintomas_positivos_tipo_alucinaciones_String,
        sintomas_positivos_alucinaciones: datosFormulario.sintomas_positivos_alucinaciones,
        sintomas_positivos_tipo_lenguaje: datosFormulario.sintomas_positivos_tipo_lenguaje_String,
        sintomas_positivos_tipo_pensamiento: datosFormulario.sintomas_positivos_tipo_pensamiento_String,
        sintomas_positivos_tipo_ritmo_pensamiento: datosFormulario.sintomas_positivos_tipo_ritmo_pensamiento_string,
        sintomas_positivos_tipo_contenido_pensamiento: datosFormulario.sintomas_positivos_tipo_contenido_pensamiento_String,
        sintomas_negativos_duracion: datosFormulario.sintomas_negativos_duracion,
        sintomas_negativos_aspecto: datosFormulario.sintomas_negativos_tipo_aspecto_String,
        sintomas_negativos_atencion: datosFormulario.sintomas_negativos_atencion_string,
        sintomas_negativos_actividad: datosFormulario.sintomas_negativos_actividad_string,
        sintomas_negativos_afectividad: datosFormulario.sintomas_negativos_tipo_afectividad_String,
        sintomas_negativos_bajo_funcionamiento: datosFormulario.sintomas_negativos_bajo_funcionamiento,
        sintomas_negativos_bajo_funcionamiento_comentario: datosFormulario.sintomas_negativos_bajo_funcionamiento_comentario,
    };
    const jsonString = JSON.stringify(datosReglas);
    console.log("Datos del formulario en formato JSON: ", jsonString);
    if (validar_campos(datosFormulario)) {
        $("#justificacion-title").click(function () {
            $("#justificacion").toggle();

            $(this).toggleClass('collapsed');

            if ($(this).hasClass('collapsed')) {
                $("#justificacion-icon").css("transform", "rotate(-90deg)");
            } else {
                $("#justificacion-icon").css("transform", "rotate(0deg)");
            }
        });

        $("#recomendacion-title").click(function () {
            $("#recomendacion").toggle();
            $(this).toggleClass('collapsed');

            if ($(this).hasClass('collapsed')) {
                $("#recomendacion-icon").css("transform", "rotate(-90deg)");
            } else {
                $("#recomendacion-icon").css("transform", "rotate(0deg)");
            }
        });

        $.ajax({
            type: "POST",
            url: "/diagnosticar",
            contentType: "application/json",
            data: jsonString,
            success: function (response) {
                console.log("Respuesta: ", JSON.stringify(response));
                document.getElementById("nombre-paciente").textContent = datosFormulario.nombre;

                let diagnostico = response.diagnostico;
                $(".diagnostico-seccion").css("display", "none");
                switch (diagnostico) {
                    case "Esquizofrenia":
                        $("#esquizofrenia").css("display", "block");
                        break;
                    case "Evaluar esquizofrenia temporal":
                        $("#evaluar-temporal").css("display", "block");
                        break;
                    case "Esquizofrenia no posible":
                        $("#no-posible-esquizofrenia").css("display", "block");
                        break;
                    default:
                        console.log("Opción de diagnostico desconocida");
                }

                let recomendacion = response.recomendacion;
                if (recomendacion != null) {
                    $("#recomendacion-title").css("display", "block");
                    $("#recomendacion").css("display", "block");
                    $("#recomendacion").text(recomendacion ? recomendacion : "-");
                }

                let justificacion = response.justificacion;
                if (justificacion != null) {
                    $("#justificacion").css("display", "block");
                    let justificacionArray = justificacion.split('\n');
                    let listItems = justificacionArray.map(line => `<li>${line}</li>`).join('');
                    $("#justificacion").html(`<ul>${listItems}</ul>`);
                }
                else {
                    $("#justificacion").text("No disponible");
                }
                // Actualizar los campos del formulario con la respuesta del diagnóstico
                datosFormulario.diagnostico = response.diagnostico;
                datosFormulario.puntaje = response.puntaje;
                datosFormulario.recomendacion = response.recomendacion || "No disponible";
                datosFormulario.justificacion = response.justificacion || "No disponible";
                datosFormulario.reglas = response.reglas_ejecutadas || "No disponible";

                console.log('DATOS DEL FORMULARIO ACTUALIZADOS', datosFormulario);
                // actualizar datosFormulario
                window.datosFormulario = datosFormulario;
                mostrarPopup();
            },
            error: function (error) {
                console.error("Error en la solicitud de diagnósitco: " + JSON.stringify(error));
            }
        });

    }
    console.log("FIN FUNCION DIAGNOSTICO()");

}
// endregion determinar_diagnostico


// region snackbar
function mostrarSnackbar(message, success) {
    const snackbar = document.getElementById("snackbar");

    if (success) {
        snackbar.innerHTML = '<i class="fas fa-check-circle"></i> ' + message;
        snackbar.className = "show snackbar-success";
    } else {
        snackbar.innerHTML = '<i class="fas fa-exclamation-circle"></i> ' + message;
        snackbar.className = "show snackbar-error";
    }

    setTimeout(function () {
        volver();
        snackbar.className = snackbar.className.replace("show", "");
    }, 3000);
}

function bloquearBotones() {
    document.getElementById('confirmar-diagnostico').disabled = true;
    document.getElementById('rechazar-diagnostico').disabled = true;
}

function mostrarLoaderBoton(boton) {
    boton.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Guardando...';
    boton.disabled = true;
}
// endregion snackbar

// region guardarRegistro
async function guardarRegistro(estado) {
    console.log("### guardarRegistro ###");

    const datosFormulario = window.datosFormulario || obtenerDatosFormulario();
    datosFormulario.estado = estado;

    const comentarioMedico = document.getElementById("comentarios-medicos").value;
    const justificacionRechazo = document.getElementById("justificacion-rechazo").value;
    datosFormulario.comentario_medico = comentarioMedico || "";
    datosFormulario.justificacion_rechazo = justificacionRechazo || "";

    const jsonString = JSON.stringify(datosFormulario);
    console.log("Datos del formulario en formato JSON: ", jsonString);

    bloquearBotones();
    const botonClickeado = estado === 'Confirmado'
        ? document.getElementById('confirmar-diagnostico')
        : document.getElementById('confirmar-rechazar-diagnostico');

    mostrarLoaderBoton(botonClickeado);

    // Obtener el archivo seleccionado
    // let fileInput = document.getElementById('imagen');
    // let file = fileInput.files[0];

    // // Crear un objeto FormData
    // let formData = new FormData();
    // formData.append('file', file);

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

    //Metodo de Cargar Paciente
    console.log('###### Metodo de Cargar Paciente #####');
    console.log('jsonString: ' + jsonString);
    const medicoData = JSON.parse(localStorage.getItem('medico'));
    let datosCombinados = null;

    if (medicoData) {
        datosCombinados = {
            /*paciente: { 
                ...datosFormulario,
            },*/
            paciente: {
                nombre: datosFormulario.nombre,
                edad: datosFormulario.edad,
                sexo: datosFormulario.sexo,
                historia_clinica: {
                    antecedentes_familiares: datosFormulario.antecedentes_familiares || '',
                    trastorno_autista: datosFormulario.trastorno_autista || '',
                    trastorno_comunicacion: datosFormulario.trastorno_Comunicacion || '',
                    trastorno_esquizoafectivo: datosFormulario.trastorno_esquizoafectivo || '',
                    trastorno_bipolar: datosFormulario.trastorno_bipolar || '',
                    trastorno_depresivo: datosFormulario.trastorno_depresivo || '',
                    sustancias: datosFormulario.sustancias || '',
                    estudio: {
                        estudio_causa_natural: datosFormulario.estudio_causa_natural || '',
                        estudio_comentario: datosFormulario.estudio_comentario || '',
                        imagen: datosFormulario.imagen || '',
                    },
                },
                sintomas_positivos: {
                    sintomas_positivos_duracion: datosFormulario.sintomas_positivos_duracion,
                    sintomas_positivos_alucinaciones: datosFormulario.sintomas_positivos_tipo_alucinaciones,
                    sintomas_positivos_tipo_lenguaje: datosFormulario.sintomas_positivos_tipo_lenguaje,
                    sintomas_positivos_tipo_pensamiento: datosFormulario.sintomas_positivos_tipo_pensamiento,
                    sintomas_positivos_tipo_ritmo_pensamiento: datosFormulario.sintomas_positivos_tipo_ritmo_pensamiento,
                    sintomas_positivos_tipo_contenido_pensamiento: datosFormulario.sintomas_positivos_tipo_contenido_pensamiento,
                },
                sintomas_negativos: {
                    sintomas_negativos_duracion: datosFormulario.sintomas_negativos_duracion,
                    sintomas_negativos_aspecto: datosFormulario.sintomas_negativos_aspecto,
                    sintomas_negativos_atencion: datosFormulario.sintomas_negativos_atencion,
                    sintomas_negativos_actividad: datosFormulario.sintomas_negativos_actividad,
                    sintomas_negativos_afectividad: datosFormulario.sintomas_negativos_afectividad,
                    sintomas_negativos_bajo_funcionamiento: datosFormulario.sintomas_negativos_bajo_funcionamiento,
                    sintomas_negativos_bajo_funcionamiento_comentario: datosFormulario.sintomas_negativos_bajo_funcionamiento_comentario,
                },
            },
            diagnostico: {
                diagnostico: datosFormulario.diagnostico,
                estado: datosFormulario.estado,
                comentario_medico: datosFormulario.comentario_medico,
                justificacion_rechazo: datosFormulario.justificacion_rechazo,
                justificacion: datosFormulario.justificacion,
                recomendacion: datosFormulario.recomendacion,
                reglas: datosFormulario.reglas,
                puntaje: datosFormulario.puntaje,
            },
            medico: {
                ...medicoData,
            }
        };
    }
    console.log(JSON.stringify(datosCombinados),);

    try {
        const response = await $.ajax({
            type: "POST",
            url: "/ingresarPaciente",
            contentType: "application/json",
            data: JSON.stringify(datosCombinados),
        });
        if (response.exito) {
            console.log('Paciente ingresado con éxito:', response);
            mostrarSnackbar(response.mensaje, true);
            return true;
        } else {
            console.error('Error al registrar al paciente:', response);
            mostrarSnackbar(response.mensaje, false);
            return false;
        }
    } catch (error) {
        console.error('Error al registrar al paciente:', response);
        mostrarSnackbar(response.mensaje, false);
        return false;
    }
}
// endregion guardarRegistro

// region checkBoxes
function unselect(valor, name) {
    var cboxes = document.getElementsByName(name);
    var len = cboxes.length;
    for (var i = 0; i < len; i++) {
        if (cboxes[i].value != valor) cboxes[i].checked = false;
    }
}

function unselectUnicos(valor, name) {
    var cboxes = document.getElementsByName(name);
    var len = cboxes.length;
    for (var i = 0; i < len; i++) {
        if (valor.includes(cboxes[i].value)) cboxes[i].checked = false;
    }
}
// endregion checkBoxes
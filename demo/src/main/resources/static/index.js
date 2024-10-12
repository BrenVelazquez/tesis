/*document.addEventListener('DOMContentLoaded', function() {
    const nombreMedico = localStorage.getItem("nombreMedico"); 
    console.log(nombreMedico); 
    if (nombreMedico) {
        document.getElementById('username').textContent = `Dr/Dra: ${nombreMedico}`;
    }
}, false);*/

let nombreMedico;

$(document).ready(function () {
    /*console.log("nombreMedico guardado:", localStorage.getItem('nombreMedico'));
    nombreMedico = localStorage.getItem("nombreMedico"); 
    
    if (nombreMedico) {
        document.getElementById('username').textContent = `Dr/Dra: ${nombreMedico}`;
    }*/
    const medicoData = JSON.parse(localStorage.getItem('medico')); // Recuperar y convertir de JSON
    if (medicoData) {
        document.getElementById('username').textContent = `Dr/Dra: ${medicoData.nombre+" "+medicoData.apellido}`;
    }
});

document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('btnCerrarSesion').addEventListener('click', function() {
        localStorage.removeItem('medico'); // Eliminar información del médico
        window.location.href = 'Login/login.html'; // Redirigir a la página de login
    });
});
/*
function cerrarSesion() {
    window.location.href = "Login/login.html";
}*/

/*
function sendNombre(){
   //localStorage.setItem("nombreMedico", nombreMedico); // Guarda el valor
   localStorage.setItem("nombreMedico",JSON.stringify(medicoData)); // Guarda el valor
}*/

/*function openConsultar(){
    let newWindow=window.open("/consultar-casos.html", "_self");
        newWindow.addEventListener('load', function(){
            newWindow.dniMedico = medico.dni;
        }, false);
}

function openExportar(){
    let newWindow=window.open("/exportar.html", "_self");
        newWindow.addEventListener('load', function(){
            newWindow.dniMedico = medico.dni;
        }, false);
}*/
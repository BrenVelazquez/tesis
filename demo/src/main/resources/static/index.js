/*document.addEventListener('DOMContentLoaded', function() {
    const nombreMedico = localStorage.getItem("nombreMedico"); 
    console.log(nombreMedico); 
    if (nombreMedico) {
        document.getElementById('username').textContent = `Dr/Dra: ${nombreMedico}`;
    }
}, false);*/

let nombreMedico;

$(document).ready(function () {
    console.log("nombreMedico guardado:", localStorage.getItem('nombreMedico'));
    nombreMedico = localStorage.getItem("nombreMedico"); 
    
    if (nombreMedico) {
        document.getElementById('username').textContent = `Dr/Dra: ${nombreMedico}`;
    }
});

function sendNombre(){
    localStorage.setItem("nombreMedico", nombreMedico); // Guarda el valor
}

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
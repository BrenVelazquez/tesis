let nombreMedico;

$(document).ready(function () {
    const medicoData = JSON.parse(localStorage.getItem('medico')); // Recuperar y convertir de JSON
    if (medicoData) {
        document.getElementById('username').textContent = `Dr/Dra: ${medicoData.nombre_medico + " " + medicoData.apellido_medico}`;
    }else{
        window.location.href = 'Login/login.html'; // Redirigir a la página de login
    }
});

document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('btnCerrarSesion').addEventListener('click', function () {
        localStorage.removeItem('medico'); // Eliminar información del médico
        window.location.href = '/Login/login.html'; // Redirigir a la página de login
    });
});
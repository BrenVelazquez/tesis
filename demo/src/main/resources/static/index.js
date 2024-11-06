$(document).ready(function () {
    const medicoData = JSON.parse(localStorage.getItem('medico'));
    if (medicoData) {
        document.getElementById('username').textContent = `Dr/Dra: ${medicoData.nombre_medico + " " + medicoData.apellido_medico}`;
    } else {
        cerrarSesion();
    }
});

document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('btnCerrarSesion').addEventListener('click', function () {
        cerrarSesion();
    });
});

function cerrarSesion() {
    localStorage.removeItem('medico');
    window.location.href = '/Login/login.html';
}
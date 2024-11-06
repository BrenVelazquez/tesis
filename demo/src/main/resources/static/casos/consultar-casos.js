let pacientes = [];

document.addEventListener('DOMContentLoaded', function () {
    if (validarMedico()) {
        obtenerPacientes();
    }
});

$(document).ready(function () {
    validarMedico();
});

function validarMedico() {
    const medicoData = JSON.parse(localStorage.getItem('medico'));
    if (medicoData) {
        document.getElementById('username').textContent = `Dr/Dra: ${medicoData.nombre_medico + " " + medicoData.apellido_medico}`;
        return true;
    } else {
        window.location.href = '/Login/login.html';
    }
}

async function obtenerPacientes() {
    const tabla = document.getElementById('tabla-pacientes');
    const noPacientesMsg = document.getElementById('no-pacientes-msg');
    const thead = tabla.getElementsByTagName('thead')[0];
    try {
        const response = await $.ajax({
            type: "GET",
            url: "/obtenerPacientes",
            contentType: "application/json"
        });

        if (response && response.length > 0) {
            console.log('Pacientes obtenidos con éxito:', response);
            pacientes = response;
            mostrarPacientesEnTabla(response);
            noPacientesMsg.style.display = 'none';
            thead.style.display = 'table-header-group';
        } else {
            noPacientesMsg.style.display = 'block';
            thead.style.display = 'none';
        }
    } catch (error) {
        console.error('Error al obtener los pacientes:', error);
    }
}

function mostrarPacientesEnTabla(pacientes) {
    limpiarTabla();
    const tabla = document.getElementById('tabla-pacientes');
    const tbody = tabla.getElementsByTagName('tbody')[0];

    pacientes.forEach(paciente => {
        const fila = tbody.insertRow();
        fila.insertCell(0).innerText = paciente.nombre;
        fila.insertCell(1).innerText = paciente.diagnostico;
        fila.insertCell(2).innerText = paciente.estado == 1 ? "Confirmado" : "Rechazado";
        fila.insertCell(3).innerText = paciente.fecha;
        fila.insertCell(4).innerText = paciente.nombreMedico;

        const cellAcciones = fila.insertCell(5);
        cellAcciones.innerHTML = `<button class="btn-detalle" onclick="verDetalle(${paciente.idPaciente})">Ver detalles</button>`;
    });
}

function limpiarTabla() {
    const tabla = document.getElementById('tabla-pacientes');
    const filas = tabla.getElementsByTagName('tbody')[0];
    if (filas) {
        while (filas.firstChild) {
            filas.removeChild(filas.firstChild);
        }
    }
}

function verDetalle(id) {
    if (validarMedico()) {
        window.location.href = `/detalle/detalle.html?idPaciente=${id}`;
    }
}

function buscarPacientes() {
    const searchTerm = document.getElementById('search-input').value.toLowerCase();

    const pacientesFiltrados = pacientes.filter(paciente =>
        paciente.nombre.toLowerCase().includes(searchTerm) ||
        paciente.diagnostico.toLowerCase().includes(searchTerm) ||
        paciente.estado.toLowerCase().includes(searchTerm) ||
        paciente.fecha.toLowerCase().includes(searchTerm)
    );
    mostrarPacientesEnTabla(pacientesFiltrados);
}

function refrescarTabla() {
    document.getElementById('search-input').value = '';
    mostrarPacientesEnTabla(pacientes);
}

function volver() {
    if (validarMedico()) {
        window.location.href = "/home";
    }
}

document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('btnCerrarSesion').addEventListener('click', function () {
        localStorage.removeItem('medico');
        window.location.href = '/';
    });
});
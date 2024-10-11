<<<<<<< HEAD
let nombreMedico;


$(document).ready(function () {
    console.log("nombreMedico guardado3:", localStorage.getItem('nombreMedico'));
    nombreMedico = localStorage.getItem("nombreMedico"); 
    
    if (nombreMedico) {
        document.getElementById('username').textContent = `Dr/Dra: ${nombreMedico}`;
    }
});

const pacientes = [
    { nombre: "Armando Quilo", diagnostico: "Esquizofrenia", estado: "Aprobado", fecha: "02/09/24" },
    { nombre: "Norma Caries", diagnostico: "No Posible Esquizofrenia", estado: "Aprobado", fecha: "02/09/24" },
    { nombre: "María Ana Martínez", diagnostico: "Evaluar Esquizofrenia Temporal", estado: "Aprobado", fecha: "03/09/24" },
    { nombre: "Victor Gonzalez", diagnostico: "Esquizofrenia", estado: "Aprobado", fecha: "11/09/24" },
    { nombre: "Fabricio Sanchez", diagnostico: "Esquizofrenia", estado: "Aprobado", fecha: "11/09/24" },
    { nombre: "Analía Chavez", diagnostico: "Esquizofrenia", estado: "Aprobado", fecha: "11/09/24" },
    { nombre: "Romina Cordón", diagnostico: "Esquizofrenia", estado: "Aprobado", fecha: "12/09/24" }
];
=======
document.addEventListener('DOMContentLoaded', function () {
    obtenerPacientes();
});
let pacientes = [];
>>>>>>> 1168b328f742551a8e2ce14a3fe8614c860716d3

async function obtenerPacientes() {
    try {
        const response = await $.ajax({
            type: "GET",
            url: "/obtenerPacientes",
            contentType: "application/json"
        });

        if (response && response.length > 0) {
            console.log('Pacientes obtenidos con éxito:', response);
            console.log("response: " + JSON.stringify(response));
            pacientes = response;
            mostrarPacientesEnTabla(response);
        } else {
            console.error('No se encontraron pacientes:', response);
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

        const cellAcciones = fila.insertCell(4);
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
    window.location.href = `/detalle/detalle.html?idPaciente=${id}`;
}

function buscarPacientes() {
    const searchTerm = document.getElementById('search-input').value.toLowerCase();
    console.log('searchTerm: ' + searchTerm);

    const pacientesFiltrados = pacientes.filter(paciente =>
        paciente.nombre.toLowerCase().includes(searchTerm) ||
        paciente.diagnostico.toLowerCase().includes(searchTerm) ||
        paciente.estado.toLowerCase().includes(searchTerm) ||
        paciente.fecha.toLowerCase().includes(searchTerm)
    );
    console.log('pacientesFiltrados: ' + JSON.stringify(pacientesFiltrados));
    mostrarPacientesEnTabla(pacientesFiltrados);
}

function refrescarTabla() {
    document.getElementById('search-input').value = '';
    mostrarPacientesEnTabla(pacientes);
}

function volver() {
    window.location.href = "/";
}

// window.onload = obtenerPacientes;

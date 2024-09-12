// Datos de ejemplo de pacientes
const pacientes = [
    { id: '885e969d', nombre: "Armando Quilo", diagnostico: "Posible Esquizofrenia", estado: "Aprobado" },
    { id: '5f2633bc', nombre: "Norma Caries", diagnostico: "No Posible Esquizofrenia", estado: "Aprobado" },
    { id: '018c54d1', nombre: "María Ana Martínez", diagnostico: "Evaluar Esquizofrenia Temporal", estado: "Aprobado" },
    { id: '13e6ceea', nombre: "Victor Gonzalez", diagnostico: "Posible Esquizofrenia", estado: "Aprobado" },
    { id: 'f6d88814', nombre: "Fabricio Sanchez", diagnostico: "Posible Esquizofrenia", estado: "Aprobado" },
    { id: '0ec05a60', nombre: "Analía Chavez", diagnostico: "Posible Esquizofrenia", estado: "Aprobado" },
    { id: '6edb40ae', nombre: "Romina Cordón", diagnostico: "Posible Esquizofrenia", estado: "Aprobado" }
];

// Función para cargar los datos de los pacientes en la tabla
function cargarPacientes(pacientesFiltrados = pacientes, filtrado = false) {
    const tbody = document.querySelector("#tabla-pacientes tbody");
    tbody.innerHTML = "";
    const list = filtrado == true ? pacientesFiltrados : pacientes;
    list.forEach(paciente => {
        const row = tbody.insertRow();
        row.innerHTML = `
            <td>${paciente.id}</td>
            <td>${paciente.nombre}</td>
            <td>${paciente.diagnostico}</td>
            <td>${paciente.estado}</td>
            <td><button class="btn-detalle" onclick="verDetalle(${paciente.id})">Ver detalles</button></td>
        `;
    });
}

// Función para ver los detalles de un paciente
function verDetalle(id) {
    const paciente = pacientes.find(p => p.id === id);
    if (paciente) {
        alert(`Detalles del paciente:
        ID: ${paciente.id}
        Nombre: ${paciente.nombre}
        Diagnóstico: ${paciente.diagnostico}
        Estado: ${paciente.estado}`);
    }
}

// Función para buscar pacientes
function buscarPacientes() {
    const searchTerm = document.getElementById('search-input').value.toLowerCase();
    console.log('searchTerm: ' + searchTerm);

    const pacientesFiltrados = pacientes.filter(paciente =>
        paciente.nombre.toLowerCase().includes(searchTerm) ||
        paciente.diagnostico.toLowerCase().includes(searchTerm) ||
        paciente.estado.toLowerCase().includes(searchTerm)
    );
    console.log('pacientesFiltrados: ' + JSON.stringify(pacientesFiltrados));
    cargarPacientes(pacientesFiltrados, true);
}

// Cargar los pacientes cuando la página se cargue
window.onload = cargarPacientes;
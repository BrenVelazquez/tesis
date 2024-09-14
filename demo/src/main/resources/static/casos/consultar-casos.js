const pacientes = [
    { id: '885e969d', nombre: "Armando Quilo", diagnostico: "Posible Esquizofrenia", estado: "Aprobado", fecha: "02/09/24" },
    { id: '5f2633bc', nombre: "Norma Caries", diagnostico: "No Posible Esquizofrenia", estado: "Aprobado", fecha: "02/09/24" },
    { id: '018c54d1', nombre: "María Ana Martínez", diagnostico: "Evaluar Esquizofrenia Temporal", estado: "Aprobado", fecha: "03/09/24" },
    { id: '13e6ceea', nombre: "Victor Gonzalez", diagnostico: "Posible Esquizofrenia", estado: "Aprobado", fecha: "11/09/24" },
    { id: 'f6d88814', nombre: "Fabricio Sanchez", diagnostico: "Posible Esquizofrenia", estado: "Aprobado", fecha: "11/09/24" },
    { id: '0ec05a60', nombre: "Analía Chavez", diagnostico: "Posible Esquizofrenia", estado: "Aprobado", fecha: "11/09/24" },
    { id: '6edb40ae', nombre: "Romina Cordón", diagnostico: "Posible Esquizofrenia", estado: "Aprobado", fecha: "12/09/24" }
];

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
            <td>${paciente.fecha}</td>
            <td><button class="btn-detalle" onclick="verDetalle(${paciente.id})">Ver detalles</button></td>
        `;
    });
}

function verDetalle(id) {
    const paciente = pacientes.find(p => p.id === id);
    if (paciente) {
        alert(`Detalles del paciente:
        ID: ${paciente.id}
        Nombre: ${paciente.nombre}
        Diagnóstico: ${paciente.diagnostico}
        Estado: ${paciente.estado}
        Fecha de Ingreso: ${paciente.fecha}`);
    }
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
    cargarPacientes(pacientesFiltrados, true);
}

function refrescarTabla() {
    document.getElementById('search-input').value = '';
    cargarPacientes();
}

function volver() {
    window.location.href = "/";
}

window.onload = cargarPacientes;
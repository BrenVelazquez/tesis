const modal = document.getElementById('modal');
const modalMessage = document.getElementById('modalMessage');
const btnClose = document.getElementById('btnClose');
const btnDownload = document.getElementById('btnDownload');
const loader = document.querySelector('.loader');
const modalIcon = document.querySelector('.modal-icon');

let nombreMedico;
$(document).ready(function () {
    const medicoData = JSON.parse(localStorage.getItem('medico')); // Recuperar y convertir de JSON
    if (medicoData) {
        document.getElementById('username').textContent = `Dr/Dra: ${medicoData.nombre+" "+medicoData.apellido}`;
    }
});

function showModal(message) {
    modalMessage.textContent = message;
    modal.style.display = 'flex';
    btnClose.disabled = true;
    loader.style.display = 'block';
    modalIcon.style.display = 'none';
}
function closeModal() {
    modal.style.display = 'none';
}

btnDownload.addEventListener('click', function () {
    showModal('Iniciando la descarga del archivo Excel con todos los casos...');
    fetch("/descargarExcel")
        .then(response => {
            if (!response.ok) {
                throw new Error("Error al descargar el archivo");
            }
            return response.blob();
        })
        .then(blob => {
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement("a");
            a.href = url;
            a.download = "dataset.xlsx";
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
            setTimeout(function () {
                modalMessage.textContent = '¡Descarga completada! El archivo Excel se ha guardado en su carpeta de Descargas.';
                btnClose.disabled = false;
                loader.style.display = 'none';
                modalIcon.style.display = 'block';
            }, 1000);
        })
        .catch(error => {
            console.error(error);
        });
});

window.addEventListener('click', function (event) {
    if (event.target === modal) {
        closeModal();
    }
});

function volver() {
    window.location.href = "../index.html";
}

document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('btnCerrarSesion').addEventListener('click', function() {
        localStorage.removeItem('medico'); // Eliminar información del médico
        window.location.href = '../Login/login.html'; // Redirigir a la página de login
    });
});

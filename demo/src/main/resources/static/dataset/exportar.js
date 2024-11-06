const modal = document.getElementById('modal');
const modalMessage = document.getElementById('modalMessage');
const btnClose = document.getElementById('btnClose');
const btnDownload = document.getElementById('btnDownload');
const loader = document.querySelector('.loader');
const modalIcon = document.querySelector('.modal-icon');
const errorIcon = document.querySelector('.error-icon');

$(document).ready(function () {
    validarMedico()
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

function showModal(message) {
    modalMessage.textContent = message;
    modal.style.display = 'flex';
    btnClose.disabled = true;
    loader.style.display = 'block';
    modalIcon.style.display = 'none';
    errorIcon.style.display = 'none';
}
function closeModal() {
    modal.style.display = 'none';
}

btnDownload.addEventListener('click', function () {
    if (validarMedico()) {
        showModal('Iniciando la descarga del archivo Excel con todos los casos...');
        fetch("/descargarExcel")
            .then(response => {
                if (response.status === 404) {
                    return response.json().then(data => {
                        throw new Error(data.message || "Error al descargar el archivo.");
                    });
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
                console.error("error: ", error);
                modalMessage.textContent = error.message || 'Ocurrió un error al descargar el excel, por favor intente nuevamente más tarde.';
                btnClose.disabled = false;
                loader.style.display = 'none';
                errorIcon.style.display = 'block';
            });
    }
});

window.addEventListener('click', function (event) {
    if (event.target === modal) {
        closeModal();
    }
});

function volver() {
    window.location.href = "/home";
}

document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('btnCerrarSesion').addEventListener('click', function () {
        localStorage.removeItem('medico');
        window.location.href = '/';
    });
});

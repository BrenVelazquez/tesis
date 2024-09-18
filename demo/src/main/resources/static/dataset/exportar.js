const modal = document.getElementById('modal');
const modalMessage = document.getElementById('modalMessage');
const btnClose = document.getElementById('btnClose'); 
const btnDownload = document.getElementById('btnDownload');

function showModal(message) {
    modalMessage.textContent = message;
    modal.style.display = 'flex';
    btnClose.disabled = true;
}

function closeModal() {
    modal.style.display = 'none';
}

btnDownload.addEventListener('click', function () {
    showModal('Iniciando la descarga del archivo Excel con todos los casos...');
    setTimeout(function () {
        showModal('¡Descarga completada! El archivo Excel se ha guardado en su carpeta de Descargas.');
        btnClose.disabled = false; 
    }, 2000);
});

window.addEventListener('click', function (event) {
    if (event.target === modal) {
        closeModal();
    }
});
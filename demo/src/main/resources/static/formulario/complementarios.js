
document.addEventListener('DOMContentLoaded', function () {
    // Configuración de la previsualización de la imagen
    const imagenInput = document.getElementById('imagen');
    const imagenPreview = document.getElementById('imagen-preview');
    const insertPhotoMessage = document.querySelector('.insert-photo-message');

    console.log('imagenInput: ' + imagenInput);
    console.log('imagenPreview: ' + imagenPreview);
    console.log('insertPhotoMessage: ' + insertPhotoMessage);

    if (imagenInput && imagenPreview) {
        imagenInput.addEventListener('change', function () {
            const file = this.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    imagenPreview.src = e.target.result;
                    imagenPreview.style.display = 'block';
                    if (insertPhotoMessage) {
                        insertPhotoMessage.style.display = 'none';
                    }
                };
                reader.readAsDataURL(file);
            } else {
                imagenPreview.style.display = 'none';
                if (insertPhotoMessage) {
                    insertPhotoMessage.style.display = 'block';
                }
            }
        });
    }

    if (insertPhotoMessage && imagenInput) {
        insertPhotoMessage.addEventListener('click', function () {
            imagenInput.click();
        });
    } else {
        console.error('Elementos de imagen no encontrados.');
    }
});
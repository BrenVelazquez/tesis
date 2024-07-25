document.addEventListener('DOMContentLoaded', function () {
    console.log("NO ESTA ENTRANDO ACA");
    // Manejo del multiselect tipo-alucinaciones
    const tipoAlucinacionesSelect = document.getElementById('alucinaciones');
    if (tipoAlucinacionesSelect) {
        tipoAlucinacionesSelect.addEventListener('change', function () {
            const selectedOptions = Array.from(this.selectedOptions).map(option => option.value);
            console.log('Tipos de alucinaciones seleccionadas: ', selectedOptions);
            // TODO: REVISAR
            // const selectedOptionsString = selectedOptions.join(', ') || 'Ninguna seleccionada';
            // selectedAlucinationsLabel.textContent = `Seleccionadas: ${selectedOptionsString}`;
            selectedOptions.forEach(option => {
                const optionDiv = document.createElement('div');
                optionDiv.className = 'selected-option';
                optionDiv.innerHTML = `
                    ${option.textContent}
                    <button data-value="${option.value}">&times;</button>
                `;
                selectedOptionsContainer.appendChild(optionDiv);
            });
        
        });
    } else {
        console.error('Elemento select no encontrado.');
    }
    const selectedOptionsContainer = document.getElementById('selected-options');


});

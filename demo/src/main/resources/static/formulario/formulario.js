let datosFormulario = {};
       
        function mostrarPopup() {
            $("#popup").fadeIn(1000);
        }

        function mostrarJustificacion()
        {
            $(".justificacion").fadeIn(1000);
        }

        function cerrarPopup() {
            $("#message-container").fadeIn();
            setTimeout(function () {
                $("#message-container").fadeOut();
                $("#popup").fadeOut();
                // Redirige al inicio (index.html) en localhost:8080
                window.location.href = "http://192.168.0.13:8080/";
            }, 3000); // Muestra el mensaje durante 3 segundos
        }

        function initializeButtons() {
            const buttonPairs = [
                { yes: 'transtorno-autista-si', no: 'transtorno-autista-no' },
                { yes: 'transtorno-comunicacion-si', no: 'transtorno-comunicacion-no' },
                { yes: 'transtorno-esquizoafectivo-si', no: 'transtorno-esquizoafectivo-no' },
                { yes: 'transtorno-depresivo-si', no: 'transtorno-depresivo-no' },
                { yes: 'transtorno-bipolar-si', no: 'transtorno-bipolar-no' },
                { yes: 'estudios-si', no: 'estudios-no' },
                { yes: 'sustancias-si', no: 'sustancias-no' },
                { yes: 'antecedentes-familiares-si', no: 'antecedentes-familiares-no' }
            ];
        
            buttonPairs.forEach(pair => {
                const btnYes = document.getElementById(pair.yes);
                const btnNo = document.getElementById(pair.no);
        
                if (btnYes && btnNo) {
                    btnYes.addEventListener('click', function () {
                        btnYes.classList.remove('selected');
                        btnNo.classList.remove('selected');
                        this.classList.add('selected');
                    });
        
                    btnNo.addEventListener('click', function () {
                        btnYes.classList.remove('selected');
                        btnNo.classList.remove('selected');
                        this.classList.add('selected');
                    });
                } else {
                    console.error(`Botones no encontrados: ${pair.yes} o ${pair.no}`);
                }
            });
        }
        
        /*Antecedente Autista*/
        // const btnAutistaYes = document.getElementById('transtorno-autista-si');
        // const btnAutistaNo = document.getElementById('transtorno-autista-no');
        // btnAutistaYes.addEventListener('click', function () {
        //     btnAutistaYes.classList.remove('selected');
        //     btnAutistaNo.classList.remove('selected');
        //     this.classList.add('selected');
        // });
        // btnAutistaNo.addEventListener('click', function () {
        //     btnAutistaYes.classList.remove('selected');
        //     btnAutistaNo.classList.remove('selected');
        //     this.classList.add('selected');
        // });
        // /*Fin Antecedente Autista*/

        // /*Antecedente Comunicacion*/
        // const btnComunicacionYes = document.getElementById('transtorno-comunicacion-si');
        // const btnComunicacionNo = document.getElementById('transtorno-comunicacion-no');
        // btnComunicacionYes.addEventListener('click', function () {
        //     btnComunicacionYes.classList.remove('selected');
        //     btnComunicacionNo.classList.remove('selected');
        //     this.classList.add('selected');
        // });
        // btnComunicacionNo.addEventListener('click', function () {
        //     btnComunicacionYes.classList.remove('selected');
        //     btnComunicacionNo.classList.remove('selected');
        //     this.classList.add('selected');
        // });
        // /*Fin Antecedente Comunicacion*/

        // /*Antecedente Esquizoafectivo*/
        // const btnEsquizoafectivoYes = document.getElementById('transtorno-esquizoafectivo-si');
        // const btnEsquizoafectivoNo = document.getElementById('transtorno-esquizoafectivo-no');
        // btnEsquizoafectivoYes.addEventListener('click', function () {
        //     btnEsquizoafectivoYes.classList.remove('selected');
        //     btnEsquizoafectivoNo.classList.remove('selected');
        //     this.classList.add('selected');
        // });
        // btnEsquizoafectivoNo.addEventListener('click', function () {
        //     btnEsquizoafectivoYes.classList.remove('selected');
        //     btnEsquizoafectivoNo.classList.remove('selected');
        //     this.classList.add('selected');
        // });
        // /*Fin Antecedente Esquizoafectivo*/

        // /*Antecedente Depresivo*/
        // const btnDepresivoYes = document.getElementById('transtorno-depresivo-si');
        // const btnDepresivoNo = document.getElementById('transtorno-depresivo-no');
        // btnDepresivoYes.addEventListener('click', function () {
        //     btnDepresivoYes.classList.remove('selected');
        //     btnDepresivoNo.classList.remove('selected');
        //     this.classList.add('selected');
        // });
        // btnDepresivoNo.addEventListener('click', function () {
        //     btnDepresivoYes.classList.remove('selected');
        //     btnDepresivoNo.classList.remove('selected');
        //     this.classList.add('selected');
        // });
        // /*Fin Antecedente Depresivo*/

        //  /*Antecedente Bipolar*/
        //  const btnBipolarYes = document.getElementById('transtorno-bipolar-si');
        // const btnBipolarNo = document.getElementById('transtorno-bipolar-no');
        // btnBipolarYes.addEventListener('click', function () {
        //     btnBipolarYes.classList.remove('selected');
        //     btnBipolarNo.classList.remove('selected');
        //     this.classList.add('selected');
        // });
        // btnBipolarNo.addEventListener('click', function () {
        //     btnBipolarYes.classList.remove('selected');
        //     btnBipolarNo.classList.remove('selected');
        //     this.classList.add('selected');
        // });
        // /*Fin Antecedente Bipolar*/

        // /*Estudios*/
        // const btnEstudiosYes = document.getElementById('estudios-si');
        // const btnEstudiossNo = document.getElementById('estudios-no');
        // btnEstudiosYes.addEventListener('click', function () {
        //     btnEstudiosYes.classList.remove('selected');
        //     btnEstudiossNo.classList.remove('selected');
        //     this.classList.add('selected');
        // });
        // btnEstudiossNo.addEventListener('click', function () {
        //     btnEstudiosYes.classList.remove('selected');
        //     btnEstudiossNo.classList.remove('selected');
        //     this.classList.add('selected');
        // });
        // /*Fin Estudios*/

        // /*Sustancias*/
        // const btnSustanciasYes = document.getElementById('sustancias-si');
        // const btnSustanciasNo = document.getElementById('sustancias-no');
        // btnSustanciasYes.addEventListener('click', function () {
        //     btnSustanciasYes.classList.remove('selected');
        //     btnSustanciasNo.classList.remove('selected');
        //     this.classList.add('selected');
        // });
        // btnSustanciasNo.addEventListener('click', function () {
        //     btnSustanciasYes.classList.remove('selected');
        //     btnSustanciasNo.classList.remove('selected');
        //     this.classList.add('selected');
        // });
        // /*Fin Sustancias*/

        // /*Antecedentes Familiares*/
        // const btnAntecedentesFamiliaresYes = document.getElementById('antecedentes-familiares-si');
        // const btnAntecedentesFamiliaresNo = document.getElementById('antecedentes-familiares-no');
        // btnAntecedentesFamiliaresYes.addEventListener('click', function () {
        //     btnAntecedentesFamiliaresYes.classList.remove('selected');
        //     btnAntecedentesFamiliaresNo.classList.remove('selected');
        //     this.classList.add('selected');
        // });
        // btnAntecedentesFamiliaresNo.addEventListener('click', function () {
        //     btnAntecedentesFamiliaresYes.classList.remove('selected');
        //     btnAntecedentesFamiliaresNo.classList.remove('selected');
        //     this.classList.add('selected');
        // });
        /*Fin Antecedentes Familiares*/

     

        function determinar_diagnostico() {

            console.log("INICIO FUNCION DIAGNOSTICO()");

            // Obtener los valores de los campos del formulario
            const edad = document.getElementById("edad").value;
            const sexo = document.getElementById("sexo").value;
            const nombre = document.getElementById("nombre").value;

            const transtornoAutista = document.querySelector("#transtorno-autista-si.selected") ? "Si" : document.querySelector("#transtorno-autista-no.selected") ? "No" : "";
            const transtornoComunicacion = document.querySelector("#transtorno-comunicacion-si.selected") ? "Si" : document.querySelector("#transtorno-comunicacion-no.selected") ? "No" : "";
            const transtornoEsquizoafectivo = document.querySelector("#transtorno-esquizoafectivo-si.selected") ? "Si" : document.querySelector("#transtorno-esquizoafectivo-no.selected") ? "No" : "";
            const transtornoDepresivo = document.querySelector("#transtorno-depresivo-si.selected") ? "Si" : document.querySelector("#transtorno-depresivo-no.selected") ? "No" : "";
            const transtornoBipolar = document.querySelector("#transtorno-bipolar-si.selected") ? "Si" : document.querySelector("#transtorno-bipolar-no.selected") ? "No" : "";
            const antecedentesFamiliares = document.querySelector("#antecedentes-familiares-si.selected") ? "Si" : document.querySelector("#antecedentes-familiares-no.selected") ? "No" : "";
            const estudios = document.querySelector("#estudios-si.selected") ? "Si" : document.querySelector("#estudios-no.selected") ? "No" : "";
            const sustancias = document.querySelector("#sustancias-si.selected") ? "Si" : document.querySelector("#sustancias-no.selected") ? "No" : "";
            // Obtener el valor de inmunodepresión
            // const transtornoAutista = document.querySelector("#transtorno-autista-si.selected")
            //     ? "Si"
            //     : document.querySelector("#transtorno-autista-no.selected")
            //         ? "No"
            //         : "";

            // // Obtener el valor de transtorno de comunicacion
            // const transtornoComunicacion = document.querySelector("#transtorno-comunicacion-si.selected")
            //     ? "Si"
            //     : document.querySelector("#transtorno-comunicacion-no.selected")
            //         ? "No"
            //         : "";

            // // Obtener el valor de transtorno esquizoafectivo
            // const transtornoEsquizoafectivo = document.querySelector("#transtorno-esquizoafectivo-si.selected")
            //     ? "Si"
            //     : document.querySelector("#transtorno-esquizoafectivo-no.selected")
            //         ? "No"
            //         : "";

            // // Obtener el valor de transtorno depresivo
            // const transtornoDepresivo = document.querySelector("#transtorno-depresivo-si.selected")
            //     ? "Si"
            //     : document.querySelector("#transtorno-depresivo-no.selected")
            //         ? "No"
            //         : "";

            // // Obtener el valor de transtorno bipolar
            // const transtornoBipolar = document.querySelector("#transtorno-bipolar-si.selected")
            //     ? "Si"
            //     : document.querySelector("#transtorno-bipolar-no.selected")
            //         ? "No"
            //         : "";
            // // Obtener el valor de antecedentes familiares
            // const antecedentesFamiliares = document.querySelector("#antecedentes-familiares-si.selected")
            //     ? "Si"
            //     : document.querySelector("#antecedentes-familiares-no.selected")
            //         ? "No"
            //         : "";
            
            // // Obtener el valor de estudios
            // const estudios = document.querySelector("#estudios-si.selected")
            //     ? "Si"
            //     : document.querySelector("#estudios-no.selected")
            //         ? "No"
            //         : "";

            // // Obtener el valor de sustancias
            // const sustancias = document.querySelector("#sustancias-si.selected")
            //     ? "Si"
            //     : document.querySelector("#sustancias-no.selected")
            //         ? "No"
            //         : "";



            // Crear un objeto con los valores recolectados
            datosFormulario = {
                edad: edad,
                sexo: sexo,
                nombre: nombre,
                transtorno_autista: transtornoAutista,
                transtorno_comunicacion: transtornoComunicacion,
                transtorno_esquizoafectivo: transtornoEsquizoafectivo,
                transtorno_depresivo: transtornoDepresivo,
                transtorno_bipolar: transtornoBipolar,
                estudios: estudios,
                sustancias: sustancias,
                antecedentes_familiares: antecedentesFamiliares,
            };

            const jsonString = JSON.stringify(datosFormulario);
            console.log("Datos del formulario en formato JSON: ", jsonString);

        }
let datosLogin = {};

function validar_campos(datosLogin) {
    resetearErrores();
    document.getElementById("error-login").style.display='none';
    let esValido = true;


    if (datosLogin.mail == "") {
        mostrarError(document.getElementById("mail"), "Por favor, ingrese su email.");
        esValido = false;
    }
    if (datosLogin.contraseña == "") {
        mostrarError(document.getElementById("contraseña"), "Por favor, ingrese su contraseña.");
        esValido = false;
    }
    return esValido;
}

function mostrarError(campo, mensaje) {
    campo.style.borderColor = "red";
    const errorDiv = document.createElement("div");
    errorDiv.className = "error-message";
    errorDiv.style.color = "red";
    errorDiv.textContent = mensaje;
    campo.parentNode.appendChild(errorDiv);
}

function resetearErrores() {
    const errores = document.querySelectorAll(".error-message");
    errores.forEach(error => {
        const campo = error.parentNode.querySelector("input, select");
        campo.style.borderColor = "";
        error.parentNode.removeChild(error);
    });
}

function obtenerDatosLogin() {
    const mail = document.getElementById("mail").value;
    const contraseña = document.getElementById("contraseña").value;
    datosLogin = {
        mail: mail,
        contraseña: contraseña,
    };
    return datosLogin
}




async function iniciar_sesion() {
    const datosLogin = obtenerDatosLogin();
    if (validar_campos(datosLogin)){
        const jsonString = JSON.stringify(datosLogin);
        console.log("Datos del formulario en formato JSON: ", jsonString);
    
        console.log('jsonString: ' + jsonString);
        try {
            const response = await $.ajax({
                type: "POST",
                url: "/login",
                contentType: "application/json",
                data: jsonString,
            });
            if (response.exito) {
                window.open("index.html", "_self");
                return true;
            } else {
                document.getElementById("error-login").style.display='block';
    
                return false;
            }
        } catch (error) {
            return false;
        }
    } 
    
}
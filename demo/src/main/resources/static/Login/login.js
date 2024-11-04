let datosLogin = {};

function togglePasswordVisibility() {
    const passwordInput = document.getElementById("contraseña");
    const eyeIcon = document.getElementById("eyeIcon");

    if (passwordInput.type === "password") {
        passwordInput.type = "text";
        eyeIcon.classList.remove("fa-eye");
        eyeIcon.classList.add("fa-eye-slash");
    } else {
        passwordInput.type = "password";
        eyeIcon.classList.remove("fa-eye-slash");
        eyeIcon.classList.add("fa-eye");
    }
}


function validar_campos(datosLogin) {
    resetearErrores();
    document.getElementById("error-login").style.display = 'none';
    let esValido = true;
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (datosLogin.mail == "") {
        mostrarError(document.getElementById("mail"), "Por favor, ingrese su email.");
        esValido = false;
    } else if (!emailPattern.test(datosLogin.mail)) {
        mostrarError(document.getElementById("mail"), "Ingrese un email válido.");
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
    errorDiv.style.marginBotton = "5px";
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
    if (validar_campos(datosLogin)) {
        const jsonString = JSON.stringify(datosLogin);
        try {
            const response = await $.ajax({
                type: "POST",
                url: "/login",
                contentType: "application/json",
                data: jsonString,
            });
            if (response.mensaje === "Usuario y contraseña correcto.") {
                const medico = response.medico;

                console.log(response);
                localStorage.setItem("medico", JSON.stringify(medico));
                window.location.href = "/home";
                return true;
            } else {
                document.getElementById("error-login").style.display = 'block';
                return false;
            }
        } catch (error) {
            return false;
        }
    }
}
let datosFormulario = {};


function iniciar_sesion() {
    const usuario = document.getElementById("usuario").value;
    const contraseña = document.getElementById("contraseña").value;
    if(usuario!=user1 && contraseña!=123456)
        document.getElementById("error-login").textContent = "Usuario o contraseña incorrectos";
    else
        window.location = 'index.html';

}
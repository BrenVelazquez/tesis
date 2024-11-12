package com.sistexperto.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.sistexperto.model.Medico;
import com.sistexperto.service.LoginService;

@Controller
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/")
    public String login() {
        return "Login/login.html";
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Map<String, String> loginRequest) {
        String mail = loginRequest.get("mail");
        String contraseña = loginRequest.get("contraseña");
        Medico medico = loginService.login(mail, contraseña);
        Map<String, Object> respuesta = new HashMap<>();

        if (medico != null) {
            respuesta.put("medico", medico);
            respuesta.put("mensaje", "Usuario y contraseña correcto.");
        } else {
            respuesta.put("mensaje", "Médico no encontrado.");
        }
        return ResponseEntity.ok(respuesta);
    }
}

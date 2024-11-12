package com.sistexperto.service;

import com.sistexperto.model.Medico;
import org.springframework.stereotype.Service;

import com.sistexperto.database.database;

@Service
public class LoginService {

    public LoginService() {
    }

    public Medico login(String mail, String password) {
        Medico medico = database.login(mail, password);
        return medico;
    }
}

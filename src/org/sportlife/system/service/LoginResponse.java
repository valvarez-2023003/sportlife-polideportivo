package org.sportlife.system.service;

import org.sportlife.system.model.User;

/**
 * Respuesta del proceso de autenticación.
 * Encapsula el estado del login y, si fue exitoso, el usuario autenticado.
 *
 * @author Cristofer Ramos
 */
public class LoginResponse {

    private final LoginStatus status;
    private final User user;

    public LoginResponse(LoginStatus status, User user) {
        this.status = status;
        this.user = user;
    }

    public LoginStatus getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }
}
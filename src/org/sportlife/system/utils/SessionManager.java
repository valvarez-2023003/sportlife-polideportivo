package org.sportlife.system.utils;

import org.sportlife.system.model.User;

/**
 * Gestor de sesión del usuario autenticado.
 * Guarda el usuario logueado para consultarlo desde cualquier controlador.
 *
 * @author Cristofer Ramos
 */
public class SessionManager {

    private static SessionManager instancia;
    private User usuarioActual;

    private SessionManager() {
    }

    public static SessionManager getInstancia() {
        if (instancia == null) {
            instancia = new SessionManager();
        }
        return instancia;
    }

    public void setUsuarioActual(User usuario) {
        this.usuarioActual = usuario;
    }

    public User getUsuarioActual() {
        return usuarioActual;
    }

    public String getIdUsuarioActual() {
        return usuarioActual != null ? usuarioActual.getIdUser() : null;
    }

    public String getRolUsuarioActual() {
        return usuarioActual != null ? usuarioActual.getRole() : null;
    }

    public void cerrarSesion() {
        this.usuarioActual = null;
    }
}
package org.sportlife.system.service;

public enum LoginStatus {
    SUCCESS,             // Login exitoso
    USER_NOT_FOUND,      // El usuario o correo no existe
    INVALID_PASSWORD,    // La contraseña es incorrecta
    CREDENTIALS_EMPTY,   // Campos vacíos
    ERROR                // Error de conexión con la base de datos
}
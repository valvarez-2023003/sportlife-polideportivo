/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.sportlife.system.service;

/**
 *
 * @author Cristofer Ramos
 */

public enum UserStatus {
    LOGIN_SUCCESS,          // Autenticación exitosa
    USER_NOT_FOUND,         // El usuario o correo no existe
    INVALID_PASSWORD,       // La contraseña es incorrecta
    CREDENTIALS_EMPTY,      // Campos vacíos
    ERROR_LOGIN             // Error de conexión con la base de datos
}

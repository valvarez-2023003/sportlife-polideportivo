/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.sportlife.system.repository;

import org.sportlife.system.model.User;

/**
 *
 * @author Cristofer Ramos
 */

public interface UserInterface {
    User checkUserExistsStrict(String usernameOrEmail);
    User verifyUserPassword(String usernameOrEmail, String password);
}

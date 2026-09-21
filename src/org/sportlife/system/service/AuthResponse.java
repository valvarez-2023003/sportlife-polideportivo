/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.sportlife.system.service;

import org.sportlife.system.model.User;

/**
 *
 * @author Cristofer Ramos
 */

public class AuthResponse {
    private UserStatus status;
    private User user;

    public AuthResponse(UserStatus status, User user){
        this.status = status;
        this.user = user;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }  
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.sportlife.system.service;

import org.sportlife.system.model.User;
import org.sportlife.system.repository.UserRepository;
import org.sportlife.system.utils.Validations;

/**
 *
 * @author Cristofer Ramos
 */

public class UserService {
    private final UserRepository userRepository;
    private final Validations validations;

    public UserService(){
        this.userRepository = new UserRepository();
        this.validations = new Validations();
    }

    public AuthResponse authenticate(String usernameOrEmail, String password){
        if(validations.emptyText(usernameOrEmail) || validations.emptyText(password)){
            return new AuthResponse(UserStatus.CREDENTIALS_EMPTY, null);
        }

        try{
            User foundUser = userRepository.checkUserExistsStrict(usernameOrEmail.trim());
            
            if(foundUser == null){
                return new AuthResponse(UserStatus.USER_NOT_FOUND, null);
            }

            User authenticatedUser = userRepository.verifyUserPassword(usernameOrEmail.trim(), password.trim());
            
            if(authenticatedUser != null){
                return new AuthResponse(UserStatus.LOGIN_SUCCESS, authenticatedUser);
            }else{
                return new AuthResponse(UserStatus.INVALID_PASSWORD, null);
            }
            
        }catch (Exception e){
            e.printStackTrace();
            return new AuthResponse(UserStatus.ERROR_LOGIN, null);
        }
    }
}

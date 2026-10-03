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

    public UserService() {
        this.userRepository = new UserRepository();
    }

    public LoginResponse authenticate(String usernameOrEmail, String password) {
        if (Validations.emptyText(usernameOrEmail) || Validations.emptyText(password)) {
            return new LoginResponse(LoginStatus.CREDENTIALS_EMPTY, null);
        }

        try {
            User foundUser = userRepository.checkUserExistsStrict(usernameOrEmail.trim());

            if (foundUser == null) {
                return new LoginResponse(LoginStatus.USER_NOT_FOUND, null);
            }

            User authenticatedUser = userRepository.verifyUserPassword(usernameOrEmail.trim(), password.trim());

            if (authenticatedUser != null) {
                return new LoginResponse(LoginStatus.SUCCESS, authenticatedUser);
            } else {
                return new LoginResponse(LoginStatus.INVALID_PASSWORD, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new LoginResponse(LoginStatus.ERROR, null);
        }
    }

    public RegisterStatus registerCustomer(User user) {
        try {
            boolean success = userRepository.registerCustomer(user);
            return success ? RegisterStatus.USER_CREATED : RegisterStatus.ERROR_USER_CREATE;
        } catch (Exception e) {
            e.printStackTrace();
            return RegisterStatus.ERROR_USER_CREATE;
        }
    }
}
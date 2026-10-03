package org.sportlife.system.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.sportlife.system.model.User;
import org.sportlife.system.service.LoginResponse;
import org.sportlife.system.service.LoginStatus;
import org.sportlife.system.service.UserService;
import org.sportlife.system.utils.AlertInformation;
import org.sportlife.system.utils.Validations;
import org.sportlife.system.utils.ViewFactory;

/**
 *
 * @author Cristofer Ramos
 */
public class LoginController implements Initializable {

    @FXML private Button btnLogIn;
    @FXML private Button btnCreatOnAccount;
    @FXML private PasswordField pwdPassword;
    @FXML private TextField txtUserName;

    private final UserService userService;
    private final ViewFactory viewFactory;

    public LoginController() {
        this.userService = new UserService();
        this.viewFactory = new ViewFactory();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    public void onLogin(MouseEvent event) {
        String usernameOrEmail = txtUserName.getText().trim();
        String password = pwdPassword.getText().trim();

        if (Validations.emptyText(usernameOrEmail) || Validations.emptyText(password)) {
            AlertInformation.viewAlert("ERROR", "Campos Vacíos", "Error de Validación",
                "Por favor, ingrese su nombre de usuario o correo y contraseña.");
            return;
        }

        LoginResponse response = userService.authenticate(usernameOrEmail, password);

        switch (response.getStatus()) {
            case USER_NOT_FOUND:
                AlertInformation.viewAlert("ERROR", "Usuario No Encontrado",
                    "Error de Autenticación",
                    "El nombre de usuario o correo no existe en el sistema.");
                txtUserName.requestFocus();
                break;

            case INVALID_PASSWORD:
                AlertInformation.viewAlert("ERROR", "Contraseña Incorrecta",
                    "Error de Autenticación",
                    "La contraseña ingresada no es correcta.");
                pwdPassword.clear();
                pwdPassword.requestFocus();
                break;

            case ERROR:
                AlertInformation.viewAlert("ERROR", "Error del Sistema",
                    "Error de Base de Datos",
                    "Ocurrió un error inesperado al intentar conectar con el servidor.");
                break;

            case SUCCESS:
                User authenticatedUser = response.getUser();
                if (authenticatedUser != null && authenticatedUser.getRole() != null) {
                    String role = authenticatedUser.getRole().trim().toLowerCase();
                    viewFactory.loadView(role);
                } else {
                    AlertInformation.viewAlert("ERROR", "Rol Inválido",
                        "Error de Autenticación",
                        "El usuario no tiene rol asignado.");
                }
                break;

            case CREDENTIALS_EMPTY:
                AlertInformation.viewAlert("ERROR", "Campos Vacíos",
                    "Error de Validación",
                    "Por favor, ingrese sus credenciales.");
                break;
        }
    }

    @FXML
    public void onRegister(MouseEvent event) {
        viewFactory.viewRegister();
    }
}
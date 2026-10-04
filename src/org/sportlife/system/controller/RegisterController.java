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
import org.sportlife.system.service.RegisterStatus;
import org.sportlife.system.service.UserService;
import org.sportlife.system.utils.AlertInformation;
import org.sportlife.system.utils.Validations;
import org.sportlife.system.utils.ViewFactory;

/**
 *
 * @author Cristofer Ramos
 */
public class RegisterController implements Initializable {

    @FXML private Button btnCreateAccount;
    @FXML private Button btnLogin;
    @FXML private PasswordField pwdConfirmPassword;
    @FXML private PasswordField pwdPassword;
    @FXML private TextField txtEmail;
    @FXML private TextField txtName;
    @FXML private TextField txtLastName;
    @FXML private TextField txtNumberPhone;
    @FXML private TextField txtUserName;

    private final UserService userService;
    private final ViewFactory viewFactory;

    public RegisterController() {
        this.userService = new UserService();
        this.viewFactory = new ViewFactory();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    public void onCancel(MouseEvent event) {
        viewFactory.viewLogin();
    }

    @FXML
    public void onCreateUser(MouseEvent event) {
        String userName = txtUserName.getText().trim();
        String name = txtName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtNumberPhone.getText().trim();
        String password = pwdPassword.getText();
        String confirmPassword = pwdConfirmPassword.getText();

        // Validación de campos vacíos
        if (Validations.emptyText(userName) || Validations.emptyText(name)
                || Validations.emptyText(lastName) || Validations.emptyText(email)
                || Validations.emptyText(phone) || Validations.emptyText(password)
                || Validations.emptyText(confirmPassword)) {
            AlertInformation.viewAlert("ERROR", "Campos Vacíos", "Error de Validación",
                "Por favor, complete todos los campos del formulario.");
            return;
        }

        if (!Validations.validateLengthText(userName, 25)) {
            AlertInformation.viewAlert("ERROR", "Longitud Inválida", "Error de Campo",
                "Nombre de usuario excedió la cantidad de caracteres (Max.25).");
            return;
        }

        if(!Validations.validateOnlyLetters(name)){
            AlertInformation.viewAlert("ERROR", "Caracteres Inválidos", "Error de Campo",
                "El nombre únicamente puede contener letras (mayúsculas y minúsculas). No puede contener ningún otro símbolo.");
            return;
        }

        if(!Validations.validateLengthText(name, 50)){
            AlertInformation.viewAlert("ERROR", "Longitud Inválida", "Error de Campo",
                "El nombre excedió la cantidad máxima de caracteres permitidos (Máx. 50).");
            return;
        }
        
        if(!Validations.validateOnlyLetters(lastName)){
            AlertInformation.viewAlert("ERROR", "Caracteres Inválidos", "Error de Campo",
                "El apellido únicamente puede contener letras (mayúsculas y minúsculas). No puede contener ningún otro símbolo.");
            return;
        }

        if(!Validations.validateLengthText(lastName, 50)){
            AlertInformation.viewAlert("ERROR", "Longitud Inválida", "Error de Campo",
                "El apellido excedió la cantidad máxima de caracteres permitidos (Máx. 50).");
            return;
        }

        if(!Validations.validateEmail(email)){
            AlertInformation.viewAlert("ERROR", "Formato Inválido", "Error de Campo",
                "Correo electrónico no válido. Por favor, ingrese un formato correcto (ej: usuario@dominio.com).");
            return;
        }

        if(!Validations.validatePhone(phone)){
            AlertInformation.viewAlert("ERROR", "Teléfono Inválido", "Error de Campo",
                "El número de teléfono debe contener exactamente 8 dígitos numéricos.");
            return;
        }

        if(!Validations.equalsText(password, confirmPassword)){
            AlertInformation.viewAlert("ERROR", "Contraseñas No Coinciden", "Error de Validación",
                "La contraseña ingresada no coincide con la confirmación.");
            pwdPassword.clear();
            pwdConfirmPassword.clear();
            return;
        }

        // Crear usuario
        User newCustomer = new User();
        newCustomer.setUser(userName);
        newCustomer.setName(name);
        newCustomer.setLastname(lastName);
        newCustomer.setEmail(email);
        newCustomer.setPhone(phone);
        newCustomer.setPassword(password);

        RegisterStatus status = userService.registerCustomer(newCustomer);

        if (status == RegisterStatus.USER_CREATED) {
            AlertInformation.viewAlert("INFO", "Registro Exitoso", "Éxito",
                "El usuario ha sido registrado correctamente.");
            viewFactory.viewLogin();
        } else {
            AlertInformation.viewAlert("ERROR", "Error de Registro", "Error de Base de Datos",
                "No se pudo registrar el usuario. Intente nuevamente o contacte al administrador.");
        }
    }
}
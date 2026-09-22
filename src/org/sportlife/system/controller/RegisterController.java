/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.sportlife.system.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.sportlife.system.utils.ViewFactory;
import org.sportlife.system.model.User;
import org.sportlife.system.service.UserService;
import org.sportlife.system.service.UserStatus;
import org.sportlife.system.utils.AlertInformation;
import org.sportlife.system.utils.Validations;

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
    private final AlertInformation alertInfo;
    private final Validations validations;

    public RegisterController(){
        this.userService = new UserService();
        this.viewFactory = new ViewFactory();
        this.alertInfo = new AlertInformation();
        this.validations = new Validations();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
    }

    @FXML
    public void onCancel(MouseEvent event){
        viewFactory.viewLogin();
    }

    @FXML
    public void onCreateUser(MouseEvent event){
        String userName = txtUserName.getText().trim();
        String name = txtName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtNumberPhone.getText().trim();
        String password = pwdPassword.getText().trim();
        String confirmPassword = pwdConfirmPassword.getText().trim();

        if(validations.emptyText(userName) || validations.emptyText(name) || validations.emptyText(lastName) ||
            validations.emptyText(email) || validations.emptyText(phone) || validations.emptyText(password) || validations.emptyText(confirmPassword)) {
            alertInfo.viewAlert("ERROR", "Campos Vacíos", "Error de Validación", "Por favor, complete todos los campos del formulario.");
            return;
        }

        if(!validations.validateLengthText(userName, 25)){
            alertInfo.viewAlert("ERROR", "Longitud Inválida", "Error de Campo", "Nombre de usuario no aceptable, excedió la cantidad de caracteres.");
            return;
        }
        
        if(!validations.validateLengthText(name, 50)){
            alertInfo.viewAlert("ERROR", "Longitud Inválida", "Error de Campo", "Nombre excedió en caracteres, no aceptado.");
            return;
        }
        
        if(!validations.validateLengthText(lastName, 50)){
            alertInfo.viewAlert("ERROR", "Longitud Inválida", "Error de Campo", "Apellido excedió en caracteres, no aceptado.");
            return;
        }

        if(!validations.validateEmail(email)){
            alertInfo.viewAlert("ERROR", "Formato Inválido", "Error de Campo", "Correo electrónico no válido. Por favor, ingrese un formato correcto (ej: usuario@dominio.com).");
            return;
        }
        
        if(!validations.validateLengthText(phone, 10)){
           alertInfo.viewAlert("ERROR", "Longitud Inválida", "Error de Campo", "Número de teléfono excedió en caracteres, no aceptado.");
           return;
        }
        
        if (!validations.equalsText(password, confirmPassword)){
            alertInfo.viewAlert("ERROR", "Contraseñas No Coinciden", "Error de Validación", "La contraseña ingresada no coincide con la confirmación.");
            pwdPassword.clear();
            pwdConfirmPassword.clear();
            return;
        }

        User newCustomer = new User();
        newCustomer.setUser(userName);
        newCustomer.setName(name);
        newCustomer.setLastname(lastName);
        newCustomer.setEmail(email);
        newCustomer.setPhone(phone);
        newCustomer.setPassword(password);

        UserStatus status = userService.registerCustomer(newCustomer);

        if(status == UserStatus.USER_CREATED){
            alertInfo.viewAlert("INFO", "Registro Exitoso", "Éxito", "El usuario ha sido registrado correctamente. Ahora puede iniciar sesión.");
            viewFactory.viewLogin(); // Redirige al login
        } else {
            alertInfo.viewAlert("ERROR", "Error de Registro", "Error de Base de Datos", "No se pudo registrar el usuario. Intente nuevamente o contacte al administrador.");
        }    
    }
}

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

public class RegisterController implements Initializable {
    @FXML private Button btnCreateAccount;
    @FXML private Button btnLogin;
    @FXML private PasswordField pwdConfirmPassword;
    @FXML private PasswordField pwdPassword;
    @FXML private TextField txtEmail;
    @FXML private TextField txtFullName;
    @FXML private TextField txtNumberPhone;
    @FXML private TextField txtUserName;

    private ViewFactory viewFactory;

    public RegisterController(){
        this.viewFactory = new ViewFactory();
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
        System.out.println("Botón crear cuenta funcionando - Lógica pendiente");
    }
}

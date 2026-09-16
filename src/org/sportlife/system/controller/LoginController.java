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
import javafx.scene.input.MouseEvent;
import org.sportlife.system.utils.ViewFactory;

public class LoginController implements Initializable {

    @FXML
    private Button btnCrearCuenta;

    @FXML
    private Button btnInciaSesion;

    @FXML
    private PasswordField pwdPassword;

    @FXML
    private TextField txtUserName;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

@FXML
public void onLogin(MouseEvent event) {
    System.out.println("Botón iniciar sesión funcionando");
}

@FXML
public void onRegister(MouseEvent event) {
    ViewFactory viewFactory = new ViewFactory();
    viewFactory.viewRegister();
}
}

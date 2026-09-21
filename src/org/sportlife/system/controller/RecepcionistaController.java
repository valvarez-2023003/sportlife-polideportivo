/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.sportlife.system.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import org.sportlife.system.utils.ViewFactory;

/**
 *
 * @author Cristofer Ramos
 */

public class RecepcionistaController implements Initializable {
    private final ViewFactory viewFactory;

    public RecepcionistaController(){
        this.viewFactory = new ViewFactory();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
    }

    @FXML
    public void returnToLogin(MouseEvent event){
        System.out.println("Recepcionista cerrando sesión...");
        viewFactory.viewLogin();
    }
}

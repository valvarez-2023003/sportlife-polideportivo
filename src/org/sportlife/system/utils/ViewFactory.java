/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.sportlife.system.utils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import org.sportlife.system.ClasePrincipal;

/**
 *
 * @author Cristofer Ramos
 */

public class ViewFactory {
    private final String PATH_VIEW = "/org/sportlife/system/view/";

    public Scene loadFileFXML(String nameFXML, int width, int height){
        String pathOfFile = PATH_VIEW + nameFXML;
        try{
            FXMLLoader loaderFXML = new FXMLLoader();
            URL urlFile = ClasePrincipal.class.getResource(pathOfFile);
            loaderFXML.setBuilderFactory(new JavaFXBuilderFactory());
            loaderFXML.setLocation(urlFile);
            return new Scene(loaderFXML.load(), width, height);
        }catch(IOException e){
            throw new UncheckedIOException("Error al cargar el archivo FXML: " + pathOfFile, e);
        }
    }

    public void loadScene(String nameFXML){
        Scene scene = null;
        try{
            switch (nameFXML.toLowerCase()){
                case "login", "loginview" ->{
                    SceneManager.getInstanciaSceneManager().getStagePrincipal().setTitle("LOGIN - SPORTLIFE");
                    SceneManager.getInstanciaSceneManager().getStagePrincipal().setResizable(false);
                    scene = loadFileFXML("LoginView.fxml", 779, 528);
                }
                case "register", "registerview" ->{
                    SceneManager.getInstanciaSceneManager().getStagePrincipal().setTitle("REGISTRO - SPORTLIFE");
                    SceneManager.getInstanciaSceneManager().getStagePrincipal().setResizable(false);
                    scene = loadFileFXML("RegisterView.fxml", 950, 600);
                }
                case "gerente" ->{
                    SceneManager.getInstanciaSceneManager().getStagePrincipal().setTitle("SPORTLIFE - PANEL GERENTE");
                    SceneManager.getInstanciaSceneManager().getStagePrincipal().setResizable(true);
                    scene = loadFileFXML("GerenteView.fxml", 900, 600);
                }
                case "administrador" ->{
                    SceneManager.getInstanciaSceneManager().getStagePrincipal().setTitle("SPORTLIFE - PANEL ADMINISTRADOR");
                    SceneManager.getInstanciaSceneManager().getStagePrincipal().setResizable(true);
                    scene = loadFileFXML("AdminView.fxml", 900, 600);
                }
                case "recepcionista" ->{
                    SceneManager.getInstanciaSceneManager().getStagePrincipal().setTitle("SPORTLIFE - PANEL RECEPCIONISTA");
                    SceneManager.getInstanciaSceneManager().getStagePrincipal().setResizable(true);
                    scene = loadFileFXML("RecepcionistaView.fxml", 900, 600);
                }
                case "user", "formulario" ->{
                    SceneManager.getInstanciaSceneManager().getStagePrincipal().setTitle("SPORTLIFE - FORMULARIO CLIENTE");
                    SceneManager.getInstanciaSceneManager().getStagePrincipal().setResizable(false);
                    scene = loadFileFXML("FormularioView.fxml", 1000, 500);
                }
                default ->{
                    scene = loadFileFXML("LoginView.fxml", 779, 528);
                }
            }
            if(scene != null){
                SceneManager.getInstanciaSceneManager().changeScene(scene);
            }
        }catch (NullPointerException e){
            System.err.println("Error al cargar la escena: " + nameFXML);
        }
    }
    public void viewLogin(){ 
        loadScene("login"); 
    }
    public void viewRegister(){ 
        loadScene("register"); 
    }
}

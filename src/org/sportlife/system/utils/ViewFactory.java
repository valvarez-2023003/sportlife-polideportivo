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

public class ViewFactory {

    private final String PATH_VIEW = "/org/sportlife/system/view/";

    public Scene loadFileFXML(String nameFXML, int width, int height) {

        String pathOfFile = PATH_VIEW + nameFXML;

        try {

            FXMLLoader loaderFXML = new FXMLLoader();

            URL urlFile = ClasePrincipal.class.getResource(pathOfFile);

            loaderFXML.setBuilderFactory(new JavaFXBuilderFactory());
            loaderFXML.setLocation(urlFile);

            return new Scene(loaderFXML.load(), width, height);

        } catch (IOException e) {

            throw new UncheckedIOException(e);
        }
    }

    public void loadScene(String nameFXML) {

        Scene scene = null;

        try {

            switch (nameFXML) {

                case "login" -> {
                    SceneManager.getInstanciaSceneManager()
                            .getStagePrincipal()
                            .setTitle("LOGIN - SPORTLIFE");

                    SceneManager.getInstanciaSceneManager()
                            .getStagePrincipal()
                            .setResizable(false);

                    scene = loadFileFXML("LoginView.fxml", 779, 528);
                }

                case "register" -> {
                    SceneManager.getInstanciaSceneManager()
                            .getStagePrincipal()
                            .setTitle("REGISTRO - SPORTLIFE");

                    SceneManager.getInstanciaSceneManager()
                            .getStagePrincipal()
                            .setResizable(false);

                    scene = loadFileFXML("RegisterView.fxml", 950, 581);
                }

                default -> {
                    scene = loadFileFXML("LoginView.fxml", 779, 528);
                }
            }

            SceneManager.getInstanciaSceneManager().changeScene(scene);

        } catch (NullPointerException objetoNulo) {

            System.out.println("Error al cargar la escena.");
        }
    }

    public void viewLogin() {
        loadScene("login");
    }

    public void viewRegister() {
        loadScene("register");
    }
}

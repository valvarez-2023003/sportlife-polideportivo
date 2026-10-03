package org.sportlife.system.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.sportlife.system.utils.ViewFactory;

/**
 * Controlador de la barra lateral del panel de administrador.
 * Se encarga de la navegación entre las vistas del <center>.
 */
public class BarraLateralAdminController implements Initializable {

    @FXML private BorderPane pnlRoot;
    @FXML private VBox pnlSidebar;
    @FXML private HBox pnlLogoContainer;
    @FXML private ImageView imgLogo;
    @FXML private Button btnHome;
    @FXML private Button btnEditFields;
    @FXML private Button btnCalendar;
    @FXML private Button btnLogout;

    @FXML private StackPane pnlContentCenter;

    private final ViewFactory viewFactory;

    private static final String PATH_VIEW = "/org/sportlife/system/view/";
    private String currentView = "";

    public BarraLateralAdminController() {
        this.viewFactory = new ViewFactory();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadContent("AdminView.fxml");
    }

    // ==========================================================
    //  MÉTODOS DE NAVEGACIÓN
    // ==========================================================

    @FXML
    public void onOpenHome(MouseEvent event) {
        loadContent("AdminView.fxml");
        updateSelectedButton(btnHome);
    }

    @FXML
    public void onOpenEditFields(MouseEvent event) {
        loadContent("GestionCanchasView.fxml");
        updateSelectedButton(btnEditFields);
    }

@FXML
public void onOpenCalendar(MouseEvent event) {
    loadContent("CalendarioView.fxml");
    updateSelectedButton(btnCalendar);
}

    @FXML
    public void onLogout(MouseEvent event) {
        System.out.println("Cerrando sesión...");
        viewFactory.viewLogin();
    }

    // ==========================================================
    //  UTILIDADES
    // ==========================================================

    private void loadContent(String fxmlName) {
        if (fxmlName.equals(currentView)) return;

        try {
            URL url = getClass().getResource(PATH_VIEW + fxmlName);
            if (url == null) {
                System.err.println(">>> No se encontró la vista: " + fxmlName);
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Node content = loader.load();
            pnlContentCenter.getChildren().setAll(content);
            currentView = fxmlName;

            System.out.println(">>> Vista cargada: " + fxmlName);

        } catch (IOException e) {
            System.err.println(">>> Error cargando la vista: " + fxmlName);
            e.printStackTrace();
        }
    }

    private void updateSelectedButton(Button selected) {
        Button[] allButtons = {btnHome, btnEditFields, btnCalendar};
        for (Button b : allButtons) {
            b.getStyleClass().remove("menu-button-selected");
            if (!b.getStyleClass().contains("menu-button")) {
                b.getStyleClass().add("menu-button");
            }
        }
        selected.getStyleClass().remove("menu-button");
        if (!selected.getStyleClass().contains("menu-button-selected")) {
            selected.getStyleClass().add("menu-button-selected");
        }
    }
}
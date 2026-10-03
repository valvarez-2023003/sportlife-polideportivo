package org.sportlife.system.controller;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Controlador de la vista principal del panel de administrador (INICIO).
 */
public class AdminController implements Initializable {

    @FXML private HBox pnlHeader;
    @FXML private Label lblHeaderTitle;
    @FXML private HBox pnlUserBadge;
    @FXML private Label lblUserIcon;
    @FXML private Label lblUserRole;

    @FXML private VBox pnlMainCard;
    @FXML private VBox pnlWelcomeContainer;
    @FXML private Label lblWelcomeTitle;
    @FXML private Label lblWelcomeSubtitle;
    @FXML private HBox pnlStatCard;
    @FXML private Label lblStatCardTitle;

    @FXML private TableView<?> tblFields;
    @FXML private TableColumn<?, ?> colCode;
    @FXML private TableColumn<?, ?> colDescription;
    @FXML private TableColumn<?, ?> colDiscipline;
    @FXML private TableColumn<?, ?> colCovered;
    @FXML private TableColumn<?, ?> colStatus;
    @FXML private TableColumn<?, ?> colPricePerHour;

    // Formato de precio: "Q 1,500.00"
    private final DecimalFormat formatoPrecio = new DecimalFormat("'Q ' #,##0.00");

    public AdminController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(">>> AdminController (INICIO) inicializado.");
        // TODO: cargar las canchas disponibles en tblFields desde la BD
    }

    /**
     * Formatea un precio para mostrarlo en la tabla.
     * Al llenar la tabla:
     *     colPricePerHour.setCellValueFactory(data -> 
     *         new SimpleStringProperty(formatearPrecio(data.getValue().getPricePerHour())));
     */
    public String formatearPrecio(Double precio) {
        if (precio == null) return "";
        return formatoPrecio.format(precio);
    }
}
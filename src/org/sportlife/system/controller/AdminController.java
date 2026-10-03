package org.sportlife.system.controller;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.sportlife.system.model.Cancha;
import org.sportlife.system.service.CanchaService;
import org.sportlife.system.utils.AlertInformation;

/**
 * Controlador de la vista principal del panel de administrador (INICIO).
 *
 * @author Cristofer Ramos
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

    @FXML private ComboBox<String> cmbStatusFilter;

    @FXML private TableView<Cancha> tblFields;
    @FXML private TableColumn<Cancha, Integer> colCode;
    @FXML private TableColumn<Cancha, String> colDescription;
    @FXML private TableColumn<Cancha, String> colDiscipline;
    @FXML private TableColumn<Cancha, String> colCovered;
    @FXML private TableColumn<Cancha, String> colStatus;
    @FXML private TableColumn<Cancha, String> colPricePerHour;

    private final CanchaService canchaService = new CanchaService();
    private final ObservableList<Cancha> canchas = FXCollections.observableArrayList();
    private FilteredList<Cancha> canchasFiltradas;
    private final DecimalFormat formatoPrecio = new DecimalFormat("'Q ' #,##0.00");

    private static final String FILTRO_TODAS = "TODAS";
    private static final String FILTRO_DISPONIBLES = "DISPONIBLES";
    private static final String FILTRO_MANTENIMIENTO = "EN MANTENIMIENTO";

    public AdminController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(">>> AdminController (INICIO) inicializado.");
        configurarColumnas();
        configurarFiltro();
        cargarCanchas();
    }

    // ==========================================================
    //  CONFIGURACIÓN
    // ==========================================================

    private void configurarColumnas() {
        colCode.setCellValueFactory(data ->
            new SimpleObjectProperty<>(data.getValue().getIdCancha()));
        colDescription.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getNombre()));
        colDiscipline.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getTipoDeporte()));
        colCovered.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getTechadaTexto()));
        colStatus.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getEstado()));
        colPricePerHour.setCellValueFactory(data ->
            new SimpleStringProperty(formatearPrecio(data.getValue().getPrecioPorHora())));
    }

    private void configurarFiltro() {
        cmbStatusFilter.getItems().addAll(
            FILTRO_TODAS,
            FILTRO_DISPONIBLES,
            FILTRO_MANTENIMIENTO
        );
        cmbStatusFilter.setValue(FILTRO_TODAS);

        // Envolver la lista en FilteredList para aplicar el filtro en la UI
        canchasFiltradas = new FilteredList<>(canchas, c -> true);

        // Escuchar cambios en el ComboBox
        cmbStatusFilter.valueProperty().addListener((obs, oldVal, newVal) -> {
            aplicarFiltro(newVal);
        });

        tblFields.setItems(canchasFiltradas);
    }

    private void aplicarFiltro(String filtro) {
        if (filtro == null) filtro = FILTRO_TODAS;

        switch (filtro) {
            case FILTRO_DISPONIBLES -> canchasFiltradas.setPredicate(
                c -> "Disponible".equalsIgnoreCase(c.getEstado()));
            case FILTRO_MANTENIMIENTO -> canchasFiltradas.setPredicate(
                c -> "En mantenimiento".equalsIgnoreCase(c.getEstado()));
            default -> canchasFiltradas.setPredicate(c -> true);
        }
    }

    // ==========================================================
    //  CARGA DE DATOS
    // ==========================================================

    private void cargarCanchas() {
        try {
            // Cargamos TODAS las canchas; el filtro se aplica en la UI
            List<Cancha> lista = canchaService.listarTodas();
            canchas.setAll(lista);
            System.out.println(">>> Canchas cargadas: " + lista.size());
        } catch (Exception e) {
            System.err.println(">>> Error al cargar canchas: " + e.getMessage());
            AlertInformation.viewAlert("ERROR", "Error de carga",
                "Error de Base de Datos", e.getMessage());
        }
    }

    // ==========================================================
    //  UTILIDAD
    // ==========================================================

    public String formatearPrecio(Double precio) {
        if (precio == null) return "";
        return formatoPrecio.format(precio);
    }
}
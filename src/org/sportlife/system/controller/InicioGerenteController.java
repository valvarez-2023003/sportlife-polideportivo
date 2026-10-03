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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.sportlife.system.model.Cancha;
import org.sportlife.system.service.CanchaService;
import org.sportlife.system.utils.AlertInformation;

/**
 * Controlador del INICIO del Gerente: ver las canchas disponibles.
 *
 * @author Cristofer Ramos
 */
public class InicioGerenteController implements Initializable {

    @FXML private ComboBox<String> cmbStatusFilter;
    @FXML private TableView<Cancha> tblCourts;
    @FXML private TableColumn<Cancha, Integer> colCode;
    @FXML private TableColumn<Cancha, String> colDescription;
    @FXML private TableColumn<Cancha, String> colDiscipline;
    @FXML private TableColumn<Cancha, String> colCovered;
    @FXML private TableColumn<Cancha, String> colStatus;
    @FXML private TableColumn<Cancha, String> colPricePerHour;

    private final CanchaService canchaService = new CanchaService();
    private final ObservableList<Cancha> canchas = FXCollections.observableArrayList();
    private FilteredList<Cancha> filtradas;
    private final DecimalFormat formatoPrecio = new DecimalFormat("'Q ' #,##0.00");

    private static final String FILTRO_TODAS = "TODAS";
    private static final String FILTRO_DISPONIBLES = "DISPONIBLES";
    private static final String FILTRO_MANTENIMIENTO = "EN MANTENIMIENTO";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(">>> InicioGerenteController inicializado.");
        configurarColumnas();
        configurarFiltro();
        cargarCanchas();
    }

    private void configurarColumnas() {
        colCode.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getIdCancha()));
        colDescription.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombre()));
        colDiscipline.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTipoDeporte()));
        colCovered.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTechadaTexto()));
        colStatus.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getEstado()));
        colPricePerHour.setCellValueFactory(d -> new SimpleStringProperty(formatearPrecio(d.getValue().getPrecioPorHora())));
    }

    private void configurarFiltro() {
        cmbStatusFilter.getItems().addAll(FILTRO_TODAS, FILTRO_DISPONIBLES, FILTRO_MANTENIMIENTO);
        cmbStatusFilter.setValue(FILTRO_TODAS);

        filtradas = new FilteredList<>(canchas, c -> true);
        cmbStatusFilter.valueProperty().addListener((obs, o, n) -> aplicarFiltro(n));
        tblCourts.setItems(filtradas);
    }

    private void aplicarFiltro(String filtro) {
        if (filtro == null) filtro = FILTRO_TODAS;
        switch (filtro) {
            case FILTRO_DISPONIBLES ->
                filtradas.setPredicate(c -> "Disponible".equalsIgnoreCase(c.getEstado()));
            case FILTRO_MANTENIMIENTO ->
                filtradas.setPredicate(c -> "En mantenimiento".equalsIgnoreCase(c.getEstado()));
            default ->
                filtradas.setPredicate(c -> true);
        }
    }

    private void cargarCanchas() {
        try {
            List<Cancha> lista = canchaService.listarTodas();
            canchas.setAll(lista);
            System.out.println(">>> Canchas cargadas: " + lista.size());
        } catch (Exception e) {
            AlertInformation.viewAlert("ERROR", "Error de carga", "Error de BD", e.getMessage());
        }
    }

    private String formatearPrecio(Double precio) {
        if (precio == null) return "";
        return formatoPrecio.format(precio);
    }
}
package org.sportlife.system.controller;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import org.sportlife.system.model.Reserva;
import org.sportlife.system.service.ReservaService;
import org.sportlife.system.utils.AlertInformation;

/**
 * Controlador de la vista INICIO del recepcionista.
 * Muestra solicitudes pendientes y permite confirmar o rechazar.
 *
 * @author Cristofer Ramos
 */
public class RecepcionistaController implements Initializable {

    @FXML private Label lblHeaderTitle;
    @FXML private Label lblUserIcon;
    @FXML private Label lblUserRole;

    @FXML private TableView<Reserva> tblPendingReservations;
    @FXML private TableColumn<Reserva, Integer> colIdReserva;
    @FXML private TableColumn<Reserva, String> colClient;
    @FXML private TableColumn<Reserva, String> colCourt;
    @FXML private TableColumn<Reserva, String> colDiscipline;
    @FXML private TableColumn<Reserva, String> colCovered;
    @FXML private TableColumn<Reserva, String> colDate;
    @FXML private TableColumn<Reserva, String> colTimeRange;
    @FXML private TableColumn<Reserva, Integer> colTotalHours;
    @FXML private TableColumn<Reserva, String> colTotalCost;

    private final ReservaService reservaService = new ReservaService();
    private final ObservableList<Reserva> pendientes = FXCollections.observableArrayList();
    private final DecimalFormat formatoPrecio = new DecimalFormat("'Q ' #,##0.00");
    private final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(">>> RecepcionistaController (INICIO) inicializado.");
        configurarColumnas();
        cargarPendientes();
    }

    private void configurarColumnas() {
        colIdReserva.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getIdReserva()));
        colClient.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCliente()));
        colCourt.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombreCancha()));
        colDiscipline.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTipoDeporte()));
        colCovered.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTechadaTexto()));
        colDate.setCellValueFactory(d -> new SimpleStringProperty(
            d.getValue().getFechaReserva() == null ? "" : formatoFecha.format(d.getValue().getFechaReserva())));
        colTimeRange.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getHorarioTexto()));
        colTotalHours.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getTotalHoras()));
        colTotalCost.setCellValueFactory(d -> new SimpleStringProperty(
            formatoPrecio.format(d.getValue().getCostoTotal())));
    }

    private void cargarPendientes() {
        try {
            List<Reserva> lista = reservaService.listarPendientes();
            pendientes.setAll(lista);
            tblPendingReservations.setItems(pendientes);
            System.out.println(">>> Reservas pendientes: " + lista.size());
        } catch (Exception e) {
            e.printStackTrace();
            AlertInformation.viewAlert("ERROR", "Error de carga",
                "Error de Base de Datos", e.getMessage());
        }
    }

    @FXML
    public void onConfirm(MouseEvent event) {
        Reserva r = tblPendingReservations.getSelectionModel().getSelectedItem();
        if (r == null) {
            AlertInformation.viewAlert("WARNING", "Sin selección", "Aviso",
                "Seleccione una reserva para confirmar.");
            return;
        }
        try {
            reservaService.confirmar(r.getIdReserva());
            AlertInformation.viewAlert("INFO", "Reserva confirmada", "Éxito",
                "La reserva #" + r.getIdReserva() + " fue confirmada.");
            cargarPendientes();
        } catch (Exception e) {
            AlertInformation.viewAlert("ERROR", "Error", "Error de BD", e.getMessage());
        }
    }

    @FXML
    public void onReject(MouseEvent event) {
        Reserva r = tblPendingReservations.getSelectionModel().getSelectedItem();
        if (r == null) {
            AlertInformation.viewAlert("WARNING", "Sin selección", "Aviso",
                "Seleccione una reserva para rechazar.");
            return;
        }
        try {
            reservaService.rechazar(r.getIdReserva());
            AlertInformation.viewAlert("INFO", "Reserva rechazada", "Éxito",
                "La reserva #" + r.getIdReserva() + " fue rechazada.");
            cargarPendientes();
        } catch (Exception e) {
            AlertInformation.viewAlert("ERROR", "Error", "Error de BD", e.getMessage());
        }
    }

    @FXML
    public void onRefresh(MouseEvent event) {
        cargarPendientes();
    }
}
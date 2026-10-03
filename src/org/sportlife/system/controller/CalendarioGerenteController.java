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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.sportlife.system.model.Reserva;
import org.sportlife.system.service.ReservaService;
import org.sportlife.system.utils.AlertInformation;

/**
 * Controlador del Calendario del Gerente (solo confirmadas).
 *
 * @author Cristofer Ramos
 */
public class CalendarioGerenteController implements Initializable {

    @FXML private TableView<Reserva> tblCalendar;
    @FXML private TableColumn<Reserva, String> colDate;
    @FXML private TableColumn<Reserva, String> colCourt;
    @FXML private TableColumn<Reserva, String> colClient;
    @FXML private TableColumn<Reserva, String> colTimeRange;
    @FXML private TableColumn<Reserva, Integer> colTotalHours;
    @FXML private TableColumn<Reserva, String> colCost;

    private final ReservaService reservaService = new ReservaService();
    private final ObservableList<Reserva> reservas = FXCollections.observableArrayList();
    private final DecimalFormat formatoPrecio = new DecimalFormat("'Q ' #,##0.00");
    private final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(">>> CalendarioGerenteController inicializado.");
        colDate.setCellValueFactory(d -> new SimpleStringProperty(
            d.getValue().getFechaReserva() == null ? "" : formatoFecha.format(d.getValue().getFechaReserva())));
        colCourt.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombreCancha()));
        colClient.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCliente()));
        colTimeRange.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getHorarioTexto()));
        colTotalHours.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getTotalHoras()));
        colCost.setCellValueFactory(d -> new SimpleStringProperty(formatoPrecio.format(d.getValue().getCostoTotal())));
        cargarConfirmadas();
    }

    private void cargarConfirmadas() {
        try {
            List<Reserva> todas = reservaService.listarTodas();
            reservas.setAll(todas.stream()
                .filter(r -> "Confirmada".equalsIgnoreCase(r.getEstadoReserva()))
                .toList());
            tblCalendar.setItems(reservas);
            System.out.println(">>> Reservas confirmadas: " + reservas.size());
        } catch (Exception e) {
            AlertInformation.viewAlert("ERROR", "Error de carga", "Error de BD", e.getMessage());
        }
    }
}
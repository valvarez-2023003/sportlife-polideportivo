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
import javafx.scene.input.MouseEvent;
import org.sportlife.system.model.Reserva;
import org.sportlife.system.service.ReservaService;
import org.sportlife.system.utils.AlertInformation;
import org.sportlife.system.utils.SessionManager;

/**
 * Controlador del INICIO del cliente (Mis Reservas).
 *
 * @author Cristofer Ramos
 */
public class ClienteInicioController implements Initializable {

    @FXML private TableView<Reserva> tblMyReservations;
    @FXML private TableColumn<Reserva, Integer> colIdReserva;
    @FXML private TableColumn<Reserva, String> colCourt;
    @FXML private TableColumn<Reserva, String> colDiscipline;
    @FXML private TableColumn<Reserva, String> colDate;
    @FXML private TableColumn<Reserva, String> colTimeRange;
    @FXML private TableColumn<Reserva, Integer> colTotalHours;
    @FXML private TableColumn<Reserva, String> colCost;
    @FXML private TableColumn<Reserva, String> colStatus;

    private final ReservaService reservaService = new ReservaService();
    private final ObservableList<Reserva> reservas = FXCollections.observableArrayList();
    private final DecimalFormat formatoPrecio = new DecimalFormat("'Q ' #,##0.00");
    private final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(">>> ClienteInicioController inicializado.");
        configurarColumnas();
        cargarMisReservas();
    }

    private void configurarColumnas() {
        colIdReserva.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getIdReserva()));
        colCourt.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombreCancha()));
        colDiscipline.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTipoDeporte()));
        colDate.setCellValueFactory(d -> new SimpleStringProperty(
            d.getValue().getFechaReserva() == null ? "" : formatoFecha.format(d.getValue().getFechaReserva())));
        colTimeRange.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getHorarioTexto()));
        colTotalHours.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getTotalHoras()));
        colCost.setCellValueFactory(d -> new SimpleStringProperty(formatoPrecio.format(d.getValue().getCostoTotal())));
        colStatus.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getEstadoReserva()));
    }

    private void cargarMisReservas() {
        try {
            String idUser = SessionManager.getInstancia().getIdUsuarioActual();
            if (idUser == null) {
                AlertInformation.viewAlert("ERROR", "Sesión inválida", "Error", "No hay usuario autenticado.");
                return;
            }
            List<Reserva> lista = reservaService.listarPorUsuario(idUser);
            reservas.setAll(lista);
            tblMyReservations.setItems(reservas);
            System.out.println(">>> Mis reservas: " + lista.size());
        } catch (Exception e) {
            AlertInformation.viewAlert("ERROR", "Error de carga", "Error de BD", e.getMessage());
        }
    }

    @FXML
    public void onRefresh(MouseEvent event) {
        cargarMisReservas();
    }

    @FXML
    public void onViewReceipt(MouseEvent event) {
        Reserva r = tblMyReservations.getSelectionModel().getSelectedItem();
        if (r == null) {
            AlertInformation.viewAlert("WARNING", "Sin selección", "Aviso",
                "Seleccione una reserva para ver el comprobante.");
            return;
        }
        if (!"Confirmada".equalsIgnoreCase(r.getEstadoReserva())) {
            AlertInformation.viewAlert("WARNING", "Reserva no confirmada", "Aviso",
                "Solo puedes ver el comprobante de reservas confirmadas.");
            return;
        }
        // TODO: abrir vista de comprobante
        AlertInformation.viewAlert("INFO", "Comprobante", "Reserva #" + r.getIdReserva(),
            "Cancha: " + r.getNombreCancha() + "\n" +
            "Cliente: " + r.getCliente() + "\n" +
            "Fecha: " + formatoFecha.format(r.getFechaReserva()) + "\n" +
            "Horario: " + r.getHorarioTexto() + "\n" +
            "Total: " + formatoPrecio.format(r.getCostoTotal()));
    }
}
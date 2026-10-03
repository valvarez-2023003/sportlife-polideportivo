package org.sportlife.system.controller;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
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
import org.sportlife.system.model.Reserva;
import org.sportlife.system.service.ReservaService;
import org.sportlife.system.utils.AlertInformation;

/**
 * Controlador del Calendario del Administrador. Muestra todas las reservas y su
 * estado actual (Ocupada ahora / Reservada / Libre).
 *
 * @author Cristofer Ramos
 */
public class CalendarioController implements Initializable {

    @FXML
    private Label lblHeaderTitle;
    @FXML
    private Label lblUserIcon;
    @FXML
    private Label lblUserRole;

    @FXML
    private TableView<Reserva> tblReservations;
    @FXML
    private TableColumn<Reserva, String> colDate;
    @FXML
    private TableColumn<Reserva, String> colCourt;
    @FXML
    private TableColumn<Reserva, String> colClient;
    @FXML
    private TableColumn<Reserva, String> colTimeRange;
    @FXML
    private TableColumn<Reserva, Integer> colTotalHours;
    @FXML
    private TableColumn<Reserva, String> colCost;
    @FXML
    private TableColumn<Reserva, String> colLiveStatus;

    private final ReservaService reservaService = new ReservaService();
    private final ObservableList<Reserva> reservas = FXCollections.observableArrayList();
    private final DecimalFormat formatoPrecio = new DecimalFormat("'Q ' #,##0.00");
    private final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(">>> CalendarioController (Admin) inicializado.");
        configurarColumnas();
        cargarReservas();
    }

    private void configurarColumnas() {
        colDate.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getFechaReserva() == null ? "" : formatoFecha.format(d.getValue().getFechaReserva())));
        colCourt.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombreCancha()));
        colClient.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCliente()));
        colTimeRange.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getHorarioTexto()));
        colTotalHours.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getTotalHoras()));
        colCost.setCellValueFactory(d -> new SimpleStringProperty(formatoPrecio.format(d.getValue().getCostoTotal())));

        // Columna de estado calculado: Ocupada ahora / Reservada / Finalizada / Cancelada
        colLiveStatus.setCellValueFactory(d -> new SimpleStringProperty(calcularEstadoTemporal(d.getValue())));
    }

    /**
     * Calcula el estado temporal de una reserva: - "En curso" si es hoy y la
     * hora actual está entre inicio y fin. - "Próxima" si es hoy o futura y aún
     * no ha empezado. - "Finalizada" si ya pasó la hora fin. - Otros estados
     * (Rechazada, Cancelada) se muestran tal cual.
     */
    private String calcularEstadoTemporal(Reserva r) {
        if (r == null) {
            return "";
        }

        // Los estados de negocio tienen prioridad
        String estado = r.getEstadoReserva();
        if ("Rechazada".equalsIgnoreCase(estado) || "Cancelada".equalsIgnoreCase(estado)
                || "Pendiente".equalsIgnoreCase(estado)) {
            return estado;
        }

        // Para reservas Confirmadas, calculamos el estado temporal
        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();
        LocalDate fechaReserva = r.getFechaReserva();

        if (fechaReserva == null) {
            return estado;
        }

        if (fechaReserva.isBefore(hoy)) {
            return "Finalizada";
        } else if (fechaReserva.isAfter(hoy)) {
            return "Próxima";
        } else {
            // Es hoy: validar que las horas no sean nulas
            if (r.getHoraInicio() == null || r.getHoraFin() == null) {
                return "Hoy (sin horario)";
            }
            if (ahora.isBefore(r.getHoraInicio())) {
                return "Hoy (más tarde)";
            } else if (ahora.isAfter(r.getHoraFin())) {
                return "Finalizada hoy";
            } else {
                return "En curso";
            }
        }
    }

    private void cargarReservas() {
        try {
            List<Reserva> todas = reservaService.listarTodas();

            // Solo mostrar las reservas CONFIRMADAS
            List<Reserva> confirmadas = todas.stream()
                    .filter(r -> "Confirmada".equalsIgnoreCase(r.getEstadoReserva()))
                    .toList();

            reservas.setAll(confirmadas);
            tblReservations.setItems(reservas);
            System.out.println(">>> Reservas confirmadas en calendario admin: " + confirmadas.size());
        } catch (Exception e) {
            AlertInformation.viewAlert("ERROR", "Error de carga",
                    "Error de Base de Datos", e.getMessage());
        }
    }

    @FXML
    public void onRefresh(javafx.scene.input.MouseEvent event) {
        cargarReservas();
    }
}

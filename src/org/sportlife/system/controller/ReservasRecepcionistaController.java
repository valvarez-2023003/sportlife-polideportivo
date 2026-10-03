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
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.sportlife.system.model.Reserva;
import org.sportlife.system.service.ReservaService;
import org.sportlife.system.utils.AlertInformation;

/**
 * Controlador de la vista de todas las reservas del recepcionista.
 *
 * <p>Ciclo de vida de una reserva:</p>
 * <ul>
 *   <li><b>Pendiente</b> → el cliente solicitó la reserva, el recepcionista aún no la revisó.</li>
 *   <li><b>Confirmada</b> → el recepcionista aprobó la solicitud.</li>
 *   <li><b>Rechazada</b> → el recepcionista NO aprobó la solicitud.</li>
 *   <li><b>Cancelada</b> → una reserva previamente Confirmada fue anulada después.</li>
 * </ul>
 *
 * <p>Transiciones válidas:</p>
 * <pre>
 *   Pendiente  → Confirmada   (botón Confirmar)
 *   Pendiente  → Rechazada    (botón Rechazar)
 *   Confirmada → Cancelada    (botón Cancelar)
 *   Cualquiera → Cualquiera   (botón Actualizar, para corrección manual)
 * </pre>
 *
 * @author Cristofer Ramos
 */
public class ReservasRecepcionistaController implements Initializable {

    @FXML private ComboBox<String> cmbStatusFilter;
    @FXML private DatePicker datePicker;
    @FXML private TextField txtHoraInicio;
    @FXML private TextField txtHoraFin;
    @FXML private ComboBox<String> cmbEstado;

    @FXML private Button btnConfirm;
    @FXML private Button btnReject;
    @FXML private Button btnCancel;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnRefresh;

    @FXML private TableView<Reserva> tblReservations;
    @FXML private TableColumn<Reserva, Integer> colIdReserva;
    @FXML private TableColumn<Reserva, String> colClient;
    @FXML private TableColumn<Reserva, String> colCourt;
    @FXML private TableColumn<Reserva, String> colDiscipline;
    @FXML private TableColumn<Reserva, String> colDate;
    @FXML private TableColumn<Reserva, String> colTimeRange;
    @FXML private TableColumn<Reserva, Integer> colTotalHours;
    @FXML private TableColumn<Reserva, String> colCost;
    @FXML private TableColumn<Reserva, String> colStatus;

    private final ReservaService reservaService = new ReservaService();
    private final ObservableList<Reserva> reservas = FXCollections.observableArrayList();
    private FilteredList<Reserva> filtradas;
    private final DecimalFormat formatoPrecio = new DecimalFormat("'Q ' #,##0.00");
    private final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(">>> ReservasRecepcionistaController inicializado.");
        configurarColumnas();
        configurarFiltro();
        configurarComboEstado();
        configurarSeleccionFila();
        cargarReservas();
    }

    // ==========================================================
    //  CONFIGURACIÓN
    // ==========================================================

    private void configurarColumnas() {
        colIdReserva.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getIdReserva()));
        colClient.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCliente()));
        colCourt.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombreCancha()));
        colDiscipline.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTipoDeporte()));
        colDate.setCellValueFactory(d -> new SimpleStringProperty(
            d.getValue().getFechaReserva() == null ? "" : formatoFecha.format(d.getValue().getFechaReserva())));
        colTimeRange.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getHorarioTexto()));
        colTotalHours.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getTotalHoras()));
        colCost.setCellValueFactory(d -> new SimpleStringProperty(formatoPrecio.format(d.getValue().getCostoTotal())));
        colStatus.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getEstadoReserva()));
    }

    private void configurarFiltro() {
        cmbStatusFilter.getItems().addAll("TODAS", "Pendiente", "Confirmada", "Rechazada", "Cancelada");
        cmbStatusFilter.setValue("TODAS");

        filtradas = new FilteredList<>(reservas, r -> true);
        cmbStatusFilter.valueProperty().addListener((obs, o, n) -> {
            if (n == null || "TODAS".equals(n)) {
                filtradas.setPredicate(r -> true);
            } else {
                filtradas.setPredicate(r -> n.equalsIgnoreCase(r.getEstadoReserva()));
            }
        });
        tblReservations.setItems(filtradas);
    }

    private void configurarComboEstado() {
        cmbEstado.getItems().addAll("Pendiente", "Confirmada", "Rechazada", "Cancelada");
    }

    private void configurarSeleccionFila() {
        tblReservations.getSelectionModel().selectedItemProperty().addListener((obs, o, r) -> {
            if (r != null) {
                datePicker.setValue(r.getFechaReserva());
                txtHoraInicio.setText(r.getHoraInicio() == null ? "" : r.getHoraInicio().toString());
                txtHoraFin.setText(r.getHoraFin() == null ? "" : r.getHoraFin().toString());
                cmbEstado.setValue(r.getEstadoReserva());
            }
        });
    }

    private void cargarReservas() {
        try {
            List<Reserva> lista = reservaService.listarTodas();
            reservas.setAll(lista);
            System.out.println(">>> Reservas totales: " + lista.size());
        } catch (Exception e) {
            AlertInformation.viewAlert("ERROR", "Error de carga", "Error de BD", e.getMessage());
        }
    }

    // ==========================================================
    //  ACCIONES
    // ==========================================================

    /**
     * Confirma una reserva Pendiente.
     * Transición: Pendiente → Confirmada
     */
    @FXML
    public void onConfirm(MouseEvent event) {
        Reserva r = tblReservations.getSelectionModel().getSelectedItem();
        if (r == null) {
            AlertInformation.viewAlert("WARNING", "Sin selección", "Aviso",
                "Seleccione una reserva para confirmar.");
            return;
        }
        try {
            reservaService.confirmar(r.getIdReserva());
            AlertInformation.viewAlert("INFO", "Confirmada", "Éxito",
                "La reserva #" + r.getIdReserva() + " fue confirmada.");
            cargarReservas();
        } catch (Exception e) {
            AlertInformation.viewAlert("ERROR", "Error", "Error de BD", e.getMessage());
        }
    }

    /**
     * Rechaza una reserva Pendiente.
     * Transición: Pendiente → Rechazada
     */
    @FXML
    public void onReject(MouseEvent event) {
        Reserva r = tblReservations.getSelectionModel().getSelectedItem();
        if (r == null) {
            AlertInformation.viewAlert("WARNING", "Sin selección", "Aviso",
                "Seleccione una reserva para rechazar.");
            return;
        }
        try {
            reservaService.rechazar(r.getIdReserva());
            AlertInformation.viewAlert("INFO", "Rechazada", "Éxito",
                "La reserva #" + r.getIdReserva() + " fue rechazada.");
            cargarReservas();
        } catch (Exception e) {
            AlertInformation.viewAlert("ERROR", "Error", "Error de BD", e.getMessage());
        }
    }

    /**
     * Cancela una reserva previamente Confirmada.
     * Transición: Confirmada → Cancelada
     *
     * <p>Regla de negocio: solo se pueden cancelar reservas que ya fueron
     * confirmadas. Las reservas Pendientes deben rechazarse, no cancelarse.</p>
     */
    @FXML
    public void onCancel(MouseEvent event) {
        Reserva r = tblReservations.getSelectionModel().getSelectedItem();
        if (r == null) {
            AlertInformation.viewAlert("WARNING", "Sin selección", "Aviso",
                "Seleccione una reserva para cancelar.");
            return;
        }

        // Regla de negocio: solo se cancelan reservas Confirmadas
        if (!"Confirmada".equalsIgnoreCase(r.getEstadoReserva())) {
            AlertInformation.viewAlert("WARNING", "Estado inválido", "Aviso",
                "Solo se pueden cancelar reservas que estén en estado 'Confirmada'.\n" +
                "Para reservas Pendientes use 'Rechazar'.");
            return;
        }

        try {
            reservaService.cancelar(r.getIdReserva());
            AlertInformation.viewAlert("INFO", "Cancelada", "Éxito",
                "La reserva #" + r.getIdReserva() + " fue cancelada.");
            cargarReservas();
        } catch (Exception e) {
            AlertInformation.viewAlert("ERROR", "Error", "Error de BD", e.getMessage());
        }
    }

    /**
     * Actualiza una reserva con los valores del formulario.
     * Permite corregir fecha, hora o estado manualmente.
     */
    @FXML
    public void onUpdate(MouseEvent event) {
        Reserva r = tblReservations.getSelectionModel().getSelectedItem();
        if (r == null) {
            AlertInformation.viewAlert("WARNING", "Sin selección", "Aviso",
                "Seleccione una reserva para actualizar.");
            return;
        }
        try {
            LocalDate fecha = datePicker.getValue();
            LocalTime hIni = LocalTime.parse(txtHoraInicio.getText());
            LocalTime hFin = LocalTime.parse(txtHoraFin.getText());
            String estado = cmbEstado.getValue();

            if (fecha == null || estado == null) {
                AlertInformation.viewAlert("ERROR", "Datos incompletos", "Error de validación",
                    "Debe completar fecha, horas y estado.");
                return;
            }

            reservaService.actualizar(r.getIdReserva(), fecha, hIni, hFin, estado);
            AlertInformation.viewAlert("INFO", "Actualizada", "Éxito",
                "La reserva #" + r.getIdReserva() + " fue actualizada.");
            cargarReservas();
        } catch (Exception e) {
            AlertInformation.viewAlert("ERROR", "Error", "Datos inválidos",
                "Verifique el formato de las horas (HH:MM). Detalle: " + e.getMessage());
        }
    }

    /**
     * Elimina permanentemente una reserva de la base de datos.
     */
    @FXML
    public void onDelete(MouseEvent event) {
        Reserva r = tblReservations.getSelectionModel().getSelectedItem();
        if (r == null) {
            AlertInformation.viewAlert("WARNING", "Sin selección", "Aviso",
                "Seleccione una reserva para eliminar.");
            return;
        }
        try {
            reservaService.eliminar(r.getIdReserva());
            AlertInformation.viewAlert("INFO", "Eliminada", "Éxito",
                "La reserva #" + r.getIdReserva() + " fue eliminada.");
            cargarReservas();
        } catch (Exception e) {
            AlertInformation.viewAlert("ERROR", "Error", "Error de BD", e.getMessage());
        }
    }

    /**
     * Recarga la lista de reservas desde la BD.
     */
    @FXML
    public void onRefresh(MouseEvent event) {
        cargarReservas();
    }
}
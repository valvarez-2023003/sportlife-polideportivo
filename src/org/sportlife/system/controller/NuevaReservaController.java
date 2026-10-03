package org.sportlife.system.controller;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import org.sportlife.system.model.Cancha;
import org.sportlife.system.model.Reserva;
import org.sportlife.system.service.CanchaService;
import org.sportlife.system.service.ReservaService;
import org.sportlife.system.utils.AlertInformation;
import org.sportlife.system.utils.SessionManager;

/**
 * Controlador del formulario de nueva reserva (cliente).
 *
 * @author Cristofer Ramos
 */
public class NuevaReservaController implements Initializable {

    @FXML private ComboBox<String> cmbSport;
    @FXML private ComboBox<Cancha> cmbCourt;
    @FXML private DatePicker dpReservationDate;
    @FXML private Spinner<LocalTime> spnStartTime;
    @FXML private Spinner<LocalTime> spnEndTime;
    @FXML private Label lblTotalHours;
    @FXML private Label lblTotalCost;

    private final CanchaService canchaService = new CanchaService();
    private final ReservaService reservaService = new ReservaService();
    private final DecimalFormat formatoPrecio = new DecimalFormat("'Q ' #,##0.00");
    private final DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

    private static final LocalTime HORA_MIN = LocalTime.of(7, 0);
    private static final LocalTime HORA_MAX = LocalTime.of(22, 0);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(">>> NuevaReservaController inicializado.");
        configurarSpinners();
        configurarComboCanchas();
        cargarDeportes();
        configurarListeners();

        // ✅ Cargar canchas al inicio (sin filtro de deporte)
        cargarCanchas(null);

        // ✅ Poner la fecha de mañana por defecto
        dpReservationDate.setValue(LocalDate.now().plusDays(1));

        // Calcular estado inicial
        recalcular();
    }

    // ==========================================================
    //  CONFIGURACIÓN
    // ==========================================================

    private void configurarSpinners() {
        spnStartTime.setValueFactory(crearFactoryHora(LocalTime.of(8, 0)));
        spnEndTime.setValueFactory(crearFactoryHora(LocalTime.of(10, 0)));
    }

    /**
     * Crea un SpinnerValueFactory para horas entre 07:00 y 22:00.
     */
    private SpinnerValueFactory<LocalTime> crearFactoryHora(LocalTime inicial) {
        SpinnerValueFactory<LocalTime> factory = new SpinnerValueFactory<>() {
            @Override
            public void decrement(int steps) {
                LocalTime nuevo = getValue().minusHours(steps);
                if (!nuevo.isBefore(HORA_MIN)) setValue(nuevo);
            }

            @Override
            public void increment(int steps) {
                LocalTime nuevo = getValue().plusHours(steps);
                if (!nuevo.isAfter(HORA_MAX)) setValue(nuevo);
            }
        };
        factory.setValue(inicial);
        factory.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalTime t) {
                return t == null ? "" : formatoHora.format(t);
            }

            @Override
            public LocalTime fromString(String s) {
                if (s == null || s.isBlank()) return null;
                try {
                    return LocalTime.parse(s);
                } catch (Exception e) {
                    return null;
                }
            }
        });
        return factory;
    }

private void configurarComboCanchas() {
    cmbCourt.setConverter(new StringConverter<>() {
        @Override
        public String toString(Cancha c) {
            if (c == null) return "";
            String tipoTecho = c.isTechada() ? "Techada" : "Aire libre";
            return c.getCodigo() + " - " + c.getNombre()
                    + "  [" + tipoTecho + "]"
                    + "  (Q " + c.getPrecioPorHora() + "/h)";
        }

        @Override
        public Cancha fromString(String s) {
            return null;
        }
    });
}

    private void cargarDeportes() {
    cmbSport.setItems(FXCollections.observableArrayList(
        "Fútbol", "Basquet", "Tenis", "Vóley", "Otro"
    ));
}

    private void configurarListeners() {
        // Cambio de deporte → filtrar canchas
        cmbSport.valueProperty().addListener((obs, o, n) -> {
            cargarCanchas(n);
            recalcular();
        });

        // Cambio de cancha → recalcular
        cmbCourt.valueProperty().addListener((obs, o, n) -> recalcular());

        // Cambio de fecha → recalcular (por si validamos fecha)
        dpReservationDate.valueProperty().addListener((obs, o, n) -> recalcular());

        // Cambio de hora inicio → ajustar hora fin y recalcular
        spnStartTime.valueProperty().addListener((obs, o, n) -> {
            if (n != null) {
                LocalTime fin = spnEndTime.getValue();
                // Si la hora fin es menor o igual a la inicio, ajustarla
                if (fin == null || !fin.isAfter(n)) {
                    LocalTime nuevoFin = n.plusHours(1);
                    if (!nuevoFin.isAfter(HORA_MAX)) {
                        spnEndTime.getValueFactory().setValue(nuevoFin);
                    }
                }
            }
            recalcular();
        });

        // Cambio de hora fin → recalcular
        spnEndTime.valueProperty().addListener((obs, o, n) -> recalcular());
    }

    // ==========================================================
    //  CARGA DE CANCHAS
    // ==========================================================

private void cargarCanchas(String deporte) {
    try {
        List<Cancha> canchas = canchaService.listarDisponibles();

        // Si es "Otro" o null → mostrar todas
        boolean mostrarTodas = (deporte == null)
                || deporte.isBlank()
                || "Otro".equalsIgnoreCase(deporte);

        if (!mostrarTodas) {
            canchas = canchas.stream()
                .filter(c -> c.getTipoDeporte() != null
                    && c.getTipoDeporte().equalsIgnoreCase(deporte))
                .toList();
        }

        cmbCourt.setItems(FXCollections.observableArrayList(canchas));
        cmbCourt.getSelectionModel().clearSelection();

        System.out.println(">>> Canchas cargadas en combo: " + canchas.size()
            + (mostrarTodas ? " (TODAS)" : " (filtro: " + deporte + ")"));

    } catch (Exception e) {
        System.err.println(">>> Error al cargar canchas: " + e.getMessage());
        AlertInformation.viewAlert("ERROR", "Error al cargar canchas",
            "Error de BD", e.getMessage());
    }
}

    // ==========================================================
    //  CÁLCULO AUTOMÁTICO
    // ==========================================================

    private void recalcular() {
        Cancha c = cmbCourt.getValue();
        LocalTime ini = spnStartTime.getValue();
        LocalTime fin = spnEndTime.getValue();

        // Si falta cancha, hora inicio o fin → resetear
        if (c == null || ini == null || fin == null || !fin.isAfter(ini)) {
            lblTotalHours.setText("0 horas");
            lblTotalCost.setText("Q 0.00");
            return;
        }

        // Calcular horas
        long horas = Duration.between(ini, fin).toHours();

        // Calcular costo
        double costo = c.getPrecioPorHora() * horas;

        // Actualizar etiquetas
        lblTotalHours.setText(horas + (horas == 1 ? " hora" : " horas"));
        lblTotalCost.setText(formatoPrecio.format(costo));

        System.out.println(">>> Recalcular: " + horas + "h x Q " + c.getPrecioPorHora()
            + " = Q " + costo);
    }

    // ==========================================================
    //  ACCIÓN PRINCIPAL
    // ==========================================================

    @FXML
    public void onRequestReservation(MouseEvent event) {
        String idUser = SessionManager.getInstancia().getIdUsuarioActual();
        if (idUser == null) {
            AlertInformation.viewAlert("ERROR", "Sesión inválida", "Error",
                "Vuelva a iniciar sesión.");
            return;
        }

        // Validaciones
        Cancha cancha = cmbCourt.getValue();
        LocalDate fecha = dpReservationDate.getValue();
        LocalTime ini = spnStartTime.getValue();
        LocalTime fin = spnEndTime.getValue();

        if (cancha == null) {
            AlertInformation.viewAlert("ERROR", "Falta cancha", "Validación",
                "Seleccione una cancha.");
            return;
        }
        if (fecha == null) {
            AlertInformation.viewAlert("ERROR", "Falta fecha", "Validación",
                "Seleccione una fecha.");
            return;
        }
        if (fecha.isBefore(LocalDate.now())) {
            AlertInformation.viewAlert("ERROR", "Fecha inválida", "Validación",
                "La fecha no puede ser en el pasado.");
            return;
        }
        if (ini == null || fin == null || !fin.isAfter(ini)) {
            AlertInformation.viewAlert("ERROR", "Horario inválido", "Validación",
                "La hora de fin debe ser mayor a la de inicio.");
            return;
        }

        try {
            Reserva r = new Reserva();
            r.setIdCancha(cancha.getIdCancha());
            r.setFechaReserva(fecha);
            r.setHoraInicio(ini);
            r.setHoraFin(fin);

            reservaService.crear(r, idUser);

            AlertInformation.viewAlert("INFO", "Solicitud enviada", "Éxito",
                "Tu solicitud fue enviada. Espera la confirmación del recepcionista.");
            limpiarFormulario();

        } catch (Exception e) {
            AlertInformation.viewAlert("ERROR", "Error al solicitar",
                "Error de BD", e.getMessage());
        }
    }

    private void limpiarFormulario() {
        cmbSport.getSelectionModel().clearSelection();
        cmbCourt.getSelectionModel().clearSelection();
        dpReservationDate.setValue(LocalDate.now().plusDays(1));
        spnStartTime.getValueFactory().setValue(LocalTime.of(8, 0));
        spnEndTime.getValueFactory().setValue(LocalTime.of(10, 0));
        lblTotalHours.setText("0 horas");
        lblTotalCost.setText("Q 0.00");
    }
}
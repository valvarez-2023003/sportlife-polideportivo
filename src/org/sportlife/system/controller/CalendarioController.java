package org.sportlife.system.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controlador de la vista de Calendario de Reservas (solo lectura para admin).
 *
 * @author Cristofer Ramos
 */
public class CalendarioController implements Initializable {

    @FXML private Label lblHeaderTitle;
    @FXML private Label lblUserIcon;
    @FXML private Label lblUserRole;

    @FXML private TableView<Reserva> tblReservations;
    @FXML private TableColumn<Reserva, LocalDate> colDate;
    @FXML private TableColumn<Reserva, String> colCourt;
    @FXML private TableColumn<Reserva, String> colDiscipline;
    @FXML private TableColumn<Reserva, Integer> colTotalHours;
    @FXML private TableColumn<Reserva, String> colCovered;

    private final ObservableList<Reserva> reservas = FXCollections.observableArrayList();
    private final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(">>> CalendarioController inicializado.");
        configurarColumnas();
        cargarDatosDePrueba();
        configurarOrdenamiento();
    }

    // ==========================================================
    //  CONFIGURACIÓN DE LA TABLA
    // ==========================================================

    private void configurarColumnas() {
        // Fecha formateada como dd/MM/yyyy
        colDate.setCellValueFactory(cellData ->
            new SimpleObjectProperty<>(cellData.getValue().getFecha())
        );
        colDate.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate fecha, boolean empty) {
                super.updateItem(fecha, empty);
                setText(empty || fecha == null ? null : formatoFecha.format(fecha));
            }
        });

        colCourt.setCellValueFactory(new PropertyValueFactory<>("cancha"));
        colDiscipline.setCellValueFactory(new PropertyValueFactory<>("disciplina"));
        colTotalHours.setCellValueFactory(new PropertyValueFactory<>("horasTotales"));
        colCovered.setCellValueFactory(new PropertyValueFactory<>("techo"));
    }

    private void configurarOrdenamiento() {
        SortedList<Reserva> sortedData = new SortedList<>(reservas);
        sortedData.comparatorProperty().bind(tblReservations.comparatorProperty());

        colDate.setSortType(TableColumn.SortType.ASCENDING);
        tblReservations.getSortOrder().add(colDate);

        tblReservations.setItems(sortedData);
    }

    // ==========================================================
    //  CARGA DE DATOS (por ahora de prueba)
    // ==========================================================

    private void cargarDatosDePrueba() {
        reservas.addAll(
            new Reserva(LocalDate.of(2026, 10, 15), "Cancha Fútbol 5 Norte", "Fútbol", 2, "Techada"),
            new Reserva(LocalDate.of(2026, 10, 12), "Cancha Basket Central",  "Basket", 1, "Techada"),
            new Reserva(LocalDate.of(2026, 10, 20), "Cancha Tenis 1",        "Tenis",  3, "Aire libre"),
            new Reserva(LocalDate.of(2026, 10, 13), "Cancha Fútbol 7 Sur",   "Fútbol", 2, "Aire libre"),
            new Reserva(LocalDate.of(2026, 10, 18), "Cancha Vóley Playa",    "Vóley",  2, "Aire libre")
        );
    }

    // ==========================================================
    //  CLASE INTERNA: Reserva
    // ==========================================================

    public static class Reserva {
        private final LocalDate fecha;
        private final String cancha;
        private final String disciplina;
        private final int horasTotales;
        private final String techo;

        public Reserva(LocalDate fecha, String cancha, String disciplina, int horasTotales, String techo) {
            this.fecha = fecha;
            this.cancha = cancha;
            this.disciplina = disciplina;
            this.horasTotales = horasTotales;
            this.techo = techo;
        }

        public LocalDate getFecha() { return fecha; }
        public String getCancha() { return cancha; }
        public String getDisciplina() { return disciplina; }
        public int getHorasTotales() { return horasTotales; }
        public String getTecho() { return techo; }
    }
}
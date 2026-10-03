package org.sportlife.system.controller;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.sportlife.system.model.Cancha;
import org.sportlife.system.service.CanchaService;
import org.sportlife.system.utils.AlertInformation;
import org.sportlife.system.utils.NumericTextFormatter;
import org.sportlife.system.utils.SessionManager;

/**
 * Controlador de la vista de Gestión de Canchas.
 *
 * @author Cristofer Ramos
 */
public class GestionCanchasController implements Initializable {

    // Encabezado
    @FXML private Label lblHeaderTitle;
    @FXML private Label lblUserIcon;
    @FXML private Label lblUserRole;

    // Formulario
    @FXML private TextField txtCode;
    @FXML private ComboBox<String> cmbDiscipline;
    @FXML private ComboBox<String> cmbCovered;
    @FXML private ComboBox<String> cmbStatus;
    @FXML private TextField txtPricePerHour;
    @FXML private TextArea txtDescription;

    // Acciones
    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClear;

    // Tabla
    @FXML private TableView<Cancha> tblCourts;
    @FXML private TableColumn<Cancha, Integer> colCode;
    @FXML private TableColumn<Cancha, String> colDescription;
    @FXML private TableColumn<Cancha, String> colDiscipline;
    @FXML private TableColumn<Cancha, String> colCovered;
    @FXML private TableColumn<Cancha, String> colStatus;
    @FXML private TableColumn<Cancha, String> colPricePerHour;

    private final CanchaService canchaService = new CanchaService();
    private final ObservableList<Cancha> canchas = FXCollections.observableArrayList();
    private final DecimalFormat formatoPrecio = new DecimalFormat("'Q ' #,##0.00");

    public GestionCanchasController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(">>> GestionCanchasController inicializado.");

        cmbDiscipline.getItems().addAll("Fútbol", "Basquet", "Tenis", "Vóley", "Otro");
        cmbCovered.getItems().addAll("Techada", "Aire libre");
        cmbStatus.getItems().addAll("Disponible", "En mantenimiento");

        txtPricePerHour.setTextFormatter(NumericTextFormatter.createDecimalFormatter(7, 2));

        configurarColumnas();
        cargarCanchasEnTabla();
        configurarSeleccionFila();
    }

    // ==========================================================
    //  CONFIGURACIÓN DE LA TABLA
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

    private void configurarSeleccionFila() {
        tblCourts.getSelectionModel().selectedItemProperty().addListener(
            (obs, anterior, seleccionada) -> {
                if (seleccionada != null) {
                    cargarFormularioDesdeCancha(seleccionada);
                }
            }
        );
    }

    private void cargarCanchasEnTabla() {
        try {
            List<Cancha> lista = canchaService.listarTodas();
            canchas.setAll(lista);
            tblCourts.setItems(canchas);
            System.out.println(">>> Canchas cargadas: " + lista.size());
        } catch (Exception e) {
            System.err.println(">>> Error al cargar canchas: " + e.getMessage());
            AlertInformation.viewAlert("ERROR", "Error de carga",
                "Error de Base de Datos", e.getMessage());
        }
    }

    // ==========================================================
    //  ACCIONES
    // ==========================================================

    @FXML
    public void onAdd(MouseEvent event) {
        String idAdmin = obtenerIdAdmin();
        if (idAdmin == null) return;

        if (!validarFormulario()) return;

        try {
            Cancha cancha = construirCanchaDesdeFormulario();
            canchaService.crear(cancha, idAdmin);

            AlertInformation.viewAlert("INFO", "Cancha creada",
                "Éxito", "La cancha se registró correctamente.");

            onClear(event);
            cargarCanchasEnTabla();

        } catch (Exception e) {
            AlertInformation.viewAlert("ERROR", "Error al crear",
                "Error de Base de Datos", e.getMessage());
        }
    }

    @FXML
    public void onUpdate(MouseEvent event) {
        String idAdmin = obtenerIdAdmin();
        if (idAdmin == null) return;

        Cancha seleccionada = tblCourts.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            AlertInformation.viewAlert("WARNING", "Sin selección",
                "Aviso", "Seleccione una cancha de la tabla para editar.");
            return;
        }

        if (!validarFormulario()) return;

        try {
            Cancha cancha = construirCanchaDesdeFormulario();
            cancha.setIdCancha(seleccionada.getIdCancha());
            canchaService.actualizar(cancha, idAdmin);

            AlertInformation.viewAlert("INFO", "Cancha actualizada",
                "Éxito", "La cancha se actualizó correctamente.");

            onClear(event);
            cargarCanchasEnTabla();

        } catch (Exception e) {
            AlertInformation.viewAlert("ERROR", "Error al actualizar",
                "Error de Base de Datos", e.getMessage());
        }
    }

    @FXML
    public void onDelete(MouseEvent event) {
        String idAdmin = obtenerIdAdmin();
        if (idAdmin == null) return;

        Cancha seleccionada = tblCourts.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            AlertInformation.viewAlert("WARNING", "Sin selección",
                "Aviso", "Seleccione una cancha de la tabla para eliminar.");
            return;
        }

        try {
            canchaService.eliminar(seleccionada.getIdCancha(), idAdmin);

            AlertInformation.viewAlert("INFO", "Cancha eliminada",
                "Éxito", "La cancha se eliminó correctamente.");

            onClear(event);
            cargarCanchasEnTabla();

        } catch (Exception e) {
            AlertInformation.viewAlert("ERROR", "Error al eliminar",
                "Error de Base de Datos", e.getMessage());
        }
    }

    @FXML
    public void onClear(MouseEvent event) {
        txtCode.clear();
        txtPricePerHour.clear();
        txtDescription.clear();
        cmbDiscipline.getSelectionModel().clearSelection();
        cmbCovered.getSelectionModel().clearSelection();
        cmbStatus.getSelectionModel().clearSelection();
        tblCourts.getSelectionModel().clearSelection();
    }

    // ==========================================================
    //  UTILIDADES
    // ==========================================================

    private Cancha construirCanchaDesdeFormulario() {
        Cancha c = new Cancha();
        c.setCodigo(txtCode.getText().trim());
        c.setNombre(txtDescription.getText().trim());
        c.setTipoDeporte(cmbDiscipline.getValue());
        c.setTechada(cmbCovered.getValue().equals("Techada"));
        c.setPrecioPorHora(Double.parseDouble(txtPricePerHour.getText()));
        c.setEstado(cmbStatus.getValue());
        return c;
    }

    private void cargarFormularioDesdeCancha(Cancha c) {
        txtCode.setText(c.getCodigo());
        txtDescription.setText(c.getNombre());
        cmbDiscipline.setValue(c.getTipoDeporte());
        cmbCovered.setValue(c.isTechada() ? "Techada" : "Aire libre");
        cmbStatus.setValue(c.getEstado());
        txtPricePerHour.setText(String.valueOf(c.getPrecioPorHora()));
    }

    private String obtenerIdAdmin() {
        String idAdmin = SessionManager.getInstancia().getIdUsuarioActual();
        if (idAdmin == null) {
            AlertInformation.viewAlert("ERROR", "Sesión inválida",
                "Error de Autenticación",
                "No hay un usuario autenticado. Vuelva a iniciar sesión.");
        }
        return idAdmin;
    }

    private boolean validarFormulario() {
        if (txtCode.getText() == null || txtCode.getText().isBlank()) {
            AlertInformation.viewAlert("ERROR", "Campo vacío", "Error de Validación",
                "El campo Código es obligatorio.");
            txtCode.requestFocus();
            return false;
        }
        if (cmbDiscipline.getValue() == null) {
            AlertInformation.viewAlert("ERROR", "Campo vacío", "Error de Validación",
                "Debe seleccionar un deporte.");
            return false;
        }
        if (cmbCovered.getValue() == null) {
            AlertInformation.viewAlert("ERROR", "Campo vacío", "Error de Validación",
                "Debe seleccionar si es techada o aire libre.");
            return false;
        }
        if (cmbStatus.getValue() == null) {
            AlertInformation.viewAlert("ERROR", "Campo vacío", "Error de Validación",
                "Debe seleccionar un estado.");
            return false;
        }
        if (txtPricePerHour.getText() == null || txtPricePerHour.getText().isBlank()) {
            AlertInformation.viewAlert("ERROR", "Campo vacío", "Error de Validación",
                "El campo Precio/hora es obligatorio.");
            txtPricePerHour.requestFocus();
            return false;
        }
        if (txtDescription.getText() == null || txtDescription.getText().isBlank()) {
            AlertInformation.viewAlert("ERROR", "Campo vacío", "Error de Validación",
                "El campo Descripción es obligatorio.");
            txtDescription.requestFocus();
            return false;
        }
        return true;
    }

    public String formatearPrecio(Double precio) {
        if (precio == null) return "";
        return formatoPrecio.format(precio);
    }
}
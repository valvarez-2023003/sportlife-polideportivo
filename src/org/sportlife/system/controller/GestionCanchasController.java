package org.sportlife.system.controller;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
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
import org.sportlife.system.utils.NumericTextFormatter;

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
    @FXML private TableView<?> tblCourts;
    @FXML private TableColumn<?, ?> colCode;
    @FXML private TableColumn<?, ?> colDescription;
    @FXML private TableColumn<?, ?> colDiscipline;
    @FXML private TableColumn<?, ?> colCovered;
    @FXML private TableColumn<?, ?> colStatus;
    @FXML private TableColumn<?, ?> colPricePerHour;

    // Formato de precio: "Q 1,500.00"
    private final DecimalFormat formatoPrecio = new DecimalFormat("'Q ' #,##0.00");

    public GestionCanchasController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(">>> GestionCanchasController inicializado.");

        // Llenar los ComboBox
        cmbDiscipline.getItems().addAll("Fútbol", "Basket", "Tenis", "Vóley", "Otro");
        cmbCovered.getItems().addAll("Techada", "Aire libre");
        cmbStatus.getItems().addAll("Disponible", "En mantenimiento", "Ocupada");

        // Validación numérica del precio/hora (hasta 7 enteros, 2 decimales)
        txtPricePerHour.setTextFormatter(NumericTextFormatter.createDecimalFormatter(7, 2));
    }

    // ==========================================================
    //  ACCIONES
    // ==========================================================

    @FXML
    public void onAdd(MouseEvent event) {
        System.out.println("Acción: AGREGAR cancha");
        System.out.println("  Código:      " + txtCode.getText());
        System.out.println("  Deporte:     " + cmbDiscipline.getValue());
        System.out.println("  Techo:       " + cmbCovered.getValue());
        System.out.println("  Estado:      " + cmbStatus.getValue());
        System.out.println("  Precio/hora: " + txtPricePerHour.getText());
        System.out.println("  Descripción: " + txtDescription.getText());
        // TODO: implementar inserción en BD
    }

    @FXML
    public void onUpdate(MouseEvent event) {
        System.out.println("Acción: EDITAR cancha");
        // TODO: implementar
    }

    @FXML
    public void onDelete(MouseEvent event) {
        System.out.println("Acción: ELIMINAR cancha");
        // TODO: implementar
    }

    @FXML
    public void onClear(MouseEvent event) {
        txtCode.clear();
        txtPricePerHour.clear();
        txtDescription.clear();
        cmbDiscipline.getSelectionModel().clearSelection();
        cmbCovered.getSelectionModel().clearSelection();
        cmbStatus.getSelectionModel().clearSelection();
        System.out.println("Formulario limpiado.");
    }

    // ==========================================================
    //  UTILIDAD: Formatear precio
    // ==========================================================

    /**
     * Formatea un Double a String con el formato "Q 1,500.00".
     * Ejemplo de uso cuando se cargue la tabla:
     *   colPricePerHour.setCellValueFactory(data ->
     *       new SimpleStringProperty(formatearPrecio(data.getValue().getPrecioPorHora())));
     */
    public String formatearPrecio(Double precio) {
        if (precio == null) return "";
        return formatoPrecio.format(precio);
    }
}
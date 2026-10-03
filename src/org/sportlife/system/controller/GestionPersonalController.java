package org.sportlife.system.controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.sportlife.system.model.User;
import org.sportlife.system.service.UserService;
import org.sportlife.system.utils.AlertInformation;
import org.sportlife.system.utils.SessionManager;
import org.sportlife.system.utils.Validations;

/**
 * Controlador de la vista de Gestión de Personal (Gerente).
 * Permite crear, editar y eliminar usuarios del personal administrativo.
 *
 * @author Cristofer Ramos
 */
public class GestionPersonalController implements Initializable {

    @FXML private TextField txtUserName;
    @FXML private TextField txtName;
    @FXML private TextField txtLastName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;
    @FXML private PasswordField pwdPassword;
    @FXML private PasswordField pwdConfirmPassword;
    @FXML private ComboBox<String> cmbRole;

    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClear;

    @FXML private TableView<User> tblStaff;
    @FXML private TableColumn<User, String> colUserName;
    @FXML private TableColumn<User, String> colName;
    @FXML private TableColumn<User, String> colLastName;
    @FXML private TableColumn<User, String> colEmail;
    @FXML private TableColumn<User, String> colRole;

    private final UserService userService = new UserService();
    private final ObservableList<User> staff = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(">>> GestionPersonalController inicializado.");
        cmbRole.getItems().addAll("Administrador", "Recepcionista");
        configurarColumnas();
        configurarSeleccionFila();
        cargarStaff();
    }

    private void configurarColumnas() {
        colUserName.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getUser()));
        colName.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getName()));
        colLastName.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getLastname()));
        colEmail.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getEmail()));
        colRole.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getRole()));
    }

    private void configurarSeleccionFila() {
        tblStaff.getSelectionModel().selectedItemProperty().addListener((obs, o, u) -> {
            if (u != null) cargarFormularioDesdeUsuario(u);
        });
    }

    private void cargarStaff() {
        try {
            List<User> lista = userService.listarStaff();
            staff.setAll(lista);
            tblStaff.setItems(staff);
            System.out.println(">>> Staff cargado: " + lista.size());
        } catch (Exception e) {
            AlertInformation.viewAlert("ERROR", "Error de carga", "Error de BD", e.getMessage());
        }
    }

    // ==========================================================
    //  ACCIONES
    // ==========================================================

    @FXML
    public void onAdd(MouseEvent event) {
        String idGerente = obtenerIdGerente();
        if (idGerente == null) return;
        if (!validarFormulario()) return;

        try {
            User u = construirUsuarioDesdeFormulario();
            userService.crearStaff(u, idGerente);
            AlertInformation.viewAlert("INFO", "Usuario creado", "Éxito",
                "El usuario " + u.getUser() + " fue registrado.");
            onClear(event);
            cargarStaff();
        } catch (Exception e) {
            AlertInformation.viewAlert("ERROR", "Error al crear", "Error de BD", e.getMessage());
        }
    }

    @FXML
    public void onUpdate(MouseEvent event) {
        String idGerente = obtenerIdGerente();
        if (idGerente == null) return;

        User seleccionado = tblStaff.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            AlertInformation.viewAlert("WARNING", "Sin selección", "Aviso",
                "Seleccione un usuario de la tabla para editar.");
            return;
        }
        if (!validarFormulario()) return;

        try {
            User u = construirUsuarioDesdeFormulario();
            u.setIdUser(seleccionado.getIdUser());
            userService.actualizarStaff(u, idGerente);
            AlertInformation.viewAlert("INFO", "Usuario actualizado", "Éxito",
                "El usuario " + u.getUser() + " fue actualizado.");
            onClear(event);
            cargarStaff();
        } catch (Exception e) {
            AlertInformation.viewAlert("ERROR", "Error al actualizar", "Error de BD", e.getMessage());
        }
    }

    @FXML
    public void onDelete(MouseEvent event) {
        String idGerente = obtenerIdGerente();
        if (idGerente == null) return;

        User seleccionado = tblStaff.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            AlertInformation.viewAlert("WARNING", "Sin selección", "Aviso",
                "Seleccione un usuario de la tabla para eliminar.");
            return;
        }

        try {
            userService.eliminarStaff(seleccionado.getIdUser(), idGerente);
            AlertInformation.viewAlert("INFO", "Usuario eliminado", "Éxito",
                "El usuario " + seleccionado.getUser() + " fue eliminado.");
            onClear(event);
            cargarStaff();
        } catch (Exception e) {
            AlertInformation.viewAlert("ERROR", "Error al eliminar", "Error de BD", e.getMessage());
        }
    }

    @FXML
    public void onClear(MouseEvent event) {
        txtUserName.clear();
        txtName.clear();
        txtLastName.clear();
        txtEmail.clear();
        txtPhone.clear();
        pwdPassword.clear();
        pwdConfirmPassword.clear();
        cmbRole.getSelectionModel().clearSelection();
        tblStaff.getSelectionModel().clearSelection();
    }

    // ==========================================================
    //  UTILIDADES
    // ==========================================================

    private User construirUsuarioDesdeFormulario() {
        User u = new User();
        u.setUser(txtUserName.getText().trim());
        u.setName(txtName.getText().trim());
        u.setLastname(txtLastName.getText().trim());
        u.setEmail(txtEmail.getText().trim());
        u.setPhone(txtPhone.getText().trim());
        u.setPassword(pwdPassword.getText());
        u.setRole(cmbRole.getValue());
        return u;
    }

    private void cargarFormularioDesdeUsuario(User u) {
        txtUserName.setText(u.getUser());
        txtName.setText(u.getName());
        txtLastName.setText(u.getLastname());
        txtEmail.setText(u.getEmail());
        // La contraseña no se carga por seguridad
        pwdPassword.clear();
        pwdConfirmPassword.clear();
        cmbRole.setValue(u.getRole());
    }

    private String obtenerIdGerente() {
        String id = SessionManager.getInstancia().getIdUsuarioActual();
        if (id == null) {
            AlertInformation.viewAlert("ERROR", "Sesión inválida",
                "Error", "No hay usuario autenticado.");
        }
        return id;
    }

    /**
     * Valida el formulario con las mismas reglas que el registro público.
     */
    private boolean validarFormulario() {
        String userName = txtUserName.getText() == null ? "" : txtUserName.getText().trim();
        String name = txtName.getText() == null ? "" : txtName.getText().trim();
        String lastName = txtLastName.getText() == null ? "" : txtLastName.getText().trim();
        String email = txtEmail.getText() == null ? "" : txtEmail.getText().trim();
        String phone = txtPhone.getText() == null ? "" : txtPhone.getText().trim();
        String password = pwdPassword.getText();
        String confirm = pwdConfirmPassword.getText();

        // Campos vacíos
        if (Validations.emptyText(userName) || Validations.emptyText(name)
                || Validations.emptyText(lastName) || Validations.emptyText(email)
                || Validations.emptyText(password) || Validations.emptyText(confirm)) {
            AlertInformation.viewAlert("ERROR", "Campos vacíos", "Validación",
                "Complete todos los campos.");
            return false;
        }

        // Longitud del usuario
        if (!Validations.validateLengthText(userName, 25)) {
            AlertInformation.viewAlert("ERROR", "Longitud inválida", "Validación",
                "El usuario no puede exceder 25 caracteres.");
            return false;
        }

        // Nombre
        if (!Validations.validateOnlyLetters(name) || !Validations.validateLengthText(name, 50)) {
            AlertInformation.viewAlert("ERROR", "Nombre inválido", "Validación",
                "El nombre debe tener solo letras y máximo 50 caracteres.");
            return false;
        }

        // Apellido
        if (!Validations.validateOnlyLetters(lastName) || !Validations.validateLengthText(lastName, 50)) {
            AlertInformation.viewAlert("ERROR", "Apellido inválido", "Validación",
                "El apellido debe tener solo letras y máximo 50 caracteres.");
            return false;
        }

        // Email
        if (!Validations.validateEmail(email)) {
            AlertInformation.viewAlert("ERROR", "Correo inválido", "Validación",
                "El correo no tiene un formato válido.");
            return false;
        }

        // Teléfono
        if (!Validations.validatePhone(phone)) {
            AlertInformation.viewAlert("ERROR", "Teléfono inválido", "Validación",
                "El teléfono debe tener 8 dígitos.");
            return false;
        }

        // Contraseñas
        if (!Validations.equalsText(password, confirm)) {
            AlertInformation.viewAlert("ERROR", "Contraseñas no coinciden", "Validación",
                "La contraseña y su confirmación deben coincidir.");
            pwdPassword.clear();
            pwdConfirmPassword.clear();
            return false;
        }

        // Rol
        if (cmbRole.getValue() == null) {
            AlertInformation.viewAlert("ERROR", "Rol vacío", "Validación",
                "Debe seleccionar un rol.");
            return false;
        }

        return true;
    }
}
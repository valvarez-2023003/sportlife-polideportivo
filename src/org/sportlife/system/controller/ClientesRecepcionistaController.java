package org.sportlife.system.controller;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.sportlife.system.config.ConexionDB;
import org.sportlife.system.utils.AlertInformation;

/**
 * Controlador de la vista de clientes (solo lectura).
 *
 * @author Cristofer Ramos
 */
public class ClientesRecepcionistaController implements Initializable {

    @FXML private TableView<ClienteRow> tblClients;
    @FXML private TableColumn<ClienteRow, String> colName;
    @FXML private TableColumn<ClienteRow, String> colLastname;
    @FXML private TableColumn<ClienteRow, String> colEmail;
    @FXML private TableColumn<ClienteRow, String> colPhone;
    @FXML private TableColumn<ClienteRow, String> colUser;

    private final ObservableList<ClienteRow> clientes = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colName.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().nombre));
        colLastname.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().apellido));
        colEmail.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().email));
        colPhone.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().telefono));
        colUser.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().usuario));
        cargarClientes();
    }

    private void cargarClientes() {
        try (Connection conn = ConexionDB.getInstanciaConexionDB().getConnection();
             CallableStatement cs = conn.prepareCall("{CALL sp_get_registered_customers()}");
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                clientes.add(new ClienteRow(
                    rs.getString("name"),
                    rs.getString("lastname"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("user")
                ));
            }
            tblClients.setItems(clientes);
            System.out.println(">>> Clientes cargados: " + clientes.size());
        } catch (Exception e) {
            AlertInformation.viewAlert("ERROR", "Error de carga", "Error de BD", e.getMessage());
        }
    }

    public static class ClienteRow {
        public String nombre, apellido, email, telefono, usuario;
        public ClienteRow(String n, String a, String e, String t, String u) {
            this.nombre = n; this.apellido = a; this.email = e; this.telefono = t; this.usuario = u;
        }
    }
}
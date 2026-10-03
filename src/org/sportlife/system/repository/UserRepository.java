package org.sportlife.system.repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.sportlife.system.config.ConexionDB;
import org.sportlife.system.model.User;

/**
 * Repositorio de acceso a datos para usuarios.
 * Llama a los procedimientos almacenados de MySQL.
 *
 * @author Cristofer Ramos
 */
public class UserRepository {

    private final ConexionDB conexionDB;

    public UserRepository() {
        this.conexionDB = ConexionDB.getInstanciaConexionDB();
    }

    /**
     * Verifica estrictamente si un usuario o correo existe (sensible a mayúsculas).
     */
    public User checkUserExistsStrict(String usernameOrEmail) {
        String sql = "{CALL sp_check_user_exists_strict(?)}";
        return executeQuery(sql, usernameOrEmail, null);
    }

    /**
     * Verifica las credenciales de un usuario (usuario/correo + contraseña).
     */
    public User verifyUserPassword(String usernameOrEmail, String password) {
        String sql = "{CALL sp_verify_user_password(?, ?)}";
        return executeQuery(sql, usernameOrEmail, password);
    }

    /**
     * Registra un nuevo cliente (rol 'User') en la base de datos.
     */
    public boolean registerCustomer(User user) {
        String sql = "{CALL sp_register_customer(?, ?, ?, ?, ?, ?)}";
        try (Connection conn = conexionDB.getConnection();
             CallableStatement callableStatement = conn.prepareCall(sql)) {

            callableStatement.setString(1, user.getName());
            callableStatement.setString(2, user.getLastname());
            callableStatement.setString(3, user.getEmail());
            callableStatement.setString(4, user.getUser());
            callableStatement.setString(5, user.getPassword());
            callableStatement.setString(6, user.getPhone());

            callableStatement.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al registrar cliente en UserRepository: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lista todo el personal administrativo (Gerente, Administrador, Recepcionista).
     */
    public List<User> listarStaff() throws SQLException {
        List<User> lista = new ArrayList<>();
        String sql = "{CALL sp_get_administrative_staff()}";

        try (Connection conn = conexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                User u = new User();
                u.setIdUser(rs.getString("id_user"));
                u.setName(rs.getString("name"));
                u.setLastname(rs.getString("lastname"));
                u.setEmail(rs.getString("email"));
                u.setUser(rs.getString("user"));
                u.setRole(rs.getString("role"));
                lista.add(u);
            }
        }
        return lista;
    }

    /**
     * Crea un usuario del personal (solo un Gerente puede hacerlo).
     */
    public boolean crearStaff(User user, String idGerente) throws SQLException {
        String sql = "{CALL sp_create_users(?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = conexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, user.getName());
            cs.setString(2, user.getLastname());
            cs.setString(3, user.getEmail());
            cs.setString(4, user.getUser());
            cs.setString(5, user.getPassword());
            cs.setString(6, user.getRole());
            cs.setString(7, idGerente);

            cs.execute();
            return true;
        }
    }

    /**
     * Actualiza un usuario existente (solo un Gerente puede hacerlo).
     */
    public boolean actualizarStaff(User user, String idGerente) throws SQLException {
        String sql = "{CALL sp_update_users(?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = conexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, user.getIdUser());
            cs.setString(2, user.getName());
            cs.setString(3, user.getLastname());
            cs.setString(4, user.getEmail());
            cs.setString(5, user.getUser());
            cs.setString(6, user.getPassword());
            cs.setString(7, user.getRole());
            cs.setString(8, idGerente);

            cs.execute();
            return true;
        }
    }

    /**
     * Elimina un usuario (solo un Gerente puede hacerlo).
     */
    public boolean eliminarStaff(String idUser, String idGerente) throws SQLException {
        String sql = "{CALL sp_delete_users(?, ?)}";

        try (Connection conn = conexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, idUser);
            cs.setString(2, idGerente);

            cs.execute();
            return true;
        }
    }

    /**
     * Método auxiliar que ejecuta una consulta de un solo usuario.
     * Se usa para checkUserExistsStrict y verifyUserPassword.
     */
    private User executeQuery(String sql, String param1, String param2) {
        try (Connection conn = conexionDB.getConnection();
             CallableStatement callableStatement = conn.prepareCall(sql)) {

            callableStatement.setString(1, param1);
            if (param2 != null) {
                callableStatement.setString(2, param2);
            }

            try (ResultSet resultSet = callableStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new User(
                        resultSet.getString("id_user"),
                        resultSet.getString("name"),
                        resultSet.getString("lastname"),
                        resultSet.getString("email"),
                        resultSet.getString("user"),
                        "", // La contraseña no se envía por seguridad
                        resultSet.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error de base de datos en UserRepository: " + e.getMessage());
        }
        return null;
    }
}
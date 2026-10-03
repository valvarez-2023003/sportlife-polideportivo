package org.sportlife.system.repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.sportlife.system.config.ConexionDB;
import org.sportlife.system.model.Cancha;

/**
 * Repositorio para el CRUD de canchas.
 * Llama a los procedimientos almacenados de MySQL.
 *
 * @author Cristofer Ramos
 */
public class CanchaRepository {

    private final ConexionDB conexionDB;

    public CanchaRepository() {
        this.conexionDB = ConexionDB.getInstanciaConexionDB();
    }

    /**
     * Inserta una nueva cancha en la BD.
     */
    public boolean insertar(Cancha cancha, String idAdmin) throws SQLException {
        String sql = "{CALL sp_create_cancha(?, ?, ?, ?, ?, ?)}";
        try (Connection conn = conexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, cancha.getCodigo());
            cs.setString(2, cancha.getNombre());
            cs.setString(3, cancha.getTipoDeporte());
            cs.setBoolean(4, cancha.isTechada());
            cs.setDouble(5, cancha.getPrecioPorHora());
            cs.setString(6, idAdmin);

            cs.execute();
            return true;
        }
    }

/**
 * Actualiza una cancha existente.
 */
public boolean actualizar(Cancha cancha, String idAdmin) throws SQLException {
    String sql = "{CALL sp_update_cancha(?, ?, ?, ?, ?, ?, ?, ?)}";
    try (Connection conn = conexionDB.getConnection();
         CallableStatement cs = conn.prepareCall(sql)) {

        cs.setInt(1, cancha.getIdCancha());
        cs.setString(2, cancha.getCodigo());
        cs.setString(3, cancha.getNombre());
        cs.setString(4, cancha.getTipoDeporte());
        cs.setBoolean(5, cancha.isTechada());
        cs.setDouble(6, cancha.getPrecioPorHora());
        cs.setString(7, cancha.getEstado());
        cs.setString(8, idAdmin);

        cs.execute();
        return true;
    }
}

    /**
     * Elimina una cancha por su id.
     */
    public boolean eliminar(int idCancha, String idAdmin) throws SQLException {
        String sql = "{CALL sp_delete_cancha(?, ?)}";
        try (Connection conn = conexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, idCancha);
            cs.setString(2, idAdmin);

            cs.execute();
            return true;
        }
    }

    /**
     * Lista todas las canchas.
     */
    public List<Cancha> listarTodas() throws SQLException {
        List<Cancha> canchas = new ArrayList<>();
        String sql = "{CALL sp_get_all_canchas()}";

        try (Connection conn = conexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                Cancha c = new Cancha(
                    rs.getInt("id_cancha"),
                    rs.getString("codigo"),
                    rs.getString("nombre"),
                    rs.getString("tipo_deporte"),
                    rs.getBoolean("techada"),
                    rs.getDouble("precio_por_hora"),
                    rs.getString("estado")
                );
                canchas.add(c);
            }
        }
        return canchas;
    }

    /**
     * Lista solo las canchas disponibles.
     */
    public List<Cancha> listarDisponibles() throws SQLException {
        List<Cancha> canchas = new ArrayList<>();
        String sql = "{CALL sp_get_available_canchas()}";

        try (Connection conn = conexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                Cancha c = new Cancha(
                    rs.getInt("id_cancha"),
                    rs.getString("codigo"),
                    rs.getString("nombre"),
                    rs.getString("tipo_deporte"),
                    rs.getBoolean("techada"),
                    rs.getDouble("precio_por_hora"),
                    rs.getString("estado")
                );
                canchas.add(c);
            }
        }
        return canchas;
    }
}
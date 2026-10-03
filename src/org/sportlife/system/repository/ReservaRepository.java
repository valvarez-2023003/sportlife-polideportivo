/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.sportlife.system.repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import org.sportlife.system.config.ConexionDB;
import org.sportlife.system.model.Reserva;

/**
 * Repositorio para el CRUD de reservas.
 *
 * @author Cristofer Ramos
 */
public class ReservaRepository {

    private final ConexionDB conexionDB;

    public ReservaRepository() {
        this.conexionDB = ConexionDB.getInstanciaConexionDB();
    }

    public List<Reserva> listarPendientes() throws SQLException {
        return ejecutarListado("{CALL sp_get_reservas_pendientes()}");
    }

    public List<Reserva> listarTodas() throws SQLException {
        return ejecutarListado("{CALL sp_get_all_reservas()}");
    }

    public void confirmar(int idReserva) throws SQLException {
        String sql = "{CALL sp_confirmar_reserva(?)}";
        try (Connection conn = conexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, idReserva);
            cs.execute();
        }
    }

public boolean rechazar(int idReserva) throws SQLException {
    String sql = "{CALL sp_rechazar_reserva(?)}";
    try (Connection conn = conexionDB.getConnection();
         CallableStatement cs = conn.prepareCall(sql)) {
        cs.setInt(1, idReserva);
        int filas = cs.executeUpdate();  // ← devuelve filas afectadas
        return filas > 0;                // ← true si actualizó
    }
}

public void cancelar(int idReserva) throws SQLException {
    String sql = "{CALL sp_cancelar_reserva(?)}";
    try (Connection conn = conexionDB.getConnection();
         CallableStatement cs = conn.prepareCall(sql)) {
        cs.setInt(1, idReserva);
        cs.execute();
    }
}

    public void actualizar(int idReserva, java.time.LocalDate fecha,
                           java.time.LocalTime hIni, java.time.LocalTime hFin,
                           String estado) throws SQLException {
        String sql = "{CALL sp_update_reserva(?, ?, ?, ?, ?)}";
        try (Connection conn = conexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, idReserva);
            cs.setDate(2, Date.valueOf(fecha));
            cs.setTime(3, Time.valueOf(hIni));
            cs.setTime(4, Time.valueOf(hFin));
            cs.setString(5, estado);
            cs.execute();
        }
    }

    public void eliminar(int idReserva) throws SQLException {
        String sql = "{CALL sp_delete_reserva(?)}";
        try (Connection conn = conexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, idReserva);
            cs.execute();
        }
    }

    private List<Reserva> ejecutarListado(String sql) throws SQLException {
        List<Reserva> lista = new ArrayList<>();
        try (Connection conn = conexionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                Reserva r = new Reserva();
                r.setIdReserva(rs.getInt("id_reserva"));
                r.setCodigoCancha(rs.getString("codigo_cancha"));
                r.setNombreCancha(rs.getString("nombre_cancha"));
                r.setTipoDeporte(rs.getString("tipo_deporte"));
                r.setTechada(rs.getBoolean("techada"));
                r.setCliente(rs.getString("cliente"));
                r.setEmailCliente(rs.getString("email_cliente"));
                r.setFechaReserva(rs.getDate("fecha_reserva").toLocalDate());
                r.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
                r.setHoraFin(rs.getTime("hora_fin").toLocalTime());
                r.setTotalHoras(rs.getInt("total_horas"));
                r.setCostoTotal(rs.getDouble("costo_total"));
                r.setEstadoReserva(rs.getString("estado_reserva"));
                lista.add(r);
            }
        }
        return lista;
    }
    
    /**
 * Lista las reservas de un usuario específico.
 */
public List<Reserva> listarPorUsuario(String idUser) throws SQLException {
    List<Reserva> lista = new ArrayList<>();
    String sql = "{CALL sp_get_reservas_por_usuario(?)}";
    
    try (Connection conn = conexionDB.getConnection();
         CallableStatement cs = conn.prepareCall(sql)) {
        
        cs.setString(1, idUser);
        
        try (ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                Reserva r = new Reserva();
                r.setIdReserva(rs.getInt("id_reserva"));
                r.setCodigoCancha(rs.getString("codigo_cancha"));
                r.setNombreCancha(rs.getString("nombre_cancha"));
                r.setTipoDeporte(rs.getString("tipo_deporte"));
                r.setTechada(rs.getBoolean("techada"));
                r.setCliente(rs.getString("cliente"));
                r.setEmailCliente(rs.getString("email_cliente"));
                r.setFechaReserva(rs.getDate("fecha_reserva").toLocalDate());
                r.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
                r.setHoraFin(rs.getTime("hora_fin").toLocalTime());
                r.setTotalHoras(rs.getInt("total_horas"));
                r.setCostoTotal(rs.getDouble("costo_total"));
                r.setEstadoReserva(rs.getString("estado_reserva"));
                lista.add(r);
            }
        }
    }
    return lista;
}

/**
 * Crea una nueva reserva (estado inicial: Pendiente).
 */
public void crear(Reserva reserva, String idUser) throws SQLException {
    String sql = "{CALL sp_create_reserva(?, ?, ?, ?, ?)}";
    
    try (Connection conn = conexionDB.getConnection();
         CallableStatement cs = conn.prepareCall(sql)) {
        
        cs.setInt(1, reserva.getIdCancha());
        cs.setString(2, idUser);
        cs.setDate(3, Date.valueOf(reserva.getFechaReserva()));
        cs.setTime(4, Time.valueOf(reserva.getHoraInicio()));
        cs.setTime(5, Time.valueOf(reserva.getHoraFin()));
        
        cs.execute();
    }
}
}
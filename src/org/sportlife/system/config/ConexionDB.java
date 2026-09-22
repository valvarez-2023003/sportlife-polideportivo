package org.sportlife.system.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static ConexionDB instanciaConexionDB;
    private Connection connection;

    private ConexionDB() {
        conectar();
    }

    private void conectar() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + Enviroment.LOCATION_SERVICE + "/" + Enviroment.DATA_BASE,
                    Enviroment.USER,
                    Enviroment.PASSWORD
            );
        } catch (ClassNotFoundException classNotFound) {
            System.out.println("Error de clase no encontrada: " + classNotFound.getMessage());
        } catch (SQLException sqlException) {
            System.out.println("Error de conexion sql: " + sqlException.getMessage());
            sqlException.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error padre: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static ConexionDB getInstanciaConexionDB() {
        if (instanciaConexionDB == null) {
            instanciaConexionDB = new ConexionDB();
        }
        return instanciaConexionDB;
    }

    public Connection getConnection() {
        try {
            // Si la conexión es nula o ya fue cerrada por un try-with-resources, la recreamos
            if (connection == null || connection.isClosed()) {
                conectar();
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar el estado de la conexión: " + e.getMessage());
        }
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}

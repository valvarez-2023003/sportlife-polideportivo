/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.sportlife.system.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author informatica
 */

public class ConexionDB {
    private static ConexionDB instanciaConexionDB;

    private ConexionDB(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException classNotFound){
            System.out.println("Error de clase no encontrada: " + classNotFound.getMessage());
        }
    }

    public Connection getConnection(){
        try{
            return DriverManager.getConnection(
                "jdbc:mysql://" + Enviroment.LOCATION_SERVICE + "/" + Enviroment.DATA_BASE,
                Enviroment.USER,
                Enviroment.PASSWORD
            );
        }catch (SQLException sqlException){
            System.err.println("Error de conexión SQL: " + sqlException.getMessage());
            return null;
        }
    }

    public static ConexionDB getInstanciaConexionDB(){
        if(instanciaConexionDB == null){
            instanciaConexionDB = new ConexionDB();
        }
        return instanciaConexionDB;
    }
}

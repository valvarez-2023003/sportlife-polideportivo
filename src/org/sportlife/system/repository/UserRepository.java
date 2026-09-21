/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.sportlife.system.repository;

import org.sportlife.system.config.ConexionDB;
import org.sportlife.system.model.User;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Cristofer Ramos
 */

public class UserRepository implements UserInterface {
    private final ConexionDB conexionDB;

    public UserRepository(){
        this.conexionDB = ConexionDB.getInstanciaConexionDB();
    }

    @Override
    public User checkUserExistsStrict(String usernameOrEmail){
        String sql = "{CALL sp_check_user_exists_strict(?)}";
        return executeQuery(sql, usernameOrEmail, null);
    }

    @Override
    public User verifyUserPassword(String usernameOrEmail, String password){
        String sql = "{CALL sp_verify_user_password(?, ?)}";
        return executeQuery(sql, usernameOrEmail, password);
    }

    // Método auxiliar para evitar repetir código de JDBC
    private User executeQuery(String sql, String param1, String param2){
        try(Connection conn = conexionDB.getConnection();
             CallableStatement callableStatement = conn.prepareCall(sql)){
            
            callableStatement.setString(1, param1);
            if(param2 != null){
                callableStatement.setString(2, param2);
            }
            
            try(ResultSet resultSet = callableStatement.executeQuery()){
                if(resultSet.next()){
                    return new User(
                        resultSet.getString("id_user"),
                        resultSet.getString("name"),
                        resultSet.getString("lastname"),
                        resultSet.getString("email"),
                        resultSet.getString("user"),
                        "", // No enviamos la contraseña por seguridad en el objeto final
                        resultSet.getString("role")
                    );
                }
            }
        }catch (SQLException e){
            System.err.println("Error de base de datos en UserRepository: " + e.getMessage());
        }
        return null;
    }
}

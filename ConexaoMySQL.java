// src/main/java/com/ecomap/db/ConexaoMySQL.java
package com.ecomap.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoMySQL {
    private static final String URL = "jdbc:mysql://localhost:3306/ecomap"; // ajuste o nome do banco
    private static final String USUARIO = "root"; // ajuste seu usuário
    private static final String SENHA = "gerador8"; // ajuste sua senha

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}

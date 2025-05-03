package com.ecomap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlantioService {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/ecomap";
    private static final String USER = "root";
    private static final String PASSWORD = "sua_senha";

    public void salvarPlantio(double latitude, double longitude) {
        String sql = "INSERT INTO plantios (latitude, longitude) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setDouble(1, latitude);
            stmt.setDouble(2, longitude);
            stmt.executeUpdate();

            System.out.println("Plantio salvo com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao salvar plantio: " + e.getMessage());
        }
    }
}

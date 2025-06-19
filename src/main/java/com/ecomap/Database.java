package com.ecomap;

import java.sql.*;
import java.util.*;
import java.net.*;
import java.io.*;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/ecomap";
    private static final String USER = "root";
    private static final String PASS = "gerador8";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean authenticate(String username, String password) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean register(String username, String password) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO users(username, password) VALUES (?, ?)")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static int getUserId(String username) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("SELECT id FROM users WHERE username=?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static boolean hasPlantedAt(double lat, double lon) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM plantings WHERE latitude=? AND longitude=?")) {
            stmt.setDouble(1, lat);
            stmt.setDouble(2, lon);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    // NOVO: método plant() com nome e caracteristicas da planta
    public static boolean plant(int userId, double lat, double lon, String plantName, String plantNotes) {
        if (hasPlantedAt(lat, lon)) return false;
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO plantings(user_id, latitude, longitude, plant_name, plant_notes) VALUES (?, ?, ?, ?, ?)")) {
            stmt.setInt(1, userId);
            stmt.setDouble(2, lat);
            stmt.setDouble(3, lon);
            stmt.setString(4, plantName);
            stmt.setString(5, plantNotes);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static List<String[]> getAllPlantings() {
        List<String[]> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT u.username, p.latitude, p.longitude, p.plant_name, p.plant_notes FROM plantings p JOIN users u ON p.user_id = u.id")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new String[]{
                        rs.getString("username"),
                        String.valueOf(rs.getDouble("latitude")),
                        String.valueOf(rs.getDouble("longitude")),
                        rs.getString("plant_name") != null ? rs.getString("plant_name") : "",
                        rs.getString("plant_notes") != null ? rs.getString("plant_notes") : ""
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<String> getRanking() {
        List<String> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT u.username, COUNT(*) as total FROM plantings p JOIN users u ON p.user_id = u.id GROUP BY u.username ORDER BY total DESC")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("username") + " - " + rs.getInt("total") + " planta(s)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean deletePlant(int userId, double lat, double lon) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(
                     "DELETE FROM plantings WHERE user_id=? AND latitude=? AND longitude=?")) {
            stmt.setInt(1, userId);
            stmt.setDouble(2, lat);
            stmt.setDouble(3, lon);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static String getEnderecoFromLatLon(double lat, double lon) {
        try {
            String urlStr = String.format(
                    "https://nominatim.openstreetmap.org/reverse?format=json&lat=%f&lon=%f",
                    lat, lon
            );
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0"); // necessário para Nominatim

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            String json = response.toString();
            int start = json.indexOf("\"display_name\":\"") + 17;
            int end = json.indexOf("\",", start);
            if (start > 16 && end > start) {
                return json.substring(start, end);
            }

        } catch (Exception e) {
            return "Endereço não encontrado";
        }
        return "Endereço não encontrado";
    }
}

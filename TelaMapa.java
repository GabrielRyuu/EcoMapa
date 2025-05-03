package com.ecomap.ui;

import com.ecomap.db.ConexaoMySQL;
import com.ecomap.service.Ranking;
import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.js.JsObject;
import com.teamdev.jxbrowser.js.JsAccessible;
import com.teamdev.jxbrowser.navigation.Navigation;
import com.teamdev.jxbrowser.navigation.event.FrameLoadFinished;
import com.teamdev.jxbrowser.view.swing.BrowserView;
import com.teamdev.jxbrowser.frame.Frame;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Paths;
import java.sql.*;
import java.util.List;

import static com.teamdev.jxbrowser.engine.RenderingMode.HARDWARE_ACCELERATED;
import java.text.DecimalFormat;

public class TelaMapa extends JFrame {

    private final Engine engine;
    private final Browser browser;
    private final String usuario;

    static {
        System.setProperty("jxbrowser.license.key",
                "OK6AEKNYF3DI7FPUR2SMWHF79D1R64HRJ4C3895YG1V0VOZACPOSC3CWUZDNRORTCNEW9QVNA4G1Y25GNW514TNOCJBWGWUBAGHH5IVJZT5J0N0ZE9769PSK1488JDCAC7T7NLQZQMVO949RN");
    }

    public TelaMapa(String usuario) {
        this.usuario = usuario;

        engine = Engine.newInstance(
                EngineOptions.newBuilder(HARDWARE_ACCELERATED)
                        .userDataDir(Paths.get("user-data"))
                        .build()
        );
        browser = engine.newBrowser();

        setTitle("EcoMap - Bem-vindo, " + usuario);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        initComponents();

        Navigation nav = browser.navigation();
        nav.on(FrameLoadFinished.class, event -> {
            Frame frame = event.frame();
            JsObject window = frame.executeJavaScript("window");
            window.putProperty("hostObject", new PlantioBridge(usuario));
            mostrarPlantiosNoMapa();
        });

        String url = getClass().getResource("/mapbox.html").toExternalForm();
        browser.navigation().loadUrl(url);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel sidePanel = new JPanel(new GridLayout(5, 1, 10, 10));
        JTextField txtSearch = new JTextField();
        JButton btnSearch = new JButton("Pesquisar");
        sidePanel.add(txtSearch);
        sidePanel.add(btnSearch);

        // Removido: btnAdd
        JButton btnRank = new JButton("Ver Ranking");
        JButton btnBusca = new JButton("Buscar Locais");
        sidePanel.add(btnRank);
        sidePanel.add(btnBusca);

        add(sidePanel, BorderLayout.WEST);

        BrowserView view = BrowserView.newInstance(browser);
        add(view, BorderLayout.CENTER);

        btnRank.addActionListener(e -> mostrarRanking());
        btnBusca.addActionListener(e -> buscarLocais());
        btnSearch.addActionListener(e -> {
            String txt = txtSearch.getText().trim();
            if (isLatitudeLongitude(txt)) {
                String[] p = txt.split(",");
                double lat = Double.parseDouble(p[0].trim());
                double lng = Double.parseDouble(p[1].trim());
                centralizarNoMapa(lat, lng);
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Digite coordenadas no formato: latitude,longitude",
                        "Busca inválida",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });
    }

    private void mostrarRanking() {
        Ranking r = new Ranking();
        List<Ranking.RankingUsuario> lista = r.obterRanking();
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nenhum plantio registrado.",
                    "Ranking",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder("Ranking de Plantios:\n\n");
        for (int i = 0; i < lista.size(); i++) {
            sb.append(i + 1).append(". ").append(lista.get(i)).append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "Ranking", JOptionPane.INFORMATION_MESSAGE);
    }

    private void buscarLocais() {
        String sql =
                "SELECT latitude, longitude, COUNT(*) AS total " +
                        "FROM plantio " +
                        "GROUP BY latitude, longitude " +
                        "ORDER BY total DESC";
        try (Connection conn = ConexaoMySQL.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            StringBuilder sb = new StringBuilder("Locais de Plantio:\n\n");
            while (rs.next()) {
                double lat = rs.getDouble("latitude");
                double lng = rs.getDouble("longitude");
                int total = rs.getInt("total");
                sb.append(String.format("Lat: %.6f, Lng: %.6f – %d plantios\n", lat, lng, total));
            }
            String msg = sb.length() > 16 ? sb.toString() : "Nenhum local encontrado.";
            JOptionPane.showMessageDialog(this, msg, "Buscar Locais", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erro ao buscar locais: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarPlantiosNoMapa() {
        String sql = "SELECT latitude, longitude FROM plantio";
        try (Connection conn = ConexaoMySQL.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            StringBuilder js = new StringBuilder();
            while (rs.next()) {
                double lat = rs.getDouble("latitude");
                double lng = rs.getDouble("longitude");
                js.append("new mapboxgl.Marker()")
                        .append(".setLngLat([").append(lng).append(",").append(lat).append("])")
                        .append(".addTo(map);");
            }
            if (js.length() > 0) {
                browser.mainFrame().ifPresent(frame -> frame.executeJavaScript(js.toString()));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private boolean isLatitudeLongitude(String s) {
        String[] p = s.split(",");
        if (p.length != 2) return false;
        try {
            Double.parseDouble(p[0].trim());
            Double.parseDouble(p[1].trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void centralizarNoMapa(double lat, double lng) {
        String js = "map.flyTo({center:[" + lng + "," + lat + "],zoom:15});";
        browser.mainFrame().ifPresent(frame -> frame.executeJavaScript(js));
    }

    @Override
    public void dispose() {
        super.dispose();
        browser.close();
        engine.close();
    }

    /** Ponte Java ↔ JavaScript para adicionar/remover plantio */
    @JsAccessible
    public static class PlantioBridge {
        private final String usuario;
        private static final DecimalFormat FORMAT = new DecimalFormat("#.######");

        public PlantioBridge(String usuario) {
            this.usuario = usuario;
        }

        @JsAccessible
        public void savePlantio(double lat, double lng) {
            SwingUtilities.invokeLater(() -> {
                String sql =
                        "INSERT INTO plantio (usuario_id, latitude, longitude) " +
                                "SELECT id, ?, ? FROM usuario WHERE email = ?";
                try (Connection conn = ConexaoMySQL.conectar();
                     PreparedStatement ps = conn.prepareStatement(sql)) {

                    ps.setDouble(1, lat);
                    ps.setDouble(2, lng);
                    ps.setString(3, usuario);
                    ps.executeUpdate();

                    JOptionPane.showMessageDialog(null,
                            "Plantio adicionado!\nLat: " + lat + "  Lng: " + lng);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "Erro ao salvar: " + ex.getMessage(),
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            });
        }

        @JsAccessible
        public void removePlantio(double lat, double lng) {
            SwingUtilities.invokeLater(() -> {
                int confirm = JOptionPane.showConfirmDialog(null,
                        String.format("Deseja remover o plantio em:\nLat: %.6f, Lng: %.6f?", lat, lng),
                        "Remover Plantio",
                        JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return;

                String sql = "DELETE FROM plantio WHERE latitude = ? AND longitude = ? AND usuario_id = (SELECT id FROM usuario WHERE email = ?)";
                try (Connection conn = ConexaoMySQL.conectar();
                     PreparedStatement ps = conn.prepareStatement(sql)) {

                    ps.setDouble(1, lat);
                    ps.setDouble(2, lng);
                    ps.setString(3, usuario);
                    int deleted = ps.executeUpdate();

                    if (deleted > 0) {
                        JOptionPane.showMessageDialog(null, "Plantio removido com sucesso.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Nenhum plantio encontrado para remover.");
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "Erro ao remover: " + ex.getMessage(),
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            });
        }
    }
}

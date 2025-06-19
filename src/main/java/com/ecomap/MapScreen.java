package com.ecomap;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.*;
import org.jxmapviewer.input.*;
import org.jxmapviewer.viewer.WaypointPainter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class MapScreen extends JFrame {
    private final String username;
    private final int userId;
    private final JXMapViewer mapViewer = new JXMapViewer();
    private final JPanel sidePanel = new JPanel();
    private final JTextField searchField = new JTextField(12);
    private final JRadioButton allRadio = new JRadioButton("Todas", true);
    private final JRadioButton mineRadio = new JRadioButton("Minhas");
    private final JLabel statusLabel = new JLabel("🌱 Clique no mapa para plantar");

    private BufferedImage markerImage;

    public MapScreen(String username) {
        this.username = username;
        this.userId = Database.getUserId(username);

        // carregar imagem do marcador
        try {
            markerImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("/icons/plant2.png")));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar imagem do marcador.", "Erro", JOptionPane.ERROR_MESSAGE);
        }

        setTitle("EcoMap - Bem-vindo " + username);
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        setupMap();
        setupTopAndFilters();
        setupSidePanel();
        setupStatsPanel();

        add(mapViewer, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        updateMapAndList();
        setVisible(true);
    }

    private void setupMap() {
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        tileFactory.setThreadPoolSize(8);
        mapViewer.setTileFactory(tileFactory);
        mapViewer.setZoom(4);
        mapViewer.setAddressLocation(new GeoPosition(-23.5505, -46.6333));

        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));

        mapViewer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point pt = e.getPoint();
                GeoPosition pos = mapViewer.convertPointToGeoPosition(pt);
                double lat = pos.getLatitude();
                double lon = pos.getLongitude();

                // Primeiro, verifica se clicou em algum marcador
                boolean clickedOnMarker = false;
                List<String[]> plantings = Database.getAllPlantings();
                for (String[] p : plantings) {
                    double markerLat = Double.parseDouble(p[1]);
                    double markerLon = Double.parseDouble(p[2]);
                    String plantName = p[3];
                    String plantNotes = p[4];

                    GeoPosition markerPos = new GeoPosition(markerLat, markerLon);
                    Point2D markerPoint = mapViewer.getTileFactory().geoToPixel(markerPos, mapViewer.getZoom());

                    // Ajustar para posição visível na tela
                    Rectangle viewportBounds = mapViewer.getViewportBounds();
                    double x = markerPoint.getX() - viewportBounds.getX();
                    double y = markerPoint.getY() - viewportBounds.getY();
                    Point2D screenPoint = new Point2D.Double(x, y);

                    double distance = pt.distance(screenPoint);

                    if (distance < 20) { // tolerância de clique em pixels
                        // Clicou no marcador → mostra detalhes
                        JOptionPane.showMessageDialog(mapViewer,
                                "🌿 Planta: " + plantName + "\n" +
                                        "📝 Notas: " + plantNotes + "\n" +
                                        "📍 Localização: " + String.format("%.5f", markerLat) + ", " + String.format("%.5f", markerLon),
                                "Detalhes da Plantação",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        clickedOnMarker = true;
                        break;
                    }
                }

                // Se não clicou em nenhum marcador → segue com plantar
                if (!clickedOnMarker) {
                    JPanel panel = new JPanel(new GridLayout(2, 2));
                    JTextField plantNameField = new JTextField();
                    JTextField plantNotesField = new JTextField();

                    panel.add(new JLabel("Nome da Planta:"));
                    panel.add(plantNameField);
                    panel.add(new JLabel("Características / Notas:"));
                    panel.add(plantNotesField);

                    int result = JOptionPane.showConfirmDialog(null, panel, "Registrar Plantação", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        String plantName = plantNameField.getText().trim();
                        String plantNotes = plantNotesField.getText().trim();

                        if (Database.plant(userId, lat, lon, plantName, plantNotes)) {
                            statusLabel.setText("✅ Plantado: " + plantName + " em " + String.format("%.5f", lat) + ", " + String.format("%.5f", lon));
                            updateMapAndList();
                        } else {
                            statusLabel.setText("⚠️ Já existe plantação nesse local.");
                        }
                    }
                }
            }
        });
    }



    private void setupTopAndFilters() {
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.setBackground(new Color(245, 250, 255));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(245, 250, 255));

        JLabel userLabel = new JLabel("👤 " + username);
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        userLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JButton centerButton = createModernButton("📍 Centro", new Color(100, 149, 237));
        JButton reloadButton = createModernButton("🔄 Atualizar", new Color(100, 149, 237));
        JButton rankingButton = createModernButton("🏆 Ranking", new Color(60, 179, 113));
        JButton logoutButton = createModernButton("🚪 Logout", new Color(255, 99, 71));

        centerButton.addActionListener(e -> mapViewer.setAddressLocation(new GeoPosition(-23.5505, -46.6333)));
        reloadButton.addActionListener(e -> updateMapAndList());
        rankingButton.addActionListener(e -> new RankingScreen());
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginScreen();
        });

        topPanel.add(centerButton);
        topPanel.add(reloadButton);
        topPanel.add(rankingButton);
        topPanel.add(logoutButton);
        topPanel.add(userLabel);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(new Color(240, 240, 240));

        ButtonGroup group = new ButtonGroup();
        group.add(allRadio);
        group.add(mineRadio);

        allRadio.addActionListener(e -> updateMapAndList());
        mineRadio.addActionListener(e -> updateMapAndList());
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                updateMapAndList();
            }
        });

        filterPanel.add(allRadio);
        filterPanel.add(mineRadio);
        filterPanel.add(new JLabel("Usuário:"));
        filterPanel.add(searchField);

        topContainer.add(topPanel);
        topContainer.add(filterPanel);
        add(topContainer, BorderLayout.NORTH);
    }

    private JButton createModernButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void setupSidePanel() {
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setPreferredSize(new Dimension(320, getHeight()));
        sidePanel.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(sidePanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("🌿 Plantações"));
        add(scrollPane, BorderLayout.EAST);
    }

    private void setupStatsPanel() {
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statsPanel.setBackground(new Color(225, 245, 235));

        JLabel totalLabel = new JLabel();
        JLabel mineLabel = new JLabel();
        JLabel topLabel = new JLabel();

        statsPanel.add(totalLabel);
        statsPanel.add(Box.createHorizontalStrut(20));
        statsPanel.add(mineLabel);
        statsPanel.add(Box.createHorizontalStrut(20));
        statsPanel.add(topLabel);

        add(statsPanel, BorderLayout.AFTER_LAST_LINE);

        Runnable updateStats = () -> {
            List<String[]> plantings = Database.getAllPlantings();
            int total = plantings.size();
            int mine = (int) plantings.stream().filter(p -> p[0].equals(username)).count();
            String top = Database.getRanking().stream().findFirst().orElse("-");

            totalLabel.setText("🌱 Total: " + total);
            mineLabel.setText("👤 Suas: " + mine);
            topLabel.setText("🏆 Top: " + top.split(" - ")[0]);
        };

        updateStats.run();
        addWindowFocusListener(new WindowAdapter() {
            public void windowGainedFocus(WindowEvent e) {
                updateStats.run();
            }
        });
    }

    private void updateMapAndList() {
        sidePanel.removeAll();
        List<String[]> plantings = Database.getAllPlantings();

        Set<ImageWaypoint> waypoints = new HashSet<>();

        for (String[] p : plantings) {
            String user = p[0];
            double lat = Double.parseDouble(p[1]);
            double lon = Double.parseDouble(p[2]);
            String plantName = p[3];
            String plantNotes = p[4];

            if (mineRadio.isSelected() && !user.equals(username)) continue;
            if (!searchField.getText().isBlank() && !user.contains(searchField.getText())) continue;

            GeoPosition pos = new GeoPosition(lat, lon);
            waypoints.add(new ImageWaypoint(pos, markerImage));

            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(user.equals(username) ? new Color(235, 255, 235) : new Color(245, 245, 245));
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200)),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)
            ));

            JLabel info = new JLabel("<html><b>" + user + "</b><br/>" +
                    String.format("%.5f", lat) + ", " + String.format("%.5f", lon) + "<br/>" +
                    "<b>🌿 " + plantName + "</b><br/><i>" + plantNotes + "</i></html>");
            info.setFont(new Font("SansSerif", Font.PLAIN, 13));
            card.add(info, BorderLayout.CENTER);

            if (user.equals(username)) {
                JButton del = new JButton("Remover 🗑️");
                del.setFont(new Font("SansSerif", Font.BOLD, 12));
                del.setForeground(Color.WHITE);
                del.setBackground(new Color(220, 53, 69));
                del.setFocusPainted(false);
                del.setCursor(new Cursor(Cursor.HAND_CURSOR));
                del.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                del.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "Deseja remover esta plantação?", "Confirmação", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        if (Database.deletePlant(userId, lat, lon)) {
                            updateMapAndList();
                            statusLabel.setText("❌ Plantação removida.");
                        }
                    }
                });
                card.add(del, BorderLayout.EAST);
            }

            card.setAlignmentX(Component.LEFT_ALIGNMENT);
            sidePanel.add(card);
            sidePanel.add(Box.createVerticalStrut(8));
        }

        WaypointPainter<ImageWaypoint> painter = new WaypointPainter<>();
        painter.setWaypoints(waypoints);
        painter.setRenderer(new ImageWaypointRenderer());

        mapViewer.setOverlayPainter(painter);
        mapViewer.repaint();

        sidePanel.revalidate();
        sidePanel.repaint();
    }
}

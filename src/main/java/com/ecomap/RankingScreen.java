package com.ecomap;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RankingScreen extends JFrame {
    public RankingScreen() {
        setTitle("🏆 Ranking de Plantações");
        setSize(420, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel rankingPanel = new JPanel();
        rankingPanel.setLayout(new BoxLayout(rankingPanel, BoxLayout.Y_AXIS));
        rankingPanel.setBackground(new Color(250, 250, 255));
        rankingPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        List<String> ranking = Database.getRanking();
        String[] medalhas = {"🥇", "🥈", "🥉"};
        Font titleFont = new Font("SansSerif", Font.BOLD, 18);
        Font itemFont = new Font("SansSerif", Font.PLAIN, 16);

        for (int i = 0; i < ranking.size(); i++) {
            String line = ranking.get(i);
            String display = (i < 3 ? medalhas[i] : (i + 1) + "º") + "  " + line;

            JPanel card = new JPanel(new BorderLayout());
            card.setMaximumSize(new Dimension(360, 50));
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200)),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
            ));

            JLabel label = new JLabel(display);
            label.setFont(itemFont);
            card.add(label, BorderLayout.CENTER);

            rankingPanel.add(Box.createVerticalStrut(10));
            rankingPanel.add(card);
        }

        JScrollPane scrollPane = new JScrollPane(rankingPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("🌿 Ranking por número de plantações"));

        JLabel title = new JLabel("🏆 Ranking de Usuários");
        title.setFont(titleFont);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(title, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }
}

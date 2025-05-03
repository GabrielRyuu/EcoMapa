package com.ecomap;

import com.ecomap.ui.TelaLogin;

public class App {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new TelaLogin().setVisible(true);
        });
    }
}

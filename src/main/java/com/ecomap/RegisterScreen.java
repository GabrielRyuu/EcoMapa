package com.ecomap;

import javax.swing.*;
import java.awt.*;

public class RegisterScreen extends JFrame {
    public RegisterScreen() {
        setTitle("🌱 EcoMap - Registro");
        setSize(400, 280);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Painel principal com GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel title = new JLabel("Criar uma nova conta", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        // Campos
        JLabel userLabel = new JLabel("Usuário:");
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        panel.add(userLabel, gbc);

        JTextField userField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(userField, gbc);

        JLabel passLabel = new JLabel("Senha:");
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(passLabel, gbc);

        JPasswordField passField = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(passField, gbc);

        // Botão registrar
        JButton registerButton = new JButton("Registrar");
        registerButton.setBackground(new Color(60, 150, 60));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(registerButton, gbc);

        // Voltar para login
        JButton loginBackButton = new JButton("Já tem conta? Entrar");
        loginBackButton.setFocusPainted(false);
        loginBackButton.setContentAreaFilled(false);
        loginBackButton.setBorderPainted(false);
        loginBackButton.setForeground(new Color(0, 100, 200));
        gbc.gridy = 4;
        panel.add(loginBackButton, gbc);

        add(panel);

        // Ações
        registerButton.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());
            if (Database.register(user, pass)) {
                JOptionPane.showMessageDialog(this, "Usuário registrado com sucesso!");
                dispose();
                new LoginScreen();
            } else {
                JOptionPane.showMessageDialog(this, "Erro: nome de usuário já existente ou falha no cadastro.");
            }
        });

        loginBackButton.addActionListener(e -> {
            dispose();
            new LoginScreen();
        });

        setVisible(true);
    }
}

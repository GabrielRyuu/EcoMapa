package com.ecomap.ui;

import com.ecomap.service.AuthService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class TelaLogin extends JFrame {
    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JButton botaoEntrar;
    private JButton botaoRegistrar;

    public TelaLogin() {
        setTitle("EcoMap - Login");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Aplica um look moderno ao JFrame
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new MigLayout("wrap 1", "[center]", "[]20[][]10[][]20[]"));

        JLabel titulo = new JLabel("Bem-vindo ao EcoMap");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(new Color(34, 139, 34));
        add(titulo);

        campoEmail = new JTextField(20);
        campoEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campoEmail.setBorder(BorderFactory.createTitledBorder("Email"));
        add(campoEmail, "growx");

        campoSenha = new JPasswordField(20);
        campoSenha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campoSenha.setBorder(BorderFactory.createTitledBorder("Senha"));
        add(campoSenha, "growx");

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        botoes.setOpaque(false);

        botaoEntrar = new JButton("Entrar");
        botaoEntrar.setBackground(new Color(34, 139, 34));
        botaoEntrar.setForeground(Color.WHITE);
        botaoEntrar.setFocusPainted(false);

        botaoRegistrar = new JButton("Registrar");
        botaoRegistrar.setBackground(new Color(70, 130, 180));
        botaoRegistrar.setForeground(Color.WHITE);
        botaoRegistrar.setFocusPainted(false);

        botoes.add(botaoEntrar);
        botoes.add(botaoRegistrar);
        add(botoes);

        botaoEntrar.addActionListener(e -> fazerLogin());
        botaoRegistrar.addActionListener(e -> abrirTelaRegistro());
    }

    private void fazerLogin() {
        String email = campoEmail.getText().trim();
        String senha = new String(campoSenha.getPassword());

        if (AuthService.autenticar(email, senha)) {
            JOptionPane.showMessageDialog(this, "Login bem-sucedido!");
            dispose();
            new TelaMapa(email).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Email ou senha inválidos.");
        }
    }

    private void abrirTelaRegistro() {
        dispose();
        new TelaRegistro().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {
                // Se falhar, ignora e segue com o padrão
            }
            new TelaLogin().setVisible(true);
        });
    }
}

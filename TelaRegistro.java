package com.ecomap.ui;

import com.ecomap.service.AuthService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class TelaRegistro extends JFrame {
    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JPasswordField campoConfirmarSenha;
    private JButton botaoRegistrar;
    private JButton botaoVoltar;

    public TelaRegistro() {
        setTitle("EcoMap - Registro");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new MigLayout("wrap 1", "[center]", "[]20[][]10[][]10[][]20[]"));

        JLabel titulo = new JLabel("Crie sua conta");
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

        campoConfirmarSenha = new JPasswordField(20);
        campoConfirmarSenha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campoConfirmarSenha.setBorder(BorderFactory.createTitledBorder("Confirmar Senha"));
        add(campoConfirmarSenha, "growx");

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        botoes.setOpaque(false);

        botaoRegistrar = new JButton("Registrar");
        botaoRegistrar.setBackground(new Color(34, 139, 34));
        botaoRegistrar.setForeground(Color.WHITE);
        botaoRegistrar.setFocusPainted(false);

        botaoVoltar = new JButton("Voltar");
        botaoVoltar.setBackground(new Color(70, 130, 180));
        botaoVoltar.setForeground(Color.WHITE);
        botaoVoltar.setFocusPainted(false);

        botoes.add(botaoRegistrar);
        botoes.add(botaoVoltar);
        add(botoes);

        botaoRegistrar.addActionListener(e -> registrarUsuario());
        botaoVoltar.addActionListener(e -> voltarParaLogin());
    }

    private void registrarUsuario() {
        String email = campoEmail.getText().trim();
        String senha = new String(campoSenha.getPassword());
        String confirmar = new String(campoConfirmarSenha.getPassword());

        if (!senha.equals(confirmar)) {
            JOptionPane.showMessageDialog(this, "As senhas não coincidem.");
            return;
        }

        boolean sucesso = AuthService.registrar(email, senha);
        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Conta registrada com sucesso!");
            dispose();
            new TelaLogin().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao registrar. Tente outro email.");
        }
    }

    private void voltarParaLogin() {
        dispose();
        new TelaLogin().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {
                // Ignora se não conseguir aplicar o tema
            }
            new TelaRegistro().setVisible(true);
        });
    }
}

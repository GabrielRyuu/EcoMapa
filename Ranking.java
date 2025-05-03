package com.ecomap.service;

import com.ecomap.db.ConexaoMySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Ranking {

    public static class RankingUsuario {
        private final String nome;
        private final int totalPlantios;

        public RankingUsuario(String nome, int totalPlantios) {
            this.nome = nome;
            this.totalPlantios = totalPlantios;
        }

        public String getNome() {
            return nome;
        }

        public int getTotalPlantios() {
            return totalPlantios;
        }

        @Override
        public String toString() {
            return nome + " - " + totalPlantios + " plantios";
        }
    }

    /**
     * Busca o ranking de plantios:
     *   • usa a tabela `usuario` (no singular)
     *   • faz LEFT JOIN com `plantio`
     *   • agrupa por u.id e u.nome
     *   • ordena do maior para o menor total de plantios
     */
    public List<RankingUsuario> obterRanking() {
        List<RankingUsuario> rankingUsuarios = new ArrayList<>();

        String sql =
                "SELECT u.nome, COUNT(p.id) AS total_plantios " +
                        "FROM usuario u " +
                        "LEFT JOIN plantio p ON u.id = p.usuario_id " +
                        "GROUP BY u.id, u.nome " +
                        "ORDER BY total_plantios DESC";

        try (Connection conn = ConexaoMySQL.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String nome = rs.getString("nome");
                int total = rs.getInt("total_plantios");
                rankingUsuarios.add(new RankingUsuario(nome, total));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // aqui você pode logar ou rethrow se quiser
        }

        return rankingUsuarios;
    }
}

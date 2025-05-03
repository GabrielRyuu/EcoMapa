# 🌱 EcoMap - Desktop App

**EcoMap** é um aplicativo desktop interativo que ajuda usuários a encontrar locais com pouca vegetação e incentiva o plantio de árvores, promovendo a sustentabilidade urbana. O sistema inclui login, registro, mapa interativo com pontos de plantio, e um ranking dos usuários que mais plantaram!

---

## 🖥️ Tecnologias Utilizadas

- 🎯 **Java 17**
- 🧰 **Swing + MigLayout** – Interface gráfica moderna
- 🗺 **JxBrowser** – Mapa interativo integrado com HTML/JS
- 🛢 **MySQL** – Banco de dados relacional local
- 🔐 **Autenticação personalizada** – Com `AuthService`
- ☁️ **Mapbox** – Visualização de pontos de plantio

---

## ✨ Funcionalidades

✅ Cadastro e login de usuários  
✅ Adicionar pontos de plantio no mapa  
✅ Visualização interativa com mapa embutido  
✅ Ranking dos usuários que mais plantaram  
✅ Interface gráfica amigável e intuitiva  
✅ Remoção de plantações com confirmação  

---

## 🧪 Como Executar

1. Clone o projeto:
   ```bash
   git clone https://github.com/seu-usuario/ecomap.git
   cd ecomap
Configure o MySQL:

Crie o banco de dados ecomap

Execute o script de criação da tabela usuario e plantio (ver abaixo)

Configure seu ConexaoMySQL.java com:

java
Copiar
Editar
String url = "jdbc:mysql://localhost:3306/ecomap";
String usuario = "root";
String senha = "sua-senha";
Execute a classe TelaLogin para iniciar o sistema.

🗄️ Estrutura do Banco de Dados
sql
Copiar
Editar
CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL
);

CREATE TABLE plantio (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_email VARCHAR(100),
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    data_plantio TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
🎥 Imagens do Sistema
Em breve: prints da tela de login, mapa, ranking...

💡 Próximas Melhorias
📸 Upload de fotos dos plantios
🧑‍🤝‍🧑 Comunidade e comentários por plantio
🌐 Versão Web

👨‍💻 Desenvolvedor

Gabriel Cortes Teixeira

📧 gcortesteixeira@gmail.com

🔗 https://www.linkedin.com/in/gabriel-cortes-teixeira-0b9a4722b/










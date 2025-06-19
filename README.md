# 🌱 EcoMap Desktop

> Um sistema Java Desktop com mapa interativo e plantio virtual, focado em sustentabilidade e experiência do usuário.

O **EcoMap Desktop** é um projeto open source desenvolvido em Java que permite aos usuários simular plantações em um mapa real interativo. Cada plantio é representado por um marcador visual com informações detalhadas, e o sistema possui um painel lateral moderno com funcionalidades como filtros, exportação, login e ranking.

## 🚀 Funcionalidades

- 📍 Clique no mapa para plantar e visualizar o marcador.
- 🧭 Mapa interativo com tiles do OpenStreetMap (JXMapViewer).
- 👤 Login e controle de usuário.
- 📑 Filtros por usuário: veja todas as plantações ou apenas as suas.
- 📤 Exportação de plantações para CSV.
- 🗓️ Visualização da data de plantio.
- 🏆 Ranking com as maiores plantações por usuário.
- 🔓 Logout e controle de sessão.
- 🗺️ Geocodificação reversa: conversão de coordenadas para endereços reais.
- 🌐 Interface moderna e intuitiva.

## 🖼️ Preview

> ⚠️ Imagens e gifs da interface podem ser inseridos aqui futuramente.

## 🛠️ Tecnologias

- **Java Swing**
- **JXMapViewer** (com OpenStreetMap)
- **MySQL** (para persistência dos dados)
- **Java DAO Pattern**
- **CSV Export**
- **Design responsivo no Desktop**

## 📂 Estrutura do Projeto

EcoMap/
├── com.ecomap/
│ ├── MapScreen.java
│ ├── LoginScreen.java
│ ├── RankingScreen.java
│ ├── models/
│ ├── dao/
│ └── util/
└── resources/
├── marker.png
└── icons/

bash
Copiar
Editar

## 📦 Como Executar

1. Clone o repositório:
   ```bash
   git clone https://github.com/seuusuario/ecomap-desktop.git
   cd ecomap-desktop
Importe no IntelliJ ou Eclipse como projeto Maven (ou configure manualmente o classpath com JXMapViewer e MySQL Connector).

Configure o banco MySQL e atualize os parâmetros de conexão na classe Database.java.

Estrutura do banco: 
CREATE DATABASE ecomap;
USE ecomap;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE plantings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    latitude DOUBLE,
    longitude DOUBLE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);


Execute a classe LoginScreen.java ou MapScreen.java.

🧪 Requisitos
Java 11+

MySQL 8+

Biblioteca JXMapViewer

Driver JDBC do MySQL

IDE Java (recomendado: IntelliJ IDEA)

💡 Próximas Melhorias
📲 Versão Web com Spring Boot + React.

📱 Aplicativo mobile com Flutter.

📊 Dashboard de estatísticas em tempo real.

🔐 Integração com autenticação via OAuth2.

🤝 Contribuição
Contribuições são bem-vindas! Sinta-se livre para abrir issues ou pull requests.

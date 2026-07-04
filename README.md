# GET Training Academy – Sistema de Inscrições Web
## Aplicação Web 3 Camadas | Java 17 + Servlets + JSP + PostgreSQL

---

## Configuração no NetBeans 25

### 1. Abrir o Projecto
- File → Open Project
- Navegar até `C:\Users\Cauchy\Documents\Ucan_2026\SDP1\sdpfrequencia`
- Seleccionar (é um projecto Maven, reconhece automaticamente o `pom.xml`)

### 2. Configurar Java 17
- Tools → Java Platforms → Add Platform...
- Seleccionar `C:\Program Files\Java\jdk-17`
- Clicar em "Set as Default" (ou configurar por projecto: Project Properties → Sources → Source Level: 17)

### 3. Adicionar Tomcat 10 ao NetBeans
- Tools → Servers → Add Server...
- Seleccionar: **Apache Tomcat or TomEE**
- Installation folder: `C:\Users\Cauchy\Documents\Ucan_2026\SDP1\apache-tomcat-10.1.56`
- Username: `admin` | Password: (pode deixar em branco)
- Clicar Finish

### 4. Configurar o Servidor do Projecto
- Botão direito no projecto → Properties
- Run → Server: seleccionar **Apache Tomcat 10.1.56**
- Context Path: `/sdpfrequencia`

### 5. Executar
- Botão direito → Run (ou F6)
- Abrir browser: `http://localhost:8080/sdpfrequencia/`

---

## Base de Dados

- **Sistema**: PostgreSQL 18
- **Base de dados**: `sdpfrequencia`
- **User**: `postgres`
- **Senha**: `atac`
- **Porta**: 5432

As tabelas já foram criadas com o script `bd_schema.sql`.

---

## Estrutura das URLs

| URL | Descrição |
|-----|-----------|
| `http://localhost:8080/sdpfrequencia/` | Página inicial (redireciona para formulário) |
| `http://localhost:8080/sdpfrequencia/inscricao` | Formulário de inscrição |
| `http://localhost:8080/sdpfrequencia/listagem` | Lista de inscrições |
| `http://localhost:8080/sdpfrequencia/listagem?id=1` | Detalhe da inscrição #1 |

---

## Arquitectura 3 Camadas

```
VISÃO (JSP)         CONTROLADOR (Servlet)     MODELO (Bean + DAO)
formulario.jsp  →   InscricaoServlet      →   Formando, Curso, etc.
listagem.jsp    →   ListagemServlet       →   InscricaoDAO
detalhe.jsp     →       ↑                →   FormandoDAO, CursoDAO
sucesso.jsp     →       ↑                →   PostgreSQL 18
erro.jsp        ←   (erro handling)      ←
```

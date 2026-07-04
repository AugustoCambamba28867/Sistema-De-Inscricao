<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard – GET Training</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .dashboard-stats {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            margin-bottom: 20px;
        }
        .stat-card {
            background-color: #f8f9fa;
            border: 1px solid #ddd;
            border-radius: 8px;
            padding: 20px;
            text-align: center;
        }
        .stat-card h3 {
            margin: 0;
            color: #666;
            font-size: 14px;
        }
        .stat-card .value {
            font-size: 36px;
            font-weight: bold;
            color: #e73c3e;
            margin-top: 10px;
        }
    </style>
</head>
<body>

<header class="cabecalho">
    <div class="logo-bloco">
        <div class="logo-icone">▮</div>
        <div class="logo-texto">
            <div class="marca">GET <span>Training</span></div>
            <div class="subtitulo">Painel Administrativo</div>
        </div>
    </div>
    <div class="cabecalho-contatos">
        <div>Olá, ${sessionScope.admin.username}</div>
        <a href="${pageContext.request.contextPath}/logout" style="color: white;">Sair</a>
    </div>
</header>

<nav class="nav-bar">
    <a href="${pageContext.request.contextPath}/dashboard" class="activo">📊 Dashboard</a>
    <a href="${pageContext.request.contextPath}/listagem">📋 Gerir Inscrições</a>
    <c:if test="${sessionScope.admin.papel == 'SUPER_ADMIN'}">
        <a href="${pageContext.request.contextPath}/utilizadores">👥 Utilizadores</a>
    </c:if>
</nav>

<main class="conteudo">
    <section class="card">
        <span class="secao-titulo">Visão Geral</span>
        
        <div class="dashboard-stats">
            <div class="stat-card">
                <h3>Total de Inscrições</h3>
                <div class="value">${totalInscricoes}</div>
            </div>
        </div>

        <span class="secao-titulo" style="margin-top: 20px;">Inscrições por Curso</span>
        <ul>
            <c:forEach var="entry" items="${inscricoesPorCurso}">
                <li><strong>${entry.key}</strong>: ${entry.value}</li>
            </c:forEach>
        </ul>
    </section>
</main>

</body>
</html>

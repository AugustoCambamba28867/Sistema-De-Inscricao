<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Inscrição registada com sucesso – GET Training Academy Center.">
    <title>Inscrição Registada – GET Training Academy</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<header class="cabecalho">
    <div class="logo-bloco">
        <div class="logo-icone">▮</div>
        <div class="logo-texto">
            <div class="marca">GET <span>Training</span></div>
            <div class="subtitulo">Academy Center</div>
        </div>
    </div>
    <div class="cabecalho-contatos">
        <div>geral@get-ao.com</div>
        <div>www.get-ao.com</div>
    </div>
</header>

<nav class="nav-bar">
    <a href="${pageContext.request.contextPath}/inscricao" id="nav-inscricao">📝 Nova Inscrição</a>
    <a href="${pageContext.request.contextPath}/listagem" id="nav-listagem">📋 Listagem</a>
</nav>

<main class="conteudo">
    <div class="card" style="text-align:center; padding: 48px 32px;">

        <div class="sucesso-icone">✓</div>

        <h1 class="sucesso-titulo">Inscrição Registada!</h1>
        <p class="sucesso-sub">
            A inscrição de <strong>${nomeFormando}</strong>
            no curso <strong>${nomeCurso}</strong> foi gravada com sucesso.
        </p>

        <div class="alerta alerta-info" style="display:inline-block; text-align:left;">
            🔖 Número de Inscrição: <strong>#${inscricaoId}</strong>
        </div>

        <br><br>

        <div class="btn-grupo" style="justify-content:center;">
            <a href="${pageContext.request.contextPath}/inscricao"
               class="btn btn-primario" id="btnNovaInscricao">
                📝 Nova Inscrição
            </a>
            <a href="${pageContext.request.contextPath}/listagem"
               class="btn btn-secundario" id="btnVerListagem">
                📋 Ver Listagem
            </a>
            <a href="${pageContext.request.contextPath}/listagem?id=${inscricaoId}"
               class="btn btn-secundario" id="btnVerDetalhe">
                🔍 Ver Detalhe
            </a>
        </div>
    </div>
</main>

<footer class="rodape">
    <p>&copy; 2026 GET Training Academy Center · geral@get-ao.com · www.get-ao.com</p>
</footer>

</body>
</html>

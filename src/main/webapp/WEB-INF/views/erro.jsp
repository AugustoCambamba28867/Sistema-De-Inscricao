<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Erro – GET Training Academy</title>
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
    <div class="card" style="text-align:center; padding:48px 32px;">

        <div style="font-size:72px; margin-bottom:16px;">⚠️</div>

        <h1 style="font-size:28px; font-weight:700; color:#c62828; margin-bottom:8px;">
            Ocorreu um Erro
        </h1>

        <c:choose>
            <c:when test="${not empty mensagemErro}">
                <div class="alerta alerta-erro" style="text-align:left; margin:24px 0;">
                    ${mensagemErro}
                </div>
            </c:when>
            <c:when test="${not empty pageContext.errorData}">
                <div class="alerta alerta-erro" style="text-align:left; margin:24px 0;">
                    Código: <strong>${pageContext.errorData.statusCode}</strong><br>
                    Página: ${pageContext.errorData.requestURI}
                </div>
            </c:when>
            <c:otherwise>
                <p style="color:#757575; margin:24px 0;">
                    Ocorreu um erro inesperado. Por favor tente novamente.
                </p>
            </c:otherwise>
        </c:choose>

        <div class="btn-grupo" style="justify-content:center;">
            <a href="${pageContext.request.contextPath}/inscricao"
               class="btn btn-primario" id="btnVoltarInicio">
                🏠 Ir para o formulário
            </a>
            <a href="${pageContext.request.contextPath}/listagem"
               class="btn btn-secundario" id="btnVoltarListagemErro">
                📋 Ver listagem
            </a>
        </div>
    </div>
</main>

<footer class="rodape">
    <p>&copy; 2026 GET Training Academy Center · geral@get-ao.com · www.get-ao.com</p>
</footer>

</body>
</html>

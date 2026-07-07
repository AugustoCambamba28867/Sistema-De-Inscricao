<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login – GET Training Academy</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="login-wrap">
    <div class="login-card">
        <div class="login-logo">
            <div class="logo-icone" style="background:linear-gradient(135deg,#E53935 0%,#B71C1C 100%);box-shadow:0 4px 12px rgba(229,57,53,.4);">▮</div>
            <div class="marca">GET <span>Training</span></div>
            <p>Painel de controlo administrativo</p>
        </div>

        <c:if test="${not empty erro}">
            <div class="alerta alerta-erro">⚠ ${erro}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="campo" style="margin-bottom:16px;">
                <label for="username">Utilizador</label>
                <input type="text" id="username" name="username" placeholder="admin" required autocomplete="username">
            </div>
            <div class="campo" style="margin-bottom:28px;">
                <label for="password">Palavra-passe</label>
                <input type="password" id="password" name="password" placeholder="••••••••" required autocomplete="current-password">
            </div>
            <button type="submit" class="btn btn-primario" style="width:100%;justify-content:center;padding:13px;">
                Entrar no sistema →
            </button>
        </form>

        <p style="text-align:center;margin-top:20px;font-size:12px;color:var(--cinzento);">
            © 2026 GET Training Academy Center
        </p>
    </div>
</div>

</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Alterar Senha – GET Training Academy</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="login-wrap">
    <div class="login-card">
        <div class="login-logo">
            <div class="logo-icone" style="background:linear-gradient(135deg,#3B82F6 0%,#1D4ED8 100%);box-shadow:0 4px 12px rgba(59,130,246,.35);">▮</div>
            <div class="marca">GET <span>Training</span></div>
            <p>Altere a sua senha para continuar.</p>
        </div>

        <c:if test="${not empty erro}">
            <div class="alerta alerta-erro">⚠ ${erro}</div>
        </c:if>
        <c:if test="${not empty sucesso}">
            <div class="alerta alerta-sucesso">✔ ${sucesso}</div>
        </c:if>
        <c:if test="${param.required == 'true'}">
            <div class="alerta alerta-aviso">É necessário alterar a senha antes de continuar.</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/change-password" method="post">
            <jsp:include page="/WEB-INF/views/includes/csrfToken.jspf" />
            <input type="hidden" name="action" value="update">
            <div class="campo" style="margin-bottom:16px;">
                <label for="newPassword">Nova senha</label>
                <input type="password" id="newPassword" name="newPassword" required autocomplete="new-password">
            </div>
            <div class="campo" style="margin-bottom:28px;">
                <label for="confirmPassword">Confirmar nova senha</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required autocomplete="new-password">
            </div>
            <button type="submit" class="btn btn-primario" style="width:100%;justify-content:center;padding:13px;">
                Atualizar senha →
            </button>
        </form>

        <form action="${pageContext.request.contextPath}/change-password" method="post" style="margin-top:14px; text-align:center;">
            <jsp:include page="/WEB-INF/views/includes/csrfToken.jspf" />
            <input type="hidden" name="action" value="continue">
            <button type="submit" class="btn btn-secundario" style="width:100%;justify-content:center;padding:13px;">
                Continuar com a mesma senha
            </button>
        </form>

        <p style="text-align:center;margin-top:20px;font-size:12px;color:var(--cinzento);">
            © 2026 GET Training Academy Center
        </p>
    </div>
</div>

</body>
</html>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login Administrativo – GET Training</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .login-container {
            max-width: 400px;
            margin: 100px auto;
        }
    </style>
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
</header>

<main class="conteudo login-container">
    <section class="card">
        <h2 style="text-align:center; margin-bottom: 20px;">Painel de Controlo</h2>
        
        <c:if test="${not empty erro}">
            <div class="alerta alerta-erro" role="alert">
                ${erro}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="campo">
                <label for="username">Utilizador</label>
                <input type="text" id="username" name="username" required>
            </div>
            
            <div class="campo">
                <label for="password">Palavra-passe</label>
                <input type="password" id="password" name="password" required>
            </div>
            
            <div class="btn-grupo">
                <button type="submit" class="btn btn-primario" style="width: 100%;">Entrar</button>
            </div>
        </form>
    </section>
</main>

</body>
</html>

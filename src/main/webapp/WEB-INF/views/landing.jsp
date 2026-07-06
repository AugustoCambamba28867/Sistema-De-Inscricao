<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GET Training Academy</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="landing-body">
    <div class="landing-shell">
        <header class="landing-header">
            <div class="logo-bloco">
                <div class="logo-icone">▮</div>
                <div class="logo-texto">
                    <div class="marca">GET <span>Training</span></div>
                    <div class="subtitulo">Academy Center</div>
                </div>
            </div>
            <a href="${pageContext.request.contextPath}/login" class="btn btn-primario">Entrar no painel</a>
        </header>

        <main class="hero">
            <section class="hero-copy">
                <div class="hero-badge">Gestão profissional de inscrições</div>
                <h1>Centralize inscrições, acompanhe dados e administre a sua formação com eficiência.</h1>
                <p>Uma interface moderna e segura para a equipa administrativa da GET Training, com acesso simples ao painel e sem cadastro público.</p>
                <div class="hero-actions">
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-primario">Fazer login</a>
                    <span class="hero-note">Acesso exclusivo para administradores</span>
                </div>
                <ul class="feature-list">
                    <li>📋 Gestão de inscrições</li>
                    <li>🛡️ Acesso seguro</li>
                    <li>📈 Painel de controlo</li>
                </ul>
            </section>

            <aside class="hero-card">
                <div class="hero-card-header">Painel administrativo</div>
                <div class="hero-metrics">
                    <div>
                        <strong>+100</strong>
                        <span>inscrições</span>
                    </div>
                    <div>
                        <strong>24/7</strong>
                        <span>acesso protegido</span>
                    </div>
                </div>
                <p>Após o login, o formulário de inscrição fica disponível no painel do administrador para registar novos candidatos.</p>
            </aside>
        </main>
    </div>
</body>
</html>

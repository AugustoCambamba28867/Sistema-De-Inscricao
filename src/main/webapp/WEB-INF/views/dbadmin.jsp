<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Administração da Base de Dados – GET Training</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .dbadmin-grid { display:grid; gap:20px; margin-bottom:24px; }
        .dbadmin-card { padding:24px; border-radius:18px; background:#fff; border:1px solid var(--borda); }
        .dbadmin-card h2 { margin-top:0; font-size:1.15rem; }
        .dbadmin-card p { margin:0.6rem 0; color:var(--cinzento-dk); }
        .audit-table { width:100%; border-collapse:collapse; }
        .audit-table th, .audit-table td { padding:12px 10px; border-bottom:1px solid #eee; text-align:left; }
        .audit-table th { background:#f8fafc; color:#334155; font-weight:700; }
        .audit-table tbody tr:hover { background:#f8fafc; }
    </style>
</head>
<body>
<header class="cabecalho">
    <div class="logo-bloco">
        <div class="logo-icone">▮</div>
        <div class="logo-texto">
            <div class="marca">GET <span>Training</span></div>
            <div class="subtitulo">Painel de Administrador</div>
        </div>
    </div>
    <div class="cabecalho-contatos">
        <div>Olá, <strong>${sessionScope.admin.username}</strong></div>
        <a href="${pageContext.request.contextPath}/logout">⎋ Sair</a>
    </div>
</header>

<nav class="nav-bar">
    <a href="${pageContext.request.contextPath}/dashboard">📊 Dashboard</a>
    <a href="${pageContext.request.contextPath}/listagem">📋 Inscrições</a>
    <a href="${pageContext.request.contextPath}/relatorios">📑 Relatórios</a>
    <a href="${pageContext.request.contextPath}/utilizadores">👥 Utilizadores</a>
    <a href="${pageContext.request.contextPath}/dbadmin" class="activo">🛠️ Base de Dados</a>
</nav>

<main class="conteudo">
    <div class="page-header">
        <h1>Administração da Base de Dados <span>· Backup e Reset</span></h1>
    </div>

    <c:if test="${param.reset == 'ok'}">
        <div class="alerta alerta-sucesso">✔ O reset foi executado com sucesso. Dados de aplicação limpos.</div>
    </c:if>

    <div class="dbadmin-grid">
        <section class="dbadmin-card">
            <h2>📦 Backup da Base de Dados</h2>
            <p>Faça download de um ficheiro SQL completo contendo os dados atuais do sistema.</p>
            <form method="post" action="${pageContext.request.contextPath}/dbadmin">
                <jsp:include page="/WEB-INF/views/includes/csrfToken.jspf" />
                <input type="hidden" name="action" value="backup">
                <button type="submit" class="btn btn-primario">Gerar Backup SQL</button>
            </form>
        </section>

        <section class="dbadmin-card">
            <h2>⚠️ Reset de Dados de Aplicação</h2>
            <p>Limpa todas as inscrições, formulários e cursos. Os administradores e o historial de auditoria são preservados.</p>
            <form method="post" action="${pageContext.request.contextPath}/dbadmin">
                <jsp:include page="/WEB-INF/views/includes/csrfToken.jspf" />
                <input type="hidden" name="action" value="reset">
                <label for="confirmacao" style="display:block; margin-bottom:8px; font-weight:600;">Digite <strong>RESETAR</strong> para confirmar:</label>
                <input id="confirmacao" name="confirmacao" type="text" required style="width:100%; padding:10px; border-radius:8px; border:1px solid #ccc; margin-bottom:16px;">
                <button type="submit" class="btn btn-perigo">Executar Reset</button>
            </form>
        </section>
    </div>

    <section class="dbadmin-card">
        <h2>📝 Auditoria de Ações Administrativas</h2>
        <p>Registo dos últimos movimentos feitos pelos administradores.</p>
        <div class="tabela-container" style="overflow-x:auto;">
            <table class="audit-table">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Data / Hora</th>
                        <th>Administrador</th>
                        <th>Ação</th>
                        <th>Detalhes</th>
                        <th>IP</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="log" items="${auditLogs}">
                        <tr>
                            <td>${log.id}</td>
                            <td>${log.createdAt}</td>
                            <td>${log.username}</td>
                            <td>${log.action}</td>
                            <td>${log.details}</td>
                            <td>${log.ipAddress}</td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty auditLogs}">
                        <tr><td colspan="6" style="text-align:center; color:var(--cinzento); padding:24px;">Sem registos de auditoria disponíveis.</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </section>
</main>
</body>
</html>

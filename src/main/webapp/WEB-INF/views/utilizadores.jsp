<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestão de Utilizadores – GET Training</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .modal-backdrop { position: fixed; inset: 0; background: rgba(15, 23, 42, 0.65); display: flex; align-items: center; justify-content: center; z-index: 2000; padding: 16px; }
        .modal-card { background: #fff; border-radius: 16px; width: min(100%, 420px); padding: 24px; box-shadow: 0 20px 45px rgba(0,0,0,0.25); }
        .modal-actions { display: flex; justify-content: flex-end; gap: 10px; margin-top: 18px; }
    </style>
</head>
<body>

<header class="cabecalho">
    <div class="logo-bloco">
        <div class="logo-icone">▮</div>
        <div class="logo-texto">
            <div class="marca">GET <span>Training</span></div>
            <div class="subtitulo">Painel administrativo</div>
        </div>
    </div>
    <div class="cabecalho-contatos">
        <div>Olá, <strong>${sessionScope.admin.username}</strong>
            <span class="badge-papel ${sessionScope.admin.papel}" style="margin-left:8px;">${sessionScope.admin.papel}</span>
        </div>
        <a href="${pageContext.request.contextPath}/logout">⎋ Sair</a>
    </div>
</header>

<nav class="nav-bar">
    <a href="${pageContext.request.contextPath}/dashboard">📊 Dashboard</a>
    <a href="${pageContext.request.contextPath}/listagem">📋 Gerir Inscrições</a>
    <a href="${pageContext.request.contextPath}/utilizadores" class="activo">👥 Utilizadores</a>
</nav>

<main class="conteudo">
    <h1 style="font-size:24px; font-weight:700; color:#333; margin-bottom:24px;">Gestão de administradores</h1>

    <c:if test="${param.sucesso == 'adicionado'}">
        <div class="alerta alerta-sucesso">✔ Utilizador adicionado com sucesso.</div>
    </c:if>
    <c:if test="${param.sucesso == 'eliminado'}">
        <div class="alerta alerta-sucesso">✔ Utilizador eliminado com sucesso.</div>
    </c:if>

    <div class="card" style="margin-bottom: 24px;">
        <span class="secao-titulo">Adicionar novo utilizador</span>
        <form action="${pageContext.request.contextPath}/utilizadores" method="post">
            <input type="hidden" name="action" value="adicionar">
            <div class="form-linha col-3">
                <div class="campo">
                    <label>Username</label>
                    <input type="text" name="username" required>
                </div>
                <div class="campo">
                    <label>Password</label>
                    <input type="password" name="password" required>
                </div>
                <div class="campo">
                    <label>Permissão</label>
                    <select name="papel" required style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;">
                        <option value="GESTOR">GESTOR (Pode visualizar e editar)</option>
                        <option value="LEITOR">LEITOR (Apenas visualizar)</option>
                        <option value="SUPER_ADMIN">SUPER_ADMIN (Acesso Total)</option>
                    </select>
                </div>
            </div>
            <button type="submit" class="btn btn-primario">Adicionar utilizador</button>
        </form>
    </div>

    <div class="card">
        <span class="secao-titulo">Lista de Utilizadores</span>
        <div class="tabela-container">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Username</th>
                        <th>Papel</th>
                        <th>Acções</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="u" items="${utilizadores}">
                        <tr>
                            <td>${u.id}</td>
                            <td>${u.username}</td>
                            <td><span class="badge-papel ${u.papel}">${u.papel}</span></td>
                            <td>
                                <c:if test="${u.id != sessionScope.admin.id}">
                                    <form method="post" action="${pageContext.request.contextPath}/utilizadores" class="delete-form" data-confirm-message="Tem a certeza que deseja eliminar o utilizador ${u.username}?">
                                        <input type="hidden" name="action" value="eliminar">
                                        <input type="hidden" name="id" value="${u.id}">
                                        <button type="submit" class="btn btn-perigo" style="padding:4px 8px; font-size:12px;">🗑 Eliminar</button>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</main>
<div id="confirmModal" class="modal-backdrop" hidden>
    <div class="modal-card" role="dialog" aria-modal="true" aria-labelledby="confirmTitle">
        <h3 id="confirmTitle" style="margin:0 0 8px;color:#111827;">Confirmar eliminação</h3>
        <p id="confirmMessage" style="margin:0;color:#374151;line-height:1.5;"></p>
        <div class="modal-actions">
            <button type="button" class="btn btn-secundario" id="confirmCancel">Cancelar</button>
            <button type="button" class="btn btn-perigo" id="confirmConfirm">Sim, eliminar</button>
        </div>
    </div>
</div>
<script>
    const modal = document.getElementById('confirmModal');
    const modalMessage = document.getElementById('confirmMessage');
    const cancelBtn = document.getElementById('confirmCancel');
    const confirmBtn = document.getElementById('confirmConfirm');
    let pendingForm = null;

    document.querySelectorAll('form.delete-form').forEach(form => {
        form.addEventListener('submit', function (event) {
            event.preventDefault();
            pendingForm = form;
            modalMessage.textContent = form.dataset.confirmMessage || 'Tem a certeza que deseja eliminar este item?';
            modal.hidden = false;
        });
    });

    function closeModal() {
        modal.hidden = true;
        pendingForm = null;
    }

    cancelBtn.addEventListener('click', closeModal);
    modal.addEventListener('click', function (event) {
        if (event.target === modal) closeModal();
    });
    document.addEventListener('keydown', function (event) {
        if (event.key === 'Escape' && !modal.hidden) closeModal();
    });
    confirmBtn.addEventListener('click', function () {
        if (pendingForm) {
            modal.hidden = true;
            pendingForm.submit();
        }
    });
</script>
</body>
</html>

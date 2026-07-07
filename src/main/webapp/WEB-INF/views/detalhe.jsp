<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Detalhe da Inscrição – GET Training Academy</title>
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
    <a href="${pageContext.request.contextPath}/listagem" class="activo" id="nav-listagem">📋 Listagem</a>
</nav>

<main class="conteudo">

    <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:24px;">
        <h1 style="font-size:24px; font-weight:700; color:#333;">
            🔍 Detalhe da Inscrição #${inscricao.id}
        </h1>
        <a href="${pageContext.request.contextPath}/listagem"
           class="btn btn-secundario" id="btnVoltar">
            ← Voltar
        </a>
    </div>

    <!-- CURSO -->
    <div class="card" id="detalhe-curso">
        <span class="secao-titulo">Curso</span>
        <div class="detalhe-grid">
            <span class="detalhe-label">Curso:</span>
            <span class="detalhe-valor">${inscricao.curso.nome}</span>
            <span class="detalhe-label">Horário:</span>
            <span class="detalhe-valor">
                <span class="badge-horario">${inscricao.curso.horario}</span>
            </span>
            <span class="detalhe-label">Data Inscrição:</span>
            <span class="detalhe-valor">${inscricao.dataInscricao}</span>
        </div>
    </div>

    <!-- FORMANDO -->
    <div class="card" id="detalhe-formando">
        <span class="secao-titulo">Dados do Formando</span>
        <div class="detalhe-grid">
            <span class="detalhe-label">Nome:</span>
            <span class="detalhe-valor">${inscricao.formando.nome}</span>
            <span class="detalhe-label">Morada:</span>
            <span class="detalhe-valor">${inscricao.formando.morada}</span>
            <span class="detalhe-label">Localidade:</span>
            <span class="detalhe-valor">${inscricao.formando.localidade}</span>
            <span class="detalhe-label">Município:</span>
            <span class="detalhe-valor">${inscricao.formando.municipio}</span>
            <span class="detalhe-label">Telefone:</span>
            <span class="detalhe-valor">${inscricao.formando.telefone}</span>
            <span class="detalhe-label">Telemóvel:</span>
            <span class="detalhe-valor">${inscricao.formando.telemovel}</span>
            <span class="detalhe-label">Email:</span>
            <span class="detalhe-valor">${inscricao.formando.email}</span>
            <span class="detalhe-label">Nascimento:</span>
            <span class="detalhe-valor">${inscricao.formando.dataNascimento}</span>
            <span class="detalhe-label">Sexo:</span>
            <span class="detalhe-valor">
                <c:choose>
                    <c:when test="${inscricao.formando.sexo == 'M'}">Masculino</c:when>
                    <c:when test="${inscricao.formando.sexo == 'F'}">Feminino</c:when>
                    <c:otherwise>–</c:otherwise>
                </c:choose>
            </span>
        </div>
    </div>

    <!-- ENTIDADE PAGADORA -->
    <c:if test="${not empty inscricao.entidade and not empty inscricao.entidade.nome}">
    <div class="card" id="detalhe-entidade">
        <span class="secao-titulo">Entidade Pagadora</span>
        <div class="detalhe-grid">
            <span class="detalhe-label">Nome:</span>
            <span class="detalhe-valor">${inscricao.entidade.nome}</span>
            <span class="detalhe-label">Morada:</span>
            <span class="detalhe-valor">${inscricao.entidade.morada}</span>
            <span class="detalhe-label">Localidade:</span>
            <span class="detalhe-valor">${inscricao.entidade.localidade}</span>
            <span class="detalhe-label">Município:</span>
            <span class="detalhe-valor">${inscricao.entidade.municipio}</span>
            <span class="detalhe-label">Telefone:</span>
            <span class="detalhe-valor">${inscricao.entidade.telefone}</span>
            <span class="detalhe-label">Telemóvel:</span>
            <span class="detalhe-valor">${inscricao.entidade.telemovel}</span>
            <span class="detalhe-label">Fax:</span>
            <span class="detalhe-valor">${inscricao.entidade.fax}</span>
            <span class="detalhe-label">Email:</span>
            <span class="detalhe-valor">${inscricao.entidade.email}</span>
            <span class="detalhe-label">NIF:</span>
            <span class="detalhe-valor">${inscricao.entidade.nif}</span>
        </div>
    </div>
    </c:if>

    <!-- RESPONSÁVEL RH -->
    <c:if test="${not empty inscricao.responsavelRH and not empty inscricao.responsavelRH.nome}">
    <div class="card" id="detalhe-rh">
        <span class="secao-titulo">Responsável de RH</span>
        <div class="detalhe-grid">
            <span class="detalhe-label">Nome:</span>
            <span class="detalhe-valor">${inscricao.responsavelRH.nome}</span>
            <span class="detalhe-label">Telefone:</span>
            <span class="detalhe-valor">${inscricao.responsavelRH.telefone}</span>
            <span class="detalhe-label">Telemóvel:</span>
            <span class="detalhe-valor">${inscricao.responsavelRH.telemovel}</span>
            <span class="detalhe-label">Email:</span>
            <span class="detalhe-valor">${inscricao.responsavelRH.email}</span>
        </div>
    </div>
    </c:if>

    <!-- ACÇÕES -->
    <div class="card">
        <div class="btn-grupo">
            <a href="${pageContext.request.contextPath}/listagem"
               class="btn btn-secundario" id="btnVoltarListagem">← Voltar à listagem</a>
            <form method="post" action="${pageContext.request.contextPath}/listagem"
                  class="delete-form" data-confirm-message="Tem a certeza que deseja eliminar esta inscrição?"
                  style="display:inline;">
                <input type="hidden" name="action" value="eliminar">
                <input type="hidden" name="id" value="${inscricao.id}">
                <button type="submit" class="btn btn-perigo" id="btnEliminarDetalhe">
                    🗑 Eliminar esta inscrição
                </button>
            </form>
        </div>
    </div>

</main>

<footer class="rodape">
    <p>&copy; 2026 GET Training Academy Center · geral@get-ao.com · www.get-ao.com</p>
</footer>

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

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inscrições – GET Training Academy</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.3/dist/chart.umd.min.js"></script>
    <style>
        .modal-backdrop { position: fixed; inset: 0; background: rgba(15, 23, 42, 0.65); display: none; align-items: center; justify-content: center; z-index: 2000; padding: 16px; }
        .modal-backdrop:not([hidden]) { display: flex; }
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
            <div class="subtitulo">Sistema de Gestão</div>
        </div>
    </div>
    <div class="cabecalho-contatos">
        <div>Olá, <strong>${sessionScope.admin.username}</strong></div>
        <a href="${pageContext.request.contextPath}/logout">⎋ Sair</a>
    </div>
</header>

<nav class="nav-bar">
    <a href="${pageContext.request.contextPath}/dashboard">📊 Dashboard</a>
    <a href="${pageContext.request.contextPath}/listagem" class="activo">📋 Inscrições</a>
    <a href="${pageContext.request.contextPath}/relatorios">📑 Relatórios</a>
    <c:if test="${sessionScope.admin.papel == 'SUPER_ADMIN'}">
        <a href="${pageContext.request.contextPath}/utilizadores">👥 Utilizadores</a>
        <a href="${pageContext.request.contextPath}/dbadmin">🛠️ Base de Dados</a>
    </c:if>
</nav>

<main class="conteudo">

    <div class="page-header">
        <h1>Inscrições <span>· ${inscricoes.size()} resultado(s)</span></h1>
    </div>

    <%-- ======= Feedback ======= --%>
    <c:if test="${not empty param.eliminado}">
        <div class="alerta alerta-sucesso">✔ Inscrição #${param.eliminado} eliminada com sucesso.</div>
    </c:if>
    <c:if test="${not empty param.sucesso}">
        <div class="alerta alerta-sucesso">✔ Inscrição atualizada com sucesso.</div>
    </c:if>

    <%-- ======= Filtros ======= --%>
    <form method="get" action="${pageContext.request.contextPath}/listagem" id="filtroForm">
        <div class="filtro-bar">
            <div class="campo" style="min-width:180px;">
                <label style="font-size:12px;font-weight:600;color:var(--cinzento);text-transform:uppercase;letter-spacing:.5px;">Filtrar por Curso</label>
                <select name="curso" id="filtroCurso">
                    <option value="">— Todos os Cursos —</option>
                    <c:forEach var="c" items="${cursos}">
                        <option value="${c}" ${c == cursoFiltro ? 'selected' : ''}>${c}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="campo" style="min-width:140px;">
                <label style="font-size:12px;font-weight:600;color:var(--cinzento);text-transform:uppercase;letter-spacing:.5px;">Ordenar por</label>
                <select name="ordenar" id="filtroOrdenar">
                    <option value="">Data (mais recente)</option>
                    <option value="nome"  ${ordenarPor == 'nome'  ? 'selected' : ''}>Nome A-Z</option>
                    <option value="curso" ${ordenarPor == 'curso' ? 'selected' : ''}>Curso A-Z</option>
                </select>
            </div>
            <div class="campo" style="min-width:120px;">
                <label style="font-size:12px;font-weight:600;color:var(--cinzento);text-transform:uppercase;letter-spacing:.5px;">Mostrar Top N</label>
                <input type="number" name="topN" id="filtroTopN" min="1" max="999" placeholder="Ex: 5 ou 10"
                       value="${not empty topNParam ? topNParam : ''}">
            </div>
            <div style="display:flex;gap:8px;align-items:flex-end;">
                <button type="submit" class="btn btn-primario">🔍 Filtrar</button>
                <a href="${pageContext.request.contextPath}/listagem" class="btn btn-secundario">↺ Limpar</a>
            </div>
        </div>
    </form>

    <%-- ======= Tabela ======= --%>
    <div class="card">
        <c:choose>
            <c:when test="${empty inscricoes}">
                <div class="alerta alerta-info" style="text-align:center;padding:40px;">
                    <p style="font-size:17px;">📭 Nenhuma inscrição encontrada com os filtros actuais.</p>
                    <br>
                    <a href="${pageContext.request.contextPath}/listagem" class="btn btn-secundario">↺ Ver todos</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="tabela-container">
                    <table id="tabelaInscricoes">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Formando</th>
                                <th>Email</th>
                                <th>Curso</th>
                                <th>Horário</th>
                                <th>Acções</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:set var="rank" value="0"/>
                            <c:forEach var="ins" items="${inscricoes}">
                                <c:set var="rank" value="${rank + 1}"/>
                                <tr>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty topNParam && rank == 1}"><span class="rank-badge gold">🥇</span></c:when>
                                            <c:when test="${not empty topNParam && rank == 2}"><span class="rank-badge silver">🥈</span></c:when>
                                            <c:when test="${not empty topNParam && rank == 3}"><span class="rank-badge bronze">🥉</span></c:when>
                                            <c:otherwise><strong style="color:var(--cinzento);">${ins.id}</strong></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <div style="font-weight:600;color:var(--cinzento-dk);">${ins.formando.nome}</div>
                                    </td>
                                    <td><span style="color:var(--cinzento);font-size:13px;">${ins.formando.email}</span></td>
                                    <td><strong>${ins.curso.nome}</strong></td>
                                    <td><span class="badge-horario">${ins.curso.horario}</span></td>
                                    <td>
                                        <div style="display:flex;gap:6px;flex-wrap:wrap;">
                                            <a href="${pageContext.request.contextPath}/listagem?id=${ins.id}"
                                               class="btn btn-info" id="btnDetalhe${ins.id}">🔍 Ver</a>
                                            <c:if test="${sessionScope.admin.papel != 'LEITOR'}">
                                                <a href="${pageContext.request.contextPath}/editar?id=${ins.id}"
                                                   class="btn btn-secundario" style="padding:7px 14px;font-size:12.5px;" id="btnEditar${ins.id}">✏️ Editar</a>
                                            </c:if>
                                            <c:if test="${sessionScope.admin.papel == 'SUPER_ADMIN'}">
                                                <form method="post" action="${pageContext.request.contextPath}/listagem"
                                                      class="delete-form" data-confirm-message="Tem a certeza que deseja eliminar esta inscrição?"
                                                      style="display:inline;">
                                                    <jsp:include page="/WEB-INF/views/includes/csrfToken.jspf" />
                                                    <input type="hidden" name="action" value="eliminar">
                                                    <input type="hidden" name="id" value="${ins.id}">
                                                    <button type="submit" class="btn btn-perigo" id="btnEliminar${ins.id}">🗑 Eliminar</button>
                                                </form>
                                            </c:if>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <%-- ======= Mini Chart por Curso Filtrado ======= --%>
    <c:if test="${not empty inscricoes and empty cursoFiltro}">
        <div class="card">
            <span class="secao-titulo">📊 Distribuição da Listagem Actual</span>
            <canvas id="chartListagem" height="120"></canvas>
        </div>
    </c:if>

</main>

<script>
    <c:if test="${not empty inscricoes and empty cursoFiltro}">
    // Agrupar inscrições por curso no lado do cliente
    const rows = document.querySelectorAll('#tabelaInscricoes tbody tr');
    const contagem = {};
    rows.forEach(r => {
        const curso = r.cells[3].textContent.trim();
        contagem[curso] = (contagem[curso] || 0) + 1;
    });
    const labels = Object.keys(contagem);
    const data   = Object.values(contagem);
    const colors = ['#E53935','#3B82F6','#059669','#7C3AED','#EA580C','#D97706','#0891B2','#DB2777'];

    if (labels.length > 0) {
        new Chart(document.getElementById('chartListagem'), {
            type: 'bar',
            data: {
                labels,
                datasets: [{
                    label: 'Inscrições',
                    data,
                    backgroundColor: colors,
                    borderRadius: 8,
                    borderSkipped: false
                }]
            },
            options: {
                plugins: { legend:{display:false}, tooltip:{ backgroundColor:'rgba(15,23,42,0.9)', padding:12, cornerRadius:8 } },
                scales: { y:{ beginAtZero:true, grid:{color:'#f1f5f9'}, ticks:{stepSize:1} }, x:{grid:{display:false}} },
                animation: { duration: 800 }
            }
        });
    }
    </c:if>
</script>
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

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard – GET Training Academy</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.3/dist/chart.umd.min.js"></script>
</head>
<body>

<header class="cabecalho">
    <div class="logo-bloco">
        <div class="logo-icone">▮</div>
        <div class="logo-texto">
            <div class="marca">GET <span>Training</span></div>
            <div class="subtitulo">Management System</div>
        </div>
    </div>
    <div class="cabecalho-contatos">
        <div>Olá, <strong>${sessionScope.admin.username}</strong></div>
        <a href="${pageContext.request.contextPath}/logout">⎋ Sair</a>
    </div>
</header>

<nav class="nav-bar">
    <a href="${pageContext.request.contextPath}/dashboard" class="activo">📊 Dashboard</a>
    <a href="${pageContext.request.contextPath}/listagem">📋 Inscrições</a>
    <a href="${pageContext.request.contextPath}/relatorios">📑 Relatórios</a>
    <c:if test="${sessionScope.admin.papel == 'SUPER_ADMIN'}">
        <a href="${pageContext.request.contextPath}/utilizadores">👥 Utilizadores</a>
    </c:if>
</nav>

<main class="conteudo">

    <div class="page-header">
        <h1>Dashboard <span>· Visão Geral</span></h1>
    </div>

    <%-- ======== KPI Cards ======== --%>
    <div class="stats-grid">
        <div class="stat-card vermelho">
            <div class="stat-icon">📝</div>
            <div class="stat-info">
                <h3>Total de Inscrições</h3>
                <div class="value">${totalInscricoes}</div>
            </div>
        </div>
        <div class="stat-card azul">
            <div class="stat-icon">🎓</div>
            <div class="stat-info">
                <h3>Cursos Disponíveis</h3>
                <div class="value">${totalCursos}</div>
            </div>
        </div>
        <div class="stat-card sucesso">
            <div class="stat-icon">📅</div>
            <div class="stat-info">
                <h3>Inscrições Hoje</h3>
                <div class="value">${inscricoesHoje}</div>
            </div>
        </div>
        <div class="stat-card roxo">
            <div class="stat-icon">📆</div>
            <div class="stat-info">
                <h3>Este Mês</h3>
                <div class="value">${inscricoesEsteMes}</div>
            </div>
        </div>
    </div>

    <%-- ======== Charts Row ======== --%>
    <div class="charts-grid">
        <div class="chart-card">
            <div class="chart-title">🍕 Inscrições por Curso (Pizza)</div>
            <canvas id="chartPizza" height="230"></canvas>
        </div>
        <div class="chart-card">
            <div class="chart-title">📊 Inscrições por Curso (Barras)</div>
            <canvas id="chartBarras" height="230"></canvas>
        </div>
    </div>

    <div class="charts-grid">
        <div class="chart-card">
            <div class="chart-title">📈 Tendência Mensal (últimos 6 meses)</div>
            <canvas id="chartMensal" height="210"></canvas>
        </div>
        <div class="chart-card">
            <div class="chart-title">⏰ Preferência de Horário</div>
            <canvas id="chartHorario" height="210"></canvas>
        </div>
    </div>

    <%-- ======== Top Cursos Table ======== --%>
    <div class="card">
        <span class="secao-titulo">🏆 Top Cursos Mais Procurados</span>
        <div class="tabela-container">
            <table>
                <thead>
                    <tr><th>#</th><th>Curso</th><th>Inscrições</th><th>Popularidade</th></tr>
                </thead>
                <tbody>
                    <c:set var="i" value="0"/>
                    <c:forEach var="entry" items="${topCursos}">
                        <c:set var="i" value="${i + 1}"/>
                        <tr>
                            <td>
                                <span class="rank-badge
                                    <c:choose>
                                        <c:when test='${i == 1}'>gold</c:when>
                                        <c:when test='${i == 2}'>silver</c:when>
                                        <c:when test='${i == 3}'>bronze</c:when>
                                        <c:otherwise>other</c:otherwise>
                                    </c:choose>">
                                    <c:choose>
                                        <c:when test="${i == 1}">🥇</c:when>
                                        <c:when test="${i == 2}">🥈</c:when>
                                        <c:when test="${i == 3}">🥉</c:when>
                                        <c:otherwise>${i}</c:otherwise>
                                    </c:choose>
                                </span>
                            </td>
                            <td><strong>${entry['curso']}</strong></td>
                            <td>${entry['total']}</td>
                            <td style="width:200px;">
                                <div class="progress-bar">
                                    <div class="progress-fill" style="width:${totalInscricoes > 0 ? (entry['total'] * 100 / totalInscricoes) : 0}%"></div>
                                </div>
                                <small style="color:var(--cinzento);">${totalInscricoes > 0 ? (entry['total'] * 100 / totalInscricoes) : 0}%</small>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty topCursos}">
                        <tr><td colspan="4" style="text-align:center;color:var(--cinzento);padding:24px;">Sem dados disponíveis</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>

</main>

<script>
    // Dados do servidor
    const dadosCurso  = {
        labels: [<c:forEach var="e" items="${inscricoesPorCurso}">"<c:out value='${e.key}'/>",</c:forEach>],
        values: [<c:forEach var="e" items="${inscricoesPorCurso}">${e.value},</c:forEach>]
    };
    const dadosMensal = {
        labels: [<c:forEach var="e" items="${inscricoesPorMes}">"${e.key}",</c:forEach>],
        values: [<c:forEach var="e" items="${inscricoesPorMes}">${e.value},</c:forEach>]
    };
    const dadosHorario = {
        labels: [<c:forEach var="e" items="${inscricoesPorHorario}">"<c:out value='${e.key}'/>",</c:forEach>],
        values: [<c:forEach var="e" items="${inscricoesPorHorario}">${e.value},</c:forEach>]
    };

    const palette = ['#E53935','#3B82F6','#059669','#7C3AED','#EA580C','#D97706','#0891B2','#DB2777'];

    const tooltipPlugin = { backgroundColor:'rgba(15,23,42,0.9)', titleFont:{size:13,weight:'bold'}, bodyFont:{size:12}, padding:12, cornerRadius:8 };

    // Pizza
    new Chart(document.getElementById('chartPizza'), {
        type: 'doughnut',
        data: {
            labels: dadosCurso.labels,
            datasets: [{ data: dadosCurso.values, backgroundColor: palette, borderWidth: 3, borderColor: '#fff', hoverBorderWidth: 4 }]
        },
        options: {
            plugins: { legend:{ position:'bottom', labels:{padding:14,font:{size:12}} }, tooltip: tooltipPlugin },
            cutout: '60%',
            animation: { animateScale: true, duration: 800 }
        }
    });

    // Barras
    new Chart(document.getElementById('chartBarras'), {
        type: 'bar',
        data: {
            labels: dadosCurso.labels,
            datasets: [{ label: 'Inscrições', data: dadosCurso.values, backgroundColor: palette, borderRadius: 6, borderSkipped: false }]
        },
        options: {
            plugins: { legend:{display:false}, tooltip: tooltipPlugin },
            scales: {
                y: { beginAtZero:true, grid:{color:'#f1f5f9'}, ticks:{stepSize:1} },
                x: { grid:{display:false} }
            },
            animation: { duration: 800 }
        }
    });

    // Linha mensal
    new Chart(document.getElementById('chartMensal'), {
        type: 'line',
        data: {
            labels: dadosMensal.labels,
            datasets: [{
                label: 'Inscrições',
                data: dadosMensal.values,
                borderColor: '#E53935',
                backgroundColor: 'rgba(229,57,53,0.08)',
                borderWidth: 2.5,
                fill: true,
                tension: 0.4,
                pointBackgroundColor: '#E53935',
                pointRadius: 5,
                pointHoverRadius: 8
            }]
        },
        options: {
            plugins: { legend:{display:false}, tooltip: tooltipPlugin },
            scales: {
                y: { beginAtZero:true, grid:{color:'#f1f5f9'}, ticks:{stepSize:1} },
                x: { grid:{display:false} }
            }
        }
    });

    // Pizza horário
    new Chart(document.getElementById('chartHorario'), {
        type: 'polarArea',
        data: {
            labels: dadosHorario.labels,
            datasets: [{ data: dadosHorario.values, backgroundColor: palette.map(c => c + 'CC'), borderWidth: 2 }]
        },
        options: {
            plugins: { legend:{ position:'bottom', labels:{padding:12,font:{size:12}} }, tooltip: tooltipPlugin },
            animation: { duration: 800 }
        }
    });
</script>
</body>
</html>

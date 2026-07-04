<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Relatórios – GET Training Academy</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.3/dist/chart.umd.min.js"></script>
    <style>
        .tipo-cards { display:grid; grid-template-columns:repeat(auto-fit,minmax(210px,1fr)); gap:16px; margin-bottom:24px; }
        .tipo-card {
            border:2px solid var(--borda); border-radius:var(--radius-lg); padding:18px 20px;
            cursor:pointer; transition:all .2s ease; background:var(--branco); text-align:center;
        }
        .tipo-card:hover { border-color:var(--vermelho); transform:translateY(-2px); box-shadow:var(--sombra); }
        .tipo-card.active { border-color:var(--vermelho); background:var(--vermelho-lt); }
        .tipo-card .ico { font-size:28px; margin-bottom:8px; }
        .tipo-card p { font-size:13px; font-weight:600; color:var(--cinzento-dk); }
        .tipo-card small { font-size:11px; color:var(--cinzento); }
        .filtros-dinamicos { background:var(--cinzento-lt); border-radius:var(--radius-lg); padding:20px 24px; margin-bottom:20px; border:1px solid var(--borda); }
        .resultado-header { display:flex; justify-content:space-between; align-items:center; margin-bottom:16px; }
        .resultado-count { font-size:13px; color:var(--cinzento); font-weight:500; }
    </style>
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
    <a href="${pageContext.request.contextPath}/dashboard">📊 Dashboard</a>
    <a href="${pageContext.request.contextPath}/listagem">📋 Inscrições</a>
    <a href="${pageContext.request.contextPath}/relatorios" class="activo">📑 Relatórios</a>
    <c:if test="${sessionScope.admin.papel == 'SUPER_ADMIN'}">
        <a href="${pageContext.request.contextPath}/utilizadores">👥 Utilizadores</a>
    </c:if>
</nav>

<main class="conteudo">

    <div class="page-header">
        <h1>Relatórios <span>· Consultas Dinâmicas</span></h1>
    </div>

    <%-- ======= KPI Cards ======= --%>
    <div class="stats-grid">
        <div class="stat-card vermelho">
            <div class="stat-icon">👥</div>
            <div class="stat-info"><h3>Total Estudantes</h3><div class="value">${totalEstudantes}</div></div>
        </div>
        <div class="stat-card azul">
            <div class="stat-icon">👦</div>
            <div class="stat-info"><h3>Masculinos</h3><div class="value">${totalMasculino}</div></div>
        </div>
        <div class="stat-card roxo">
            <div class="stat-icon">👧</div>
            <div class="stat-info"><h3>Femininas</h3><div class="value">${totalFeminino}</div></div>
        </div>
    </div>

    <%-- ======= Tabela Resumo por Curso ======= --%>
    <div class="card">
        <span class="secao-titulo">📚 Resumo por Curso</span>
        <div class="tabela-container">
            <table>
                <thead><tr><th>#</th><th>Curso</th><th>Total</th><th>Masculino</th><th>Feminino</th><th>Proporção</th></tr></thead>
                <tbody>
                    <c:set var="r" value="0"/>
                    <c:forEach var="c" items="${inscritosPorCurso}">
                        <c:set var="r" value="${r+1}"/>
                        <tr>
                            <td><strong style="color:var(--cinzento);">${r}</strong></td>
                            <td><strong>${c['curso']}</strong></td>
                            <td><strong style="color:var(--vermelho);font-size:16px;">${c['total']}</strong></td>
                            <td><span style="color:var(--azul);">👦 ${c['masculino']}</span></td>
                            <td><span style="color:#DB2777;">👧 ${c['feminino']}</span></td>
                            <td style="width:180px;">
                                <div class="progress-bar">
                                    <div class="progress-fill" style="width:${totalEstudantes > 0 ? c['total'] * 100 / totalEstudantes : 0}%"></div>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty inscritosPorCurso}">
                        <tr><td colspan="6" style="text-align:center;color:var(--cinzento);padding:20px;">Sem dados</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>

    <%-- ======= CONSULTA DINÂMICA ======= --%>
    <div class="card">
        <span class="secao-titulo">🔎 Consulta Dinâmica</span>

        <form method="get" action="${pageContext.request.contextPath}/relatorios" id="relForm">

            <%-- Tipo de Consulta --%>
            <p style="font-size:13px;font-weight:600;color:var(--cinzento);margin-bottom:12px;text-transform:uppercase;letter-spacing:.5px;">Escolha o tipo de relatório:</p>
            <div class="tipo-cards">
                <label class="tipo-card ${tipoFiltro == 'TOP_CURSOS' ? 'active' : ''}">
                    <input type="radio" name="tipo" value="TOP_CURSOS" hidden ${tipoFiltro == 'TOP_CURSOS' ? 'checked' : ''}>
                    <div class="ico">🏆</div>
                    <p>Top Cursos</p>
                    <small>Mais inscrições</small>
                </label>
                <label class="tipo-card ${tipoFiltro == 'TOP_CURSOS_GENERO' ? 'active' : ''}">
                    <input type="radio" name="tipo" value="TOP_CURSOS_GENERO" hidden ${tipoFiltro == 'TOP_CURSOS_GENERO' ? 'checked' : ''}>
                    <div class="ico">🎓</div>
                    <p>Cursos por Género</p>
                    <small>Inscrições masculinas/femininas</small>
                </label>
                <label class="tipo-card ${tipoFiltro == 'TOP_ESTUDANTES_GENERO' ? 'active' : ''}">
                    <input type="radio" name="tipo" value="TOP_ESTUDANTES_GENERO" hidden ${tipoFiltro == 'TOP_ESTUDANTES_GENERO' ? 'checked' : ''}>
                    <div class="ico">👤</div>
                    <p>Estudantes por Género</p>
                    <small>Lista de Masc./Fem.</small>
                </label>
                <label class="tipo-card ${tipoFiltro == 'ESTUDANTES_CURSO_GENERO' ? 'active' : ''}">
                    <input type="radio" name="tipo" value="ESTUDANTES_CURSO_GENERO" hidden ${tipoFiltro == 'ESTUDANTES_CURSO_GENERO' ? 'checked' : ''}>
                    <div class="ico">🔬</div>
                    <p>Filtro Avançado</p>
                    <small>Curso + Género + Top N</small>
                </label>
            </div>

            <%-- Filtros dinâmicos --%>
            <div class="filtros-dinamicos">
                <div class="form-linha col-3">
                    <div class="campo">
                        <label>🔢 Top N (quantidade)</label>
                        <input type="number" name="topN" min="1" max="999" value="${topNFiltro}" placeholder="Ex: 5">
                    </div>
                    <div class="campo" id="campo-sexo">
                        <label>⚧ Género</label>
                        <select name="sexo">
                            <option value="TODOS" ${sexoFiltro == 'TODOS' ? 'selected' : ''}>— Todos —</option>
                            <option value="M"     ${sexoFiltro == 'M'     ? 'selected' : ''}>👦 Masculino</option>
                            <option value="F"     ${sexoFiltro == 'F'     ? 'selected' : ''}>👧 Feminino</option>
                        </select>
                    </div>
                    <div class="campo" id="campo-curso">
                        <label>🎓 Curso</label>
                        <select name="cursoFiltro">
                            <option value="">— Todos os Cursos —</option>
                            <c:forEach var="c" items="${cursosDisponiveis}">
                                <option value="${c}" ${c == cursoFiltro2 ? 'selected' : ''}>${c}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>

            <button type="submit" class="btn btn-primario" style="font-size:15px;padding:12px 32px;">
                🔍 Gerar Relatório
            </button>
        </form>
    </div>

    <%-- ======= RESULTADO DA CONSULTA ======= --%>
    <c:if test="${not empty resultado}">
        <div class="card">
            <div class="resultado-header">
                <span class="secao-titulo">📋 ${tituloResultado}</span>
                <span class="resultado-count">${resultado.size()} resultado(s)</span>
            </div>

            <c:choose>
                <%-- Resultado de tipo "cursos" --%>
                <c:when test="${tipoResultado == 'cursos'}">
                    <div style="display:grid;grid-template-columns:1fr 1fr;gap:20px;">
                        <div class="tabela-container">
                            <table>
                                <thead><tr><th>#</th><th>Curso</th><th>Inscrições</th></tr></thead>
                                <tbody>
                                    <c:set var="idx" value="0"/>
                                    <c:forEach var="item" items="${resultado}">
                                        <c:set var="idx" value="${idx+1}"/>
                                        <tr>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${idx==1}">🥇</c:when>
                                                    <c:when test="${idx==2}">🥈</c:when>
                                                    <c:when test="${idx==3}">🥉</c:when>
                                                    <c:otherwise><strong>${idx}</strong></c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td><strong>${item['curso']}</strong></td>
                                            <td>
                                                <strong style="font-size:18px;color:var(--vermelho);">${item['total']}</strong>
                                                <div class="progress-bar" style="margin-top:6px;">
                                                    <div class="progress-fill" style="width:${item['total'] * 100 / resultado[0]['total']}%"></div>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                        <div><canvas id="chartResultado" height="250"></canvas></div>
                    </div>
                </c:when>

                <%-- Resultado de tipo "estudantes" --%>
                <c:when test="${tipoResultado == 'estudantes'}">
                    <div class="tabela-container">
                        <table>
                            <thead><tr><th>#</th><th>Nome</th><th>Curso</th><th>Email</th><th>Género</th></tr></thead>
                            <tbody>
                                <c:set var="idx" value="0"/>
                                <c:forEach var="item" items="${resultado}">
                                    <c:set var="idx" value="${idx+1}"/>
                                    <tr>
                                        <td><strong style="color:var(--cinzento);">${idx}</strong></td>
                                        <td><strong>${item['estudante']}</strong></td>
                                        <td>${item['curso']}</td>
                                        <td style="color:var(--cinzento);font-size:13px;">${item['email']}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${item['sexo'] == 'M'}"><span style="color:var(--azul);">👦 Masc.</span></c:when>
                                                <c:otherwise><span style="color:#DB2777;">👧 Fem.</span></c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>
            </c:choose>
        </div>

        <%-- Gráfico para resultados de cursos --%>
        <c:if test="${tipoResultado == 'cursos'}">
        <script>
            const labels = [<c:forEach var="i" items="${resultado}">"<c:out value='${i["curso"]}'/>",</c:forEach>];
            const vals   = [<c:forEach var="i" items="${resultado}">${i['total']},</c:forEach>];
            const colors = ['#E53935','#3B82F6','#059669','#7C3AED','#EA580C','#D97706','#0891B2','#DB2777'];
            new Chart(document.getElementById('chartResultado'), {
                type: 'doughnut',
                data: { labels, datasets: [{ data: vals, backgroundColor: colors, borderWidth: 3, borderColor:'#fff', hoverBorderWidth:4 }] },
                options: { plugins:{ legend:{position:'bottom'}, tooltip:{backgroundColor:'rgba(15,23,42,.9)',padding:12,cornerRadius:8} }, cutout:'55%', animation:{animateScale:true} }
            });
        </script>
        </c:if>
    </c:if>

</main>

<script>
    // Seleccionar tipo visualmente
    document.querySelectorAll('.tipo-card').forEach(card => {
        card.addEventListener('click', () => {
            document.querySelectorAll('.tipo-card').forEach(c => c.classList.remove('active'));
            card.classList.add('active');
        });
    });
</script>
</body>
</html>

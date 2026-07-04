<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Lista de todas as inscrições – GET Training Academy Center.">
    <title>Listagem de Inscrições – GET Training Academy</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<header class="cabecalho">
    <div class="logo-bloco">
        <div class="logo-icone">▮</div>
        <div class="logo-texto">
            <div class="marca">GET <span>Training</span></div>
            <div class="subtitulo">Painel Administrativo</div>
        </div>
    </div>
    <div class="cabecalho-contatos">
        <div>Olá, ${sessionScope.admin.username}</div>
        <a href="${pageContext.request.contextPath}/logout" style="color: white;">Sair</a>
    </div>
</header>

<nav class="nav-bar">
    <a href="${pageContext.request.contextPath}/dashboard" id="nav-dashboard">📊 Dashboard</a>
    <a href="${pageContext.request.contextPath}/listagem" class="activo" id="nav-listagem">📋 Gerir Inscrições</a>
    <c:if test="${sessionScope.admin.papel == 'SUPER_ADMIN'}">
        <a href="${pageContext.request.contextPath}/utilizadores">👥 Utilizadores</a>
    </c:if>
</nav>

<main class="conteudo">

    <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:24px;">
        <h1 style="font-size:24px; font-weight:700; color:#333;">
            📋 Inscrições Registadas
        </h1>
    </div>

    <%-- Feedback ao eliminar ou atualizar --%>
    <c:if test="${not empty param.eliminado}">
        <div class="alerta alerta-sucesso" id="msgEliminado">
            ✔ Inscrição #${param.eliminado} eliminada com sucesso.
        </div>
    </c:if>
    <c:if test="${not empty param.sucesso}">
        <div class="alerta alerta-sucesso" id="msgAtualizado">
            ✔ Inscrição atualizada com sucesso.
        </div>
    </c:if>

    <div class="card">
        <c:choose>
            <c:when test="${empty inscricoes}">
                <div class="alerta alerta-info" style="text-align:center; padding:40px;">
                    <p style="font-size:18px;">📭 Ainda não existem inscrições registadas.</p>
                </div>
            </c:when>
            <c:otherwise>
                <p style="font-size:14px; color:#757575; margin-bottom:16px;">
                    Total: <strong>${inscricoes.size()}</strong> inscrição(ões)
                </p>
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
                            <c:forEach var="ins" items="${inscricoes}">
                                <tr>
                                    <td><strong>${ins.id}</strong></td>
                                    <td>${ins.formando.nome}</td>
                                    <td style="color:#757575;">${ins.formando.email}</td>
                                    <td>${ins.curso.nome}</td>
                                    <td>
                                        <span class="badge-horario">${ins.curso.horario}</span>
                                    </td>
                                    <td>
                                        <div style="display:flex; gap:8px;">
                                            <a href="${pageContext.request.contextPath}/listagem?id=${ins.id}"
                                               class="btn btn-secundario"
                                               style="padding:6px 14px; font-size:12px;"
                                               id="btnDetalhe${ins.id}">
                                                🔍 Ver
                                            </a>
                                            <c:if test="${sessionScope.admin.papel != 'LEITOR'}">
                                                <a href="${pageContext.request.contextPath}/editar?id=${ins.id}"
                                                   class="btn btn-primario"
                                                   style="padding:6px 14px; font-size:12px;"
                                                   id="btnEditar${ins.id}">
                                                    ✏️ Editar
                                                </a>
                                            </c:if>
                                            <c:if test="${sessionScope.admin.papel == 'SUPER_ADMIN'}">
                                                <form method="post"
                                                      action="${pageContext.request.contextPath}/listagem"
                                                      onsubmit="return confirm('Eliminar inscrição #${ins.id}?')"
                                                      style="display:inline;">
                                                    <input type="hidden" name="action" value="eliminar">
                                                    <input type="hidden" name="id" value="${ins.id}">
                                                    <button type="submit" class="btn btn-perigo"
                                                            id="btnEliminar${ins.id}">
                                                        🗑 Eliminar
                                                    </button>
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

</main>

<footer class="rodape">
    <p>&copy; 2026 GET Training Academy Center · geral@get-ao.com · www.get-ao.com</p>
</footer>

</body>
</html>

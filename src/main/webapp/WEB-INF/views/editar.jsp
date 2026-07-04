<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Inscrição – GET Training Academy</title>
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
    <a href="${pageContext.request.contextPath}/dashboard">📊 Dashboard</a>
    <a href="${pageContext.request.contextPath}/listagem">📋 Gerir Inscrições</a>
</nav>

<main class="conteudo">
    <h1 style="font-size:24px;font-weight:700;color:#333;margin-bottom:24px;">Editar Inscrição #${inscricao.id}</h1>

    <c:if test="${not empty mensagemErro}">
        <div class="alerta alerta-erro" role="alert" id="erroServidor">
            <strong>⚠ Erro:</strong> ${mensagemErro}
        </div>
    </c:if>

    <form id="formInscricao" action="${pageContext.request.contextPath}/editar" method="post" novalidate>
        <input type="hidden" name="inscricaoId" value="${inscricao.id}">

        <!-- SECÇÃO: CURSO -->
        <section class="card" id="seccao-curso">
            <span class="secao-titulo">Curso</span>
            <div class="form-linha col-1">
                <div class="campo">
                    <label for="curso">Nome do Curso <span class="obrig">*</span></label>
                    <input type="text" id="curso" name="curso" maxlength="150" value="<c:out value='${inscricao.curso.nome}'/>" required>
                </div>
            </div>
            <div class="campo">
                <label>Horário Preferencial <span class="obrig">*</span></label>
                <div class="radio-grupo" id="horarioDiv">
                    <label class="radio-opcao"><input type="radio" name="horario" value="Manhã" <c:if test="${inscricao.curso.horario == 'Manhã'}">checked</c:if>> Manhã</label>
                    <label class="radio-opcao"><input type="radio" name="horario" value="Tarde" <c:if test="${inscricao.curso.horario == 'Tarde'}">checked</c:if>> Tarde</label>
                    <label class="radio-opcao"><input type="radio" name="horario" value="Fim de Tarde" <c:if test="${inscricao.curso.horario == 'Fim de Tarde'}">checked</c:if>> Fim de Tarde</label>
                    <label class="radio-opcao"><input type="radio" name="horario" value="Sábado" <c:if test="${inscricao.curso.horario == 'Sábado'}">checked</c:if>> Sábado</label>
                </div>
            </div>
        </section>

        <!-- SECÇÃO: DADOS DO FORMANDO -->
        <section class="card" id="seccao-formando">
            <span class="secao-titulo">Dados do Formando</span>
            <div class="form-linha col-1">
                <div class="campo">
                    <label for="nome">Nome Completo <span class="obrig">*</span></label>
                    <input type="text" id="nome" name="nome" maxlength="150" value="<c:out value='${inscricao.formando.nome}'/>" required>
                </div>
            </div>
            <div class="form-linha col-1">
                <div class="campo">
                    <label for="morada">Morada</label>
                    <input type="text" id="morada" name="morada" maxlength="200" value="<c:out value='${inscricao.formando.morada}'/>">
                </div>
            </div>
            <div class="form-linha col-2">
                <div class="campo">
                    <label for="localidade">Localidade</label>
                    <input type="text" id="localidade" name="localidade" maxlength="100" value="<c:out value='${inscricao.formando.localidade}'/>">
                </div>
                <div class="campo">
                    <label for="municipio">Município</label>
                    <input type="text" id="municipio" name="municipio" maxlength="100" value="<c:out value='${inscricao.formando.municipio}'/>">
                </div>
            </div>
            <div class="form-linha col-2">
                <div class="campo">
                    <label for="telefone">Telefone</label>
                    <input type="tel" id="telefone" name="telefone" maxlength="20" value="<c:out value='${inscricao.formando.telefone}'/>">
                </div>
                <div class="campo">
                    <label for="telemovel">Telemóvel</label>
                    <input type="tel" id="telemovel" name="telemovel" maxlength="20" value="<c:out value='${inscricao.formando.telemovel}'/>">
                </div>
            </div>
            <div class="form-linha col-1">
                <div class="campo">
                    <label for="email">Email <span class="obrig">*</span></label>
                    <input type="email" id="email" name="email" maxlength="100" value="<c:out value='${inscricao.formando.email}'/>" required>
                </div>
            </div>
            <div class="form-linha col-2">
                <div class="campo">
                    <label for="dataNascimento">Data de Nascimento</label>
                    <input type="date" id="dataNascimento" name="dataNascimento" value="<c:out value='${inscricao.formando.dataNascimento}'/>">
                </div>
                <div class="campo">
                    <label>Sexo <span class="obrig">*</span></label>
                    <div class="radio-grupo" id="sexoDiv">
                        <label class="radio-opcao"><input type="radio" name="sexo" value="M" <c:if test="${inscricao.formando.sexo == 'M'}">checked</c:if>> Masculino</label>
                        <label class="radio-opcao"><input type="radio" name="sexo" value="F" <c:if test="${inscricao.formando.sexo == 'F'}">checked</c:if>> Feminino</label>
                    </div>
                </div>
            </div>
        </section>

        <!-- SECÇÃO: ENTIDADE PAGADORA -->
        <section class="card" id="seccao-entidade">
            <span class="secao-titulo">Entidade Pagadora</span>
            <div class="form-linha col-1">
                <div class="campo">
                    <label for="epNome">Nome da Entidade</label>
                    <input type="text" id="epNome" name="epNome" maxlength="150" value="<c:out value='${inscricao.entidade.nome}'/>">
                </div>
            </div>
            <div class="form-linha col-1">
                <div class="campo">
                    <label for="epMorada">Morada</label>
                    <input type="text" id="epMorada" name="epMorada" maxlength="200" value="<c:out value='${inscricao.entidade.morada}'/>">
                </div>
            </div>
            <div class="form-linha col-2">
                <div class="campo">
                    <label for="epLocalidade">Localidade</label>
                    <input type="text" id="epLocalidade" name="epLocalidade" maxlength="100" value="<c:out value='${inscricao.entidade.localidade}'/>">
                </div>
                <div class="campo">
                    <label for="epMunicipio">Município</label>
                    <input type="text" id="epMunicipio" name="epMunicipio" maxlength="100" value="<c:out value='${inscricao.entidade.municipio}'/>">
                </div>
            </div>
            <div class="form-linha col-3">
                <div class="campo">
                    <label for="epTelefone">Telefone</label>
                    <input type="tel" id="epTelefone" name="epTelefone" maxlength="20" value="<c:out value='${inscricao.entidade.telefone}'/>">
                </div>
                <div class="campo">
                    <label for="epTelemovel">Telemóvel</label>
                    <input type="tel" id="epTelemovel" name="epTelemovel" maxlength="20" value="<c:out value='${inscricao.entidade.telemovel}'/>">
                </div>
                <div class="campo">
                    <label for="epFax">Fax</label>
                    <input type="tel" id="epFax" name="epFax" maxlength="20" value="<c:out value='${inscricao.entidade.fax}'/>">
                </div>
            </div>
            <div class="form-linha col-2">
                <div class="campo">
                    <label for="epEmail">Email</label>
                    <input type="email" id="epEmail" name="epEmail" maxlength="100" value="<c:out value='${inscricao.entidade.email}'/>">
                </div>
                <div class="campo">
                    <label for="epNif">NIF</label>
                    <input type="text" id="epNif" name="epNif" maxlength="20" value="<c:out value='${inscricao.entidade.nif}'/>">
                </div>
            </div>
        </section>

        <!-- SECÇÃO: RESPONSÁVEL RH -->
        <section class="card" id="seccao-rh">
            <span class="secao-titulo">Responsável de RH</span>
            <div class="form-linha col-1">
                <div class="campo">
                    <label for="rhNome">Nome</label>
                    <input type="text" id="rhNome" name="rhNome" maxlength="150" value="<c:out value='${inscricao.responsavelRH.nome}'/>">
                </div>
            </div>
            <div class="form-linha col-3">
                <div class="campo">
                    <label for="rhTelefone">Telefone</label>
                    <input type="tel" id="rhTelefone" name="rhTelefone" maxlength="20" value="<c:out value='${inscricao.responsavelRH.telefone}'/>">
                </div>
                <div class="campo">
                    <label for="rhTelemovel">Telemóvel</label>
                    <input type="tel" id="rhTelemovel" name="rhTelemovel" maxlength="20" value="<c:out value='${inscricao.responsavelRH.telemovel}'/>">
                </div>
                <div class="campo">
                    <label for="rhEmail">Email</label>
                    <input type="email" id="rhEmail" name="rhEmail" maxlength="100" value="<c:out value='${inscricao.responsavelRH.email}'/>">
                </div>
            </div>
        </section>

        <!-- BOTÕES -->
        <div class="card">
            <div class="btn-grupo">
                <button type="submit" class="btn btn-primario">💾 Guardar Alterações</button>
                <a href="${pageContext.request.contextPath}/listagem" class="btn btn-secundario">❌ Cancelar</a>
            </div>
        </div>
    </form>
</main>
<script src="${pageContext.request.contextPath}/js/validation.js"></script>
</body>
</html>

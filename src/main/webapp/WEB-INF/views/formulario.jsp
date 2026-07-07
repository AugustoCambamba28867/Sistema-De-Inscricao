<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Ficha de Inscrição GET Training Academy Center – Registe-se num curso de formação profissional.">
    <title>Ficha de Inscrição – GET Training Academy</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<!-- ===== CABEÇALHO ===== -->
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

<!-- ===== NAVEGAÇÃO ===== -->
<nav class="nav-bar">
    <a href="${pageContext.request.contextPath}/inscricao" class="activo" id="nav-inscricao">📝 Nova Inscrição</a>
    <a href="${pageContext.request.contextPath}/listagem" id="nav-listagem">📋 Listagem</a>
</nav>

<!-- ===== CONTEÚDO ===== -->
<main class="conteudo">

    <h1 style="font-size:24px;font-weight:700;color:#333;margin-bottom:24px;">Ficha de Inscrição</h1>

    <%-- Mensagens de erro vindas do servidor --%>
    <c:if test="${not empty erros}">
        <div class="alerta alerta-erro" role="alert" id="erroServidor">
            <strong>⚠ Por favor corrija os seguintes erros:</strong><br>
            ${erros}
        </div>
    </c:if>

    <form id="formInscricao" action="${pageContext.request.contextPath}/inscricao"
          method="post" novalidate>

        <!-- ===== SECÇÃO: CURSO ===== -->
        <section class="card" id="seccao-curso">
            <span class="secao-titulo">Curso</span>

            <div class="form-linha col-1">
                <div class="campo">
                    <label for="curso">Nome do Curso <span class="obrig">*</span></label>
                    <input type="text" id="curso" name="curso" maxlength="150"
                           placeholder="Ex: Gestão de Recursos Humanos"
                           value="<c:out value='${param.curso}'/>">
                </div>
            </div>

            <div class="campo">
                <label>Horário Preferencial <span class="obrig">*</span></label>
                <div class="radio-grupo" id="horarioDiv">
                    <label class="radio-opcao">
                        <input type="radio" name="horario" value="Manhã"
                               <c:if test="${param.horario == 'Manhã'}">checked</c:if>>
                        Manhã
                    </label>
                    <label class="radio-opcao">
                        <input type="radio" name="horario" value="Tarde"
                               <c:if test="${param.horario == 'Tarde'}">checked</c:if>>
                        Tarde
                    </label>
                    <label class="radio-opcao">
                        <input type="radio" name="horario" value="Fim de Tarde"
                               <c:if test="${param.horario == 'Fim de Tarde'}">checked</c:if>>
                        Fim de Tarde
                    </label>
                    <label class="radio-opcao">
                        <input type="radio" name="horario" value="Sábado"
                               <c:if test="${param.horario == 'Sábado'}">checked</c:if>>
                        Sábado
                    </label>
                </div>
            </div>
        </section>

        <!-- ===== SECÇÃO: DADOS DO FORMANDO ===== -->
        <section class="card" id="seccao-formando">
            <span class="secao-titulo">Dados do Formando</span>

            <div class="form-linha col-1">
                <div class="campo">
                    <label for="nome">Nome Completo <span class="obrig">*</span></label>
                    <input type="text" id="nome" name="nome" maxlength="150"
                           placeholder="Nome completo do formando"
                           value="<c:out value='${param.nome}'/>">
                </div>
            </div>

            <div class="form-linha col-1">
                <div class="campo">
                    <label for="morada">Morada</label>
                    <input type="text" id="morada" name="morada" maxlength="200"
                           placeholder="Rua, Bairro, Número"
                           value="<c:out value='${param.morada}'/>">
                </div>
            </div>

            <div class="form-linha col-2">
                <div class="campo">
                    <label for="localidade">Localidade</label>
                    <input type="text" id="localidade" name="localidade" maxlength="100"
                           placeholder="Localidade"
                           value="<c:out value='${param.localidade}'/>">
                </div>
                <div class="campo">
                    <label for="municipio">Município</label>
                    <input type="text" id="municipio" name="municipio" maxlength="100"
                           placeholder="Município"
                           value="<c:out value='${param.municipio}'/>">
                </div>
            </div>

            <div class="form-linha col-2">
                <div class="campo">
                    <label for="telefone">Telefone</label>
                    <input type="tel" id="telefone" name="telefone" maxlength="20"
                           placeholder="+244 9XX XXX XXX"
                           value="<c:out value='${param.telefone}'/>">
                </div>
                <div class="campo">
                    <label for="telemovel">Telemóvel</label>
                    <input type="tel" id="telemovel" name="telemovel" maxlength="20"
                           placeholder="+244 9XX XXX XXX"
                           value="<c:out value='${param.telemovel}'/>">
                </div>
            </div>

            <div class="form-linha col-1">
                <div class="campo">
                    <label for="email">Email <span class="obrig">*</span></label>
                    <input type="email" id="email" name="email" maxlength="100"
                           placeholder="exemplo@email.com"
                           value="<c:out value='${param.email}'/>">
                </div>
            </div>

            <div class="form-linha col-2">
                <div class="campo">
                    <label for="dataNascimento">Data de Nascimento</label>
                    <input type="date" id="dataNascimento" name="dataNascimento"
                           value="<c:out value='${param.dataNascimento}'/>">
                </div>
                <div class="campo">
                    <label>Sexo <span class="obrig">*</span></label>
                    <div class="radio-grupo" id="sexoDiv">
                        <label class="radio-opcao">
                            <input type="radio" name="sexo" value="M"
                                   <c:if test="${param.sexo == 'M'}">checked</c:if>> Masculino
                        </label>
                        <label class="radio-opcao">
                            <input type="radio" name="sexo" value="F"
                                   <c:if test="${param.sexo == 'F'}">checked</c:if>> Feminino
                        </label>
                    </div>
                </div>
            </div>
        </section>

        <!-- ===== SECÇÃO: ENTIDADE PAGADORA ===== -->
        <section class="card" id="seccao-entidade">
            <span class="secao-titulo">Entidade Pagadora</span>

            <div class="form-linha col-1">
                <div class="campo">
                    <label for="epNome">Nome da Entidade</label>
                    <input type="text" id="epNome" name="epNome" maxlength="150"
                           placeholder="Nome da empresa ou entidade"
                           value="<c:out value='${param.epNome}'/>">
                </div>
            </div>

            <div class="form-linha col-1">
                <div class="campo">
                    <label for="epMorada">Morada</label>
                    <input type="text" id="epMorada" name="epMorada" maxlength="200"
                           placeholder="Morada da entidade"
                           value="<c:out value='${param.epMorada}'/>">
                </div>
            </div>

            <div class="form-linha col-2">
                <div class="campo">
                    <label for="epLocalidade">Localidade</label>
                    <input type="text" id="epLocalidade" name="epLocalidade" maxlength="100"
                           placeholder="Localidade"
                           value="<c:out value='${param.epLocalidade}'/>">
                </div>
                <div class="campo">
                    <label for="epMunicipio">Município</label>
                    <input type="text" id="epMunicipio" name="epMunicipio" maxlength="100"
                           placeholder="Município"
                           value="<c:out value='${param.epMunicipio}'/>">
                </div>
            </div>

            <div class="form-linha col-3">
                <div class="campo">
                    <label for="epTelefone">Telefone</label>
                    <input type="tel" id="epTelefone" name="epTelefone" maxlength="20"
                           placeholder="+244 ..."
                           value="<c:out value='${param.epTelefone}'/>">
                </div>
                <div class="campo">
                    <label for="epTelemovel">Telemóvel</label>
                    <input type="tel" id="epTelemovel" name="epTelemovel" maxlength="20"
                           placeholder="+244 ..."
                           value="<c:out value='${param.epTelemovel}'/>">
                </div>
                <div class="campo">
                    <label for="epFax">Fax</label>
                    <input type="tel" id="epFax" name="epFax" maxlength="20"
                           placeholder="Fax"
                           value="<c:out value='${param.epFax}'/>">
                </div>
            </div>

            <div class="form-linha col-2">
                <div class="campo">
                    <label for="epEmail">Email</label>
                    <input type="email" id="epEmail" name="epEmail" maxlength="100"
                           placeholder="email@empresa.com"
                           value="<c:out value='${param.epEmail}'/>">
                </div>
                <div class="campo">
                    <label for="epNif">NIF</label>
                    <input type="text" id="epNif" name="epNif" maxlength="20"
                           placeholder="Número de Identificação Fiscal"
                           value="<c:out value='${param.epNif}'/>">
                </div>
            </div>
        </section>

        <!-- ===== SECÇÃO: RESPONSÁVEL RH ===== -->
        <section class="card" id="seccao-rh">
            <span class="secao-titulo">Responsável de RH</span>

            <div class="form-linha col-1">
                <div class="campo">
                    <label for="rhNome">Nome</label>
                    <input type="text" id="rhNome" name="rhNome" maxlength="150"
                           placeholder="Nome do Responsável de RH"
                           value="<c:out value='${param.rhNome}'/>">
                </div>
            </div>

            <div class="form-linha col-3">
                <div class="campo">
                    <label for="rhTelefone">Telefone</label>
                    <input type="tel" id="rhTelefone" name="rhTelefone" maxlength="20"
                           placeholder="+244 ..."
                           value="<c:out value='${param.rhTelefone}'/>">
                </div>
                <div class="campo">
                    <label for="rhTelemovel">Telemóvel</label>
                    <input type="tel" id="rhTelemovel" name="rhTelemovel" maxlength="20"
                           placeholder="+244 ..."
                           value="<c:out value='${param.rhTelemovel}'/>">
                </div>
                <div class="campo">
                    <label for="rhEmail">Email</label>
                    <input type="email" id="rhEmail" name="rhEmail" maxlength="100"
                           placeholder="rh@empresa.com"
                           value="<c:out value='${param.rhEmail}'/>">
                </div>
            </div>
        </section>

        <!-- ===== BOTÕES ===== -->
        <div class="card">
            <div class="btn-grupo">
                <button type="submit" class="btn btn-primario" id="btnGravar">
                    💾 Gravar Inscrição
                </button>
                <button type="button" onclick="preencherAutomatico()" class="btn" id="btnAutoFill"
                    style="background:linear-gradient(135deg,#059669 0%,#047857 100%);color:white;box-shadow:0 2px 8px rgba(5,150,105,.3);">
                    ⚡ Auto-Preencher (Teste)
                </button>
                <button type="reset" class="btn btn-secundario" id="btnLimpar">
                    🔄 Limpar
                </button>
                <a href="${pageContext.request.contextPath}/listagem"
                   class="btn btn-secundario" id="btnListagem">
                    📋 Ver Inscrições
                </a>
            </div>
        </div>

    </form>
</main>

<!-- ===== RODAPÉ ===== -->
<footer class="rodape">
    <p>&copy; 2026 GET Training Academy Center · geral@get-ao.com · www.get-ao.com</p>
</footer>

<script src="${pageContext.request.contextPath}/js/validation.js"></script>
<script>
// Auto-preenchimento com dados de teste (para demos e provas)
const nomesMasc = ['Augusto Cambamba','Carlos Manuel','João Pedro Silva','Miguel António','Fernando Costa','David Lopes','Rafael Neto'];
const nomesFem  = ['Ana Maria Sousa','Beatriz Santos','Maria Luísa','Vanessa Gomes','Catarina Ferreira','Diana Alves'];
const cursos    = ['Gestão de Recursos Humanos','Contabilidade e Gestão','Marketing Digital','Informática e Programação','Logística e Supply Chain','Finanças Empresariais'];
const horarios  = ['Manhã','Tarde','Fim de Tarde','Sábado'];
const municipios= ['Luanda','Viana','Cacuaco','Belas','Kilamba Kiaxi','Sambizanga'];

function selecionarRadio(nome, valor) {
    const radios = document.querySelectorAll(`input[name="${nome}"]`);
    radios.forEach(radio => {
        radio.checked = (radio.value === valor);
    });
}

function preencherAutomatico() {
    const isMasc = Math.random() > 0.45;
    const nomes  = isMasc ? nomesMasc : nomesFem;
    const nome   = nomes[Math.floor(Math.random() * nomes.length)];
    const curso  = cursos[Math.floor(Math.random() * cursos.length)];
    const horario= horarios[Math.floor(Math.random() * horarios.length)];
    const mun    = municipios[Math.floor(Math.random() * municipios.length)];
    const uid    = Date.now();

    // Curso
    document.getElementById('curso').value = curso;
    selecionarRadio('horario', horario);

    // Formando
    document.getElementById('nome').value = nome;
    document.getElementById('morada').value = 'Rua ' + Math.floor(Math.random()*200+1) + ', Bairro Central';
    document.getElementById('localidade').value = mun;
    document.getElementById('municipio').value = mun;
    document.getElementById('telefone').value = '+244 222 ' + String(Math.floor(Math.random()*900000+100000)).slice(0,3) + ' ' + String(Math.floor(Math.random()*9000+1000));
    document.getElementById('telemovel').value = '+244 9' + (isMasc ? '2' : '3') + String(Math.floor(Math.random()*9000000+1000000));
    document.getElementById('email').value = nome.toLowerCase().replace(/\s+/g,'.').replace(/[\u00e3\u00e7\u00e9\u00ea\u00ed\u00f5\u00fa]/g,'a') + uid + '@gmail.com';
    const ano = 1990 + Math.floor(Math.random()*20);
    const mes = String(Math.floor(Math.random()*12+1)).padStart(2,'0');
    const dia = String(Math.floor(Math.random()*28+1)).padStart(2,'0');
    document.getElementById('dataNascimento').value = `${ano}-${mes}-${dia}`;
    selecionarRadio('sexo', isMasc ? 'M' : 'F');

    // Entidade Pagadora
    document.getElementById('epNome').value = 'Empresa ' + mun + ' Lda.';
    document.getElementById('epMorada').value = 'Av. 1º Congresso, Nº ' + Math.floor(Math.random()*500+1);
    document.getElementById('epLocalidade').value = mun;
    document.getElementById('epMunicipio').value = mun;
    document.getElementById('epTelefone').value = '+244 222 ' + String(Math.floor(Math.random()*900000+100000)).slice(0,6);
    document.getElementById('epEmail').value = 'geral@empresa' + uid + '.co.ao';
    document.getElementById('epNif').value = String(Math.floor(Math.random()*9000000000+1000000000));

    // Resp. RH
    const rhNome = isMasc ? nomesFem[Math.floor(Math.random()*nomesFem.length)] : nomesMasc[Math.floor(Math.random()*nomesMasc.length)];
    document.getElementById('rhNome').value = rhNome;
    document.getElementById('rhEmail').value = rhNome.toLowerCase().replace(/\s+/g,'.')+'.rh@empresa'+uid+'.co.ao';
    document.getElementById('rhTelemovel').value = '+244 924 ' + String(Math.floor(Math.random()*900000+100000)).slice(0,3)+' '+String(Math.floor(Math.random()*9000+1000));

    // Visual feedback
    const btn = document.getElementById('btnAutoFill');
    if (btn) {
        btn.textContent = '✅ Preenchido!';
        setTimeout(() => { btn.textContent = '⚡ Auto-Preencher (Teste)'; }, 2000);
    }
}
</script>
</body>
</html>

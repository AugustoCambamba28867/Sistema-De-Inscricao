/**
 * GET Training – Validação do lado do cliente (JavaScript)
 * Complementa a validação do servidor (Servlet)
 */

document.addEventListener('DOMContentLoaded', function () {

    const form = document.getElementById('formInscricao');
    if (!form) return;

    form.addEventListener('submit', function (e) {
        limparErros();
        let valido = true;

        // ---- Curso ----
        const curso = document.getElementById('curso');
        if (!curso || curso.value.trim() === '') {
            mostrarErro(curso, 'O nome do curso é obrigatório.');
            valido = false;
        }

        // ---- Horário ----
        const horarios = document.querySelectorAll('input[name="horario"]');
        const algumHorario = Array.from(horarios).some(r => r.checked);
        if (!algumHorario) {
            const horarioDiv = document.getElementById('horarioDiv');
            mostrarErroDiv(horarioDiv, 'Seleccione um horário preferencial.');
            valido = false;
        }

        // ---- Nome do Formando ----
        const nome = document.getElementById('nome');
        if (!nome || nome.value.trim().length < 3) {
            mostrarErro(nome, 'O nome deve ter pelo menos 3 caracteres.');
            valido = false;
        }

        // ---- Email do Formando ----
        const email = document.getElementById('email');
        if (!email || !validarEmail(email.value.trim())) {
            mostrarErro(email, 'Introduza um email válido (ex: nome@dominio.com).');
            valido = false;
        }

        // ---- Telefone ----
        const telefone = document.getElementById('telefone');
        if (telefone && telefone.value.trim() !== '' && !validarTelefone(telefone.value.trim())) {
            mostrarErro(telefone, 'Telefone inválido (apenas dígitos, +, -, espaço).');
            valido = false;
        }

        // ---- Telemóvel ----
        const telemovel = document.getElementById('telemovel');
        if (telemovel && telemovel.value.trim() !== '' && !validarTelefone(telemovel.value.trim())) {
            mostrarErro(telemovel, 'Telemóvel inválido (apenas dígitos, +, -, espaço).');
            valido = false;
        }

        // ---- Data de Nascimento ----
        const dataNasc = document.getElementById('dataNascimento');
        if (dataNasc && dataNasc.value !== '') {
            const hoje = new Date();
            const nasc = new Date(dataNasc.value);
            if (nasc >= hoje) {
                mostrarErro(dataNasc, 'A data de nascimento não pode ser futura.');
                valido = false;
            }
        }

        // ---- Sexo ----
        const sexos = document.querySelectorAll('input[name="sexo"]');
        const algumSexo = Array.from(sexos).some(r => r.checked);
        if (!algumSexo) {
            const sexoDiv = document.getElementById('sexoDiv');
            mostrarErroDiv(sexoDiv, 'Seleccione o sexo.');
            valido = false;
        }

        // ---- Email da Entidade Pagadora (se preenchido) ----
        const epEmail = document.getElementById('epEmail');
        if (epEmail && epEmail.value.trim() !== '' && !validarEmail(epEmail.value.trim())) {
            mostrarErro(epEmail, 'Email da entidade pagadora inválido.');
            valido = false;
        }

        // ---- NIF da Entidade (se preenchido) ----
        const epNif = document.getElementById('epNif');
        if (epNif && epNif.value.trim() !== '' && epNif.value.trim().length < 9) {
            mostrarErro(epNif, 'O NIF deve ter pelo menos 9 dígitos.');
            valido = false;
        }

        // ---- Email do RH (se preenchido) ----
        const rhEmail = document.getElementById('rhEmail');
        if (rhEmail && rhEmail.value.trim() !== '' && !validarEmail(rhEmail.value.trim())) {
            mostrarErro(rhEmail, 'Email do Responsável RH inválido.');
            valido = false;
        }

        if (!valido) {
            e.preventDefault();
            // Rolar para o primeiro erro
            const primeiroErro = document.querySelector('.msg-erro-inline');
            if (primeiroErro) {
                primeiroErro.scrollIntoView({ behavior: 'smooth', block: 'center' });
            }
        }
    });

    // ---- Funções Auxiliares ----

    function validarEmail(email) {
        return /^[\w._%+\-]+@[\w.\-]+\.[a-zA-Z]{2,}$/.test(email);
    }

    function validarTelefone(tel) {
        return /^[\d\s\+\-]{7,20}$/.test(tel);
    }

    function mostrarErro(campo, mensagem) {
        if (!campo) return;
        campo.classList.add('invalido');
        const div = document.createElement('div');
        div.className = 'msg-erro-inline';
        div.style.cssText = 'color:#c62828;font-size:12px;margin-top:4px;';
        div.textContent = mensagem;
        campo.parentNode.appendChild(div);
    }

    function mostrarErroDiv(container, mensagem) {
        if (!container) return;
        const div = document.createElement('div');
        div.className = 'msg-erro-inline';
        div.style.cssText = 'color:#c62828;font-size:12px;margin-top:4px;';
        div.textContent = mensagem;
        container.appendChild(div);
    }

    function limparErros() {
        document.querySelectorAll('.invalido').forEach(el => el.classList.remove('invalido'));
        document.querySelectorAll('.msg-erro-inline').forEach(el => el.remove());
    }

    // ---- Feedback visual nos campos ----
    document.querySelectorAll('input, select').forEach(function (campo) {
        campo.addEventListener('blur', function () {
            if (this.value.trim() !== '') {
                this.style.borderColor = '#4CAF50';
            } else {
                this.style.borderColor = '';
            }
        });
        campo.addEventListener('focus', function () {
            this.style.borderColor = '';
        });
    });
});

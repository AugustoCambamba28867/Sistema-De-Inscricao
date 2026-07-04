package sdpfrequencia.visao;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import sdpfrequencia.modelo.FichaInscricao;
import sdpfrequencia.negocio.FichaInscricaoService;

/**
 * Painel de Inscrição em duas etapas (Wizard).
 * Etapa 1: Dados do Curso e Dados Pessoais do Formando.
 * Etapa 2: Entidade Pagadora e Responsável de RH.
 * 
 * Desenvolvido pelo Grupo 3.
 */
public class InscricaoPanel extends VBox {

    private final FichaInscricaoService service = new FichaInscricaoService();

    // ================= ESTILOS PREMIUM =================
    private final String FONT = "-fx-font-family: 'Segoe UI', Arial, sans-serif;";
    private final String CARD = "-fx-background-color: white; -fx-padding: 25; -fx-background-radius: 12; "
            + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 4); " + FONT;
    private final String INPUT = "-fx-background-color: #f8fafc; -fx-border-color: #e2e8f0; "
            + "-fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 8; -fx-text-fill: #334155; " + FONT;
    private final String LBL = "-fx-text-fill: #64748b; -fx-font-weight: bold; -fx-font-size: 13px; " + FONT;
    private final String SECTION = "-fx-text-fill: #1e293b; -fx-font-size: 15px; -fx-font-weight: bold; "
            + "-fx-border-width: 0 0 2 0; -fx-border-color: #e2e8f0; -fx-padding: 0 0 5 0; " + FONT;
    private final String BTN_PRIMARY = "-fx-background-color: #dc2626; -fx-text-fill: white; -fx-font-weight: bold; "
            + "-fx-padding: 12 30; -fx-background-radius: 6; -fx-cursor: hand; " + FONT;
    private final String BTN_SECONDARY = "-fx-background-color: #475569; -fx-text-fill: white; -fx-font-weight: bold; "
            + "-fx-padding: 12 30; -fx-background-radius: 6; -fx-cursor: hand; " + FONT;
    private final String BTN_OUTLINE = "-fx-background-color: transparent; -fx-text-fill: #475569; -fx-font-weight: bold; "
            + "-fx-padding: 12 30; -fx-background-radius: 6; -fx-border-color: #cbd5e1; "
            + "-fx-border-radius: 6; -fx-cursor: hand; " + FONT;

    // Área de conteúdo que troca entre etapas
    private StackPane areaConteudo;

    // Indicadores visuais do wizard
    private Circle circulo1, circulo2;
    private Label lblEtapa1, lblEtapa2;
    private Region linhaPasso;

    // ================= CAMPOS ETAPA 1 (CURSO + FORMANDO) =================
    private TextField curso;
    private ToggleGroup horarioGroup;
    private RadioButton rbManha, rbTarde, rbFimTarde, rbSabado;

    private TextField nome, morada, localidade, municipio, telefone, telemovel, email;
    private TextField dia, mes, ano;
    private ToggleGroup sexoGroup;
    private RadioButton masc, fem;

    // ================= CAMPOS ETAPA 2 (ENTIDADE + RH) =================
    private TextField epNome, epMorada, epLocal, epMun, epTel, epMov, epFax, epEmail, epNif;
    private TextField rhNome, rhTel, rhMov, rhEmail;

    public InscricaoPanel() {
        this.setSpacing(20);
        this.setPadding(new Insets(10, 30, 30, 30));
        this.setStyle("-fx-background-color: transparent;");

        // Título
        Label titulo = new Label("Nova Ficha de Inscrição");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #0f172a;" + FONT);

        // Wizard Step Indicator
        HBox stepIndicator = criarIndicadorEtapas();

        // Inicializar campos
        inicializarCamposEtapa1();
        inicializarCamposEtapa2();

        // Criar painéis de cada etapa
        VBox painelEtapa1 = criarEtapa1();
        VBox painelEtapa2 = criarEtapa2();

        // StackPane para trocar de etapa
        areaConteudo = new StackPane();
        areaConteudo.getChildren().addAll(painelEtapa2, painelEtapa1);

        // Botões de navegação
        Button btnProximo = new Button("PRÓXIMO  ➜");
        btnProximo.setStyle(BTN_PRIMARY);
        btnProximo.setPrefWidth(200);

        Button btnAnterior = new Button("⬅  ANTERIOR");
        btnAnterior.setStyle(BTN_OUTLINE);
        btnAnterior.setPrefWidth(200);
        btnAnterior.setVisible(false);
        btnAnterior.setManaged(false);

        Button btnGravar = new Button("✔  GRAVAR INSCRIÇÃO");
        btnGravar.setStyle(BTN_PRIMARY);
        btnGravar.setPrefWidth(250);
        btnGravar.setVisible(false);
        btnGravar.setManaged(false);

        Button btnLimpar = new Button("LIMPAR CAMPOS");
        btnLimpar.setStyle(BTN_SECONDARY);
        btnLimpar.setPrefWidth(200);

        // Hover effects
        addHover(btnProximo, BTN_PRIMARY, "#dc2626", "#b91c1c");
        addHover(btnAnterior, BTN_OUTLINE, "#cbd5e1", "#94a3b8");
        addHover(btnGravar, BTN_PRIMARY, "#dc2626", "#b91c1c");
        addHover(btnLimpar, BTN_SECONDARY, "#475569", "#334155");

        // Espaçador entre botões
        Region espacador = new Region();
        HBox.setHgrow(espacador, Priority.ALWAYS);

        HBox barraNavegacao = new HBox(15, btnLimpar, espacador, btnAnterior, btnProximo, btnGravar);
        barraNavegacao.setAlignment(Pos.CENTER);
        barraNavegacao.setPadding(new Insets(5, 0, 0, 0));

        // ================= AÇÕES DE NAVEGAÇÃO =================
        btnProximo.setOnAction(ev -> {
            // Validar campos obrigatórios da etapa 1
            if (nome.getText().trim().isEmpty() || curso.getText().trim().isEmpty()) {
                alerta("Campos Obrigatórios", "Por favor, preencha pelo menos o Nome e o Curso antes de avançar.", Alert.AlertType.WARNING);
                return;
            }

            painelEtapa1.setVisible(false);
            painelEtapa2.setVisible(true);
            painelEtapa2.toFront();

            btnProximo.setVisible(false);
            btnProximo.setManaged(false);
            btnAnterior.setVisible(true);
            btnAnterior.setManaged(true);
            btnGravar.setVisible(true);
            btnGravar.setManaged(true);

            atualizarIndicador(2);
        });

        btnAnterior.setOnAction(ev -> {
            painelEtapa2.setVisible(false);
            painelEtapa1.setVisible(true);
            painelEtapa1.toFront();

            btnProximo.setVisible(true);
            btnProximo.setManaged(true);
            btnAnterior.setVisible(false);
            btnAnterior.setManaged(false);
            btnGravar.setVisible(false);
            btnGravar.setManaged(false);

            atualizarIndicador(1);
        });

        // ================= AÇÃO GRAVAR =================
        btnGravar.setOnAction(ev -> {
            FichaInscricao ficha = new FichaInscricao();

            // Curso
            ficha.setCurso(curso.getText());
            ficha.setHorario(horarioGroup.getSelectedToggle() != null
                    ? ((RadioButton) horarioGroup.getSelectedToggle()).getText() : "");

            // Formando
            ficha.setNome(nome.getText());
            ficha.setMorada(morada.getText());
            ficha.setLocalidade(localidade.getText());
            ficha.setMunicipio(municipio.getText());
            ficha.setTelefone(telefone.getText());
            ficha.setTelemovel(telemovel.getText());
            ficha.setEmail(email.getText());
            ficha.setNascimento(dia.getText() + "/" + mes.getText() + "/" + ano.getText());
            ficha.setSexo(sexoGroup.getSelectedToggle() != null
                    ? ((RadioButton) sexoGroup.getSelectedToggle()).getText() : "");

            // Entidade Pagadora
            ficha.setEpNome(epNome.getText());
            ficha.setEpMorada(epMorada.getText());
            ficha.setEpLocalidade(epLocal.getText());
            ficha.setEpMunicipio(epMun.getText());
            ficha.setEpTelefone(epTel.getText());
            ficha.setEpTelemovel(epMov.getText());
            ficha.setEpFax(epFax.getText());
            ficha.setEpEmail(epEmail.getText());
            ficha.setEpNif(epNif.getText());

            // Responsável RH
            ficha.setRhNome(rhNome.getText());
            ficha.setRhTelefone(rhTel.getText());
            ficha.setRhTelemovel(rhMov.getText());
            ficha.setRhEmail(rhEmail.getText());

            try {
                service.salvar(ficha);
                alerta("Sucesso", "✔ Ficha gravada com sucesso no sistema!", Alert.AlertType.INFORMATION);
                limparTudo();

                // Voltar à etapa 1
                painelEtapa2.setVisible(false);
                painelEtapa1.setVisible(true);
                painelEtapa1.toFront();
                btnProximo.setVisible(true);
                btnProximo.setManaged(true);
                btnAnterior.setVisible(false);
                btnAnterior.setManaged(false);
                btnGravar.setVisible(false);
                btnGravar.setManaged(false);
                atualizarIndicador(1);
            } catch (Exception ex) {
                alerta("Erro", "⚠ " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        // ================= AÇÃO LIMPAR =================
        btnLimpar.setOnAction(ev -> limparTudo());

        this.getChildren().addAll(titulo, stepIndicator, areaConteudo, barraNavegacao);
    }

    // ================= INDICADOR DE ETAPAS (WIZARD) =================
    private HBox criarIndicadorEtapas() {
        circulo1 = new Circle(18);
        circulo1.setFill(Color.web("#dc2626"));
        circulo1.setStroke(Color.web("#dc2626"));
        circulo1.setStrokeWidth(2);
        Label num1 = new Label("1");
        num1.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        StackPane step1Circle = new StackPane(circulo1, num1);

        lblEtapa1 = new Label("Curso e Formando");
        lblEtapa1.setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold; -fx-font-size: 13px;" + FONT);

        VBox step1Box = new VBox(5, step1Circle, lblEtapa1);
        step1Box.setAlignment(Pos.CENTER);

        linhaPasso = new Region();
        linhaPasso.setStyle("-fx-background-color: #e2e8f0;");
        linhaPasso.setPrefHeight(3);
        linhaPasso.setPrefWidth(120);
        linhaPasso.setMaxWidth(200);
        HBox.setHgrow(linhaPasso, Priority.ALWAYS);

        circulo2 = new Circle(18);
        circulo2.setFill(Color.web("#f1f5f9"));
        circulo2.setStroke(Color.web("#cbd5e1"));
        circulo2.setStrokeWidth(2);
        Label num2 = new Label("2");
        num2.setStyle("-fx-text-fill: #94a3b8; -fx-font-weight: bold; -fx-font-size: 14px;");
        StackPane step2Circle = new StackPane(circulo2, num2);

        lblEtapa2 = new Label("Entidade e RH");
        lblEtapa2.setStyle("-fx-text-fill: #94a3b8; -fx-font-weight: bold; -fx-font-size: 13px;" + FONT);

        VBox step2Box = new VBox(5, step2Circle, lblEtapa2);
        step2Box.setAlignment(Pos.CENTER);

        HBox indicador = new HBox(15, step1Box, linhaPasso, step2Box);
        indicador.setAlignment(Pos.CENTER);
        indicador.setPadding(new Insets(10, 50, 10, 50));
        indicador.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.03), 8, 0, 0, 3); " + FONT);

        return indicador;
    }

    private void atualizarIndicador(int etapaAtual) {
        if (etapaAtual == 1) {
            circulo1.setFill(Color.web("#dc2626"));
            circulo1.setStroke(Color.web("#dc2626"));
            lblEtapa1.setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold; -fx-font-size: 13px;" + FONT);

            circulo2.setFill(Color.web("#f1f5f9"));
            circulo2.setStroke(Color.web("#cbd5e1"));
            lblEtapa2.setStyle("-fx-text-fill: #94a3b8; -fx-font-weight: bold; -fx-font-size: 13px;" + FONT);

            linhaPasso.setStyle("-fx-background-color: #e2e8f0;");
        } else {
            circulo1.setFill(Color.web("#16a34a"));
            circulo1.setStroke(Color.web("#16a34a"));
            lblEtapa1.setStyle("-fx-text-fill: #16a34a; -fx-font-weight: bold; -fx-font-size: 13px;" + FONT);

            circulo2.setFill(Color.web("#dc2626"));
            circulo2.setStroke(Color.web("#dc2626"));
            lblEtapa2.setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold; -fx-font-size: 13px;" + FONT);

            linhaPasso.setStyle("-fx-background-color: #16a34a;");
        }
    }

    // ================= INICIALIZAÇÃO DE CAMPOS =================
    private void inicializarCamposEtapa1() {
        curso = campo("Nome do Curso");
        horarioGroup = new ToggleGroup();
        rbManha = radio("Manhã", horarioGroup);
        rbTarde = radio("Tarde", horarioGroup);
        rbFimTarde = radio("Fim de Tarde", horarioGroup);
        rbSabado = radio("Sábado", horarioGroup);

        nome = campo("Nome completo");
        morada = campo("Sua morada");
        localidade = campo("Localidade");
        municipio = campo("Município");
        telefone = campo("Telefone");
        telemovel = campo("Telemóvel");
        email = campo("exemplo@email.com");

        dia = campo("DD"); dia.setPrefWidth(55); dia.setMaxWidth(55);
        mes = campo("MM"); mes.setPrefWidth(55); mes.setMaxWidth(55);
        ano = campo("AAAA"); ano.setPrefWidth(75); ano.setMaxWidth(75);

        sexoGroup = new ToggleGroup();
        masc = radio("Masculino", sexoGroup);
        fem = radio("Feminino", sexoGroup);
    }

    private void inicializarCamposEtapa2() {
        epNome = campo("Nome da Entidade");
        epMorada = campo("Morada da Entidade");
        epLocal = campo("Localidade");
        epMun = campo("Município");
        epTel = campo("Telefone");
        epMov = campo("Telemóvel");
        epFax = campo("Fax");
        epEmail = campo("Email corporativo");
        epNif = campo("NIF");

        rhNome = campo("Nome do Responsável");
        rhTel = campo("Telefone");
        rhMov = campo("Telemóvel");
        rhEmail = campo("Email");
    }

    // ================= ETAPA 1: CURSO + FORMANDO =================
    private VBox criarEtapa1() {
        // Card Curso
        VBox cursoBox = card("INFORMAÇÕES DO CURSO",
                new VBox(8, lbl("Nome do Curso"), curso),
                new VBox(8, lbl("Horário Preferencial:"), new HBox(20, rbManha, rbTarde, rbFimTarde, rbSabado))
        );

        // Card Formando
        GridPane gFormando = grid();
        addRow(gFormando, "Nome:", nome, 0);
        addRow(gFormando, "Morada:", morada, 1);
        addRow2(gFormando, "Localidade:", localidade, "Município:", municipio, 2);
        addRow2(gFormando, "Telefone:", telefone, "Telemóvel:", telemovel, 3);
        addRow(gFormando, "Email:", email, 4);

        Label lblNasc = lbl("Data Nasc.:");
        gFormando.add(lblNasc, 0, 5);
        HBox nascBox = new HBox(5, dia, new Label("/"), mes, new Label("/"), ano);
        nascBox.setAlignment(Pos.CENTER_LEFT);
        gFormando.add(nascBox, 1, 5);

        Label lblSexo = lbl("Sexo:");
        gFormando.add(lblSexo, 2, 5);
        gFormando.add(new HBox(15, masc, fem), 3, 5);

        VBox formandoBox = card("DADOS DO FORMANDO", gFormando);

        // Dica visual
        Label dica = new Label("⚡ Preencha os dados acima e clique em PRÓXIMO para continuar.");
        dica.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 12px; -fx-font-style: italic;" + FONT);

        VBox painel = new VBox(20, cursoBox, formandoBox, dica);
        return painel;
    }

    // ================= ETAPA 2: ENTIDADE + RH =================
    private VBox criarEtapa2() {
        // Card Entidade Pagadora
        GridPane gpEntidade = grid();
        addRow(gpEntidade, "Nome:", epNome, 0);
        addRow(gpEntidade, "Morada:", epMorada, 1);
        addRow2(gpEntidade, "Localidade:", epLocal, "Município:", epMun, 2);
        addRow3(gpEntidade, "Telefone:", epTel, "Telemóvel:", epMov, "Fax:", epFax, 3);
        addRow2(gpEntidade, "Email:", epEmail, "NIF:", epNif, 4);

        VBox entidadeBox = card("ENTIDADE PAGADORA", gpEntidade);

        // Card Responsável RH
        GridPane gpRh = grid();
        addRow(gpRh, "Nome:", rhNome, 0);
        addRow2(gpRh, "Telefone:", rhTel, "Telemóvel:", rhMov, 1);
        addRow(gpRh, "Email:", rhEmail, 2);

        VBox rhBox = card("RESPONSÁVEL DE RECURSOS HUMANOS", gpRh);

        // Dica visual
        Label dica = new Label("⚡ Preencha os dados da entidade pagadora e clique em GRAVAR INSCRIÇÃO para concluir.");
        dica.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 12px; -fx-font-style: italic;" + FONT);

        VBox painel = new VBox(20, entidadeBox, rhBox, dica);
        painel.setVisible(false);
        return painel;
    }

    // ================= MÉTODOS AUXILIARES UI =================

    private GridPane grid() {
        GridPane g = new GridPane();
        g.setHgap(15);
        g.setVgap(15);
        return g;
    }

    private void addRow(GridPane g, String labelText, TextField field, int row) {
        g.add(lbl(labelText), 0, row);
        g.add(field, 1, row, 3, 1);
    }

    private void addRow2(GridPane g, String l1, TextField f1, String l2, TextField f2, int row) {
        g.add(lbl(l1), 0, row);
        g.add(f1, 1, row);
        g.add(lbl(l2), 2, row);
        g.add(f2, 3, row);
    }

    private void addRow3(GridPane g, String l1, TextField f1, String l2, TextField f2, String l3, TextField f3, int row) {
        g.add(lbl(l1), 0, row);
        g.add(f1, 1, row);
        g.add(lbl(l2), 2, row);
        g.add(f2, 3, row);
        g.add(lbl(l3), 4, row);
        g.add(f3, 5, row);
    }

    private TextField campo(String prompt) {
        TextField t = new TextField();
        t.setPromptText(prompt);
        t.setStyle(INPUT);
        t.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                t.setStyle(INPUT + "-fx-border-color: #3b82f6; -fx-background-color: white;");
            } else {
                t.setStyle(INPUT);
            }
        });
        return t;
    }

    private Label lbl(String text) {
        Label l = new Label(text);
        l.setStyle(LBL);
        return l;
    }

    private RadioButton radio(String text, ToggleGroup g) {
        RadioButton r = new RadioButton(text);
        r.setToggleGroup(g);
        r.setStyle("-fx-text-fill: #475569; " + FONT);
        return r;
    }

    private VBox card(String tituloText, javafx.scene.Node... nodes) {
        Label title = new Label(tituloText);
        title.setStyle(SECTION);
        title.setMaxWidth(Double.MAX_VALUE);

        VBox content = new VBox(15, nodes);
        VBox root = new VBox(15, title, content);
        root.setStyle(CARD);
        return root;
    }

    private void addHover(Button btn, String base, String corOriginal, String corHover) {
        btn.setOnMouseEntered(e -> btn.setStyle(base.replace(corOriginal, corHover)));
        btn.setOnMouseExited(e -> btn.setStyle(base));
    }

    private void alerta(String titulo, String mensagem, Alert.AlertType type) {
        Alert a = new Alert(type);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(mensagem);
        a.showAndWait();
    }

    private void limparTudo() {
        curso.clear(); nome.clear(); morada.clear(); localidade.clear(); municipio.clear();
        telefone.clear(); telemovel.clear(); email.clear(); dia.clear(); mes.clear(); ano.clear();
        epNome.clear(); epMorada.clear(); epLocal.clear(); epMun.clear();
        epTel.clear(); epMov.clear(); epFax.clear(); epEmail.clear(); epNif.clear();
        rhNome.clear(); rhTel.clear(); rhMov.clear(); rhEmail.clear();

        if (horarioGroup.getSelectedToggle() != null) {
            horarioGroup.getSelectedToggle().setSelected(false);
        }
        if (sexoGroup.getSelectedToggle() != null) {
            sexoGroup.getSelectedToggle().setSelected(false);
        }
    }
}

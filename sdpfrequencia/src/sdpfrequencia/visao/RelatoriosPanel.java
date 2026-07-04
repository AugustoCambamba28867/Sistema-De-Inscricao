package sdpfrequencia.visao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import sdpfrequencia.modelo.FichaInscricao;
import sdpfrequencia.negocio.FichaInscricaoService;

/**
 * Painel de Relatórios com TableView profissional.
 * Permite visualizar todas as fichas gravadas no ficheiro binário (dados.dat),
 * pesquisar por nome e ver os detalhes completos de cada ficha.
 * 
 * Desenvolvido pelo Grupo 3.
 */
public class RelatoriosPanel extends VBox {

    private final FichaInscricaoService service = new FichaInscricaoService();
    private final String FONT = "-fx-font-family: 'Segoe UI', Arial, sans-serif;";
    private TableView<FichaInscricao> tabela;
    private ObservableList<FichaInscricao> masterData = FXCollections.observableArrayList();
    private TextArea areaDetalhes;

    public RelatoriosPanel() {
        this.setSpacing(20);
        this.setPadding(new Insets(10, 30, 30, 30));
        this.setStyle("-fx-background-color: transparent;");

        Label titulo = new Label("Relatório Geral de Inscrições");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #0f172a;" + FONT);

        Label subtitulo = new Label("Dados lidos do ficheiro binário de acesso aleatório (dados.dat)");
        subtitulo.setStyle("-fx-font-size: 13px; -fx-text-fill: #94a3b8; -fx-font-style: italic;" + FONT);

        // ================= BARRA DE FERRAMENTAS =================
        TextField txtBusca = new TextField();
        txtBusca.setPromptText("🔍 Pesquisar por nome do formando...");
        txtBusca.setStyle("-fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-radius: 6; " +
                "-fx-background-radius: 6; -fx-padding: 10;" + FONT);
        HBox.setHgrow(txtBusca, Priority.ALWAYS);

        Button btnRefresh = new Button("🔄 Atualizar");
        btnRefresh.setStyle("-fx-background-color: #f1f5f9; -fx-border-color: #cbd5e1; -fx-border-radius: 6; " +
                "-fx-cursor: hand; -fx-padding: 10 18; -fx-font-weight: bold;" + FONT);
        btnRefresh.setOnAction(e -> carregarDados());
        btnRefresh.setOnMouseEntered(e -> btnRefresh.setStyle("-fx-background-color: #e2e8f0; -fx-border-color: #94a3b8; " +
                "-fx-border-radius: 6; -fx-cursor: hand; -fx-padding: 10 18; -fx-font-weight: bold;" + FONT));
        btnRefresh.setOnMouseExited(e -> btnRefresh.setStyle("-fx-background-color: #f1f5f9; -fx-border-color: #cbd5e1; " +
                "-fx-border-radius: 6; -fx-cursor: hand; -fx-padding: 10 18; -fx-font-weight: bold;" + FONT));

        // Label com contagem
        Label lblContagem = new Label("Total: 0 fichas");
        lblContagem.setStyle("-fx-text-fill: #64748b; -fx-font-size: 13px; -fx-font-weight: bold;" + FONT);

        HBox toolBar = new HBox(15, txtBusca, btnRefresh, lblContagem);
        toolBar.setAlignment(Pos.CENTER_LEFT);
        toolBar.setPadding(new Insets(10, 0, 5, 0));

        // ================= TABELA PRINCIPAL =================
        tabela = new TableView<>();
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabela.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #e2e8f0;");
        tabela.setPlaceholder(new Label("Nenhuma ficha encontrada. Clique em 🔄 Atualizar."));

        TableColumn<FichaInscricao, String> colNome = coluna("Formando", "nome", 180);
        TableColumn<FichaInscricao, String> colCurso = coluna("Curso", "curso", 140);
        TableColumn<FichaInscricao, String> colHorario = coluna("Horário", "horario", 90);
        TableColumn<FichaInscricao, String> colEmail = coluna("Email", "email", 160);
        TableColumn<FichaInscricao, String> colTel = coluna("Telefone", "telefone", 100);
        TableColumn<FichaInscricao, String> colMov = coluna("Telemóvel", "telemovel", 100);
        TableColumn<FichaInscricao, String> colMun = coluna("Município", "municipio", 100);
        TableColumn<FichaInscricao, String> colSexo = coluna("Sexo", "sexo", 80);
        TableColumn<FichaInscricao, String> colNasc = coluna("Nascimento", "nascimento", 100);
        TableColumn<FichaInscricao, String> colEpNome = coluna("Entidade", "epNome", 150);
        TableColumn<FichaInscricao, String> colRhNome = coluna("Resp. RH", "rhNome", 130);

        tabela.getColumns().addAll(colNome, colCurso, colHorario, colEmail, colTel, colMov,
                colMun, colSexo, colNasc, colEpNome, colRhNome);

        // ================= ÁREA DE DETALHES =================
        Label lblDetalhes = new Label("📋 Detalhes da Ficha Selecionada");
        lblDetalhes.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1e293b;" + FONT);

        areaDetalhes = new TextArea("Selecione uma ficha na tabela acima para ver todos os detalhes.");
        areaDetalhes.setEditable(false);
        areaDetalhes.setPrefRowCount(10);
        areaDetalhes.setWrapText(true);
        areaDetalhes.setStyle("-fx-control-inner-background: #f8fafc; -fx-font-family: 'Consolas', monospace; " +
                "-fx-font-size: 13px; -fx-padding: 10; -fx-border-color: #e2e8f0; -fx-border-radius: 8; " +
                "-fx-background-radius: 8;");

        VBox detalhesBox = new VBox(8, lblDetalhes, areaDetalhes);
        detalhesBox.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 12; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.04), 8, 0, 0, 3);");

        // ================= EVENTO DE SELEÇÃO NA TABELA =================
        tabela.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                exibirDetalhes(newVal);
            }
        });

        // ================= FILTRO DE PESQUISA =================
        FilteredList<FichaInscricao> filteredData = new FilteredList<>(masterData, p -> true);
        txtBusca.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(ficha -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String filtro = newValue.toLowerCase();
                return (ficha.getNome() != null && ficha.getNome().toLowerCase().contains(filtro))
                        || (ficha.getCurso() != null && ficha.getCurso().toLowerCase().contains(filtro))
                        || (ficha.getEpNome() != null && ficha.getEpNome().toLowerCase().contains(filtro));
            });
        });
        tabela.setItems(filteredData);

        // Atualizar contagem ao carregar
        masterData.addListener((javafx.collections.ListChangeListener<FichaInscricao>) c -> {
            lblContagem.setText("Total: " + masterData.size() + " ficha(s)");
        });

        carregarDados();

        this.getChildren().addAll(titulo, subtitulo, toolBar, tabela, detalhesBox);
        VBox.setVgrow(tabela, Priority.ALWAYS);
    }

    // ================= EXIBIR TODOS OS DETALHES DA FICHA =================
    private void exibirDetalhes(FichaInscricao f) {
        StringBuilder sb = new StringBuilder();

        sb.append("╔══════════════════════════════════════════════════════╗\n");
        sb.append("║           FICHA DE INSCRIÇÃO COMPLETA               ║\n");
        sb.append("╚══════════════════════════════════════════════════════╝\n\n");

        sb.append("▶ DADOS DO CURSO\n");
        sb.append("  Curso............: ").append(safe(f.getCurso())).append("\n");
        sb.append("  Horário..........: ").append(safe(f.getHorario())).append("\n\n");

        sb.append("▶ DADOS DO FORMANDO\n");
        sb.append("  Nome.............: ").append(safe(f.getNome())).append("\n");
        sb.append("  Morada...........: ").append(safe(f.getMorada())).append("\n");
        sb.append("  Localidade.......: ").append(safe(f.getLocalidade())).append("\n");
        sb.append("  Município........: ").append(safe(f.getMunicipio())).append("\n");
        sb.append("  Telefone.........: ").append(safe(f.getTelefone())).append("\n");
        sb.append("  Telemóvel........: ").append(safe(f.getTelemovel())).append("\n");
        sb.append("  Email............: ").append(safe(f.getEmail())).append("\n");
        sb.append("  Data Nascimento..: ").append(safe(f.getNascimento())).append("\n");
        sb.append("  Sexo.............: ").append(safe(f.getSexo())).append("\n\n");

        sb.append("▶ ENTIDADE PAGADORA\n");
        sb.append("  Nome.............: ").append(safe(f.getEpNome())).append("\n");
        sb.append("  Morada...........: ").append(safe(f.getEpMorada())).append("\n");
        sb.append("  Localidade.......: ").append(safe(f.getEpLocalidade())).append("\n");
        sb.append("  Município........: ").append(safe(f.getEpMunicipio())).append("\n");
        sb.append("  Telefone.........: ").append(safe(f.getEpTelefone())).append("\n");
        sb.append("  Telemóvel........: ").append(safe(f.getEpTelemovel())).append("\n");
        sb.append("  Fax..............: ").append(safe(f.getEpFax())).append("\n");
        sb.append("  Email............: ").append(safe(f.getEpEmail())).append("\n");
        sb.append("  NIF..............: ").append(safe(f.getEpNif())).append("\n\n");

        sb.append("▶ RESPONSÁVEL DE RECURSOS HUMANOS\n");
        sb.append("  Nome.............: ").append(safe(f.getRhNome())).append("\n");
        sb.append("  Telefone.........: ").append(safe(f.getRhTelefone())).append("\n");
        sb.append("  Telemóvel........: ").append(safe(f.getRhTelemovel())).append("\n");
        sb.append("  Email............: ").append(safe(f.getRhEmail())).append("\n");

        areaDetalhes.setText(sb.toString());
        areaDetalhes.setStyle("-fx-control-inner-background: #f0fdf4; -fx-font-family: 'Consolas', monospace; " +
                "-fx-font-size: 13px; -fx-padding: 10; -fx-border-color: #bbf7d0; -fx-border-radius: 8; " +
                "-fx-background-radius: 8;");
    }

    private String safe(String s) {
        return (s == null || s.trim().isEmpty()) ? "—" : s;
    }

    // ================= MÉTODOS AUXILIARES =================
    private TableColumn<FichaInscricao, String> coluna(String titulo, String propriedade, int largura) {
        TableColumn<FichaInscricao, String> col = new TableColumn<>(titulo);
        col.setCellValueFactory(new PropertyValueFactory<>(propriedade));
        col.setPrefWidth(largura);
        return col;
    }

    private void carregarDados() {
        try {
            masterData.setAll(service.listarTodas());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Erro ao carregar dados do ficheiro binário: " + e.getMessage());
            alert.showAndWait();
        }
    }
}

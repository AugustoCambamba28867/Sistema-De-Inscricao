package sdpfrequencia;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import sdpfrequencia.modelo.FichaInscricao;
import sdpfrequencia.negocio.FichaInscricaoService;

import java.util.List;

public class HelloApplication extends Application {

    private final FichaInscricaoService service = new FichaInscricaoService();
    private TextArea area = new TextArea();

    // ================= ESTILOS PREMIUM =================
    private final String FONT_FAMILY = "-fx-font-family: 'Segoe UI', Arial, sans-serif;";
    private final String BG_COLOR = "-fx-background-color: #f8fafc;";
    private final String CARD_STYLE = "-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 12; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 4); " + FONT_FAMILY;
    private final String INPUT_STYLE = "-fx-background-color: #f1f5f9; -fx-border-color: #e2e8f0; " +
            "-fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 8; -fx-text-fill: #334155; " + FONT_FAMILY;
    private final String LABEL_STYLE = "-fx-text-fill: #475569; -fx-font-weight: bold; " + FONT_FAMILY;
    private final String SECTION_TITLE_STYLE = "-fx-text-fill: #1e293b; -fx-font-size: 16px; -fx-font-weight: bold; " +
            "-fx-border-width: 0 0 2 0; -fx-border-color: #e2e8f0; -fx-padding: 0 0 5 0; " + FONT_FAMILY;
    private final String PRIMARY_BTN_STYLE = "-fx-background-color: #dc2626; -fx-text-fill: white; -fx-font-weight: bold; " +
            "-fx-padding: 10 24; -fx-background-radius: 6; -fx-cursor: hand; " + FONT_FAMILY;
    private final String SECONDARY_BTN_STYLE = "-fx-background-color: #475569; -fx-text-fill: white; -fx-font-weight: bold; " +
            "-fx-padding: 10 24; -fx-background-radius: 6; -fx-cursor: hand; " + FONT_FAMILY;

    @Override
    public void start(Stage stage) {

        Label titulo = new Label("FICHA DE INSCRIÇÃO");
        titulo.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #0f172a; " + FONT_FAMILY);

        // ================= CURSO =================
        TextField curso = campo("Nome do Curso");
        ToggleGroup horarioGroup = new ToggleGroup();
        RadioButton m = radio("Manhã", horarioGroup);
        RadioButton t = radio("Tarde", horarioGroup);
        RadioButton f = radio("Fim de Tarde", horarioGroup);
        RadioButton s = radio("Sábado", horarioGroup);

        VBox cursoBox = card("INFORMAÇÕES DO CURSO", 
                new HBox(10, new Label("Curso:"), curso),
                new Label("Horário Preferencial:"),
                new HBox(20, m, t, f, s)
        );

        // ================= FORMANDO =================
        TextField nome = campo("Nome completo");
        TextField morada = campo("Sua morada");
        TextField localidade = campo("Localidade");
        TextField municipio = campo("Município");
        TextField telefone = campo("Telefone");
        TextField telemovel = campo("Telemóvel");
        TextField email = campo("exemplo@email.com");

        TextField d = campo("DD"); d.setPrefWidth(50);
        TextField mo = campo("MM"); mo.setPrefWidth(50);
        TextField a = campo("AAAA"); a.setPrefWidth(80);

        ToggleGroup sexoGroup = new ToggleGroup();
        RadioButton masc = radio("Masculino", sexoGroup);
        RadioButton fem = radio("Feminino", sexoGroup);

        GridPane g = grid();
        add(g, "Nome:", nome, 0);
        add(g, "Morada:", morada, 1);
        add2(g, "Localidade:", localidade, "Município:", municipio, 2);
        add2(g, "Telefone:", telefone, "Telemóvel:", telemovel, 3);
        add(g, "Email:", email, 4);
        
        Label lblNasc = new Label("Data Nasc.:"); lblNasc.setStyle(LABEL_STYLE);
        g.add(lblNasc, 0, 5);
        g.add(new HBox(8, d, new Label("/"), mo, new Label("/"), a), 1, 5);
        
        Label lblSexo = new Label("Sexo:"); lblSexo.setStyle(LABEL_STYLE);
        g.add(lblSexo, 2, 5);
        g.add(new HBox(15, masc, fem), 3, 5);

        VBox formandoBox = card("DADOS DO FORMANDO", g);

        // ================= ENTIDADE =================
        TextField epNome = campo("Nome da Entidade");
        TextField epMorada = campo("Morada da Entidade");
        TextField epLocal = campo("Localidade");
        TextField epMun = campo("Município");
        TextField epTel = campo("Telefone");
        TextField epMov = campo("Telemóvel");
        TextField epFax = campo("Fax");
        TextField epEmail = campo("Email corporativo");
        TextField epNif = campo("NIF");

        GridPane ep = grid();
        add(ep, "Nome:", epNome, 0);
        add(ep, "Morada:", epMorada, 1);
        add2(ep, "Localidade:", epLocal, "Município:", epMun, 2);
        add3(ep, "Telefone:", epTel, "Telemóvel:", epMov, "Fax:", epFax, 3);
        add2(ep, "Email:", epEmail, "NIF:", epNif, 4);

        VBox epBox = card("ENTIDADE PAGADORA", ep);

        // ================= RH =================
        TextField rhNome = campo("Nome do Responsável");
        TextField rhTel = campo("Telefone");
        TextField rhMov = campo("Telemóvel");
        TextField rhEmail = campo("Email");

        GridPane rh = grid();
        add(rh, "Nome:", rhNome, 0);
        add2(rh, "Telefone:", rhTel, "Telemóvel:", rhMov, 1);
        add(rh, "Email:", rhEmail, 2);

        VBox rhBox = card("RESPONSÁVEL DE RECURSOS HUMANOS", rh);

        // ================= BOTÕES =================
        Button salvar = new Button("SALVAR FICHA");
        salvar.setStyle(PRIMARY_BTN_STYLE);
        
        Button listar = new Button("LISTAR FICHAS");
        listar.setStyle(SECONDARY_BTN_STYLE);

        // Animações Hover (Efeitos Premium)
        salvar.setOnMouseEntered(e -> salvar.setStyle(PRIMARY_BTN_STYLE.replace("#dc2626", "#b91c1c")));
        salvar.setOnMouseExited(e -> salvar.setStyle(PRIMARY_BTN_STYLE));
        listar.setOnMouseEntered(e -> listar.setStyle(SECONDARY_BTN_STYLE.replace("#475569", "#334155")));
        listar.setOnMouseExited(e -> listar.setStyle(SECONDARY_BTN_STYLE));

        // ================= AÇÕES (INTEGRAÇÃO NEGÓCIO) =================
        salvar.setOnAction(e -> {
            FichaInscricao ficha = new FichaInscricao();
            
            // Preencher Modelo
            ficha.setCurso(curso.getText());
            ficha.setHorario(horarioGroup.getSelectedToggle() != null ? ((RadioButton)horarioGroup.getSelectedToggle()).getText() : "");
            
            ficha.setNome(nome.getText());
            ficha.setMorada(morada.getText());
            ficha.setLocalidade(localidade.getText());
            ficha.setMunicipio(municipio.getText());
            ficha.setTelefone(telefone.getText());
            ficha.setTelemovel(telemovel.getText());
            ficha.setEmail(email.getText());
            ficha.setNascimento(d.getText() + "/" + mo.getText() + "/" + a.getText());
            ficha.setSexo(sexoGroup.getSelectedToggle() != null ? ((RadioButton)sexoGroup.getSelectedToggle()).getText() : "");
            
            ficha.setEpNome(epNome.getText());
            ficha.setEpMorada(epMorada.getText());
            ficha.setEpLocalidade(epLocal.getText());
            ficha.setEpMunicipio(epMun.getText());
            ficha.setEpTelefone(epTel.getText());
            ficha.setEpTelemovel(epMov.getText());
            ficha.setEpFax(epFax.getText());
            ficha.setEpEmail(epEmail.getText());
            ficha.setEpNif(epNif.getText());
            
            ficha.setRhNome(rhNome.getText());
            ficha.setRhTelefone(rhTel.getText());
            ficha.setRhTelemovel(rhMov.getText());
            ficha.setRhEmail(rhEmail.getText());

            try {
                service.salvar(ficha);
                msgSucesso("✔ Ficha guardada com sucesso no sistema!");
            } catch (Exception ex) {
                msgErro("⚠ " + ex.getMessage());
            }
        });

        listar.setOnAction(e -> {
            try {
                List<FichaInscricao> lista = service.listarTodas();
                StringBuilder sb = new StringBuilder();
                
                if(lista.isEmpty()) {
                    area.setText("Nenhuma ficha encontrada.");
                    return;
                }
                
                int count = 1;
                for(FichaInscricao itemFicha : lista) {
                    sb.append("==== FICHA #").append(count++).append(" ====\n");
                    sb.append("▶ CURSO: ").append(itemFicha.getCurso()).append(" | Horário: ").append(itemFicha.getHorario()).append("\n");
                    sb.append("▶ FORMANDO: ").append(itemFicha.getNome()).append(" | Email: ").append(itemFicha.getEmail()).append("\n");
                    sb.append("▶ ENTIDADE: ").append(itemFicha.getEpNome()).append(" | NIF: ").append(itemFicha.getEpNif()).append("\n");
                    sb.append("▶ RH: ").append(itemFicha.getRhNome()).append(" | Contato: ").append(itemFicha.getRhTelemovel()).append("\n\n");
                }
                area.setStyle("-fx-control-inner-background: #f8fafc; -fx-font-family: 'Consolas', monospace; -fx-font-size: 13px; -fx-padding: 10;");
                area.setText(sb.toString());
            } catch (Exception ex) {
                msgErro("⚠ " + ex.getMessage());
            }
        });

        area.setEditable(false);
        area.setPrefRowCount(10);
        area.setStyle("-fx-control-inner-background: #ffffff; -fx-border-color: #e2e8f0; -fx-border-radius: 6;");

        HBox btnBox = new HBox(15, salvar, listar);
        btnBox.setAlignment(Pos.CENTER_LEFT);

        VBox root = new VBox(25,
                criarCabecalho(),
                titulo,
                cursoBox,
                formandoBox,
                epBox,
                rhBox,
                btnBox,
                area
        );

        root.setPadding(new Insets(30));
        root.setStyle(BG_COLOR);

        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: #f8fafc; -fx-border-color: transparent;");

        Scene scene = new Scene(scroll, 850, 700);
        stage.setScene(scene);
        stage.setTitle("Get Training - Cadastro Elite");
        stage.show();
    }

    // ================= MÉTODOS AUXILIARES UI =================

    private TextField campo(String prompt){
        TextField t = new TextField();
        t.setStyle(INPUT_STYLE);
        t.setPromptText(prompt);
        // Efeito foco
        t.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) { t.setStyle(INPUT_STYLE + "-fx-border-color: #3b82f6; -fx-background-color: white;"); } 
            else { t.setStyle(INPUT_STYLE); }
        });
        return t;
    }

    private RadioButton radio(String txt, ToggleGroup g){
        RadioButton r = new RadioButton(txt);
        r.setToggleGroup(g);
        r.setStyle("-fx-text-fill: #334155; " + FONT_FAMILY);
        return r;
    }

    private GridPane grid(){
        GridPane g = new GridPane();
        g.setHgap(15); g.setVgap(15);
        return g;
    }

    private void add(GridPane g, String l, TextField f, int r){
        Label lbl = new Label(l); lbl.setStyle(LABEL_STYLE);
        g.add(lbl, 0, r);
        g.add(f, 1, r, 3, 1);
    }

    private void add2(GridPane g, String l1, TextField f1, String l2, TextField f2, int r){
        Label lbl1 = new Label(l1); lbl1.setStyle(LABEL_STYLE);
        Label lbl2 = new Label(l2); lbl2.setStyle(LABEL_STYLE);
        g.add(lbl1, 0, r); g.add(f1, 1, r);
        g.add(lbl2, 2, r); g.add(f2, 3, r);
    }

    private void add3(GridPane g, String l1, TextField f1, String l2, TextField f2, String l3, TextField f3, int r){
        Label lbl1 = new Label(l1); lbl1.setStyle(LABEL_STYLE);
        Label lbl2 = new Label(l2); lbl2.setStyle(LABEL_STYLE);
        Label lbl3 = new Label(l3); lbl3.setStyle(LABEL_STYLE);
        g.add(lbl1, 0, r); g.add(f1, 1, r);
        g.add(lbl2, 2, r); g.add(f2, 3, r);
        g.add(lbl3, 4, r); g.add(f3, 5, r);
    }

    private VBox card(String tituloText, javafx.scene.Node... nodes){
        Label title = new Label(tituloText);
        title.setStyle(SECTION_TITLE_STYLE);
        title.setMaxWidth(Double.MAX_VALUE);
        
        VBox content = new VBox(15, nodes);
        VBox v = new VBox(20, title, content);
        v.setStyle(CARD_STYLE);
        return v;
    }

    private void msgSucesso(String m){ 
        area.setStyle("-fx-control-inner-background: #dcfce7; -fx-text-fill: #166534; -fx-font-weight: bold; " + FONT_FAMILY);
        area.setText(m); 
    }
    
    private void msgErro(String m){ 
        area.setStyle("-fx-control-inner-background: #fee2e2; -fx-text-fill: #991b1b; -fx-font-weight: bold; " + FONT_FAMILY);
        area.setText(m); 
    }

    // ================= CABEÇALHO (Reaproveitado com melhorias) =================
    private HBox criarCabecalho() {
        Label logo = new Label("▮");
        logo.setStyle("-fx-text-fill:#dc2626; -fx-font-size:45px; -fx-font-weight:bold;");

        Label nome = new Label("GET ");
        nome.setStyle("-fx-font-size:28px; -fx-font-weight:bold; -fx-text-fill:#0f172a;");

        Label training = new Label("Training");
        training.setStyle("-fx-font-size:24px; -fx-text-fill:#dc2626; -fx-font-weight:bold;");

        VBox nomeBox = new VBox(-5, new HBox(nome, training), new Label("Academy Center"));
        nomeBox.setAlignment(Pos.CENTER_LEFT);

        HBox left = new HBox(15, logo, nomeBox);
        left.setAlignment(Pos.CENTER_LEFT);

        Label email = new Label("✉ geral@get-ao.com");
        Label site = new Label("🌐 www.get-ao.com");

        VBox right = new VBox(8, email, site);
        right.setAlignment(Pos.CENTER_RIGHT);
        right.setStyle("-fx-text-fill:#64748b; -fx-font-size:13px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(15, left, spacer, right);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 4);");

        return header;
    }
}

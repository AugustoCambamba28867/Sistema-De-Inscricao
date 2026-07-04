package sdpfrequencia;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import sdpfrequencia.visao.AdministracaoPanel;
import sdpfrequencia.visao.InscricaoPanel;
import sdpfrequencia.visao.RelatoriosPanel;

public class Launcher extends Application {

    private BorderPane root;
    private final String FONT_FAMILY = "-fx-font-family: 'Segoe UI', Arial, sans-serif;";
    private final String SIDEBAR_COLOR = "-fx-background-color: #0f172a;";
    private final String SIDEBAR_BTN_STYLE = "-fx-background-color: transparent; -fx-text-fill: #cbd5e1; -fx-font-size: 15px; -fx-font-weight: bold; -fx-padding: 15 25; -fx-alignment: center-left; -fx-cursor: hand; " + FONT_FAMILY;
    private final String SIDEBAR_BTN_ACTIVE = "-fx-background-color: #1e293b; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold; -fx-padding: 15 25; -fx-alignment: center-left; " + FONT_FAMILY;

    private Button currentBtn;

    @Override
    public void start(Stage stage) {
        root = new BorderPane();
        root.setStyle("-fx-background-color: #f8fafc;");

        // Sidebar
        VBox sidebar = criarSidebar();
        root.setLeft(sidebar);

        // Footer
        root.setBottom(criarFooter());

        // Conteúdo Inicial (Home)
        mostrarHome();

        Scene scene = new Scene(root, 1100, 700);
        stage.setScene(scene);
        stage.setTitle("GET Training - Gestão Elite v1.0");
        stage.show();
    }

    private VBox criarSidebar() {
        VBox sidebar = new VBox();
        sidebar.setStyle(SIDEBAR_COLOR);
        sidebar.setPrefWidth(260);
        sidebar.setPadding(new Insets(30, 0, 30, 0));

        // Logo
        Label logo = new Label("▮ GET Training");
        logo.setStyle("-fx-text-fill: #f97316; -fx-font-size: 22px; -fx-font-weight: bold; -fx-padding: 0 20 40 25;");

        Button btnHome = criarMenuBtn("🏠  Início");
        Button btnInscricao = criarMenuBtn("📝  Inscrição");
        Button btnRelatorios = criarMenuBtn("📊  Relatórios");
        Button btnAdmin = criarMenuBtn("⚙️  Administração");
        
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        
        Button btnSair = criarMenuBtn("🚪  Sair");

        // Eventos
        btnHome.setOnAction(e -> { setActive(btnHome); mostrarHome(); });
        btnInscricao.setOnAction(e -> { setActive(btnInscricao); root.setCenter(new InscricaoPanel()); });
        btnRelatorios.setOnAction(e -> { setActive(btnRelatorios); root.setCenter(new RelatoriosPanel()); });
        btnAdmin.setOnAction(e -> { setActive(btnAdmin); root.setCenter(new AdministracaoPanel()); });
        btnSair.setOnAction(e -> System.exit(0));

        setActive(btnHome); // Início ativo
        sidebar.getChildren().addAll(logo, btnHome, btnInscricao, btnRelatorios, btnAdmin, spacer, btnSair);
        return sidebar;
    }

    private Button criarMenuBtn(String text) {
        Button b = new Button(text);
        b.setStyle(SIDEBAR_BTN_STYLE);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setOnMouseEntered(e -> { if(b != currentBtn) b.setStyle(SIDEBAR_BTN_ACTIVE); });
        b.setOnMouseExited(e -> { if(b != currentBtn) b.setStyle(SIDEBAR_BTN_STYLE); });
        return b;
    }

    private void setActive(Button b) {
        if(currentBtn != null) currentBtn.setStyle(SIDEBAR_BTN_STYLE);
        currentBtn = b;
        currentBtn.setStyle(SIDEBAR_BTN_ACTIVE);
    }

    private void mostrarHome() {
        VBox home = new VBox(20);
        home.setAlignment(Pos.CENTER);
        home.setPadding(new Insets(50));
        
        Label l1 = new Label("Bem-vindo ao Sistema de Gestão");
        l1.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        
        Label l2 = new Label("Utilize o menu lateral para navegar entre os módulos.");
        l2.setStyle("-fx-font-size: 16px; -fx-text-fill: #64748b;");
        
        home.getChildren().addAll(l1, l2);
        root.setCenter(home);
    }

    private HBox criarFooter() {
        HBox footer = new HBox();
        footer.setPadding(new Insets(10, 25, 10, 25));
        footer.setStyle("-fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-width: 1 0 0 0;");
        Label l = new Label("© 2026 GET Training | Grupo 3 | v1.0.0");
        l.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 12px;");
        footer.getChildren().add(l);
        return footer;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
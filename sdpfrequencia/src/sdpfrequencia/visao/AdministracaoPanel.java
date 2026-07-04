package sdpfrequencia.visao;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import sdpfrequencia.negocio.FichaInscricaoService;
import java.io.File;

public class AdministracaoPanel extends VBox {

    private final FichaInscricaoService service = new FichaInscricaoService();

    public AdministracaoPanel() {
        this.setSpacing(30);
        this.setPadding(new Insets(10, 30, 30, 30));
        this.setStyle("-fx-background-color: transparent;");

        Label titulo = new Label("Painel Administrativo");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #0f172a;");

        // Cards de Estatísticas
        HBox statsBox = new HBox(20);
        
        int total = 0;
        try { total = service.listarTodas().size(); } catch(Exception e){}
        
        VBox cardTotal = statCard("Total de Inscrições", String.valueOf(total), "👤", "#3b82f6");
        VBox cardStatus = statCard("Status do Sistema", "Online", "📡", "#10b981");
        
        statsBox.getChildren().addAll(cardTotal, cardStatus);

        // Ações de Manutenção
        Label lblManutencao = new Label("Ferramentas de Manutenção");
        lblManutencao.setStyle("-fx-font-weight: bold; -fx-text-fill: #1e293b; -fx-font-size: 16px;");

        Button btnLimpar = new Button("🗑  Limpar Base de Dados");
        btnLimpar.setStyle("-fx-background-color: #fee2e2; -fx-text-fill: #991b1b; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
        
        btnLimpar.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmar Exclusão");
            confirm.setHeaderText("Deseja realmente apagar todos os dados?");
            confirm.setContentText("Esta ação não pode ser desfeita.");
            
            confirm.showAndWait().ifPresent(res -> {
                if (res == ButtonType.OK) {
                    File file = new File("dados.dat");
                    if(file.exists()) file.delete();
                    Alert msg = new Alert(Alert.AlertType.INFORMATION);
                    msg.setTitle("Sucesso");
                    msg.setContentText("A base de dados foi reiniciada.");
                    msg.show();
                }
            });
        });

        VBox manutencaoBox = new VBox(15, lblManutencao, btnLimpar);
        manutencaoBox.setStyle("-fx-background-color: white; -fx-padding: 25; -fx-background-radius: 12; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 4);");

        this.getChildren().addAll(titulo, statsBox, manutencaoBox);
    }

    private VBox statCard(String title, String val, String icon, String color) {
        Label lIcon = new Label(icon);
        lIcon.setStyle("-fx-font-size: 30px; -fx-text-fill: " + color + ";");
        
        Label lTitle = new Label(title);
        lTitle.setStyle("-fx-text-fill: #64748b; -fx-font-size: 14px;");
        
        Label lVal = new Label(val);
        lVal.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        
        VBox v = new VBox(5, lIcon, lTitle, lVal);
        v.setPrefWidth(250);
        v.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 12; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 4);");
        return v;
    }
}

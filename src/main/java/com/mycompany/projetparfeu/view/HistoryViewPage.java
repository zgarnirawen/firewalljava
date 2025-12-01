package com.mycompany.projetparfeu.view;

import com.mycompany.projetparfeu.model.blockchain.BlockChain;
import com.mycompany.projetparfeu.model.blockchain.Block;
import com.mycompany.projetparfeu.model.decision.DecisionResult;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Vue de l'historique/blockchain du pare-feu.
 * Affiche tous les blocs de la chaîne.
 * 
 * @author ZGARNI
 */
public class HistoryViewPage extends BorderPane {
    
    private Stage primaryStage;
    private BlockChain blockchain;
    private VBox blocksContainer;
    private Label chainInfoLabel;
    
    private static final DateTimeFormatter FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public HistoryViewPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.blockchain = new BlockChain();
        
        setupUI();
        loadBlockchain();
    }
    
    private void setupUI() {
        // Header
        VBox header = createHeader();
        setTop(header);
        
        // Centre - blockchain
        ScrollPane centerScroll = new ScrollPane();
        centerScroll.setFitToWidth(true);
        centerScroll.setStyle("-fx-background-color: #ecf0f1;");
        
        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(20));
        
        // Info de la chaîne
        VBox infoSection = createInfoSection();
        
        // Container des blocs
        blocksContainer = new VBox(15);
        blocksContainer.setAlignment(Pos.TOP_CENTER);
        
        centerContent.getChildren().addAll(infoSection, new Separator(), blocksContainer);
        centerScroll.setContent(centerContent);
        setCenter(centerScroll);
        
        // Bottom - boutons
        HBox bottom = createBottomButtons();
        setBottom(bottom);
        
        // Style
        setStyle("-fx-background-color: #ecf0f1;");
    }
    
    /**
     * Crée le header.
     */
    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #34495e;");
        
        Label titleLabel = new Label("History");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.WHITE);
        
        Label subtitleLabel = new Label("Blockchain Transaction History");
        subtitleLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        subtitleLabel.setTextFill(Color.web("#bdc3c7"));
        
        header.getChildren().addAll(titleLabel, subtitleLabel);
        return header;
    }
    
    /**
     * Section d'information de la chaîne.
     */
    private VBox createInfoSection() {
        VBox section = new VBox(15);
        section.setPadding(new Insets(20));
        section.setAlignment(Pos.CENTER);
        section.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 10; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );
        
        Label sectionTitle = new Label("⛓️ Blockchain Information");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 20));
        sectionTitle.setTextFill(Color.web("#2c3e50"));
        
        HBox statsBox = new HBox(40);
        statsBox.setAlignment(Pos.CENTER);
        
        // Nombre de blocs
        VBox blocksBox = createInfoCard("Blocks", String.valueOf(blockchain.getSize()), "#3498db");
        
        // Validité
        boolean isValid = blockchain.isChainValid();
        VBox validityBox = createInfoCard("Status", isValid ? "✅ VALID" : "❌ INVALID", 
                                          isValid ? "#27ae60" : "#e74c3c");
        
        // Dernier bloc
        Block lastBlock = blockchain.getLastBlock();
        VBox lastBlockBox = createInfoCard("Last Block", "Block #" + lastBlock.index(), "#9b59b6");
        
        statsBox.getChildren().addAll(blocksBox, validityBox, lastBlockBox);
        
        chainInfoLabel = new Label();
        chainInfoLabel.setFont(Font.font("System", FontWeight.NORMAL, 12));
        chainInfoLabel.setTextFill(Color.web("#7f8c8d"));
        
        section.getChildren().addAll(sectionTitle, statsBox, chainInfoLabel);
        return section;
    }
    
    /**
     * Crée une carte d'info.
     */
    private VBox createInfoCard(String title, String value, String color) {
        VBox card = new VBox(5);
        card.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.NORMAL, 12));
        titleLabel.setTextFill(Color.web("#7f8c8d"));
        
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        valueLabel.setTextFill(Color.web(color));
        
        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }
    
    /**
     * Crée un bloc visuel.
     */
    private VBox createBlockCard(Block block, int index) {
        VBox blockCard = new VBox(15);
        blockCard.setPadding(new Insets(20));
        blockCard.setPrefWidth(700);
        blockCard.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 10; " +
            "-fx-border-color: #9b59b6; " +
            "-fx-border-width: 2; " +
            "-fx-border-radius: 10; " +
            "-fx-effect: dropshadow(gaussian, rgba(155,89,182,0.3), 15, 0, 0, 3);"
        );
        
        // En-tête du bloc
        HBox blockHeader = new HBox(15);
        blockHeader.setAlignment(Pos.CENTER_LEFT);
        
        Label blockIndexLabel = new Label("BLOCK #" + block.index());
        blockIndexLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        blockIndexLabel.setTextFill(Color.web("#9b59b6"));
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        LocalDateTime timestamp = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(block.timestamp()),
            ZoneId.systemDefault()
        );
        
        Label timestampLabel = new Label("⏰ " + timestamp.format(FORMATTER));
        timestampLabel.setFont(Font.font("System", FontWeight.NORMAL, 12));
        timestampLabel.setTextFill(Color.web("#7f8c8d"));
        
        blockHeader.getChildren().addAll(blockIndexLabel, spacer, timestampLabel);
        
        // Hashes
        GridPane hashGrid = new GridPane();
        hashGrid.setHgap(10);
        hashGrid.setVgap(5);
        
        Label hashTitleLabel = new Label("Hash:");
        hashTitleLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        hashTitleLabel.setTextFill(Color.web("#2c3e50"));
        
        Label hashValueLabel = new Label(block.hash().substring(0, 32) + "...");
        hashValueLabel.setFont(Font.font("Courier New", FontWeight.NORMAL, 11));
        hashValueLabel.setTextFill(Color.web("#34495e"));
        
        Label prevHashTitleLabel = new Label("Prev Hash:");
        prevHashTitleLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        prevHashTitleLabel.setTextFill(Color.web("#2c3e50"));
        
        String prevHashDisplay = block.index() == 0 ? "Genesis Block" : 
                                 block.previousHash().substring(0, 32) + "...";
        Label prevHashValueLabel = new Label(prevHashDisplay);
        prevHashValueLabel.setFont(Font.font("Courier New", FontWeight.NORMAL, 11));
        prevHashValueLabel.setTextFill(Color.web("#34495e"));
        
        hashGrid.add(hashTitleLabel, 0, 0);
        hashGrid.add(hashValueLabel, 1, 0);
        hashGrid.add(prevHashTitleLabel, 0, 1);
        hashGrid.add(prevHashValueLabel, 1, 1);
        
        // Décisions
        VBox decisionsBox = new VBox(8);
        
        Label decisionsTitle = new Label("📋 Decisions (" + block.decisions().size() + ")");
        decisionsTitle.setFont(Font.font("System", FontWeight.BOLD, 14));
        decisionsTitle.setTextFill(Color.web("#2c3e50"));
        
        decisionsBox.getChildren().add(decisionsTitle);
        
        if (block.decisions().isEmpty()) {
            Label emptyLabel = new Label("No decisions in this block");
            emptyLabel.setFont(Font.font("System", FontWeight.ITALIC, 12));
            emptyLabel.setTextFill(Color.web("#95a5a6"));
            decisionsBox.getChildren().add(emptyLabel);
        } else {
            int count = 1;
            for (DecisionResult decision : block.decisions()) {
                HBox decisionRow = new HBox(10);
                decisionRow.setAlignment(Pos.CENTER_LEFT);
                decisionRow.setPadding(new Insets(5));
                decisionRow.setStyle(
                    "-fx-background-color: #f8f9fa; " +
                    "-fx-background-radius: 5; " +
                    "-fx-border-color: #dee2e6; " +
                    "-fx-border-radius: 5;"
                );
                
                Label numLabel = new Label(count + ".");
                numLabel.setFont(Font.font("System", FontWeight.BOLD, 11));
                numLabel.setPrefWidth(25);
                
                Label actionLabel = new Label(decision.getAction().getSymbol());
                actionLabel.setFont(Font.font("System", FontWeight.BOLD, 11));
                actionLabel.setPrefWidth(80);
                
                String actionColor = "#95a5a6";
                if (decision.getAction().toString().equals("DROP")) actionColor = "#e74c3c";
                else if (decision.getAction().toString().equals("ACCEPT")) actionColor = "#27ae60";
                else if (decision.getAction().toString().equals("ALERT")) actionColor = "#f39c12";
                
                actionLabel.setTextFill(Color.web(actionColor));
                
                Label scoreLabel = new Label("Score: " + decision.getTotalScore());
                scoreLabel.setFont(Font.font("System", FontWeight.NORMAL, 11));
                scoreLabel.setPrefWidth(70);
                
                Label ipLabel = new Label(decision.getPacket().getSrcIP() + " → " + 
                                         decision.getPacket().getDestIP());
                ipLabel.setFont(Font.font("Courier New", FontWeight.NORMAL, 10));
                
                decisionRow.getChildren().addAll(numLabel, actionLabel, scoreLabel, ipLabel);
                decisionsBox.getChildren().add(decisionRow);
                count++;
            }
        }
        
        // Ligne de séparation
        Separator separator = new Separator();
        
        // Indicateur de chaînage
        if (index < blockchain.getSize() - 1) {
            Label chainIndicator = new Label("⬇ Chained to next block");
            chainIndicator.setFont(Font.font("System", FontWeight.BOLD, 11));
            chainIndicator.setTextFill(Color.web("#9b59b6"));
            chainIndicator.setAlignment(Pos.CENTER);
            chainIndicator.setMaxWidth(Double.MAX_VALUE);
            blockCard.getChildren().add(chainIndicator);
        }
        
        blockCard.getChildren().addAll(blockHeader, separator, hashGrid, separator, decisionsBox);
        
        return blockCard;
    }
    
    /**
     * Boutons en bas.
     */
    private HBox createBottomButtons() {
        HBox bottom = new HBox(15);
        bottom.setAlignment(Pos.CENTER);
        bottom.setPadding(new Insets(20));
        bottom.setStyle("-fx-background-color: #34495e;");
        
        Button refreshButton = createStyledButton("🔄 Refresh", "#3498db", 150);
        refreshButton.setOnAction(e -> loadBlockchain());
        
        Button verifyButton = createStyledButton("✓ Verify", "#27ae60", 150);
        verifyButton.setOnAction(e -> handleVerify());
        
        Button backButton = createStyledButton("← Back", "#95a5a6", 150);
        backButton.setOnAction(e -> handleBack());
        
        bottom.getChildren().addAll(backButton, refreshButton, verifyButton);
        return bottom;
    }
    
    /**
     * Crée un bouton stylisé.
     */
    private Button createStyledButton(String text, String color, int width) {
        Button button = new Button(text);
        button.setPrefWidth(width);
        button.setPrefHeight(40);
        button.setFont(Font.font("System", FontWeight.BOLD, 14));
        button.setStyle(
            "-fx-background-color: " + color + "; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 5; " +
            "-fx-cursor: hand;"
        );
        
        button.setOnMouseEntered(e -> 
            button.setStyle(
                "-fx-background-color: derive(" + color + ", -10%); " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 5; " +
                "-fx-cursor: hand;"
            )
        );
        
        button.setOnMouseExited(e -> 
            button.setStyle(
                "-fx-background-color: " + color + "; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 5; " +
                "-fx-cursor: hand;"
            )
        );
        
        return button;
    }
    
    /**
     * Charge et affiche la blockchain.
     */
    private void loadBlockchain() {
        blocksContainer.getChildren().clear();
        
        var chain = blockchain.getChain();
        
        for (int i = chain.size() - 1; i >= 0; i--) {
            Block block = chain.get(i);
            VBox blockCard = createBlockCard(block, i);
            blocksContainer.getChildren().add(blockCard);
        }
        
        chainInfoLabel.setText("Displaying " + chain.size() + " blocks in chronological order (newest first)");
    }
    
    /**
     * Vérifie l'intégrité de la blockchain.
     */
    private void handleVerify() {
        boolean isValid = blockchain.isChainValid();
        
        Alert alert = new Alert(isValid ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle("Blockchain Verification");
        alert.setHeaderText(isValid ? "✅ Blockchain is Valid" : "❌ Blockchain is Invalid");
        
        if (isValid) {
            alert.setContentText("The blockchain is intact. All blocks are valid and properly chained.");
        } else {
            alert.setContentText("WARNING: The blockchain has been compromised! Invalid blocks detected.");
        }
        
        alert.showAndWait();
    }
    
    /**
     * Retourne au menu principal.
     */
    private void handleBack() {
        MainView mainView = new MainView(primaryStage);
        Scene scene = new Scene(mainView, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Firewall Application");
    }
}
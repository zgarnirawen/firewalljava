package com.mycompany.projetparfeu.view;

import com.mycompany.projetparfeu.model.decision.DecisionResult;
import com.mycompany.projetparfeu.model.generator.Packet;
import com.mycompany.projetparfeu.model.analyzer.DetectionSignal;
import com.mycompany.projetparfeu.model.blockchain.BlockChain;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Vue d'analyse et de décision.
 * Affiche les résultats détaillés de l'analyse d'un paquet.
 * 
 * @author ZGARNI
 */
public class AnalysisViewPage extends BorderPane {
    
    private Stage primaryStage;
    private DecisionResult result;
    private Packet packet;
    private BlockChain blockchain;
    
    // Labels pour affichage
    private Label actionLabel;
    private Label scoreLabel;
    private ProgressBar threatProgressBar;
    private TextArea analysisDetailsArea;
    private VBox signalsContainer;
    
    public AnalysisViewPage(Stage primaryStage, DecisionResult result, Packet packet) {
        this.primaryStage = primaryStage;
        this.result = result;
        this.packet = packet;
        this.blockchain = new BlockChain();
        
        setupUI();
        displayAnalysis();
    }
    
    private void setupUI() {
        // Header
        VBox header = createHeader();
        setTop(header);
        
        // Centre - résultats d'analyse
        ScrollPane centerScroll = new ScrollPane();
        centerScroll.setFitToWidth(true);
        centerScroll.setStyle("-fx-background-color: #ecf0f1;");
        
        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(20));
        
        // Section score
        VBox scoreSection = createScoreSection();
        
        // Section signaux détectés
        VBox signalsSection = createSignalsSection();
        
        // Section détails d'analyse
        VBox detailsSection = createDetailsSection();
        
        // Section décision finale
        VBox decisionSection = createDecisionSection();
        
        centerContent.getChildren().addAll(
            scoreSection,
            new Separator(),
            signalsSection,
            new Separator(),
            detailsSection,
            new Separator(),
            decisionSection
        );
        
        centerScroll.setContent(centerContent);
        setCenter(centerScroll);
        
        // Bottom - boutons d'action
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
        
        Label titleLabel = new Label("Analysis");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.WHITE);
        
        Label subtitleLabel = new Label("Detailed Packet Analysis Results");
        subtitleLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        subtitleLabel.setTextFill(Color.web("#bdc3c7"));
        
        header.getChildren().addAll(titleLabel, subtitleLabel);
        return header;
    }
    
    /**
     * Section du score de menace.
     */
    private VBox createScoreSection() {
        VBox section = new VBox(20);
        section.setAlignment(Pos.CENTER);
        section.setPadding(new Insets(30));
        section.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 10; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );
        
        Label sectionTitle = new Label("🎯 Threat Score Analysis");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 20));
        sectionTitle.setTextFill(Color.web("#2c3e50"));
        
        HBox scoreBox = new HBox(50);
        scoreBox.setAlignment(Pos.CENTER);
        
        // Action
        VBox actionBox = new VBox(8);
        actionBox.setAlignment(Pos.CENTER);
        
        Label actionTitleLabel = new Label("Action Taken:");
        actionTitleLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        actionTitleLabel.setTextFill(Color.web("#7f8c8d"));
        
        actionLabel = new Label();
        actionLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        
        actionBox.getChildren().addAll(actionTitleLabel, actionLabel);
        
        // Score
        VBox scoreValueBox = new VBox(8);
        scoreValueBox.setAlignment(Pos.CENTER);
        
        Label scoreTitleLabel = new Label("Threat Score:");
        scoreTitleLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        scoreTitleLabel.setTextFill(Color.web("#7f8c8d"));
        
        scoreLabel = new Label();
        scoreLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        
        scoreValueBox.getChildren().addAll(scoreTitleLabel, scoreLabel);
        
        scoreBox.getChildren().addAll(actionBox, scoreValueBox);
        
        // Barre de progression
        VBox progressBox = new VBox(10);
        progressBox.setAlignment(Pos.CENTER);
        progressBox.setPrefWidth(400);
        
        Label progressLabel = new Label("Threat Level:");
        progressLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        progressLabel.setTextFill(Color.web("#7f8c8d"));
        
        threatProgressBar = new ProgressBar(0);
        threatProgressBar.setPrefWidth(400);
        threatProgressBar.setPrefHeight(30);
        
        progressBox.getChildren().addAll(progressLabel, threatProgressBar);
        
        section.getChildren().addAll(sectionTitle, scoreBox, progressBox);
        return section;
    }
    
    /**
     * Section des signaux détectés.
     */
    private VBox createSignalsSection() {
        VBox section = new VBox(15);
        section.setPadding(new Insets(20));
        section.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 10; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );
        
        Label sectionTitle = new Label("⚠️ Detection Signals (" + result.getSignals().size() + ")");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        sectionTitle.setTextFill(Color.web("#2c3e50"));
        
        signalsContainer = new VBox(10);
        
        section.getChildren().addAll(sectionTitle, signalsContainer);
        return section;
    }
    
    /**
     * Crée une carte de signal.
     */
    private HBox createSignalCard(DetectionSignal signal, int index) {
        HBox card = new HBox(15);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(15));
        card.setStyle(
            "-fx-background-color: #f8f9fa; " +
            "-fx-background-radius: 5; " +
            "-fx-border-color: #dee2e6; " +
            "-fx-border-width: 1; " +
            "-fx-border-radius: 5;"
        );
        
        // Numéro
        Label numLabel = new Label("#" + index);
        numLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        numLabel.setTextFill(Color.web("#7f8c8d"));
        numLabel.setPrefWidth(40);
        
        // Description
        VBox detailsBox = new VBox(5);
        detailsBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(detailsBox, Priority.ALWAYS);
        
        Label descLabel = new Label(signal.getDescription());
        descLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        descLabel.setWrapText(true);
        
        Label typeLabel = new Label("Type: " + signal.getClass().getSimpleName() + 
                                    " | Level: " + signal.getThreatLevel());
        typeLabel.setFont(Font.font("System", FontWeight.ITALIC, 11));
        typeLabel.setTextFill(Color.web("#7f8c8d"));
        
        detailsBox.getChildren().addAll(descLabel, typeLabel);
        
        // Score
        VBox scoreBox = new VBox(5);
        scoreBox.setAlignment(Pos.CENTER);
        
        Label scoreValueLabel = new Label(String.valueOf(signal.getScore()));
        scoreValueLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        
        String scoreColor = "#27ae60";
        if (signal.getScore() >= 5) scoreColor = "#e74c3c";
        else if (signal.getScore() >= 2) scoreColor = "#f39c12";
        
        scoreValueLabel.setTextFill(Color.web(scoreColor));
        
        Label scoreTextLabel = new Label("Score");
        scoreTextLabel.setFont(Font.font("System", FontWeight.NORMAL, 11));
        scoreTextLabel.setTextFill(Color.web("#7f8c8d"));
        
        scoreBox.getChildren().addAll(scoreValueLabel, scoreTextLabel);
        
        card.getChildren().addAll(numLabel, detailsBox, scoreBox);
        return card;
    }
    
    /**
     * Section des détails d'analyse.
     */
    private VBox createDetailsSection() {
        VBox section = new VBox(15);
        section.setPadding(new Insets(20));
        section.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 10; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );
        
        Label sectionTitle = new Label("📋 Analysis Details");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        sectionTitle.setTextFill(Color.web("#2c3e50"));
        
        analysisDetailsArea = new TextArea();
        analysisDetailsArea.setEditable(false);
        analysisDetailsArea.setPrefHeight(250);
        analysisDetailsArea.setWrapText(true);
        analysisDetailsArea.setStyle(
            "-fx-font-family: 'Courier New'; " +
            "-fx-font-size: 12px; " +
            "-fx-background-color: #f8f9fa; " +
            "-fx-border-color: #dee2e6; " +
            "-fx-border-radius: 5;"
        );
        
        section.getChildren().addAll(sectionTitle, analysisDetailsArea);
        return section;
    }
    
    /**
     * Section de la décision finale.
     */
    private VBox createDecisionSection() {
        VBox section = new VBox(15);
        section.setPadding(new Insets(20));
        section.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 10; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );
        
        Label sectionTitle = new Label("⚖️ Final Decision");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        sectionTitle.setTextFill(Color.web("#2c3e50"));
        
        VBox decisionBox = new VBox(10);
        decisionBox.setAlignment(Pos.CENTER);
        decisionBox.setPadding(new Insets(20));
        decisionBox.setStyle(
            "-fx-background-color: #f8f9fa; " +
            "-fx-background-radius: 5; " +
            "-fx-border-width: 2; " +
            "-fx-border-radius: 5;"
        );
        
        Label reasonTitleLabel = new Label("Reason:");
        reasonTitleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        reasonTitleLabel.setTextFill(Color.web("#2c3e50"));
        
        Label reasonLabel = new Label(result.getReason());
        reasonLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        reasonLabel.setWrapText(true);
        reasonLabel.setTextFill(Color.web("#34495e"));
        
        decisionBox.getChildren().addAll(reasonTitleLabel, reasonLabel);
        
        // Couleur de bordure selon l'action
        String borderColor = "#95a5a6";
        if (result.getAction().toString().equals("DROP")) borderColor = "#e74c3c";
        else if (result.getAction().toString().equals("ACCEPT")) borderColor = "#27ae60";
        else if (result.getAction().toString().equals("ALERT")) borderColor = "#f39c12";
        
        decisionBox.setStyle(decisionBox.getStyle() + "-fx-border-color: " + borderColor + ";");
        
        section.getChildren().addAll(sectionTitle, decisionBox);
        return section;
    }
    
    /**
     * Boutons en bas.
     */
    private HBox createBottomButtons() {
        HBox bottom = new HBox(15);
        bottom.setAlignment(Pos.CENTER);
        bottom.setPadding(new Insets(20));
        bottom.setStyle("-fx-background-color: #34495e;");
        
        Button decisionButton = createStyledButton("⚖️ View Decision Details", "#9b59b6", 220);
        decisionButton.setOnAction(e -> handleDecision());
        
        Button saveButton = createStyledButton("💾 Save to History", "#27ae60", 200);
        saveButton.setOnAction(e -> handleSave());
        
        Button backButton = createStyledButton("← Back to Selection", "#95a5a6", 200);
        backButton.setOnAction(e -> handleBack());
        
        bottom.getChildren().addAll(backButton, decisionButton, saveButton);
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
     * Affiche les résultats de l'analyse.
     */
    private void displayAnalysis() {
        // Mettre à jour l'action
        actionLabel.setText(result.getAction().getSymbol() + " " + result.getAction());
        
        String actionColor = "#95a5a6";
        if (result.getAction().toString().equals("DROP")) actionColor = "#e74c3c";
        else if (result.getAction().toString().equals("ACCEPT")) actionColor = "#27ae60";
        else if (result.getAction().toString().equals("ALERT")) actionColor = "#f39c12";
        else if (result.getAction().toString().equals("LOG")) actionColor = "#3498db";
        
        actionLabel.setTextFill(Color.web(actionColor));
        
        // Mettre à jour le score
        int score = result.getTotalScore();
        scoreLabel.setText(score + " / 10");
        
        String scoreColor = "#27ae60";
        if (score >= 5) scoreColor = "#e74c3c";
        else if (score >= 2) scoreColor = "#f39c12";
        
        scoreLabel.setTextFill(Color.web(scoreColor));
        
        // Mettre à jour la barre de progression
        threatProgressBar.setProgress(score / 10.0);
        threatProgressBar.setStyle("-fx-accent: " + scoreColor + ";");
        
        // Afficher les signaux
        if (result.getSignals().isEmpty()) {
            Label noSignalsLabel = new Label("No threat signals detected - Clean packet");
            noSignalsLabel.setFont(Font.font("System", FontWeight.ITALIC, 14));
            noSignalsLabel.setTextFill(Color.web("#27ae60"));
            signalsContainer.getChildren().add(noSignalsLabel);
        } else {
            int index = 1;
            for (DetectionSignal signal : result.getSignals()) {
                HBox signalCard = createSignalCard(signal, index++);
                signalsContainer.getChildren().add(signalCard);
            }
        }
        
        // Afficher les détails complets
        analysisDetailsArea.setText(result.getDetailedSummary());
    }
    
    /**
     * Affiche plus de détails sur la décision.
     */
    private void handleDecision() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Decision Details");
        alert.setHeaderText("Complete Decision Report");
        
        TextArea content = new TextArea(result.getDetailedSummary());
        content.setEditable(false);
        content.setWrapText(true);
        content.setPrefHeight(400);
        content.setStyle("-fx-font-family: 'Courier New';");
        
        alert.getDialogPane().setContent(content);
        alert.getDialogPane().setPrefWidth(700);
        alert.showAndWait();
    }
    
    /**
     * Sauvegarde dans l'historique/blockchain.
     */
    private void handleSave() {
        blockchain.addDecision(result);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Saved");
        alert.setHeaderText("Decision Saved to Blockchain");
        alert.setContentText("The analysis has been successfully saved to the blockchain history.");
        alert.showAndWait();
    }
    
    /**
     * Retourne à la sélection de paquets.
     */
    private void handleBack() {
        PacketSelectionView view = new PacketSelectionView(primaryStage);
        Scene scene = new Scene(view, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Packet Selection");
    }
}
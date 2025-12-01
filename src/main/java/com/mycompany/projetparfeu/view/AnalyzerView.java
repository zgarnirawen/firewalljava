package com.mycompany.projetparfeu.view;

import com.mycompany.projetparfeu.model.engine.FirewallEngine;
import com.mycompany.projetparfeu.model.generator.*;
import com.mycompany.projetparfeu.model.decision.DecisionResult;
import com.mycompany.projetparfeu.model.analyzer.DetectionSignal;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

/**
 * Vue de l'analyseur de paquets.
 * Permet d'analyser des paquets individuels ou en lot.
 * 
 * @author ZGARNI
 */
public class AnalyzerView extends VBox {
    
    private FirewallEngine firewall;
    private PacketSelector selector;
    
    // Contrôles pour l'analyse individuelle
    private TextField srcIPField;
    private TextField destIPField;
    private Spinner<Integer> srcPortSpinner;
    private Spinner<Integer> destPortSpinner;
    private ComboBox<String> protocolCombo;
    private TextArea payloadArea;
    private CheckBox maliciousCheckBox;
    private ComboBox<String> attackTypeCombo;
    
    // Zone de résultats
    private TextArea resultsArea;
    private Label actionLabel;
    private Label scoreLabel;
    private ProgressBar threatProgressBar;
    
    // Contrôles pour l'analyse en lot
    private Spinner<Integer> batchCountSpinner;
    private Spinner<Integer> maliciousPercentageSpinner;
    private TextArea batchResultsArea;
    
    public AnalyzerView(FirewallEngine firewall, PacketSelector selector) {
        this.firewall = firewall;
        this.selector = selector;
        setupUI();
    }
    
    private void setupUI() {
        setPadding(new Insets(20));
        setSpacing(15);
        setStyle("-fx-background-color: #f5f5f5;");
        
        // Titre
        Label titleLabel = new Label("🔍 ANALYSEUR DE PAQUETS");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web("#2c3e50"));
        
        // Créer les onglets
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        Tab singleTab = new Tab("Analyse Individuelle", createSingleAnalysisView());
        Tab batchTab = new Tab("Analyse en Lot", createBatchAnalysisView());
        Tab randomTab = new Tab("Test Rapide", createQuickTestView());
        
        tabPane.getTabs().addAll(singleTab, batchTab, randomTab);
        
        getChildren().addAll(titleLabel, new Separator(), tabPane);
        VBox.setVgrow(tabPane, Priority.ALWAYS);
    }
    
    /**
     * Vue d'analyse individuelle.
     */
    private VBox createSingleAnalysisView() {
        VBox view = new VBox(15);
        view.setPadding(new Insets(20));
        
        // Section de saisie du paquet
        VBox inputSection = createPacketInputSection();
        
        // Section des résultats
        VBox resultsSection = createResultsSection();
        
        view.getChildren().addAll(inputSection, new Separator(), resultsSection);
        return view;
    }
    
    /**
     * Section de saisie d'un paquet.
     */
    private VBox createPacketInputSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(15));
        section.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5;");
        
        Label sectionTitle = new Label("📝 Informations du Paquet");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        
        // IP Source
        Label srcIPLabel = new Label("IP Source:");
        srcIPField = new TextField("192.168.1.100");
        srcIPField.setPromptText("192.168.1.100");
        
        // IP Destination
        Label destIPLabel = new Label("IP Destination:");
        destIPField = new TextField("203.0.113.10");
        destIPField.setPromptText("203.0.113.10");
        
        // Port Source
        Label srcPortLabel = new Label("Port Source:");
        srcPortSpinner = new Spinner<>(0, 65535, 54321);
        srcPortSpinner.setEditable(true);
        srcPortSpinner.setPrefWidth(150);
        
        // Port Destination
        Label destPortLabel = new Label("Port Destination:");
        destPortSpinner = new Spinner<>(0, 65535, 80);
        destPortSpinner.setEditable(true);
        destPortSpinner.setPrefWidth(150);
        
        // Protocole
        Label protocolLabel = new Label("Protocole:");
        protocolCombo = new ComboBox<>();
        protocolCombo.getItems().addAll("HTTP", "HTTPS", "TCP", "UDP", "ICMP", "FTP", "SSH", "DNS");
        protocolCombo.setValue("HTTP");
        protocolCombo.setPrefWidth(150);
        
        // Type de paquet
        maliciousCheckBox = new CheckBox("Paquet Malicieux");
        maliciousCheckBox.setOnAction(e -> {
            attackTypeCombo.setDisable(!maliciousCheckBox.isSelected());
        });
        
        // Type d'attaque
        Label attackTypeLabel = new Label("Type d'Attaque:");
        attackTypeCombo = new ComboBox<>();
        attackTypeCombo.getItems().addAll(
            "SQL_INJECTION", 
            "XSS", 
            "PATH_TRAVERSAL", 
            "COMMAND_INJECTION", 
            "PORT_SCAN", 
            "DOS"
        );
        attackTypeCombo.setValue("SQL_INJECTION");
        attackTypeCombo.setDisable(true);
        attackTypeCombo.setPrefWidth(200);
        
        // Payload
        Label payloadLabel = new Label("Payload:");
        payloadArea = new TextArea("GET /index.html HTTP/1.1");
        payloadArea.setPrefRowCount(3);
        payloadArea.setWrapText(true);
        
        // Ajouter au grid
        grid.add(srcIPLabel, 0, 0);
        grid.add(srcIPField, 1, 0);
        grid.add(destIPLabel, 2, 0);
        grid.add(destIPField, 3, 0);
        
        grid.add(srcPortLabel, 0, 1);
        grid.add(srcPortSpinner, 1, 1);
        grid.add(destPortLabel, 2, 1);
        grid.add(destPortSpinner, 3, 1);
        
        grid.add(protocolLabel, 0, 2);
        grid.add(protocolCombo, 1, 2);
        grid.add(maliciousCheckBox, 2, 2);
        
        grid.add(attackTypeLabel, 0, 3);
        grid.add(attackTypeCombo, 1, 3, 3, 1);
        
        grid.add(payloadLabel, 0, 4);
        grid.add(payloadArea, 1, 4, 3, 1);
        
        // Bouton d'analyse
        Button analyzeButton = new Button("🔍 Analyser ce Paquet");
        analyzeButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 30;");
        analyzeButton.setOnAction(e -> analyzeSinglePacket());
        
        HBox buttonBox = new HBox(analyzeButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));
        
        section.getChildren().addAll(sectionTitle, grid, buttonBox);
        return section;
    }
    
    /**
     * Section d'affichage des résultats.
     */
    private VBox createResultsSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(15));
        section.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5;");
        
        Label sectionTitle = new Label("📊 Résultat de l'Analyse");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        // Indicateurs visuels
        HBox indicatorsBox = new HBox(30);
        indicatorsBox.setAlignment(Pos.CENTER);
        indicatorsBox.setPadding(new Insets(15));
        
        // Action
        VBox actionBox = new VBox(5);
        actionBox.setAlignment(Pos.CENTER);
        Label actionTitleLabel = new Label("Action:");
        actionTitleLabel.setFont(Font.font("System", FontWeight.NORMAL, 12));
        actionLabel = new Label("En attente...");
        actionLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        actionLabel.setTextFill(Color.web("#95a5a6"));
        actionBox.getChildren().addAll(actionTitleLabel, actionLabel);
        
        // Score
        VBox scoreBox = new VBox(5);
        scoreBox.setAlignment(Pos.CENTER);
        Label scoreTitleLabel = new Label("Score de Menace:");
        scoreTitleLabel.setFont(Font.font("System", FontWeight.NORMAL, 12));
        scoreLabel = new Label("0/10");
        scoreLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        scoreLabel.setTextFill(Color.web("#95a5a6"));
        scoreBox.getChildren().addAll(scoreTitleLabel, scoreLabel);
        
        // Barre de progression de menace
        VBox threatBox = new VBox(5);
        threatBox.setAlignment(Pos.CENTER);
        Label threatTitleLabel = new Label("Niveau de Menace:");
        threatTitleLabel.setFont(Font.font("System", FontWeight.NORMAL, 12));
        threatProgressBar = new ProgressBar(0);
        threatProgressBar.setPrefWidth(200);
        threatProgressBar.setPrefHeight(25);
        threatProgressBar.setStyle("-fx-accent: #95a5a6;");
        threatBox.getChildren().addAll(threatTitleLabel, threatProgressBar);
        
        indicatorsBox.getChildren().addAll(actionBox, scoreBox, threatBox);
        
        // Zone de texte détaillée
        resultsArea = new TextArea();
        resultsArea.setEditable(false);
        resultsArea.setPrefHeight(200);
        resultsArea.setWrapText(true);
        resultsArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12px;");
        resultsArea.setText("Aucune analyse effectuée.\nSaisissez les informations d'un paquet et cliquez sur 'Analyser'.");
        
        section.getChildren().addAll(sectionTitle, indicatorsBox, resultsArea);
        return section;
    }
    
    /**
     * Vue d'analyse en lot.
     */
    private VBox createBatchAnalysisView() {
        VBox view = new VBox(15);
        view.setPadding(new Insets(20));
        
        // Contrôles
        VBox controlsSection = new VBox(15);
        controlsSection.setPadding(new Insets(15));
        controlsSection.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5;");
        
        Label sectionTitle = new Label("⚙️ Paramètres de l'Analyse en Lot");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        
        Label countLabel = new Label("Nombre de paquets:");
        batchCountSpinner = new Spinner<>(1, 1000, 10);
        batchCountSpinner.setEditable(true);
        batchCountSpinner.setPrefWidth(150);
        
        Label percentLabel = new Label("% de paquets malicieux:");
        maliciousPercentageSpinner = new Spinner<>(0, 100, 30);
        maliciousPercentageSpinner.setEditable(true);
        maliciousPercentageSpinner.setPrefWidth(150);
        
        grid.add(countLabel, 0, 0);
        grid.add(batchCountSpinner, 1, 0);
        grid.add(percentLabel, 0, 1);
        grid.add(maliciousPercentageSpinner, 1, 1);
        
        Button analyzeBatchButton = new Button("🚀 Lancer l'Analyse");
        analyzeBatchButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 30;");
        analyzeBatchButton.setOnAction(e -> analyzeBatch());
        
        HBox buttonBox = new HBox(analyzeBatchButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));
        
        controlsSection.getChildren().addAll(sectionTitle, grid, buttonBox);
        
        // Résultats
        VBox resultsSection = new VBox(10);
        resultsSection.setPadding(new Insets(15));
        resultsSection.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5;");
        
        Label resultsTitle = new Label("📊 Résultats de l'Analyse en Lot");
        resultsTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        batchResultsArea = new TextArea();
        batchResultsArea.setEditable(false);
        batchResultsArea.setPrefHeight(300);
        batchResultsArea.setWrapText(true);
        batchResultsArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12px;");
        batchResultsArea.setText("Aucune analyse en lot effectuée.");
        
        resultsSection.getChildren().addAll(resultsTitle, batchResultsArea);
        
        view.getChildren().addAll(controlsSection, resultsSection);
        VBox.setVgrow(resultsSection, Priority.ALWAYS);
        
        return view;
    }
    
    /**
     * Vue de test rapide.
     */
    private VBox createQuickTestView() {
        VBox view = new VBox(20);
        view.setPadding(new Insets(20));
        view.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("⚡ Test Rapide");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        
        Label descLabel = new Label("Sélectionnez un paquet aléatoire depuis les fichiers CSV");
        descLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        descLabel.setTextFill(Color.web("#7f8c8d"));
        
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button testBenignButton = new Button("🟢 Tester un Paquet Sain");
        testBenignButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 15 30;");
        testBenignButton.setOnAction(e -> testRandomPacket(false));
        
        Button testMaliciousButton = new Button("🔴 Tester un Paquet Malicieux");
        testMaliciousButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 15 30;");
        testMaliciousButton.setOnAction(e -> testRandomPacket(true));
        
        buttonBox.getChildren().addAll(testBenignButton, testMaliciousButton);
        
        view.getChildren().addAll(titleLabel, descLabel, buttonBox);
        return view;
    }
    
    /**
     * Analyse un paquet individuel.
     */
    private void analyzeSinglePacket() {
        try {
            Packet packet;
            
            if (maliciousCheckBox.isSelected()) {
                packet = new PaquetMalicieux(
                    srcIPField.getText(),
                    destIPField.getText(),
                    srcPortSpinner.getValue(),
                    destPortSpinner.getValue(),
                    protocolCombo.getValue(),
                    payloadArea.getText(),
                    attackTypeCombo.getValue()
                );
            } else {
                packet = new PaquetSimple(
                    srcIPField.getText(),
                    destIPField.getText(),
                    srcPortSpinner.getValue(),
                    destPortSpinner.getValue(),
                    protocolCombo.getValue(),
                    payloadArea.getText()
                );
            }
            
            DecisionResult result = firewall.processPacket(packet);
            displayResult(result);
            
        } catch (Exception e) {
            showError("Erreur", "Erreur lors de l'analyse: " + e.getMessage());
        }
    }
    
    /**
     * Affiche le résultat d'une analyse.
     */
    private void displayResult(DecisionResult result) {
        // Mettre à jour l'action
        actionLabel.setText(result.getAction().getSymbol() + " " + result.getAction());
        
        switch (result.getAction().toString()) {
            case "ACCEPT":
                actionLabel.setTextFill(Color.web("#27ae60"));
                break;
            case "DROP":
                actionLabel.setTextFill(Color.web("#e74c3c"));
                break;
            case "ALERT":
                actionLabel.setTextFill(Color.web("#f39c12"));
                break;
            case "LOG":
                actionLabel.setTextFill(Color.web("#3498db"));
                break;
        }
        
        // Mettre à jour le score
        int score = result.getTotalScore();
        scoreLabel.setText(score + "/10");
        
        if (score >= 5) {
            scoreLabel.setTextFill(Color.web("#e74c3c"));
            threatProgressBar.setStyle("-fx-accent: #e74c3c;");
        } else if (score >= 2) {
            scoreLabel.setTextFill(Color.web("#f39c12"));
            threatProgressBar.setStyle("-fx-accent: #f39c12;");
        } else {
            scoreLabel.setTextFill(Color.web("#27ae60"));
            threatProgressBar.setStyle("-fx-accent: #27ae60;");
        }
        
        threatProgressBar.setProgress(score / 10.0);
        
        // Afficher les détails
        resultsArea.setText(result.getDetailedSummary());
    }
    
    /**
     * Analyse un lot de paquets.
     */
    private void analyzeBatch() {
        try {
            int count = batchCountSpinner.getValue();
            int percentage = maliciousPercentageSpinner.getValue();
            
            List<Packet> packets = selector.selectRandomPackets(count, percentage);
            List<DecisionResult> results = firewall.processPackets(packets);
            
            StringBuilder summary = new StringBuilder();
            summary.append("═══════════════════════════════════════════════════════\n");
            summary.append("        ANALYSE EN LOT - ").append(count).append(" PAQUETS\n");
            summary.append("═══════════════════════════════════════════════════════\n\n");
            
            long accepted = results.stream().filter(DecisionResult::isAccepted).count();
            long blocked = results.stream().filter(DecisionResult::isBlocked).count();
            long alerted = results.stream().filter(DecisionResult::needsAlert).count();
            
            summary.append("Total traités : ").append(count).append("\n");
            summary.append("  ✓ Acceptés  : ").append(accepted).append(String.format(" (%.1f%%)\n", accepted * 100.0 / count));
            summary.append("  ✗ Bloqués   : ").append(blocked).append(String.format(" (%.1f%%)\n", blocked * 100.0 / count));
            summary.append("  ⚠ Alertes   : ").append(alerted).append(String.format(" (%.1f%%)\n", alerted * 100.0 / count));
            summary.append("\n");
            
            summary.append("───────────────────────────────────────────────────────\n");
            summary.append("DÉTAILS DES ANALYSES:\n");
            summary.append("───────────────────────────────────────────────────────\n\n");
            
            for (int i = 0; i < Math.min(results.size(), 20); i++) {
                DecisionResult result = results.get(i);
                summary.append(String.format("%d. %s | Score: %d | %s → %s\n",
                    i + 1,
                    result.getAction().getSymbol(),
                    result.getTotalScore(),
                    result.getPacket().getSrcIP(),
                    result.getPacket().getDestIP()
                ));
            }
            
            if (results.size() > 20) {
                summary.append("\n... et ").append(results.size() - 20).append(" autres résultats\n");
            }
            
            batchResultsArea.setText(summary.toString());
            
        } catch (Exception e) {
            showError("Erreur", "Erreur lors de l'analyse en lot: " + e.getMessage());
        }
    }
    
    /**
     * Teste un paquet aléatoire.
     */
    private void testRandomPacket(boolean malicious) {
        try {
            Packet packet = selector.selectRandomPacket(malicious);
            DecisionResult result = firewall.processPacket(packet);
            displayResult(result);
            
        } catch (Exception e) {
            showError("Erreur", "Erreur lors du test: " + e.getMessage());
        }
    }
    
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
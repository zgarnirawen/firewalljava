package com.mycompany.projetparfeu.view;

import com.mycompany.projetparfeu.model.config.FirewallConfig;
import com.mycompany.projetparfeu.model.engine.FirewallEngine;
import com.mycompany.projetparfeu.model.generator.*;
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

/**
 * Vue de sélection et génération de paquets.
 * Permet de choisir entre paquets sains et malicieux.
 * 
 * @author ZGARNI
 */
public class PacketSelectionView extends BorderPane {
    
    private Stage primaryStage;
    private PacketSelector selector;
    private FirewallEngine firewall;
    private Packet currentPacket;
    
    private TextArea packetDisplayArea;
    private TextArea filteringResultArea;
    private Button analyzeButton;
    
    public PacketSelectionView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        // Initialiser les composants
        try {
            this.selector = new PacketSelector();
            this.selector.loadPacketFiles();
            this.firewall = new FirewallEngine(new FirewallConfig());
            this.firewall.start();
        } catch (Exception e) {
            showError("Erreur d'initialisation: " + e.getMessage());
        }
        
        setupUI();
    }
    
    private void setupUI() {
        // Header
        VBox header = createHeader();
        setTop(header);
        
        // Centre - contenu principal
        VBox center = createCenterContent();
        setCenter(center);
        
        // Bottom - boutons d'action
        HBox bottom = createBottomButtons();
        setBottom(bottom);
        
        // Style général
        setStyle("-fx-background-color: #ecf0f1;");
    }
    
    /**
     * Crée le header de la vue.
     */
    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #34495e;");
        
        Label titleLabel = new Label("Packet Selection");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.WHITE);
        
        Label subtitleLabel = new Label("Choose a packet type to generate and analyze");
        subtitleLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        subtitleLabel.setTextFill(Color.web("#bdc3c7"));
        
        header.getChildren().addAll(titleLabel, subtitleLabel);
        return header;
    }
    
    /**
     * Crée le contenu central.
     */
    private VBox createCenterContent() {
        VBox center = new VBox(20);
        center.setPadding(new Insets(30));
        center.setAlignment(Pos.TOP_CENTER);
        
        // Section de sélection
        VBox selectionSection = createSelectionSection();
        
        // Zone d'affichage du paquet
        VBox displaySection = createDisplaySection();
        
        // Zone de résultats de filtrage
        VBox resultsSection = createResultsSection();
        
        center.getChildren().addAll(selectionSection, displaySection, resultsSection);
        return center;
    }
    
    /**
     * Section de sélection du type de paquet.
     */
    private VBox createSelectionSection() {
        VBox section = new VBox(15);
        section.setAlignment(Pos.CENTER);
        section.setPadding(new Insets(20));
        section.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 10; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );
        
        Label sectionTitle = new Label("Select Packet Type");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        sectionTitle.setTextFill(Color.web("#2c3e50"));
        
        HBox buttonsBox = new HBox(20);
        buttonsBox.setAlignment(Pos.CENTER);
        
        // Bouton Paquet Sain
        Button healthyButton = createStyledButton("🟢 Healthy Packet", "#27ae60", 200);
        healthyButton.setOnAction(e -> handleHealthyPacket());
        
        // Bouton Paquet Malicieux
        Button maliciousButton = createStyledButton("🔴 Malicious Packet", "#e74c3c", 200);
        maliciousButton.setOnAction(e -> handleMaliciousPacket());
        
        buttonsBox.getChildren().addAll(healthyButton, maliciousButton);
        
        section.getChildren().addAll(sectionTitle, buttonsBox);
        return section;
    }
    
    /**
     * Section d'affichage du paquet généré.
     */
    private VBox createDisplaySection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(20));
        section.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 10; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );
        
        Label sectionTitle = new Label("Generated Packet");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        sectionTitle.setTextFill(Color.web("#2c3e50"));
        
        packetDisplayArea = new TextArea();
        packetDisplayArea.setEditable(false);
        packetDisplayArea.setPrefHeight(150);
        packetDisplayArea.setWrapText(true);
        packetDisplayArea.setStyle(
            "-fx-font-family: 'Courier New'; " +
            "-fx-font-size: 12px; " +
            "-fx-background-color: #f8f9fa; " +
            "-fx-border-color: #dee2e6; " +
            "-fx-border-radius: 5;"
        );
        packetDisplayArea.setText("No packet generated yet.\nClick a button above to generate a packet.");
        
        section.getChildren().addAll(sectionTitle, packetDisplayArea);
        return section;
    }
    
    /**
     * Section des résultats de filtrage.
     */
    private VBox createResultsSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(20));
        section.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 10; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );
        
        Label sectionTitle = new Label("Packet Filtering Result");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        sectionTitle.setTextFill(Color.web("#2c3e50"));
        
        filteringResultArea = new TextArea();
        filteringResultArea.setEditable(false);
        filteringResultArea.setPrefHeight(100);
        filteringResultArea.setWrapText(true);
        filteringResultArea.setStyle(
            "-fx-font-family: 'Courier New'; " +
            "-fx-font-size: 12px; " +
            "-fx-background-color: #f8f9fa; " +
            "-fx-border-color: #dee2e6; " +
            "-fx-border-radius: 5;"
        );
        filteringResultArea.setText("Filtering results will appear here after analysis.");
        
        section.getChildren().addAll(sectionTitle, filteringResultArea);
        return section;
    }
    
    /**
     * Boutons d'action en bas.
     */
    private HBox createBottomButtons() {
        HBox bottom = new HBox(15);
        bottom.setAlignment(Pos.CENTER);
        bottom.setPadding(new Insets(20));
        bottom.setStyle("-fx-background-color: #34495e;");
        
        // Bouton Analyze
        analyzeButton = createStyledButton("🔍 Analyze", "#3498db", 150);
        analyzeButton.setDisable(true);
        analyzeButton.setOnAction(e -> handleAnalyze());
        
        // Bouton Back
        Button backButton = createStyledButton("← Back", "#95a5a6", 150);
        backButton.setOnAction(e -> handleBack());
        
        bottom.getChildren().addAll(backButton, analyzeButton);
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
     * Génère un paquet sain.
     */
    private void handleHealthyPacket() {
        try {
            currentPacket = selector.selectRandomPacket(false);
            displayPacket(currentPacket, "🟢 HEALTHY PACKET");
            analyzeButton.setDisable(false);
            filteringResultArea.setText("Ready to analyze. Click 'Analyze' button.");
        } catch (Exception e) {
            showError("Error generating healthy packet: " + e.getMessage());
        }
    }
    
    /**
     * Génère un paquet malicieux.
     */
    private void handleMaliciousPacket() {
        try {
            currentPacket = selector.selectRandomPacket(true);
            displayPacket(currentPacket, "🔴 MALICIOUS PACKET");
            analyzeButton.setDisable(false);
            filteringResultArea.setText("Ready to analyze. Click 'Analyze' button.");
        } catch (Exception e) {
            showError("Error generating malicious packet: " + e.getMessage());
        }
    }
    
    /**
     * Affiche les détails du paquet.
     */
    private void displayPacket(Packet packet, String type) {
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════\n");
        sb.append("  ").append(type).append("\n");
        sb.append("═══════════════════════════════════════════════════\n\n");
        sb.append("Source IP      : ").append(packet.getSrcIP()).append("\n");
        sb.append("Destination IP : ").append(packet.getDestIP()).append("\n");
        sb.append("Source Port    : ").append(packet.getSrcPort()).append("\n");
        sb.append("Dest Port      : ").append(packet.getDestPort()).append("\n");
        sb.append("Protocol       : ").append(packet.getProtocol()).append("\n");
        sb.append("Size           : ").append(packet.getSize()).append(" bytes\n");
        sb.append("Payload        : ").append(packet.getPayload()).append("\n");
        
        if (packet instanceof PaquetMalicieux) {
            sb.append("\n⚠️ Attack Type  : ").append(((PaquetMalicieux) packet).getTypeAttaque()).append("\n");
        }
        
        packetDisplayArea.setText(sb.toString());
    }
    
    /**
     * Analyse le paquet et ouvre la vue d'analyse.
     */
    private void handleAnalyze() {
        if (currentPacket == null) {
            showError("No packet to analyze!");
            return;
        }
        
        try {
            DecisionResult result = firewall.processPacket(currentPacket);
            
            // Afficher résumé dans la zone de filtrage
            StringBuilder sb = new StringBuilder();
            sb.append("Quick Filtering Result:\n");
            sb.append("Action: ").append(result.getAction()).append("\n");
            sb.append("Score: ").append(result.getTotalScore()).append("/10\n");
            sb.append("Reason: ").append(result.getReason()).append("\n");
            filteringResultArea.setText(sb.toString());
            
            // Ouvrir la vue d'analyse complète
            AnalysisViewPage view = new AnalysisViewPage(primaryStage, result, currentPacket);
            Scene scene = new Scene(view, 1000, 700);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Analysis Results");
            
        } catch (Exception e) {
            showError("Error analyzing packet: " + e.getMessage());
        }
    }
    
    /**
     * Retourne au menu principal.
     */
    private void handleBack() {
        if (firewall != null) {
            firewall.stop();
        }
        MainView mainView = new MainView(primaryStage);
        Scene scene = new Scene(mainView, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Firewall Application");
    }
    
    /**
     * Affiche un message d'erreur.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
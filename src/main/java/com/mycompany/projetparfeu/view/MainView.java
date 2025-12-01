package com.mycompany.projetparfeu.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Vue principale de l'application Firewall.
 * Affiche le menu principal avec navigation vers les différentes sections.
 * 
 * @author ZGARNI
 */
public class MainView extends VBox {
    
    private Stage primaryStage;
    
    public MainView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setupUI();
    }
    
    private void setupUI() {
        // Configuration du conteneur principal
        setAlignment(Pos.CENTER);
        setSpacing(25);
        setPadding(new Insets(50));
        setStyle("-fx-background-color: linear-gradient(to bottom, #ecf0f1, #bdc3c7);");
        
        // Header
        Label headerLabel = new Label("Firewall Application");
        headerLabel.setFont(Font.font("System", FontWeight.BOLD, 48));
        headerLabel.setTextFill(Color.web("#2c3e50"));
        headerLabel.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);");
        
        // Description
        Label descriptionLabel = new Label("Protect your network");
        descriptionLabel.setFont(Font.font("System", FontWeight.NORMAL, 20));
        descriptionLabel.setTextFill(Color.web("#7f8c8d"));
        
        // Container pour les boutons
        VBox buttonsContainer = new VBox(15);
        buttonsContainer.setAlignment(Pos.CENTER);
        buttonsContainer.setPadding(new Insets(30, 0, 0, 0));
        
        // Bouton Generate
        Button generateButton = createMenuButton("🔍 Generate Packet", "#3498db");
        generateButton.setOnAction(e -> handleGenerate());
        
        // Bouton Statistics
        Button statisticsButton = createMenuButton("📊 Statistics", "#27ae60");
        statisticsButton.setOnAction(e -> handleStatistics());
        
        // Bouton History
        Button historyButton = createMenuButton("⛓️ History", "#9b59b6");
        historyButton.setOnAction(e -> handleHistory());
        
        // Bouton Configuration
        Button configurationButton = createMenuButton("⚙️ Configuration", "#e67e22");
        configurationButton.setOnAction(e -> handleConfiguration());
        
        buttonsContainer.getChildren().addAll(
            generateButton, 
            statisticsButton, 
            historyButton, 
            configurationButton
        );
        
        // Footer
        Label footerLabel = new Label("Version 1.0 - © 2024");
        footerLabel.setFont(Font.font("System", FontWeight.NORMAL, 12));
        footerLabel.setTextFill(Color.web("#95a5a6"));
        footerLabel.setPadding(new Insets(30, 0, 0, 0));
        
        // Ajouter tous les éléments
        getChildren().addAll(
            headerLabel, 
            descriptionLabel, 
            buttonsContainer, 
            footerLabel
        );
    }
    
    /**
     * Crée un bouton de menu stylisé.
     */
    private Button createMenuButton(String text, String color) {
        Button button = new Button(text);
        button.setPrefWidth(250);
        button.setPrefHeight(50);
        button.setFont(Font.font("System", FontWeight.BOLD, 16));
        button.setStyle(
            "-fx-background-color: " + color + "; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 10; " +
            "-fx-cursor: hand; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"
        );
        
        // Effet hover
        button.setOnMouseEntered(e -> {
            button.setStyle(
                "-fx-background-color: derive(" + color + ", -10%); " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 10; " +
                "-fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 8, 0, 0, 3); " +
                "-fx-scale-x: 1.05; " +
                "-fx-scale-y: 1.05;"
            );
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(
                "-fx-background-color: " + color + "; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 10; " +
                "-fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"
            );
        });
        
        return button;
    }
    
    /**
     * Ouvre la vue de génération de paquets.
     */
    private void handleGenerate() {
        PacketSelectionView view = new PacketSelectionView(primaryStage);
        Scene scene = new Scene(view, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Packet Selection");
    }
    
    /**
     * Ouvre la vue des statistiques.
     */
    private void handleStatistics() {
        StatisticsViewPage view = new StatisticsViewPage(primaryStage);
        Scene scene = new Scene(view, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Statistics");
    }
    
    /**
     * Ouvre la vue de l'historique/blockchain.
     */
    private void handleHistory() {
        HistoryViewPage view = new HistoryViewPage(primaryStage);
        Scene scene = new Scene(view, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("History");
    }
    
    /**
     * Ouvre la vue de configuration.
     */
    private void handleConfiguration() {
        FirewallConfigViewPage view = new FirewallConfigViewPage(primaryStage);
        Scene scene = new Scene(view, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Firewall Configuration");
    }
}
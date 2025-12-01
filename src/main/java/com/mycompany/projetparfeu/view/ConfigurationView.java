package com.mycompany.projetparfeu.view;

import com.mycompany.projetparfeu.model.config.FirewallConfig;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Vue de configuration du pare-feu.
 * Permet de modifier tous les paramètres du pare-feu.
 * 
 * @author ZGARNI
 */
public class ConfigurationView extends VBox {
    
    private FirewallConfig config;
    
    // Contrôles pour les seuils
    private Spinner<Integer> blockThresholdSpinner;
    private Spinner<Integer> alertThresholdSpinner;
    
    // Contrôles pour les tailles
    private Spinner<Integer> minPacketSizeSpinner;
    private Spinner<Integer> maxPacketSizeSpinner;
    
    // Liste des mots suspects
    private ListView<String> suspiciousWordsList;
    private TextField newWordField;
    
    // Liste des IPs blacklistées
    private ListView<String> blacklistedIPsList;
    private TextField newIPField;
    
    // Liste des ports surveillés
    private ListView<Integer> monitoredPortsList;
    private Spinner<Integer> newPortSpinner;
    
    // Labels pour affichage temps réel
    private Label blockThresholdLabel;
    private Label alertThresholdLabel;
    
    public ConfigurationView(FirewallConfig config) {
        this.config = config;
        setupUI();
    }
    
    private void setupUI() {
        setPadding(new Insets(20));
        setSpacing(15);
        setStyle("-fx-background-color: #f5f5f5;");
        
        // Titre
        Label titleLabel = new Label("⚙️ CONFIGURATION DU PARE-FEU");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web("#2c3e50"));
        
        // Sections
        getChildren().addAll(
            titleLabel,
            new Separator(),
            createThresholdsSection(),
            new Separator(),
            createPacketSizesSection(),
            new Separator(),
            createSuspiciousWordsSection(),
            new Separator(),
            createBlacklistSection(),
            new Separator(),
            createPortsSection(),
            new Separator(),
            createActionButtons()
        );
    }
    
    /**
     * Section des seuils de décision.
     */
    private VBox createThresholdsSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(10));
        section.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5;");
        
        Label sectionTitle = new Label("🎯 Seuils de Décision");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        // Seuil de blocage
        HBox blockBox = new HBox(15);
        blockBox.setAlignment(Pos.CENTER_LEFT);
        Label blockLabel = new Label("Seuil de blocage:");
        blockLabel.setMinWidth(200);
        
        blockThresholdSpinner = new Spinner<>(1, 10, config.getBlockThreshold());
        blockThresholdSpinner.setEditable(true);
        blockThresholdSpinner.setPrefWidth(100);
        blockThresholdSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            config.setBlockThreshold(newVal);
            blockThresholdLabel.setText("Score ≥ " + newVal + " → BLOQUER");
        });
        
        blockThresholdLabel = new Label("Score ≥ " + config.getBlockThreshold() + " → BLOQUER");
        blockThresholdLabel.setTextFill(Color.web("#e74c3c"));
        blockThresholdLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        
        ProgressBar blockProgress = new ProgressBar(config.getBlockThreshold() / 10.0);
        blockProgress.setPrefWidth(200);
        blockProgress.setStyle("-fx-accent: #e74c3c;");
        
        blockBox.getChildren().addAll(blockLabel, blockThresholdSpinner, blockThresholdLabel, blockProgress);
        
        // Seuil d'alerte
        HBox alertBox = new HBox(15);
        alertBox.setAlignment(Pos.CENTER_LEFT);
        Label alertLabel = new Label("Seuil d'alerte:");
        alertLabel.setMinWidth(200);
        
        alertThresholdSpinner = new Spinner<>(1, 10, config.getAlertThreshold());
        alertThresholdSpinner.setEditable(true);
        alertThresholdSpinner.setPrefWidth(100);
        alertThresholdSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            config.setAlertThreshold(newVal);
            alertThresholdLabel.setText("Score ≥ " + newVal + " → ALERTER");
        });
        
        alertThresholdLabel = new Label("Score ≥ " + config.getAlertThreshold() + " → ALERTER");
        alertThresholdLabel.setTextFill(Color.web("#f39c12"));
        alertThresholdLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        
        ProgressBar alertProgress = new ProgressBar(config.getAlertThreshold() / 10.0);
        alertProgress.setPrefWidth(200);
        alertProgress.setStyle("-fx-accent: #f39c12;");
        
        alertBox.getChildren().addAll(alertLabel, alertThresholdSpinner, alertThresholdLabel, alertProgress);
        
        section.getChildren().addAll(sectionTitle, blockBox, alertBox);
        return section;
    }
    
    /**
     * Section des tailles de paquets.
     */
    private VBox createPacketSizesSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(10));
        section.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5;");
        
        Label sectionTitle = new Label("📦 Limites de Taille des Paquets");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        // Taille minimale
        HBox minBox = new HBox(15);
        minBox.setAlignment(Pos.CENTER_LEFT);
        Label minLabel = new Label("Taille minimale (bytes):");
        minLabel.setMinWidth(200);
        
        minPacketSizeSpinner = new Spinner<>(0, 65535, config.getMinPacketSize());
        minPacketSizeSpinner.setEditable(true);
        minPacketSizeSpinner.setPrefWidth(150);
        minPacketSizeSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            config.setMinPacketSize(newVal);
        });
        
        minBox.getChildren().addAll(minLabel, minPacketSizeSpinner);
        
        // Taille maximale
        HBox maxBox = new HBox(15);
        maxBox.setAlignment(Pos.CENTER_LEFT);
        Label maxLabel = new Label("Taille maximale (bytes):");
        maxLabel.setMinWidth(200);
        
        maxPacketSizeSpinner = new Spinner<>(0, 65535, config.getMaxPacketSize());
        maxPacketSizeSpinner.setEditable(true);
        maxPacketSizeSpinner.setPrefWidth(150);
        maxPacketSizeSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            config.setMaxPacketSize(newVal);
        });
        
        maxBox.getChildren().addAll(maxLabel, maxPacketSizeSpinner);
        
        section.getChildren().addAll(sectionTitle, minBox, maxBox);
        return section;
    }
    
    /**
     * Section des mots suspects.
     */
    private VBox createSuspiciousWordsSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(10));
        section.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5;");
        
        Label sectionTitle = new Label("🔍 Mots Suspects");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        suspiciousWordsList = new ListView<>();
        suspiciousWordsList.getItems().addAll(config.getSuspiciousWords());
        suspiciousWordsList.setPrefHeight(150);
        
        HBox addBox = new HBox(10);
        addBox.setAlignment(Pos.CENTER_LEFT);
        newWordField = new TextField();
        newWordField.setPromptText("Nouveau mot suspect...");
        newWordField.setPrefWidth(300);
        
        Button addWordButton = new Button("Ajouter");
        addWordButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        addWordButton.setOnAction(e -> {
            String word = newWordField.getText().trim();
            if (!word.isEmpty() && !suspiciousWordsList.getItems().contains(word)) {
                config.addSuspiciousWord(word);
                suspiciousWordsList.getItems().add(word);
                newWordField.clear();
            }
        });
        
        Button removeWordButton = new Button("Supprimer");
        removeWordButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        removeWordButton.setOnAction(e -> {
            String selected = suspiciousWordsList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                config.removeSuspiciousWord(selected);
                suspiciousWordsList.getItems().remove(selected);
            }
        });
        
        addBox.getChildren().addAll(newWordField, addWordButton, removeWordButton);
        
        section.getChildren().addAll(sectionTitle, suspiciousWordsList, addBox);
        return section;
    }
    
    /**
     * Section de la blacklist IP.
     */
    private VBox createBlacklistSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(10));
        section.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5;");
        
        Label sectionTitle = new Label("🚫 IPs Blacklistées");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        blacklistedIPsList = new ListView<>();
        blacklistedIPsList.getItems().addAll(config.getBlacklistedIPs());
        blacklistedIPsList.setPrefHeight(100);
        
        HBox addBox = new HBox(10);
        addBox.setAlignment(Pos.CENTER_LEFT);
        newIPField = new TextField();
        newIPField.setPromptText("192.168.1.100");
        newIPField.setPrefWidth(200);
        
        Button addIPButton = new Button("Ajouter IP");
        addIPButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        addIPButton.setOnAction(e -> {
            String ip = newIPField.getText().trim();
            if (!ip.isEmpty() && !blacklistedIPsList.getItems().contains(ip)) {
                config.addBlacklistedIP(ip);
                blacklistedIPsList.getItems().add(ip);
                newIPField.clear();
            }
        });
        
        Button removeIPButton = new Button("Retirer IP");
        removeIPButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        removeIPButton.setOnAction(e -> {
            String selected = blacklistedIPsList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                config.removeBlacklistedIP(selected);
                blacklistedIPsList.getItems().remove(selected);
            }
        });
        
        addBox.getChildren().addAll(newIPField, addIPButton, removeIPButton);
        
        section.getChildren().addAll(sectionTitle, blacklistedIPsList, addBox);
        return section;
    }
    
    /**
     * Section des ports surveillés.
     */
    private VBox createPortsSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(10));
        section.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5;");
        
        Label sectionTitle = new Label("🔌 Ports Surveillés");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        monitoredPortsList = new ListView<>();
        monitoredPortsList.getItems().addAll(config.getMonitoredPorts());
        monitoredPortsList.setPrefHeight(100);
        
        HBox addBox = new HBox(10);
        addBox.setAlignment(Pos.CENTER_LEFT);
        newPortSpinner = new Spinner<>(0, 65535, 80);
        newPortSpinner.setEditable(true);
        newPortSpinner.setPrefWidth(150);
        
        Button addPortButton = new Button("Ajouter Port");
        addPortButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        addPortButton.setOnAction(e -> {
            int port = newPortSpinner.getValue();
            if (!monitoredPortsList.getItems().contains(port)) {
                config.addMonitoredPort(port);
                monitoredPortsList.getItems().add(port);
            }
        });
        
        Button removePortButton = new Button("Retirer Port");
        removePortButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        removePortButton.setOnAction(e -> {
            Integer selected = monitoredPortsList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                config.removeMonitoredPort(selected);
                monitoredPortsList.getItems().remove(selected);
            }
        });
        
        addBox.getChildren().addAll(newPortSpinner, addPortButton, removePortButton);
        
        section.getChildren().addAll(sectionTitle, monitoredPortsList, addBox);
        return section;
    }
    
    /**
     * Boutons d'action.
     */
    private HBox createActionButtons() {
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        
        Button saveButton = new Button("💾 Sauvegarder Configuration");
        saveButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20;");
        saveButton.setOnAction(e -> saveConfiguration());
        
        Button resetButton = new Button("🔄 Réinitialiser");
        resetButton.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20;");
        resetButton.setOnAction(e -> resetConfiguration());
        
        buttonBox.getChildren().addAll(saveButton, resetButton);
        return buttonBox;
    }
    
    private void saveConfiguration() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sauvegarde");
        alert.setHeaderText(null);
        alert.setContentText("Configuration sauvegardée avec succès!");
        alert.showAndWait();
    }
    
    private void resetConfiguration() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Réinitialisation");
        alert.setHeaderText("Voulez-vous vraiment réinitialiser la configuration?");
        alert.setContentText("Toutes les modifications seront perdues.");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                config = new FirewallConfig();
                refreshUI();
            }
        });
    }
    
    private void refreshUI() {
        blockThresholdSpinner.getValueFactory().setValue(config.getBlockThreshold());
        alertThresholdSpinner.getValueFactory().setValue(config.getAlertThreshold());
        minPacketSizeSpinner.getValueFactory().setValue(config.getMinPacketSize());
        maxPacketSizeSpinner.getValueFactory().setValue(config.getMaxPacketSize());
        suspiciousWordsList.getItems().setAll(config.getSuspiciousWords());
        blacklistedIPsList.getItems().setAll(config.getBlacklistedIPs());
        monitoredPortsList.getItems().setAll(config.getMonitoredPorts());
    }
    
    public FirewallConfig getConfig() {
        return config;
    }
}
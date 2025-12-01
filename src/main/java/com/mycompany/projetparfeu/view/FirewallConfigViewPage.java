package com.mycompany.projetparfeu.view;

import com.mycompany.projetparfeu.model.config.FirewallConfig;
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
 * Vue de configuration du pare-feu.
 * Permet de modifier les règles, ports, IPs et niveaux de sécurité.
 * 
 * @author ZGARNI
 */
public class FirewallConfigViewPage extends BorderPane {
    
    private Stage primaryStage;
    private FirewallConfig config;
    
    // Contrôles de seuils
    private Spinner<Integer> blockThresholdSpinner;
    private Spinner<Integer> alertThresholdSpinner;
    
    // Contrôles de tailles
    private Spinner<Integer> minPacketSizeSpinner;
    private Spinner<Integer> maxPacketSizeSpinner;
    
    // Listes
    private ListView<String> suspiciousWordsList;
    private TextField newWordField;
    
    private ListView<String> blacklistedIPsList;
    private TextField newIPField;
    
    private ListView<Integer> monitoredPortsList;
    private Spinner<Integer> newPortSpinner;
    
    // ComboBox pour niveau de sécurité
    private ComboBox<String> securityLevelCombo;
    
    public FirewallConfigViewPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.config = new FirewallConfig();
        
        setupUI();
        loadConfiguration();
    }
    
    private void setupUI() {
        // Header
        VBox header = createHeader();
        setTop(header);
        
        // Centre - formulaire de configuration
        ScrollPane centerScroll = new ScrollPane();
        centerScroll.setFitToWidth(true);
        centerScroll.setStyle("-fx-background-color: #ecf0f1;");
        
        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(20));
        
        // Sections
        VBox thresholdsSection = createThresholdsSection();
        VBox packetSizesSection = createPacketSizesSection();
        VBox securitySection = createSecurityLevelSection();
        VBox wordsSection = createSuspiciousWordsSection();
        VBox ipsSection = createBlacklistSection();
        VBox portsSection = createPortsSection();
        
        centerContent.getChildren().addAll(
            thresholdsSection,
            new Separator(),
            packetSizesSection,
            new Separator(),
            securitySection,
            new Separator(),
            wordsSection,
            new Separator(),
            ipsSection,
            new Separator(),
            portsSection
        );
        
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
        
        Label titleLabel = new Label("Firewall Configuration");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.WHITE);
        
        Label subtitleLabel = new Label("Configure security rules and parameters");
        subtitleLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        subtitleLabel.setTextFill(Color.web("#bdc3c7"));
        
        header.getChildren().addAll(titleLabel, subtitleLabel);
        return header;
    }
    
    /**
     * Section des seuils de décision.
     */
    private VBox createThresholdsSection() {
        VBox section = createSection("🎯 Decision Thresholds");
        
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(10));
        
        // Seuil de blocage
        Label blockLabel = new Label("Block Threshold:");
        blockLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        
        blockThresholdSpinner = new Spinner<>(1, 10, config.getBlockThreshold());
        blockThresholdSpinner.setEditable(true);
        blockThresholdSpinner.setPrefWidth(100);
        
        Label blockDescLabel = new Label("Score ≥ this value → BLOCK packet");
        blockDescLabel.setFont(Font.font("System", FontWeight.ITALIC, 12));
        blockDescLabel.setTextFill(Color.web("#7f8c8d"));
        
        // Seuil d'alerte
        Label alertLabel = new Label("Alert Threshold:");
        alertLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        
        alertThresholdSpinner = new Spinner<>(1, 10, config.getAlertThreshold());
        alertThresholdSpinner.setEditable(true);
        alertThresholdSpinner.setPrefWidth(100);
        
        Label alertDescLabel = new Label("Score ≥ this value → ALERT");
        alertDescLabel.setFont(Font.font("System", FontWeight.ITALIC, 12));
        alertDescLabel.setTextFill(Color.web("#7f8c8d"));
        
        grid.add(blockLabel, 0, 0);
        grid.add(blockThresholdSpinner, 1, 0);
        grid.add(blockDescLabel, 2, 0);
        
        grid.add(alertLabel, 0, 1);
        grid.add(alertThresholdSpinner, 1, 1);
        grid.add(alertDescLabel, 2, 1);
        
        section.getChildren().add(grid);
        return section;
    }
    
    /**
     * Section des tailles de paquets.
     */
    private VBox createPacketSizesSection() {
        VBox section = createSection("📦 Packet Size Limits");
        
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(10));
        
        // Taille minimale
        Label minLabel = new Label("Minimum Size (bytes):");
        minLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        
        minPacketSizeSpinner = new Spinner<>(0, 65535, config.getMinPacketSize());
        minPacketSizeSpinner.setEditable(true);
        minPacketSizeSpinner.setPrefWidth(150);
        
        // Taille maximale
        Label maxLabel = new Label("Maximum Size (bytes):");
        maxLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        
        maxPacketSizeSpinner = new Spinner<>(0, 65535, config.getMaxPacketSize());
        maxPacketSizeSpinner.setEditable(true);
        maxPacketSizeSpinner.setPrefWidth(150);
        
        grid.add(minLabel, 0, 0);
        grid.add(minPacketSizeSpinner, 1, 0);
        grid.add(maxLabel, 0, 1);
        grid.add(maxPacketSizeSpinner, 1, 1);
        
        section.getChildren().add(grid);
        return section;
    }
    
    /**
     * Section du niveau de sécurité.
     */
    private VBox createSecurityLevelSection() {
        VBox section = createSection("🛡️ Security Level");
        
        HBox levelBox = new HBox(15);
        levelBox.setAlignment(Pos.CENTER_LEFT);
        levelBox.setPadding(new Insets(10));
        
        Label levelLabel = new Label("Security Level:");
        levelLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        
        securityLevelCombo = new ComboBox<>();
        securityLevelCombo.getItems().addAll("Low", "Medium", "High", "Maximum");
        securityLevelCombo.setValue("Medium");
        securityLevelCombo.setPrefWidth(200);
        
        Label levelDescLabel = new Label("(affects threshold defaults)");
        levelDescLabel.setFont(Font.font("System", FontWeight.ITALIC, 12));
        levelDescLabel.setTextFill(Color.web("#7f8c8d"));
        
        securityLevelCombo.setOnAction(e -> handleSecurityLevelChange());
        
        levelBox.getChildren().addAll(levelLabel, securityLevelCombo, levelDescLabel);
        section.getChildren().add(levelBox);
        return section;
    }
    
    /**
     * Section des mots suspects.
     */
    private VBox createSuspiciousWordsSection() {
        VBox section = createSection("🔍 Suspicious Words/Patterns");
        
        suspiciousWordsList = new ListView<>();
        suspiciousWordsList.setPrefHeight(150);
        
        HBox addBox = new HBox(10);
        addBox.setAlignment(Pos.CENTER_LEFT);
        addBox.setPadding(new Insets(10, 0, 0, 0));
        
        newWordField = new TextField();
        newWordField.setPromptText("Enter suspicious word...");
        newWordField.setPrefWidth(300);
        
        Button addButton = createSmallButton("Add", "#3498db");
        addButton.setOnAction(e -> handleAddWord());
        
        Button removeButton = createSmallButton("Remove", "#e74c3c");
        removeButton.setOnAction(e -> handleRemoveWord());
        
        addBox.getChildren().addAll(newWordField, addButton, removeButton);
        
        section.getChildren().addAll(suspiciousWordsList, addBox);
        return section;
    }
    
    /**
     * Section de la blacklist IP.
     */
    private VBox createBlacklistSection() {
        VBox section = createSection("🚫 Blacklisted IPs");
        
        blacklistedIPsList = new ListView<>();
        blacklistedIPsList.setPrefHeight(120);
        
        HBox addBox = new HBox(10);
        addBox.setAlignment(Pos.CENTER_LEFT);
        addBox.setPadding(new Insets(10, 0, 0, 0));
        
        newIPField = new TextField();
        newIPField.setPromptText("192.168.1.100");
        newIPField.setPrefWidth(200);
        
        Button addButton = createSmallButton("Add IP", "#3498db");
        addButton.setOnAction(e -> handleAddIP());
        
        Button removeButton = createSmallButton("Remove IP", "#e74c3c");
        removeButton.setOnAction(e -> handleRemoveIP());
        
        addBox.getChildren().addAll(newIPField, addButton, removeButton);
        
        section.getChildren().addAll(blacklistedIPsList, addBox);
        return section;
    }
    
    /**
     * Section des ports surveillés.
     */
    private VBox createPortsSection() {
        VBox section = createSection("🔌 Monitored Ports");
        
        monitoredPortsList = new ListView<>();
        monitoredPortsList.setPrefHeight(120);
        
        HBox addBox = new HBox(10);
        addBox.setAlignment(Pos.CENTER_LEFT);
        addBox.setPadding(new Insets(10, 0, 0, 0));
        
        newPortSpinner = new Spinner<>(0, 65535, 80);
        newPortSpinner.setEditable(true);
        newPortSpinner.setPrefWidth(150);
        
        Button addButton = createSmallButton("Add Port", "#3498db");
        addButton.setOnAction(e -> handleAddPort());
        
        Button removeButton = createSmallButton("Remove Port", "#e74c3c");
        removeButton.setOnAction(e -> handleRemovePort());
        
        addBox.getChildren().addAll(newPortSpinner, addButton, removeButton);
        
        section.getChildren().addAll(monitoredPortsList, addBox);
        return section;
    }
    
    /**
     * Crée une section standard.
     */
    private VBox createSection(String title) {
        VBox section = new VBox(15);
        section.setPadding(new Insets(20));
        section.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 10; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.web("#2c3e50"));
        
        section.getChildren().add(titleLabel);
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
        
        Button saveButton = createStyledButton("💾 Save Configuration", "#27ae60", 200);
        saveButton.setOnAction(e -> handleSaveRules());
        
        Button resetButton = createStyledButton("🔄 Reset Defaults", "#e67e22", 200);
        resetButton.setOnAction(e -> handleReset());
        
        Button backButton = createStyledButton("← Back", "#95a5a6", 150);
        backButton.setOnAction(e -> handleBack());
        
        bottom.getChildren().addAll(backButton, saveButton, resetButton);
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
     * Crée un petit bouton.
     */
    private Button createSmallButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle(
            "-fx-background-color: " + color + "; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 5; " +
            "-fx-cursor: hand; " +
            "-fx-padding: 5 15;"
        );
        
        return button;
    }
    
    /**
     * Charge la configuration actuelle.
     */
    private void loadConfiguration() {
        suspiciousWordsList.getItems().setAll(config.getSuspiciousWords());
        blacklistedIPsList.getItems().setAll(config.getBlacklistedIPs());
        monitoredPortsList.getItems().setAll(config.getMonitoredPorts());
    }
    
    /**
     * Gère le changement de niveau de sécurité.
     */
    private void handleSecurityLevelChange() {
        String level = securityLevelCombo.getValue();
        
        switch (level) {
            case "Low" -> {
                blockThresholdSpinner.getValueFactory().setValue(5);
                alertThresholdSpinner.getValueFactory().setValue(3);
            }
            case "Medium" -> {
                blockThresholdSpinner.getValueFactory().setValue(3);
                alertThresholdSpinner.getValueFactory().setValue(2);
            }
            case "High" -> {
                blockThresholdSpinner.getValueFactory().setValue(2);
                alertThresholdSpinner.getValueFactory().setValue(1);
            }
            case "Maximum" -> {
                blockThresholdSpinner.getValueFactory().setValue(1);
                alertThresholdSpinner.getValueFactory().setValue(1);
            }
        }
    }
    
    // Handlers pour les mots suspects
    private void handleAddWord() {
        String word = newWordField.getText().trim();
        if (!word.isEmpty() && !suspiciousWordsList.getItems().contains(word)) {
            config.addSuspiciousWord(word);
            suspiciousWordsList.getItems().add(word);
            newWordField.clear();
        }
    }
    
    private void handleRemoveWord() {
        String selected = suspiciousWordsList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            config.removeSuspiciousWord(selected);
            suspiciousWordsList.getItems().remove(selected);
        }
    }
    
    // Handlers pour les IPs
    private void handleAddIP() {
        String ip = newIPField.getText().trim();
        if (!ip.isEmpty() && !blacklistedIPsList.getItems().contains(ip)) {
            config.addBlacklistedIP(ip);
            blacklistedIPsList.getItems().add(ip);
            newIPField.clear();
        }
    }
    
    private void handleRemoveIP() {
        String selected = blacklistedIPsList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            config.removeBlacklistedIP(selected);
            blacklistedIPsList.getItems().remove(selected);
        }
    }
    
    // Handlers pour les ports
    private void handleAddPort() {
        int port = newPortSpinner.getValue();
        if (!monitoredPortsList.getItems().contains(port)) {
            config.addMonitoredPort(port);
            monitoredPortsList.getItems().add(port);
        }
    }
    
    private void handleRemovePort() {
        Integer selected = monitoredPortsList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            config.removeMonitoredPort(selected);
            monitoredPortsList.getItems().remove(selected);
        }
    }
    
    /**
     * Sauvegarde la configuration.
     */
    private void handleSaveRules() {
        config.setBlockThreshold(blockThresholdSpinner.getValue());
        config.setAlertThreshold(alertThresholdSpinner.getValue());
        config.setMinPacketSize(minPacketSizeSpinner.getValue());
        config.setMaxPacketSize(maxPacketSizeSpinner.getValue());
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Configuration Saved");
        alert.setHeaderText(null);
        alert.setContentText("Firewall configuration has been saved successfully!");
        alert.showAndWait();
    }
    
    /**
     * Réinitialise la configuration.
     */
    private void handleReset() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Reset Configuration");
        confirm.setHeaderText("Reset to default values?");
        confirm.setContentText("This will restore all settings to default values.");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                config = new FirewallConfig();
                blockThresholdSpinner.getValueFactory().setValue(config.getBlockThreshold());
                alertThresholdSpinner.getValueFactory().setValue(config.getAlertThreshold());
                minPacketSizeSpinner.getValueFactory().setValue(config.getMinPacketSize());
                maxPacketSizeSpinner.getValueFactory().setValue(config.getMaxPacketSize());
                loadConfiguration();
            }
        });
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
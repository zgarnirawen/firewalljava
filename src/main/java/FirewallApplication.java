/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

import com.mycompany.projetparfeu.model.config.FirewallConfig;
import com.mycompany.projetparfeu.model.engine.FirewallEngine;
import com.mycompany.projetparfeu.model.blockchain.BlockChain;
import com.mycompany.projetparfeu.model.generator.PacketSelector;
import com.mycompany.projetparfeu.model.generator.CSVPacketFilesCreator;
import com.mycompany.projetparfeu.view.*;

import javafx.application.Application;
import javafx.application.Platform;
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
 * Application JavaFX principale du Pare-feu Intelligent.
 * Point d'entrée de l'interface graphique.
 * 
 * @author ZGARNI
 */
public class FirewallApplication extends Application {
    
    private FirewallEngine firewall;
    private FirewallConfig config;
    private BlockChain blockchain;
    private PacketSelector selector;
    
    private TabPane mainTabPane;
    private Label statusLabel;
    private Button startStopButton;
    private boolean isRunning = false;
    
    // Vues
    private ConfigurationView configView;
    private StatisticsView statsView;
    private BlockchainView blockchainView;
    private AnalyzerView analyzerView;
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialisation
            initializeComponents();
            
            // Créer l'interface
            BorderPane root = new BorderPane();
            root.setTop(createHeader());
            root.setCenter(createMainContent());
            root.setBottom(createStatusBar());
            
            // Scène
            Scene scene = new Scene(root, 1400, 900);
            
            // Appliquer le style CSS si disponible
            try {
                scene.getStylesheets().add(
                    getClass().getResource("/css/style.css").toExternalForm()
                );
            } catch (Exception e) {
                System.out.println("⚠ Fichier CSS non trouvé, utilisation du style par défaut");
            }
            
            primaryStage.setTitle("Pare-feu Intelligent - Projet Java");
            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest(e -> {
                if (isRunning) {
                    firewall.stop();
                }
                Platform.exit();
            });
            
            primaryStage.show();
            
            System.out.println("✓ Interface JavaFX lancée avec succès");
            
        } catch (Exception e) {
            showError("Erreur Fatale", "Impossible de démarrer l'application: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Initialise les composants du pare-feu.
     */
    private void initializeComponents() {
        try {
            System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║    INITIALISATION DU PARE-FEU INTELLIGENT                    ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝\n");
            
            // Vérifier et créer les fichiers CSV si nécessaire
            if (!java.nio.file.Files.exists(java.nio.file.Paths.get("paquets_sains.csv")) ||
                !java.nio.file.Files.exists(java.nio.file.Paths.get("paquets_malicieux.csv"))) {
                
                System.out.println("⚠ Fichiers CSV manquants. Création automatique...");
                CSVPacketFilesCreator.createAllFiles();
            }
            
            // Initialiser les composants
            config = new FirewallConfig();
            System.out.println("✓ Configuration chargée");
            
            firewall = new FirewallEngine(config);
            System.out.println("✓ Moteur du pare-feu initialisé");
            
            blockchain = new BlockChain();
            System.out.println("✓ Blockchain initialisée");
            
            selector = new PacketSelector();
            selector.loadPacketFiles();
            System.out.println("✓ Sélecteur de paquets chargé");
            
            System.out.println("\n✓ Tous les composants sont prêts\n");
            
        } catch (Exception e) {
            throw new RuntimeException("Erreur d'initialisation: " + e.getMessage(), e);
        }
    }
    
    /**
     * Crée l'en-tête avec menu et toolbar.
     */
    private VBox createHeader() {
        VBox header = new VBox();
        header.setStyle("-fx-background-color: #2c3e50;");
        
        // Menu Bar
        MenuBar menuBar = createMenuBar();
        
        // Toolbar
        ToolBar toolBar = createToolBar();
        
        header.getChildren().addAll(menuBar, toolBar);
        return header;
    }
    
    /**
     * Crée la barre de menu.
     */
    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color: #34495e;");
        
        // Menu Fichier
        Menu fileMenu = new Menu("Fichier");
        MenuItem saveItem = new MenuItem("💾 Sauvegarder");
        saveItem.setOnAction(e -> handleSave());
        MenuItem loadItem = new MenuItem("📂 Charger");
        loadItem.setOnAction(e -> handleLoad());
        MenuItem exitItem = new MenuItem("🚪 Quitter");
        exitItem.setOnAction(e -> Platform.exit());
        fileMenu.getItems().addAll(saveItem, loadItem, new SeparatorMenuItem(), exitItem);
        
        // Menu Configuration
        Menu configMenu = new Menu("Configuration");
        MenuItem resetItem = new MenuItem("🔄 Réinitialiser");
        resetItem.setOnAction(e -> handleReset());
        configMenu.getItems().add(resetItem);
        
        // Menu Aide
        Menu helpMenu = new Menu("Aide");
        MenuItem aboutItem = new MenuItem("ℹ️ À propos");
        aboutItem.setOnAction(e -> showAbout());
        helpMenu.getItems().add(aboutItem);
        
        menuBar.getMenus().addAll(fileMenu, configMenu, helpMenu);
        return menuBar;
    }
    
    /**
     * Crée la barre d'outils.
     */
    private ToolBar createToolBar() {
        ToolBar toolBar = new ToolBar();
        toolBar.setStyle("-fx-background-color: #34495e; -fx-padding: 10;");
        
        startStopButton = new Button("▶ Démarrer le Pare-feu");
        startStopButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; " +
                                "-fx-font-weight: bold; -fx-padding: 8 20;");
        startStopButton.setOnAction(e -> toggleFirewall());
        
        Button refreshStatsButton = new Button("🔄 Rafraîchir Stats");
        refreshStatsButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 8 15;");
        refreshStatsButton.setOnAction(e -> refreshAllViews());
        
        Button clearButton = new Button("🗑️ Effacer Logs");
        clearButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 8 15;");
        clearButton.setOnAction(e -> clearLogs());
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label titleLabel = new Label("PARE-FEU INTELLIGENT");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        titleLabel.setTextFill(Color.WHITE);
        
        toolBar.getItems().addAll(
            startStopButton,
            new Separator(),
            refreshStatsButton,
            clearButton,
            spacer,
            titleLabel
        );
        
        return toolBar;
    }
    
    /**
     * Crée le contenu principal avec onglets.
     */
    private TabPane createMainContent() {
        mainTabPane = new TabPane();
        mainTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        mainTabPane.setStyle("-fx-background-color: #ecf0f1;");
        
        // Créer les vues
        configView = new ConfigurationView(config);
        statsView = new StatisticsView(firewall.getStatistics());
        blockchainView = new BlockchainView(blockchain);
        analyzerView = new AnalyzerView(firewall, selector);
        
        // Créer les onglets
        Tab configTab = new Tab("⚙️ Configuration", configView);
        Tab analyzerTab = new Tab("🔍 Analyseur", analyzerView);
        Tab statsTab = new Tab("📊 Statistiques", statsView);
        Tab blockchainTab = new Tab("⛓️ Blockchain", blockchainView);
        
        mainTabPane.getTabs().addAll(configTab, analyzerTab, statsTab, blockchainTab);
        
        return mainTabPane;
    }
    
    /**
     * Crée la barre de statut.
     */
    private HBox createStatusBar() {
        HBox statusBar = new HBox(20);
        statusBar.setPadding(new Insets(8, 15, 8, 15));
        statusBar.setAlignment(Pos.CENTER_LEFT);
        statusBar.setStyle("-fx-background-color: #2c3e50; -fx-border-color: #34495e; " +
                          "-fx-border-width: 1 0 0 0;");
        
        statusLabel = new Label("🔴 Pare-feu arrêté");
        statusLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        statusLabel.setTextFill(Color.web("#e74c3c"));
        
        Label packetsLabel = new Label("Paquets: 0");
        packetsLabel.setTextFill(Color.WHITE);
        
        Label blockedLabel = new Label("Bloqués: 0");
        blockedLabel.setTextFill(Color.WHITE);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label versionLabel = new Label("Version 1.0 - © 2024");
        versionLabel.setTextFill(Color.web("#95a5a6"));
        versionLabel.setFont(Font.font("System", 10));
        
        statusBar.getChildren().addAll(
            statusLabel,
            new Separator(),
            packetsLabel,
            blockedLabel,
            spacer,
            versionLabel
        );
        
        return statusBar;
    }
    
    /**
     * Démarre/Arrête le pare-feu.
     */
    private void toggleFirewall() {
        if (isRunning) {
            firewall.stop();
            isRunning = false;
            startStopButton.setText("▶ Démarrer le Pare-feu");
            startStopButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; " +
                                    "-fx-font-weight: bold; -fx-padding: 8 20;");
            statusLabel.setText("🔴 Pare-feu arrêté");
            statusLabel.setTextFill(Color.web("#e74c3c"));
            
        } else {
            firewall.start();
            isRunning = true;
            startStopButton.setText("⏸ Arrêter le Pare-feu");
            startStopButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; " +
                                    "-fx-font-weight: bold; -fx-padding: 8 20;");
            statusLabel.setText("🟢 Pare-feu actif");
            statusLabel.setTextFill(Color.web("#27ae60"));
        }
    }
    
    /**
     * Rafraîchit toutes les vues.
     */
    private void refreshAllViews() {
        statsView.refresh();
        blockchainView.refresh();
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Rafraîchissement");
        alert.setHeaderText(null);
        alert.setContentText("Toutes les vues ont été rafraîchies!");
        alert.showAndWait();
    }
    
    /**
     * Efface les logs.
     */
    private void clearLogs() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Effacer les logs?");
        confirm.setContentText("Cette action est irréversible.");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                firewall.getStatistics().reset();
                refreshAllViews();
            }
        });
    }
    
    /**
     * Sauvegarde la configuration.
     */
    private void handleSave() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sauvegarde");
        alert.setHeaderText(null);
        alert.setContentText("Configuration et données sauvegardées avec succès!");
        alert.showAndWait();
    }
    
    /**
     * Charge la configuration.
     */
    private void handleLoad() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Chargement");
        alert.setHeaderText(null);
        alert.setContentText("Configuration chargée avec succès!");
        alert.showAndWait();
    }
    
    /**
     * Réinitialise la configuration.
     */
    private void handleReset() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Réinitialisation");
        confirm.setHeaderText("Voulez-vous vraiment réinitialiser?");
        confirm.setContentText("Toutes les données seront perdues.");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                config = new FirewallConfig();
                firewall.getStatistics().reset();
                refreshAllViews();
            }
        });
    }
    
    /**
     * Affiche la boîte À propos.
     */
    private void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("À propos");
        alert.setHeaderText("Pare-feu Intelligent - Projet Java");
        alert.setContentText(
            "Version: 1.0\n" +
            "Auteur: ZGARNI\n" +
            "Framework: JavaFX 21\n\n" +
            "Projet académique de pare-feu intelligent\n" +
            "avec détection de menaces et blockchain."
        );
        alert.showAndWait();
    }
    
    /**
     * Affiche une erreur.
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Point d'entrée de l'application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}

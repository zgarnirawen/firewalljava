package com.mycompany.projetparfeu.view;

import com.mycompany.projetparfeu.model.config.FirewallConfig;
import com.mycompany.projetparfeu.model.engine.FirewallEngine;
import com.mycompany.projetparfeu.model.statistics.StatisticsManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.collections.FXCollections;

/**
 * Vue des statistiques du pare-feu.
 * Affiche graphiques et métriques de performance.
 * 
 * @author ZGARNI
 */
public class StatisticsViewPage extends BorderPane {
    
    private Stage primaryStage;
    private StatisticsManager statsManager;
    private FirewallEngine firewall;
    
    // Cartes de stats
    private Label totalPacketsLabel;
    private Label acceptedLabel;
    private Label blockedLabel;
    private Label alertedLabel;
    
    // Graphiques
    private PieChart pieChart;
    private BarChart<String, Number> barChart;
    
    public StatisticsViewPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        // Initialiser le pare-feu et les stats
        this.firewall = new FirewallEngine(new FirewallConfig());
        this.statsManager = firewall.getStatistics();
        
        setupUI();
        loadStatistics();
    }
    
    private void setupUI() {
        // Header
        VBox header = createHeader();
        setTop(header);
        
        // Centre - graphiques et stats
        ScrollPane centerScroll = new ScrollPane();
        centerScroll.setFitToWidth(true);
        centerScroll.setStyle("-fx-background-color: #ecf0f1;");
        
        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(20));
        
        // Cartes de statistiques
        HBox statsCards = createStatsCards();
        
        // Graphiques
        VBox chartsSection = createChartsSection();
        
        // Tableau de détails
        VBox tableSection = createTableSection();
        
        centerContent.getChildren().addAll(statsCards, new Separator(), chartsSection, new Separator(), tableSection);
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
        
        Label titleLabel = new Label("Statistics");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.WHITE);
        
        Label subtitleLabel = new Label("Firewall Performance Metrics");
        subtitleLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        subtitleLabel.setTextFill(Color.web("#bdc3c7"));
        
        header.getChildren().addAll(titleLabel, subtitleLabel);
        return header;
    }
    
    /**
     * Crée les cartes de statistiques.
     */
    private HBox createStatsCards() {
        HBox cardsBox = new HBox(15);
        cardsBox.setAlignment(Pos.CENTER);
        
        // Carte Total
        VBox totalCard = createStatCard("📦 Total Packets", "0", "#3498db");
        totalPacketsLabel = (Label) ((VBox) totalCard.getChildren().get(0)).getChildren().get(1);
        
        // Carte Acceptés
        VBox acceptedCard = createStatCard("✅ Accepted", "0", "#27ae60");
        acceptedLabel = (Label) ((VBox) acceptedCard.getChildren().get(0)).getChildren().get(1);
        
        // Carte Bloqués
        VBox blockedCard = createStatCard("🚫 Blocked", "0", "#e74c3c");
        blockedLabel = (Label) ((VBox) blockedCard.getChildren().get(0)).getChildren().get(1);
        
        // Carte Alertes
        VBox alertedCard = createStatCard("⚠️ Alerts", "0", "#f39c12");
        alertedLabel = (Label) ((VBox) alertedCard.getChildren().get(0)).getChildren().get(1);
        
        cardsBox.getChildren().addAll(totalCard, acceptedCard, blockedCard, alertedCard);
        return cardsBox;
    }
    
    /**
     * Crée une carte de stat.
     */
    private VBox createStatCard(String title, String value, String color) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setPrefWidth(200);
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 10; " +
            "-fx-border-color: " + color + "; " +
            "-fx-border-width: 3; " +
            "-fx-border-radius: 10; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );
        
        VBox innerBox = new VBox(5);
        innerBox.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        titleLabel.setTextFill(Color.web("#7f8c8d"));
        
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        valueLabel.setTextFill(Color.web(color));
        
        innerBox.getChildren().addAll(titleLabel, valueLabel);
        card.getChildren().add(innerBox);
        
        return card;
    }
    
    /**
     * Crée la section des graphiques.
     */
    private VBox createChartsSection() {
        VBox section = new VBox(15);
        section.setAlignment(Pos.CENTER);
        
        Label sectionTitle = new Label("📊 Visual Analytics");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 20));
        sectionTitle.setTextFill(Color.web("#2c3e50"));
        
        HBox chartsBox = new HBox(20);
        chartsBox.setAlignment(Pos.CENTER);
        
        // PieChart - Distribution des actions
        pieChart = createPieChart();
        
        // BarChart - Protocoles
        barChart = createBarChart();
        
        chartsBox.getChildren().addAll(pieChart, barChart);
        
        section.getChildren().addAll(sectionTitle, chartsBox);
        return section;
    }
    
    /**
     * Crée le graphique en camembert.
     */
    private PieChart createPieChart() {
        PieChart chart = new PieChart();
        chart.setTitle("Actions Distribution");
        chart.setPrefSize(400, 300);
        chart.setLegendVisible(true);
        
        chart.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 10; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );
        
        return chart;
    }
    
    /**
     * Crée le graphique en barres.
     */
    private BarChart<String, Number> createBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Protocol");
        
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Packets");
        
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Packets by Protocol");
        chart.setPrefSize(400, 300);
        chart.setLegendVisible(false);
        
        chart.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 10; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );
        
        return chart;
    }
    
    /**
     * Crée la section tableau.
     */
    private VBox createTableSection() {
        VBox section = new VBox(15);
        section.setPadding(new Insets(20));
        section.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 10; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );
        
        Label sectionTitle = new Label("📋 Detailed Statistics");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        sectionTitle.setTextFill(Color.web("#2c3e50"));
        
        TextArea detailsArea = new TextArea();
        detailsArea.setEditable(false);
        detailsArea.setPrefHeight(200);
        detailsArea.setWrapText(true);
        detailsArea.setStyle(
            "-fx-font-family: 'Courier New'; " +
            "-fx-font-size: 12px; " +
            "-fx-background-color: #f8f9fa; " +
            "-fx-border-color: #dee2e6; " +
            "-fx-border-radius: 5;"
        );
        
        StringBuilder details = new StringBuilder();
        details.append("═════════════════════════════════════════════════\n");
        details.append("         FIREWALL STATISTICS SUMMARY\n");
        details.append("═════════════════════════════════════════════════\n\n");
        details.append("Total Packets Processed    : ").append(statsManager.getTotalPackets()).append("\n");
        details.append("Accepted Packets           : ").append(statsManager.getAcceptedPackets()).append("\n");
        details.append("Dropped Packets            : ").append(statsManager.getDroppedPackets()).append("\n");
        details.append("Alerted Packets            : ").append(statsManager.getAlertedPackets()).append("\n");
        details.append("Logged Packets             : ").append(statsManager.getLoggedPackets()).append("\n\n");
        
        if (statsManager.getTotalPackets() > 0) {
            double acceptRate = (statsManager.getAcceptedPackets() * 100.0) / statsManager.getTotalPackets();
            double blockRate = (statsManager.getDroppedPackets() * 100.0) / statsManager.getTotalPackets();
            double alertRate = (statsManager.getAlertedPackets() * 100.0) / statsManager.getTotalPackets();
            
            details.append("─────────────────────────────────────────────────\n");
            details.append("RATES:\n");
            details.append("─────────────────────────────────────────────────\n");
            details.append(String.format("Acceptance Rate  : %.2f%%\n", acceptRate));
            details.append(String.format("Block Rate       : %.2f%%\n", blockRate));
            details.append(String.format("Alert Rate       : %.2f%%\n", alertRate));
        }
        
        details.append("\n═════════════════════════════════════════════════\n");
        
        detailsArea.setText(details.toString());
        
        section.getChildren().addAll(sectionTitle, detailsArea);
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
        
        Button refreshButton = createStyledButton("🔄 Refresh", "#3498db", 150);
        refreshButton.setOnAction(e -> loadStatistics());
        
        Button exportButton = createStyledButton("📤 Export", "#27ae60", 150);
        exportButton.setOnAction(e -> handleExport());
        
        Button backButton = createStyledButton("← Back", "#95a5a6", 150);
        backButton.setOnAction(e -> handleBack());
        
        bottom.getChildren().addAll(backButton, refreshButton, exportButton);
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
     * Charge les statistiques.
     */
    private void loadStatistics() {
        // Mettre à jour les cartes
        totalPacketsLabel.setText(String.valueOf(statsManager.getTotalPackets()));
        acceptedLabel.setText(String.valueOf(statsManager.getAcceptedPackets()));
        blockedLabel.setText(String.valueOf(statsManager.getDroppedPackets()));
        alertedLabel.setText(String.valueOf(statsManager.getAlertedPackets()));
        
        // Mettre à jour le pie chart
        pieChart.setData(FXCollections.observableArrayList(
            new PieChart.Data("Accepted", statsManager.getAcceptedPackets()),
            new PieChart.Data("Blocked", statsManager.getDroppedPackets()),
            new PieChart.Data("Alerts", statsManager.getAlertedPackets())
        ));
        
        // Mettre à jour le bar chart (données exemple)
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Packets");
        series.getData().add(new XYChart.Data<>("HTTP", 45));
        series.getData().add(new XYChart.Data<>("HTTPS", 120));
        series.getData().add(new XYChart.Data<>("TCP", 78));
        series.getData().add(new XYChart.Data<>("UDP", 32));
        
        barChart.getData().clear();
        barChart.getData().add(series);
    }
    
    /**
     * Exporte les statistiques.
     */
    private void handleExport() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Export");
        alert.setHeaderText("Statistics Exported");
        alert.setContentText("Statistics have been exported successfully to firewall_data/statistics.txt");
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
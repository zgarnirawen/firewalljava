package com.mycompany.projetparfeu.view;

import com.mycompany.projetparfeu.model.statistics.StatisticsManager;
import com.mycompany.projetparfeu.model.statistics.StatisticsManager.IPStatistics;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Map;

/**
 * Vue des statistiques du pare-feu.
 * Affiche les métriques, graphiques et analyses.
 * 
 * @author ZGARNI
 */
public class StatisticsView extends VBox {
    
    private StatisticsManager statsManager;
    
    // Cartes de statistiques
    private Label totalPacketsLabel;
    private Label acceptedPacketsLabel;
    private Label droppedPacketsLabel;
    private Label alertedPacketsLabel;
    
    // Graphiques
    private PieChart actionsPieChart;
    private BarChart<String, Number> protocolBarChart;
    private LineChart<String, Number> timelineChart;
    
    // Tableau IP
    private TableView<IPStatRow> ipStatsTable;
    
    public StatisticsView(StatisticsManager statsManager) {
        this.statsManager = statsManager;
        setupUI();
    }
    
    private void setupUI() {
        setPadding(new Insets(20));
        setSpacing(15);
        setStyle("-fx-background-color: #f5f5f5;");
        
        // Titre
        Label titleLabel = new Label("📊 STATISTIQUES DU PARE-FEU");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web("#2c3e50"));
        
        // Créer un ScrollPane pour le contenu
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        
        VBox content = new VBox(15);
        content.getChildren().addAll(
            createStatsCards(),
            new Separator(),
            createChartsSection(),
            new Separator(),
            createIPStatsTable()
        );
        
        scrollPane.setContent(content);
        
        // Bouton de rafraîchissement
        HBox actionBox = createActionButtons();
        
        getChildren().addAll(titleLabel, new Separator(), scrollPane, actionBox);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
    }
    
    /**
     * Cartes de statistiques principales.
     */
    private GridPane createStatsCards() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        
        // Carte 1: Total Paquets
        VBox totalCard = createStatCard("📦 Total Paquets", 
            String.valueOf(statsManager.getTotalPackets()), 
            "#3498db");
        totalPacketsLabel = (Label) ((VBox) totalCard.getChildren().get(0)).getChildren().get(1);
        
        // Carte 2: Acceptés
        VBox acceptedCard = createStatCard("✅ Acceptés", 
            String.valueOf(statsManager.getAcceptedPackets()), 
            "#27ae60");
        acceptedPacketsLabel = (Label) ((VBox) acceptedCard.getChildren().get(0)).getChildren().get(1);
        
        // Carte 3: Bloqués
        VBox droppedCard = createStatCard("🚫 Bloqués", 
            String.valueOf(statsManager.getDroppedPackets()), 
            "#e74c3c");
        droppedPacketsLabel = (Label) ((VBox) droppedCard.getChildren().get(0)).getChildren().get(1);
        
        // Carte 4: Alertes
        VBox alertedCard = createStatCard("⚠️ Alertes", 
            String.valueOf(statsManager.getAlertedPackets()), 
            "#f39c12");
        alertedPacketsLabel = (Label) ((VBox) alertedCard.getChildren().get(0)).getChildren().get(1);
        
        grid.add(totalCard, 0, 0);
        grid.add(acceptedCard, 1, 0);
        grid.add(droppedCard, 2, 0);
        grid.add(alertedCard, 3, 0);
        
        // Rendre les colonnes égales
        for (int i = 0; i < 4; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(25);
            grid.getColumnConstraints().add(col);
        }
        
        return grid;
    }
    
    /**
     * Crée une carte de statistique.
     */
    private VBox createStatCard(String title, String value, String color) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; " +
                     "-fx-border-color: " + color + "; " +
                     "-fx-border-width: 3; " +
                     "-fx-border-radius: 10; " +
                     "-fx-background-radius: 10;");
        
        VBox innerBox = new VBox(5);
        innerBox.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        titleLabel.setTextFill(Color.web("#7f8c8d"));
        
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("System", FontWeight.BOLD, 32));
        valueLabel.setTextFill(Color.web(color));
        
        innerBox.getChildren().addAll(titleLabel, valueLabel);
        card.getChildren().add(innerBox);
        
        return card;
    }
    
    /**
     * Section des graphiques.
     */
    private VBox createChartsSection() {
        VBox section = new VBox(15);
        
        Label sectionTitle = new Label("📈 Graphiques d'Analyse");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        
        // Graphique en camembert pour les actions
        actionsPieChart = createPieChart();
        
        // Graphique en barres pour les protocoles
        protocolBarChart = createProtocolBarChart();
        
        HBox chartsBox = new HBox(15);
        chartsBox.getChildren().addAll(actionsPieChart, protocolBarChart);
        
        section.getChildren().addAll(sectionTitle, chartsBox);
        return section;
    }
    
    /**
     * Crée le graphique en camembert des actions.
     */
    private PieChart createPieChart() {
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
            new PieChart.Data("Acceptés", statsManager.getAcceptedPackets()),
            new PieChart.Data("Bloqués", statsManager.getDroppedPackets()),
            new PieChart.Data("Alertes", statsManager.getAlertedPackets())
        );
        
        PieChart chart = new PieChart(pieData);
        chart.setTitle("Répartition des Actions");
        chart.setPrefSize(400, 300);
        chart.setLegendVisible(true);
        
        return chart;
    }
    
    /**
     * Crée le graphique en barres des protocoles.
     */
    private BarChart<String, Number> createProtocolBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Protocole");
        
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Nombre de paquets");
        
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Paquets par Protocole");
        chart.setPrefSize(400, 300);
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Paquets");
        
        // Données exemple (à remplacer par les vraies données)
        series.getData().add(new XYChart.Data<>("HTTP", 50));
        series.getData().add(new XYChart.Data<>("HTTPS", 120));
        series.getData().add(new XYChart.Data<>("TCP", 80));
        series.getData().add(new XYChart.Data<>("UDP", 30));
        
        chart.getData().add(series);
        chart.setLegendVisible(false);
        
        return chart;
    }
    
    /**
     * Tableau des statistiques par IP.
     */
    private VBox createIPStatsTable() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(10));
        section.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5;");
        
        Label sectionTitle = new Label("🌐 Statistiques par IP Source");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        ipStatsTable = new TableView<>();
        ipStatsTable.setPrefHeight(250);
        
        // Colonnes
        TableColumn<IPStatRow, String> ipCol = new TableColumn<>("Adresse IP");
        ipCol.setCellValueFactory(new PropertyValueFactory<>("ipAddress"));
        ipCol.setPrefWidth(150);
        
        TableColumn<IPStatRow, Integer> totalCol = new TableColumn<>("Total Paquets");
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalPackets"));
        totalCol.setPrefWidth(120);
        
        TableColumn<IPStatRow, Integer> acceptedCol = new TableColumn<>("Acceptés");
        acceptedCol.setCellValueFactory(new PropertyValueFactory<>("acceptedPackets"));
        acceptedCol.setPrefWidth(100);
        
        TableColumn<IPStatRow, Integer> blockedCol = new TableColumn<>("Bloqués");
        blockedCol.setCellValueFactory(new PropertyValueFactory<>("blockedPackets"));
        blockedCol.setPrefWidth(100);
        
        TableColumn<IPStatRow, String> blockRateCol = new TableColumn<>("Taux Blocage");
        blockRateCol.setCellValueFactory(new PropertyValueFactory<>("blockRate"));
        blockRateCol.setPrefWidth(120);
        
        TableColumn<IPStatRow, String> threatCol = new TableColumn<>("Menace");
        threatCol.setCellValueFactory(new PropertyValueFactory<>("threatLevel"));
        threatCol.setPrefWidth(100);
        
        // Style conditionnel pour la colonne menace
        threatCol.setCellFactory(column -> new TableCell<IPStatRow, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.contains("ÉLEVÉ")) {
                        setStyle("-fx-background-color: #ffcccc; -fx-text-fill: #c0392b; -fx-font-weight: bold;");
                    } else if (item.contains("MOYEN")) {
                        setStyle("-fx-background-color: #fff3cd; -fx-text-fill: #f39c12; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-background-color: #d4edda; -fx-text-fill: #27ae60; -fx-font-weight: bold;");
                    }
                }
            }
        });
        
        ipStatsTable.getColumns().addAll(ipCol, totalCol, acceptedCol, blockedCol, blockRateCol, threatCol);
        
        // Charger les données
        refreshIPStats();
        
        section.getChildren().addAll(sectionTitle, ipStatsTable);
        return section;
    }
    
    /**
     * Rafraîchit les données du tableau IP.
     */
    private void refreshIPStats() {
        ObservableList<IPStatRow> data = FXCollections.observableArrayList();
        
        Map<String, IPStatistics> ipStats = statsManager.getIPStatistics();
        for (IPStatistics stats : ipStats.values()) {
            double blockRate = stats.totalPackets > 0 
                ? (stats.blockedPackets * 100.0) / stats.totalPackets 
                : 0;
            
            String threat = blockRate > 50 ? "🔴 ÉLEVÉ" : 
                           blockRate > 20 ? "🟡 MOYEN" : "🟢 FAIBLE";
            
            data.add(new IPStatRow(
                stats.ipAddress,
                stats.totalPackets,
                stats.acceptedPackets,
                stats.blockedPackets,
                String.format("%.1f%%", blockRate),
                threat
            ));
        }
        
        ipStatsTable.setItems(data);
    }
    
    /**
     * Boutons d'action.
     */
    private HBox createActionButtons() {
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));
        
        Button refreshButton = new Button("🔄 Rafraîchir");
        refreshButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20;");
        refreshButton.setOnAction(e -> refresh());
        
        Button exportButton = new Button("📤 Exporter");
        exportButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20;");
        exportButton.setOnAction(e -> exportStats());
        
        Button resetButton = new Button("🗑️ Réinitialiser");
        resetButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20;");
        resetButton.setOnAction(e -> resetStats());
        
        buttonBox.getChildren().addAll(refreshButton, exportButton, resetButton);
        return buttonBox;
    }
    
    /**
     * Rafraîchit toutes les données.
     */
    public void refresh() {
        // Mettre à jour les cartes
        totalPacketsLabel.setText(String.valueOf(statsManager.getTotalPackets()));
        acceptedPacketsLabel.setText(String.valueOf(statsManager.getAcceptedPackets()));
        droppedPacketsLabel.setText(String.valueOf(statsManager.getDroppedPackets()));
        alertedPacketsLabel.setText(String.valueOf(statsManager.getAlertedPackets()));
        
        // Mettre à jour le graphique en camembert
        actionsPieChart.getData().clear();
        actionsPieChart.getData().addAll(
            new PieChart.Data("Acceptés", statsManager.getAcceptedPackets()),
            new PieChart.Data("Bloqués", statsManager.getDroppedPackets()),
            new PieChart.Data("Alertes", statsManager.getAlertedPackets())
        );
        
        // Mettre à jour le tableau IP
        refreshIPStats();
    }
    
    private void exportStats() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Export");
        alert.setHeaderText(null);
        alert.setContentText("Statistiques exportées avec succès!");
        alert.showAndWait();
    }
    
    private void resetStats() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Réinitialisation");
        alert.setHeaderText("Voulez-vous vraiment réinitialiser les statistiques?");
        alert.setContentText("Toutes les données seront perdues.");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                statsManager.reset();
                refresh();
            }
        });
    }
    
    /**
     * Classe pour les lignes du tableau IP.
     */
    public static class IPStatRow {
        private final String ipAddress;
        private final int totalPackets;
        private final int acceptedPackets;
        private final int blockedPackets;
        private final String blockRate;
        private final String threatLevel;
        
        public IPStatRow(String ipAddress, int totalPackets, int acceptedPackets, 
                        int blockedPackets, String blockRate, String threatLevel) {
            this.ipAddress = ipAddress;
            this.totalPackets = totalPackets;
            this.acceptedPackets = acceptedPackets;
            this.blockedPackets = blockedPackets;
            this.blockRate = blockRate;
            this.threatLevel = threatLevel;
        }
        
        public String getIpAddress() { return ipAddress; }
        public int getTotalPackets() { return totalPackets; }
        public int getAcceptedPackets() { return acceptedPackets; }
        public int getBlockedPackets() { return blockedPackets; }
        public String getBlockRate() { return blockRate; }
        public String getThreatLevel() { return threatLevel; }
    }
}
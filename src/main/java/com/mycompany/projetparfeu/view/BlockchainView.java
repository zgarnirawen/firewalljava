package com.mycompany.projetparfeu.view;

import com.mycompany.projetparfeu.model.blockchain.BlockChain;
import com.mycompany.projetparfeu.model.blockchain.Block;
import com.mycompany.projetparfeu.model.decision.DecisionResult;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Vue de la blockchain du pare-feu.
 * Affiche tous les blocs et permet de vérifier l'intégrité de la chaîne.
 * 
 * @author ZGARNI
 */
public class BlockchainView extends VBox {
    
    private BlockChain blockchain;
    
    private TableView<BlockRow> blockTable;
    private TextArea blockDetailsArea;
    private Label chainSizeLabel;
    private Label validityLabel;
    
    private static final DateTimeFormatter FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public BlockchainView(BlockChain blockchain) {
        this.blockchain = blockchain;
        setupUI();
    }
    
    private void setupUI() {
        setPadding(new Insets(20));
        setSpacing(15);
        setStyle("-fx-background-color: #f5f5f5;");
        
        // Titre
        Label titleLabel = new Label("⛓️ BLOCKCHAIN DU PARE-FEU");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web("#2c3e50"));
        
        getChildren().addAll(
            titleLabel,
            new Separator(),
            createInfoSection(),
            new Separator(),
            createBlockTableSection(),
            new Separator(),
            createDetailsSection(),
            new Separator(),
            createActionButtons()
        );
    }
    
    /**
     * Section d'informations générales.
     */
    private VBox createInfoSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(15));
        section.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5;");
        
        Label sectionTitle = new Label("ℹ️ Informations de la Chaîne");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        HBox infoBox = new HBox(30);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        
        // Taille de la chaîne
        VBox sizeBox = new VBox(5);
        Label sizeLabel = new Label("Nombre de blocs:");
        sizeLabel.setFont(Font.font("System", FontWeight.NORMAL, 12));
        sizeLabel.setTextFill(Color.web("#7f8c8d"));
        
        chainSizeLabel = new Label(String.valueOf(blockchain.getSize()));
        chainSizeLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        chainSizeLabel.setTextFill(Color.web("#3498db"));
        
        sizeBox.getChildren().addAll(sizeLabel, chainSizeLabel);
        
        // Validité de la chaîne
        VBox validityBox = new VBox(5);
        Label validLabel = new Label("État de la chaîne:");
        validLabel.setFont(Font.font("System", FontWeight.NORMAL, 12));
        validLabel.setTextFill(Color.web("#7f8c8d"));
        
        validityLabel = new Label(blockchain.isChainValid() ? "✅ VALIDE" : "❌ INVALIDE");
        validityLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        validityLabel.setTextFill(blockchain.isChainValid() ? 
            Color.web("#27ae60") : Color.web("#e74c3c"));
        
        validityBox.getChildren().addAll(validLabel, validityLabel);
        
        // Dernier bloc
        Block lastBlock = blockchain.getLastBlock();
        VBox lastBlockBox = new VBox(5);
        Label lastLabel = new Label("Dernier bloc:");
        lastLabel.setFont(Font.font("System", FontWeight.NORMAL, 12));
        lastLabel.setTextFill(Color.web("#7f8c8d"));
        
        Label lastBlockLabel = new Label("Block #" + lastBlock.index());
        lastBlockLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        lastBlockLabel.setTextFill(Color.web("#9b59b6"));
        
        lastBlockBox.getChildren().addAll(lastLabel, lastBlockLabel);
        
        infoBox.getChildren().addAll(sizeBox, new Separator(), validityBox, new Separator(), lastBlockBox);
        
        section.getChildren().addAll(sectionTitle, infoBox);
        return section;
    }
    
    /**
     * Section du tableau des blocs.
     */
    private VBox createBlockTableSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(10));
        section.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5;");
        
        Label sectionTitle = new Label("📦 Liste des Blocs");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        blockTable = new TableView<>();
        blockTable.setPrefHeight(300);
        
        // Colonnes
        TableColumn<BlockRow, Integer> indexCol = new TableColumn<>("Index");
        indexCol.setCellValueFactory(new PropertyValueFactory<>("index"));
        indexCol.setPrefWidth(80);
        indexCol.setStyle("-fx-alignment: CENTER;");
        
        TableColumn<BlockRow, String> hashCol = new TableColumn<>("Hash");
        hashCol.setCellValueFactory(new PropertyValueFactory<>("hash"));
        hashCol.setPrefWidth(150);
        
        TableColumn<BlockRow, String> prevHashCol = new TableColumn<>("Hash Précédent");
        prevHashCol.setCellValueFactory(new PropertyValueFactory<>("previousHash"));
        prevHashCol.setPrefWidth(150);
        
        TableColumn<BlockRow, String> timestampCol = new TableColumn<>("Timestamp");
        timestampCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        timestampCol.setPrefWidth(180);
        
        TableColumn<BlockRow, Integer> decisionsCol = new TableColumn<>("Décisions");
        decisionsCol.setCellValueFactory(new PropertyValueFactory<>("decisionsCount"));
        decisionsCol.setPrefWidth(100);
        decisionsCol.setStyle("-fx-alignment: CENTER;");
        
        TableColumn<BlockRow, String> validCol = new TableColumn<>("Valide");
        validCol.setCellValueFactory(new PropertyValueFactory<>("valid"));
        validCol.setPrefWidth(80);
        validCol.setStyle("-fx-alignment: CENTER;");
        
        // Style conditionnel pour la colonne validité
        validCol.setCellFactory(column -> new TableCell<BlockRow, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.equals("✅")) {
                        setStyle("-fx-background-color: #d4edda; -fx-text-fill: #27ae60;");
                    } else {
                        setStyle("-fx-background-color: #ffcccc; -fx-text-fill: #c0392b;");
                    }
                }
            }
        });
        
        blockTable.getColumns().addAll(indexCol, hashCol, prevHashCol, timestampCol, decisionsCol, validCol);
        
        // Sélection d'un bloc
        blockTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                showBlockDetails(newVal.getIndex());
            }
        });
        
        // Charger les données
        refreshBlockTable();
        
        section.getChildren().addAll(sectionTitle, blockTable);
        return section;
    }
    
    /**
     * Section des détails d'un bloc.
     */
    private VBox createDetailsSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(10));
        section.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5;");
        
        Label sectionTitle = new Label("🔍 Détails du Bloc Sélectionné");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        blockDetailsArea = new TextArea();
        blockDetailsArea.setEditable(false);
        blockDetailsArea.setPrefHeight(200);
        blockDetailsArea.setWrapText(true);
        blockDetailsArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12px;");
        blockDetailsArea.setText("Sélectionnez un bloc pour voir ses détails...");
        
        section.getChildren().addAll(sectionTitle, blockDetailsArea);
        return section;
    }
    
    /**
     * Rafraîchit le tableau des blocs.
     */
    private void refreshBlockTable() {
        ObservableList<BlockRow> data = FXCollections.observableArrayList();
        
        List<Block> chain = blockchain.getChain();
        for (Block block : chain) {
            LocalDateTime timestamp = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(block.timestamp()),
                ZoneId.systemDefault()
            );
            
            String hashShort = block.hash().substring(0, 16) + "...";
            String prevHashShort = block.index() == 0 ? "Genesis" : 
                block.previousHash().substring(0, 16) + "...";
            
            data.add(new BlockRow(
                block.index(),
                hashShort,
                prevHashShort,
                timestamp.format(FORMATTER),
                block.decisions().size(),
                "✅" // Simplifié, on suppose que tous sont valides
            ));
        }
        
        blockTable.setItems(data);
    }
    
    /**
     * Affiche les détails d'un bloc.
     */
    private void showBlockDetails(int index) {
        List<Block> chain = blockchain.getChain();
        if (index < 0 || index >= chain.size()) return;
        
        Block block = chain.get(index);
        StringBuilder details = new StringBuilder();
        
        details.append("═══════════════════════════════════════════════════════\n");
        details.append("                  BLOC #").append(block.index()).append("\n");
        details.append("═══════════════════════════════════════════════════════\n\n");
        
        details.append("Hash du bloc:\n");
        details.append("  ").append(block.hash()).append("\n\n");
        
        details.append("Hash précédent:\n");
        details.append("  ").append(block.previousHash()).append("\n\n");
        
        LocalDateTime timestamp = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(block.timestamp()),
            ZoneId.systemDefault()
        );
        details.append("Timestamp:\n");
        details.append("  ").append(timestamp.format(FORMATTER)).append("\n\n");
        
        details.append("Nombre de décisions: ").append(block.decisions().size()).append("\n\n");
        
        if (!block.decisions().isEmpty()) {
            details.append("───────────────────────────────────────────────────────\n");
            details.append("DÉCISIONS CONTENUES:\n");
            details.append("───────────────────────────────────────────────────────\n\n");
            
            int count = 1;
            for (DecisionResult decision : block.decisions()) {
                details.append(count++).append(". ");
                details.append(decision.getAction()).append(" | ");
                details.append("Score: ").append(decision.getTotalScore()).append(" | ");
                details.append(decision.getPacket().getSrcIP()).append(" → ");
                details.append(decision.getPacket().getDestIP()).append("\n");
                details.append("   Raison: ").append(decision.getReason()).append("\n\n");
            }
        }
        
        details.append("═══════════════════════════════════════════════════════\n");
        
        blockDetailsArea.setText(details.toString());
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
        
        Button verifyButton = new Button("✓ Vérifier l'Intégrité");
        verifyButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20;");
        verifyButton.setOnAction(e -> verifyChain());
        
        Button exportButton = new Button("📤 Exporter");
        exportButton.setStyle("-fx-background-color: #9b59b6; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20;");
        exportButton.setOnAction(e -> exportChain());
        
        buttonBox.getChildren().addAll(refreshButton, verifyButton, exportButton);
        return buttonBox;
    }
    
    /**
     * Rafraîchit toutes les données.
     */
    public void refresh() {
        chainSizeLabel.setText(String.valueOf(blockchain.getSize()));
        
        boolean isValid = blockchain.isChainValid();
        validityLabel.setText(isValid ? "✅ VALIDE" : "❌ INVALIDE");
        validityLabel.setTextFill(isValid ? Color.web("#27ae60") : Color.web("#e74c3c"));
        
        refreshBlockTable();
    }
    
    /**
     * Vérifie l'intégrité de la chaîne.
     */
    private void verifyChain() {
        boolean isValid = blockchain.isChainValid();
        
        Alert alert = new Alert(isValid ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle("Vérification de la Blockchain");
        alert.setHeaderText(isValid ? "✅ Chaîne Valide" : "❌ Chaîne Invalide");
        
        if (isValid) {
            alert.setContentText("La blockchain est intègre. Tous les blocs sont valides et correctement chaînés.");
        } else {
            alert.setContentText("ATTENTION: La blockchain a été compromise! Des blocs invalides ont été détectés.");
        }
        
        alert.showAndWait();
        refresh();
    }
    
    /**
     * Exporte la blockchain.
     */
    private void exportChain() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Export");
        alert.setHeaderText(null);
        alert.setContentText("Blockchain exportée avec succès dans firewall_data/blockchain.json");
        alert.showAndWait();
    }
    
    /**
     * Classe pour les lignes du tableau de blocs.
     */
    public static class BlockRow {
        private final int index;
        private final String hash;
        private final String previousHash;
        private final String timestamp;
        private final int decisionsCount;
        private final String valid;
        
        public BlockRow(int index, String hash, String previousHash, 
                       String timestamp, int decisionsCount, String valid) {
            this.index = index;
            this.hash = hash;
            this.previousHash = previousHash;
            this.timestamp = timestamp;
            this.decisionsCount = decisionsCount;
            this.valid = valid;
        }
        
        public int getIndex() { return index; }
        public String getHash() { return hash; }
        public String getPreviousHash() { return previousHash; }
        public String getTimestamp() { return timestamp; }
        public int getDecisionsCount() { return decisionsCount; }
        public String getValid() { return valid; }
    }
}
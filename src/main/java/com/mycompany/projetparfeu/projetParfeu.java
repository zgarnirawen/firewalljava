/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.projetparfeu;


import com.mycompany.projetparfeu.model.config.FirewallConfig;
import com.mycompany.projetparfeu.model.engine.FirewallEngine;
import com.mycompany.projetparfeu.model.generator.*;
import com.mycompany.projetparfeu.model.decision.DecisionResult;
import com.mycompany.projetparfeu.model.blockchain.BlockChain;
import com.mycompany.projetparfeu.model.persistence.DataPersistence;

import java.util.List;
import java.util.Scanner;

/**
 * Application principale du pare-feu intelligent.
 * Version simplifiée avec configuration figée et sélection de paquets depuis fichiers CSV.
 * 
 * @author ZGARNI
 */
public class ProjetParfeu {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║         PARE-FEU INTELLIGENT - PROJET JAVA                   ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        
        try {
            // ========== ÉTAPE 1 : VÉRIFICATION DES FICHIERS ==========
            System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("ÉTAPE 1 : VÉRIFICATION DES FICHIERS CSV");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            
            // Vérifier si les fichiers existent, sinon les créer
            if (!java.nio.file.Files.exists(java.nio.file.Paths.get("paquets_sains.csv")) ||
                !java.nio.file.Files.exists(java.nio.file.Paths.get("paquets_malicieux.csv"))) {
                
                System.out.println("⚠ Fichiers CSV manquants. Création automatique...\n");
                CSVPacketFilesCreator.createAllFiles();
            } else {
                System.out.println("✓ Fichiers CSV trouvés");
            }
            
            // ========== ÉTAPE 2 : INITIALISATION ==========
            System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("ÉTAPE 2 : INITIALISATION DU SYSTÈME");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            
            // Configuration figée (hardcodée)
            FirewallConfig config = new FirewallConfig();
            System.out.println("✓ Configuration chargée (valeurs par défaut)");
            System.out.println("   - Seuil de blocage : " + config.getBlockThreshold());
            System.out.println("   - Seuil d'alerte   : " + config.getAlertThreshold());
            
            // Initialiser le pare-feu
            FirewallEngine firewall = new FirewallEngine(config);
            BlockChain blockchain = new BlockChain();
            
            // Charger les fichiers de paquets
            PacketSelector selector = new PacketSelector();
            selector.loadPacketFiles();
            selector.printStatistics();
            
            // Démarrer le pare-feu
            firewall.start();
            
            // ========== ÉTAPE 3 : MENU PRINCIPAL ==========
            boolean continuer = true;
            
            while (continuer) {
                System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
                System.out.println("║                    MENU PRINCIPAL                            ║");
                System.out.println("╚══════════════════════════════════════════════════════════════╝");
                System.out.println("\n1. Tester UN SEUL paquet (choix interactif)");
                System.out.println("2. Tester PLUSIEURS paquets (génération automatique)");
                System.out.println("3. Afficher les statistiques");
                System.out.println("4. Afficher la blockchain");
                System.out.println("5. Sauvegarder les résultats");
                System.out.println("6. Quitter");
                System.out.print("\nVotre choix : ");
                
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consommer la ligne
                
                switch (choice) {
                    case 1 -> testerUnSeulPaquet(selector, firewall, blockchain, scanner);
                    case 2 -> testerPlusieursPaquets(selector, firewall, blockchain, scanner);
                    case 3 -> firewall.printStatistics();
                    case 4 -> blockchain.printChain();
                    case 5 -> sauvegarderResultats(firewall, config);
                    case 6 -> {
                        continuer = false;
                        System.out.println("\n👋 Arrêt du pare-feu...");
                        firewall.stop();
                    }
                    default -> System.out.println("❌ Choix invalide");
                }
            }
            
            System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║              PROGRAMME TERMINÉ - MERCI !                     ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝\n");
            
        } catch (Exception e) {
            System.err.println("\n❌ ERREUR FATALE : " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
    
    // ========== MÉTHODES AUXILIAIRES ==========
    
    /**
     * Teste un seul paquet avec choix interactif.
     */
    private static void testerUnSeulPaquet(PacketSelector selector, 
                                           FirewallEngine firewall, 
                                           BlockChain blockchain,
                                           Scanner scanner) {
        try {
            // L'utilisateur choisit le type
            Packet packet = selector.userSelectPacket(scanner);
            
            System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("⚙️  TRAITEMENT DU PAQUET");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            
            // Analyser le paquet
            DecisionResult result = firewall.processPacket(packet);
            
            // Ajouter à la blockchain
            blockchain.addDecision(result);
            
            // Afficher le résultat détaillé
            System.out.println(result.getDetailedSummary());
            
            // Attendre que l'utilisateur appuie sur Entrée
            System.out.print("\nAppuyez sur Entrée pour continuer...");
            scanner.nextLine();
            
        } catch (Exception e) {
            System.err.println("❌ Erreur : " + e.getMessage());
        }
    }
    
    /**
     * Teste plusieurs paquets avec génération automatique.
     */
    private static void testerPlusieursPaquets(PacketSelector selector,
                                               FirewallEngine firewall,
                                               BlockChain blockchain,
                                               Scanner scanner) {
        try {
            // L'utilisateur choisit les paramètres
            List<Packet> packets = selector.userSelectMultiplePackets(scanner);
            
            System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("⚙️  TRAITEMENT DE " + packets.size() + " PAQUETS");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            
            // Traiter tous les paquets
            List<DecisionResult> results = firewall.processPackets(packets);
            
            // Ajouter à la blockchain
            blockchain.addBlock(results);
            
            // Résumé
            long accepted = results.stream()
                .filter(DecisionResult::isAccepted)
                .count();
            long blocked = results.stream()
                .filter(DecisionResult::isBlocked)
                .count();
            long alerted = results.stream()
                .filter(DecisionResult::needsAlert)
                .count();
            
            System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("📊 RÉSUMÉ DU TRAITEMENT");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("Total traités : " + results.size());
            System.out.println("  ✓ Acceptés  : " + accepted);
            System.out.println("  ✗ Bloqués   : " + blocked);
            System.out.println("  ⚠ Alertes   : " + alerted);
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            
            // Attendre
            System.out.print("Appuyez sur Entrée pour continuer...");
            scanner.nextLine();
            
        } catch (Exception e) {
            System.err.println("❌ Erreur : " + e.getMessage());
        }
    }
    
    /**
     * Sauvegarde tous les résultats.
     */
    private static void sauvegarderResultats(FirewallEngine firewall, FirewallConfig config) {
        try {
            System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("💾 SAUVEGARDE DES RÉSULTATS");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            
            DataPersistence persistence = new DataPersistence();
            
            // Sauvegarder les décisions
            List<DecisionResult> decisions = firewall.getDecisionHistory();
            if (!decisions.isEmpty()) {
                persistence.saveDecisions(decisions);
            } else {
                System.out.println("⚠ Aucune décision à sauvegarder");
            }
            
            // Sauvegarder la configuration
            persistence.saveConfig(config);
            
            // Exporter les statistiques
            String stats = buildStatisticsReport(firewall);
            persistence.exportStatistics(stats);
            
            System.out.println("\n✓ Toutes les données ont été sauvegardées avec succès !");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }
    
    /**
     * Construit un rapport de statistiques.
     */
    private static String buildStatisticsReport(FirewallEngine firewall) {
        StringBuilder report = new StringBuilder();
        
        report.append("STATISTIQUES DU PARE-FEU\n");
        report.append("========================\n\n");
        report.append("Total paquets traités : ").append(firewall.getTotalPackets()).append("\n");
        report.append("Acceptés              : ").append(firewall.getAcceptedPackets()).append("\n");
        report.append("Bloqués               : ").append(firewall.getDroppedPackets()).append("\n");
        report.append("Alertes               : ").append(firewall.getAlertedPackets()).append("\n");
        report.append("Journalisés           : ").append(firewall.getLoggedPackets()).append("\n");
        
        return report.toString();
    }
}
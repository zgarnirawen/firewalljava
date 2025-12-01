/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projetparfeu.controller;

import com.mycompany.projetparfeu.model.config.FirewallConfig;
import com.mycompany.projetparfeu.view.FirewallConfigViewApp;
import javafx.scene.control.Alert;

import javafx.scene.control.Alert;

public class FirewallConfigController {

    private FirewallConfig model;
    private FirewallConfigViewApp view;

    public FirewallConfigController(FirewallConfig model, FirewallConfigViewApp view) {
        this.model = model;
        this.view = view;
        initView();
        initController();
    }

    private void initView() {
        // Initialiser les champs avec les valeurs du modèle
        view.blockThresholdField.setText(String.valueOf(model.getBlockThreshold()));
        view.alertThresholdField.setText(String.valueOf(model.getAlertThreshold()));
        view.minPacketSizeField.setText(String.valueOf(model.getMinPacketSize()));
        view.maxPacketSizeField.setText(String.valueOf(model.getMaxPacketSize()));

        view.suspiciousWordsList.getItems().addAll(model.getSuspiciousWords());
        view.blacklistedIpsList.getItems().addAll(model.getBlacklistedIPs());
        view.monitoredPortsList.getItems().addAll(model.getMonitoredPorts());
    }

    private void initController() {
        // Mots suspects
        view.addSuspiciousBtn.setOnAction(e -> {
            String word = view.suspiciousWordField.getText();
            if (!word.isBlank()) {
                view.suspiciousWordsList.getItems().add(word);
                model.addSuspiciousWord(word);
                view.suspiciousWordField.clear();
            }
        });
        view.removeSuspiciousBtn.setOnAction(e -> {
            String selected = view.suspiciousWordsList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                view.suspiciousWordsList.getItems().remove(selected);
                model.removeSuspiciousWord(selected);
            }
        });

        // IPs
        view.addIpBtn.setOnAction(e -> {
            String ip = view.blacklistedIpField.getText();
            if (!ip.isBlank()) {
                view.blacklistedIpsList.getItems().add(ip);
                model.addBlacklistedIP(ip);
                view.blacklistedIpField.clear();
            }
        });
        view.removeIpBtn.setOnAction(e -> {
            String selected = view.blacklistedIpsList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                view.blacklistedIpsList.getItems().remove(selected);
                model.removeBlacklistedIP(selected);
            }
        });

        // Ports
        view.addPortBtn.setOnAction(e -> {
            try {
                int port = Integer.parseInt(view.monitoredPortField.getText());
                if (port >= 0 && port <= 65535) {
                    view.monitoredPortsList.getItems().add(port);
                    model.addMonitoredPort(port);
                    view.monitoredPortField.clear();
                }
            } catch (NumberFormatException ex) {
                showError("Port invalide");
            }
        });
        view.removePortBtn.setOnAction(e -> {
            Integer selected = view.monitoredPortsList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                view.monitoredPortsList.getItems().remove(selected);
                model.removeMonitoredPort(selected);
            }
        });

        // Sauvegarder
        view.saveBtn.setOnAction(e -> {
            try {
                model.setBlockThreshold(Integer.parseInt(view.blockThresholdField.getText()));
                model.setAlertThreshold(Integer.parseInt(view.alertThresholdField.getText()));
                model.setMinPacketSize(Integer.parseInt(view.minPacketSizeField.getText()));
                model.setMaxPacketSize(Integer.parseInt(view.maxPacketSizeField.getText()));
                showInfo("Configuration sauvegardée !");
            } catch (Exception ex) {
                showError("Erreur : " + ex.getMessage());
            }
        });
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).show();
    }

    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).show();
    }
}

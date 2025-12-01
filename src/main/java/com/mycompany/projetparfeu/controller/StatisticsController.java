/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ZGARNI
 */
package com.mycompany.projetparfeu.controller;

import com.mycompany.projetparfeu.model.statistics.StatisticsManager;
import com.mycompany.projetparfeu.view.StatisticsView;
import javafx.application.Platform;

public class StatisticsController {

    private final StatisticsManager model;
    private final StatisticsView view;

    public StatisticsController(StatisticsManager model, StatisticsView view) {
        this.model = model;
        this.view = view;

        // Start a thread to periodically update the view
        startUpdater();
    }

    private void startUpdater() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    String summary = String.format(
                        "Total: %d | Accepted: %d | Dropped: %d | Alerted: %d",
                        model.getTotalPackets(),
                        model.getAcceptedPackets(),
                        model.getDroppedPackets(),
                        model.getAlertedPackets()
                    );

                    view.updateStats(summary);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();
    }
}

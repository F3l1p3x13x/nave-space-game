package com.juegofeli.game;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SpaceShipGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Misión Estelar de Felipe");
            GamePanel gamePanel = new GamePanel();
            
            frame.add(gamePanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            
            // Agregar listener para limpiar recursos al cerrar
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    gamePanel.cleanup();
                    System.exit(0);
                }
            });
            
            frame.setVisible(true);
        });
    }
} 
package com.emotionalsurvey;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        getContentPane().setBackground(new Color(240, 240, 240));

        JLabel title = new JLabel("Emotional Games", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Color.BLACK);
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(3, 1, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 150, 40, 150));

        // Button 1 - Emotional Survey
        JButton surveyButton = new RoundedButton("Emotional Survey");
        surveyButton.addActionListener(e -> {
            dispose();  // Close main menu
            EmotionalSurveyApp.main(null);  // Launch Part 1
        });

        // Button 2 - Emotion Recognition
        JButton recognitionButton = new RoundedButton("Emotion Recognition");
        recognitionButton.addActionListener(e -> {
            dispose();  // Close main menu
            EmotionGameApp.main(null);  // Launch Part 2
        });

        // Button 3 - Exit
        JButton exitButton = new RoundedButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(surveyButton);
        buttonPanel.add(recognitionButton);
        buttonPanel.add(exitButton);

        add(title, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu().setVisible(true));
    }
}

package com.emotionalsurvey;

import javax.swing.*;
import java.awt.*;

public class EmotionalSurveyApp extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private SurveyPanel surveyPanel;
    private ResultsPanel resultsPanel;

    public EmotionalSurveyApp() {
        setTitle("Emotional Survey");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Initialize panels
        surveyPanel = new SurveyPanel();
        resultsPanel = new ResultsPanel();

        // Add cards
        cardPanel.add(surveyPanel, "Survey");
        cardPanel.add(resultsPanel, "Results");

        // Set survey completion callback
        surveyPanel.setCompletionCallback(results -> {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Would you like to play the Emotion Recognition game before viewing your results?",
                "Continue?",
                JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                dispose(); // Close this window
                EmotionGameApp.main(null); // Launch Game 2
            } else {
                resultsPanel.setResults(results);
                cardLayout.show(cardPanel, "Results");
            }
        });

        add(cardPanel);
        cardLayout.show(cardPanel, "Survey");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmotionalSurveyApp().setVisible(true));
    }
}

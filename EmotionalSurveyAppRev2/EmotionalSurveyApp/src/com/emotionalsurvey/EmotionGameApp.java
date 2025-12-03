package com.emotionalsurvey;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EmotionGameApp extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private EmotionIntroPanel introPanel;
    private EmotionGamePanel gamePanel;
    private EmotionGameResultsPanel resultsPanel;

    public EmotionGameApp() {

        setTitle("Emotional Recognition Game (Part 2)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Panels
        introPanel = new EmotionIntroPanel(this);
        gamePanel = new EmotionGamePanel(this);
        resultsPanel = new EmotionGameResultsPanel(this);

        mainPanel.add(introPanel, "INTRO");
        mainPanel.add(gamePanel, "GAME");
        mainPanel.add(resultsPanel, "RESULTS");

        add(mainPanel);
        cardLayout.show(mainPanel, "INTRO");
    }

    // Start the game from intro screen
    public void startGame() {
        gamePanel.reset();
        cardLayout.show(mainPanel, "GAME");
    }

    // Called when user finishes the questions
    public void showResults(List<EmotionQuestion> questions, String[] answers) {
        resultsPanel.displayResults(questions, answers);
        cardLayout.show(mainPanel, "RESULTS");
    }

    public void restartGame() {
        gamePanel.reset();
        cardLayout.show(mainPanel, "GAME");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EmotionGameApp app = new EmotionGameApp();
            app.setVisible(true);
        });
    }
}

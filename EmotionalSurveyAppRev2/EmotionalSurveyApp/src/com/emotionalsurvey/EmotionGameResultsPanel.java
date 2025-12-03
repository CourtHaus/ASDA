package com.emotionalsurvey;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class EmotionGameResultsPanel extends JPanel {

    private EmotionGameApp parentApp;
    private JLabel totalCorrectLabel;
    private JLabel percentageLabel;
    private JLabel levelLabel;
    private EmotionAccuracyChartPanel chartPanel;

    public EmotionGameResultsPanel(EmotionGameApp parentApp) {
        this.parentApp = parentApp;

        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(30, 30, 30, 30));
        setBackground(new Color(245, 245, 250));

        initializeComponents();
    }

    private void initializeComponents() {

        JLabel titleLabel = new JLabel("Emotion Game Results", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(60, 60, 60));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 250));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        // BorderLayout
        
        JPanel summaryPanel = new JPanel(new BorderLayout(20, 0));
        summaryPanel.setBackground(new Color(245, 245, 250));

        totalCorrectLabel = new JLabel("0 / 0");
        JPanel totalCorrectPanel = createMetricPanel("Total Correct", totalCorrectLabel);

        percentageLabel = new JLabel("0.0%");
        JPanel percentagePanel = createMetricPanel("Score Percentage", percentageLabel);

        levelLabel = new JLabel("N/A");

        // NON-TRUNCATION PANEL FOR EMOTIONAL RECOGNITION
        JPanel levelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        levelPanel.setBackground(Color.WHITE);
        levelPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel levelTitle = new JLabel("Emotional Recognition");
        levelTitle.setFont(new Font("Arial", Font.PLAIN, 14));
        levelTitle.setForeground(new Color(100, 100, 100));

        levelLabel.setFont(new Font("Arial", Font.BOLD, 24));
        levelLabel.setForeground(new Color(60, 60, 60));

        levelPanel.add(levelTitle);
        levelPanel.add(levelLabel);


        // BorderLayout distribution
  
        summaryPanel.add(totalCorrectPanel, BorderLayout.WEST);
        summaryPanel.add(percentagePanel, BorderLayout.CENTER);
        summaryPanel.add(levelPanel, BorderLayout.EAST);

        topPanel.add(summaryPanel, BorderLayout.CENTER);

        // Chart
        chartPanel = new EmotionAccuracyChartPanel();
        chartPanel.setPreferredSize(new Dimension(0, 350));

        // Button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 250));

        JButton restartButton = new JButton("Take Game Again");
        restartButton.setFont(new Font("Arial", Font.BOLD, 16));
        restartButton.addActionListener(e -> parentApp.restartGame());

        buttonPanel.add(restartButton);

        add(topPanel, BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createMetricPanel(String title, JLabel valueLabel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(100, 100, 100));

        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(new Color(60, 60, 60));

        JPanel valuePanelWrapper = new JPanel();
        valuePanelWrapper.setOpaque(false);
        valuePanelWrapper.setLayout(new BoxLayout(valuePanelWrapper, BoxLayout.X_AXIS));
        valuePanelWrapper.add(Box.createHorizontalGlue());
        valuePanelWrapper.add(valueLabel);
        valuePanelWrapper.add(Box.createHorizontalGlue());

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(valuePanelWrapper);

        return panel;
    }

    public void displayResults(List<EmotionQuestion> questions, String[] selectedAnswers) {
        int totalQuestions = questions.size();
        int correctCount = 0;
        boolean[] correctFlags = new boolean[totalQuestions];

        for (int i = 0; i < totalQuestions; i++) {
            EmotionQuestion q = questions.get(i);
            String selected = selectedAnswers[i];

            boolean correct = selected != null &&
                    selected.trim().equalsIgnoreCase(q.getCorrectEmotion().trim());

            correctFlags[i] = correct;
            if (correct) {
                correctCount++;
            }
        }

        double percentage = (totalQuestions == 0)
                ? 0.0
                : (correctCount * 100.0 / totalQuestions);

        String level;
        Color levelColor;

        if (percentage < 40.0) {
            level = "Low Emotional Recognition";
            levelColor = new Color(52, 152, 219);

        } else if (percentage < 75.0) {
            level = "Moderate Emotional Recognition";
            levelColor = new Color(241, 196, 15);

        } else {
            level = "High Emotional Recognition";
            levelColor = new Color(231, 76, 60);
        }

        totalCorrectLabel.setText(correctCount + " / " + totalQuestions);
        percentageLabel.setText(String.format("%.1f%%", percentage));

        levelLabel.setText(level);
        levelLabel.setForeground(levelColor);

        chartPanel.setData(correctFlags);

        revalidate();
        repaint();
    }
}

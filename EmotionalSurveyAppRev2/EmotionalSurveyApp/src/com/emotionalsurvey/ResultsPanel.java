package com.emotionalsurvey;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ResultsPanel extends JPanel {
    private JLabel totalScoreLabel;
    private JLabel averageScoreLabel;
    private JLabel categoryLabel;
    private BarChartPanel chartPanel;

    public ResultsPanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(30, 30, 30, 30));
        setBackground(new Color(245, 245, 250));

        initializeComponents();
    }

    private void initializeComponents() {
        JLabel titleLabel = new JLabel("Survey Results", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(60, 60, 60));

        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new GridLayout(1, 3, 20, 0));
        summaryPanel.setBackground(new Color(245, 245, 250));

        totalScoreLabel = new JLabel("0 / 100");
        JPanel totalScorePanel = createMetricPanel("Total Score", totalScoreLabel);

        averageScoreLabel = new JLabel("0.0 / 5.0");
        JPanel averageScorePanel = createMetricPanel("Average Response", averageScoreLabel);

        categoryLabel = new JLabel("N/A");
        JPanel categoryPanel = createMetricPanel("Emotional Reactivity", categoryLabel);

        summaryPanel.add(totalScorePanel);
        summaryPanel.add(averageScorePanel);
        summaryPanel.add(categoryPanel);

        chartPanel = new BarChartPanel();

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(245, 245, 250));


        JPanel topPanel = new JPanel(new BorderLayout(0, 20));
        topPanel.setBackground(new Color(245, 245, 250));
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(summaryPanel, BorderLayout.CENTER);

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
                new EmptyBorder(15, 15, 15, 15)));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(100, 100, 100));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel valuePanelWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        valuePanelWrapper.setBackground(Color.WHITE);

        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(new Color(60, 60, 60));

        valuePanelWrapper.add(valueLabel);

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(valuePanelWrapper);

        return panel;
    }

    public void setResults(int[] responses) {
        int totalScore = 0;
        for (int response : responses) {
            totalScore += response;
        }

        double averageScore = (double) totalScore / responses.length;

        String category;
        Color categoryColor;
        if (averageScore < 2.5) {
            category = "Low Emotional Reactivity";
            categoryColor = new Color(52, 152, 219);
        } else if (averageScore < 3.5) {
            category = "Medium Emotional Reactivity";
            categoryColor = new Color(241, 196, 15);
        } else {
            category = "High Emotional Reactivity";
            categoryColor = new Color(231, 76, 60);
        }

        totalScoreLabel.setText(totalScore + " / 100");
        averageScoreLabel.setText(String.format("%.2f / 5.0", averageScore));
        categoryLabel.setText(category);
        categoryLabel.setForeground(categoryColor);

        chartPanel.setData(responses);
    }
}

package com.emotionalsurvey;

import javax.swing.*;
import java.awt.*;

public class EmotionAccuracyChartPanel extends JPanel {

    private boolean[] correctFlags;

    public void setData(boolean[] correctFlags) {
        this.correctFlags = correctFlags;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (correctFlags == null || correctFlags.length == 0) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        int padding = 60;
        int chartHeight = height - 2 * padding;
        int chartWidth = width - 2 * padding;

        g2d.setColor(new Color(220, 220, 220));
        g2d.drawLine(padding, height - padding, width - padding, height - padding);
        g2d.drawLine(padding, padding, padding, height - padding);

        g2d.setColor(new Color(150, 150, 150));
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        g2d.drawString("Incorrect (0)", padding - 55, height - padding + 5);
        g2d.drawString("Correct (1)", padding - 45, padding + 5);

        int n = correctFlags.length;
        int barWidth = Math.max(5, chartWidth / n - 4);

        for (int i = 0; i < n; i++) {
            int value = correctFlags[i] ? 1 : 0;
            int barHeight = (int) ((value / 1.0) * (chartHeight - 20));

            int x = padding + i * (chartWidth / n) + 2;
            int y = height - padding - barHeight;

            g2d.setColor(new Color(241, 196, 15));
            g2d.fillRect(x, y, barWidth, barHeight);

            g2d.setColor(new Color(120, 120, 120));
            String label = String.valueOf(i + 1);
            int lw = g2d.getFontMetrics().stringWidth(label);

            g2d.drawString(label, x + (barWidth - lw) / 2, height - padding + 15);
        }

        g2d.dispose();
    }
}

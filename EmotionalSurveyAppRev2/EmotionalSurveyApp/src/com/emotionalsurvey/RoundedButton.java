package com.emotionalsurvey;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RoundedButton extends JButton {

    private static final int ARC_WIDTH = 20;
    private static final int ARC_HEIGHT = 20;

    public RoundedButton(String text) {
        super(text);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setForeground(Color.BLACK);
        setFont(new Font("SansSerif", Font.PLAIN, 18));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                setForeground(Color.BLUE);
            }

            public void mouseExited(MouseEvent e) {
                setForeground(Color.BLACK);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(220, 220, 220));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), ARC_WIDTH, ARC_HEIGHT);

        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 45);
    }
}

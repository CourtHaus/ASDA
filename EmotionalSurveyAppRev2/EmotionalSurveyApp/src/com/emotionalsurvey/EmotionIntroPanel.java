package com.emotionalsurvey;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class EmotionIntroPanel extends JPanel {

    public EmotionIntroPanel(EmotionGameApp parentApp) {

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 250));

        // TITLE
        JLabel titleLabel = new JLabel("Emotional Recognition Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setBorder(new EmptyBorder(40, 0, 20, 0));

        add(titleLabel, BorderLayout.NORTH);

        // CENTER BOX WRAPPER 
        JPanel centerWrapper = new JPanel();
        centerWrapper.setOpaque(false);
        centerWrapper.setLayout(new GridBagLayout());   

        // WHITE BOX 
        JPanel whiteBox = new JPanel();
        whiteBox.setBackground(Color.WHITE);
        whiteBox.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
                new EmptyBorder(40, 60, 40, 60)
            )
        );
        whiteBox.setLayout(new BoxLayout(whiteBox, BoxLayout.Y_AXIS));

        // TEXT
        JLabel introText = new JLabel(
            "<html><div style='text-align:center; font-size:18px;'>"
            + "In this game, you will see a series of facial expressions.<br><br>"
            + "Your task is to choose the emotion that best matches each face<br>"
            + "from the four given options.<br><br>"
            + "Try to select the emotion that feels most accurate.<br>"
            + "There are no right or wrong answers."
            + "</div></html>"
        );
        introText.setAlignmentX(Component.CENTER_ALIGNMENT);

        whiteBox.add(introText);

        centerWrapper.add(whiteBox);

        add(centerWrapper, BorderLayout.CENTER);

        // START BUTTON
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(40, 0, 60, 0));

        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setPreferredSize(new Dimension(160, 40));

        startButton.addActionListener(e -> parentApp.startGame());

        bottomPanel.add(startButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }
}

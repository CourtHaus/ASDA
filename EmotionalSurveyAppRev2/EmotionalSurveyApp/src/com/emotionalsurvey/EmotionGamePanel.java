package com.emotionalsurvey;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

public class EmotionGamePanel extends JPanel {

    private static final String IMAGE_FOLDER = "resources_game_pt2";

    private EmotionGameApp parentApp;
    private List<EmotionQuestion> questions;
    private int currentQuestionIndex = 0;
    private String[] selectedAnswers;

    private JLabel questionNumberLabel;
    private JProgressBar progressBar;
    private JLabel imageLabel;
    private JRadioButton[] optionButtons;
    private ButtonGroup optionsGroup;
    private JButton previousButton;
    private JButton nextButton;

    public EmotionGamePanel(EmotionGameApp parentApp) {
        this.parentApp = parentApp;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 250));

        this.questions = loadQuestions();
        this.selectedAnswers = new String[questions.size()];

        initializeComponents();
        updateDisplay();
    }

    private List<EmotionQuestion> loadQuestions() {
        List<EmotionQuestion> questionList = new ArrayList<>();

        File dir = new File(IMAGE_FOLDER);
        File[] imageFiles = dir.listFiles((d, name) ->
                name.toLowerCase().endsWith(".jpg") ||
                        name.toLowerCase().endsWith(".jpeg") ||
                        name.toLowerCase().endsWith(".png")
        );

        if (imageFiles == null || imageFiles.length == 0) return questionList;

        Arrays.sort(imageFiles, Comparator.comparing(File::getName));

        List<String> allEmotions = new ArrayList<>();
        for (File file : imageFiles) {
            String base = file.getName();
            int dot = base.lastIndexOf('.');
            if (dot > 0) allEmotions.add(formatEmotion(base.substring(0, dot)));
        }

        Random r = new Random();

        for (int i = 0; i < imageFiles.length; i++) {
            File file = imageFiles[i];
            String base = file.getName();
            int dot = base.lastIndexOf('.');

            if (dot <= 0) continue;

            String raw = base.substring(0, dot);
            String correctEmotion = formatEmotion(raw);

            List<String> options = new ArrayList<>();
            options.add(correctEmotion);

            List<String> distractors = new ArrayList<>(allEmotions);
            distractors.remove(correctEmotion);

            Collections.shuffle(distractors, r);
            for (int j = 0; j < 3 && j < distractors.size(); j++) {
                options.add(distractors.get(j));
            }

            Collections.shuffle(options, r);

            questionList.add(new EmotionQuestion(
                    i + 1,
                    file.getAbsolutePath(),
                    correctEmotion,
                    options
            ));
        }

        return questionList;
    }

    private String formatEmotion(String raw) {
        String[] parts = raw.toLowerCase().split("[_\\s]+");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            if (!parts[i].isEmpty()) {
                sb.append(Character.toUpperCase(parts[i].charAt(0)))
                        .append(parts[i].substring(1));
                if (i < parts.length - 1) sb.append(" ");
            }
        }
        return sb.toString();
    }

    private void initializeComponents() {

        // HEADER
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 245, 250));

        JLabel titleLabel = new JLabel("Emotional Recognition Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        questionNumberLabel = new JLabel("", SwingConstants.CENTER);
        questionNumberLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        progressBar = new JProgressBar(0, Math.max(1, questions.size()));
        progressBar.setValue(1);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(0, 25));

        JPanel headerText = new JPanel(new GridLayout(2, 1));
        headerText.setBackground(new Color(245, 245, 250));
        headerText.add(titleLabel);
        headerText.add(questionNumberLabel);

        headerPanel.add(headerText, BorderLayout.CENTER);
        headerPanel.add(progressBar, BorderLayout.SOUTH);

        // IMAGE PANEL
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 40, 0, 40),
                BorderFactory.createLineBorder(new Color(200, 200, 200), 2)
        ));
        imagePanel.setBackground(Color.WHITE);

        imageLabel = new JLabel("", SwingConstants.CENTER);
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        // OPTIONS SECTION
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBackground(new Color(245, 245, 250));
        optionsPanel.setBorder(new EmptyBorder(25, 40, 10, 40));

        JLabel instructionLabel = new JLabel("Which emotion fits this face?", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        optionsPanel.add(instructionLabel);
        optionsPanel.add(Box.createVerticalStrut(20));

       
        JPanel centeredWrapper = new JPanel(new GridBagLayout());
        centeredWrapper.setOpaque(false);

        JPanel grid = new JPanel(new GridLayout(2, 2, 60, 20));
        grid.setOpaque(false);

        optionButtons = new JRadioButton[4];
        optionsGroup = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            JRadioButton rb = new JRadioButton("Option");
            rb.setFont(new Font("Arial", Font.PLAIN, 15));
            rb.setOpaque(false);
            optionButtons[i] = rb;

            optionsGroup.add(rb);
            grid.add(rb);
        }

        centeredWrapper.add(grid);
        optionsPanel.add(centeredWrapper);

        // NAVIGATION
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 12));
        navPanel.setBackground(new Color(245, 245, 250));

        previousButton = new JButton("← Previous");
        previousButton.addActionListener(e -> {
            saveSelection();
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--;
                updateDisplay();
            }
        });

        nextButton = new JButton("Next →");
        nextButton.addActionListener(e -> {
            saveSelection();
            if (currentQuestionIndex < questions.size() - 1) {
                currentQuestionIndex++;
                updateDisplay();
            } else {
                parentApp.showResults(questions, selectedAnswers);
            }
        });

        navPanel.add(previousButton);
        navPanel.add(nextButton);

        // FINAL ASSEMBLY
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(new Color(245, 245, 250));
        bottom.add(optionsPanel, BorderLayout.NORTH);
        bottom.add(navPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
        add(imagePanel, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    private void updateDisplay() {

        EmotionQuestion q = questions.get(currentQuestionIndex);

        questionNumberLabel.setText("Question " + (currentQuestionIndex + 1) +
                " of " + questions.size());
        progressBar.setValue(currentQuestionIndex + 1);

        // Proper image scaling keeps the proportions
        ImageIcon ic = new ImageIcon(q.getImagePath());
        Image img = ic.getImage();

        int maxWidth = 800;
        int maxHeight = 320;

        double scale = Math.min((double) maxWidth / img.getWidth(null),
                (double) maxHeight / img.getHeight(null));

        Image scaled = img.getScaledInstance(
                (int) (img.getWidth(null) * scale),
                (int) (img.getHeight(null) * scale),
                Image.SCALE_SMOOTH
        );

        imageLabel.setIcon(new ImageIcon(scaled));

        List<String> opts = q.getOptions();
        optionsGroup.clearSelection();

        for (int i = 0; i < 4; i++) {
            if (i < opts.size()) {
                optionButtons[i].setText(opts.get(i));
                optionButtons[i].setVisible(true);
            } else {
                optionButtons[i].setVisible(false);
            }
        }

        if (selectedAnswers[currentQuestionIndex] != null) {
            for (JRadioButton rb : optionButtons) {
                if (rb.getText().equals(selectedAnswers[currentQuestionIndex])) {
                    rb.setSelected(true);
                    break;
                }
            }
        }

        previousButton.setEnabled(currentQuestionIndex > 0);

        if (currentQuestionIndex == questions.size() - 1) {
            nextButton.setText("Finish");
        } else {
            nextButton.setText("Next →");
        }
    }

    private void saveSelection() {
        for (JRadioButton rb : optionButtons) {
            if (rb.isVisible() && rb.isSelected()) {
                selectedAnswers[currentQuestionIndex] = rb.getText();
                return;
            }
        }
    }

    public void reset() {
        currentQuestionIndex = 0;
        Arrays.fill(selectedAnswers, null);
        updateDisplay();
    }
}

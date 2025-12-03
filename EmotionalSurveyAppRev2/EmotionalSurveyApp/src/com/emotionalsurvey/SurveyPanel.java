package com.emotionalsurvey;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.function.Consumer;

public class SurveyPanel extends JPanel {
	private List<Question> questions;
	private int currentQuestionIndex = 0;
	private int[] responses;

	private JLabel questionNumberLabel;
	private JLabel imageLabel;
	private JSlider responseSlider;
	private JButton previousButton;
	private JButton nextButton;
	private JProgressBar progressBar;

	private Consumer<int[]> completionCallback;

	public SurveyPanel() {
		this.questions = loadQuestions();
		this.responses = new int[questions.size()];

		for (int i = 0; i < responses.length; i++) {
			responses[i] = 3;
		}

		setLayout(new BorderLayout(10, 10));
		setBorder(new EmptyBorder(20, 20, 20, 20));
		setBackground(new Color(245, 245, 250));

		initializeComponents();
		updateDisplay();
	}

	private List<Question> loadQuestions() {
		List<Question> questionList = new ArrayList<>();
		File imageDir = new File("resources/images");
		File[] imageFiles = imageDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));

		if (imageFiles != null && imageFiles.length > 0) {
			for (int i = 0; i < Math.min(imageFiles.length, 20); i++) {
				questionList.add(new Question(i + 1, imageFiles[i].getAbsolutePath()));
			}
		}

		return questionList;
	}

	private void initializeComponents() {
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setBackground(new Color(245, 245, 250));

		JLabel titleLabel = new JLabel("Emotional Response Survey", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setForeground(new Color(60, 60, 60));

		questionNumberLabel = new JLabel("Question 1 of 20", SwingConstants.CENTER);
		questionNumberLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		questionNumberLabel.setForeground(new Color(100, 100, 100));

		progressBar = new JProgressBar(0, questions.size());
		progressBar.setValue(1);
		progressBar.setStringPainted(true);
		progressBar.setPreferredSize(new Dimension(0, 25));

		headerPanel.add(titleLabel, BorderLayout.NORTH);
		headerPanel.add(questionNumberLabel, BorderLayout.CENTER);
		headerPanel.add(progressBar, BorderLayout.SOUTH);

		JPanel imagePanel = new JPanel(new BorderLayout());
		imagePanel.setBackground(Color.WHITE);
		imagePanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));

		imageLabel = new JLabel();
		imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		imageLabel.setVerticalAlignment(SwingConstants.CENTER);
		imagePanel.add(imageLabel, BorderLayout.CENTER);

		JPanel sliderPanel = new JPanel();
		sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));
		sliderPanel.setBackground(new Color(245, 245, 250));
		sliderPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

		JLabel instructionLabel = new JLabel("Rate your emotional response to this image:", SwingConstants.CENTER);
		instructionLabel.setFont(new Font("Arial", Font.BOLD, 16));
		instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		instructionLabel.setForeground(new Color(60, 60, 60));

		responseSlider = new JSlider(1, 5, 3);
		responseSlider.setMajorTickSpacing(1);
		responseSlider.setPaintTicks(true);
		responseSlider.setPaintLabels(true);
		responseSlider.setSnapToTicks(true);
		responseSlider.setBackground(new Color(245, 245, 250));

		Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
		labelTable.put(1, new JLabel("1"));
		labelTable.put(2, new JLabel("2"));
		labelTable.put(3, new JLabel("3"));
		labelTable.put(4, new JLabel("4"));
		labelTable.put(5, new JLabel("5"));
		responseSlider.setLabelTable(labelTable);

		JPanel scaleLabelsPanel = new JPanel(new BorderLayout());
		scaleLabelsPanel.setBackground(new Color(245, 245, 250));
		JLabel leftLabel = new JLabel("Strongly Disagree");
		leftLabel.setFont(new Font("Arial", Font.ITALIC, 12));
		JLabel rightLabel = new JLabel("Strongly Agree");
		rightLabel.setFont(new Font("Arial", Font.ITALIC, 12));
		scaleLabelsPanel.add(leftLabel, BorderLayout.WEST);
		scaleLabelsPanel.add(rightLabel, BorderLayout.EAST);

		sliderPanel.add(Box.createVerticalStrut(10));
		sliderPanel.add(instructionLabel);
		sliderPanel.add(Box.createVerticalStrut(20));
		sliderPanel.add(responseSlider);
		sliderPanel.add(Box.createVerticalStrut(10));
		sliderPanel.add(scaleLabelsPanel);

		JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		navigationPanel.setBackground(new Color(245, 245, 250));

		previousButton = new JButton("← Previous");
		previousButton.setFont(new Font("Arial", Font.PLAIN, 14));
		previousButton.setPreferredSize(new Dimension(120, 40));
		previousButton.addActionListener(e -> previousQuestion());

		nextButton = new JButton("Next →");
		nextButton.setFont(new Font("Arial", Font.BOLD, 14));
		nextButton.setPreferredSize(new Dimension(120, 40));
		nextButton.addActionListener(e -> nextQuestion());

		navigationPanel.add(previousButton);
		navigationPanel.add(nextButton);

		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBackground(new Color(245, 245, 250));
		bottomPanel.add(sliderPanel, BorderLayout.NORTH);
		bottomPanel.add(navigationPanel, BorderLayout.SOUTH);

		add(headerPanel, BorderLayout.NORTH);
		add(imagePanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
	}

	private void updateDisplay() {
		Question currentQuestion = questions.get(currentQuestionIndex);

		questionNumberLabel.setText("Question " + (currentQuestionIndex + 1) + " of " + questions.size());
		progressBar.setValue(currentQuestionIndex + 1);

		ImageIcon imageIcon = new ImageIcon(currentQuestion.getImagePath());
		Image scaledImage = imageIcon.getImage().getScaledInstance(600, 400, Image.SCALE_SMOOTH);
		imageLabel.setIcon(new ImageIcon(scaledImage));

		responseSlider.setValue(responses[currentQuestionIndex]);

		previousButton.setEnabled(currentQuestionIndex > 0);

		if (currentQuestionIndex == questions.size() - 1) {
			nextButton.setText("Finish");
		} else {
			nextButton.setText("Next →");
		}
	}

	private void saveCurrentResponse() {
		responses[currentQuestionIndex] = responseSlider.getValue();
	}

	private void previousQuestion() {
		if (currentQuestionIndex > 0) {
			saveCurrentResponse();
			currentQuestionIndex--;
			updateDisplay();
		}
	}

	private void nextQuestion() {
		saveCurrentResponse();

		if (currentQuestionIndex < questions.size() - 1) {
			currentQuestionIndex++;
			updateDisplay();
		} else {
			if (completionCallback != null) {
				completionCallback.accept(responses);
			}
		}
	}

	public void reset() {
		currentQuestionIndex = 0;
		for (int i = 0; i < responses.length; i++) {
			responses[i] = 3;
		}
		updateDisplay();
	}

	public void setCompletionCallback(Consumer<int[]> callback) {
		this.completionCallback = callback;
	}
}

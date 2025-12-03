package org.group1.asda.ui.emotionrecognition;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.group1.asda.domain.emotional.FacialEmotionGameState;
import org.group1.asda.navigation.Router;

public class EmotionRecognitionResultsController {
    @FXML private Label correctLabel;
    @FXML private Label totalLabel;
    @FXML private Label accuracyLabel;
    @FXML private Label recognitionLevelLabel;
    @FXML private Label feedbackText;

    private FacialEmotionGameState gameState;

    @FXML
    public void initialize() {
        // Results will be set via setGameState method
    }

    public void setGameState(FacialEmotionGameState state) {
        this.gameState = state;
        displayResults();
    }

    private void displayResults() {
        if (gameState == null) return;

        int correct = gameState.getRecognitionCorrectCount();
        int total = gameState.getTotalQuestions();
        double accuracy = gameState.getRecognitionAccuracy();

        correctLabel.setText(String.valueOf(correct));
        totalLabel.setText(String.valueOf(total));
        accuracyLabel.setText(String.format("%.1f%%", accuracy));

        // Set recognition level based on accuracy
        String level;
        String levelStyle;
        if (accuracy >= 75.0) {
            level = "High Emotional Recognition";
            levelStyle = "-fx-text-fill: #27ae60;"; // Green
        } else if (accuracy >= 40.0) {
            level = "Moderate Emotional Recognition";
            levelStyle = "-fx-text-fill: #f39c12;"; // Orange
        } else {
            level = "Low Emotional Recognition";
            levelStyle = "-fx-text-fill: #3498db;"; // Blue
        }

        recognitionLevelLabel.setText(level);
        recognitionLevelLabel.setStyle(levelStyle);

        // Set feedback based on accuracy
        setFeedback(accuracy);
    }

    private void setFeedback(double accuracy) {
        if (accuracy >= 80) {
            feedbackText.setText("Excellent emotional recognition ability! You demonstrate strong understanding of facial emotions.");
        } else if (accuracy >= 60) {
            feedbackText.setText("Good emotional recognition. You show solid ability to identify emotions with room for growth.");
        } else if (accuracy >= 40) {
            feedbackText.setText("Moderate emotional recognition. Consider practicing emotion identification to improve.");
        } else {
            feedbackText.setText("Emotional recognition needs development. Practice observing facial expressions to improve.");
        }
    }

    @FXML
    private void onPlayAgain() {
        Router.getInstance().goTo("emotional-survey");
    }

    @FXML
    private void onHome() {
        Router.getInstance().goTo("home");
    }
}

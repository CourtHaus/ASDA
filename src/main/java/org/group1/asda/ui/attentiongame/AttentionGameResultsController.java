package org.group1.asda.ui.attentiongame;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.group1.asda.domain.AttentionGameState;
import org.group1.asda.navigation.Router;

public class AttentionGameResultsController {
    @FXML private Label attemptsLabel;
    @FXML private Label correctLabel;
    @FXML private Label incorrectLabel;
    @FXML private Label accuracyLabel;
    @FXML private Label timeLabel;
    @FXML private Label feedbackTitle;
    @FXML private Label feedbackText;

    private AttentionGameState gameState;

    @FXML
    public void initialize() {
        // Results will be set via setGameState method
    }

    public void setGameState(AttentionGameState state) {
        this.gameState = state;
        displayResults();
    }

    private void displayResults() {
        if (gameState == null) return;

        int attempts = gameState.getTotalCorrect() + gameState.getTotalIncorrect();
        int correct = gameState.getTotalCorrect();
        int incorrect = gameState.getTotalIncorrect();
        double accuracy = gameState.getAccuracy();
        double timeSeconds = gameState.getElapsedTimeSeconds();

        attemptsLabel.setText(String.valueOf(attempts));
        correctLabel.setText(String.valueOf(correct));
        incorrectLabel.setText(String.valueOf(incorrect));
        accuracyLabel.setText(String.format("%.0f%%", accuracy));
        timeLabel.setText(String.format("%.2fs", timeSeconds));

        // Get performance feedback
        String perfIndex = gameState.getAttentionPerformanceIndex();
        setFeedback(accuracy, perfIndex);
    }

    private void setFeedback(double accuracy, String perfIndex) {
        if (accuracy >= 90) {
            feedbackTitle.setText("Excellent attention.");
            feedbackText.setText("Recommendation: Performance suggests strong sustained attention.");
        } else if (accuracy >= 75) {
            feedbackTitle.setText("Good attention.");
            feedbackText.setText("Recommendation: Performance shows adequate sustained attention with room for improvement.");
        } else if (accuracy >= 60) {
            feedbackTitle.setText("Moderate attention.");
            feedbackText.setText("Recommendation: Performance indicates some difficulty with sustained attention. Consider strategies to improve focus.");
        } else {
            feedbackTitle.setText("Attention needs improvement.");
            feedbackText.setText("Recommendation: Performance suggests challenges with sustained attention. Consider consulting a professional if concerns persist.");
        }
    }

    @FXML
    private void onPlayAgain() {
        // Navigate back to tutorial or directly to game
        Router.getInstance().goTo("attention-game-tutorial");
    }

    @FXML
    private void onHome() {
        Router.getInstance().goTo("home");
    }
}

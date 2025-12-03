package org.group1.asda.ui.matchinggame;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.group1.asda.domain.GameState;
import org.group1.asda.navigation.Router;

public class MatchingGameResultsController {
    @FXML private Label timeLabel;
    @FXML private Label attemptsLabel;
    @FXML private Label correctLabel;
    @FXML private Label incorrectLabel;
    @FXML private Label accuracyLabel;
    @FXML private Label memoryScoreLabel;
    @FXML private Label apiLabel;
    @FXML private Label feedbackText;

    private GameState gameState;

    @FXML
    public void initialize() {
        // Results will be set via setGameState method
    }

    public void setGameState(GameState state) {
        this.gameState = state;
        displayResults();
    }

    private void displayResults() {
        if (gameState == null) return;

        int attempts = gameState.getTotalAttempts();
        int correct = gameState.getTotalCorrect();
        int incorrect = gameState.getTotalIncorrect();
        double accuracy = gameState.getAccuracy();
        double timeSeconds = gameState.getElapsedTimeSeconds();
        int memoryScore = gameState.getMemoryScore();

        timeLabel.setText(String.format("%.0fs", timeSeconds));
        attemptsLabel.setText(String.valueOf(attempts));
        correctLabel.setText(String.valueOf(correct));
        incorrectLabel.setText(String.valueOf(incorrect));
        accuracyLabel.setText(String.format("%.0f%%", accuracy));
        memoryScoreLabel.setText(String.valueOf(memoryScore));

        // Calculate API (Attention Performance Index)
        double api = calculateAPI(accuracy, memoryScore);
        apiLabel.setText(String.format("%.1f", api));

        // Set feedback based on API
        setFeedback(api);
    }

    private double calculateAPI(double accuracy, int memoryScore) {
        // Simple API calculation - can be refined
        // Scale memory score (0-1000) to contribute to API
        double memoryComponent = (memoryScore / 1000.0) * 50;
        double accuracyComponent = (accuracy / 100.0) * 50;
        return memoryComponent + accuracyComponent;
    }

    private void setFeedback(double api) {
        if (api >= 80) {
            feedbackText.setText("Excellent Focus Consistency - Strong sustained attention demonstrated.");
        } else if (api >= 65) {
            feedbackText.setText("Good Focus Consistency - Adequate sustained attention with room for improvement.");
        } else if (api >= 50) {
            feedbackText.setText("Moderate Focus Consistency - Consider strategies to improve attention.");
        } else {
            feedbackText.setText("Below Average Focus Consistency - Consider behavioral self-assessment.");
        }
    }

    @FXML
    private void onPlayAgain() {
        Router.getInstance().goTo("matching-game-tutorial");
    }

    @FXML
    private void onHome() {
        Router.getInstance().goTo("home");
    }
}

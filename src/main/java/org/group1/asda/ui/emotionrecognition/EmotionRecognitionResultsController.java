package org.group1.asda.ui.emotionrecognition;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.beans.binding.Bindings;
import org.group1.asda.domain.emotional.FacialEmotionGameState;
import org.group1.asda.navigation.Router;

public class EmotionRecognitionResultsController {
    @FXML private Label correctLabel;
    @FXML private Label totalLabel;
    @FXML private Label accuracyLabel;
    @FXML private Label recognitionLevelLabel;
    @FXML private Label feedbackText;
    @FXML private Label correctPercentLabel;
    @FXML private Label incorrectPercentLabel;
    @FXML private Region accuracyFill;
    @FXML private Region correctFill;
    @FXML private Region incorrectFill;
    @FXML private StackPane accuracyTrack;
    @FXML private StackPane correctTrack;
    @FXML private StackPane incorrectTrack;

    private double accuracyPct = 0;
    private double correctPct = 0;
    private double incorrectPct = 0;
    private boolean barsBound = false;

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
        double accuracy = total == 0 ? 0 : gameState.getRecognitionAccuracy();

        correctLabel.setText(String.valueOf(correct));
        totalLabel.setText("of " + total + " patterns");
        accuracyLabel.setText(String.format("%.1f%%", accuracy));

        correctPct = total == 0 ? 0 : (correct / (double) total);
        incorrectPct = total == 0 ? 0 : ((total - correct) / (double) total);
        accuracyPct = accuracy / 100.0;

        correctPercentLabel.setText(String.format("%.0f%%", correctPct * 100));
        incorrectPercentLabel.setText(String.format("%.0f%%", incorrectPct * 100));

        bindBarsOnce();
        updateBindings(); // force initial sizing in case widths already known

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

    private void bindBarsOnce() {
        if (barsBound) return;
        barsBound = true;
        bindBar(accuracyFill, accuracyTrack, () -> accuracyPct);
        bindBar(correctFill, correctTrack, () -> correctPct);
        bindBar(incorrectFill, incorrectTrack, () -> incorrectPct);
    }

    private void bindBar(Region bar, StackPane track, java.util.function.Supplier<Double> pctSupplier) {
        bar.prefWidthProperty().unbind();
        bar.minWidthProperty().unbind();
        bar.maxWidthProperty().unbind();

        var binding = Bindings.createDoubleBinding(
            () -> {
                double base = Math.max(track.getWidth(), track.getPrefWidth());
                return Math.max(0, Math.min(1, pctSupplier.get())) * base;
            },
            track.widthProperty(),
            track.prefWidthProperty()
        );
        bar.prefWidthProperty().bind(binding);
        bar.minWidthProperty().bind(binding);
        bar.maxWidthProperty().bind(binding);
        // ensure visible and painted
        bar.setVisible(true);
        bar.setManaged(true);
    }

    private void updateBindings() {
        // trigger evaluation after binding in case track already has width
        accuracyTrack.requestLayout();
        correctTrack.requestLayout();
        incorrectTrack.requestLayout();
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

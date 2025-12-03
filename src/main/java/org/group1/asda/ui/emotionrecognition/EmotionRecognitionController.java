package org.group1.asda.ui.emotionrecognition;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import org.group1.asda.domain.emotional.EmotionalGameState;
import org.group1.asda.domain.emotional.EmotionPattern;
import org.group1.asda.navigation.Router;

import java.util.List;

public class EmotionRecognitionController {
    @FXML private BorderPane rootPane;
    @FXML private Label questionLabel;
    @FXML private ProgressBar progressBar;
    @FXML private ImageView patternImage;
    @FXML private Button option1;
    @FXML private Button option2;
    @FXML private Button option3;
    @FXML private Button option4;
    @FXML private Label feedbackLabel;

    private final EmotionalGameState gameState = new EmotionalGameState();
    private List<String> currentOptions;
    private boolean waitingForNext = false;

    @FXML
    public void initialize() {
        updateDisplay();
    }

    private void updateDisplay() {
        int index = gameState.getCurrentQuestionIndex();
        int total = gameState.getTotalQuestions();

        questionLabel.setText(String.format("Pattern %d of %d", index + 1, total));
        progressBar.setProgress((index + 1) / (double) total);

        EmotionPattern pattern = gameState.getCurrentPattern();
        if (pattern != null) {
            try {
                Image image = new Image(getClass().getResourceAsStream(pattern.getImagePath()));
                patternImage.setImage(image);
            } catch (Exception e) {
                System.err.println("Error loading image: " + pattern.getImagePath());
            }

            currentOptions = gameState.getEmotionOptions(index);
            option1.setText(currentOptions.get(0));
            option2.setText(currentOptions.get(1));
            option3.setText(currentOptions.get(2));
            option4.setText(currentOptions.get(3));

            enableOptions(true);
            feedbackLabel.setText("");
            waitingForNext = false;
        }
    }

    @FXML
    private void onOption1() { selectOption(0, option1); }

    @FXML
    private void onOption2() { selectOption(1, option2); }

    @FXML
    private void onOption3() { selectOption(2, option3); }

    @FXML
    private void onOption4() { selectOption(3, option4); }

    private void selectOption(int optionIndex, Button clickedButton) {
        if (waitingForNext) return;

        String selectedEmotion = currentOptions.get(optionIndex);
        EmotionPattern pattern = gameState.getCurrentPattern();

        gameState.setRecognitionAnswer(gameState.getCurrentQuestionIndex(), selectedEmotion);

        boolean isCorrect = selectedEmotion.equals(pattern.getCorrectEmotion());

        clickedButton.getStyleClass().add("btn-option-selected");
        enableOptions(false);

        if (isCorrect) {
            feedbackLabel.setText("✓ Correct!");
            feedbackLabel.setStyle("-fx-text-fill: #7fae95;");
        } else {
            feedbackLabel.setText("✗ Incorrect. The emotion was: " + pattern.getCorrectEmotion());
            feedbackLabel.setStyle("-fx-text-fill: #c14545;");
        }

        waitingForNext = true;

        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
        pause.setOnFinished(e -> {
            clickedButton.getStyleClass().remove("btn-option-selected");

            if (gameState.isLastQuestion()) {
                showResults();
            } else {
                gameState.nextQuestion();
                updateDisplay();
            }
        });
        pause.play();
    }

    private void enableOptions(boolean enable) {
        option1.setDisable(!enable);
        option2.setDisable(!enable);
        option3.setDisable(!enable);
        option4.setDisable(!enable);
    }

    private void showResults() {
        int correct = gameState.getRecognitionCorrectCount();
        int total = gameState.getTotalQuestions();
        double accuracy = gameState.getRecognitionAccuracy();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Complete");
        alert.setHeaderText("Emotion Recognition Complete!");
        alert.setContentText(String.format("Score: %d / %d\nAccuracy: %.1f%%\n\n" +
            "Your emotional recognition ability has been assessed.", correct, total, accuracy));
        alert.showAndWait();
        Router.getInstance().goTo("home");
    }

    @FXML
    private void onHome() {
        Router.getInstance().goTo("home");
    }
}

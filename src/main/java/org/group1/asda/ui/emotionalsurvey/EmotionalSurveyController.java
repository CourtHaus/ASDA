package org.group1.asda.ui.emotionalsurvey;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import org.group1.asda.domain.emotional.EmotionalGameState;
import org.group1.asda.domain.emotional.EmotionPattern;
import org.group1.asda.navigation.Router;

public class EmotionalSurveyController {
    @FXML private BorderPane rootPane;
    @FXML private Label questionLabel;
    @FXML private ProgressBar progressBar;
    @FXML private ImageView patternImage;
    @FXML private Slider responseSlider;
    @FXML private Button previousButton;
    @FXML private Button nextButton;
    @FXML private Button finishButton;

    private final EmotionalGameState gameState = new EmotionalGameState();

    @FXML
    public void initialize() {
        updateDisplay();
    }

    private void updateDisplay() {
        int index = gameState.getCurrentQuestionIndex();
        int total = gameState.getTotalQuestions();

        questionLabel.setText(String.format("Question %d of %d", index + 1, total));
        progressBar.setProgress((index + 1) / (double) total);

        EmotionPattern pattern = gameState.getCurrentPattern();
        if (pattern != null) {
            try {
                Image image = new Image(getClass().getResourceAsStream(pattern.getImagePath()));
                patternImage.setImage(image);
            } catch (Exception e) {
                System.err.println("Error loading image: " + pattern.getImagePath());
            }
        }

        responseSlider.setValue(gameState.getSurveyResponse(index));

        previousButton.setDisable(index == 0);

        boolean isLast = gameState.isLastQuestion();
        nextButton.setVisible(!isLast);
        nextButton.setManaged(!isLast);
        finishButton.setVisible(isLast);
        finishButton.setManaged(isLast);
    }

    @FXML
    private void onPrevious() {
        saveCurrentResponse();
        gameState.previousQuestion();
        updateDisplay();
    }

    @FXML
    private void onNext() {
        saveCurrentResponse();
        gameState.nextQuestion();
        updateDisplay();
    }

    @FXML
    private void onFinish() {
        saveCurrentResponse();
        showResults();
    }

    private void showResults() {
        EmotionalSurveyResultsController controller = Router.getInstance()
            .goToAndGetController("emotional-survey-results", EmotionalSurveyResultsController.class);

        if (controller != null) {
            controller.setGameState(gameState);
        } else {
            // Fallback: if screen fails, return home
            Router.getInstance().goTo("home");
        }
    }

    @FXML
    private void onHome() {
        Router.getInstance().goTo("home");
    }

    private void saveCurrentResponse() {
        int index = gameState.getCurrentQuestionIndex();
        int value = (int) Math.round(responseSlider.getValue());
        gameState.setSurveyResponse(index, value);
    }
}

package org.group1.asda.ui.emotionalsurvey;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.group1.asda.domain.emotional.EmotionalGameState;
import org.group1.asda.domain.emotional.EmotionPattern;
import org.group1.asda.navigation.Router;

import java.io.IOException;

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
        showCompletionDialog();
    }

    private void showCompletionDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/survey-complete-dialog.fxml"));
            VBox dialogRoot = loader.load();

            SurveyCompleteDialogController dialogController = loader.getController();
            dialogController.setAverageScore(gameState.getSurveyAverage());

            // Load CSS
            Scene dialogScene = new Scene(dialogRoot);
            dialogScene.getStylesheets().add(getClass().getResource("/css/survey-complete-dialog.css").toExternalForm());

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.UNDECORATED);
            dialogStage.setScene(dialogScene);
            dialogStage.centerOnScreen();

            dialogStage.showAndWait();

            String action = dialogController.getSelectedAction();
            if ("PLAY_GAME".equals(action)) {
                Router.getInstance().goTo("emotion-recognition");
            } else if ("VIEW_RESULTS".equals(action)) {
                showResults();
            } else if ("HOME".equals(action)) {
                Router.getInstance().goTo("home");
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Fallback to simple alert if dialog fails to load
            showResults();
        }
    }

    private void showResults() {
        double avg = gameState.getSurveyAverage();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Survey Results");
        alert.setHeaderText("Emotional Response Survey Complete!");
        alert.setContentText(String.format("Average Response: %.2f / 5.0\n\n" +
            "Your responses have been recorded.", avg));
        alert.showAndWait();
        Router.getInstance().goTo("home");
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

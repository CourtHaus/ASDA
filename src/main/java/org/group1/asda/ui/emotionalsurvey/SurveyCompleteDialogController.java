package org.group1.asda.ui.emotionalsurvey;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SurveyCompleteDialogController {
    @FXML private Label averageLabel;
    @FXML private Button playGameButton;
    @FXML private Button viewResultsButton;
    @FXML private Button homeButton;

    private String selectedAction = null;

    public void setAverageScore(double average) {
        averageLabel.setText(String.format("Average Response: %.2f / 5.0", average));
    }

    @FXML
    public void initialize() {
        playGameButton.setOnAction(e -> {
            selectedAction = "PLAY_GAME";
            closeDialog();
        });

        viewResultsButton.setOnAction(e -> {
            selectedAction = "VIEW_RESULTS";
            closeDialog();
        });

        homeButton.setOnAction(e -> {
            selectedAction = "HOME";
            closeDialog();
        });
    }

    public String getSelectedAction() {
        return selectedAction;
    }

    private void closeDialog() {
        Stage stage = (Stage) playGameButton.getScene().getWindow();
        stage.close();
    }
}

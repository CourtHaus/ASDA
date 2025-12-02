package org.group1.asda.ui.questionnaire;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.group1.asda.navigation.Router;

/**
 * Controller for the questionnaire introduction screen.
 * Displays information about the AQ assessment before beginning.
 */
public class QuestionnaireIntroController {

    @FXML
    private Button backButton;

    @FXML
    private Button beginButton;

    @FXML
    public void initialize() {
        // Focus the begin button by default for accessibility
        if (beginButton != null) {
            beginButton.requestFocus();
        }
    }

    @FXML
    public void onBack() {
        Router.getInstance().goTo("home");
    }

    @FXML
    public void onBegin() {
        Router.getInstance().goTo("questionnaire");
    }

    @FXML
    public void onKeyPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.ESCAPE) {
            onBack();
        } else if (e.getCode() == KeyCode.ENTER) {
            onBegin();
        }
    }
}

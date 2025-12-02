package org.group1.asda.ui.attentiongametutorial;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.group1.asda.navigation.Router;

/**
 * Controller for the attention game tutorial screen.
 * Displays instructions and information about the attention game before beginning.
 */
public class AttentionGameTutorialController {

    @FXML
    private Button backButton;

    @FXML
    private Button beginButton;

    @FXML
    public void initialize() {
        // Focus the begin button by default for accessibility
        beginButton.requestFocus();
    }

    @FXML
    public void onBack() {
        Router.getInstance().goTo("home");
    }

    @FXML
    public void onBegin() {
        Router.getInstance().goTo("attention-game");
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

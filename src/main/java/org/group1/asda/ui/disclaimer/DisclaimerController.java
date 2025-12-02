package org.group1.asda.ui.disclaimer;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.group1.asda.navigation.Router;

public class DisclaimerController {

    @FXML
    public void onContinue() {
        Router.getInstance().goTo("questionnaire");
    }

    @FXML
    public void onCancel() {
        Router.getInstance().goTo("home");
    }

    @FXML
    public void onKeyPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.ESCAPE) {
            onCancel();
        } else if (e.getCode() == KeyCode.ENTER) {
            onContinue();
        }
    }
}

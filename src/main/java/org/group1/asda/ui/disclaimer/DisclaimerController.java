package org.group1.asda.ui.disclaimer;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.group1.asda.navigation.Router;

public class DisclaimerController {
    @FXML private Label titleLabel;
    @FXML private Label bodyLabel;
    @FXML private Button continueBtn;
    @FXML private Button cancelBtn;

    @FXML
    public void initialize() {
        if (titleLabel != null) {
            titleLabel.setText("Welcome to the Adolescent Autism Spectrum Quotient (AQ)");
        }
        if (bodyLabel != null) {
            bodyLabel.setText(String.join("\n",
                    "IMPORTANT DISCLAIMER:",
                    "This is the official AQ screening questionnaire developed by",
                    "S. Baron-Cohen et al. (2006) for identifying autism spectrum traits.",
                    "It is NOT a diagnostic instrument.",
                    "Only qualified healthcare professionals can provide a formal diagnosis.",
                    "",
                    "This assessment consists of 50 questions",
                    "and should take approximately 10-15 minutes to complete.",
                    "",
                    "NOTE: Questions are written in third-person ('S/he...')",
                    "Please answer as if describing yourself.")
            );
        }
    }

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

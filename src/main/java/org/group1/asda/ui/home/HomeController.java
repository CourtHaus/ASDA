package org.group1.asda.ui.home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.group1.asda.navigation.Router;
import org.group1.asda.persistence.QuestionDao;

public class HomeController {
    @FXML private Label title;
    @FXML private Button startBtn;
    @FXML private Button matchingGameBtn;
    @FXML private Button attentionGameBtn;

    @FXML
    public void initialize() {
        int count = new QuestionDao().count();
        if (title != null) {
            title.setText("ASDA – Questionnaire (" + count + " questions)");
        }
    }

    @FXML
    public void onStart(ActionEvent e) {
        // Route to disclaimer/acknowledgement screen before starting questions
        Router.getInstance().goTo("disclaimer");
    }

    @FXML
    public void onStartMatchingGame(ActionEvent e) {
        // Route to matching game
        Router.getInstance().goTo("matching-game");
    }

    @FXML
    public void onStartAttentionGame(ActionEvent e) {
        // Route to attention game
        Router.getInstance().goTo("attention-game");
    }
}

package org.group1.asda.ui.home;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.group1.asda.navigation.Router;
import org.group1.asda.persistence.QuestionDao;

public class HomeController {
    @FXML private Text title;

    @FXML
    public void initialize() {
        // Title is set in FXML with gradient
    }

    @FXML
    public void onStart(MouseEvent e) {
        // Route to questionnaire intro screen before starting questions
        Router.getInstance().goTo("questionnaire-intro");
    }

    @FXML
    public void onStartMatchingGame(MouseEvent e) {
        // Route to matching game tutorial before starting the game
        Router.getInstance().goTo("matching-game-tutorial");
    }

    @FXML
    public void onStartAttentionGame(MouseEvent e) {
        // Route to attention game tutorial before starting the game
        Router.getInstance().goTo("attention-game-tutorial");
    }

    @FXML
    public void onStartEmotionalSurvey(MouseEvent e) {
        Router.getInstance().goTo("emotional-survey");
    }

    @FXML
    public void onStartEmotionRecognition(MouseEvent e) {
        Router.getInstance().goTo("emotion-recognition");
    }
}

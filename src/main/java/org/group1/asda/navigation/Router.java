package org.group1.asda.navigation;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;

public class Router {
    private static Router INSTANCE;
    private final Stage stage;
    private final StackPane root = new StackPane();

    public static Router getInstance() { return INSTANCE; }

    public Router(Stage stage) {
        INSTANCE = this;
        this.stage = stage;
        Scene scene = new Scene(root, 1024, 680);

        // Load stylesheets
        URL base = getClass().getResource("/css/base.css");
        URL theme = getClass().getResource("/css/theme-light.css");
        URL home = getClass().getResource("/css/home.css");
        URL assessment = getClass().getResource("/css/assessment.css");
        URL loading = getClass().getResource("/css/loading.css");
        URL disclosure = getClass().getResource("/css/disclosure.css");
        URL questionnaireIntro = getClass().getResource("/css/questionnaire-intro.css");
        URL results = getClass().getResource("/css/results.css");
        URL attentionGameTutorial = getClass().getResource("/css/attention-game-tutorial.css");
        URL attentionGame = getClass().getResource("/css/attention-game.css");
        URL attentionGameResults = getClass().getResource("/css/attention-game-results.css");
        URL matchingGameTutorial = getClass().getResource("/css/matching-game-tutorial.css");
        URL matchingGame = getClass().getResource("/css/matching-game.css");
        URL matchingGameResults = getClass().getResource("/css/matching-game-results.css");
        URL emotionalSurvey = getClass().getResource("/css/emotional-survey.css");
        URL emotionRecognition = getClass().getResource("/css/emotion-recognition.css");
        URL emotionRecognitionResults = getClass().getResource("/css/emotion-recognition-results.css");

        if (base != null) scene.getStylesheets().add(base.toExternalForm());
        if (theme != null) scene.getStylesheets().add(theme.toExternalForm());
        if (home != null) scene.getStylesheets().add(home.toExternalForm());
        if (assessment != null) scene.getStylesheets().add(assessment.toExternalForm());
        if (loading != null) scene.getStylesheets().add(loading.toExternalForm());
        if (disclosure != null) scene.getStylesheets().add(disclosure.toExternalForm());
        if (questionnaireIntro != null) scene.getStylesheets().add(questionnaireIntro.toExternalForm());
        if (results != null) scene.getStylesheets().add(results.toExternalForm());
        if (attentionGameTutorial != null) scene.getStylesheets().add(attentionGameTutorial.toExternalForm());
        if (attentionGame != null) scene.getStylesheets().add(attentionGame.toExternalForm());
        if (attentionGameResults != null) scene.getStylesheets().add(attentionGameResults.toExternalForm());
        if (matchingGameTutorial != null) scene.getStylesheets().add(matchingGameTutorial.toExternalForm());
        if (matchingGame != null) scene.getStylesheets().add(matchingGame.toExternalForm());
        if (matchingGameResults != null) scene.getStylesheets().add(matchingGameResults.toExternalForm());
        if (emotionalSurvey != null) scene.getStylesheets().add(emotionalSurvey.toExternalForm());
        if (emotionRecognition != null) scene.getStylesheets().add(emotionRecognition.toExternalForm());
        if (emotionRecognitionResults != null) scene.getStylesheets().add(emotionRecognitionResults.toExternalForm());

        stage.setScene(scene);
    }

    public void goTo(String screen) {
        Parent view = switch (screen) {
            case "home" -> load("/fxml/home.fxml");
            case "loading" -> load("/fxml/loading.fxml");
            case "disclaimer" -> load("/fxml/disclosure.fxml");
            case "questionnaire-intro" -> load("/fxml/questionnaire-intro.fxml");
            case "questionnaire" -> load("/fxml/questionnaire.fxml");
            case "results" -> load("/fxml/results.fxml");
            case "matching-game-tutorial" -> load("/fxml/matching-game-tutorial.fxml");
            case "matching-game" -> load("/fxml/matching-game.fxml");
            case "matching-game-results" -> load("/fxml/matching-game-results.fxml");
            case "attention-game-tutorial" -> load("/fxml/attention-game-tutorial.fxml");
            case "attention-game" -> load("/fxml/attention-game.fxml");
            case "attention-game-results" -> load("/fxml/attention-game-results.fxml");
            case "emotional-survey" -> load("/fxml/emotional-survey.fxml");
            case "emotion-recognition" -> load("/fxml/emotion-recognition.fxml");
            case "emotion-recognition-results" -> load("/fxml/emotion-recognition-results.fxml");
            default -> new Label("Unknown screen: " + screen);
        };
        if (root.getChildren().isEmpty()) {
            root.getChildren().setAll(view);
        } else {
            fadeTo(view);
        }
    }

    /**
     * Load the screen, set it as current view, and return its controller.
     * Returns null if FXML is missing or controller cannot be obtained/cast.
     */
    public <T> T goToAndGetController(String screen, Class<T> controllerType) {
        String path = switch (screen) {
            case "home" -> "/fxml/home.fxml";
            case "loading" -> "/fxml/loading.fxml";
            case "disclaimer" -> "/fxml/disclosure.fxml";
            case "questionnaire-intro" -> "/fxml/questionnaire-intro.fxml";
            case "questionnaire" -> "/fxml/questionnaire.fxml";
            case "results" -> "/fxml/results.fxml";
            case "matching-game-tutorial" -> "/fxml/matching-game-tutorial.fxml";
            case "matching-game" -> "/fxml/matching-game.fxml";
            case "matching-game-results" -> "/fxml/matching-game-results.fxml";
            case "attention-game-tutorial" -> "/fxml/attention-game-tutorial.fxml";
            case "attention-game" -> "/fxml/attention-game.fxml";
            case "attention-game-results" -> "/fxml/attention-game-results.fxml";
            case "emotional-survey" -> "/fxml/emotional-survey.fxml";
            case "emotion-recognition" -> "/fxml/emotion-recognition.fxml";
            case "emotion-recognition-results" -> "/fxml/emotion-recognition-results.fxml";
            default -> null;
        };
        if (path == null) return null;
        try {
            URL url = getClass().getResource(path);
            if (url == null) {
                setView(new Label("Missing view: " + path));
                return null;
            }
            FXMLLoader loader = new FXMLLoader(url);
            Parent view = loader.load();
            setView(view);
            Object controller = loader.getController();
            if (controllerType.isInstance(controller)) {
                return controllerType.cast(controller);
            }
        } catch (IOException e) {
            e.printStackTrace();
            setView(new Label("Failed to load: " + path + " (" + e.getMessage() + ")"));
        }
        return null;
    }

    private void setView(Parent view) {
        if (root.getChildren().isEmpty()) {
            root.getChildren().setAll(view);
        } else {
            fadeTo(view);
        }
    }

    private Parent load(String path) {
        try {
            URL url = getClass().getResource(path);
            if (url == null) return new Label("Missing view: " + path);
            return FXMLLoader.load(url);
        } catch (IOException e) {
            // Print full stack trace to help diagnose FXML/controller initialization errors
            e.printStackTrace();
            Throwable cause = e.getCause();
            String details = e.getMessage();
            if (cause != null) {
                details = details + "; cause: " + cause.getClass().getSimpleName() + ": " + String.valueOf(cause.getMessage());
            }
            return new Label("Failed to load: " + path + " (" + details + ")");
        }
    }

    private void fadeTo(Parent next) {
        Parent current = root.getChildren().isEmpty() ? null : (Parent) root.getChildren().get(0);
        if (current == null) {
            root.getChildren().setAll(next);
            return;
        }
        FadeTransition out = new FadeTransition(Duration.millis(150), current);
        out.setToValue(0.0);
        out.setOnFinished(ev -> {
            root.getChildren().setAll(next);
            FadeTransition in = new FadeTransition(Duration.millis(150), next);
            in.setFromValue(0.0);
            in.setToValue(1.0);
            in.play();
        });
        out.play();
    }
}

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
        URL assessment = getClass().getResource("/css/assessment.css");

        if (base != null) scene.getStylesheets().add(base.toExternalForm());
        if (theme != null) scene.getStylesheets().add(theme.toExternalForm());
        if (assessment != null) scene.getStylesheets().add(assessment.toExternalForm());

        stage.setScene(scene);
    }

    public void goTo(String screen) {
        Parent view = switch (screen) {
            case "home" -> load("/fxml/home.fxml");
            case "disclaimer" -> load("/fxml/disclaimer.fxml");
            case "questionnaire" -> load("/fxml/questionnaire.fxml");
            case "results" -> load("/fxml/results.fxml");
            default -> new Label("Unknown screen: " + screen);
        };
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

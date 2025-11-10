package org.group1.asda.ui.attentiongame;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.group1.asda.domain.AttentionGameState;
import org.group1.asda.domain.Stimulus;
import org.group1.asda.navigation.Router;

import java.util.Optional;
import java.util.Random;

public class AttentionGameController {
    @FXML private BorderPane rootPane;
    @FXML private VBox introPanel;
    @FXML private Button startButton;
    @FXML private VBox gamePanel;
    @FXML private Label headerLabel;
    @FXML private Canvas stimulusCanvas;
    @FXML private Label statsLabel;

    // Pastel Colors
    private static final Color PRIMARY_COLOR   = Color.rgb(255, 182, 193); // Light Pink
    private static final Color SECONDARY_COLOR = Color.rgb(176, 224, 230); // Powder Blue

    // Config
    private static final int TOTAL_TRIALS = 100;
    private static final int STIMULUS_MS  = 1300;
    private static final int ISI_MS       = 700;

    // State
    private final AttentionGameState gameState = new AttentionGameState();
    private final Random rng = new Random();
    private Stimulus last = null;
    private Stimulus current = null;
    private boolean awaitingResponse = false;
    private boolean pressedThisTrial = false;
    private int trialIndex = 0;

    private PauseTransition showTimer;
    private PauseTransition isiTimer;

    @FXML
    public void initialize() {
        // Show intro panel by default
        introPanel.setVisible(true);
        introPanel.setManaged(true);
        gamePanel.setVisible(false);
        gamePanel.setManaged(false);

        // Setup keyboard handler
        rootPane.setOnKeyPressed(this::onKeyPressed);
        rootPane.setFocusTraversable(true);
        rootPane.requestFocus();
    }

    @FXML
    public void onStartGame() {
        // Hide intro, show game
        introPanel.setVisible(false);
        introPanel.setManaged(false);
        gamePanel.setVisible(true);
        gamePanel.setManaged(true);
        rootPane.requestFocus();
        startGame();
    }

    private void startGame() {
        gameState.resetRoundStats();
        gameState.startTimer();

        showTimer = new PauseTransition(Duration.millis(STIMULUS_MS));
        showTimer.setOnFinished(e -> {
            scoreTrial();
            clearCanvas();
            awaitingResponse = false;
            pressedThisTrial = false;
            isiTimer.play();
        });

        isiTimer = new PauseTransition(Duration.millis(ISI_MS));
        isiTimer.setOnFinished(e -> nextTrial());

        nextTrial();
    }

    private void nextTrial() {
        if (trialIndex >= TOTAL_TRIALS) {
            endGame();
            return;
        }

        current = randomStimulus();
        drawStimulus(current);

        awaitingResponse = true;
        pressedThisTrial = false;
        gameState.addAttempt();

        showTimer.playFromStart();

        trialIndex++;
        updateStatsLabel();
    }

    private void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.SPACE) {
            onSpacePressed();
        }
    }

    private void onSpacePressed() {
        if (!awaitingResponse || pressedThisTrial) return;
        pressedThisTrial = true;
    }

    private void scoreTrial() {
        boolean isTarget = (last != null && current != null && current.matches(last));
        boolean correct = (isTarget && pressedThisTrial) || (!isTarget && !pressedThisTrial);

        if (correct) {
            gameState.addCorrect();
        } else {
            gameState.addIncorrect();
        }

        last = current;
        updateStatsLabel();
    }

    private void updateStatsLabel() {
        int correct = gameState.getTotalCorrect();
        int incorrect = gameState.getTotalIncorrect();
        int attempts = correct + incorrect;
        double accuracy = gameState.getAccuracy();
        statsLabel.setText(String.format(
            "Trials: %d/%d   Correct: %d   Incorrect: %d   Accuracy: %.1f%%",
            attempts, TOTAL_TRIALS, correct, incorrect, accuracy));
    }

    private void endGame() {
        gameState.stopTimer();

        String finalSummary = gameState.getFinalSummary();
        String attentionProfile = gameState.getAttentionPerformanceIndex();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Complete");
        alert.setHeaderText("Concentration & Attention Game Complete!");
        alert.setContentText(
            finalSummary + "\n\n" + attentionProfile +
            "\n\nNote: This tool is for research and self-awareness only.\n" +
            "It does not diagnose ADHD, ADD, Autism, or any medical condition.\n" +
            "If significant attention variance is indicated, consider seeking a licensed clinician for formal evaluation."
        );

        ButtonType homeButton = new ButtonType("Return to Home");
        alert.getButtonTypes().setAll(homeButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            Router.getInstance().goTo("home");
        }
    }

    private Stimulus randomStimulus() {
        String[] shapes = {"Circle", "Square", "Triangle"};
        Color[] colors = {PRIMARY_COLOR, SECONDARY_COLOR};
        return new Stimulus(shapes[rng.nextInt(shapes.length)], colors[rng.nextInt(colors.length)]);
    }

    private void drawStimulus(Stimulus stimulus) {
        GraphicsContext gc = stimulusCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, stimulusCanvas.getWidth(), stimulusCanvas.getHeight());

        if (stimulus == null) return;

        double canvasW = stimulusCanvas.getWidth();
        double canvasH = stimulusCanvas.getHeight();
        double size = Math.min(canvasW, canvasH) / 3;
        double x = (canvasW - size) / 2;
        double y = (canvasH - size) / 2;

        gc.setFill(stimulus.getColor());

        switch (stimulus.getShape()) {
            case "Circle":
                gc.fillOval(x, y, size, size);
                break;
            case "Square":
                gc.fillRect(x, y, size, size);
                break;
            case "Triangle":
                double[] xPoints = {x + size / 2, x, x + size};
                double[] yPoints = {y, y + size, y + size};
                gc.fillPolygon(xPoints, yPoints, 3);
                break;
        }
    }

    private void clearCanvas() {
        GraphicsContext gc = stimulusCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, stimulusCanvas.getWidth(), stimulusCanvas.getHeight());
    }
}

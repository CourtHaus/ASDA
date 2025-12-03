package org.group1.asda.ui.attentiongame;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.group1.asda.domain.AttentionGameState;
import org.group1.asda.domain.Stimulus;
import org.group1.asda.navigation.Router;

import java.util.Random;

public class AttentionGameController {
    @FXML private BorderPane rootPane;
    @FXML private ImageView shapeImageView;
    @FXML private Label trialsLabel;
    @FXML private Label correctLabel;
    @FXML private Label incorrectLabel;
    @FXML private Label accuracyLabel;
    @FXML private VBox gameContent;
    @FXML private VBox pauseOverlay;
    @FXML private Button pauseButton;
    @FXML private Button resumeButton;

    // Shape images (blue and red)
    private Image blueCircle;
    private Image blueSquare;
    private Image blueTriangle;
    private Image redCircle;
    private Image redTriangle;
    private Image redSquare;

    // Colors
    private static final Color BLUE_COLOR = Color.rgb(120, 150, 179);  // #7896b3
    private static final Color RED_COLOR = Color.rgb(255, 114, 114);   // #ff7272

    // Config
    private static final int TOTAL_TRIALS = 50;
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
    private boolean isPaused = false;

    private PauseTransition showTimer;
    private PauseTransition isiTimer;

    @FXML
    public void initialize() {
        loadShapeImages();

        // Setup keyboard handler
        rootPane.setOnKeyPressed(this::onKeyPressed);
        rootPane.setFocusTraversable(true);
        rootPane.requestFocus();

        // Start the game immediately since tutorial is now separate
        startGame();
    }

    private void loadShapeImages() {
        try {
            blueCircle = new Image(getClass().getResourceAsStream("/images/loading/Ellipse 9.png"));
            blueSquare = new Image(getClass().getResourceAsStream("/images/loading/Rectangle 25.png"));
            blueTriangle = new Image(getClass().getResourceAsStream("/images/loading/Polygon 8.png"));
            redCircle = new Image(getClass().getResourceAsStream("/images/loading/Ellipse 10.png"));
            // For red triangle and square, we'll use a placeholder or create red versions
            // For now, using the available images
            redTriangle = new Image(getClass().getResourceAsStream("/images/loading/Polygon 8.png")); // TODO: Add red version
            redSquare = new Image(getClass().getResourceAsStream("/images/loading/Rectangle 25.png")); // TODO: Add red version
        } catch (Exception e) {
            System.err.println("Error loading shape images: " + e.getMessage());
        }
    }

    private void startGame() {
        gameState.resetRoundStats();
        gameState.startTimer();

        showTimer = new PauseTransition(Duration.millis(STIMULUS_MS));
        showTimer.setOnFinished(e -> {
            scoreTrial();
            clearShape();
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
        displayStimulus(current);

        awaitingResponse = true;
        pressedThisTrial = false;
        gameState.addAttempt();

        showTimer.playFromStart();

        trialIndex++;
        updateStatsDisplay();
    }

    private void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.SPACE) {
            onSpacePressed();
        }
    }

    private void onSpacePressed() {
        if (!awaitingResponse || pressedThisTrial || isPaused) return;
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
        updateStatsDisplay();
    }

    private void updateStatsDisplay() {
        int correct = gameState.getTotalCorrect();
        int incorrect = gameState.getTotalIncorrect();
        int attempts = correct + incorrect;
        double accuracy = gameState.getAccuracy();

        trialsLabel.setText(String.format("%d/%d", attempts, TOTAL_TRIALS));
        correctLabel.setText(String.valueOf(correct));
        incorrectLabel.setText(String.valueOf(incorrect));
        accuracyLabel.setText(String.format("%.0f", accuracy));
    }

    private void endGame() {
        gameState.stopTimer();
        navigateToResults();
    }

    private void navigateToResults() {
        AttentionGameResultsController resultsController =
            Router.getInstance().goToAndGetController("attention-game-results", AttentionGameResultsController.class);

        if (resultsController != null) {
            resultsController.setGameState(gameState);
        } else {
            System.err.println("Error loading results screen controller");
            // Fallback to home
            Router.getInstance().goTo("home");
        }
    }

    private Stimulus randomStimulus() {
        String[] shapes = {"Circle", "Square", "Triangle"};
        Color[] colors = {BLUE_COLOR, RED_COLOR};
        return new Stimulus(shapes[rng.nextInt(shapes.length)], colors[rng.nextInt(colors.length)]);
    }

    private void displayStimulus(Stimulus stimulus) {
        if (stimulus == null) {
            clearShape();
            return;
        }

        Image image = getShapeImage(stimulus);
        if (image != null) {
            shapeImageView.setImage(image);
            shapeImageView.setVisible(true);
        }
    }

    private Image getShapeImage(Stimulus stimulus) {
        boolean isBlue = stimulus.getColor().equals(BLUE_COLOR);
        String shape = stimulus.getShape();

        if ("Circle".equals(shape)) {
            return isBlue ? blueCircle : redCircle;
        } else if ("Square".equals(shape)) {
            return isBlue ? blueSquare : redSquare;
        } else if ("Triangle".equals(shape)) {
            return isBlue ? blueTriangle : redTriangle;
        }

        return null;
    }

    private void clearShape() {
        shapeImageView.setImage(null);
        shapeImageView.setVisible(false);
    }

    @FXML
    private void onPause() {
        if (isPaused) return;

        isPaused = true;
        pauseTimers();

        // Show pause overlay
        gameContent.setVisible(false);
        pauseOverlay.setVisible(true);
        pauseOverlay.setManaged(true);

        // Swap buttons
        pauseButton.setVisible(false);
        pauseButton.setManaged(false);
        resumeButton.setVisible(true);
        resumeButton.setManaged(true);
    }

    @FXML
    private void onResume() {
        if (!isPaused) return;

        isPaused = false;
        resumeTimers();

        // Hide pause overlay
        gameContent.setVisible(true);
        pauseOverlay.setVisible(false);
        pauseOverlay.setManaged(false);

        // Swap buttons
        pauseButton.setVisible(true);
        pauseButton.setManaged(true);
        resumeButton.setVisible(false);
        resumeButton.setManaged(false);

        rootPane.requestFocus();
    }

    @FXML
    private void onHome() {
        // Stop timers before leaving
        if (showTimer != null) showTimer.stop();
        if (isiTimer != null) isiTimer.stop();

        Router.getInstance().goTo("home");
    }

    private void pauseTimers() {
        if (showTimer != null && showTimer.getStatus() == PauseTransition.Status.RUNNING) {
            showTimer.pause();
        }
        if (isiTimer != null && isiTimer.getStatus() == PauseTransition.Status.RUNNING) {
            isiTimer.pause();
        }
    }

    private void resumeTimers() {
        if (showTimer != null && showTimer.getStatus() == PauseTransition.Status.PAUSED) {
            showTimer.play();
        }
        if (isiTimer != null && isiTimer.getStatus() == PauseTransition.Status.PAUSED) {
            isiTimer.play();
        }
    }
}

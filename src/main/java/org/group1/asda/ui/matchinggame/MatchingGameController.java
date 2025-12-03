package org.group1.asda.ui.matchinggame;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.group1.asda.domain.Card;
import org.group1.asda.domain.GameState;
import org.group1.asda.navigation.Router;

import java.util.ArrayList;
import java.util.List;

public class MatchingGameController {
    @FXML private BorderPane rootPane;
    @FXML private GridPane grid;
    @FXML private Label roundLabel;
    @FXML private Label correctLabel;
    @FXML private Label incorrectLabel;
    @FXML private Label accuracyLabel;
    @FXML private Label instructionLabel;
    @FXML private VBox gameContent;
    @FXML private VBox pauseOverlay;
    @FXML private Button pauseButton;
    @FXML private Button resumeButton;

    private static final int MAX_ROUNDS = 5;

    private GameState gameState = new GameState();
    private List<Card> cards;
    private List<CardButton> cardButtons = new ArrayList<>();
    private List<CardButton> flippedButtons = new ArrayList<>();
    private boolean lockBoard = false;
    private boolean isPaused = false;
    private int matchedPairs = 0;
    private PauseTransition previewTimer;
    private PauseTransition flipBackTimer;

    @FXML
    public void initialize() {
        setupRound();
    }

    private void setupRound() {
        grid.getChildren().clear();
        cardButtons.clear();
        flippedButtons.clear();
        lockBoard = true;
        matchedPairs = 0;

        int pairs = Math.min(2 + (gameState.getCurrRound() - 1) * 2, 8);
        cards = gameState.generateDeck(pairs);
        gameState.resetRoundStats();

        int cols = 4;
        int index = 0;
        for (Card card : cards) {
            CardButton btn = new CardButton(card);
            btn.setOnAction(e -> flip(btn));

            int row = index / cols;
            int col = index % cols;
            grid.add(btn, col, row);
            cardButtons.add(btn);
            index++;
        }

        updateStats();
        showPreview();
    }

    private void showPreview() {
        for (CardButton btn : cardButtons) {
            btn.flip();
        }

        instructionLabel.setText("Memorize the cards! Game starts in 3...");

        final int[] countdown = {3};
        previewTimer = new PauseTransition(Duration.seconds(1));
        previewTimer.setOnFinished(e -> {
            countdown[0]--;
            if (countdown[0] > 0) {
                instructionLabel.setText(String.format("Memorize the cards! Game starts in %d...", countdown[0]));
                previewTimer.playFromStart();
            } else {
                for (CardButton btn : cardButtons) {
                    btn.flip();
                }
                lockBoard = false;
                instructionLabel.setText("Select two cards and see if they match. Try to remember what you see!");
            }
        });
        previewTimer.play();
    }

    private void flip(CardButton btn) {
        if (lockBoard || btn.isFlipped() || isPaused) return;
        btn.flip();
        flippedButtons.add(btn);

        if (flippedButtons.size() == 2) {
            lockBoard = true;

            CardButton first = flippedButtons.get(0);
            CardButton second = flippedButtons.get(1);

            boolean firstSeenBefore  = first.getCard().hasBeenSeen();
            boolean secondSeenBefore = second.getCard().hasBeenSeen();
            boolean match = first.getCard().matches(second.getCard());

            boolean countThisAttempt = (firstSeenBefore || secondSeenBefore) || match;
            if (countThisAttempt) gameState.addAttempt();

            if (match) {
                matchedPairs++;
                gameState.addCorrect();

                first.setMatchedStyle();
                second.setMatchedStyle();

                flippedButtons.clear();
                lockBoard = false;
                updateStats();

                if (matchedPairs == cards.size() / 2) endRound();
            } else {
                if (firstSeenBefore || secondSeenBefore) {
                    gameState.addIncorrect();
                }

                first.setIncorrectStyle();
                second.setIncorrectStyle();

                flipBackTimer = new PauseTransition(Duration.millis(1200));
                flipBackTimer.setOnFinished(e -> {
                    first.flip();
                    second.flip();
                    first.resetStyle();
                    second.resetStyle();
                    flippedButtons.clear();
                    lockBoard = false;
                    updateStats();
                });
                flipBackTimer.play();
            }

            first.getCard().markSeen();
            second.getCard().markSeen();
        }
    }

    private void updateStats() {
        roundLabel.setText(String.valueOf(gameState.getCurrRound()));
        correctLabel.setText(String.valueOf(gameState.getTotalCorrect()));
        incorrectLabel.setText(String.valueOf(gameState.getTotalIncorrect()));
        double acc = gameState.getAccuracy();
        accuracyLabel.setText(String.format("%.0f%%", acc));
    }

    private void endRound() {
        gameState.stopTimer();

        if (gameState.getCurrRound() < MAX_ROUNDS) {
            gameState.nextRound();
            setupRound();
        } else {
            navigateToResults();
        }
    }

    private void navigateToResults() {
        MatchingGameResultsController resultsController =
            Router.getInstance().goToAndGetController("matching-game-results", MatchingGameResultsController.class);

        if (resultsController != null) {
            resultsController.setGameState(gameState);
        } else {
            System.err.println("Error loading results screen controller");
            Router.getInstance().goTo("home");
        }
    }

    @FXML
    private void onPause() {
        if (isPaused) return;

        isPaused = true;
        pauseTimers();

        gameContent.setVisible(false);
        pauseOverlay.setVisible(true);
        pauseOverlay.setManaged(true);

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

        gameContent.setVisible(true);
        pauseOverlay.setVisible(false);
        pauseOverlay.setManaged(false);

        pauseButton.setVisible(true);
        pauseButton.setManaged(true);
        resumeButton.setVisible(false);
        resumeButton.setManaged(false);

        rootPane.requestFocus();
    }

    @FXML
    private void onHome() {
        if (previewTimer != null) previewTimer.stop();
        if (flipBackTimer != null) flipBackTimer.stop();
        Router.getInstance().goTo("home");
    }

    private void pauseTimers() {
        if (previewTimer != null && previewTimer.getStatus() == PauseTransition.Status.RUNNING) {
            previewTimer.pause();
        }
        if (flipBackTimer != null && flipBackTimer.getStatus() == PauseTransition.Status.RUNNING) {
            flipBackTimer.pause();
        }
    }

    private void resumeTimers() {
        if (previewTimer != null && previewTimer.getStatus() == PauseTransition.Status.PAUSED) {
            previewTimer.play();
        }
        if (flipBackTimer != null && flipBackTimer.getStatus() == PauseTransition.Status.PAUSED) {
            flipBackTimer.play();
        }
    }

    // Card button with custom rendering
    private static class CardButton extends Button {
        private Card card;
        private boolean flipped = false;
        private Canvas canvas;
        private static final int CARD_SIZE = 100;

        public CardButton(Card card) {
            this.card = card;
            this.canvas = new Canvas(CARD_SIZE, CARD_SIZE);

            StackPane content = new StackPane(canvas);
            content.setAlignment(Pos.CENTER);
            setGraphic(content);

            setPrefSize(CARD_SIZE + 20, (CARD_SIZE + 20) * 1.3);
            setMinSize(CARD_SIZE + 20, (CARD_SIZE + 20) * 1.3);
            setMaxSize(CARD_SIZE + 20, (CARD_SIZE + 20) * 1.3);

            resetStyle();
            render();
        }

        public void flip() {
            flipped = !flipped;
            render();
        }

        public boolean isFlipped() {
            return flipped;
        }

        public Card getCard() {
            return card;
        }

        public void setMatchedStyle() {
            getStyleClass().removeAll("card-button", "card-button-back", "card-button-incorrect");
            getStyleClass().add("card-button-matched");
        }

        public void setIncorrectStyle() {
            getStyleClass().removeAll("card-button", "card-button-back", "card-button-matched");
            getStyleClass().add("card-button-incorrect");
        }

        public void resetStyle() {
            getStyleClass().removeAll("card-button-matched", "card-button-incorrect", "card-button");
            if (flipped) {
                getStyleClass().add("card-button");
            } else {
                getStyleClass().add("card-button-back");
            }
        }

        private void render() {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, CARD_SIZE, CARD_SIZE);

            if (flipped) {
                drawCard(gc);
            } else {
                gc.setFill(Color.WHITE);
                gc.setFont(javafx.scene.text.Font.font(40));
                gc.fillText("?", CARD_SIZE / 2 - 12, CARD_SIZE / 2 + 14);
            }
        }

        private void drawCard(GraphicsContext gc) {
            gc.setFill(card.getColor());
            int size = CARD_SIZE - 30;
            int x = 15;
            int y = 15;

            switch (card.getShape()) {
                case "Circle":
                    gc.fillOval(x, y, size, size);
                    break;
                case "Square":
                    gc.fillRect(x, y, size, size);
                    break;
                case "Triangle":
                    double[] xPoints = {x + size / 2.0, x, x + size};
                    double[] yPoints = {y, y + size, y + size};
                    gc.fillPolygon(xPoints, yPoints, 3);
                    break;
            }
        }
    }
}

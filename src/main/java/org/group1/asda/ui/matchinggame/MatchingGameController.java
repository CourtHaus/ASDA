package org.group1.asda.ui.matchinggame;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.group1.asda.domain.Card;
import org.group1.asda.domain.GameState;
import org.group1.asda.navigation.Router;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MatchingGameController {
    @FXML private Label statsLabel;
    @FXML private GridPane grid;

    private GameState gameState = new GameState();
    private List<Card> cards;
    private List<CardButton> cardButtons = new ArrayList<>();
    private List<CardButton> flippedButtons = new ArrayList<>();
    private boolean lockBoard = false;
    private int matchedPairs = 0;

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

        int pairs = Math.min(2 + (gameState.getCurrRound() - 1) * 2, 10);
        cards = gameState.generateDeck(pairs);
        gameState.resetRoundStats();

        int cols = (int) Math.ceil(Math.sqrt(pairs * 2));

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

        final int[] countdown = {3};
        statsLabel.setText(String.format("Round: %d | Memorize the cards! Starting in %d...",
            gameState.getCurrRound(), countdown[0]));

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> {
            countdown[0]--;
            if (countdown[0] > 0) {
                statsLabel.setText(String.format("Round: %d | Memorize the cards! Starting in %d...",
                    gameState.getCurrRound(), countdown[0]));
                pause.playFromStart();
            } else {
                for (CardButton btn : cardButtons) {
                    btn.flip();
                }
                lockBoard = false;
                updateStats();
            }
        });
        pause.play();
    }

    private void flip(CardButton btn) {
        if (lockBoard || btn.isFlipped()) return;
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

                PauseTransition pause = new PauseTransition(Duration.millis(1200));
                pause.setOnFinished(e -> {
                    first.flip();
                    second.flip();
                    first.resetStyle();
                    second.resetStyle();
                    flippedButtons.clear();
                    lockBoard = false;
                    updateStats();
                });
                pause.play();
            }

            first.getCard().markSeen();
            second.getCard().markSeen();
        }
    }

    private void updateStats() {
        statsLabel.setText(String.format(
            "Round: %d | Correct: %d | Incorrect: %d | Accuracy: %.1f%%",
            gameState.getCurrRound(),
            gameState.getTotalCorrect(),
            gameState.getTotalIncorrect(),
            gameState.getAccuracy()
        ));
    }

    private void endRound() {
        gameState.stopTimer();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Round Complete");
        alert.setHeaderText("Round " + gameState.getCurrRound() + " Complete!");
        alert.setContentText(gameState.getResultsSummary());
        alert.showAndWait();

        if (gameState.getCurrRound() < 10) {
            gameState.nextRound();
            setupRound();
        } else {
            showFinalResults();
        }
    }

    private void showFinalResults() {
        String finalSummary = gameState.getFinalSummary();
        String attentionProfile = gameState.getAttentionPerformanceIndex();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Complete");
        alert.setHeaderText("All 10 Rounds Complete!");
        alert.setContentText(
            "FINAL RESULTS\n" + finalSummary + "\n\n" + attentionProfile +
            "\n\nNote: This tool is for research and self-awareness only.\n" +
            "It does not diagnose ADHD, Autism, or any medical condition.\n" +
            "If significant attention variance is indicated, consider seeking a licensed clinician for formal evaluation."
        );

        ButtonType homeButton = new ButtonType("Return to Home");
        alert.getButtonTypes().setAll(homeButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            Router.getInstance().goTo("home");
        }
    }

    // Inner class for card buttons with custom rendering
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

            setPrefSize(CARD_SIZE + 20, CARD_SIZE + 20);
            setMinSize(CARD_SIZE + 20, CARD_SIZE + 20);
            setMaxSize(CARD_SIZE + 20, CARD_SIZE + 20);

            setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-width: 1;");
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
            setStyle("-fx-background-color: #90EE90; -fx-border-color: #ccc; -fx-border-width: 1;");
        }

        public void setIncorrectStyle() {
            setStyle("-fx-background-color: #FFB6C1; -fx-border-color: #ccc; -fx-border-width: 1;");
        }

        public void resetStyle() {
            setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-width: 1;");
        }

        private void render() {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, CARD_SIZE, CARD_SIZE);

            if (flipped) {
                drawCard(gc);
            }
        }

        private void drawCard(GraphicsContext gc) {
            gc.setFill(card.getColor());
            int size = CARD_SIZE - 20;
            int x = 10;
            int y = 10;

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

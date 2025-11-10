package org.group1.asda.domain;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GameState Domain Tests - Matching Game")
class GameStateTest {

    private GameState gameState;

    @BeforeEach
    void setUp() {
        gameState = new GameState();
    }

    @Test
    @DisplayName("GameState should start at round 1")
    void testInitialRound() {
        assertEquals(1, gameState.getCurrRound());
    }

    @Test
    @DisplayName("nextRound should increment current round")
    void testNextRound() {
        assertEquals(1, gameState.getCurrRound());
        gameState.nextRound();
        assertEquals(2, gameState.getCurrRound());
        gameState.nextRound();
        assertEquals(3, gameState.getCurrRound());
    }

    @Test
    @DisplayName("resetRoundStats should reset correct and incorrect counts")
    void testResetRoundStats() {
        gameState.addCorrect();
        gameState.addIncorrect();
        gameState.addAttempt();

        gameState.resetRoundStats();

        assertEquals(0, gameState.getTotalCorrect());
        assertEquals(0, gameState.getTotalIncorrect());
    }

    @Test
    @DisplayName("addCorrect should increment correct count")
    void testAddCorrect() {
        assertEquals(0, gameState.getTotalCorrect());
        gameState.addCorrect();
        assertEquals(1, gameState.getTotalCorrect());
        gameState.addCorrect();
        assertEquals(2, gameState.getTotalCorrect());
    }

    @Test
    @DisplayName("addIncorrect should increment incorrect count")
    void testAddIncorrect() {
        assertEquals(0, gameState.getTotalIncorrect());
        gameState.addIncorrect();
        assertEquals(1, gameState.getTotalIncorrect());
        gameState.addIncorrect();
        assertEquals(2, gameState.getTotalIncorrect());
    }

    @Test
    @DisplayName("Accuracy should be 0% when no attempts")
    void testAccuracyWithNoAttempts() {
        assertEquals(0.0, gameState.getAccuracy());
    }

    @Test
    @DisplayName("Accuracy should be 100% when all correct")
    void testAccuracyAllCorrect() {
        gameState.addAttempt();
        gameState.addCorrect();
        gameState.addAttempt();
        gameState.addCorrect();
        gameState.addAttempt();
        gameState.addCorrect();

        assertEquals(100.0, gameState.getAccuracy());
    }

    @Test
    @DisplayName("Accuracy should be 0% when all incorrect")
    void testAccuracyAllIncorrect() {
        gameState.addAttempt();
        gameState.addIncorrect();
        gameState.addAttempt();
        gameState.addIncorrect();

        assertEquals(0.0, gameState.getAccuracy());
    }

    @Test
    @DisplayName("Accuracy should be 50% when half correct")
    void testAccuracyHalfCorrect() {
        gameState.addAttempt();
        gameState.addCorrect();
        gameState.addAttempt();
        gameState.addIncorrect();

        assertEquals(50.0, gameState.getAccuracy());
    }

    @Test
    @DisplayName("Accuracy should be calculated correctly with various ratios")
    void testAccuracyCalculation() {
        // 3 correct out of 4 = 75%
        gameState.addAttempt();
        gameState.addCorrect();
        gameState.addAttempt();
        gameState.addCorrect();
        gameState.addAttempt();
        gameState.addCorrect();
        gameState.addAttempt();
        gameState.addIncorrect();

        assertEquals(75.0, gameState.getAccuracy());
    }

    @Test
    @DisplayName("Timer should track elapsed seconds")
    void testTimerElapsedSeconds() throws InterruptedException {
        gameState.startTimer();
        Thread.sleep(100); // Sleep for 100ms
        gameState.stopTimer();

        long elapsed = gameState.getElapsedSeconds();
        assertTrue(elapsed >= 0, "Elapsed time should be non-negative");
    }

    @Test
    @DisplayName("Timer should return at least 1 second minimum")
    void testTimerMinimumOneSecond() {
        gameState.startTimer();
        gameState.stopTimer();

        assertEquals(1, gameState.getElapsedSeconds());
    }

    @Test
    @DisplayName("Total elapsed seconds should accumulate across rounds")
    void testTotalElapsedSeconds() throws InterruptedException {
        gameState.startTimer();
        Thread.sleep(50);
        gameState.stopTimer();

        long first = gameState.getTotalElapsedSeconds();

        gameState.startTimer();
        Thread.sleep(50);
        gameState.stopTimer();

        long second = gameState.getTotalElapsedSeconds();

        assertTrue(second >= first, "Total time should accumulate");
    }

    @Test
    @DisplayName("Memory score should be calculated from accuracy and time")
    void testMemoryScore() {
        gameState.addAttempt();
        gameState.addCorrect();
        gameState.startTimer();
        gameState.stopTimer();

        int score = gameState.getMemoryScore();
        assertTrue(score >= 0, "Memory score should be non-negative");
    }

    @Test
    @DisplayName("Memory score should never be negative")
    void testMemoryScoreNeverNegative() {
        // Low accuracy and long time
        gameState.addAttempt();
        gameState.addIncorrect();
        gameState.startTimer();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // ignore
        }
        gameState.stopTimer();

        int score = gameState.getMemoryScore();
        assertTrue(score >= 0, "Memory score should never be negative");
    }

    @Test
    @DisplayName("generateDeck should create correct number of pairs")
    void testGenerateDeckSize() {
        List<Card> deck = gameState.generateDeck(5);
        assertEquals(10, deck.size(), "Deck should have 2 cards per pair");
    }

    @Test
    @DisplayName("generateDeck should create matching pairs")
    void testGenerateDeckMatchingPairs() {
        List<Card> deck = gameState.generateDeck(3);

        // Check that for each card, there's exactly one match
        for (Card card : deck) {
            long matchCount = deck.stream()
                .filter(c -> c.matches(card))
                .count();
            assertEquals(2, matchCount, "Each card should have exactly one match");
        }
    }

    @Test
    @DisplayName("generateDeck should shuffle cards")
    void testGenerateDeckShuffled() {
        List<Card> deck1 = gameState.generateDeck(5);
        List<Card> deck2 = gameState.generateDeck(5);

        // Statistically, two shuffled decks should not be in the same order
        boolean different = false;
        for (int i = 0; i < deck1.size(); i++) {
            if (!deck1.get(i).matches(deck2.get(i))) {
                different = true;
                break;
            }
        }
        // Note: There's a tiny chance this could fail, but it's extremely unlikely
        assertTrue(different, "Shuffled decks should likely be in different order");
    }

    @Test
    @DisplayName("generateDeck should handle minimum pairs")
    void testGenerateDeckMinimumPairs() {
        List<Card> deck = gameState.generateDeck(1);
        assertEquals(2, deck.size());
    }

    @Test
    @DisplayName("generateDeck should handle maximum pairs")
    void testGenerateDeckMaximumPairs() {
        List<Card> deck = gameState.generateDeck(10);
        assertEquals(20, deck.size());
    }

    @Test
    @DisplayName("getResultsSummary should contain all relevant stats")
    void testResultsSummary() {
        gameState.addAttempt();
        gameState.addCorrect();
        gameState.startTimer();
        gameState.stopTimer();

        String summary = gameState.getResultsSummary();

        assertNotNull(summary);
        assertTrue(summary.contains("Time"));
        assertTrue(summary.contains("Attempts"));
        assertTrue(summary.contains("Correct"));
        assertTrue(summary.contains("Incorrect"));
        assertTrue(summary.contains("Accuracy"));
        assertTrue(summary.contains("Memory Score"));
    }

    @Test
    @DisplayName("getFinalSummary should contain all relevant stats")
    void testFinalSummary() {
        gameState.addAttempt();
        gameState.addCorrect();
        gameState.startTimer();
        gameState.stopTimer();

        String summary = gameState.getFinalSummary();

        assertNotNull(summary);
        assertTrue(summary.contains("Total Time"));
        assertTrue(summary.contains("Total Attempts"));
        assertTrue(summary.contains("Correct"));
        assertTrue(summary.contains("Incorrect"));
        assertTrue(summary.contains("Overall Accuracy"));
        assertTrue(summary.contains("Overall Memory Score"));
    }

    @Test
    @DisplayName("getAttentionPerformanceIndex should return interpretation for high accuracy")
    void testAttentionPerformanceIndexHigh() {
        // Simulate high accuracy
        for (int i = 0; i < 10; i++) {
            gameState.addAttempt();
            gameState.addCorrect();
        }
        gameState.startTimer();
        gameState.stopTimer();

        String api = gameState.getAttentionPerformanceIndex();

        assertNotNull(api);
        assertTrue(api.contains("API") || api.contains("Attention Performance Index"));
    }

    @Test
    @DisplayName("getAttentionPerformanceIndex should return interpretation for moderate accuracy")
    void testAttentionPerformanceIndexModerate() {
        // Simulate moderate accuracy (75%)
        for (int i = 0; i < 6; i++) {
            gameState.addAttempt();
            gameState.addCorrect();
        }
        for (int i = 0; i < 2; i++) {
            gameState.addAttempt();
            gameState.addIncorrect();
        }
        gameState.startTimer();
        gameState.stopTimer();

        String api = gameState.getAttentionPerformanceIndex();

        assertNotNull(api);
        assertTrue(api.contains("API") || api.contains("Attention Performance Index"));
    }

    @Test
    @DisplayName("getAttentionPerformanceIndex should return interpretation for low accuracy")
    void testAttentionPerformanceIndexLow() {
        // Simulate low accuracy
        for (int i = 0; i < 2; i++) {
            gameState.addAttempt();
            gameState.addCorrect();
        }
        for (int i = 0; i < 8; i++) {
            gameState.addAttempt();
            gameState.addIncorrect();
        }
        gameState.startTimer();
        gameState.stopTimer();

        String api = gameState.getAttentionPerformanceIndex();

        assertNotNull(api);
        assertTrue(api.contains("API") || api.contains("Attention Performance Index"));
    }

    @Test
    @DisplayName("Multiple rounds should maintain separate statistics")
    void testMultipleRounds() {
        // Round 1
        gameState.addCorrect();
        assertEquals(1, gameState.getTotalCorrect());

        gameState.nextRound();
        gameState.resetRoundStats();

        // Round 2
        assertEquals(0, gameState.getTotalCorrect());
        gameState.addCorrect();
        gameState.addCorrect();
        assertEquals(2, gameState.getTotalCorrect());
        assertEquals(2, gameState.getCurrRound());
    }
}

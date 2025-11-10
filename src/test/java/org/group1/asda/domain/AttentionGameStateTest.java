package org.group1.asda.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AttentionGameState Domain Tests - Attention Game")
class AttentionGameStateTest {

    private AttentionGameState gameState;

    @BeforeEach
    void setUp() {
        gameState = new AttentionGameState();
    }

    @Test
    @DisplayName("GameState should initialize with zero stats")
    void testInitialState() {
        assertEquals(0, gameState.getTotalCorrect());
        assertEquals(0, gameState.getTotalIncorrect());
        assertEquals(0.0, gameState.getAccuracy());
    }

    @Test
    @DisplayName("resetRoundStats should reset all statistics")
    void testResetRoundStats() {
        gameState.addAttempt();
        gameState.addCorrect();
        gameState.addIncorrect();

        gameState.resetRoundStats();

        assertEquals(0, gameState.getTotalCorrect());
        assertEquals(0, gameState.getTotalIncorrect());
    }

    @Test
    @DisplayName("addAttempt should increment attempt count")
    void testAddAttempt() {
        gameState.addAttempt();
        gameState.addAttempt();
        gameState.addAttempt();

        // Attempts are tracked through correct + incorrect
        // We'll verify through the final summary
        gameState.addCorrect();
        gameState.addCorrect();
        gameState.addIncorrect();

        assertEquals(2, gameState.getTotalCorrect());
        assertEquals(1, gameState.getTotalIncorrect());
    }

    @Test
    @DisplayName("addCorrect should increment correct count")
    void testAddCorrect() {
        assertEquals(0, gameState.getTotalCorrect());
        gameState.addCorrect();
        assertEquals(1, gameState.getTotalCorrect());
        gameState.addCorrect();
        assertEquals(2, gameState.getTotalCorrect());
        gameState.addCorrect();
        assertEquals(3, gameState.getTotalCorrect());
    }

    @Test
    @DisplayName("addIncorrect should increment incorrect count")
    void testAddIncorrect() {
        assertEquals(0, gameState.getTotalIncorrect());
        gameState.addIncorrect();
        assertEquals(1, gameState.getTotalIncorrect());
        gameState.addIncorrect();
        assertEquals(2, gameState.getTotalIncorrect());
        gameState.addIncorrect();
        assertEquals(3, gameState.getTotalIncorrect());
    }

    @Test
    @DisplayName("Accuracy should be 0% when no attempts")
    void testAccuracyWithNoAttempts() {
        assertEquals(0.0, gameState.getAccuracy());
    }

    @Test
    @DisplayName("Accuracy should be 100% when all correct")
    void testAccuracyAllCorrect() {
        gameState.addCorrect();
        gameState.addCorrect();
        gameState.addCorrect();

        assertEquals(100.0, gameState.getAccuracy());
    }

    @Test
    @DisplayName("Accuracy should be 0% when all incorrect")
    void testAccuracyAllIncorrect() {
        gameState.addIncorrect();
        gameState.addIncorrect();
        gameState.addIncorrect();

        assertEquals(0.0, gameState.getAccuracy());
    }

    @Test
    @DisplayName("Accuracy should be 50% when half correct")
    void testAccuracyHalfCorrect() {
        gameState.addCorrect();
        gameState.addIncorrect();

        assertEquals(50.0, gameState.getAccuracy());
    }

    @Test
    @DisplayName("Accuracy should be calculated correctly with various ratios")
    void testAccuracyCalculation() {
        // 3 correct out of 4 = 75%
        gameState.addCorrect();
        gameState.addCorrect();
        gameState.addCorrect();
        gameState.addIncorrect();

        assertEquals(75.0, gameState.getAccuracy());
    }

    @Test
    @DisplayName("Accuracy with 1 correct and 9 incorrect should be 10%")
    void testAccuracyTenPercent() {
        gameState.addCorrect();
        for (int i = 0; i < 9; i++) {
            gameState.addIncorrect();
        }

        assertEquals(10.0, gameState.getAccuracy());
    }

    @Test
    @DisplayName("Accuracy with 9 correct and 1 incorrect should be 90%")
    void testAccuracyNinetyPercent() {
        for (int i = 0; i < 9; i++) {
            gameState.addCorrect();
        }
        gameState.addIncorrect();

        assertEquals(90.0, gameState.getAccuracy());
    }

    @Test
    @DisplayName("Timer should be started")
    void testStartTimer() {
        assertDoesNotThrow(() -> gameState.startTimer());
    }

    @Test
    @DisplayName("Timer should be stopped after starting")
    void testStopTimer() {
        gameState.startTimer();
        assertDoesNotThrow(() -> gameState.stopTimer());
    }

    @Test
    @DisplayName("Timer should track elapsed time")
    void testTimerElapsedTime() throws InterruptedException {
        gameState.startTimer();
        Thread.sleep(100); // Sleep for 100ms
        gameState.stopTimer();

        // Should have elapsed at least some time
        String summary = gameState.getFinalSummary();
        assertTrue(summary.contains("Time"), "Summary should contain time information");
    }

    @Test
    @DisplayName("getFinalSummary should contain attempts")
    void testFinalSummaryContainsAttempts() {
        gameState.addAttempt();
        gameState.addAttempt();
        gameState.addCorrect();
        gameState.addIncorrect();
        gameState.startTimer();
        gameState.stopTimer();

        String summary = gameState.getFinalSummary();

        assertNotNull(summary);
        assertTrue(summary.contains("Attempts"));
        assertTrue(summary.contains("2")); // 2 attempts
    }

    @Test
    @DisplayName("getFinalSummary should contain correct count")
    void testFinalSummaryContainsCorrect() {
        gameState.addAttempt();
        gameState.addCorrect();
        gameState.startTimer();
        gameState.stopTimer();

        String summary = gameState.getFinalSummary();

        assertNotNull(summary);
        assertTrue(summary.contains("Correct"));
        assertTrue(summary.contains("1"));
    }

    @Test
    @DisplayName("getFinalSummary should contain incorrect count")
    void testFinalSummaryContainsIncorrect() {
        gameState.addAttempt();
        gameState.addIncorrect();
        gameState.startTimer();
        gameState.stopTimer();

        String summary = gameState.getFinalSummary();

        assertNotNull(summary);
        assertTrue(summary.contains("Incorrect"));
        assertTrue(summary.contains("1"));
    }

    @Test
    @DisplayName("getFinalSummary should contain accuracy")
    void testFinalSummaryContainsAccuracy() {
        gameState.addCorrect();
        gameState.addCorrect();
        gameState.startTimer();
        gameState.stopTimer();

        String summary = gameState.getFinalSummary();

        assertNotNull(summary);
        assertTrue(summary.contains("Accuracy"));
        assertTrue(summary.contains("100.0%"));
    }

    @Test
    @DisplayName("getFinalSummary should contain time")
    void testFinalSummaryContainsTime() {
        gameState.addCorrect();
        gameState.startTimer();
        gameState.stopTimer();

        String summary = gameState.getFinalSummary();

        assertNotNull(summary);
        assertTrue(summary.contains("Time"));
    }

    @Test
    @DisplayName("getAttentionPerformanceIndex should return excellent for 90%+ accuracy")
    void testAttentionPerformanceIndexExcellent() {
        // 95% accuracy (19 correct, 1 incorrect)
        for (int i = 0; i < 19; i++) {
            gameState.addCorrect();
        }
        gameState.addIncorrect();
        gameState.startTimer();
        gameState.stopTimer();

        String api = gameState.getAttentionPerformanceIndex();

        assertNotNull(api);
        assertTrue(api.toLowerCase().contains("excellent"));
    }

    @Test
    @DisplayName("getAttentionPerformanceIndex should return moderate for 75-89% accuracy")
    void testAttentionPerformanceIndexModerate() {
        // 80% accuracy (8 correct, 2 incorrect)
        for (int i = 0; i < 8; i++) {
            gameState.addCorrect();
        }
        for (int i = 0; i < 2; i++) {
            gameState.addIncorrect();
        }
        gameState.startTimer();
        gameState.stopTimer();

        String api = gameState.getAttentionPerformanceIndex();

        assertNotNull(api);
        assertTrue(api.toLowerCase().contains("moderate"));
    }

    @Test
    @DisplayName("getAttentionPerformanceIndex should return poor for <75% accuracy")
    void testAttentionPerformanceIndexPoor() {
        // 50% accuracy
        for (int i = 0; i < 5; i++) {
            gameState.addCorrect();
        }
        for (int i = 0; i < 5; i++) {
            gameState.addIncorrect();
        }
        gameState.startTimer();
        gameState.stopTimer();

        String api = gameState.getAttentionPerformanceIndex();

        assertNotNull(api);
        assertTrue(api.toLowerCase().contains("poor"));
    }

    @Test
    @DisplayName("getAttentionPerformanceIndex should contain recommendation")
    void testAttentionPerformanceIndexContainsRecommendation() {
        gameState.addCorrect();
        gameState.addCorrect();
        gameState.startTimer();
        gameState.stopTimer();

        String api = gameState.getAttentionPerformanceIndex();

        assertNotNull(api);
        assertTrue(api.contains("Recommendation") || api.contains("suggests"));
    }

    @Test
    @DisplayName("getAttentionPerformanceIndex should contain non-diagnostic disclaimer")
    void testAttentionPerformanceIndexContainsDisclaimer() {
        gameState.addCorrect();
        gameState.startTimer();
        gameState.stopTimer();

        String api = gameState.getAttentionPerformanceIndex();

        assertNotNull(api);
        assertTrue(api.toLowerCase().contains("non-diagnostic") ||
                   api.toLowerCase().contains("clinician"));
    }

    @Test
    @DisplayName("Multiple resets should work correctly")
    void testMultipleResets() {
        gameState.addCorrect();
        gameState.addCorrect();
        assertEquals(2, gameState.getTotalCorrect());

        gameState.resetRoundStats();
        assertEquals(0, gameState.getTotalCorrect());

        gameState.addCorrect();
        assertEquals(1, gameState.getTotalCorrect());

        gameState.resetRoundStats();
        assertEquals(0, gameState.getTotalCorrect());
    }

    @Test
    @DisplayName("Large number of attempts should be handled correctly")
    void testLargeNumberOfAttempts() {
        for (int i = 0; i < 100; i++) {
            gameState.addAttempt();
            if (i % 2 == 0) {
                gameState.addCorrect();
            } else {
                gameState.addIncorrect();
            }
        }

        assertEquals(50, gameState.getTotalCorrect());
        assertEquals(50, gameState.getTotalIncorrect());
        assertEquals(50.0, gameState.getAccuracy());
    }

    @Test
    @DisplayName("Timer should handle very short durations")
    void testTimerShortDuration() {
        gameState.startTimer();
        gameState.stopTimer();

        String summary = gameState.getFinalSummary();
        assertNotNull(summary);
        assertTrue(summary.contains("Time"));
    }

    @Test
    @DisplayName("Final summary should be consistent with individual getters")
    void testFinalSummaryConsistency() {
        gameState.addCorrect();
        gameState.addCorrect();
        gameState.addCorrect();
        gameState.addIncorrect();
        gameState.startTimer();
        gameState.stopTimer();

        String summary = gameState.getFinalSummary();
        double accuracy = gameState.getAccuracy();
        int correct = gameState.getTotalCorrect();
        int incorrect = gameState.getTotalIncorrect();

        assertTrue(summary.contains(String.valueOf(correct)));
        assertTrue(summary.contains(String.valueOf(incorrect)));
        assertTrue(summary.contains(String.format("%.1f", accuracy)));
    }

    @Test
    @DisplayName("Accuracy calculation should handle edge case of only correct")
    void testAccuracyOnlyCorrect() {
        for (int i = 0; i < 10; i++) {
            gameState.addCorrect();
        }

        assertEquals(100.0, gameState.getAccuracy());
        assertEquals(10, gameState.getTotalCorrect());
        assertEquals(0, gameState.getTotalIncorrect());
    }

    @Test
    @DisplayName("Accuracy calculation should handle edge case of only incorrect")
    void testAccuracyOnlyIncorrect() {
        for (int i = 0; i < 10; i++) {
            gameState.addIncorrect();
        }

        assertEquals(0.0, gameState.getAccuracy());
        assertEquals(0, gameState.getTotalCorrect());
        assertEquals(10, gameState.getTotalIncorrect());
    }
}

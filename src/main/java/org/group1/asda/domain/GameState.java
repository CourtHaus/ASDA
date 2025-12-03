package org.group1.asda.domain;

import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameState {
    private int currRound = 1;
    private int totalCorrect = 0;
    private int totalIncorrect = 0;
    private int totalAttempts = 0;

    private long startTimeMs = 0L;
    private long endTimeMs = 0L;
    private long totalTimeMs = 0L; // Cumulative tracker

    public void resetRoundStats() {
        totalCorrect = 0;
        totalIncorrect = 0;
        totalAttempts = 0;
        startTimer();
    }

    public int getCurrRound() { return currRound; }
    public void nextRound() { currRound++; }

    public void addAttempt() { totalAttempts++; }
    public void addCorrect() { totalCorrect++; }
    public void addIncorrect() { totalIncorrect++; }

    public int getTotalCorrect() { return totalCorrect; }
    public int getTotalIncorrect() { return totalIncorrect; }

    public double getAccuracy() {
        if (totalAttempts == 0) return 0.0;
        double ratio = (double) totalCorrect / totalAttempts;
        return Math.round(Math.min(100.0, ratio * 100.0));
    }

    public void startTimer() {
        startTimeMs = System.currentTimeMillis();
    }

    public void stopTimer() {
        endTimeMs = System.currentTimeMillis();
        totalTimeMs += (endTimeMs - startTimeMs);
    }

    public long getElapsedSeconds() {
        return Math.max(1, (endTimeMs - startTimeMs) / 1000);
    }

    public long getTotalElapsedSeconds() {
        return Math.max(1, totalTimeMs / 1000);
    }

    public double getElapsedTimeSeconds() {
        return totalTimeMs / 1000.0;
    }

    public int getTotalAttempts() {
        return totalAttempts;
    }

    public int getMemoryScore() {
        double score = (getAccuracy() * 10) - (getElapsedSeconds() / 5.0);
        return (int) Math.max(0, score);
    }

    public List<Card> generateDeck(int pairs) {
        List<Card> deck = new ArrayList<>();
        String[] shapes = {"Circle", "Square", "Triangle"};
        Color[] colors = {
            Color.rgb(255, 170, 160),
            Color.rgb(176, 224, 230),
            Color.rgb(186, 231, 186),
            Color.rgb(255, 239, 186),
            Color.rgb(221, 160, 221)
        };

        for (int i = 0; i < pairs; i++) {
            String shape = shapes[i % shapes.length];
            Color color  = colors[i % colors.length];
            deck.add(new Card(shape, color));
            deck.add(new Card(shape, color));
        }

        Collections.shuffle(deck);
        return deck;
    }

    public String getResultsSummary() {
        return String.format(
            "Time: %d sec\nAttempts: %d\nCorrect: %d\nIncorrect: %d\nAccuracy: %.1f%%\nMemory Score: %d",
            getElapsedSeconds(),
            totalAttempts,
            totalCorrect,
            totalIncorrect,
            getAccuracy(),
            getMemoryScore()
        );
    }

    public String getFinalSummary() {
        return String.format(
            "Total Time (All Rounds): %d sec\nTotal Attempts: %d\nCorrect: %d\nIncorrect: %d\nOverall Accuracy: %.1f%%\nOverall Memory Score: %d",
            getTotalElapsedSeconds(),
            totalAttempts,
            totalCorrect,
            totalIncorrect,
            getAccuracy(),
            getMemoryScore()
        );
    }

    public String getAttentionPerformanceIndex() {
        double accuracy = getAccuracy();
        double timeScore = Math.max(0, 100 - (getTotalElapsedSeconds() / 2.0));
        double consistency = Math.min(100, (accuracy * 0.8 + timeScore * 0.2));
        double api = (accuracy * 0.6) + (timeScore * 0.25) + (consistency * 0.15);
        api = Math.max(0, Math.min(api, 100));

        String interpretation;
        if (api >= 85) {
            interpretation = "High Focus Consistency — No attention concerns indicated.";
        } else if (api >= 70) {
            interpretation = "Moderate Focus — Minor attention variance detected.";
        } else if (api >= 55) {
            interpretation = "Below Average Focus Consistency — Consider behavioral self-assessment.";
        } else {
            interpretation = "Significant Attention Variance Detected — Clinical consultation recommended.";
        }

        return String.format("Attention Performance Index (API): %.1f\n%s", api, interpretation);
    }
}

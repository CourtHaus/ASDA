package org.group1.asda.domain;

public class AttentionGameState {
    private int attempts;
    private int correct;
    private int incorrect;
    private long startNano;
    private long endNano;

    public void resetRoundStats() {
        attempts = 0;
        correct = 0;
        incorrect = 0;
        startNano = 0;
        endNano = 0;
    }

    public void startTimer() {
        startNano = System.nanoTime();
    }

    public void stopTimer() {
        endNano = System.nanoTime();
    }

    public void addAttempt() { attempts++; }
    public void addCorrect() { correct++; }
    public void addIncorrect() { incorrect++; }

    public int getTotalCorrect() { return correct; }
    public int getTotalIncorrect() { return incorrect; }
    public double getAccuracy() {
        int total = correct + incorrect;
        return total == 0 ? 0.0 : (100.0 * correct / total);
    }

    public String getFinalSummary() {
        double seconds = (endNano > startNano) ? ((endNano - startNano) / 1_000_000_000.0) : 0.0;
        return String.format(
            "Attempts: %d\nCorrect: %d\nIncorrect: %d\nAccuracy: %.1f%%\nTime: %.2f s",
            attempts, correct, incorrect, getAccuracy(), seconds
        );
    }

    public String getAttentionPerformanceIndex() {
        double acc = getAccuracy();
        String band;
        if (acc >= 90.0) band = "Excellent attention";
        else if (acc >= 75.0) band = "Moderate attention";
        else band = "Poor attention";

        String rec = (acc >= 90.0)
            ? "Performance suggests strong sustained attention."
            : (acc >= 75.0)
                ? "Attention is adequate but could fluctuate; consider practice or a quiet environment."
                : "Performance suggests difficulty maintaining attention under rapid stimuli.";

        return String.format("%s.\nRecommendation: %s\n(Non-diagnostic: consider a clinician if concerns persist.)", band, rec);
    }
}

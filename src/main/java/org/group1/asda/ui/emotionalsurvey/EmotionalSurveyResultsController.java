package org.group1.asda.ui.emotionalsurvey;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.beans.binding.Bindings;
import org.group1.asda.domain.emotional.EmotionalGameState;
import org.group1.asda.navigation.Router;

public class EmotionalSurveyResultsController {
    @FXML private Label averageLabel;
    @FXML private Label sentimentLabel;
    @FXML private Label totalLabel;
    @FXML private Label positiveLabel;
    @FXML private Label neutralLabel;
    @FXML private Label negativeLabel;
    @FXML private Region positiveFill;
    @FXML private Region neutralFill;
    @FXML private Region negativeFill;
    @FXML private StackPane positiveTrack;
    @FXML private StackPane neutralTrack;
    @FXML private StackPane negativeTrack;

    private double positivePct;
    private double neutralPct;
    private double negativePct;
    private boolean barsBound = false;

    public void setGameState(EmotionalGameState gameState) {
        if (gameState == null) return;

        double avg = gameState.getSurveyAverage();
        averageLabel.setText(String.format("%.2f / 5", avg));

        int[] responses = gameState.getSurveyResponses();
        int total = responses.length;
        int positive = 0;
        int neutral = 0;
        int negative = 0;
        for (int r : responses) {
            if (r >= 4) positive++;
            else if (r <= 2) negative++;
            else neutral++;
        }

        sentimentLabel.setText(sentimentSummary(avg));
        totalLabel.setText(String.valueOf(total));
        positiveLabel.setText(positive + " positive");
        neutralLabel.setText(neutral + " neutral");
        negativeLabel.setText(negative + " negative");

        positivePct = total == 0 ? 0 : (positive / (double) total);
        neutralPct = total == 0 ? 0 : (neutral / (double) total);
        negativePct = total == 0 ? 0 : (negative / (double) total);

        bindBarsOnce();
        updateBindings();
    }

    private void bindBarsOnce() {
        if (barsBound) return;
        barsBound = true;
        bindBar(positiveFill, positiveTrack, positivePct);
        bindBar(neutralFill, neutralTrack, neutralPct);
        bindBar(negativeFill, negativeTrack, negativePct);
    }

    private void bindBar(Region bar, StackPane track, double pct) {
        bar.prefWidthProperty().unbind();
        bar.minWidthProperty().unbind();
        bar.maxWidthProperty().unbind();

        var binding = Bindings.createDoubleBinding(
            () -> {
                double base = Math.max(track.getWidth(), track.getPrefWidth());
                return Math.max(0, Math.min(1, pct)) * base;
            },
            track.widthProperty(),
            track.prefWidthProperty()
        );
        bar.prefWidthProperty().bind(binding);
        bar.minWidthProperty().bind(binding);
        bar.maxWidthProperty().bind(binding);
        bar.setVisible(true);
        bar.setManaged(true);
    }

    private void updateBindings() {
        positiveTrack.requestLayout();
        neutralTrack.requestLayout();
        negativeTrack.requestLayout();
    }

    private String sentimentSummary(double avg) {
        if (avg >= 4.2) return "Uplifting mood";
        if (avg >= 3.4) return "Warm & calm";
        if (avg >= 2.6) return "Balanced mood";
        if (avg >= 2.0) return "Slightly tense";
        return "High tension";
    }

    @FXML
    private void onHome() {
        Router.getInstance().goTo("home");
    }
}

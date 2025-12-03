package org.group1.asda.ui.loading;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class LoadingController {
    @FXML private StackPane progressTrack;
    @FXML private Region progressFill;
    @FXML private Label percentLabel;

    private final DoubleProperty animatedProgress = new SimpleDoubleProperty(0);
    private Timeline progressTimeline;

    @FXML
    private void initialize() {
        animatedProgress.addListener((obs, oldVal, newVal) -> updateBar(newVal.doubleValue()));
        progressTrack.widthProperty().addListener((obs, oldVal, newVal) -> updateBar(animatedProgress.get()));
        updateBar(0.0);
    }

    /** Animate progress toward p in [0,1] */
    public void setProgress(double p) {
        double target = Math.max(0, Math.min(1, p));
        double start = animatedProgress.get();

        if (progressTimeline != null) {
            progressTimeline.stop();
        }

        double delta = Math.abs(target - start);
        double durationMs = 300 + (delta * 800); // ease small jumps but still quick

        progressTimeline = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(animatedProgress, start)),
            new KeyFrame(Duration.millis(durationMs), new KeyValue(animatedProgress, target))
        );
        progressTimeline.play();
    }

    private void updateBar(double value) {
        double clamped = Math.max(0, Math.min(1, value));
        double trackWidth = progressTrack.getWidth() > 0 ? progressTrack.getWidth() : progressTrack.getPrefWidth();
        progressFill.setPrefWidth(trackWidth * clamped);
        percentLabel.setText(Math.round(clamped * 100) + "%");
    }
}

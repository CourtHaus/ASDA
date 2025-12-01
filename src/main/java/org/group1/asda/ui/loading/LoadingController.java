package org.group1.asda.ui.loading;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class LoadingController {
    @FXML private StackPane scaleContainer;   // outer StackPane
    @FXML private Pane designCanvas;          // 1280x1024 reference pane
    @FXML private StackPane progressTrack;    // track container
    @FXML private Region progressFill;        // fill bar
    @FXML private Label percentLabel;

    private static final double DESIGN_W = 1280.0;
    private static final double DESIGN_H = 1024.0;

    @FXML
    private void initialize() {
        // Proportional scaling: scale the design canvas to fit available space
        designCanvas.scaleXProperty().bind(Bindings.createDoubleBinding(
                () -> scaleFactor(), scaleContainer.widthProperty(), scaleContainer.heightProperty()));
        designCanvas.scaleYProperty().bind(designCanvas.scaleXProperty());

        // Keep the canvas centered by letting StackPane do its job (child remains centered)

        // Initialize progress to 0
        setProgress(0.0);
    }

    private double scaleFactor() {
        double w = scaleContainer.getWidth();
        double h = scaleContainer.getHeight();
        if (w <= 0 || h <= 0) return 1.0;
        double sx = w / DESIGN_W;
        double sy = h / DESIGN_H;
        return Math.min(sx, sy);
    }

    /** p in [0,1] */
    public void setProgress(double p) {
        double clamped = Math.max(0, Math.min(1, p));
        // Track width is in design space; scale will be applied visually by parent
        double trackWidth = progressTrack.getPrefWidth(); // 400 in FXML
        progressFill.setPrefWidth(trackWidth * clamped);
        percentLabel.setText(Math.round(clamped * 100) + "%");
    }
}
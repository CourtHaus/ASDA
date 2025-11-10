package org.group1.asda.domain;

import javafx.scene.paint.Color;
import java.util.Objects;

public class Stimulus {
    private final String shape;
    private final Color color;

    public Stimulus(String shape, Color color) {
        this.shape = shape;
        this.color = color;
    }

    public boolean matches(Stimulus other) {
        if (other == null) return false;
        return Objects.equals(shape, other.shape) && Objects.equals(color, other.color);
    }

    public String getShape() {
        return shape;
    }

    public Color getColor() {
        return color;
    }
}

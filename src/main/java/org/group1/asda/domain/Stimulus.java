package org.group1.asda.domain;

import javafx.scene.paint.Color;

public class Stimulus {
    private final String shape;
    private final Color color;

    public Stimulus(String shape, Color color) {
        this.shape = shape;
        this.color = color;
    }

    public boolean matches(Stimulus other) {
        if (other == null) return false;
        return shape.equals(other.shape) && color.equals(other.color);
    }

    public String getShape() {
        return shape;
    }

    public Color getColor() {
        return color;
    }
}

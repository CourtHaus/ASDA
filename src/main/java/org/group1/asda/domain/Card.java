package org.group1.asda.domain;

import javafx.scene.paint.Color;
import java.util.Objects;

public class Card {
    private String shape;
    private Color color;
    private boolean seen = false;

    public Card(String shape, Color color) {
        this.shape = shape;
        this.color = color;
    }

    public boolean matches(Card other) {
        return Objects.equals(this.shape, other.shape) && Objects.equals(this.color, other.color);
    }

    public String getShape() { return shape; }
    public Color getColor() { return color; }

    public boolean hasBeenSeen() { return seen; }
    public void markSeen() { this.seen = true; }
}

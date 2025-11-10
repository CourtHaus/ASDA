package org.group1.asda.domain;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Stimulus Domain Tests - Attention Game")
class StimulusTest {

    private Stimulus redCircle;
    private Stimulus blueSquare;
    private Stimulus greenTriangle;

    @BeforeEach
    void setUp() {
        redCircle = new Stimulus("Circle", Color.RED);
        blueSquare = new Stimulus("Square", Color.BLUE);
        greenTriangle = new Stimulus("Triangle", Color.GREEN);
    }

    @Test
    @DisplayName("Stimulus should store shape correctly")
    void testGetShape() {
        assertEquals("Circle", redCircle.getShape());
        assertEquals("Square", blueSquare.getShape());
        assertEquals("Triangle", greenTriangle.getShape());
    }

    @Test
    @DisplayName("Stimulus should store color correctly")
    void testGetColor() {
        assertEquals(Color.RED, redCircle.getColor());
        assertEquals(Color.BLUE, blueSquare.getColor());
        assertEquals(Color.GREEN, greenTriangle.getColor());
    }

    @Test
    @DisplayName("Stimulus with same shape and color should match")
    void testMatchesWithIdenticalStimulus() {
        Stimulus anotherRedCircle = new Stimulus("Circle", Color.RED);
        assertTrue(redCircle.matches(anotherRedCircle));
    }

    @Test
    @DisplayName("Stimulus with different shape should not match")
    void testMatchesWithDifferentShape() {
        Stimulus redSquare = new Stimulus("Square", Color.RED);
        assertFalse(redCircle.matches(redSquare));
    }

    @Test
    @DisplayName("Stimulus with different color should not match")
    void testMatchesWithDifferentColor() {
        Stimulus blueCircle = new Stimulus("Circle", Color.BLUE);
        assertFalse(redCircle.matches(blueCircle));
    }

    @Test
    @DisplayName("Stimulus with different shape and color should not match")
    void testMatchesWithDifferentShapeAndColor() {
        assertFalse(redCircle.matches(blueSquare));
        assertFalse(blueSquare.matches(greenTriangle));
        assertFalse(redCircle.matches(greenTriangle));
    }

    @Test
    @DisplayName("Stimulus matches should return false when comparing to null")
    void testMatchesWithNull() {
        assertFalse(redCircle.matches(null));
    }

    @Test
    @DisplayName("Stimulus should handle pastel pink color")
    void testPastelPinkColor() {
        Color pastelPink = Color.rgb(255, 182, 193);
        Stimulus stimulus = new Stimulus("Circle", pastelPink);
        assertEquals(pastelPink, stimulus.getColor());
    }

    @Test
    @DisplayName("Stimulus should handle powder blue color")
    void testPowderBlueColor() {
        Color powderBlue = Color.rgb(176, 224, 230);
        Stimulus stimulus = new Stimulus("Square", powderBlue);
        assertEquals(powderBlue, stimulus.getColor());
    }

    @Test
    @DisplayName("Two stimuli with pastel pink should match")
    void testMatchesWithPastelPink() {
        Color pastelPink = Color.rgb(255, 182, 193);
        Stimulus stimulus1 = new Stimulus("Circle", pastelPink);
        Stimulus stimulus2 = new Stimulus("Circle", pastelPink);
        assertTrue(stimulus1.matches(stimulus2));
    }

    @Test
    @DisplayName("Stimuli with different pastel colors should not match")
    void testMatchesWithDifferentPastelColors() {
        Color pastelPink = Color.rgb(255, 182, 193);
        Color powderBlue = Color.rgb(176, 224, 230);
        Stimulus stimulus1 = new Stimulus("Circle", pastelPink);
        Stimulus stimulus2 = new Stimulus("Circle", powderBlue);
        assertFalse(stimulus1.matches(stimulus2));
    }

    @Test
    @DisplayName("Stimulus should handle all three shape types")
    void testAllShapeTypes() {
        Stimulus circle = new Stimulus("Circle", Color.RED);
        Stimulus square = new Stimulus("Square", Color.RED);
        Stimulus triangle = new Stimulus("Triangle", Color.RED);

        assertEquals("Circle", circle.getShape());
        assertEquals("Square", square.getShape());
        assertEquals("Triangle", triangle.getShape());

        assertFalse(circle.matches(square));
        assertFalse(square.matches(triangle));
        assertFalse(circle.matches(triangle));
    }

    @Test
    @DisplayName("Stimulus with empty shape string should be created")
    void testEmptyShapeString() {
        Stimulus emptyShape = new Stimulus("", Color.RED);
        assertEquals("", emptyShape.getShape());
        assertEquals(Color.RED, emptyShape.getColor());
    }

    @Test
    @DisplayName("Stimulus with null shape should be handled")
    void testNullShape() {
        Stimulus nullShape = new Stimulus(null, Color.RED);
        assertNull(nullShape.getShape());
        assertEquals(Color.RED, nullShape.getColor());
    }

    @Test
    @DisplayName("Stimulus with null color should be handled")
    void testNullColor() {
        Stimulus nullColor = new Stimulus("Circle", null);
        assertEquals("Circle", nullColor.getShape());
        assertNull(nullColor.getColor());
    }

    @Test
    @DisplayName("Two stimuli with null shapes and same color should match")
    void testMatchesWithNullShapes() {
        Stimulus stimulus1 = new Stimulus(null, Color.RED);
        Stimulus stimulus2 = new Stimulus(null, Color.RED);
        assertTrue(stimulus1.matches(stimulus2));
    }

    @Test
    @DisplayName("Two stimuli with same shape and null colors should match")
    void testMatchesWithNullColors() {
        Stimulus stimulus1 = new Stimulus("Circle", null);
        Stimulus stimulus2 = new Stimulus("Circle", null);
        assertTrue(stimulus1.matches(stimulus2));
    }

    @Test
    @DisplayName("Stimulus with null shape should not match stimulus with non-null shape")
    void testMatchesNullShapeWithNonNull() {
        Stimulus nullShape = new Stimulus(null, Color.RED);
        Stimulus nonNullShape = new Stimulus("Circle", Color.RED);
        assertFalse(nullShape.matches(nonNullShape));
    }

    @Test
    @DisplayName("Stimulus with null color should not match stimulus with non-null color")
    void testMatchesNullColorWithNonNull() {
        Stimulus nullColor = new Stimulus("Circle", null);
        Stimulus nonNullColor = new Stimulus("Circle", Color.RED);
        assertFalse(nullColor.matches(nonNullColor));
    }

    @Test
    @DisplayName("Stimulus should be case-sensitive for shape names")
    void testCaseSensitiveShapes() {
        Stimulus stimulus1 = new Stimulus("Circle", Color.RED);
        Stimulus stimulus2 = new Stimulus("circle", Color.RED);
        assertFalse(stimulus1.matches(stimulus2));
    }

    @Test
    @DisplayName("Stimulus should handle RGB colors with full precision")
    void testRGBColorPrecision() {
        Stimulus stimulus1 = new Stimulus("Circle", Color.rgb(255, 182, 193));
        Stimulus stimulus2 = new Stimulus("Circle", Color.rgb(255, 182, 194));
        assertFalse(stimulus1.matches(stimulus2));
    }

    @Test
    @DisplayName("Stimulus matching should be commutative")
    void testMatchesCommutative() {
        Stimulus stimulus1 = new Stimulus("Circle", Color.RED);
        Stimulus stimulus2 = new Stimulus("Circle", Color.RED);

        assertTrue(stimulus1.matches(stimulus2));
        assertTrue(stimulus2.matches(stimulus1));
    }

    @Test
    @DisplayName("Stimulus should match itself")
    void testMatchesItself() {
        assertTrue(redCircle.matches(redCircle));
    }

    @Test
    @DisplayName("Multiple stimuli with same properties should all match")
    void testMultipleStimuliMatch() {
        Stimulus s1 = new Stimulus("Square", Color.BLUE);
        Stimulus s2 = new Stimulus("Square", Color.BLUE);
        Stimulus s3 = new Stimulus("Square", Color.BLUE);

        assertTrue(s1.matches(s2));
        assertTrue(s2.matches(s3));
        assertTrue(s1.matches(s3));
    }

    @Test
    @DisplayName("Stimulus should handle standard JavaFX colors")
    void testStandardJavaFXColors() {
        Stimulus red = new Stimulus("Circle", Color.RED);
        Stimulus blue = new Stimulus("Circle", Color.BLUE);
        Stimulus green = new Stimulus("Circle", Color.GREEN);
        Stimulus white = new Stimulus("Circle", Color.WHITE);
        Stimulus black = new Stimulus("Circle", Color.BLACK);

        assertEquals(Color.RED, red.getColor());
        assertEquals(Color.BLUE, blue.getColor());
        assertEquals(Color.GREEN, green.getColor());
        assertEquals(Color.WHITE, white.getColor());
        assertEquals(Color.BLACK, black.getColor());
    }

    @Test
    @DisplayName("Stimulus constructor should accept both parameters")
    void testConstructorParameters() {
        Stimulus stimulus = new Stimulus("Triangle", Color.YELLOW);
        assertNotNull(stimulus);
        assertEquals("Triangle", stimulus.getShape());
        assertEquals(Color.YELLOW, stimulus.getColor());
    }
}

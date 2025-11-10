package org.group1.asda.domain;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Card Domain Tests - Matching Game")
class CardTest {

    private Card redCircle;
    private Card blueSquare;
    private Card greenTriangle;

    @BeforeEach
    void setUp() {
        redCircle = new Card("Circle", Color.RED);
        blueSquare = new Card("Square", Color.BLUE);
        greenTriangle = new Card("Triangle", Color.GREEN);
    }

    @Test
    @DisplayName("Card should store shape correctly")
    void testGetShape() {
        assertEquals("Circle", redCircle.getShape());
        assertEquals("Square", blueSquare.getShape());
        assertEquals("Triangle", greenTriangle.getShape());
    }

    @Test
    @DisplayName("Card should store color correctly")
    void testGetColor() {
        assertEquals(Color.RED, redCircle.getColor());
        assertEquals(Color.BLUE, blueSquare.getColor());
        assertEquals(Color.GREEN, greenTriangle.getColor());
    }

    @Test
    @DisplayName("Card with same shape and color should match")
    void testMatchesWithIdenticalCard() {
        Card anotherRedCircle = new Card("Circle", Color.RED);
        assertTrue(redCircle.matches(anotherRedCircle));
    }

    @Test
    @DisplayName("Card with different shape should not match")
    void testMatchesWithDifferentShape() {
        Card redSquare = new Card("Square", Color.RED);
        assertFalse(redCircle.matches(redSquare));
    }

    @Test
    @DisplayName("Card with different color should not match")
    void testMatchesWithDifferentColor() {
        Card blueCircle = new Card("Circle", Color.BLUE);
        assertFalse(redCircle.matches(blueCircle));
    }

    @Test
    @DisplayName("Card with different shape and color should not match")
    void testMatchesWithDifferentShapeAndColor() {
        assertFalse(redCircle.matches(blueSquare));
        assertFalse(blueSquare.matches(greenTriangle));
    }

    @Test
    @DisplayName("Card should initially not be marked as seen")
    void testInitialSeenState() {
        assertFalse(redCircle.hasBeenSeen());
    }

    @Test
    @DisplayName("Card should be marked as seen after markSeen is called")
    void testMarkSeen() {
        assertFalse(redCircle.hasBeenSeen());
        redCircle.markSeen();
        assertTrue(redCircle.hasBeenSeen());
    }

    @Test
    @DisplayName("Calling markSeen multiple times should keep card as seen")
    void testMarkSeenMultipleTimes() {
        redCircle.markSeen();
        assertTrue(redCircle.hasBeenSeen());
        redCircle.markSeen();
        assertTrue(redCircle.hasBeenSeen());
        redCircle.markSeen();
        assertTrue(redCircle.hasBeenSeen());
    }

    @Test
    @DisplayName("Card should handle RGB color correctly")
    void testRGBColor() {
        Card customCard = new Card("Square", Color.rgb(255, 170, 160));
        assertEquals("Square", customCard.getShape());
        assertEquals(Color.rgb(255, 170, 160), customCard.getColor());
    }

    @Test
    @DisplayName("Cards with RGB colors should match when colors are equal")
    void testMatchesWithRGBColors() {
        Card card1 = new Card("Circle", Color.rgb(255, 170, 160));
        Card card2 = new Card("Circle", Color.rgb(255, 170, 160));
        assertTrue(card1.matches(card2));
    }

    @Test
    @DisplayName("Cards with slightly different RGB values should not match")
    void testMatchesWithDifferentRGBValues() {
        Card card1 = new Card("Circle", Color.rgb(255, 170, 160));
        Card card2 = new Card("Circle", Color.rgb(255, 170, 161));
        assertFalse(card1.matches(card2));
    }

    @Test
    @DisplayName("Card with empty shape string should be created")
    void testEmptyShapeString() {
        Card emptyShape = new Card("", Color.RED);
        assertEquals("", emptyShape.getShape());
        assertEquals(Color.RED, emptyShape.getColor());
    }

    @Test
    @DisplayName("Card with null shape should be handled")
    void testNullShape() {
        Card nullShape = new Card(null, Color.RED);
        assertNull(nullShape.getShape());
        assertEquals(Color.RED, nullShape.getColor());
    }

    @Test
    @DisplayName("Card with null color should be handled")
    void testNullColor() {
        Card nullColor = new Card("Circle", null);
        assertEquals("Circle", nullColor.getShape());
        assertNull(nullColor.getColor());
    }

    @Test
    @DisplayName("Cards with null shapes should match if both null")
    void testMatchesWithNullShapes() {
        Card card1 = new Card(null, Color.RED);
        Card card2 = new Card(null, Color.RED);
        assertTrue(card1.matches(card2));
    }

    @Test
    @DisplayName("Cards with null colors should match if both null and same shape")
    void testMatchesWithNullColors() {
        Card card1 = new Card("Circle", null);
        Card card2 = new Card("Circle", null);
        assertTrue(card1.matches(card2));
    }

    @Test
    @DisplayName("Seen status should be independent between card instances")
    void testSeenStatusIndependence() {
        Card card1 = new Card("Circle", Color.RED);
        Card card2 = new Card("Circle", Color.RED);

        assertFalse(card1.hasBeenSeen());
        assertFalse(card2.hasBeenSeen());

        card1.markSeen();
        assertTrue(card1.hasBeenSeen());
        assertFalse(card2.hasBeenSeen());
    }

    @Test
    @DisplayName("Card should handle case-sensitive shape names")
    void testCaseSensitiveShapes() {
        Card card1 = new Card("Circle", Color.RED);
        Card card2 = new Card("circle", Color.RED);
        assertFalse(card1.matches(card2));
    }

    @Test
    @DisplayName("Card should handle all standard shapes")
    void testAllStandardShapes() {
        Card circle = new Card("Circle", Color.RED);
        Card square = new Card("Square", Color.RED);
        Card triangle = new Card("Triangle", Color.RED);

        assertEquals("Circle", circle.getShape());
        assertEquals("Square", square.getShape());
        assertEquals("Triangle", triangle.getShape());
    }

    @Test
    @DisplayName("Card should handle all pastel colors used in game")
    void testGamePastelColors() {
        Card card1 = new Card("Circle", Color.rgb(255, 170, 160));
        Card card2 = new Card("Circle", Color.rgb(176, 224, 230));
        Card card3 = new Card("Circle", Color.rgb(186, 231, 186));
        Card card4 = new Card("Circle", Color.rgb(255, 239, 186));
        Card card5 = new Card("Circle", Color.rgb(221, 160, 221));

        assertNotNull(card1.getColor());
        assertNotNull(card2.getColor());
        assertNotNull(card3.getColor());
        assertNotNull(card4.getColor());
        assertNotNull(card5.getColor());
    }
}

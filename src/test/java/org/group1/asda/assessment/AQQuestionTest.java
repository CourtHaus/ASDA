package org.group1.asda.assessment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AQQuestion Tests")
class AQQuestionTest {
    private AQQuestion question;
    private String[] responseOptions;
    private int[] scoreValues;

    @BeforeEach
    void setUp() {
        responseOptions = new String[]{"Definitely Agree", "Slightly Agree", "Slightly Disagree", "Definitely Disagree"};
        scoreValues = new int[]{1, 1, 0, 0};
        question = new AQQuestion(1, "Test question text?", responseOptions, scoreValues);
    }

    @Test
    @DisplayName("Constructor should create question with correct properties")
    void testConstructor() {
        assertEquals(1, question.getQuestionId());
        assertEquals("Test question text?", question.getQuestionText());
        assertArrayEquals(responseOptions, question.getResponseOptions());
        assertArrayEquals(scoreValues, question.getScoreValues());
        assertEquals(4, question.getNumberOfOptions());
    }

    @Test
    @DisplayName("Constructor should throw exception when arrays have different lengths")
    void testConstructorWithMismatchedArrays() {
        String[] options = new String[]{"Option 1", "Option 2"};
        int[] scores = new int[]{1, 1, 0};

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new AQQuestion(1, "Test?", options, scores);
        });

        assertTrue(exception.getMessage().contains("same length"));
    }

    @Test
    @DisplayName("getScoreForChoice should return correct score for valid index")
    void testGetScoreForChoice() {
        assertEquals(1, question.getScoreForChoice(0));
        assertEquals(1, question.getScoreForChoice(1));
        assertEquals(0, question.getScoreForChoice(2));
        assertEquals(0, question.getScoreForChoice(3));
    }

    @Test
    @DisplayName("getScoreForChoice should throw exception for negative index")
    void testGetScoreForChoiceNegativeIndex() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            question.getScoreForChoice(-1);
        });

        assertTrue(exception.getMessage().contains("Invalid choice index"));
    }

    @Test
    @DisplayName("getScoreForChoice should throw exception for index out of bounds")
    void testGetScoreForChoiceOutOfBounds() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            question.getScoreForChoice(4);
        });

        assertTrue(exception.getMessage().contains("Invalid choice index"));
    }

    @Test
    @DisplayName("isValidChoice should return true for valid 1-based choices")
    void testIsValidChoice() {
        assertTrue(question.isValidChoice(1));
        assertTrue(question.isValidChoice(2));
        assertTrue(question.isValidChoice(3));
        assertTrue(question.isValidChoice(4));
    }

    @Test
    @DisplayName("isValidChoice should return false for invalid choices")
    void testIsInvalidChoice() {
        assertFalse(question.isValidChoice(0));
        assertFalse(question.isValidChoice(5));
        assertFalse(question.isValidChoice(-1));
        assertFalse(question.isValidChoice(100));
    }

    @Test
    @DisplayName("Question with reversed scoring should work correctly")
    void testReversedScoring() {
        int[] reversedScores = new int[]{0, 0, 1, 1};
        AQQuestion reversedQuestion = new AQQuestion(2, "Reversed question?", responseOptions, reversedScores);

        assertEquals(0, reversedQuestion.getScoreForChoice(0));
        assertEquals(0, reversedQuestion.getScoreForChoice(1));
        assertEquals(1, reversedQuestion.getScoreForChoice(2));
        assertEquals(1, reversedQuestion.getScoreForChoice(3));
    }

    @Test
    @DisplayName("displayQuestion should not throw exceptions")
    void testDisplayQuestion() {
        // This method prints to console, just verify it doesn't throw
        assertDoesNotThrow(() -> question.displayQuestion());
    }
}

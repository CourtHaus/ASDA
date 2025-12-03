package org.group1.asda.domain.emotional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FacialEmotionGameStateTest {

    private FacialEmotionGameState gameState;

    @BeforeEach
    public void setUp() {
        gameState = new FacialEmotionGameState();
    }

    @Test
    public void testInitialState() {
        assertEquals(0, gameState.getCurrentQuestionIndex());
        assertEquals(15, gameState.getTotalQuestions());
        assertNotNull(gameState.getCurrentPattern());
    }

    @Test
    public void testNavigateQuestions() {
        assertEquals(0, gameState.getCurrentQuestionIndex());
        
        gameState.nextQuestion();
        assertEquals(1, gameState.getCurrentQuestionIndex());
        
        gameState.previousQuestion();
        assertEquals(0, gameState.getCurrentQuestionIndex());
        
        // Can't go below 0
        gameState.previousQuestion();
        assertEquals(0, gameState.getCurrentQuestionIndex());
    }

    @Test
    public void testLastQuestion() {
        assertFalse(gameState.isLastQuestion());
        
        // Navigate to last question
        for (int i = 0; i < gameState.getTotalQuestions() - 1; i++) {
            gameState.nextQuestion();
        }
        
        assertTrue(gameState.isLastQuestion());
    }

    @Test
    public void testSetAndGetAnswers() {
        gameState.setRecognitionAnswer(0, "Joy");
        assertEquals("Joy", gameState.getRecognitionAnswer(0));
        
        gameState.setRecognitionAnswer(1, "Sadness");
        assertEquals("Sadness", gameState.getRecognitionAnswer(1));
    }

    @Test
    public void testAccuracyCalculation() {
        // Set all answers correctly
        List<EmotionPattern> patterns = gameState.getPatterns();
        for (int i = 0; i < patterns.size(); i++) {
            gameState.setRecognitionAnswer(i, patterns.get(i).getCorrectEmotion());
        }
        
        assertEquals(patterns.size(), gameState.getRecognitionCorrectCount());
        assertEquals(100.0, gameState.getRecognitionAccuracy(), 0.01);
    }

    @Test
    public void testPartialAccuracy() {
        List<EmotionPattern> patterns = gameState.getPatterns();
        
        // Set half correct
        for (int i = 0; i < patterns.size() / 2; i++) {
            gameState.setRecognitionAnswer(i, patterns.get(i).getCorrectEmotion());
        }
        
        // Set remaining incorrect
        for (int i = patterns.size() / 2; i < patterns.size(); i++) {
            gameState.setRecognitionAnswer(i, "Wrong Answer");
        }
        
        int expectedCorrect = patterns.size() / 2;
        assertEquals(expectedCorrect, gameState.getRecognitionCorrectCount());
        
        double expectedAccuracy = (expectedCorrect * 100.0) / patterns.size();
        assertEquals(expectedAccuracy, gameState.getRecognitionAccuracy(), 0.01);
    }

    @Test
    public void testEmotionOptions() {
        List<String> options = gameState.getEmotionOptions(0);
        
        assertEquals(4, options.size());
        
        // Correct answer should be in options
        String correctEmotion = gameState.getCurrentPattern().getCorrectEmotion();
        assertTrue(options.contains(correctEmotion));
    }

    @Test
    public void testReset() {
        // Make some changes
        gameState.nextQuestion();
        gameState.setRecognitionAnswer(0, "Joy");
        gameState.setRecognitionAnswer(1, "Sadness");
        
        // Reset
        gameState.reset();
        
        assertEquals(0, gameState.getCurrentQuestionIndex());
        assertNull(gameState.getRecognitionAnswer(0));
        assertNull(gameState.getRecognitionAnswer(1));
    }

    @Test
    public void testAllPatternsHaveFacialImages() {
        List<EmotionPattern> patterns = gameState.getPatterns();
        
        for (EmotionPattern pattern : patterns) {
            assertTrue(pattern.getImagePath().contains("/images/emotional/faces/"));
            assertTrue(pattern.getImagePath().endsWith(".jpg"));
            assertNotNull(pattern.getCorrectEmotion());
        }
    }

    @Test
    public void testGetAllAnswers() {
        gameState.setRecognitionAnswer(0, "Joy");
        gameState.setRecognitionAnswer(1, "Sadness");
        
        String[] answers = gameState.getAllAnswers();
        
        assertEquals(15, answers.length);
        assertEquals("Joy", answers[0]);
        assertEquals("Sadness", answers[1]);
    }
}

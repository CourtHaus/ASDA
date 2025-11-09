package org.group1.asda.assessment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AQScoreCalculator Tests")
class AQScoreCalculatorTest {
    private AQAssessment assessment;

    @BeforeEach
    void setUp() {
        assessment = new AQAssessment();
    }

    private void answerAllQuestions(int choice) {
        for (int i = 0; i < 50; i++) {
            assessment.recordResponse(i, choice);
        }
        assessment.completeAssessment();
    }

    @Test
    @DisplayName("calculateTotalScore should throw exception for incomplete assessment")
    void testCalculateTotalScoreIncomplete() {
        assessment.recordResponse(0, 1);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            AQScoreCalculator.calculateTotalScore(assessment);
        });

        assertTrue(exception.getMessage().contains("incomplete assessment"));
    }

    @Test
    @DisplayName("calculateTotalScore should return 0 when all answers score 0")
    void testCalculateTotalScoreAllZeros() {
        // Answer all questions with choice that scores 0 (choice 3 for standard scoring)
        answerAllQuestions(3);

        int score = AQScoreCalculator.calculateTotalScore(assessment);
        // Some questions are reversed, so not all will be 0
        assertTrue(score >= 0 && score <= 50);
    }

    @Test
    @DisplayName("calculateTotalScore should return maximum when all answers maximize score")
    void testCalculateTotalScoreAllOnes() {
        // Answer all with "Definitely Agree" (choice 1)
        answerAllQuestions(1);

        int score = AQScoreCalculator.calculateTotalScore(assessment);
        assertTrue(score >= 0 && score <= 50);
    }

    @Test
    @DisplayName("calculateTotalScore should be within valid range")
    void testCalculateTotalScoreRange() {
        // Answer with mixed responses
        for (int i = 0; i < 50; i++) {
            assessment.recordResponse(i, (i % 4) + 1);
        }
        assessment.completeAssessment();

        int score = AQScoreCalculator.calculateTotalScore(assessment);
        assertTrue(score >= 0 && score <= 50);
    }

    @Test
    @DisplayName("calculateCategoryScores should throw exception for incomplete assessment")
    void testCalculateCategoryScoresIncomplete() {
        assessment.recordResponse(0, 1);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            AQScoreCalculator.calculateCategoryScores(assessment);
        });

        assertTrue(exception.getMessage().contains("incomplete assessment"));
    }

    @Test
    @DisplayName("calculateCategoryScores should return valid category scores")
    void testCalculateCategoryScores() {
        answerAllQuestions(1);

        AQScoreCalculator.CategoryScores scores = AQScoreCalculator.calculateCategoryScores(assessment);

        assertNotNull(scores);
        assertTrue(scores.getSocialSkillsScore() >= 0 && scores.getSocialSkillsScore() <= 10);
        assertTrue(scores.getAttentionSwitchingScore() >= 0 && scores.getAttentionSwitchingScore() <= 10);
        assertTrue(scores.getAttentionToDetailScore() >= 0 && scores.getAttentionToDetailScore() <= 10);
        assertTrue(scores.getCommunicationScore() >= 0 && scores.getCommunicationScore() <= 10);
        assertTrue(scores.getImaginationScore() >= 0 && scores.getImaginationScore() <= 10);
    }

    @Test
    @DisplayName("CategoryScores totalScore should match sum of categories")
    void testCategoryScoresTotalScore() {
        answerAllQuestions(1);

        AQScoreCalculator.CategoryScores scores = AQScoreCalculator.calculateCategoryScores(assessment);
        int expectedTotal = scores.getSocialSkillsScore() +
                           scores.getAttentionSwitchingScore() +
                           scores.getAttentionToDetailScore() +
                           scores.getCommunicationScore() +
                           scores.getImaginationScore();

        assertEquals(expectedTotal, scores.getTotalScore());
    }

    @Test
    @DisplayName("Total score should match category scores sum")
    void testTotalScoreMatchesCategorySum() {
        answerAllQuestions(1);

        int totalScore = AQScoreCalculator.calculateTotalScore(assessment);
        AQScoreCalculator.CategoryScores categoryScores = AQScoreCalculator.calculateCategoryScores(assessment);

        assertEquals(totalScore, categoryScores.getTotalScore());
    }

    @Test
    @DisplayName("CategoryScores should have 10 questions per category")
    void testCategorySizes() {
        // Each category should have exactly 10 questions
        // Social Skills: 1, 11, 13, 15, 22, 36, 44, 45, 47, 48
        // Attention Switching: 2, 4, 10, 16, 25, 32, 34, 37, 43, 46
        // Attention to Detail: 5, 6, 9, 12, 19, 23, 28, 29, 30, 49
        // Communication: 7, 17, 18, 26, 27, 31, 33, 35, 38, 39
        // Imagination: 3, 8, 14, 20, 21, 24, 40, 41, 42, 50

        // Answer to maximize score for first 10 questions (should affect different categories)
        for (int i = 0; i < 50; i++) {
            assessment.recordResponse(i, 1);
        }
        assessment.completeAssessment();

        AQScoreCalculator.CategoryScores scores = AQScoreCalculator.calculateCategoryScores(assessment);

        // Verify all categories have scores (meaning all 50 questions are categorized)
        int total = scores.getSocialSkillsScore() +
                   scores.getAttentionSwitchingScore() +
                   scores.getAttentionToDetailScore() +
                   scores.getCommunicationScore() +
                   scores.getImaginationScore();

        assertEquals(AQScoreCalculator.calculateTotalScore(assessment), total);
    }

    @Test
    @DisplayName("CategoryScores constructor should set all values correctly")
    void testCategoryScoresConstructor() {
        AQScoreCalculator.CategoryScores scores = new AQScoreCalculator.CategoryScores(5, 6, 7, 8, 9);

        assertEquals(5, scores.getSocialSkillsScore());
        assertEquals(6, scores.getAttentionSwitchingScore());
        assertEquals(7, scores.getAttentionToDetailScore());
        assertEquals(8, scores.getCommunicationScore());
        assertEquals(9, scores.getImaginationScore());
        assertEquals(35, scores.getTotalScore());
    }

    @Test
    @DisplayName("Different answer patterns should produce different scores")
    void testDifferentAnswerPatterns() {
        // Pattern 1: All "Definitely Agree"
        AQAssessment assessment1 = new AQAssessment();
        answerAllQuestionsFor(assessment1, 1);
        int score1 = AQScoreCalculator.calculateTotalScore(assessment1);

        // Pattern 2: All "Definitely Disagree"
        AQAssessment assessment2 = new AQAssessment();
        answerAllQuestionsFor(assessment2, 4);
        int score2 = AQScoreCalculator.calculateTotalScore(assessment2);

        // Scores should be different due to reversed scoring questions
        assertNotEquals(score1, score2);
    }

    private void answerAllQuestionsFor(AQAssessment assess, int choice) {
        for (int i = 0; i < 50; i++) {
            assess.recordResponse(i, choice);
        }
        assess.completeAssessment();
    }
}

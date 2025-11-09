package org.group1.asda.assessment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AQResultInterpreter Tests")
class AQResultInterpreterTest {
    private AQScoreCalculator.CategoryScores createCategoryScores() {
        return new AQScoreCalculator.CategoryScores(5, 5, 5, 5, 5);
    }

    @Test
    @DisplayName("interpret should throw exception for negative score")
    void testInterpretNegativeScore() {
        AQScoreCalculator.CategoryScores scores = createCategoryScores();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            AQResultInterpreter.interpret(-1, scores);
        });

        assertTrue(exception.getMessage().contains("Invalid total score"));
    }

    @Test
    @DisplayName("interpret should throw exception for score above 50")
    void testInterpretScoreAbove50() {
        AQScoreCalculator.CategoryScores scores = createCategoryScores();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            AQResultInterpreter.interpret(51, scores);
        });

        assertTrue(exception.getMessage().contains("Invalid total score"));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    @DisplayName("interpret should return 'Minimal' risk for scores 0-5")
    void testInterpretMinimalRisk(int score) {
        AQScoreCalculator.CategoryScores scores = createCategoryScores();
        AQResultInterpreter.AssessmentSummary result = AQResultInterpreter.interpret(score, scores);

        assertEquals("Minimal", result.riskLevel);
        assertNotNull(result.overallInterpretation);
        assertNotNull(result.recommendation);
        assertEquals(score, result.totalScore);
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 10, 15})
    @DisplayName("interpret should return 'Low-Moderate' risk for scores 6-15")
    void testInterpretLowModerateRisk(int score) {
        AQScoreCalculator.CategoryScores scores = createCategoryScores();
        AQResultInterpreter.AssessmentSummary result = AQResultInterpreter.interpret(score, scores);

        assertEquals("Low-Moderate", result.riskLevel);
        assertNotNull(result.overallInterpretation);
        assertNotNull(result.recommendation);
    }

    @ParameterizedTest
    @ValueSource(ints = {16, 20, 25})
    @DisplayName("interpret should return 'Moderate' risk for scores 16-25")
    void testInterpretModerateRisk(int score) {
        AQScoreCalculator.CategoryScores scores = createCategoryScores();
        AQResultInterpreter.AssessmentSummary result = AQResultInterpreter.interpret(score, scores);

        assertEquals("Moderate", result.riskLevel);
        assertNotNull(result.overallInterpretation);
        assertNotNull(result.recommendation);
    }

    @ParameterizedTest
    @ValueSource(ints = {26, 30, 32})
    @DisplayName("interpret should return 'High' risk for scores 26-32")
    void testInterpretHighRisk(int score) {
        AQScoreCalculator.CategoryScores scores = createCategoryScores();
        AQResultInterpreter.AssessmentSummary result = AQResultInterpreter.interpret(score, scores);

        assertEquals("High", result.riskLevel);
        assertNotNull(result.overallInterpretation);
        assertNotNull(result.recommendation);
    }

    @ParameterizedTest
    @ValueSource(ints = {33, 40, 50})
    @DisplayName("interpret should return 'Very High' risk for scores 33-50")
    void testInterpretVeryHighRisk(int score) {
        AQScoreCalculator.CategoryScores scores = createCategoryScores();
        AQResultInterpreter.AssessmentSummary result = AQResultInterpreter.interpret(score, scores);

        assertEquals("Very High", result.riskLevel);
        assertNotNull(result.overallInterpretation);
        assertNotNull(result.recommendation);
    }

    @Test
    @DisplayName("AssessmentSummary should store all values correctly")
    void testAssessmentSummary() {
        AQScoreCalculator.CategoryScores categoryScores = createCategoryScores();
        AQResultInterpreter.AssessmentSummary summary = AQResultInterpreter.interpret(20, categoryScores);

        assertEquals(20, summary.totalScore);
        assertEquals("Moderate", summary.riskLevel);
        assertNotNull(summary.overallInterpretation);
        assertNotNull(summary.recommendation);
        assertSame(categoryScores, summary.categoryScores);
    }

    @Test
    @DisplayName("interpretCategories should return interpretation for all categories")
    void testInterpretCategories() {
        AQScoreCalculator.CategoryScores scores = new AQScoreCalculator.CategoryScores(3, 4, 5, 6, 7);
        String interpretation = AQResultInterpreter.interpretCategories(scores);

        assertNotNull(interpretation);
        assertTrue(interpretation.contains("Social Skills"));
        assertTrue(interpretation.contains("Attention Switching"));
        assertTrue(interpretation.contains("Attention to Detail"));
        assertTrue(interpretation.contains("Communication"));
        assertTrue(interpretation.contains("Imagination"));

        assertTrue(interpretation.contains("Score: 3"));
        assertTrue(interpretation.contains("Score: 4"));
        assertTrue(interpretation.contains("Score: 5"));
        assertTrue(interpretation.contains("Score: 6"));
        assertTrue(interpretation.contains("Score: 7"));
    }

    @Test
    @DisplayName("interpretCategories should handle low scores")
    void testInterpretCategoriesLowScores() {
        AQScoreCalculator.CategoryScores scores = new AQScoreCalculator.CategoryScores(1, 2, 1, 2, 1);
        String interpretation = AQResultInterpreter.interpretCategories(scores);

        assertNotNull(interpretation);
        assertTrue(interpretation.contains("Typical") || interpretation.contains("typical"));
    }

    @Test
    @DisplayName("interpretCategories should handle high scores")
    void testInterpretCategoriesHighScores() {
        AQScoreCalculator.CategoryScores scores = new AQScoreCalculator.CategoryScores(8, 9, 8, 9, 10);
        String interpretation = AQResultInterpreter.interpretCategories(scores);

        assertNotNull(interpretation);
        assertTrue(interpretation.contains("Significant") || interpretation.contains("Exceptional"));
    }

    @Test
    @DisplayName("interpretCategories should handle mid-range scores")
    void testInterpretCategoriesMidScores() {
        AQScoreCalculator.CategoryScores scores = new AQScoreCalculator.CategoryScores(5, 5, 5, 5, 5);
        String interpretation = AQResultInterpreter.interpretCategories(scores);

        assertNotNull(interpretation);
        assertFalse(interpretation.isEmpty());
    }

    @Test
    @DisplayName("Overall interpretation should be meaningful for minimal scores")
    void testOverallInterpretationMinimal() {
        AQScoreCalculator.CategoryScores scores = createCategoryScores();
        AQResultInterpreter.AssessmentSummary summary = AQResultInterpreter.interpret(3, scores);

        assertTrue(summary.overallInterpretation.contains("minimal"));
        assertTrue(summary.recommendation.contains("typical range") ||
                   summary.recommendation.contains("No specific action"));
    }

    @Test
    @DisplayName("Overall interpretation should be meaningful for very high scores")
    void testOverallInterpretationVeryHigh() {
        AQScoreCalculator.CategoryScores scores = createCategoryScores();
        AQResultInterpreter.AssessmentSummary summary = AQResultInterpreter.interpret(45, scores);

        assertTrue(summary.overallInterpretation.contains("significant") ||
                   summary.overallInterpretation.contains("many"));
        assertTrue(summary.recommendation.contains("strongly recommend") ||
                   summary.recommendation.contains("comprehensive evaluation"));
    }

    @Test
    @DisplayName("Recommendation should escalate with higher scores")
    void testRecommendationEscalation() {
        AQScoreCalculator.CategoryScores scores = createCategoryScores();

        AQResultInterpreter.AssessmentSummary low = AQResultInterpreter.interpret(3, scores);
        AQResultInterpreter.AssessmentSummary moderate = AQResultInterpreter.interpret(20, scores);
        AQResultInterpreter.AssessmentSummary high = AQResultInterpreter.interpret(40, scores);

        // Low scores should have less urgent recommendations
        assertFalse(low.recommendation.toLowerCase().contains("strongly"));

        // High scores should have strong recommendations
        assertTrue(high.recommendation.toLowerCase().contains("strongly") ||
                   high.recommendation.toLowerCase().contains("comprehensive"));
    }
}

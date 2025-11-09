package org.group1.asda.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AssessmentService Tests")
class AssessmentServiceTest {
    private AssessmentService service;

    @BeforeEach
    void setUp() {
        service = new AssessmentService();
    }

    @Test
    @DisplayName("deriveLevel should return 'Unknown' for zero questions")
    void testDeriveLevelZeroQuestions() {
        String level = service.deriveLevel(0, 0);
        assertEquals("Unknown", level);
    }

    @Test
    @DisplayName("deriveLevel should return 'Unknown' for negative question count")
    void testDeriveLevelNegativeQuestions() {
        String level = service.deriveLevel(10, -1);
        assertEquals("Unknown", level);
    }

    @ParameterizedTest
    @CsvSource({
        "1, 1, Elevated",      // Min score (1*1=1), <= 1.66 threshold
        "1, 10, Elevated",     // 10 questions, min score
        "13, 10, Elevated",    // 10 questions, below 33% threshold (10 + 20*0.33 ≈ 16.6)
        "16, 10, Elevated"     // 10 questions, at 33% threshold
    })
    @DisplayName("deriveLevel should return 'Elevated' for low scores")
    void testDeriveLevelElevated(int totalScore, int questionCount) {
        String level = service.deriveLevel(totalScore, questionCount);
        assertEquals("Elevated", level);
    }

    @ParameterizedTest
    @CsvSource({
        "2, 1, Moderate",      // 1 question, middle score (1.66 < 2 <= 2.32)
        "17, 10, Moderate",    // 10 questions, above 33% threshold
        "23, 10, Moderate",    // 10 questions, at 66% threshold (10 + 20*0.66 ≈ 23.3)
        "34, 20, Moderate"     // 20 questions, mid-range
    })
    @DisplayName("deriveLevel should return 'Moderate' for mid-range scores")
    void testDeriveLevelModerate(int totalScore, int questionCount) {
        String level = service.deriveLevel(totalScore, questionCount);
        assertEquals("Moderate", level);
    }

    @ParameterizedTest
    @CsvSource({
        "24, 10, Low",         // 10 questions, above 66% threshold
        "30, 10, Low",         // 10 questions, max score
        "3, 1, Low",           // 1 question, max score
        "60, 20, Low",         // 20 questions, max score
        "150, 50, Low"         // 50 questions, max score
    })
    @DisplayName("deriveLevel should return 'Low' for high scores")
    void testDeriveLevelLow(int totalScore, int questionCount) {
        String level = service.deriveLevel(totalScore, questionCount);
        assertEquals("Low", level);
    }

    @Test
    @DisplayName("deriveLevel should handle 50 question assessment - minimum")
    void testDeriveLevelFiftyQuestionsMin() {
        // 50 questions: min=50, max=150, range=100
        // Elevated: <= 83.3, Moderate: <= 116.6, Low: > 116.6
        String level = service.deriveLevel(50, 50);
        assertEquals("Elevated", level);
    }

    @Test
    @DisplayName("deriveLevel should handle 50 question assessment - mid")
    void testDeriveLevelFiftyQuestionsMid() {
        String level = service.deriveLevel(100, 50);
        assertEquals("Moderate", level);
    }

    @Test
    @DisplayName("deriveLevel should handle 50 question assessment - max")
    void testDeriveLevelFiftyQuestionsMax() {
        String level = service.deriveLevel(150, 50);
        assertEquals("Low", level);
    }

    @Test
    @DisplayName("deriveLevel threshold calculation - exactly at 33%")
    void testDeriveLevelExactlyAt33Percent() {
        // For 10 questions: min=10, max=30, range=20
        // 33% threshold = 10 + 20*0.33 = 16.6
        // Score of 16 should be Elevated, 17 should be Moderate
        assertEquals("Elevated", service.deriveLevel(16, 10));
        assertEquals("Moderate", service.deriveLevel(17, 10));
    }

    @Test
    @DisplayName("deriveLevel threshold calculation - exactly at 66%")
    void testDeriveLevelExactlyAt66Percent() {
        // For 10 questions: min=10, max=30, range=20
        // 66% threshold = 10 + 20*0.66 = 23.2
        // Score of 23 should be Moderate, 24 should be Low
        assertEquals("Moderate", service.deriveLevel(23, 10));
        assertEquals("Low", service.deriveLevel(24, 10));
    }

    @Test
    @DisplayName("deriveLevel should handle single question - all three levels")
    void testDeriveLevelSingleQuestion() {
        // For 1 question: min=1, max=3, range=2
        // Elevated: <= 1.66, Moderate: <= 2.32, Low: > 2.32
        assertEquals("Elevated", service.deriveLevel(1, 1));
        assertEquals("Moderate", service.deriveLevel(2, 1));
        assertEquals("Low", service.deriveLevel(3, 1));
    }

    @Test
    @DisplayName("AssessmentResult record should store all values correctly")
    void testAssessmentResultRecord() {
        var categoryScores = new java.util.LinkedHashMap<String, Integer>();
        categoryScores.put("Social", 10);
        categoryScores.put("Communication", 15);

        AssessmentService.AssessmentResult result =
            new AssessmentService.AssessmentResult(50, 20, categoryScores, "Moderate");

        assertEquals(50, result.totalScore());
        assertEquals(20, result.questionCount());
        assertEquals("Moderate", result.level());
        assertEquals(2, result.categoryScores().size());
        assertEquals(10, result.categoryScores().get("Social"));
        assertEquals(15, result.categoryScores().get("Communication"));
    }

    @Test
    @DisplayName("deriveLevel should be consistent for same inputs")
    void testDeriveLevelConsistency() {
        String level1 = service.deriveLevel(100, 50);
        String level2 = service.deriveLevel(100, 50);

        assertEquals(level1, level2);
    }

    @Test
    @DisplayName("deriveLevel boundary testing - comprehensive")
    void testDeriveLevelBoundaries() {
        // Test boundaries for 10-question assessment
        // min=10, max=30, range=20
        // t1 = 10 + 20*0.33 = 16.66
        // t2 = 10 + 20*0.66 = 23.32

        // Just below t1
        assertEquals("Elevated", service.deriveLevel(16, 10));

        // Just above t1
        assertEquals("Moderate", service.deriveLevel(17, 10));

        // Just below t2
        assertEquals("Moderate", service.deriveLevel(23, 10));

        // Just above t2
        assertEquals("Low", service.deriveLevel(24, 10));

        // Exactly at min
        assertEquals("Elevated", service.deriveLevel(10, 10));

        // Exactly at max
        assertEquals("Low", service.deriveLevel(30, 10));
    }
}

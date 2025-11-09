package org.group1.asda.assessment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AQAssessment Tests")
class AQAssessmentTest {
    private AQAssessment assessment;

    @BeforeEach
    void setUp() {
        assessment = new AQAssessment();
    }

    @Test
    @DisplayName("Constructor should initialize with 50 questions")
    void testConstructor() {
        assertEquals(50, assessment.getTotalQuestions());
        assertFalse(assessment.isCompleted());
        assertEquals(0, assessment.getAnsweredQuestions());
    }

    @Test
    @DisplayName("getQuestions should return all 50 questions")
    void testGetQuestions() {
        List<AQQuestion> questions = assessment.getQuestions();
        assertNotNull(questions);
        assertEquals(50, questions.size());

        // Verify first question
        AQQuestion firstQuestion = questions.get(0);
        assertEquals(1, firstQuestion.getQuestionId());
        assertTrue(firstQuestion.getQuestionText().contains("prefers to do things with others"));
    }

    @Test
    @DisplayName("recordResponse should store valid response")
    void testRecordResponse() {
        assessment.recordResponse(0, 1);
        assertEquals(1, assessment.getAnsweredQuestions());

        List<Integer> responses = assessment.getUserResponses();
        assertEquals(1, responses.get(0));
    }

    @Test
    @DisplayName("recordResponse should update existing response")
    void testUpdateResponse() {
        assessment.recordResponse(0, 1);
        assessment.recordResponse(0, 3);

        assertEquals(1, assessment.getAnsweredQuestions());
        assertEquals(3, assessment.getUserResponses().get(0));
    }

    @Test
    @DisplayName("recordResponse should throw exception for invalid question index")
    void testRecordResponseInvalidIndex() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            assessment.recordResponse(-1, 1);
        });
        assertTrue(exception.getMessage().contains("Invalid question index"));

        exception = assertThrows(IllegalArgumentException.class, () -> {
            assessment.recordResponse(50, 1);
        });
        assertTrue(exception.getMessage().contains("Invalid question index"));
    }

    @Test
    @DisplayName("recordResponse should throw exception for invalid response choice")
    void testRecordResponseInvalidChoice() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            assessment.recordResponse(0, 0);
        });
        assertTrue(exception.getMessage().contains("Invalid response choice"));

        exception = assertThrows(IllegalArgumentException.class, () -> {
            assessment.recordResponse(0, 5);
        });
        assertTrue(exception.getMessage().contains("Invalid response choice"));
    }

    @Test
    @DisplayName("areAllQuestionsAnswered should return false when not all answered")
    void testAreAllQuestionsAnsweredFalse() {
        assertFalse(assessment.areAllQuestionsAnswered());

        assessment.recordResponse(0, 1);
        assertFalse(assessment.areAllQuestionsAnswered());
    }

    @Test
    @DisplayName("areAllQuestionsAnswered should return true when all answered")
    void testAreAllQuestionsAnsweredTrue() {
        // Answer all 50 questions
        for (int i = 0; i < 50; i++) {
            assessment.recordResponse(i, 1);
        }

        assertTrue(assessment.areAllQuestionsAnswered());
    }

    @Test
    @DisplayName("completeAssessment should succeed when all questions answered")
    void testCompleteAssessment() {
        // Answer all 50 questions
        for (int i = 0; i < 50; i++) {
            assessment.recordResponse(i, 1);
        }

        assertDoesNotThrow(() -> assessment.completeAssessment());
        assertTrue(assessment.isCompleted());
    }

    @Test
    @DisplayName("completeAssessment should throw exception when incomplete")
    void testCompleteAssessmentIncomplete() {
        assessment.recordResponse(0, 1);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            assessment.completeAssessment();
        });

        assertTrue(exception.getMessage().contains("not all questions answered"));
        assertFalse(assessment.isCompleted());
    }

    @Test
    @DisplayName("getAnsweredQuestions should return correct count")
    void testGetAnsweredQuestions() {
        assertEquals(0, assessment.getAnsweredQuestions());

        assessment.recordResponse(0, 1);
        assertEquals(1, assessment.getAnsweredQuestions());

        assessment.recordResponse(1, 2);
        assessment.recordResponse(2, 3);
        assertEquals(3, assessment.getAnsweredQuestions());

        // Updating same question shouldn't increase count
        assessment.recordResponse(0, 4);
        assertEquals(3, assessment.getAnsweredQuestions());
    }

    @Test
    @DisplayName("Questions should have correct response options")
    void testQuestionResponseOptions() {
        List<AQQuestion> questions = assessment.getQuestions();

        for (AQQuestion q : questions) {
            assertEquals(4, q.getNumberOfOptions());
            String[] options = q.getResponseOptions();
            assertEquals("Definitely Agree", options[0]);
            assertEquals("Slightly Agree", options[1]);
            assertEquals("Slightly Disagree", options[2]);
            assertEquals("Definitely Disagree", options[3]);
        }
    }

    @Test
    @DisplayName("Questions should have valid scoring patterns")
    void testQuestionScoringPatterns() {
        List<AQQuestion> questions = assessment.getQuestions();

        for (AQQuestion q : questions) {
            int[] scores = q.getScoreValues();
            assertEquals(4, scores.length);

            // Scores should be either {1,1,0,0} or {0,0,1,1}
            boolean isStandardScoring = scores[0] == 1 && scores[1] == 1 && scores[2] == 0 && scores[3] == 0;
            boolean isReversedScoring = scores[0] == 0 && scores[1] == 0 && scores[2] == 1 && scores[3] == 1;

            assertTrue(isStandardScoring || isReversedScoring,
                "Question " + q.getQuestionId() + " has invalid scoring pattern");
        }
    }

    @Test
    @DisplayName("All questions should have unique IDs from 1 to 50")
    void testQuestionIds() {
        List<AQQuestion> questions = assessment.getQuestions();

        for (int i = 0; i < 50; i++) {
            assertEquals(i + 1, questions.get(i).getQuestionId());
        }
    }
}

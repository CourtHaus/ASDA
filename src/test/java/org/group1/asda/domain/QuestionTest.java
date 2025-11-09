package org.group1.asda.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Question Domain Tests")
class QuestionTest {

    @Test
    @DisplayName("Question record should store all fields correctly")
    void testQuestionRecord() {
        Question question = new Question("Q001", "Do you have difficulty with social interactions?", "Social");

        assertEquals("Q001", question.code());
        assertEquals("Do you have difficulty with social interactions?", question.text());
        assertEquals("Social", question.category());
    }

    @Test
    @DisplayName("Question records with same values should be equal")
    void testQuestionEquality() {
        Question question1 = new Question("Q001", "Test question", "Social");
        Question question2 = new Question("Q001", "Test question", "Social");

        assertEquals(question1, question2);
        assertEquals(question1.hashCode(), question2.hashCode());
    }

    @Test
    @DisplayName("Question records with different values should not be equal")
    void testQuestionInequality() {
        Question question1 = new Question("Q001", "Test question", "Social");
        Question question2 = new Question("Q002", "Test question", "Social");
        Question question3 = new Question("Q001", "Different text", "Social");
        Question question4 = new Question("Q001", "Test question", "Communication");

        assertNotEquals(question1, question2);
        assertNotEquals(question1, question3);
        assertNotEquals(question1, question4);
    }

    @Test
    @DisplayName("Question should handle empty strings")
    void testQuestionWithEmptyStrings() {
        Question question = new Question("", "", "");

        assertEquals("", question.code());
        assertEquals("", question.text());
        assertEquals("", question.category());
    }

    @Test
    @DisplayName("Question should handle null values")
    void testQuestionWithNullValues() {
        Question question = new Question(null, null, null);

        assertNull(question.code());
        assertNull(question.text());
        assertNull(question.category());
    }

    @Test
    @DisplayName("Question toString should contain all field values")
    void testQuestionToString() {
        Question question = new Question("Q001", "Test question text", "Social");
        String toString = question.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("Q001"));
        assertTrue(toString.contains("Test question text"));
        assertTrue(toString.contains("Social"));
    }

    @Test
    @DisplayName("Question should handle long text")
    void testQuestionWithLongText() {
        String longText = "This is a very long question text ".repeat(10);
        Question question = new Question("Q999", longText, "Category");

        assertEquals("Q999", question.code());
        assertEquals(longText, question.text());
        assertEquals("Category", question.category());
    }

    @Test
    @DisplayName("Question should handle special characters in text")
    void testQuestionWithSpecialCharacters() {
        Question question = new Question(
            "Q-001",
            "Does the person have difficulty with \"social\" interactions? (Yes/No)",
            "Social & Communication"
        );

        assertEquals("Q-001", question.code());
        assertTrue(question.text().contains("\"social\""));
        assertTrue(question.text().contains("(Yes/No)"));
        assertEquals("Social & Communication", question.category());
    }

    @Test
    @DisplayName("Question should be immutable")
    void testQuestionImmutability() {
        Question question = new Question("Q001", "Original text", "Social");

        // Records are immutable - this test verifies the record structure
        // Cannot modify fields after creation
        assertEquals("Q001", question.code());
        assertEquals("Original text", question.text());
        assertEquals("Social", question.category());

        // Creating a new instance with different values
        Question modified = new Question("Q002", "Modified text", "Communication");

        // Original should be unchanged
        assertEquals("Q001", question.code());
        assertEquals("Original text", question.text());
        assertEquals("Social", question.category());

        // New instance has different values
        assertNotEquals(question, modified);
    }

    @Test
    @DisplayName("Multiple questions with different categories")
    void testMultipleQuestionCategories() {
        Question social = new Question("Q001", "Social question", "Social");
        Question communication = new Question("Q002", "Communication question", "Communication");
        Question attention = new Question("Q003", "Attention question", "Attention");

        assertEquals("Social", social.category());
        assertEquals("Communication", communication.category());
        assertEquals("Attention", attention.category());

        assertNotEquals(social, communication);
        assertNotEquals(social, attention);
        assertNotEquals(communication, attention);
    }
}

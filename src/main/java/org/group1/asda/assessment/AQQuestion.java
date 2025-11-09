package org.group1.asda.assessment;

/**
 * Represents a single question in the AQ (Autism Quotient) assessment
 * Contains question text, possible responses, and scoring weights
 */
public class AQQuestion {
    private final String questionText;
    private final String[] responseOptions;
    private final int[] scoreValues;
    private final int questionId;

    /**
     * Constructor for creating a question
     * @param questionId Unique identifier for the question
     * @param questionText The text of the question
     * @param responseOptions Array of possible response options
     * @param scoreValues Array of score values corresponding to each response option
     */
    public AQQuestion(int questionId, String questionText, String[] responseOptions, int[] scoreValues) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.responseOptions = responseOptions;
        this.scoreValues = scoreValues;

        if (responseOptions.length != scoreValues.length) {
            throw new IllegalArgumentException("Response options and score values must have the same length");
        }
    }

    /**
     * Display the question and response options to the user
     */
    public void displayQuestion() {
        System.out.println("Question " + questionId + ":");
        System.out.println(questionText);
        System.out.println();

        for (int i = 0; i < responseOptions.length; i++) {
            System.out.println((i + 1) + ". " + responseOptions[i]);
        }
        System.out.println();
    }

    /**
     * Get the score for a given response choice
     * @param choiceIndex The index of the user's choice (0-based)
     * @return The score value for that choice
     */
    public int getScoreForChoice(int choiceIndex) {
        if (choiceIndex < 0 || choiceIndex >= scoreValues.length) {
            throw new IllegalArgumentException("Invalid choice index: " + choiceIndex);
        }
        return scoreValues[choiceIndex];
    }

    /**
     * Validate if a response choice is valid
     * @param choice The user's choice (1-based)
     * @return true if valid, false otherwise
     */
    public boolean isValidChoice(int choice) {
        return choice >= 1 && choice <= responseOptions.length;
    }

    // Getters
    public String getQuestionText() { return questionText; }
    public String[] getResponseOptions() { return responseOptions; }
    public int[] getScoreValues() { return scoreValues; }
    public int getQuestionId() { return questionId; }
    public int getNumberOfOptions() { return responseOptions.length; }
}

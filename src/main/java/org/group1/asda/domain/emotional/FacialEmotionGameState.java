package org.group1.asda.domain.emotional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FacialEmotionGameState {
    private int currentQuestionIndex = 0;
    private String[] recognitionAnswers;
    private List<EmotionPattern> facialPatterns;

    // Emotion options based on the facial images available
    private static final List<String> FACIAL_EMOTIONS = Arrays.asList(
        "Terror", "Acceptance", "Admiration", "Annoyance", "Apprehension", 
        "Boredom", "Distraction", "Grief", "Joy", "Loathing", 
        "Pensive", "Rage", "Sadness", "Surprise", "Trust"
    );

    public FacialEmotionGameState() {
        loadFacialPatterns();
        recognitionAnswers = new String[facialPatterns.size()];
    }

    private void loadFacialPatterns() {
        facialPatterns = new ArrayList<>();

        // Map facial emotion images to their correct emotions
        facialPatterns.add(new EmotionPattern(0, "/images/emotional/faces/acceptance.jpg", "Acceptance"));
        facialPatterns.add(new EmotionPattern(1, "/images/emotional/faces/admiration.jpg", "Admiration"));
        facialPatterns.add(new EmotionPattern(2, "/images/emotional/faces/annoyance.jpg", "Annoyance"));
        facialPatterns.add(new EmotionPattern(3, "/images/emotional/faces/apprehension.jpg", "Apprehension"));
        facialPatterns.add(new EmotionPattern(4, "/images/emotional/faces/boredom.jpg", "Boredom"));
        facialPatterns.add(new EmotionPattern(5, "/images/emotional/faces/distraction.jpg", "Distraction"));
        facialPatterns.add(new EmotionPattern(6, "/images/emotional/faces/grief.jpg", "Grief"));
        facialPatterns.add(new EmotionPattern(7, "/images/emotional/faces/joy.jpg", "Joy"));
        facialPatterns.add(new EmotionPattern(8, "/images/emotional/faces/loathing.jpg", "Loathing"));
        facialPatterns.add(new EmotionPattern(9, "/images/emotional/faces/pensive.jpg", "Pensive"));
        facialPatterns.add(new EmotionPattern(10, "/images/emotional/faces/rage.jpg", "Rage"));
        facialPatterns.add(new EmotionPattern(11, "/images/emotional/faces/sadness.jpg", "Sadness"));
        facialPatterns.add(new EmotionPattern(12, "/images/emotional/faces/surprise.jpg", "Surprise"));
        facialPatterns.add(new EmotionPattern(13, "/images/emotional/faces/Terror.jpg", "Terror"));
        facialPatterns.add(new EmotionPattern(14, "/images/emotional/faces/trust.jpg", "Trust"));
    }

    public List<EmotionPattern> getPatterns() {
        return facialPatterns;
    }

    public EmotionPattern getCurrentPattern() {
        if (currentQuestionIndex >= 0 && currentQuestionIndex < facialPatterns.size()) {
            return facialPatterns.get(currentQuestionIndex);
        }
        return null;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void nextQuestion() {
        if (currentQuestionIndex < facialPatterns.size() - 1) {
            currentQuestionIndex++;
        }
    }

    public void previousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
        }
    }

    public void setRecognitionAnswer(int index, String answer) {
        if (index >= 0 && index < recognitionAnswers.length) {
            recognitionAnswers[index] = answer;
        }
    }

    public String getRecognitionAnswer(int index) {
        if (index >= 0 && index < recognitionAnswers.length) {
            return recognitionAnswers[index];
        }
        return null;
    }

    public boolean isLastQuestion() {
        return currentQuestionIndex >= facialPatterns.size() - 1;
    }

    public int getTotalQuestions() {
        return facialPatterns.size();
    }

    public int getRecognitionCorrectCount() {
        int correct = 0;
        for (int i = 0; i < facialPatterns.size(); i++) {
            if (facialPatterns.get(i).getCorrectEmotion().equals(recognitionAnswers[i])) {
                correct++;
            }
        }
        return correct;
    }

    public double getRecognitionAccuracy() {
        return (getRecognitionCorrectCount() / (double) facialPatterns.size()) * 100.0;
    }

    public List<String> getEmotionOptions(int questionIndex) {
        List<String> options = new ArrayList<>(FACIAL_EMOTIONS);
        Collections.shuffle(options);
        // Ensure correct answer is included
        String correctEmotion = facialPatterns.get(questionIndex).getCorrectEmotion();
        if (!options.contains(correctEmotion)) {
            options.set(0, correctEmotion);
        }
        return options.subList(0, Math.min(4, options.size())); // Return 4 options
    }

    public void reset() {
        currentQuestionIndex = 0;
        Arrays.fill(recognitionAnswers, null);
    }

    // Method to get all answers for results display
    public String[] getAllAnswers() {
        return recognitionAnswers.clone();
    }
}

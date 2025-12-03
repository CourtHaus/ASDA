package org.group1.asda.domain.emotional;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EmotionalGameState {
    private int currentQuestionIndex = 0;
    private int[] surveyResponses;
    private String[] recognitionAnswers;
    private List<EmotionPattern> patterns;

    // Emotion options for recognition game
    private static final List<String> EMOTIONS = Arrays.asList(
        "Joy", "Sadness", "Anger", "Fear", "Calm", "Anxiety", "Excitement", "Peace"
    );

    public EmotionalGameState() {
        loadPatterns();
        surveyResponses = new int[patterns.size()];
        recognitionAnswers = new String[patterns.size()];
        Arrays.fill(surveyResponses, 3); // Default middle value
    }

    private void loadPatterns() {
        patterns = new ArrayList<>();

        // Map image names to emotions (verified based on actual image content)
        patterns.add(new EmotionPattern(0, "/images/emotional/Symmetrical_teal_mandala_52138706.png", "Calm"));
        patterns.add(new EmotionPattern(1, "/images/emotional/Soft_pastel_watercolor_blend_95c1fcf6.png", "Peace"));
        patterns.add(new EmotionPattern(2, "/images/emotional/Angular_red_orange_triangles_44d8ca47.png", "Anger"));
        patterns.add(new EmotionPattern(3, "/images/emotional/Dark_dramatic_swirls_2b331362.png", "Fear"));
        patterns.add(new EmotionPattern(4, "/images/emotional/Colorful_cheerful_circles_dots_095da58b.png", "Joy"));
        patterns.add(new EmotionPattern(5, "/images/emotional/Gentle_rounded_shapes_c911c7e3.png", "Calm"));
        patterns.add(new EmotionPattern(6, "/images/emotional/Sharp_thorny_shapes_6c199097.png", "Anxiety"));
        patterns.add(new EmotionPattern(7, "/images/emotional/Minimalist_intersecting_lines_d32e24cc.png", "Calm"));
        patterns.add(new EmotionPattern(8, "/images/emotional/Earthy_horizontal_bands_057cbce0.png", "Calm"));
        patterns.add(new EmotionPattern(9, "/images/emotional/Bold_black_white_stripes_c42e4275.png", "Anxiety"));
        patterns.add(new EmotionPattern(10, "/images/emotional/Dense_overlapping_shapes_f5bd9da9.png", "Anxiety"));
        patterns.add(new EmotionPattern(11, "/images/emotional/Glowing_radiant_circles_f78881de.png", "Joy"));
        patterns.add(new EmotionPattern(12, "/images/emotional/Golden_spiral_pattern_1e5131ac.png", "Excitement"));
        patterns.add(new EmotionPattern(13, "/images/emotional/Abstract_blue_purple_curves_6652dd0a.png", "Peace"));
        patterns.add(new EmotionPattern(14, "/images/emotional/Chaotic_broken_fragments_34ddc593.png", "Anxiety"));
        patterns.add(new EmotionPattern(15, "/images/emotional/Organic_flowing_curves_ea06e102.png", "Peace"));
        patterns.add(new EmotionPattern(16, "/images/emotional/Delicate_flowing_ribbons_7eb91db6.png", "Peace"));
        patterns.add(new EmotionPattern(17, "/images/emotional/Lavender_cream_clouds_fcde9d54.png", "Calm"));
        patterns.add(new EmotionPattern(18, "/images/emotional/Icy_crystalline_forms_15d04c71.png", "Fear"));
        patterns.add(new EmotionPattern(19, "/images/emotional/Vibrant_paint_splatter_13d2318d.png", "Excitement"));
    }

    public List<EmotionPattern> getPatterns() {
        return patterns;
    }

    public EmotionPattern getCurrentPattern() {
        if (currentQuestionIndex >= 0 && currentQuestionIndex < patterns.size()) {
            return patterns.get(currentQuestionIndex);
        }
        return null;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void nextQuestion() {
        if (currentQuestionIndex < patterns.size() - 1) {
            currentQuestionIndex++;
        }
    }

    public void previousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
        }
    }

    public void setSurveyResponse(int index, int value) {
        if (index >= 0 && index < surveyResponses.length) {
            surveyResponses[index] = value;
        }
    }

    public int getSurveyResponse(int index) {
        if (index >= 0 && index < surveyResponses.length) {
            return surveyResponses[index];
        }
        return 3;
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
        return currentQuestionIndex >= patterns.size() - 1;
    }

    public int getTotalQuestions() {
        return patterns.size();
    }

    public double getSurveyAverage() {
        double sum = 0;
        for (int response : surveyResponses) {
            sum += response;
        }
        return sum / surveyResponses.length;
    }

    public int getRecognitionCorrectCount() {
        int correct = 0;
        for (int i = 0; i < patterns.size(); i++) {
            if (patterns.get(i).getCorrectEmotion().equals(recognitionAnswers[i])) {
                correct++;
            }
        }
        return correct;
    }

    public double getRecognitionAccuracy() {
        return (getRecognitionCorrectCount() / (double) patterns.size()) * 100.0;
    }

    public List<String> getEmotionOptions(int questionIndex) {
        List<String> options = new ArrayList<>(EMOTIONS);
        Collections.shuffle(options);
        // Ensure correct answer is included
        String correctEmotion = patterns.get(questionIndex).getCorrectEmotion();
        if (!options.contains(correctEmotion)) {
            options.set(0, correctEmotion);
        }
        return options.subList(0, Math.min(4, options.size())); // Return 4 options
    }

    public void reset() {
        currentQuestionIndex = 0;
        Arrays.fill(surveyResponses, 3);
        Arrays.fill(recognitionAnswers, null);
    }
}

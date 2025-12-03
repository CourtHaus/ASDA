package com.emotionalsurvey;

import java.util.List;

public class EmotionQuestion {
    private int id;
    private String imagePath;
    private String correctEmotion;
    private List<String> options;

    public EmotionQuestion(int id, String imagePath, String correctEmotion, List<String> options) {
        this.id = id;
        this.imagePath = imagePath;
        this.correctEmotion = correctEmotion;
        this.options = options;
    }

    public int getId() {
        return id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getCorrectEmotion() {
        return correctEmotion;
    }

    public List<String> getOptions() {
        return options;
    }
}

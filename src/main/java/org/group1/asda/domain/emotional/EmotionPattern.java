package org.group1.asda.domain.emotional;

public class EmotionPattern {
    private final int id;
    private final String imagePath;
    private final String correctEmotion;

    public EmotionPattern(int id, String imagePath, String correctEmotion) {
        this.id = id;
        this.imagePath = imagePath;
        this.correctEmotion = correctEmotion;
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
}

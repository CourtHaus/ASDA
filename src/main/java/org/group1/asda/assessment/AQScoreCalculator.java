package org.group1.asda.assessment;

import java.util.List;

/**
 * Calculates scores for the AQ assessment
 */
public class AQScoreCalculator {

    public static int calculateTotalScore(AQAssessment assessment) {
        if (!assessment.isCompleted()) {
            throw new IllegalStateException("Cannot calculate score for incomplete assessment");
        }
        List<AQQuestion> questions = assessment.getQuestions();
        List<Integer> responses = assessment.getUserResponses();
        int total = 0;
        for (int i = 0; i < questions.size(); i++) {
            AQQuestion q = questions.get(i);
            int choice1Based = responses.get(i);
            total += q.getScoreForChoice(choice1Based - 1);
        }
        return total;
    }

    public static CategoryScores calculateCategoryScores(AQAssessment assessment) {
        if (!assessment.isCompleted()) {
            throw new IllegalStateException("Cannot calculate scores for incomplete assessment");
        }
        List<AQQuestion> questions = assessment.getQuestions();
        List<Integer> responses = assessment.getUserResponses();

        int socialSkills = 0;
        int attentionSwitching = 0;
        int attentionToDetail = 0;
        int communication = 0;
        int imagination = 0;

        for (int i = 0; i < questions.size(); i++) {
            AQQuestion q = questions.get(i);
            int score = q.getScoreForChoice(responses.get(i) - 1);
            int id = q.getQuestionId();

            // Social Skills subscale (10 items)
            if (id == 1 || id == 11 || id == 13 || id == 15 ||
                id == 22 || id == 36 || id == 44 || id == 45 ||
                id == 47 || id == 48) {
                socialSkills += score;
            }
            // Attention Switching subscale (10 items)
            else if (id == 2 || id == 4 || id == 10 || id == 16 ||
                     id == 25 || id == 32 || id == 34 || id == 37 ||
                     id == 43 || id == 46) {
                attentionSwitching += score;
            }
            // Attention to Detail subscale (10 items)
            else if (id == 5 || id == 6 || id == 9 || id == 12 ||
                     id == 19 || id == 23 || id == 28 || id == 29 ||
                     id == 30 || id == 49) {
                attentionToDetail += score;
            }
            // Communication subscale (10 items)
            else if (id == 7 || id == 17 || id == 18 || id == 26 ||
                     id == 27 || id == 31 || id == 33 || id == 35 ||
                     id == 38 || id == 39) {
                communication += score;
            }
            // Imagination subscale (10 items)
            else if (id == 3 || id == 8 || id == 14 || id == 20 ||
                     id == 21 || id == 24 || id == 40 || id == 41 ||
                     id == 42 || id == 50) {
                imagination += score;
            }
        }

        return new CategoryScores(socialSkills, attentionSwitching, attentionToDetail, communication, imagination);
    }

    public static class CategoryScores {
        private final int socialSkillsScore;
        private final int attentionSwitchingScore;
        private final int attentionToDetailScore;
        private final int communicationScore;
        private final int imaginationScore;

        public CategoryScores(int socialSkillsScore, int attentionSwitchingScore, int attentionToDetailScore,
                              int communicationScore, int imaginationScore) {
            this.socialSkillsScore = socialSkillsScore;
            this.attentionSwitchingScore = attentionSwitchingScore;
            this.attentionToDetailScore = attentionToDetailScore;
            this.communicationScore = communicationScore;
            this.imaginationScore = imaginationScore;
        }

        public int getSocialSkillsScore() { return socialSkillsScore; }
        public int getAttentionSwitchingScore() { return attentionSwitchingScore; }
        public int getAttentionToDetailScore() { return attentionToDetailScore; }
        public int getCommunicationScore() { return communicationScore; }
        public int getImaginationScore() { return imaginationScore; }
        public int getTotalScore() { return socialSkillsScore + attentionSwitchingScore + attentionToDetailScore + communicationScore + imaginationScore; }
    }
}

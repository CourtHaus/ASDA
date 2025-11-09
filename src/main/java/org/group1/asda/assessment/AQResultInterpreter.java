package org.group1.asda.assessment;

/**
 * Interprets AQ assessment results and provides feedback strings.
 */
public class AQResultInterpreter {
    // AQ thresholds (0..50)
    private static final int LOW_THRESHOLD = 5;
    private static final int MODERATE_THRESHOLD = 15;
    private static final int HIGH_THRESHOLD = 25;
    private static final int VERY_HIGH_THRESHOLD = 32;

    public static AssessmentSummary interpret(int totalScore, AQScoreCalculator.CategoryScores cat) {
        if (totalScore < 0 || totalScore > 50) {
            throw new IllegalArgumentException("Invalid total score: " + totalScore);
        }
        String overall = overallInterpretation(totalScore);
        String recommendation = recommendation(totalScore);
        String risk = riskLevel(totalScore);
        return new AssessmentSummary(totalScore, overall, recommendation, risk, cat);
    }

    private static String overallInterpretation(int totalScore) {
        if (totalScore <= LOW_THRESHOLD) {
            return "Your AQ score suggests minimal autism spectrum traits. Most of your responses indicate typical patterns in social communication, attention, and behavioral preferences.";
        } else if (totalScore <= MODERATE_THRESHOLD) {
            return "Your AQ score suggests some autism spectrum traits are present. You may experience certain challenges in social situations or have specific preferences in attention and communication patterns.";
        } else if (totalScore <= HIGH_THRESHOLD) {
            return "Your AQ score suggests moderate autism spectrum traits. You may experience more significant challenges in social communication and have distinct patterns in attention and behavioral preferences.";
        } else if (totalScore <= VERY_HIGH_THRESHOLD) {
            return "Your AQ score suggests considerable autism spectrum traits. You may experience notable challenges in social communication and have strong patterns consistent with autism spectrum differences.";
        } else {
            return "Your AQ score suggests many autism spectrum traits. Your responses indicate significant patterns consistent with autism spectrum differences across multiple domains.";
        }
    }

    private static String recommendation(int totalScore) {
        if (totalScore <= LOW_THRESHOLD) {
            return "Your AQ score is in the typical range. No specific action is indicated based on this screening. If you have ongoing concerns about social communication or behavioral patterns, consider discussing them with a healthcare professional.";
        } else if (totalScore <= MODERATE_THRESHOLD) {
            return "Your AQ score suggests some autism spectrum traits. Consider discussing these results with a healthcare professional, especially if you experience challenges in daily life related to social communication or attention patterns. They can provide more detailed assessment and support strategies.";
        } else if (totalScore <= HIGH_THRESHOLD) {
            return "Your AQ score suggests moderate autism spectrum traits. We recommend consulting with a qualified healthcare professional for a comprehensive evaluation. A formal assessment can help identify specific support strategies and accommodations that may be beneficial.";
        } else if (totalScore <= VERY_HIGH_THRESHOLD) {
            return "Your AQ score suggests considerable autism spectrum traits. We strongly recommend seeking a comprehensive evaluation from a qualified healthcare professional specializing in autism spectrum conditions.";
        } else {
            return "Your AQ score suggests significant autism spectrum traits. We strongly recommend seeking a comprehensive evaluation from a qualified healthcare professional specializing in autism spectrum conditions. Early identification and appropriate support can significantly improve quality of life and daily functioning.";
        }
    }

    private static String riskLevel(int totalScore) {
        if (totalScore <= LOW_THRESHOLD) return "Minimal";
        if (totalScore <= MODERATE_THRESHOLD) return "Low-Moderate";
        if (totalScore <= HIGH_THRESHOLD) return "Moderate";
        if (totalScore <= VERY_HIGH_THRESHOLD) return "High";
        return "Very High";
    }

    public static String interpretCategories(AQScoreCalculator.CategoryScores c) {
        StringBuilder sb = new StringBuilder();
        int s = c.getSocialSkillsScore();
        sb.append("Social Skills (Score: ").append(s).append("/10): ");
        if (s <= 2) sb.append("Typical social skills and interpersonal abilities");
        else if (s <= 5) sb.append("Some challenges in social skills and social interactions");
        else if (s <= 7) sb.append("Notable difficulties in social skills and social understanding");
        else sb.append("Significant challenges in social skills and social interactions");
        sb.append("\n\n");

        int as = c.getAttentionSwitchingScore();
        sb.append("Attention Switching (Score: ").append(as).append("/10): ");
        if (as <= 2) sb.append("Typical flexibility and attention switching abilities");
        else if (as <= 5) sb.append("Some difficulties with attention switching and flexibility");
        else if (as <= 7) sb.append("Notable challenges with attention switching and adapting to change");
        else sb.append("Significant difficulties with attention switching and cognitive flexibility");
        sb.append("\n\n");

        int d = c.getAttentionToDetailScore();
        sb.append("Attention to Detail (Score: ").append(d).append("/10): ");
        if (d <= 2) sb.append("Typical attention to detail and pattern recognition");
        else if (d <= 5) sb.append("Enhanced attention to detail and pattern recognition");
        else if (d <= 7) sb.append("Strong attention to detail and exceptional pattern recognition");
        else sb.append("Exceptional attention to detail and highly focused pattern recognition");
        sb.append("\n\n");

        int cmm = c.getCommunicationScore();
        sb.append("Communication (Score: ").append(cmm).append("/10): ");
        if (cmm <= 2) sb.append("Typical communication patterns and social conversation skills");
        else if (cmm <= 5) sb.append("Some differences in communication style and social conversation");
        else if (cmm <= 7) sb.append("Notable challenges in communication and social conversation");
        else sb.append("Significant differences in communication style and social interaction");
        sb.append("\n\n");

        int im = c.getImaginationScore();
        sb.append("Imagination (Score: ").append(im).append("/10): ");
        if (im <= 2) sb.append("Typical imagination and creative thinking abilities");
        else if (im <= 5) sb.append("Some differences in imaginative and creative thinking");
        else if (im <= 7) sb.append("Notable differences in imagination and creative expression");
        else sb.append("Significant differences in imaginative thinking and creative expression");

        return sb.toString();
    }

    public static class AssessmentSummary {
        public final int totalScore;
        public final String overallInterpretation;
        public final String recommendation;
        public final String riskLevel;
        public final AQScoreCalculator.CategoryScores categoryScores;

        public AssessmentSummary(int totalScore, String overallInterpretation, String recommendation, String riskLevel,
                                 AQScoreCalculator.CategoryScores categoryScores) {
            this.totalScore = totalScore;
            this.overallInterpretation = overallInterpretation;
            this.recommendation = recommendation;
            this.riskLevel = riskLevel;
            this.categoryScores = categoryScores;
        }
    }
}

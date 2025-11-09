package org.group1.asda.ui.results;

import org.group1.asda.assessment.AQResultInterpreter;
import org.group1.asda.assessment.AQScoreCalculator;

/**
 * Simple in-memory holder for the latest AQ results computed by the GUI questionnaire flow.
 * This avoids coupling the AQ UI to the legacy DB schema. Results persist only for the app run.
 */
public final class AqUiState {
    private static AQResultInterpreter.AssessmentSummary latestSummary;
    private static AQScoreCalculator.CategoryScores latestCategories;

    private AqUiState() {}

    public static void set(AQResultInterpreter.AssessmentSummary summary,
                           AQScoreCalculator.CategoryScores categories) {
        latestSummary = summary;
        latestCategories = categories;
    }

    public static boolean hasResult() {
        return latestSummary != null && latestCategories != null;
    }

    public static AQResultInterpreter.AssessmentSummary getSummary() {
        return latestSummary;
    }

    public static AQScoreCalculator.CategoryScores getCategories() {
        return latestCategories;
    }

    public static void clear() {
        latestSummary = null;
        latestCategories = null;
    }
}

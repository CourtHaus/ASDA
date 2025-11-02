package org.group1.asda.ui.results;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.group1.asda.assessment.AQResultInterpreter;
import org.group1.asda.assessment.AQScoreCalculator;
import org.group1.asda.navigation.Router;
import org.group1.asda.persistence.SessionDao;
import org.group1.asda.service.AssessmentService;

import java.util.Optional;

public class ResultsController {
    @FXML private Label summaryLabel;
    @FXML private Button homeBtn;

    private final SessionDao sessionDao = new SessionDao();
    private final AssessmentService assessmentService = new AssessmentService();

    @FXML
    public void initialize() {
        String text = buildSummaryText();
        summaryLabel.setText(text);
    }

    private String buildSummaryText() {
        // Prefer AQ GUI results if present
        if (AqUiState.hasResult()) {
            AQResultInterpreter.AssessmentSummary summary = AqUiState.getSummary();
            AQScoreCalculator.CategoryScores c = AqUiState.getCategories();
            StringBuilder sb = new StringBuilder();
            sb.append("ASSESSMENT RESULTS\n\n");
            sb.append("AQ SCORE SUMMARY:\n");
            sb.append("Total AQ Score: ").append(summary.totalScore).append("/50\n");
            sb.append("Autism Spectrum Trait Level: ").append(summary.riskLevel).append("\n\n");

            sb.append("REFERENCE INFORMATION:\n");
            sb.append("- Most neurotypical individuals score 0-15\n");
            sb.append("- Scores of 26+ may indicate autism spectrum traits\n");
            sb.append("- Scores of 32+ are strongly associated with autism spectrum conditions\n\n");

            sb.append("AQ SUBSCALE BREAKDOWN:\n");
            sb.append("Social Skills: ").append(c.getSocialSkillsScore()).append("/10\n");
            sb.append("Attention Switching: ").append(c.getAttentionSwitchingScore()).append("/10\n");
            sb.append("Attention to Detail: ").append(c.getAttentionToDetailScore()).append("/10\n");
            sb.append("Communication: ").append(c.getCommunicationScore()).append("/10\n");
            sb.append("Imagination: ").append(c.getImaginationScore()).append("/10\n\n");

            sb.append("DETAILED SUBSCALE ANALYSIS:\n");
            sb.append(AQResultInterpreter.interpretCategories(c)).append("\n\n");

            sb.append("INTERPRETATION:\n");
            sb.append(summary.overallInterpretation).append("\n\n");

            sb.append("RECOMMENDATIONS:\n");
            sb.append(summary.recommendation).append("\n\n");

            sb.append("IMPORTANT REMINDER:\n");
            sb.append("This screening tool is not a substitute for professional diagnosis. If you have concerns about autism spectrum differences, please consult with a qualified healthcare professional for a comprehensive evaluation.\n");
            sb.append("Thank you for completing the assessment.");
            return sb.toString();
        }

        // Fallback to legacy DB-based summary if no AQ result in memory
        Optional<String> latestOpt = sessionDao.getLatestSessionId();
        if (latestOpt.isEmpty()) {
            return "No recent session found. Please complete the questionnaire first.";
        }
        String sessionId = latestOpt.get();
        AssessmentService.AssessmentResult res = assessmentService.assessFromDb(sessionId);
        if (res.questionCount() == 0) {
            return "No answers recorded for the latest session. Please complete the questionnaire.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Your Results\n");
        sb.append("Total score: ").append(res.totalScore())
          .append(" (across ").append(res.questionCount()).append(" items)\n");
        sb.append("Level: ").append(res.level()).append("\n\n");
        sb.append("Category breakdown:\n");
        res.categoryScores().forEach((cat, val) -> sb.append(" • ").append(cat).append(": ").append(val).append('\n'));
        sb.append('\n');
        sb.append("Important: This app is for informational purposes only and does not provide a medical diagnosis. ")
          .append("If you have concerns, please consult a qualified professional.");
        return sb.toString();
    }

    @FXML
    public void onHome() {
        Router.getInstance().goTo("home");
    }
}

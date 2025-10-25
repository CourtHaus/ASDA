package org.group1.asda.ui.results;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

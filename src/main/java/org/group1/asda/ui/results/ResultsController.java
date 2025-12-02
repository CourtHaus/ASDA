package org.group1.asda.ui.results;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import org.group1.asda.assessment.AQResultInterpreter;
import org.group1.asda.assessment.AQScoreCalculator;
import org.group1.asda.navigation.Router;
import org.group1.asda.persistence.SessionDao;
import org.group1.asda.service.AssessmentService;

import java.util.Optional;

public class ResultsController {
    // Summary section
    @FXML private Label totalScoreLabel;
    @FXML private Label traitLevelLabel;

    // Subscale progress bars and scores
    @FXML private ProgressBar socialSkillsProgress;
    @FXML private Label socialSkillsScore;
    @FXML private ProgressBar attentionSwitchingProgress;
    @FXML private Label attentionSwitchingScore;
    @FXML private ProgressBar attentionToDetailProgress;
    @FXML private Label attentionToDetailScore;
    @FXML private ProgressBar communicationProgress;
    @FXML private Label communicationScore;
    @FXML private ProgressBar imaginationProgress;
    @FXML private Label imaginationScore;

    // Detailed analysis section
    @FXML private VBox detailedAnalysisSection;

    // Interpretation and recommendations
    @FXML private Label interpretationLabel;
    @FXML private Label recommendationLabel;

    // Navigation
    @FXML private Button homeBtn;

    private final SessionDao sessionDao = new SessionDao();
    private final AssessmentService assessmentService = new AssessmentService();

    @FXML
    public void initialize() {
        populateResults();
    }

    private void populateResults() {
        // Prefer AQ GUI results if present
        if (AqUiState.hasResult()) {
            AQResultInterpreter.AssessmentSummary summary = AqUiState.getSummary();
            AQScoreCalculator.CategoryScores c = AqUiState.getCategories();

            // Set total score
            totalScoreLabel.setText(summary.totalScore + "/50");

            // Set trait level with appropriate styling
            traitLevelLabel.setText(summary.riskLevel);
            updateTraitLevelStyle(summary.riskLevel);

            // Set subscale progress bars and scores
            setSubscaleData(socialSkillsProgress, socialSkillsScore, c.getSocialSkillsScore(), "#7FA8C2");
            setSubscaleData(attentionSwitchingProgress, attentionSwitchingScore, c.getAttentionSwitchingScore(), "#A3C7A3");
            setSubscaleData(attentionToDetailProgress, attentionToDetailScore, c.getAttentionToDetailScore(), "#C4A3C4");
            setSubscaleData(communicationProgress, communicationScore, c.getCommunicationScore(), "#C7B299");
            setSubscaleData(imaginationProgress, imaginationScore, c.getImaginationScore(), "#99B8C7");

            // Populate detailed analysis
            populateDetailedAnalysis(c);

            // Set interpretation and recommendations
            interpretationLabel.setText(summary.overallInterpretation);
            recommendationLabel.setText(summary.recommendation);
        } else {
            // Fallback to legacy DB-based summary if no AQ result in memory
            Optional<String> latestOpt = sessionDao.getLatestSessionId();
            if (latestOpt.isEmpty()) {
                showNoDataState("No recent session found. Please complete the questionnaire first.");
                return;
            }
            String sessionId = latestOpt.get();
            AssessmentService.AssessmentResult res = assessmentService.assessFromDb(sessionId);
            if (res.questionCount() == 0) {
                showNoDataState("No answers recorded for the latest session. Please complete the questionnaire.");
                return;
            }

            // Show legacy data in available fields
            totalScoreLabel.setText(res.totalScore() + "/" + (res.questionCount()));
            traitLevelLabel.setText(res.level());
            updateTraitLevelStyle(res.level());

            interpretationLabel.setText("Total score: " + res.totalScore() + " across " + res.questionCount() + " items.");

            StringBuilder catBreakdown = new StringBuilder("Category breakdown:\n");
            res.categoryScores().forEach((cat, val) -> catBreakdown.append(" • ").append(cat).append(": ").append(val).append('\n'));
            recommendationLabel.setText(catBreakdown.toString() + "\nImportant: This app is for informational purposes only and does not provide a medical diagnosis. If you have concerns, please consult a qualified professional.");
        }
    }

    private void setSubscaleData(ProgressBar progressBar, Label scoreLabel, int score, String color) {
        double progress = score / 10.0;
        progressBar.setProgress(progress);
        progressBar.setStyle("-fx-accent: " + color + ";");
        scoreLabel.setText(score + "/10");
    }

    private void updateTraitLevelStyle(String level) {
        // Remove existing trait level style classes
        traitLevelLabel.getStyleClass().removeAll("card-trait-high", "card-trait-moderate", "card-trait-low");

        // Add appropriate style class based on level
        String lowerLevel = level.toLowerCase();
        if (lowerLevel.contains("very high") || lowerLevel.contains("high")) {
            traitLevelLabel.getStyleClass().add("card-trait-high");
        } else if (lowerLevel.contains("moderate")) {
            traitLevelLabel.getStyleClass().add("card-trait-moderate");
        } else {
            traitLevelLabel.getStyleClass().add("card-trait-low");
        }
    }

    private void populateDetailedAnalysis(AQScoreCalculator.CategoryScores c) {
        if (detailedAnalysisSection == null) return;

        // Clear any existing dynamic content (keep the title)
        detailedAnalysisSection.getChildren().removeIf(node ->
            node.getStyleClass().contains("detailed-item"));

        // Add detailed analysis items
        addDetailedItem("Social Skills", c.getSocialSkillsScore(), getDetailedDescription("Social Skills", c.getSocialSkillsScore()));
        addDetailedItem("Attention Switching", c.getAttentionSwitchingScore(), getDetailedDescription("Attention Switching", c.getAttentionSwitchingScore()));
        addDetailedItem("Attention to Detail", c.getAttentionToDetailScore(), getDetailedDescription("Attention to Detail", c.getAttentionToDetailScore()));
        addDetailedItem("Communication", c.getCommunicationScore(), getDetailedDescription("Communication", c.getCommunicationScore()));
        addDetailedItem("Imagination", c.getImaginationScore(), getDetailedDescription("Imagination", c.getImaginationScore()));
    }

    private void addDetailedItem(String category, int score, String description) {
        VBox itemBox = new VBox();
        itemBox.getStyleClass().add("detailed-item");
        itemBox.setSpacing(4);

        Label titleLabel = new Label("• " + category + " (Score: " + score + "/10)");
        titleLabel.getStyleClass().add("detailed-item-title");

        Label descLabel = new Label(description);
        descLabel.getStyleClass().add("detailed-item-text");
        descLabel.setWrapText(true);

        itemBox.getChildren().addAll(titleLabel, descLabel);
        detailedAnalysisSection.getChildren().add(itemBox);
    }

    private String getDetailedDescription(String category, int score) {
        return switch (category) {
            case "Social Skills" -> {
                if (score <= 2) yield "Typical social skills and interpersonal abilities";
                else if (score <= 5) yield "Some challenges in social skills and social interactions";
                else if (score <= 7) yield "Notable difficulties in social skills and social understanding";
                else yield "Significant challenges in social skills and social interactions";
            }
            case "Attention Switching" -> {
                if (score <= 2) yield "Typical flexibility and attention switching abilities";
                else if (score <= 5) yield "Some difficulties with attention switching and flexibility";
                else if (score <= 7) yield "Notable challenges with attention switching and adapting to change";
                else yield "Significant difficulties with attention switching and cognitive flexibility";
            }
            case "Attention to Detail" -> {
                if (score <= 2) yield "Typical attention to detail and pattern recognition";
                else if (score <= 5) yield "Enhanced attention to detail and pattern recognition";
                else if (score <= 7) yield "Strong attention to detail and exceptional pattern recognition";
                else yield "Exceptional attention to detail and highly focused pattern recognition";
            }
            case "Communication" -> {
                if (score <= 2) yield "Typical communication patterns and social conversation skills";
                else if (score <= 5) yield "Some differences in communication style and social conversation";
                else if (score <= 7) yield "Notable challenges in communication and social conversation";
                else yield "Significant differences in communication style and social interaction";
            }
            case "Imagination" -> {
                if (score <= 2) yield "Typical imagination and creative thinking abilities";
                else if (score <= 5) yield "Some differences in imaginative and creative thinking";
                else if (score <= 7) yield "Notable differences in imagination and creative expression";
                else yield "Significant differences in imaginative thinking and creative expression";
            }
            default -> "No description available";
        };
    }

    private void showNoDataState(String message) {
        totalScoreLabel.setText("--/50");
        traitLevelLabel.setText("--");
        interpretationLabel.setText(message);
        recommendationLabel.setText("Please complete the questionnaire to see your results.");
    }

    @FXML
    public void onHome() {
        Router.getInstance().goTo("home");
    }
}

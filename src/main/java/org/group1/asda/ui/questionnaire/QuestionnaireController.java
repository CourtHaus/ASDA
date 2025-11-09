package org.group1.asda.ui.questionnaire;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.input.MouseEvent;
import org.group1.asda.assessment.AQAssessment;
import org.group1.asda.assessment.AQQuestion;
import org.group1.asda.assessment.AQResultInterpreter;
import org.group1.asda.assessment.AQScoreCalculator;
import org.group1.asda.navigation.Router;
import org.group1.asda.ui.results.AqUiState;

public class QuestionnaireController {
    @FXML private Label questionLabel;
    @FXML private Label questionNumberLabel;
    @FXML private Label counterLabel;
    @FXML private Label prevArrow;
    @FXML private Label nextArrow;
    @FXML private HBox progressDotsContainer;
    @FXML private ToggleGroup answersGroup;
    @FXML private RadioButton opt1;
    @FXML private RadioButton opt2;
    @FXML private RadioButton opt3;
    @FXML private RadioButton opt4;
    @FXML private Button backBtn;
    @FXML private Button nextBtn;

    private final AQAssessment assessment = new AQAssessment();
    private int idx = 0;
    private static final int DOTS_TO_SHOW = 6; // Show 6 progress dots

    @FXML
    public void initialize() {
        if (answersGroup == null) {
            answersGroup = new ToggleGroup();
        }
        opt1.setToggleGroup(answersGroup);
        opt2.setToggleGroup(answersGroup);
        opt3.setToggleGroup(answersGroup);
        opt4.setToggleGroup(answersGroup);

        // Set option text
        opt1.setText("Definitely Agree");
        opt2.setText("Slightly Agree");
        opt3.setText("Slightly Disagree");
        opt4.setText("Definitely Disagree");

        // Setup progress dots
        createProgressDots();

        // Setup arrow navigation
        setupArrowNavigation();

        showCurrent();
    }

    private void createProgressDots() {
        progressDotsContainer.getChildren().clear();

        for (int i = 0; i < DOTS_TO_SHOW; i++) {
            Region dot = new Region();
            dot.getStyleClass().add("progress-dot");
            dot.setMinSize(12, 12);
            dot.setMaxSize(12, 12);

            final int dotIndex = i;
            dot.setOnMouseClicked(e -> navigateToDot(dotIndex));

            progressDotsContainer.getChildren().add(dot);
        }
    }

    private void setupArrowNavigation() {
        if (prevArrow != null) {
            prevArrow.setOnMouseClicked(e -> {
                if (idx > 0) {
                    idx--;
                    showCurrent();
                }
            });
        }

        if (nextArrow != null) {
            nextArrow.setOnMouseClicked(e -> {
                if (idx < assessment.getTotalQuestions() - 1) {
                    idx++;
                    showCurrent();
                }
            });
        }
    }

    private void navigateToDot(int dotIndex) {
        // Calculate actual question index based on current position
        int startIndex = Math.max(0, idx - (DOTS_TO_SHOW / 2));
        int targetIndex = startIndex + dotIndex;

        if (targetIndex >= 0 && targetIndex < assessment.getTotalQuestions()) {
            idx = targetIndex;
            showCurrent();
        }
    }

    private void updateProgressDots() {
        if (progressDotsContainer == null) return;

        int totalQuestions = assessment.getTotalQuestions();
        int startIndex = Math.max(0, idx - (DOTS_TO_SHOW / 2));

        for (int i = 0; i < progressDotsContainer.getChildren().size(); i++) {
            Region dot = (Region) progressDotsContainer.getChildren().get(i);
            int questionIndex = startIndex + i;

            // Remove existing active class
            dot.getStyleClass().removeAll("active");

            // Add active class if this is the current question
            if (questionIndex == idx && questionIndex < totalQuestions) {
                dot.getStyleClass().add("active");
            }

            // Hide dots that exceed total questions
            dot.setVisible(questionIndex < totalQuestions);
        }
    }

    private void showCurrent() {
        if (assessment.getTotalQuestions() == 0) {
            questionLabel.setText("No AQ questions available.");
            counterLabel.setText("");
            nextBtn.setDisable(true);
            backBtn.setDisable(true);
            return;
        }

        AQQuestion q = assessment.getQuestions().get(idx);
        questionLabel.setText(q.getQuestionText());
        questionNumberLabel.setText("Question " + (idx + 1) + ":");
        counterLabel.setText("Question " + (idx + 1) + " of " + assessment.getTotalQuestions());

        // Restore previous selection if present
        if (assessment.getUserResponses().size() > idx) {
            Integer saved = assessment.getUserResponses().get(idx);
            if (saved == null || saved == 0) {
                answersGroup.selectToggle(null);
            } else {
                switch (saved) {
                    case 1 -> answersGroup.selectToggle(opt1);
                    case 2 -> answersGroup.selectToggle(opt2);
                    case 3 -> answersGroup.selectToggle(opt3);
                    case 4 -> answersGroup.selectToggle(opt4);
                    default -> answersGroup.selectToggle(null);
                }
            }
        } else {
            answersGroup.selectToggle(null);
        }

        // Update button states
        backBtn.setDisable(idx == 0);
        nextBtn.setText(idx == assessment.getTotalQuestions() - 1 ? "Finish" : "Next →");

        // Update progress dots
        updateProgressDots();
    }

    @FXML
    public void onBack() {
        if (idx > 0) {
            idx--;
            showCurrent();
        }
    }

    @FXML
    public void onNext() {
        // Validate selection
        int answer = currentSelectedValue();
        if (answer == 0) {
            Alert a = new Alert(Alert.AlertType.INFORMATION,
                    "Please select an answer before continuing.",
                    ButtonType.OK);
            a.setHeaderText(null);
            a.showAndWait();
            return;
        }

        // Record response for current question
        assessment.recordResponse(idx, answer);

        if (idx < assessment.getTotalQuestions() - 1) {
            idx++;
            showCurrent();
        } else {
            // Finish: compute AQ results and go to results screen
            assessment.completeAssessment();
            AQScoreCalculator.CategoryScores cat = AQScoreCalculator.calculateCategoryScores(assessment);
            int total = AQScoreCalculator.calculateTotalScore(assessment);
            AQResultInterpreter.AssessmentSummary summary = AQResultInterpreter.interpret(total, cat);
            AqUiState.set(summary, cat);
            Router.getInstance().goTo("results");
        }
    }

    private int currentSelectedValue() {
        Toggle sel = answersGroup.getSelectedToggle();
        if (sel == null) return 0;
        if (sel == opt1) return 1;
        if (sel == opt2) return 2;
        if (sel == opt3) return 3;
        if (sel == opt4) return 4;
        return 0;
    }
}
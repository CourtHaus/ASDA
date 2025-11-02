package org.group1.asda.ui.questionnaire;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.group1.asda.assessment.AQAssessment;
import org.group1.asda.assessment.AQQuestion;
import org.group1.asda.assessment.AQResultInterpreter;
import org.group1.asda.assessment.AQScoreCalculator;
import org.group1.asda.navigation.Router;
import org.group1.asda.ui.results.AqUiState;

import java.util.Locale;

public class QuestionnaireController {
    @FXML private Label questionLabel;
    @FXML private Label counterLabel;
    @FXML private ToggleGroup answersGroup;
    @FXML private RadioButton opt1;
    @FXML private RadioButton opt2;
    @FXML private RadioButton opt3;
    @FXML private RadioButton opt4;
    @FXML private Button backBtn;
    @FXML private Button nextBtn;

    private final AQAssessment assessment = new AQAssessment();
    private int idx = 0;

    @FXML
    public void initialize() {
        if (answersGroup == null) {
            answersGroup = new ToggleGroup();
        }
        opt1.setToggleGroup(answersGroup);
        opt2.setToggleGroup(answersGroup);
        opt3.setToggleGroup(answersGroup);
        opt4.setToggleGroup(answersGroup);
        // AQ option labels (verbatim)
        opt1.setText("Definitely Agree");
        opt2.setText("Slightly Agree");
        opt3.setText("Slightly Disagree");
        opt4.setText("Definitely Disagree");

        showCurrent();
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
        counterLabel.setText((idx + 1) + "/" + assessment.getTotalQuestions());

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

        backBtn.setDisable(idx == 0);
        nextBtn.setText(idx == assessment.getTotalQuestions() - 1 ? "Finish" : "Next");
    }

    @FXML
    public void onBack() {
        if (idx > 0) { idx--; showCurrent(); }
    }

    @FXML
    public void onNext() {
        // Validate selection
        int answer = currentSelectedValue();
        if (answer == 0) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Please select an answer before continuing.", ButtonType.OK);
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

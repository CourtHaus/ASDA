package org.group1.asda.ui.questionnaire;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.group1.asda.domain.Question;
import org.group1.asda.navigation.Router;
import org.group1.asda.persistence.QuestionDao;
import org.group1.asda.persistence.ResponseDao;
import org.group1.asda.persistence.SessionDao;
import org.group1.asda.service.AssessmentService;

import java.util.*;

public class QuestionnaireController {
    @FXML private Label questionLabel;
    @FXML private Label counterLabel;
    @FXML private ToggleGroup answersGroup;
    @FXML private RadioButton opt1;
    @FXML private RadioButton opt2;
    @FXML private RadioButton opt3;
    @FXML private Button backBtn;
    @FXML private Button nextBtn;

    private final List<Question> questions = new ArrayList<>();
    private int idx = 0;

    // persistence helpers
    private final SessionDao sessionDao = new SessionDao();
    private final ResponseDao responseDao = new ResponseDao();
    private String sessionId;

    // assessment
    private final AssessmentService assessmentService = new AssessmentService();

    // keep selections in-memory so Back/Next restores state
    // key = question code (e.g., Q80), value = 1/2/3
    private final Map<String, Integer> selectionByCode = new HashMap<>();

    @FXML
    public void initialize() {
        questions.addAll(new QuestionDao().findAll());
        if (answersGroup == null) {
            answersGroup = new ToggleGroup();
        }
        opt1.setToggleGroup(answersGroup);
        opt2.setToggleGroup(answersGroup);
        opt3.setToggleGroup(answersGroup);
        opt1.setText("1 – Very True");
        opt2.setText("2 – True");
        opt3.setText("3 – False");

        // start a new session when entering the questionnaire
        sessionId = sessionDao.startSession();

        showCurrent();
    }

    private void showCurrent() {
        if (questions.isEmpty()) {
            questionLabel.setText("No questions available. Seed may have failed.");
            counterLabel.setText("");
            nextBtn.setDisable(true);
            backBtn.setDisable(true);
            return;
        }
        Question q = questions.get(idx);
        questionLabel.setText(q.text());
        counterLabel.setText((idx + 1) + "/" + questions.size());

        // Restore previous selection if present
        Integer saved = selectionByCode.get(q.code());
        if (saved == null) {
            answersGroup.selectToggle(null);
        } else {
            switch (saved) {
                case 1 -> answersGroup.selectToggle(opt1);
                case 2 -> answersGroup.selectToggle(opt2);
                case 3 -> answersGroup.selectToggle(opt3);
                default -> answersGroup.selectToggle(null);
            }
        }

        backBtn.setDisable(idx == 0);
        nextBtn.setText(idx == questions.size() - 1 ? "Finish" : "Next");
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

        // Persist on Next as requested
        Question q = questions.get(idx);
        selectionByCode.put(q.code(), answer);
        int score = answer; // store as-is for now; can remap later if needed
        responseDao.saveOrReplace(sessionId, q.code(), answer, score);

        if (idx < questions.size() - 1) {
            idx++;
            showCurrent();
        } else {
            // Finish: compute assessment, persist to session, then go to results
            AssessmentService.AssessmentResult result = assessmentService.assessFromDb(sessionId);
            sessionDao.endSession(sessionId, result.totalScore(), result.level());
            Router.getInstance().goTo("results");
        }
    }

    private int currentSelectedValue() {
        Toggle sel = answersGroup.getSelectedToggle();
        if (sel == null) return 0;
        if (sel == opt1) return 1;
        if (sel == opt2) return 2;
        if (sel == opt3) return 3;
        return 0;
    }
}

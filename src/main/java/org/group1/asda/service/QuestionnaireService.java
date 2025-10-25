package org.group1.asda.service;

import org.group1.asda.domain.Question;
import org.group1.asda.persistence.Database;
import org.group1.asda.persistence.QuestionDao;
import org.group1.asda.persistence.ResponseDao;
import org.group1.asda.persistence.SessionDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Backend façade for the questionnaire flow.
 * Keeps controllers thin and enables easy unit testing.
 */
public class QuestionnaireService {

    public record ScaleOption(int value, String label) {}

    public record QuestionPage(
            int index,
            int total,
            Question question,
            List<ScaleOption> scale,
            Optional<Integer> selectedValue
    ) {}

    private final QuestionDao questionDao = new QuestionDao();
    private final SessionDao sessionDao = new SessionDao();
    private final ResponseDao responseDao = new ResponseDao();
    private final AssessmentService assessmentService = new AssessmentService();

    private final List<Question> cachedQuestions;

    public QuestionnaireService() {
        this.cachedQuestions = questionDao.findAll();
    }

    public String startSession() {
        return sessionDao.startSession();
    }

    public int totalQuestions() {
        return cachedQuestions.size();
    }

    public QuestionPage get(String sessionId, int index) {
        if (index < 0 || index >= cachedQuestions.size()) {
            throw new IndexOutOfBoundsException("Question index out of range: " + index);
        }
        Question q = cachedQuestions.get(index);
        Optional<Integer> selected = responseDao.getAnswer(sessionId, q.code());
        return new QuestionPage(index, cachedQuestions.size(), q, loadScale(), selected);
    }

    public void answer(String sessionId, String questionCode, int value) {
        if (value < 1 || value > 3) throw new IllegalArgumentException("Answer must be 1,2,3");
        int score = value; // for now score maps 1:1 to value
        responseDao.saveOrReplace(sessionId, questionCode, value, score);
    }

    public AssessmentService.AssessmentResult finish(String sessionId) {
        AssessmentService.AssessmentResult result = assessmentService.assessFromDb(sessionId);
        sessionDao.endSession(sessionId, result.totalScore(), result.level());
        return result;
    }

    public Optional<String> latestSession() {
        return sessionDao.getLatestSessionId();
    }

    public List<ScaleOption> loadScale() {
        List<ScaleOption> out = new ArrayList<>();
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT value,label FROM scales ORDER BY value ASC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new ScaleOption(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load scale: " + e.getMessage(), e);
        }
        return out;
    }
}

package org.group1.asda.service;

import org.group1.asda.persistence.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Computes assessment totals and level for a questionnaire session.
 * Scoring currently treats answers as: 1 = Very True, 2 = True, 3 = False.
 * Lower totals indicate more endorsed symptom statements.
 * Bands are derived relative to the observed min/max range per session size.
 */
public class AssessmentService {

    public record AssessmentResult(int totalScore, int questionCount, Map<String, Integer> categoryScores, String level) {}

    /**
     * Computes totals and per-category scores directly from the DB for the given session id
     * and returns an {@link AssessmentResult}. No rows means a zero-result.
     */
    public AssessmentResult assessFromDb(String sessionId) {
        int total = 0;
        int count = 0;
        Map<String, Integer> byCat = new LinkedHashMap<>();

        try (Connection c = Database.getConnection()) {
            // Total and count
            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT COALESCE(SUM(score),0) AS total, COUNT(*) AS cnt FROM responses WHERE session_id = ?")) {
                ps.setString(1, sessionId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        total = rs.getInt("total");
                        count = rs.getInt("cnt");
                    }
                }
            }

            // Per-category sums (preserve insertion order for stable UI)
            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT q.category, COALESCE(SUM(r.score),0) AS s " +
                    "FROM responses r JOIN questions q ON q.code = r.question_code " +
                    "WHERE r.session_id = ? GROUP BY q.category ORDER BY q.category")) {
                ps.setString(1, sessionId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        byCat.put(rs.getString("category"), rs.getInt("s"));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to assess session: " + e.getMessage(), e);
        }

        String level = deriveLevel(total, count);
        return new AssessmentResult(total, count, byCat, level);
    }

    /**
     * Derive band labels based on total and number of answered questions.
     * Min = 1 * n, Max = 3 * n. Elevated <= 33% of range above min, Moderate <= 66%, else Low.
     */
    public String deriveLevel(int totalScore, int questionCount) {
        if (questionCount <= 0) return "Unknown";
        int min = questionCount * 1;
        int max = questionCount * 3;
        int range = max - min; // == 2 * n
        double t1 = min + range * 0.33;
        double t2 = min + range * 0.66;
        if (totalScore <= t1) return "Elevated";
        if (totalScore <= t2) return "Moderate";
        return "Low";
    }
}

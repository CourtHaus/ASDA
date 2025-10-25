package org.group1.asda.persistence;

import java.sql.*;
import java.util.Optional;

public class ResponseDao {
    /**
     * Saves or replaces a response for a question in a given session.
     * Since the schema doesn't enforce uniqueness, we manually delete any existing row first.
     */
    public void saveOrReplace(String sessionId, String questionCode, int answerValue, int score) {
        try (Connection c = Database.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement del = c.prepareStatement(
                    "DELETE FROM responses WHERE session_id = ? AND question_code = ?");
                 PreparedStatement ins = c.prepareStatement(
                    "INSERT INTO responses(session_id, question_code, answer_value, score) VALUES(?,?,?,?)")) {
                del.setString(1, sessionId);
                del.setString(2, questionCode);
                del.executeUpdate();

                ins.setString(1, sessionId);
                ins.setString(2, questionCode);
                ins.setInt(3, answerValue);
                ins.setInt(4, score);
                ins.executeUpdate();
                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save response: " + e.getMessage(), e);
        }
    }

    /**
     * Returns the previously saved answer value (1/2/3) for a question in a session, if any.
     */
    public Optional<Integer> getAnswer(String sessionId, String questionCode) {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT answer_value FROM responses WHERE session_id = ? AND question_code = ? LIMIT 1")) {
            ps.setString(1, sessionId);
            ps.setString(2, questionCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(rs.getInt(1));
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read saved answer: " + e.getMessage(), e);
        }
    }
}


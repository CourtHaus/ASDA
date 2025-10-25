package org.group1.asda.persistence;

import java.sql.*;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class SessionDao {
    /**
     * Starts a new questionnaire session and returns its generated id.
     */
    public String startSession() {
        String id = UUID.randomUUID().toString();
        long now = Instant.now().toEpochMilli();
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO sessions(id, started_at) VALUES(?,?)")) {
            ps.setString(1, id);
            ps.setLong(2, now);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to start session: " + e.getMessage(), e);
        }
        return id;
    }

    /**
     * Ends the session by setting ended_at and (optionally) total score and level.
     */
    public void endSession(String sessionId, Integer totalScore, String level) {
        long now = Instant.now().toEpochMilli();
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "UPDATE sessions SET ended_at = ?, total_score = ?, level = ? WHERE id = ?")) {
            ps.setLong(1, now);
            if (totalScore == null) ps.setNull(2, Types.INTEGER); else ps.setInt(2, totalScore);
            if (level == null) ps.setNull(3, Types.VARCHAR); else ps.setString(3, level);
            ps.setString(4, sessionId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to end session: " + e.getMessage(), e);
        }
    }

    public Optional<String> getLatestSessionId() {
        try (Connection c = Database.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT id FROM sessions ORDER BY started_at DESC LIMIT 1")) {
            if (rs.next()) return Optional.of(rs.getString(1));
            return Optional.empty();
        } catch (SQLException e) {
            return Optional.empty();
        }
    }
}

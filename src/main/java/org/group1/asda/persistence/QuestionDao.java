package org.group1.asda.persistence;

import org.group1.asda.domain.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDao {
    public List<Question> findAll() {
        List<Question> out = new ArrayList<>();
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT code, text, category FROM questions ORDER BY id ASC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new Question(
                        rs.getString("code"),
                        rs.getString("text"),
                        rs.getString("category")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load questions: " + e.getMessage(), e);
        }
        return out;
    }

    public int count() {
        try (Connection c = Database.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM questions")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            return 0;
        }
    }
}

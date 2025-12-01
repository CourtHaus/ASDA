package org.group1.asda.persistence;

import java.sql.*;
import java.util.List;
import java.util.function.Consumer;

public final class Database {
    // Default DB file in working dir; can be overridden via -Dasda.db.url=jdbc:sqlite:/path/to/asda.db
    private static final String DB_URL_DEFAULT = "jdbc:sqlite:asda.db"; // file in working dir

    private Database() {}

    private static String dbUrl() {
        return System.getProperty("asda.db.url", DB_URL_DEFAULT);
    }

    public static void init() {
        init((Consumer<Double>) null);
    }

    public static void init(Consumer<Double> progress) {
        try (Connection conn = DriverManager.getConnection(dbUrl())) {
            if (conn == null) return;
            if (progress != null) progress.accept(0.05);
            conn.setAutoCommit(false);
            createSchema(conn);
            if (progress != null) progress.accept(0.35);
            createIndexes(conn);
            if (progress != null) progress.accept(0.60);
            seedIfEmpty(conn);
            if (progress != null) progress.accept(0.90);
            conn.commit();
            if (progress != null) progress.accept(1.0);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database: " + e.getMessage(), e);
        }
    }

    private static void createSchema(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS questions (" +
                    "id INTEGER PRIMARY KEY, " +
                    "code TEXT NOT NULL UNIQUE, " +
                    "text TEXT NOT NULL, " +
                    "category TEXT NOT NULL DEFAULT 'General'" +
                    ")");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS scales (" +
                    "value INTEGER PRIMARY KEY, label TEXT NOT NULL" +
                    ")");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS sessions (" +
                    "id TEXT PRIMARY KEY, started_at INTEGER, ended_at INTEGER, total_score INTEGER, level TEXT" +
                    ")");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS responses (" +
                    "session_id TEXT NOT NULL, question_code TEXT NOT NULL, answer_value INTEGER NOT NULL, score INTEGER NOT NULL, " +
                    "FOREIGN KEY(session_id) REFERENCES sessions(id) ON DELETE CASCADE" +
                    ")");
        }
    }

    private static void createIndexes(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            // Uniqueness: one response per question per session
            st.executeUpdate("CREATE UNIQUE INDEX IF NOT EXISTS ux_responses_session_question ON responses(session_id, question_code)");
            // Helpful indexes for lookups and summaries
            st.executeUpdate("CREATE INDEX IF NOT EXISTS ix_responses_session ON responses(session_id)");
            st.executeUpdate("CREATE INDEX IF NOT EXISTS ix_responses_question ON responses(question_code)");
        }
    }

    private static void seedIfEmpty(Connection conn) throws SQLException {
        boolean questionsEmpty;
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM questions")) {
            questionsEmpty = rs.next() && rs.getInt(1) == 0;
        }
        if (questionsEmpty) {
            // Seed the 1–3 scale: 1=Very True, 2=True, 3=False
            try (PreparedStatement ps = conn.prepareStatement("INSERT OR IGNORE INTO scales(value,label) VALUES(?,?)")) {
                ps.setInt(1, 1); ps.setString(2, "Very True"); ps.addBatch();
                ps.setInt(1, 2); ps.setString(2, "True"); ps.addBatch();
                ps.setInt(1, 3); ps.setString(2, "False"); ps.addBatch();
                ps.executeBatch();
            }
            // Provided questionnaire items 80–109; using code like Q80, Q81, ...
            List<String> items = List.of(
                "Before age 2, arched back and bent head back, when held",
                "Before age 2, struggled against being held",
                "Abnormal craving for certain foods",
                "Eats unusually large amounts of food",
                "Covers ears at many sounds",
                "Only certain sounds seem painful to him",
                "Fails to blink at bright lights",
                "Skin color lighter or darker than others in family (which: lighter ___ darker )",
                "Prefers inanimate (nonliving) things",
                "Avoids people",
                "Insists on keeping certain object with him",
                "Always frightened or very anxious",
                "Inconsolable crying",
                "Notices changes or imperfections and tries to correct them",
                "Tidy (neat, avoids messy things)",
                "Has collected a particular thing (toy horses, bits of glass, etc.)",
                "After delay, repeats phrases he has heard",
                "After delay, repeats whole sentences he has heard",
                "Repeats questions or conversations he has heard, over and over, without variation",
                "Gets “hooked” or fixated on one topic (like cars, maps, death)",
                "Examines surfaces with fingers",
                "Holds bizarre pose or posture",
                "Chews or swallows nonfood objects",
                "Dislikes being touched or held",
                "Intensely aware of odors",
                "Hides skill or knowledge, so you are surprised later on",
                "Seems not to feel pain",
                "Terrified at unusual happenings",
                "Learned words useless to himself",
                "Learned certain words, then stopped using them"
            );
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO questions(code,text,category) VALUES(?,?,?)")) {
                int codeNum = 80;
                for (String text : items) {
                    ps.setString(1, "Q" + codeNum);
                    ps.setString(2, text);
                    // Assign rough categories (can be refined later)
                    String category = switch (codeNum) {
                        case 80,81 -> "BeforeAge2";
                        case 84,85,86,104 -> "Sensory";
                        case 88,89,103 -> "Social";
                        case 96,97,98,108,109 -> "Communication";
                        default -> "Behavior";
                    };
                    ps.setString(3, category);
                    ps.addBatch();
                    codeNum++;
                }
                ps.executeBatch();
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl());
    }
}

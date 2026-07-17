package database;

import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private Connection connection;
    private final String DB_URL = "jdbc:sqlite:game.db";

    // Initializes the Database and the connection

    public DatabaseManager() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            createTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Creates the following tables if and only if it does not already exist

    private void createTables() {

        String usersTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                high_score INTEGER DEFAULT 0,
                last_level INTEGER DEFAULT 0,
                bgm_enabled INTEGER DEFAULT 1,
                shot_enabled INTEGER DEFAULT 1,
                crash_enabled INTEGER DEFAULT 1,
                gameover_enabled INTEGER DEFAULT 1,
                selected_plane INTEGER DEFAULT 1
            )
            """;

        String historyTable = """
            CREATE TABLE IF NOT EXISTS game_history (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL,
                score INTEGER NOT NULL,
                last_level INTEGER NOT NULL,
                play_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                bgm_enabled INTEGER,
                shot_enabled INTEGER,
                crash_enabled INTEGER,
                gameover_enabled INTEGER,
                FOREIGN KEY (username) REFERENCES users(username)
            )
            """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(usersTable);
            stmt.execute(historyTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Insert the following user into the "users" table

    public boolean registerUser(String username, String password) {
        String sql = """
            INSERT INTO users (username, password) 
            VALUES (?, ?)
            """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    // Returns User object if and only if it already exists in the "user" table

    public User loginUser(String username, String password) {
        String sql = """
            SELECT * FROM users 
            WHERE username = ? AND password = ?
            """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User(username, password);
                user.setHighScore(rs.getInt("high_score"));
                user.setLastLevel(rs.getInt("last_level"));
                user.setBgmEnabled(rs.getInt("bgm_enabled") == 1);
                user.setShotEnabled(rs.getInt("shot_enabled") == 1);
                user.setCrashEnabled(rs.getInt("crash_enabled") == 1);
                user.setGameoverEnabled(rs.getInt("gameover_enabled") == 1);
                user.setSelectedPlane(rs.getInt("selected_plane"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Updates the following user according to the method input

    public boolean updateUser(User user) {
        String sql = """
            UPDATE users SET 
                high_score = ?, 
                last_level = ?, 
                bgm_enabled = ?, 
                shot_enabled = ?, 
                crash_enabled = ?, 
                gameover_enabled = ?, 
                selected_plane = ? 
            WHERE username = ?
            """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, user.getHighScore());
            pstmt.setInt(2, user.getLastLevel());
            pstmt.setInt(3, user.isBgmEnabled() ? 1 : 0);
            pstmt.setInt(4, user.isShotEnabled() ? 1 : 0);
            pstmt.setInt(5, user.isCrashEnabled() ? 1 : 0);
            pstmt.setInt(6, user.isGameoverEnabled() ? 1 : 0);
            pstmt.setInt(7, user.getSelectedPlane());
            pstmt.setString(8, user.getUsername());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Inserts the game history into "game_history" table

    public void saveGameHistory(String username, int score, int level,
                                boolean bgm, boolean shot, boolean crash, boolean gameover) {
        String sql = """
            INSERT INTO game_history 
                (username, score, level, bgm_enabled, shot_enabled, crash_enabled, gameover_enabled) 
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setInt(2, score);
            pstmt.setInt(3, level);
            pstmt.setInt(4, bgm ? 1 : 0);
            pstmt.setInt(5, shot ? 1 : 0);
            pstmt.setInt(6, crash ? 1 : 0);
            pstmt.setInt(7, gameover ? 1 : 0);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Returns a sorted list of high scores

    public List<String[]> getHighScores() {
        List<String[]> scores = new ArrayList<>();
        String sql = """
            SELECT 
                username, 
                MAX(score) as high_score, 
                last_level, 
                MAX(play_date) as latest_date 
            FROM game_history 
            GROUP BY username 
            ORDER BY high_score DESC 
            """;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String[] row = new String[4];
                row[0] = rs.getString("username");
                row[1] = String.valueOf(rs.getInt("high_score"));
                row[2] = String.valueOf(rs.getInt("last_level"));
                row[3] = rs.getString("latest_date");
                scores.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return scores;
    }

    // Checks if the following user exists

    public boolean usernameExists(String username) {
        String sql = """
            SELECT 1 FROM users 
            WHERE username = ?
            """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Returns the number of players

    public int getTotalPlayers() {
        String sql = """
            SELECT COUNT(*) as total FROM users
            """;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Returns the number of games played

    public int getTotalGamesPlayed() {
        String sql = """
            SELECT COUNT(*) as total FROM game_history
            """;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Returns the average score of games played

    public double getAverageScore() {
        String sql = """
            SELECT AVG(score) as avg_score FROM game_history
            """;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble("avg_score");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Closes the connection

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

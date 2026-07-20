package ui;

import main.GameMain;
import database.DatabaseManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HighScorePanel extends JPanel {

    private GameMain gameMain;
    private JTable scoreTable;
    private DefaultTableModel tableModel;
    private JLabel statsLabel;

    public HighScorePanel(GameMain gameMain) {

        this.gameMain = gameMain;
        setLayout(new BorderLayout());
        setBackground(new Color(20, 20, 50));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(20, 20, 50));

        // Title

        JLabel title = new JLabel("HIGH SCORES", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(Color.YELLOW);
        headerPanel.add(title, BorderLayout.NORTH);

        statsLabel = new JLabel("", SwingConstants.CENTER);
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        statsLabel.setForeground(Color.WHITE);
        headerPanel.add(statsLabel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        String[] columns = {"Rank", "Player", "Score", "Level", "Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        // Initialize table

        scoreTable = new JTable(tableModel);
        scoreTable.setRowHeight(30);
        scoreTable.setFont(new Font("Arial", Font.PLAIN, 14));
        scoreTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        scoreTable.setBackground(new Color(40, 40, 80));
        scoreTable.setForeground(Color.WHITE);
        scoreTable.getTableHeader().setBackground(new Color(60, 60, 120));
        scoreTable.getTableHeader().setForeground(Color.WHITE);

        // Scroll

        JScrollPane scrollPane = new JScrollPane(scoreTable);
        scrollPane.getViewport().setBackground(new Color(20, 20, 50));
        add(scrollPane, BorderLayout.CENTER);

        loadScores();

        // Add back button

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(20, 20, 50));
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> gameMain.showPanel("MainMenu"));
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Loads the high scores from database and adds them to the table

    private void loadScores() {
        tableModel.setRowCount(0);
        DatabaseManager db = gameMain.getDatabaseManager();
        List<String[]> scores = db.getHighScores();
        int rank = 1;
        for (String[] row : scores) {
            tableModel.addRow(new Object[]{rank++, row[0], row[1], row[2], row[3]});
        }

        int totalPlayers = db.getTotalPlayers();
        int totalGames = db.getTotalGamesPlayed();
        double avgScore = db.getAverageScore();
        statsLabel.setText(String.format("Total Players: %d | Total Games: %d | Average Score: %.1f",
                totalPlayers, totalGames, avgScore));
    }
}
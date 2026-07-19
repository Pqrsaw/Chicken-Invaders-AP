package ui;

import main.GameMain;
import javax.swing.*;
import java.awt.*;

public class HowToPlayPanel extends JPanel {

    private GameMain gameMain;

    public HowToPlayPanel(GameMain gameMain) {

        this.gameMain = gameMain;
        setLayout(new BorderLayout());
        setBackground(new Color(20, 20, 50));

        JLabel title = new JLabel("HOW TO PLAY", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(Color.YELLOW);
        title.setBackground(new Color(20, 20, 50));
        title.setOpaque(true);
        add(title, BorderLayout.NORTH);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(new Color(30, 30, 60));
        textArea.setForeground(Color.WHITE);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setMargin(new Insets(20, 20, 20, 20));

        // How to Play text :

        String text =
                "+-----------------------------------------------------------+\n" +
                        "|                      CONTROLS                             |\n" +
                        "+-----------------------------------------------------------+\n" +
                        "|  Arrow Keys / WASD     - Move the plane                   |\n" +
                        "|  Space                 - Shoot                            |\n" +
                        "|  P                     - Pause / Resume                   |\n" +
                        "|  ESC                   - Return to menu                   |\n" +
                        "+-----------------------------------------------------------+\n" +
                        "|                      POWER-UPS                            |\n" +
                        "+-----------------------------------------------------------+\n" +
                        "|  Add Fire (+)          - Increases number of shots (Perm.)|\n" +
                        "|  Rapid Fire (F)        - Increases fire rate (8s)         |\n" +
                        "|  Extra Life (♥)        - Restores one life (Perm.)        |\n" +
                        "|  Shield (S)            - Invincibility (10s)              |\n" +
                        "|  Freeze Bomb (❄)       - Freezes all enemies (3s)         |\n" +
                        "+-----------------------------------------------------------+\n" +
                        "|                      GAME INFO                            |\n" +
                        "+-----------------------------------------------------------+\n" +
                        "|  Defeat all enemies in each level to advance              |\n" +
                        "|  Defeat bosses at levels 4 and 8                          |\n" +
                        "|  Protect your plane from falling eggs                     |\n" +
                        "|  Collect power-ups to gain advantages                     |\n" +
                        "+-----------------------------------------------------------+\n" +
                        "|                      SCORING                              |\n" +
                        "+-----------------------------------------------------------+\n" +
                        "|  Normal Chicken        -   10 points                      |\n" +
                        "|  Fast Chicken          -   15 points                      |\n" +
                        "|  Zigzag Chicken        -   20 points                      |\n" +
                        "|  Shooter Chicken       -   25 points                      |\n" +
                        "|  Level Complete        -  200 bonus points                |\n" +
                        "|  Boss Level 4          -  500 bonus points                |\n" +
                        "|  Boss Level 8          - 1000 bonus points                |\n" +
                        "+-----------------------------------------------------------+";

        textArea.setText(text);

        // Scroll

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.getViewport().setBackground(new Color(20, 20, 50));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(20, 20, 50));

        // Add Back button

        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("SansSerif", Font.PLAIN, 16));
        backBtn.setPreferredSize(new Dimension(120, 40));
        backBtn.setBackground(new Color(60, 60, 120));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        backBtn.addActionListener(e -> gameMain.showPanel("MainMenu"));

        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
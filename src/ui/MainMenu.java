package ui;

import main.GameMain;
import javax.swing.*;
import java.awt.*;

public class MainMenu extends JPanel {

    private GameMain gameMain;

    public MainMenu(GameMain gameMain) {

        this.gameMain = gameMain;
        setLayout(new GridBagLayout());
        setBackground(new Color(10, 10, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title

        JLabel title = new JLabel("🐔 CHICKEN INVADERS 🐔");
        title.setFont(new Font("SansSerif", Font.BOLD, 48));
        title.setForeground(Color.YELLOW);
        title.setBackground(new Color(10, 10, 30));
        title.setOpaque(true);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, gbc);

        // Buttons

        String[] buttonTexts = {"New Game", "High Scores", "Settings", "How to Play", "Store", "Exit"};
        for (String text : buttonTexts) {
            gbc.gridy++;
            JButton btn = new JButton(text);
            btn.setFont(new Font("SansSerif", Font.PLAIN, 20));
            btn.setPreferredSize(new Dimension(220, 50));
            btn.setBackground(new Color(50, 50, 100));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

            switch (text) {
                case "New Game": btn.addActionListener(e -> gameMain.startGame());break;
                case "High Scores": btn.addActionListener(e -> gameMain.showPanel("HighScores")); break;
                case "Settings": btn.addActionListener(e -> gameMain.showPanel("Settings")); break;
                case "How to Play": btn.addActionListener(e -> gameMain.showPanel("HowToPlay")); break;
                case "Store": btn.addActionListener(e -> gameMain.showPanel("Store")); break;
                case "Exit": btn.addActionListener(e -> System.exit(0)); break;
            }

            add(btn, gbc);
        }
    }
}
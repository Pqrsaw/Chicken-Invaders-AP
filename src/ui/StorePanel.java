package ui;

import main.GameMain;
import javax.swing.*;
import java.awt.*;

public class StorePanel extends JPanel {

    private GameMain gameMain;

    public StorePanel(GameMain gameMain) {

        this.gameMain = gameMain;
        setLayout(new BorderLayout());
        setBackground(new Color(20, 20, 50));

        // Title

        JLabel title = new JLabel("STORE", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(Color.YELLOW);
        title.setBackground(new Color(20, 20, 50));
        title.setOpaque(true);
        add(title, BorderLayout.NORTH);

        // Coming soon!
        // Age vaght shod implement mikonim :)

        JLabel comingSoonLabel = new JLabel("Coming Soon!", SwingConstants.CENTER);
        comingSoonLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
        comingSoonLabel.setForeground(Color.WHITE);
        comingSoonLabel.setBackground(new Color(20, 20, 50));
        comingSoonLabel.setOpaque(true);
        add(comingSoonLabel, BorderLayout.CENTER);

        // Add back button

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(20, 20, 50));

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
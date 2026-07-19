package ui;

import main.GameMain;
import model.User;
import audio.SoundManager;
import database.DatabaseManager;
import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {

    private GameMain gameMain;
    private JCheckBox musicCheck;
    private JCheckBox shotCheck;
    private JCheckBox crashCheck;
    private JCheckBox gameoverCheck;

    public SettingsPanel(GameMain gameMain) {

        this.gameMain = gameMain;
        setLayout(new GridBagLayout());
        setBackground(new Color(20, 20, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;

        // Title

        JLabel title = new JLabel("Sound Settings", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setBackground(new Color(20, 20, 50));
        title.setOpaque(true);
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(title, gbc);
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // Check box #1

        gbc.gridy++;
        musicCheck = new JCheckBox("Background Music");
        musicCheck.setForeground(Color.WHITE);
        musicCheck.setBackground(new Color(20, 20, 50));
        musicCheck.setSelected(true);
        add(musicCheck, gbc);

        // Check box #2

        gbc.gridy++;
        shotCheck = new JCheckBox("Shot Sound");
        shotCheck.setForeground(Color.WHITE);
        shotCheck.setBackground(new Color(20, 20, 50));
        shotCheck.setSelected(true);
        add(shotCheck, gbc);

        // Check box #3

        gbc.gridy++;
        crashCheck = new JCheckBox("Crash / Explosion Sound");
        crashCheck.setForeground(Color.WHITE);
        crashCheck.setBackground(new Color(20, 20, 50));
        crashCheck.setSelected(true);
        add(crashCheck, gbc);

        // Check box #4

        gbc.gridy++;
        gameoverCheck = new JCheckBox("Game Over / Win Sound");
        gameoverCheck.setForeground(Color.WHITE);
        gameoverCheck.setBackground(new Color(20, 20, 50));
        gameoverCheck.setSelected(true);
        add(gameoverCheck, gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(20, 20, 50));

        // Add save button

        JButton saveBtn = new JButton("Save");
        saveBtn.setFont(new Font("SansSerif", Font.PLAIN, 16));
        saveBtn.setPreferredSize(new Dimension(120, 40));
        saveBtn.setBackground(new Color(60, 60, 120));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        saveBtn.addActionListener(e -> saveSettings());
        buttonPanel.add(saveBtn);

        // Add back button

        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("SansSerif", Font.PLAIN, 16));
        backBtn.setPreferredSize(new Dimension(120, 40));
        backBtn.setBackground(new Color(60, 60, 120));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        backBtn.addActionListener(e -> gameMain.showPanel("MainMenu"));
        buttonPanel.add(backBtn);

        add(buttonPanel, gbc);

        // Load user settings after panel is created

        loadSettings();
    }

    private void loadSettings() {

        User user = gameMain.getCurrentUser();

        // If the user is logged in, load preferences

        if (user != null) {

            musicCheck.setSelected(user.isBgmEnabled());
            shotCheck.setSelected(user.isShotEnabled());
            crashCheck.setSelected(user.isCrashEnabled());
            gameoverCheck.setSelected(user.isGameoverEnabled());

            SoundManager sm = gameMain.getSoundManager();
            sm.setMusicEnabled(user.isBgmEnabled());
            sm.setShotEnabled(user.isShotEnabled());
            sm.setExplosionEnabled(user.isCrashEnabled());
            sm.setGameOverEnabled(user.isGameoverEnabled());
        }
        else {

            // Else have all the options checked

            musicCheck.setSelected(true);
            shotCheck.setSelected(true);
            crashCheck.setSelected(true);
            gameoverCheck.setSelected(true);
        }
    }

    private void saveSettings() {

        User user = gameMain.getCurrentUser();

        // Cannot save if not logged in

        if (user == null) {
            JOptionPane.showMessageDialog(this,
                    "Please login first to save settings!",
                    "Not Logged In",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Gets the users preferences

        user.setBgmEnabled(musicCheck.isSelected());
        user.setShotEnabled(shotCheck.isSelected());
        user.setCrashEnabled(crashCheck.isSelected());
        user.setGameoverEnabled(gameoverCheck.isSelected());

        // Updates the database base on user preferences

        DatabaseManager db = gameMain.getDatabaseManager();
        if (db.updateUser(user)) {
            SoundManager sm = gameMain.getSoundManager();
            sm.setMusicEnabled(user.isBgmEnabled());
            sm.setShotEnabled(user.isShotEnabled());
            sm.setExplosionEnabled(user.isCrashEnabled());
            sm.setGameOverEnabled(user.isGameoverEnabled());

            JOptionPane.showMessageDialog(this,
                    "Settings saved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        else {
            JOptionPane.showMessageDialog(this,
                    "Failed to save settings!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
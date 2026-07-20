package ui;

import main.GameMain;
import model.User;
import database.DatabaseManager;
import audio.SoundManager;
import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {

    private GameMain gameMain;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    // Initialize Login Panel

    public LoginPanel(GameMain gameMain) {
        this.gameMain = gameMain;
        setLayout(new GridBagLayout());
        setBackground(new Color(20, 20, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        // Title

        JLabel title = new JLabel("Login");
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setBackground(new Color(20, 20, 50));
        title.setOpaque(true);
        add(title, gbc);

        // Username label

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        add(usernameLabel, gbc);

        // Username field

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        usernameField.setBackground(Color.WHITE);
        usernameField.setForeground(Color.BLACK);
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        add(usernameField, gbc);

        // Password label

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        add(passwordLabel, gbc);

        // Password field

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        passwordField.setBackground(Color.WHITE);
        passwordField.setForeground(Color.BLACK);
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        add(passwordField, gbc);

        // Message label

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        add(messageLabel, gbc);

        // Login button

        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton loginBtn = createStyledButton("Login");
        loginBtn.addActionListener(e -> attemptLogin());
        add(loginBtn, gbc);

        // Register button

        gbc.gridy++;
        JButton registerBtn = createStyledButton("Register");
        registerBtn.addActionListener(e -> gameMain.showPanel("Register"));
        add(registerBtn, gbc);

        // Back button

        gbc.gridy++;
        JButton backBtn = createStyledButton("Back");
        backBtn.addActionListener(e -> gameMain.showPanel("MainMenu"));
        add(backBtn, gbc);
    }

    // Creates buttons with the given name and set parameters

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 16));
        button.setPreferredSize(new Dimension(150, 40));
        button.setBackground(new Color(60, 60, 120));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        return button;
    }

    // Logins if and only if the particular user and password combination
    // exists within the database
    // (User is already registered)

    private void attemptLogin() {

        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // No empty fields allowed

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please fill all fields");
            messageLabel.setForeground(Color.RED);
            return;
        }

        // Checks if the user is in the database

        DatabaseManager db = gameMain.getDatabaseManager();
        User user = db.loginUser(username, password);

        if (user != null) {

            gameMain.setCurrentUser(user);
            messageLabel.setForeground(Color.GREEN);
            messageLabel.setText("Login successful!");

            // Initialize users sound preferences

            SoundManager sm = gameMain.getSoundManager();
            sm.setMusicEnabled(user.isBgmEnabled());
            sm.setShotEnabled(user.isShotEnabled());
            sm.setExplosionEnabled(user.isCrashEnabled());
            sm.setGameOverEnabled(user.isGameoverEnabled());

            Timer timer = new Timer(1000, e -> gameMain.showPanel("MainMenu"));
            timer.setRepeats(false);
            timer.start();
        }
        else {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Invalid username or password");
        }
    }
}
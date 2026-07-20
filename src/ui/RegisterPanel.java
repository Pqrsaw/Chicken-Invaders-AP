package ui;

import main.GameMain;
import database.DatabaseManager;
import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {

    private GameMain gameMain;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmField;
    private JLabel messageLabel;

    public RegisterPanel(GameMain gameMain) {
        this.gameMain = gameMain;
        setLayout(new GridBagLayout());
        setBackground(new Color(20, 20, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        // Title

        JLabel title = new JLabel("Register");
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

        // Confirm label

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel confirmLabel = new JLabel("Confirm:");
        confirmLabel.setForeground(Color.WHITE);
        confirmLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        add(confirmLabel, gbc);

        // Confirm field

        gbc.gridx = 1;
        confirmField = new JPasswordField(15);
        confirmField.setBackground(Color.WHITE);
        confirmField.setForeground(Color.BLACK);
        confirmField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        add(confirmField, gbc);

        // Message label

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        add(messageLabel, gbc);

        // Register button

        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton registerBtn = createStyledButton("Register");
        registerBtn.addActionListener(e -> attemptRegister());
        add(registerBtn, gbc);

        // Back button

        gbc.gridy++;
        JButton backBtn = createStyledButton("Back");
        backBtn.addActionListener(e -> gameMain.showPanel("Login"));
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

    // Registers the user if and only if the username given is not already in the database
    // (No duplicate usernames allowed)

    private void attemptRegister() {

        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirm = new String(confirmField.getPassword());

        // No empty fields allowed

        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            messageLabel.setText("Please fill all fields");
            messageLabel.setForeground(Color.RED);
            return;
        }

        // Checks if password and confirm fields are the same

        if (!password.equals(confirm)) {
            messageLabel.setText("Passwords do not match");
            messageLabel.setForeground(Color.RED);
            return;
        }

        // Checks if the username is duplicate

        DatabaseManager db = gameMain.getDatabaseManager();

        if (db.usernameExists(username)) {
            messageLabel.setText("Username already exists");
            messageLabel.setForeground(Color.RED);
            return;
        }

        if (db.registerUser(username, password)) {
            messageLabel.setForeground(Color.GREEN);
            messageLabel.setText("Registration successful!");
            Timer timer = new Timer(1500, e -> gameMain.showPanel("Login"));
            timer.setRepeats(false);
            timer.start();
        }
        else {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Registration failed");
        }
    }
}
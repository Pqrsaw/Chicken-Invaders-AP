package main;

import game.GamePanel;
import ui.*;
import database.DatabaseManager;
import audio.SoundManager;
import model.User;
import javax.swing.*;
import java.awt.*;

public class GameMain extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private MainMenu mainMenu;
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private HighScorePanel highScorePanel;
    private SettingsPanel settingsPanel;
    private HowToPlayPanel howToPlayPanel;
    private StorePanel storePanel;
    private GamePanel gamePanel;
    private User currentUser;
    private DatabaseManager dbManager;
    private SoundManager soundManager;

    // Initialize

    public GameMain() {

        setTitle("Chicken Invaders");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);

        dbManager = new DatabaseManager();
        soundManager = new SoundManager();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainMenu = new MainMenu(this);
        loginPanel = new LoginPanel(this);
        registerPanel = new RegisterPanel(this);
        highScorePanel = new HighScorePanel(this);
        settingsPanel = new SettingsPanel(this);
        howToPlayPanel = new HowToPlayPanel(this);
        storePanel = new StorePanel(this);

        mainPanel.add(mainMenu, "MainMenu");
        mainPanel.add(loginPanel, "Login");
        mainPanel.add(registerPanel, "Register");
        mainPanel.add(highScorePanel, "HighScores");
        mainPanel.add(settingsPanel, "Settings");
        mainPanel.add(howToPlayPanel, "HowToPlay");
        mainPanel.add(storePanel, "Store");

        add(mainPanel);
        setVisible(true);

        soundManager.playBackgroundMusic();
    }

    // Shows the given panel and plays background music if and only if we enter main menu

    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
        if (panelName.equals("MainMenu")) {
            soundManager.playBackgroundMusic();
        }
    }

    // Starts the game if and only if the user is logged in

    public void startGame() {
        if (currentUser == null) {
            showPanel("Login");
            return;
        }

        if (gamePanel != null) {
            mainPanel.remove(gamePanel);
        }

        gamePanel = new GamePanel(this, currentUser);
        mainPanel.add(gamePanel, "Game");
        showPanel("Game");
        gamePanel.requestFocus();
        soundManager.stopBackgroundMusic();
        soundManager.playInGameMusic();
    }

    // Ends the game and returns to the MainMenu Panel

    public void endGame() {
        if (gamePanel != null) {
            gamePanel.stopGame();
            mainPanel.remove(gamePanel);
            gamePanel = null;
        }
        soundManager.playBackgroundMusic();
        showPanel("MainMenu");
    }

    // Getters and Setters

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public String getCurrentUsername() {
        return currentUser != null ? currentUser.getUsername() : null;
    }

    public DatabaseManager getDatabaseManager() {
        return dbManager;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public static void main(String[] args) {
        new GameMain();
    }
}
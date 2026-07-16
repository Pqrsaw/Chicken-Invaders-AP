package model;

public class User {

    private String username;
    private String password;
    private int highScore;
    private int lastLevel;
    private boolean bgmEnabled;
    private boolean shotEnabled;
    private boolean crashEnabled;
    private boolean gameoverEnabled;
    private int selectedPlane;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.highScore = 0;
        this.lastLevel = 0;
        this.bgmEnabled = true;
        this.shotEnabled = true;
        this.crashEnabled = true;
        this.gameoverEnabled = true;
        this.selectedPlane = 1;
    }

    // Getters and Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        if(highScore > this.highScore){
            this.highScore = highScore;
        }
    }

    public int getLastLevel() {
        return lastLevel;
    }

    public void setLastLevel(int lastLevel) {
        if(lastLevel > this.lastLevel){
            this.lastLevel = lastLevel;
        }
    }

    public boolean isBgmEnabled() {
        return bgmEnabled;
    }

    public void setBgmEnabled(boolean bgmEnabled) {
        this.bgmEnabled = bgmEnabled;
    }

    public boolean isShotEnabled() {
        return shotEnabled;
    }

    public void setShotEnabled(boolean shotEnabled) {
        this.shotEnabled = shotEnabled;
    }

    public boolean isCrashEnabled() {
        return crashEnabled;
    }

    public void setCrashEnabled(boolean crashEnabled) {
        this.crashEnabled = crashEnabled;
    }

    public boolean isGameoverEnabled() {
        return gameoverEnabled;
    }

    public void setGameoverEnabled(boolean gameoverEnabled) {
        this.gameoverEnabled = gameoverEnabled;
    }

    public int getSelectedPlane() {
        return selectedPlane;
    }

    public void setSelectedPlane(int selectedPlane) {
        this.selectedPlane = selectedPlane;
    }
}

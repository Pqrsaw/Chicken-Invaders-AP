package audio;

import javax.sound.sampled.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {

    private Map<String, Clip> soundClips;
    private boolean musicEnabled = true;
    private boolean shotEnabled = true;
    private boolean crashEnabled = true;
    private boolean gameoverEnabled = true;

    public SoundManager() {
        soundClips = new HashMap<>();
        loadSounds();
    }

    // Loads the soundtracks

    private void loadSounds() {

        loadSound("shot", "sound-effects/mixkit-short-laser-gun-shot-1670.wav");
        loadSound("explosion", "sound-effects/mixkit-epic-impact-afar-explosion-2782.wav");
        loadSound("gameover", "sound-effects/mixkit-retro-arcade-game-over-470.wav");
        loadSound("powerup", "sound-effects/mixkit-short-laser-gun-shot-1670.wav");
        loadSound("main_theme", "sound-effects/Chicken Invaders 2 Remastered OST - Main Theme.wav");
        loadSound("ending_theme", "sound-effects/Chicken Invaders 2 Remastered OST - Ending Theme.wav");
    }

    private void loadSound(String name, String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                soundClips.put(name, clip);
                System.out.println("Loaded sound: " + name);
            } else {
                System.err.println("Sound file not found: " + path);
            }
        } catch (Exception e) {
            System.err.println("Could not load sound: " + path + " - " + e.getMessage());
        }
    }

    // plays the soundtracks

    public void playSound(String soundName) {
        if (!musicEnabled && (soundName.equals("main_theme") || soundName.equals("ending_theme"))) {
            return;
        }
        if (!shotEnabled && soundName.equals("shot")) return;
        if (!crashEnabled && soundName.equals("explosion")) return;
        if (!gameoverEnabled && soundName.equals("gameover")) return;
        if (!gameoverEnabled && soundName.equals("ending_theme")) return;

        Clip clip = soundClips.get(soundName);
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void playBackgroundMusic() {
        playSound("main_theme");
    }

    public void playInGameMusic() {
        playSound("main_theme");
    }

    public void playEndingMusic() {
        playSound("ending_theme");
    }

    public void stopBackgroundMusic() {
        Clip clip = soundClips.get("main_theme");
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void playShotSound() {
        playSound("shot");
    }

    public void playExplosionSound() {
        playSound("explosion");
    }

    public void playGameOverSound() {
        playSound("gameover");
    }

    public void playPowerUpSound() {
        playSound("powerup");
    }

    // Getters and Setters

    public void setMusicEnabled(boolean enabled) {
        this.musicEnabled = enabled;
        if (!enabled) {
            stopBackgroundMusic();
        }
        else {
            playBackgroundMusic();
        }
    }

    public void setShotEnabled(boolean enabled) {
        this.shotEnabled = enabled;
    }

    public void setExplosionEnabled(boolean enabled) {
        this.crashEnabled = enabled;
    }

    public void setGameOverEnabled(boolean enabled) {
        this.gameoverEnabled = enabled;
    }

    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    public boolean isShotEnabled() {
        return shotEnabled;
    }

    public boolean isExplosionEnabled() {
        return crashEnabled;
    }

    public boolean isGameOverEnabled() {
        return gameoverEnabled;
    }
}
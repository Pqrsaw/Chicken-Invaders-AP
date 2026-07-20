package game;

import game.objects.Bullet;
import utils.ImageLoader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Plane {

    private int x, y;
    private int width = 50;
    private int height = 60;
    private int speed;
    private int lives;
    private int maxLives = 5;
    private int shotCount;
    private int maxShots = 10;
    private long fireRate;
    private long lastShotTime;
    private boolean shieldActive;
    private long shieldTimer;
    private boolean rapidFireActive;
    private long rapidFireTimer;
    private boolean frozen;
    private long freezeTimer;

    private Map<String, Long> activePowerUps = new HashMap<>();

    private boolean movingLeft, movingRight, movingUp, movingDown;
    private BufferedImage planeImage;
    private BufferedImage bulletImage;

    private static final int PANEL_WIDTH = 800;
    private static final int PANEL_HEIGHT = 600;

    public Plane(int planeNumber) {
        this.x = PANEL_WIDTH / 2 - width / 2;
        this.y = PANEL_HEIGHT - 120;
        this.lives = 3;
        this.shotCount = 1;
        this.fireRate = 300;
        this.lastShotTime = 0;
        this.shieldActive = false;
        this.rapidFireActive = false;
        this.frozen = false;

        loadImages(planeNumber);
        setPlaneProperties(planeNumber);
    }

    // Loads image of the plane based on the users choice

    private void loadImages(int planeNumber) {
        planeImage = ImageLoader.loadImage("airplan/" + planeNumber + ".png");
        if (planeImage != null) {
            this.width = Math.min(planeImage.getWidth(), 60);
            this.height = Math.min(planeImage.getHeight(), 70);
        }
        else {
            this.width = 50;
            this.height = 60;
        }
        bulletImage = ImageLoader.loadImage("airplan/shot.png");
    }

    // Sets attributes of the plane (StorePanel)

    private void setPlaneProperties(int planeNumber) {
        switch (planeNumber) {
            case 2: speed = 7; fireRate = 250; break;
            case 3: speed = 4; fireRate = 200; lives = 5; break;
            case 4: speed = 5; fireRate = 150; break;
            case 5: speed = 6; fireRate = 280; break;
            case 6: speed = 5; fireRate = 220; lives = 4; break;
            case 7: speed = 6; fireRate = 180; break;
            default: speed = 5; fireRate = 300; break;
        }
    }

    // Updates position and state of the plane

    public void update() {
        if (movingLeft) x -= speed;
        if (movingRight) x += speed;
        if (movingUp) y -= speed;
        if (movingDown) y += speed;

        x = Math.max(0, Math.min(x, PANEL_WIDTH - width));
        y = Math.max(0, Math.min(y, PANEL_HEIGHT - height));

        updatePowerUpTimers();
    }

    // Updates timer of power up durations

    private void updatePowerUpTimers() {
        if (shieldActive) {
            shieldTimer -= 16;
            if (shieldTimer <= 0) {
                shieldActive = false;
                activePowerUps.remove("SHIELD");
            } else {
                activePowerUps.put("SHIELD", shieldTimer / 1000);
            }
        }

        if (rapidFireActive) {
            rapidFireTimer -= 16;
            if (rapidFireTimer <= 0) {
                rapidFireActive = false;
                activePowerUps.remove("RAPID_FIRE");
            } else {
                activePowerUps.put("RAPID_FIRE", rapidFireTimer / 1000);
            }
        }

        if (frozen) {
            freezeTimer -= 16;
            if (freezeTimer <= 0) {
                frozen = false;
                activePowerUps.remove("FREEZE");
            } else {
                activePowerUps.put("FREEZE", freezeTimer / 1000);
            }
        }

        if (shotCount > 1) {
            activePowerUps.put("ADD_SHOT", 0L);
        } else {
            activePowerUps.remove("ADD_SHOT");
        }
    }

    // Activates the collected power up ability

    public void activatePowerUp(String type, int duration) {
        switch (type) {
            case "SHIELD":
                shieldActive = true;
                shieldTimer = duration * 1000;
                activePowerUps.put("SHIELD", (long) duration);
                break;
            case "RAPID_FIRE":
                rapidFireActive = true;
                rapidFireTimer = duration * 1000;
                activePowerUps.put("RAPID_FIRE", (long) duration);
                break;
            case "FREEZE":
                frozen = true;
                freezeTimer = duration * 1000;
                activePowerUps.put("FREEZE", (long) duration);
                System.out.println("  FREEZE BOMB ACTIVATED! All enemies and eggs frozen for 3 seconds!");
                break;
            case "ADD_SHOT":
                addShot();
                activePowerUps.put("ADD_SHOT", 0L);
                break;
        }
    }

    // Shoots bullets

    public List<Bullet> shoot() {
        List<Bullet> bullets = new ArrayList<>();
        long currentTime = System.currentTimeMillis();
        long effectiveFireRate = rapidFireActive ? fireRate / 2 : fireRate;

        if (currentTime - lastShotTime >= effectiveFireRate) {
            lastShotTime = currentTime;

            int currentShots = rapidFireActive ? shotCount * 2 : shotCount;
            currentShots = Math.min(currentShots, maxShots);

            if (currentShots == 1) {
                bullets.add(new Bullet(x + width / 2 - 4, y - 10, bulletImage));
            } else {
                int spacing = 12;
                int startX = x + width / 2 - ((currentShots - 1) * spacing) / 2;
                for (int i = 0; i < currentShots; i++) {
                    bullets.add(new Bullet(startX + i * spacing, y - 10, bulletImage));
                }
            }
        }
        return bullets;
    }

    // Takes damage

    public void takeDamage() {
        if (!shieldActive) {
            lives--;
            if (lives < 0) lives = 0;
        }
    }

    // Applies power up effects

    public void addLife() {
        if (lives < maxLives) lives++;
    }

    public void addShot() {
        if (shotCount < maxShots) shotCount++;
    }

    public void activateShield(int duration) {
        shieldActive = true;
        shieldTimer = duration * 1000;
    }

    public void activateRapidFire(int duration) {
        rapidFireActive = true;
        rapidFireTimer = duration * 1000;
    }

    public void activateFreeze(int duration) {
        frozen = true;
        freezeTimer = duration * 1000;
    }

    // Draws the plane

    public void draw(Graphics2D g) {

        // Draws the shield if activated

        if (shieldActive) {
            g.setColor(new Color(0, 200, 255, 80));
            g.fillOval(x - 10, y - 10, width + 20, height + 20);
            g.setColor(new Color(0, 200, 255, 150));
            g.drawOval(x - 10, y - 10, width + 20, height + 20);
        }

        if (planeImage != null) {
            g.drawImage(planeImage, x, y, width, height, null);
        }
        else {

            // If we could not load the images, draws the plane from scratch

            g.setColor(Color.GREEN);
            g.fillRect(x + 10, y, 20, 15);
            g.fillRect(x + 15, y + 15, 10, 30);
            g.fillRect(x, y + 25, 40, 10);
            g.fillRect(x + 8, y + 10, 24, 5);
            g.setColor(Color.CYAN);
            g.fillRect(x + 17, y + 5, 6, 10);
        }
    }

    // Getters and Setters

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public Map<String, Long> getActivePowerUps() {
        return activePowerUps;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public boolean isShieldActive() {
        return shieldActive;
    }

    public boolean isRapidFireActive() {
        return rapidFireActive;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getLives() {
        return lives;
    }

    public int getShotCount() {
        return shotCount;
    }

    public int getDamageMultiplier() {
        return 1;
    }

    public void setMovingLeft(boolean moving) {
        movingLeft = moving;
    }

    public void setMovingRight(boolean moving) {
        movingRight = moving;
    }

    public void setMovingUp(boolean moving) {
        movingUp = moving;
    }

    public void setMovingDown(boolean moving) {
        movingDown = moving;
    }
}

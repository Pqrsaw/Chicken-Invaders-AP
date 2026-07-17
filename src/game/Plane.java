package game;

import game.objects.Bullet;
import utils.ImageLoader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Plane {

    private int x, y;
    private int width = 40;
    private int height = 50;
    private int speed;
    private int lives;
    private int maxLives = 5;
    private int shotCount;
    private int maxShots = 5;
    private long fireRate;
    private long lastShotTime;
    private boolean shieldActive;
    private long shieldTimer;
    private boolean rapidFireActive;
    private long rapidFireTimer;
    private boolean frozen;
    private long freezeTimer;

    private boolean movingLeft, movingRight, movingUp, movingDown;
    private BufferedImage planeImage;
    private BufferedImage bulletImage;

    private static final int PANEL_WIDTH = 800;
    private static final int PANEL_HEIGHT = 600;

    public Plane(int planeNumber) {
        this.x = PANEL_WIDTH / 2 - width / 2;
        this.y = PANEL_HEIGHT - 100;
        this.lives = 3;
        this.shotCount = 1;
        this.fireRate = 300;
        this.lastShotTime = 0;
        this.shieldActive = false;
        this.rapidFireActive = false;

        loadImages(planeNumber);
        setPlaneProperties(planeNumber);
    }

    // Loads image of the plane based on the users choice

    private void loadImages(int planeNumber) {
        planeImage = ImageLoader.loadImage("airplan/" + planeNumber + ".png");
        if (planeImage != null) {
            this.width = planeImage.getWidth();
            this.height = planeImage.getHeight();
        }
        bulletImage = ImageLoader.loadImage("airplan/shot.png");
    }

    // Sets attributes of the plane

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

        if (shieldActive) {
            shieldTimer -= 16;
            if (shieldTimer <= 0) shieldActive = false;
        }

        if (rapidFireActive) {
            rapidFireTimer -= 16;
            if (rapidFireTimer <= 0) rapidFireActive = false;
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
                bullets.add(new Bullet(x + width / 2 - 3, y - 10, bulletImage));
            }
            else {
                int spacing = 10;
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

    public void freeze() {
        frozen = true;
        freezeTimer = 3000;
    }

    // Draws the plane

    public void draw(Graphics2D g) {
        if (shieldActive) {
            g.setColor(new Color(0, 200, 255, 50));
            g.fillOval(x - 10, y - 10, width + 20, height + 20);
            g.setColor(new Color(0, 200, 255, 100));
            g.drawOval(x - 10, y - 10, width + 20, height + 20);
        }

        if (planeImage != null) {
            g.drawImage(planeImage, x, y, width, height, null);
        }
        else {
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

    public boolean isShieldActive() {
        return shieldActive;
    }

    public boolean isRapidFireActive() {
        return rapidFireActive;
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
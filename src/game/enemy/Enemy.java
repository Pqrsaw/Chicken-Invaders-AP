package game.enemy;

import utils.ImageLoader;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Enemy {

    protected int x, y;
    protected int width = 40;
    protected int height = 40;
    protected int health;
    protected int maxHealth;
    protected int scoreValue;
    protected boolean alive;
    protected double speed;
    protected String type;  // "NORMAL", "FAST", "ZIGZAG", "SHOOTER"
    protected BufferedImage image;

    public Enemy(int x, int y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.alive = true;
        loadImage();
    }

    // Abstract methods

    protected abstract void loadImage();

    public abstract void update();

    public abstract String getType();

    // Takes damage

    public void takeDamage() {
        health--;
        if (health <= 0) alive = false;
    }

    // Draws the enemy

    public void draw(Graphics2D g) {
        if (!alive) return;

        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        }
        else {
            g.setColor(Color.YELLOW);
            g.fillOval(x, y, width, height);
            g.setColor(Color.WHITE);
            g.fillOval(x + 8, y + 10, 8, 8);
            g.fillOval(x + 24, y + 10, 8, 8);
            g.setColor(Color.BLACK);
            g.fillOval(x + 10, y + 12, 4, 4);
            g.fillOval(x + 26, y + 12, 4, 4);
        }

        if (health < maxHealth) {
            g.setColor(Color.RED);
            g.fillRect(x, y - 5, width * health / maxHealth, 3);
            g.setColor(Color.WHITE);
            g.drawRect(x, y - 5, width, 3);
        }
    }

    // Getters and Setters

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isAlive() {
        return alive;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getScoreValue() {
        return scoreValue;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

}

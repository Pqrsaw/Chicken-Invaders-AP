package game.boss;

import game.objects.Egg;
import utils.ImageLoader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public abstract class Boss {

    protected int x, y;
    protected int width = 150;
    protected int height = 150;
    protected int maxHealth;
    protected int health;
    protected boolean alive;
    protected double moveTimer;
    protected double moveDirection;
    protected double shootTimer;
    protected double shootInterval;
    protected int level;
    protected int scoreValue;
    protected BufferedImage image;

    public Boss(int level, int health, int width, int height) {
        this.level = level;
        this.maxHealth = health;
        this.health = health;
        this.width = width;
        this.height = height;
        this.alive = true;
        this.x = 400 - width/2;
        this.y = 50;
        this.moveDirection = 1;
        this.moveTimer = 0;
        this.shootTimer = 0;
        loadImage();
    }

    // Abstract methods

    protected void loadImage() {}

    public abstract void update();

    public abstract List<Egg> shoot();

    public abstract boolean canShoot();

    protected abstract void drawHealthBar(Graphics2D g);

    public void updateShootTimer() {
        shootTimer += 0.016;
    }

    // Takes damage

    public void takeDamage(int damageMultiplier) {
        health -= damageMultiplier;
        if (health <= 0) {
            health = 0;
            alive = false;
        }
    }

    // Draws the boss

    public void draw(Graphics2D g) {
        if (!alive) return;

        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        } else {
            g.setColor(new Color(200, 50, 50));
            g.fillOval(x, y, width, height);
        }
        drawHealthBar(g);
    }

    // Getters and Setters

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public double getHealthPercent() {
        return (double) health / maxHealth;
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
}
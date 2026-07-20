package game.enemy;

import game.Cell;
import utils.ImageLoader;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Enemy {

    protected int x, y;
    protected int width = 35;
    protected int height = 35;
    protected int health;
    protected int maxHealth;
    protected int scoreValue;
    protected boolean alive;
    protected double speed;
    protected String type; // "NORMAL", "FAST", "ZIGZAG", "SHOOTER"
    protected BufferedImage image;
    protected Cell cell;
    protected boolean isFlying = false;
    protected int targetX;
    protected int targetY;
    protected double flySpeed = 2.5;
    protected double flyPhase = 0;
    protected double flyAmplitude = 0;
    protected double flyFrequency = 0.1;

    public Enemy(int x, int y, String type, Cell cell) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.cell = cell;
        this.alive = true;
        this.isFlying = false;
        loadImage();
    }

    // Abstract methods

    protected void loadImage() {}

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
        } else {
            g.setColor(Color.YELLOW);
            g.fillOval(x, y, width, height);
            g.setColor(Color.WHITE);
            g.fillOval(x + 8, y + 10, 8, 8);
            g.fillOval(x + width - 16, y + 10, 8, 8);
            g.setColor(Color.BLACK);
            g.fillOval(x + 10, y + 12, 4, 4);
            g.fillOval(x + width - 14, y + 12, 4, 4);
        }

        if (isFlying) {
            g.setColor(new Color(255, 255, 255, 50));
            g.drawLine(x + width/2, y + height/2, targetX, targetY);
        }

        if (health < maxHealth) {
            g.setColor(Color.RED);
            g.fillRect(x, y - 8, width * health / maxHealth, 4);
            g.setColor(Color.WHITE);
            g.drawRect(x, y - 8, width, 4);
        }
    }

    // Update the respawn flying

    public void updateFlying() {
        if (!isFlying || !alive) return;

        double dx = targetX - x;
        double dy = targetY - y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < 5) {
            x = targetX;
            y = targetY;
            isFlying = false;
        } else {
            double speed = flySpeed;

            double vx = (dx / distance) * speed;
            double vy = (dy / distance) * speed;

            x += vx;
            y += vy;

            // اگر دشمن Zigzag است، حرکت زیگزاگ داشته باشد
            if (this instanceof ZigzagEnemy) {
                flyPhase += flyFrequency;
                x += Math.sin(flyPhase) * flyAmplitude;
            }
        }
    }

    // Getters and Setters

    public void setTarget(int targetX, int targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
        this.isFlying = true;
        this.flyPhase = 0;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isFlying() {
        return isFlying;
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

    public Cell getCell() {
        return cell;
    }
}
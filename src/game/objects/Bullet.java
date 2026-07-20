package game.objects;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bullet {

    private int x, y;
    private int width = 8;
    private int height = 20;
    private int speed = 10;
    private boolean active = true;
    private BufferedImage image;

    public Bullet(int x, int y, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.image = image;
        if (image != null) {
            this.width = Math.min(image.getWidth(), 10);
            this.height = Math.min(image.getHeight(), 25);
        }
    }

    public void update() {
        y -= speed;
        if (y < -20) active = false;
    }

    // Draws the bullet using the image given
    // if the image does not exist
    // draws the bullet from scratch

    public void draw(Graphics2D g) {
        if (!active) return;

        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        } else {
            g.setColor(new Color(255, 255, 200));
            g.fillRoundRect(x, y, width, height, 4, 4);
            g.setColor(new Color(255, 200, 50, 100));
            g.fillOval(x - 2, y - 2, width + 4, height + 4);
        }
    }

    // Getters and Setters

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
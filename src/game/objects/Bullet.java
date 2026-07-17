package game.objects;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bullet {

    private int x, y;
    private int width = 6;
    private int height = 15;
    private int speed = 8;
    private boolean active = true;
    private BufferedImage image;

    public Bullet(int x, int y, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.image = image;
        if (image != null) {
            this.width = image.getWidth();
            this.height = image.getHeight();
        }
    }

    public void update() {
        y -= speed;
        if (y < 0) {
            active = false;
        }
    }

    // Draws the bullet using the image given
    // if the image does not exist
    // draws the bullet from scratch

    public void draw(Graphics2D g) {
        if (!active) return;

        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        }
        else {
            GradientPaint gradient = new GradientPaint(
                    x, y, new Color(255, 255, 200),
                    x, y + height, new Color(255, 200, 50)
            );
            g.setPaint(gradient);
            g.fillRoundRect(x, y, width, height, 3, 3);
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
package game.objects;

import utils.ImageLoader;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Egg {

    private int x, y;
    private int radius = 8;
    private double vx, vy;
    private boolean active = true;
    private BufferedImage image;

    // Constructor #1

    public Egg(int x, int y, double vx, double vy) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.image = ImageLoader.loadImage("chicken/egg.png");
        if (image != null) {
            this.radius = Math.min(image.getWidth() / 2, 10);  // ← حداکثر 10
        }
    }

    // Constructor #2

    public Egg(int x, int y, String direction) {
        this.x = x;
        this.y = y;
        this.image = ImageLoader.loadImage("chicken/egg.png");
        if (image != null) {
            this.radius = Math.min(image.getWidth() / 2, 10);  // ← حداکثر 10
        }

        if (direction.equals("DOWN")) {
            this.vx = 0; this.vy = 4;
        }
        else if (direction.equals("LEFT")) {
            this.vx = -4; this.vy = 0;
        }
        else if (direction.equals("RIGHT")) {
            this.vx = 4; this.vy = 0;
        }
        else if (direction.equals("UP")) {
            this.vx = 0; this.vy = -4;
        }
        else {
            this.vx = 0; this.vy = 4;
        }
    }

    // Updates the position

    public void update() {
        x += vx;
        y += vy;
        if (x < -30 || x > 830 || y < -30 || y > 630) active = false;
    }

    // Draws the egg

    public void draw(Graphics2D g) {
        if (!active) return;

        if (image != null) {
            int size = radius * 2;
            g.drawImage(image, x - radius, y - radius, size, size, null);
        }
        else {
            g.setColor(Color.WHITE);
            g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
            g.setColor(new Color(255, 255, 255, 150));
            g.fillOval(x - radius/2, y - radius/2, radius, radius);
        }
    }

    // Getters and Setters

    public Rectangle getBounds() {
        return new Rectangle(x - radius, y - radius, radius * 2, radius * 2);
    }

    public boolean isOffScreen(int height) {
        return y > height + 30 || !active;
    }

    public boolean isActive() {
        return active;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
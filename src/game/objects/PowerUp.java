package game.objects;

import game.Plane;
import utils.ImageLoader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class PowerUp {

    private int x, y;
    private int width = 30;
    private int height = 30;
    private int speed = 2;
    private String type;  // "ADD_SHOT", "BOMB", "FAST_SHOT", "FREEZE", "HEAL", "SHIELD"
    private boolean active = true;
    private double animationTimer;
    private BufferedImage image;

    public PowerUp(int x, int y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.animationTimer = 0;
        loadImage();
    }

    // Loads the power up image based on its type

    private void loadImage() {
        String filename;
        if (type.equals("ADD_SHOT")) filename = "add_shot.png";
        else if (type.equals("BOMB")) filename = "bomb_shot.png";
        else if (type.equals("FAST_SHOT")) filename = "fast_shot.png";
        else if (type.equals("FREEZE")) filename = "freeze.png";
        else if (type.equals("HEAL")) filename = "heal.png";
        else if (type.equals("SHIELD")) filename = "shield.png";
        else filename = "add_shot.png";

        this.image = ImageLoader.loadImage("powerup1/" + filename);
        if (this.image == null) {
            this.image = ImageLoader.loadImage("powerup2/" + filename);
        }
        if (this.image != null) {
            this.width = this.image.getWidth();
            this.height = this.image.getHeight();
        }
    }

    // Generates a random power up

    public static PowerUp randomPowerUp(int x, int y) {
        Random rand = new Random();
        String[] types = {"ADD_SHOT", "BOMB", "FAST_SHOT", "FREEZE", "HEAL", "SHIELD"};
        String type = types[rand.nextInt(types.length)];
        return new PowerUp(x, y, type);
    }

    // Updates the position

    public void update() {
        y += speed;
        animationTimer += 0.05;
        if (y > 620) active = false;
    }

    // Draws the power ups

    public void draw(Graphics2D g) {
        if (!active) return;

        int pulse = (int)(Math.sin(animationTimer) * 3);
        g.setColor(new Color(255, 255, 255, 50));
        g.fillOval(x - 10 - pulse, y - 10 - pulse, width + 20 + pulse * 2, height + 20 + pulse * 2);

        if (image != null) {
            g.drawImage(image, x - pulse, y - pulse, width + pulse * 2, height + pulse * 2, null);
        }
        else {
            g.setColor(getColor());
            g.fillOval(x, y, width, height);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            String symbol = getSymbol();
            FontMetrics fm = g.getFontMetrics();
            g.drawString(symbol, x + (width - fm.stringWidth(symbol)) / 2, y + (height + fm.getAscent()) / 2 - 2);
        }
    }

    // Returns the color of each power up

    private Color getColor() {
        if (type.equals("ADD_SHOT")) return new Color(0, 200, 255);
        else if (type.equals("BOMB")) return new Color(255, 100, 0);
        else if (type.equals("FAST_SHOT")) return new Color(255, 255, 0);
        else if (type.equals("FREEZE")) return new Color(0, 200, 255);
        else if (type.equals("HEAL")) return new Color(0, 255, 100);
        else if (type.equals("SHIELD")) return new Color(100, 200, 255);
        else return Color.WHITE;
    }

    // Returns the symbol of each power up

    private String getSymbol() {
        if (type.equals("ADD_SHOT")) return "+";
        else if (type.equals("BOMB")) return "B";
        else if (type.equals("FAST_SHOT")) return "F";
        else if (type.equals("FREEZE")) return "❄";
        else if (type.equals("HEAL")) return "♥";
        else if (type.equals("SHIELD")) return "S";
        else return "?";
    }

    // Applies the power up effect

    public void applyEffect(Plane plane) {
        if (type.equals("ADD_SHOT")) plane.addShot();
        else if (type.equals("BOMB")) { /* Bomb effect */ }
        else if (type.equals("FAST_SHOT")) plane.activateRapidFire(8);
        else if (type.equals("FREEZE")) plane.freeze();
        else if (type.equals("HEAL")) plane.addLife();
        else if (type.equals("SHIELD")) plane.activateShield(10);
    }

    // Getters and Setters

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isOffScreen(int height) {
        return y > height + 20 || !active;
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
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
    private static String type; // "ADD_SHOT", "FAST_SHOT", "FREEZE", "HEAL", "SHIELD"
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
        else if (type.equals("RAPID_FIRE")) filename = "fast_shot.png";
        else if (type.equals("EXTRA_LIFE")) filename = "heal.png";
        else if (type.equals("SHIELD")) filename = "shield.png";
        else if (type.equals("FREEZE")) filename = "freeze.png";
        else filename = "add_shot.png";

        this.image = ImageLoader.loadImage("powerup1/" + filename);
        if (this.image == null) {
            this.image = ImageLoader.loadImage("powerup2/" + filename);
        }
        if (this.image != null) {
            this.width = Math.min(image.getWidth(), 35);
            this.height = Math.min(image.getHeight(), 35);
        }
    }

    // Generates a random power up

    public static PowerUp randomPowerUp(int x, int y) {
        Random rand = new Random();
        String[] types = {"ADD_SHOT", "RAPID_FIRE", "EXTRA_LIFE", "SHIELD", "FREEZE"};
        String type = types[rand.nextInt(types.length)];
        return new PowerUp(x, y, type);
    }

    // Displays the power up name for the top right of HUD

    public static String getDisplayName(String type) {
        switch (type) {
            case "ADD_SHOT": return "ADD FIRE";
            case "RAPID_FIRE": return "RAPID FIRE";
            case "EXTRA_LIFE": return "EXTRA LIFE";
            case "SHIELD": return "SHIELD";
            case "FREEZE": return "FREEZE BOMB";
            default: return type;
        }
    }

    // Returns the color assigned to the power up

    public static Color getColor(String type) {
        switch (type) {
            case "ADD_SHOT": return new Color(0, 200, 255);
            case "RAPID_FIRE": return new Color(255, 200, 0);
            case "EXTRA_LIFE": return new Color(0, 255, 100);
            case "SHIELD": return new Color(100, 200, 255);
            case "FREEZE": return new Color(0, 150, 255);
            default: return Color.WHITE;
        }
    }

    // Applies the power up effect

    public void applyEffect(Plane plane) {
        switch (type) {
            case "ADD_SHOT":
                plane.activatePowerUp("ADD_SHOT", -1);
                break;
            case "RAPID_FIRE":
                plane.activatePowerUp("RAPID_FIRE", 8);
                break;
            case "EXTRA_LIFE":
                plane.addLife();
                break;
            case "SHIELD":
                plane.activatePowerUp("SHIELD", 10);
                break;
            case "FREEZE":
                plane.activatePowerUp("FREEZE", 3);
                break;
        }
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
            g.setColor(getColor(type));
            g.fillOval(x, y, width, height);
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 16));
            String symbol = getSymbol();
            FontMetrics fm = g.getFontMetrics();
            g.drawString(symbol, x + (width - fm.stringWidth(symbol)) / 2,
                    y + (height + fm.getAscent()) / 2 - 2);
        }
    }

    // Returns the Symbol assigned to the power up

    private String getSymbol() {
        if (type.equals("ADD_SHOT")) return "+";
        else if (type.equals("RAPID_FIRE")) return "F";
        else if (type.equals("EXTRA_LIFE")) return "♥";
        else if (type.equals("SHIELD")) return "S";
        else if (type.equals("FREEZE")) return "❄";
        else return "?";
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

    public String getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
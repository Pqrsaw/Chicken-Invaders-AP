package game.enemy;

import utils.ImageLoader;
import java.awt.image.BufferedImage;

public class ZigzagEnemy extends Enemy {

    private double phase;
    private double zigzagOffset;

    public ZigzagEnemy(int x, int y) {
        super(x, y, "ZIGZAG");
        this.health = 2;
        this.maxHealth = 2;
        this.scoreValue = 20;
        this.speed = 1.0;
        this.phase = Math.random() * Math.PI * 2;
        this.zigzagOffset = 0;
    }

    // Implementing abstract methods

    // Loads image of the enemy

    @Override
    protected void loadImage() {
        this.image = ImageLoader.loadImage("chicken/zigzag_chicken.png");
        if (image != null) {
            this.width = image.getWidth();
            this.height = image.getHeight();
        }
    }

    // Updates the state

    @Override
    public void update() {
        phase += 0.1;
        zigzagOffset = Math.sin(phase) * 15;
    }

    // Returns type of the enemy

    @Override
    public String getType() {
        return "ZIGZAG";
    }
}
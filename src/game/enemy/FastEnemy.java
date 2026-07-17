package game.enemy;

import utils.ImageLoader;
import java.awt.image.BufferedImage;

public class FastEnemy extends Enemy {

    private double phase;

    public FastEnemy(int x, int y) {
        super(x, y, "FAST");
        this.health = 1;
        this.maxHealth = 1;
        this.scoreValue = 15;
        this.speed = 2.0;
        this.phase = Math.random() * Math.PI * 2;
    }

    // Implementing abstract methods

    // Loads image of the enemy

    @Override
    protected void loadImage() {
        this.image = ImageLoader.loadImage("chicken/fast_chicken.png");
        if (image != null) {
            this.width = image.getWidth();
            this.height = image.getHeight();
        }
    }

    // Updates the state

    @Override
    public void update() {
        phase += 0.05;
    }

    // Returns type of the enemy

    @Override
    public String getType() {
        return "FAST";
    }
}
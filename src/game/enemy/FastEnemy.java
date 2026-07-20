package game.enemy;

import game.Cell;
import utils.ImageLoader;
import java.awt.image.BufferedImage;

public class FastEnemy extends Enemy {

    private double phase;

    public FastEnemy(int x, int y, Cell cell) {
        super(x, y, "FAST", cell);
        this.health = 1;
        this.maxHealth = 1;
        this.scoreValue = 15;
        this.speed = 2.0; // 2x
        this.flySpeed = 5.0; // 2x
        this.phase = Math.random() * Math.PI * 2;
    }

    // Implementing abstract methods

    // Loads image of the enemy

    @Override
    protected void loadImage() {
        this.image = ImageLoader.loadImage("chicken/fast_chicken.png");
        if (image != null) {
            this.width = Math.min(image.getWidth(), 35);
            this.height = Math.min(image.getHeight(), 35);
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
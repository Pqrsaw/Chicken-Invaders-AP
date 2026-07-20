package game.enemy;

import game.Cell;
import utils.ImageLoader;
import java.awt.image.BufferedImage;

public class ZigzagEnemy extends Enemy {

    private double phase;
    private double zigzagOffset;

    public ZigzagEnemy(int x, int y, Cell cell) {
        super(x, y, "ZIGZAG", cell);
        this.health = 2;
        this.maxHealth = 2;
        this.scoreValue = 20;
        this.speed = 1.0;
        this.flySpeed = 2.5;
        this.flyAmplitude = 10;
        this.flyFrequency = 0.08;
        this.phase = Math.random() * Math.PI * 2;
        this.zigzagOffset = 0;
    }

    // Implementing abstract methods

    // Loads image of the enemy

    @Override
    protected void loadImage() {
        this.image = ImageLoader.loadImage("chicken/zigzag_chicken.png");
        if (image != null) {
            this.width = Math.min(image.getWidth(), 35);
            this.height = Math.min(image.getHeight(), 35);
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
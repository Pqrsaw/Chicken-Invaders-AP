package game.enemy;

import game.Cell;
import utils.ImageLoader;
import java.awt.image.BufferedImage;

public class NormalEnemy extends Enemy {

    public NormalEnemy(int x, int y, Cell cell) {
        super(x, y, "NORMAL", cell);
        this.health = 2;
        this.maxHealth = 2;
        this.scoreValue = 10;
        this.speed = 1.0;
        this.flySpeed = 2.5;
    }

    // Implementing abstract methods

    // Loads image of the enemy

    @Override
    protected void loadImage() {
        this.image = ImageLoader.loadImage("chicken/normal_chicken.png");
        if (image != null) {
            this.width = Math.min(image.getWidth(), 35);
            this.height = Math.min(image.getHeight(), 35);
        }
    }

    // updates the state (it does not need to change for normal enemy)

    @Override
    public void update() {}

    // Returns type of the enemy

    @Override
    public String getType() {
        return "NORMAL";
    }
}
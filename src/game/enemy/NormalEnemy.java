package game.enemy;

import utils.ImageLoader;
import java.awt.image.BufferedImage;

public class NormalEnemy extends Enemy {

    public NormalEnemy(int x, int y) {
        super(x, y, "NORMAL");
        this.health = 2;
        this.maxHealth = 2;
        this.scoreValue = 10;
        this.speed = 1.0;
    }

    // Implementing abstract methods

    // Loads image of the enemy

    @Override
    protected void loadImage() {
        this.image = ImageLoader.loadImage("chicken/normal_chicken.png");
        if (image != null) {
            this.width = image.getWidth();
            this.height = image.getHeight();
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
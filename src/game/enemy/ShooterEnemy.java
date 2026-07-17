package game.enemy;

import game.objects.Egg;
import utils.ImageLoader;
import java.awt.image.BufferedImage;

public class ShooterEnemy extends Enemy {

    private double shootTimer;
    private double shootInterval;

    public ShooterEnemy(int x, int y) {
        super(x, y, "SHOOTER");
        this.health = 2;
        this.maxHealth = 2;
        this.scoreValue = 25;
        this.speed = 1.0;
        this.shootTimer = Math.random() * 3;
        this.shootInterval = 2.0 + Math.random() * 2;
    }

    // Implementing abstract methods

    // Loads image of the enemy

    @Override
    protected void loadImage() {
        this.image = ImageLoader.loadImage("chicken/shooter_chicken.png");
        if (image != null) {
            this.width = image.getWidth();
            this.height = image.getHeight();
        }
    }

    // Updates the state

    @Override
    public void update() {
        shootTimer += 0.016;
        if (shootTimer >= shootInterval) shootTimer = 0;
    }

    // Returns type of the enemy

    @Override
    public String getType() {
        return "SHOOTER";
    }

    // Returns if shooting Horizontally is available or not

    public boolean canShootHorizontally() {
        return shootTimer >= shootInterval;
    }

    // Shoots Horizontally

    public Egg shootHorizontal(int targetX, int targetY) {
        double dx = targetX - x;
        double dy = targetY - y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance == 0) return null;

        double speed = 5.0;
        double vx = (dx / distance) * speed;
        double vy = (dy / distance) * speed;
        return new Egg(x + width/2, y + height/2, vx, vy);
    }
}
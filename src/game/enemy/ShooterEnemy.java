package game.enemy;

import game.Cell;
import game.objects.Egg;
import utils.ImageLoader;
import java.awt.image.BufferedImage;

public class ShooterEnemy extends Enemy {

    private double shootTimer;
    private double shootInterval;
    private double horizontalShootTimer;
    private double horizontalShootInterval;

    public ShooterEnemy(int x, int y, Cell cell) {
        super(x, y, "SHOOTER", cell);
        this.health = 2;
        this.maxHealth = 2;
        this.scoreValue = 25;
        this.speed = 1.0;
        this.flySpeed = 2.5;
        this.shootTimer = Math.random() * 3;
        this.shootInterval = 2.0 + Math.random() * 2;
        this.horizontalShootTimer = Math.random() * 4;
        this.horizontalShootInterval = 3.0 + Math.random() * 2;
    }

    // Implementing abstract methods

    // Loads image of the enemy

    @Override
    protected void loadImage() {
        this.image = ImageLoader.loadImage("chicken/shooter_chicken.png");
        if (image != null) {
            this.width = Math.min(image.getWidth(), 35);
            this.height = Math.min(image.getHeight(), 35);
        }
    }

    // Updates the state

    @Override
    public void update() {
        shootTimer += 0.016;
        if (shootTimer >= shootInterval) shootTimer = 0;

        if (!isFlying()) {
            horizontalShootTimer += 0.016;
        }
    }

    // Returns type of enemy

    @Override
    public String getType() {
        return "SHOOTER";
    }

    // Returns if the chicken can shoot

    public boolean canShoot() {
        return shootTimer >= shootInterval;
    }

    // Return if the chicken can shoot horizontally
    // (unique ability)

    public boolean canShootHorizontally() {
        return alive && !isFlying() && horizontalShootTimer >= horizontalShootInterval;
    }

    // Shoots eggs towards the plane in a horizontal way

    public Egg shootHorizontal(int targetX, int targetY) {
        if (!canShootHorizontally()) {
            return null;
        }

        horizontalShootTimer = 0;

        double dx = targetX - (x + width/2);
        double dy = targetY - (y + height/2);
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance == 0) return null;

        double speed = 4.0;
        double vx = (dx / distance) * speed;
        double vy = (dy / distance) * speed;

        System.out.println("  Shooter enemy firing horizontal egg!");

        return new Egg(x + width/2, y + height/2, vx, vy);
    }
}
package game.boss;

import game.objects.Egg;
import utils.ImageLoader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BossLevel4 extends Boss {

    private double yOffset;
    private double yDirection;

    public BossLevel4() {
        super(4, 50, 120, 120);
        this.shootInterval = 1.5;
        this.yOffset = 0;
        this.yDirection = 0.5;
        this.scoreValue = 500;
    }

    // Implementing the abstract methods

    // Loads the boss image

    @Override
    protected void loadImage() {
        this.image = ImageLoader.loadImage("chicken/boss1.png");
        if (image != null) {
            this.width = image.getWidth();
            this.height = image.getHeight();
        }
    }

    // Updates the position

    @Override
    public void update() {
        moveTimer += 0.016;
        x += 1.5 * moveDirection;
        if (x <= 20 || x >= 780 - width) moveDirection *= -1;

        yOffset += yDirection * 0.5;
        if (Math.abs(yOffset) > 30) yDirection *= -1;
        y = 50 + (int)yOffset;
    }

    // Draws the health bar

    @Override
    protected void drawHealthBar(Graphics2D g) {
        int barWidth = 200, barHeight = 15;
        int bx = 400 - barWidth / 2, by = 20;

        g.setColor(Color.DARK_GRAY);
        g.fillRect(bx, by, barWidth, barHeight);

        double percent = getHealthPercent();
        Color color = percent > 0.5 ? Color.GREEN : percent > 0.25 ? Color.YELLOW : Color.RED;
        g.setColor(color);
        g.fillRect(bx, by, (int)(barWidth * percent), barHeight);

        g.setColor(Color.WHITE);
        g.drawRect(bx, by, barWidth, barHeight);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString("BOSS LEVEL 4", bx + 50, by + 12);
    }

    // Shoots eggs

    @Override
    public List<Egg> shoot() {
        List<Egg> eggs = new ArrayList<>();
        shootTimer += 0.016;

        if (shootTimer >= shootInterval) {
            shootTimer = 0;
            double[][] directions = {{0,1}, {0,-1}, {1,0}, {-1,0}};
            int cx = x + width / 2, cy = y + height / 2;
            for (double[] dir : directions) {
                eggs.add(new Egg(cx, cy, dir[0] * 4.0, dir[1] * 4.0));
            }
        }
        return eggs;
    }

    // Returns if the boss can shoot or not

    @Override
    public boolean canShoot() {
        return alive && shootTimer >= shootInterval;
    }
}
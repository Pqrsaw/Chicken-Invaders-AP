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
        super(4, 50, 150, 150);
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
            this.width = Math.min(image.getWidth(), 150);
            this.height = Math.min(image.getHeight(), 150);
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

        updateShootTimer();
    }

    // Draws the health bar

    @Override
    protected void drawHealthBar(Graphics2D g) {
        int barWidth = 250;
        int barHeight = 18;
        int bx = (800 - barWidth) / 2;
        int by = 15;

        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(bx - 2, by - 2, barWidth + 4, barHeight + 4);

        g.setColor(Color.DARK_GRAY);
        g.fillRect(bx, by, barWidth, barHeight);

        double percent = getHealthPercent();
        Color color = percent > 0.5 ? new Color(0, 200, 0) :
                percent > 0.25 ? new Color(200, 200, 0) : new Color(200, 0, 0);
        g.setColor(color);
        g.fillRect(bx, by, (int)(barWidth * percent), barHeight);

        g.setColor(Color.WHITE);
        g.drawRect(bx, by, barWidth, barHeight);
        g.setFont(new Font("SansSerif", Font.BOLD, 12));
        g.drawString("BOSS LEVEL 4", bx + barWidth / 2 - 50, by + 14);
    }

    // Shoots eggs

    @Override
    public List<Egg> shoot() {
        List<Egg> eggs = new ArrayList<>();

        if (shootTimer >= shootInterval) {
            shootTimer = 0;

            double[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
            int cx = x + width / 2;
            int cy = y + height / 2;
            double speed = 4.0;

            for (double[] dir : directions) {
                eggs.add(new Egg(cx, cy, dir[0] * speed, dir[1] * speed));
            }

            System.out.println("  Boss Level 4 shooting in 4 directions!");
        }

        return eggs;
    }

    // Returns if the boss can shoot or not

    @Override
    public boolean canShoot() {
        return alive && shootTimer >= shootInterval;
    }
}
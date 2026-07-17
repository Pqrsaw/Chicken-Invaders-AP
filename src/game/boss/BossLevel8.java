package game.boss;

import game.objects.Egg;
import utils.ImageLoader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BossLevel8 extends Boss {
    private double yOffset;
    private double yDirection;
    private double phase;

    public BossLevel8() {
        super(8, 100, 160, 160);
        this.shootInterval = 1.0;
        this.yOffset = 0;
        this.yDirection = 0.8;
        this.phase = 0;
        this.scoreValue = 1000;
    }

    // Implementing the abstract methods

    // Loads the boss image

    @Override
    protected void loadImage() {
        this.image = ImageLoader.loadImage("chicken/boss2.png");
        if (image != null) {
            this.width = image.getWidth();
            this.height = image.getHeight();
        }
    }

    // Updates the position

    @Override
    public void update() {
        phase += 0.02;
        double speed = 2.0 + Math.sin(phase) * 0.5;
        x += speed * moveDirection;
        if (x <= 20 || x >= 780 - width) moveDirection *= -1;

        yOffset += yDirection * 0.8;
        if (Math.abs(yOffset) > 50) yDirection *= -1;
        y = 30 + (int)yOffset;
    }

    // Draws the health bar

    @Override
    protected void drawHealthBar(Graphics2D g) {
        int barWidth = 300, barHeight = 20;
        int bx = 400 - barWidth / 2, by = 10;

        g.setColor(Color.DARK_GRAY);
        g.fillRect(bx, by, barWidth, barHeight);

        double percent = getHealthPercent();
        Color color = percent > 0.5 ? Color.GREEN : percent > 0.25 ? Color.YELLOW : Color.RED;
        g.setColor(color);
        g.fillRect(bx, by, (int)(barWidth * percent), barHeight);

        g.setColor(Color.WHITE);
        g.drawRect(bx, by, barWidth, barHeight);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("FINAL BOSS", bx + barWidth / 2 - 40, by + 15);
    }

    // Shoots eggs

    @Override
    public List<Egg> shoot() {
        List<Egg> eggs = new ArrayList<>();
        shootTimer += 0.016;

        if (shootTimer >= shootInterval) {
            shootTimer = 0;
            double[][] directions = {
                    {0,1}, {0,-1}, {1,0}, {-1,0},
                    {1,1}, {-1,1}, {1,-1}, {-1,-1}
            };
            int cx = x + width / 2, cy = y + height / 2;
            double speed = 5.0;
            for (double[] dir : directions) {
                double length = Math.sqrt(dir[0]*dir[0] + dir[1]*dir[1]);
                eggs.add(new Egg(cx, cy, (dir[0]/length)*speed, (dir[1]/length)*speed));
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
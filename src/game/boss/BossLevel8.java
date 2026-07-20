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
        super(8, 100, 180, 180);
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
            this.width = Math.min(image.getWidth(), 180);
            this.height = Math.min(image.getHeight(), 180);
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

        updateShootTimer();
    }

    // Draws the health bar

    @Override
    protected void drawHealthBar(Graphics2D g) {
        int barWidth = 350;
        int barHeight = 22;
        int bx = (800 - barWidth) / 2;
        int by = 10;

        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(bx - 3, by - 3, barWidth + 6, barHeight + 6);

        g.setColor(Color.DARK_GRAY);
        g.fillRect(bx, by, barWidth, barHeight);

        double percent = getHealthPercent();
        Color color = percent > 0.5 ? new Color(0, 200, 0) :
                percent > 0.25 ? new Color(200, 200, 0) : new Color(200, 0, 0);
        g.setColor(color);
        g.fillRect(bx, by, (int)(barWidth * percent), barHeight);

        g.setColor(Color.WHITE);
        g.drawRect(bx, by, barWidth, barHeight);
        g.setFont(new Font("SansSerif", Font.BOLD, 14));
        g.drawString("FINAL BOSS", bx + barWidth / 2 - 60, by + 17);
    }

    // Shoots eggs

    @Override
    public List<Egg> shoot() {
        List<Egg> eggs = new ArrayList<>();

        if (shootTimer >= shootInterval) {
            shootTimer = 0;

            double[][] directions = {
                    {0, 1}, {0, -1}, {1, 0}, {-1, 0},
                    {1, 1}, {-1, 1}, {1, -1}, {-1, -1}
            };
            int cx = x + width / 2;
            int cy = y + height / 2;
            double speed = 5.0;

            for (double[] dir : directions) {
                double length = Math.sqrt(dir[0]*dir[0] + dir[1]*dir[1]);
                eggs.add(new Egg(cx, cy, (dir[0]/length)*speed, (dir[1]/length)*speed));
            }

            System.out.println("  Boss Level 8 shooting in 8 directions!");
        }

        return eggs;
    }

    // Returns if the boss can shoot or not

    @Override
    public boolean canShoot() {
        return alive && shootTimer >= shootInterval;
    }
}
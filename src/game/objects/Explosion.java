package game.objects;

import utils.ImageLoader;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Explosion {

    private int x, y;
    private int radius;
    private int maxRadius;
    private int life;
    private int maxLife;
    private boolean finished;
    private BufferedImage image1;
    private BufferedImage image2;
    private int frame;
    private int frameTimer;

    public Explosion(int x, int y) {
        this.x = x;
        this.y = y;
        this.radius = 5;
        this.maxRadius = 50;
        this.life = 0;
        this.maxLife = 20;
        this.finished = false;
        this.frame = 0;
        this.frameTimer = 0;

        this.image1 = ImageLoader.loadImage("airplan/Explosion.png");
        this.image2 = ImageLoader.loadImage("airplan/Explosion2.png");
    }

    // Updates the state

    public void update() {
        life++;
        radius = 5 + (life * (maxRadius - 5) / maxLife);
        frameTimer++;
        if (frameTimer > 3) {
            frameTimer = 0;
            frame = (frame + 1) % 2;
        }
        if (life >= maxLife) finished = true;
    }

    // Draws the bomb

    public void draw(Graphics2D g) {
        if (finished) return;

        if (image1 != null && image2 != null) {
            BufferedImage currentImage = (frame == 0) ? image1 : image2;
            int size = 20 + life * 2;
            g.drawImage(currentImage, x - size/2, y - size/2, size, size, null);
        }
        else {
            float alpha = 1.0f - (float)life / maxLife;
            for (int i = 0; i < 20; i++) {
                double angle = Math.random() * Math.PI * 2;
                double distance = Math.random() * radius;
                int px = x + (int)(distance * Math.cos(angle));
                int py = y + (int)(distance * Math.sin(angle));
                int size = 5 + (int)(Math.random() * 10);
                Color color = new Color(255, 150 + (int)(Math.random() * 105),
                        50 + (int)(Math.random() * 50), (int)(alpha * 255));
                g.setColor(color);
                g.fillOval(px - size/2, py - size/2, size, size);
            }
            g.setColor(new Color(255, 255, 255, (int)(alpha * 200)));
            g.fillOval(x - radius/4, y - radius/4, radius/2, radius/2);
        }
    }

    // Getters and Setters

    public boolean isFinished() {
        return finished;
    }
}
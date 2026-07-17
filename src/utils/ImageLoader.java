package utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageLoader {

    private static Map<String, BufferedImage> cache = new HashMap<>();

    public static BufferedImage loadImage(String path) {

        if (cache.containsKey(path)) {
            return cache.get(path);
        }
        try {
            File file = new File(path);
            if (file.exists()) {
                BufferedImage image = ImageIO.read(file);
                cache.put(path, image);
                return image;
            }
            else {
                System.err.println("Image not found: " + path);
                return null;
            }
        }
        catch (IOException e) {
            System.err.println("Could not load image: " + path);
            return null;
        }
    }
}
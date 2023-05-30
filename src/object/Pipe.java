package object;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Pipe {
    public BufferedImage image, plantImage;
    public int x;
    public boolean hasPlant = false;
    public int direction = 1; // 1: UP,  2: DOWN
    public int y;
    public int height;

    public Pipe() {
        try {
            plantImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/plant.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isOut() {
        return y < (10 - height) * 48;
    }
}

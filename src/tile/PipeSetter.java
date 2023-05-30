package tile;

import main.GamePanel;
import object.Pipe;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class PipeSetter {
    public java.util.List<Pipe> pipes = new ArrayList<>();
    GamePanel gp;
    BufferedImage image;

    public PipeSetter(GamePanel gp) {
        this.gp = gp;

        getImages();
        setPipes();
    }

    public void setPipes() {
        Pipe pipe1 = new Pipe();
        pipe1.x = 36 * gp.tileSize;
        pipe1.image = image;
        pipe1.height = 2;
        pipe1.y = (9 - pipe1.height) * gp.tileSize;
        pipes.add(pipe1);

        Pipe pipe2 = new Pipe();
        pipe2.x = 39 * gp.tileSize;
        pipe2.image = image;
        pipe2.height = 3;
        pipe2.hasPlant = true;
        pipe2.y = (9 - pipe2.height) * gp.tileSize;
        pipes.add(pipe2);
    }

    public void getImages() {
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/pipe.png")));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paint(Graphics2D g2D) {
        for (Pipe pipe : pipes) {
            if (pipe.hasPlant) {
                if (pipe.direction == 1) {
                    pipe.y--;
                    if (pipe.y + 18 == (9 - pipe.height) * gp.tileSize) {
                        pipe.direction = 2;
                    }
                } else {
                    pipe.y++;
                    if (pipe.y - 128 == (9 - pipe.height) * gp.tileSize) {
                        pipe.direction = 1;
                    }
                }
                g2D.drawImage(pipe.plantImage, pipe.x - gp.tileManager.worldShift+12, pipe.y, 72, 64, null);
            }
            g2D.drawImage(pipe.image, pipe.x - gp.tileManager.worldShift, (10 - pipe.height) * gp.tileSize, gp.tileSize * 2, gp.tileSize * pipe.height, null);
        }
    }
}

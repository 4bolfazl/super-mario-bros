package ir.sharif.math.ap2023.project.model.block;

import ir.sharif.math.ap2023.project.view.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FlagRod {
    int x, y;

    public FlagRod() {
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x + 24;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Rectangle getBounds() {
        return new Rectangle(
                x + 21,
                y,
                27,
                384
        );
    }

    public BufferedImage getImage() {
        return ImageLoader.getInstance().flagRod;
    }
}
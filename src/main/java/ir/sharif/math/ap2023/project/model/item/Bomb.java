package ir.sharif.math.ap2023.project.model.item;

import ir.sharif.math.ap2023.project.model.player.Player;
import ir.sharif.math.ap2023.project.view.ImageLoader;
import ir.sharif.math.ap2023.project.view.UIManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bomb extends Item {

    public Bomb(double x, double y) {
        super(x, y);
        gravity = 0.08;
    }

    @Override
    public Rectangle getTopBounds() {
        return new Rectangle(
                (int) x,
                (int) y,
                UIManager.getInstance().getTileSize(),
                UIManager.getInstance().getTileSize()
        );
    }

    @Override
    public BufferedImage getImage() {
        return ImageLoader.getInstance().bomb;
    }

    @Override
    public void acquired(Player player) {

    }
}

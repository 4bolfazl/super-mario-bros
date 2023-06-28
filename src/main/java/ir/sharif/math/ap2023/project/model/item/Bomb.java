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
        falling = true;
        jumping = false;
        speedY = 6;
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

    @Override
    public void updateLocation() {
        if (jumping && speedY <= 0) {
            jumping = false;
            falling = true;
        } else if (jumping) {
            speedY -= gravity;
            y -= speedY;
        }

        if (falling) {
            y += speedY;
            speedY += gravity;
        }
    }
}

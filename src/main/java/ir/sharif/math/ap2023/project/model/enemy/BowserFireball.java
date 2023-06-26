package ir.sharif.math.ap2023.project.model.enemy;

import ir.sharif.math.ap2023.project.model.player.Fireball;
import ir.sharif.math.ap2023.project.view.ImageLoader;
import ir.sharif.math.ap2023.project.view.UIManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BowserFireball extends Fireball {
    public BowserFireball(double x, double y, boolean toRight) {
        super(x, y, toRight);
    }

    public void updateLocation() {
        x += speedX;
    }

    @Override
    public BufferedImage getImage() {
        return ImageLoader.getInstance().fireballImages[2];
    }

    @Override
    public Rectangle getRightBounds() {
        return new Rectangle(
                (int) x,
                (int) y,
                2 * UIManager.getInstance().getTileSize(),
                2 * UIManager.getInstance().getTileSize()
        );
    }

    @Override
    public Rectangle getLeftBounds() {
        return new Rectangle(
                (int) x,
                (int) y,
                2 * UIManager.getInstance().getTileSize(),
                2 * UIManager.getInstance().getTileSize()
        );
    }

    public Rectangle getBoundsForPlayer() {
        return new Rectangle(
                (int) x,
                (int) y,
                2 * UIManager.getInstance().getTileSize(),
                2 * UIManager.getInstance().getTileSize()+24
        );
    }
}

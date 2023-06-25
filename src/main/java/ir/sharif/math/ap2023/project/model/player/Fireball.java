package ir.sharif.math.ap2023.project.model.player;

import ir.sharif.math.ap2023.project.view.ImageLoader;
import ir.sharif.math.ap2023.project.view.UIManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Fireball {
    int x, y;
    int startPosition;
    int speedX;
    int distanceTraveled = 0;
    boolean toRight;
    boolean determined = false;
    boolean destroyed = false;

    public Fireball(int x, int y, boolean toRight) {
        this.x = x;
        this.y = y;
        this.startPosition = x;
        this.toRight = toRight;
        this.speedX = (toRight) ? 4 : -4;
    }

    public void updateLocation() {
        x += speedX;
        distanceTraveled += 3;
        if (distanceTraveled >= 4 * UIManager.getInstance().getTileSize()) {
            destroyed = true;
        }
    }

    public BufferedImage getImage() {
        if (toRight)
            return ImageLoader.getInstance().fireballImages[1];
        return ImageLoader.getInstance().fireballImages[0];
    }

    public Rectangle getRightBounds() {
        return new Rectangle(
                x,
                y + UIManager.getInstance().getTileSize() / 4,
                UIManager.getInstance().getTileSize(),
                UIManager.getInstance().getTileSize() / 4
        );
    }

    public Rectangle getLeftBounds() {
        return new Rectangle(
                x,
                y + UIManager.getInstance().getTileSize() / 4,
                UIManager.getInstance().getTileSize(),
                UIManager.getInstance().getTileSize() / 4
        );
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public boolean isToRight() {
        return toRight;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public boolean isDetermined() {
        return determined;
    }

    public void setDetermined(boolean determined) {
        this.determined = determined;
    }
}

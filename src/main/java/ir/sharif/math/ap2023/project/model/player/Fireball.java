package ir.sharif.math.ap2023.project.model.player;

import ir.sharif.math.ap2023.project.view.ImageLoader;
import ir.sharif.math.ap2023.project.view.UIManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Fireball {
    protected double x, y;
    protected double startPosition;
    public int speedX;
    protected int distanceTraveled = 0;
    protected boolean toRight;
    protected boolean determined = false;
    protected boolean destroyed = false;

    public Fireball(double x, double y, boolean toRight) {
        this.x = x;
        this.y = y;
        this.startPosition = x;
        this.toRight = toRight;
        this.speedX = (toRight) ? 4 : -4;
    }

    public Fireball() {

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
                (int) x,
                (int) y + UIManager.getInstance().getTileSize() / 4,
                UIManager.getInstance().getTileSize(),
                UIManager.getInstance().getTileSize() / 4
        );
    }

    public Rectangle getLeftBounds() {
        return new Rectangle(
                (int) x,
                (int) y + UIManager.getInstance().getTileSize() / 4,
                UIManager.getInstance().getTileSize(),
                UIManager.getInstance().getTileSize() / 4
        );
    }

    public double getX() {
        return x;
    }

    public double getY() {
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

    public double getStartPosition() {
        return startPosition;
    }

    public boolean isDetermined() {
        return determined;
    }

    public void setDetermined(boolean determined) {
        this.determined = determined;
    }
}

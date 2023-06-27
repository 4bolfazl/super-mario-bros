package ir.sharif.math.ap2023.project.model.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sharif.math.ap2023.project.view.ImageLoader;
import ir.sharif.math.ap2023.project.view.UIManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Fireball {
    public int speedX;
    protected double x, y;
    protected double startPosition;
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

    @JsonIgnore
    public BufferedImage getImage() {
        if (toRight)
            return ImageLoader.getInstance().fireballImages[1];
        return ImageLoader.getInstance().fireballImages[0];
    }

    @JsonIgnore
    public Rectangle getRightBounds() {
        return new Rectangle(
                (int) x,
                (int) y + UIManager.getInstance().getTileSize() / 4,
                UIManager.getInstance().getTileSize(),
                UIManager.getInstance().getTileSize() / 4
        );
    }

    @JsonIgnore
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

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
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

    public void setToRight(boolean toRight) {
        this.toRight = toRight;
    }

    public double getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(double startPosition) {
        this.startPosition = startPosition;
    }

    public boolean isDetermined() {
        return determined;
    }

    public void setDetermined(boolean determined) {
        this.determined = determined;
    }

    public int getSpeedX() {
        return speedX;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public int getDistanceTraveled() {
        return distanceTraveled;
    }

    public void setDistanceTraveled(int distanceTraveled) {
        this.distanceTraveled = distanceTraveled;
    }
}

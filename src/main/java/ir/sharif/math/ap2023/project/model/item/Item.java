package ir.sharif.math.ap2023.project.model.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ir.sharif.math.ap2023.project.model.player.Player;
import ir.sharif.math.ap2023.project.view.UIManager;

import java.awt.*;
import java.awt.image.BufferedImage;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "subclass")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Coin.class, name = "coinItem"),
        @JsonSubTypes.Type(value = Flower.class, name = "flowerItem"),
        @JsonSubTypes.Type(value = Mushroom.class, name = "mushroomItem"),
        @JsonSubTypes.Type(value = Star.class, name = "starItem")
})
public abstract class Item {
    double gravity = 0.45;
    double speedX = 0, speedY = 10;
    double x, y;
    boolean falling = false;
    boolean jumping = true;
    boolean toRight = true;
    boolean shouldBeCollisionChecked = false;
    int waitTime = 0;

    public Item() {
    }

    public Item(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @JsonIgnore
    public abstract BufferedImage getImage();

    public void updateLocation() {
        if (jumping && speedY <= 0) {
            setShouldBeCollisionChecked(true);
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

        if (this instanceof Star || this instanceof Mushroom)
            x += speedX;
    }

    public boolean isToRight() {
        return toRight;
    }

    public void setToRight(boolean toRight) {
        this.toRight = toRight;
    }

    public double getSpeedX() {
        return speedX;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public void addWaitTime() {
        this.waitTime++;
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

    public boolean isFalling() {
        return falling;
    }

    public void setFalling(boolean falling) {
        this.falling = falling;
    }

    public boolean isShouldBeCollisionChecked() {
        return shouldBeCollisionChecked;
    }

    public void setShouldBeCollisionChecked(boolean shouldBeCollisionChecked) {
        this.shouldBeCollisionChecked = shouldBeCollisionChecked;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    @JsonIgnore
    public Rectangle getTopBounds() {
        return new Rectangle(
                (int) x,
                (int) y,
                UIManager.getInstance().getTileSize(),
                UIManager.getInstance().getTileSize() / 2
        );
    }

    @JsonIgnore
    public Rectangle getBottomBounds() {
        return new Rectangle(
                (int) (x + UIManager.getInstance().getTileSize() / 4),
                (int) (y + UIManager.getInstance().getTileSize() / 2),
                UIManager.getInstance().getTileSize() / 2,
                UIManager.getInstance().getTileSize() / 2
        );
    }

    @JsonIgnore
    public Rectangle getRightBounds() {
        return new Rectangle(
                (int) (x + 3 * UIManager.getInstance().getTileSize() / 4),
                (int) (y + UIManager.getInstance().getTileSize() / 10),
                UIManager.getInstance().getTileSize() / 4,
                4 * UIManager.getInstance().getTileSize() / 5
        );
    }

    @JsonIgnore
    public Rectangle getLeftBounds() {
        return new Rectangle(
                (int) x,
                (int) (y + UIManager.getInstance().getTileSize() / 10),
                UIManager.getInstance().getTileSize() / 4,
                4 * UIManager.getInstance().getTileSize() / 5
        );
    }

    public double getGravity() {
        return gravity;
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    public abstract void acquired(Player player);
}

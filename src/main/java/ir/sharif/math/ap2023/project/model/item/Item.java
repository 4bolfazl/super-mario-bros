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
    @JsonIgnore
    double gravity = 0.45;
    @JsonIgnore
    double speedX = 0, speedY = 10;
    @JsonIgnore
    double x, y;
    @JsonIgnore
    boolean falling = false;
    @JsonIgnore
    boolean jumping = true;
    @JsonIgnore
    boolean toRight = true;
    @JsonIgnore
    boolean shouldBeCollisionChecked = false;
    @JsonIgnore
    int waitTime = 0;

    public Item() {
    }

    public Item(double x, double y) {
        this.x = x;
        this.y = y;
    }

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

    @JsonIgnore
    public boolean isToRight() {
        return toRight;
    }

    @JsonIgnore
    public void setToRight(boolean toRight) {
        this.toRight = toRight;
    }

    @JsonIgnore
    public double getSpeedX() {
        return speedX;
    }

    @JsonIgnore
    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    @JsonIgnore
    public double getSpeedY() {
        return speedY;
    }

    @JsonIgnore
    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    @JsonIgnore
    public int getWaitTime() {
        return waitTime;
    }

    @JsonIgnore
    public void addWaitTime() {
        this.waitTime++;
    }

    @JsonIgnore
    public double getX() {
        return x;
    }

    @JsonIgnore
    public void setX(double x) {
        this.x = x;
    }

    @JsonIgnore
    public double getY() {
        return y;
    }

    @JsonIgnore
    public void setY(double y) {
        this.y = y;
    }

    @JsonIgnore
    public boolean isFalling() {
        return falling;
    }

    @JsonIgnore
    public void setFalling(boolean falling) {
        this.falling = falling;
    }

    @JsonIgnore
    public boolean isShouldBeCollisionChecked() {
        return shouldBeCollisionChecked;
    }

    @JsonIgnore
    public void setShouldBeCollisionChecked(boolean shouldBeCollisionChecked) {
        this.shouldBeCollisionChecked = shouldBeCollisionChecked;
    }

    @JsonIgnore
    public boolean isJumping() {
        return jumping;
    }

    @JsonIgnore
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

    public abstract void acquired(Player player);
}

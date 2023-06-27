package ir.sharif.math.ap2023.project.model.enemy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ir.sharif.math.ap2023.project.view.UIManager;

import java.awt.*;
import java.awt.image.BufferedImage;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "subclass")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Bowser.class, name = "bowserEnemy"),
        @JsonSubTypes.Type(value = Goompa.class, name = "goompaEnemy"),
        @JsonSubTypes.Type(value = Koopa.class, name = "koopaEnemy"),
        @JsonSubTypes.Type(value = NukeBird.class, name = "nukebirdEnemy"),
        @JsonSubTypes.Type(value = Spiny.class, name = "spinyEnemy"),
})
public abstract class EnemyObject implements Cloneable {
    public double defaultSpeed = 1;
    int x, y;
    EnemyType type;
    int frame = 0;
    boolean dead = false;
    @JsonIgnore
    Rectangle solidArea = new Rectangle();
    double gravity = 0.9;
    boolean falling = false;
    boolean jumping = false;
    boolean toRight = true;
    double speedX = 1, speedY = 10;

    public EnemyObject(int x, int y, EnemyType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public EnemyObject() {
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        if (y == 0)
            this.y = 9;
        else
            this.y = 10 - y;
    }

    public EnemyType getType() {
        return type;
    }

    public void setType(EnemyType type) {
        this.type = type;
    }

    public void addFrame() {
        frame++;
    }

    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    @JsonIgnore

    public Rectangle getSolidArea() {
        return solidArea;
    }

    public void setSolidArea(Rectangle solidArea) {
        this.solidArea = solidArea;
    }

    public boolean isFalling() {
        return falling;
    }

    public void setFalling(boolean falling) {
        this.falling = falling;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
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

    public void updateLocation() {
        if (!isDead()) {
            if (jumping && speedY <= 0) {
                jumping = false;
                falling = true;
            } else if (jumping) {
                speedY -= gravity;
                getSolidArea().y -= speedY;
            }

            if (falling) {
                getSolidArea().y += speedY;
                speedY += gravity;
            }

            if (this instanceof Koopa && ((Koopa) this).isFreeze()) {
                if (Math.abs(speedX) < gravity)
                    speedX = 0;
                else {
                    if (speedX > 0)
                        speedX -= gravity / 5;
                    else
                        speedX += gravity / 5;
                }
            }
            getSolidArea().x += speedX;
        }
    }

    @JsonIgnore
    public Rectangle getBottomBounds() {
        return new Rectangle(
                (int) (getSolidArea().x + UIManager.getInstance().getTileSize() / 4),
                (int) (getSolidArea().y + UIManager.getInstance().getTileSize() / 2),
                UIManager.getInstance().getTileSize() / 2,
                UIManager.getInstance().getTileSize() / 2
        );
    }

    @JsonIgnore
    public Rectangle getTopBounds() {
        return new Rectangle(
                (int) getSolidArea().x + UIManager.getInstance().getTileSize() / 6,
                (int) getSolidArea().y,
                2 * UIManager.getInstance().getTileSize() / 3,
                UIManager.getInstance().getTileSize() / 2
        );
    }

    @JsonIgnore
    public Rectangle getRightBounds() {
        return new Rectangle(
                (int) (getSolidArea().x + 3 * UIManager.getInstance().getTileSize() / 4),
                (int) (getSolidArea().y + UIManager.getInstance().getTileSize() / 10),
                UIManager.getInstance().getTileSize() / 4,
                4 * UIManager.getInstance().getTileSize() / 5
        );
    }

    @JsonIgnore
    public Rectangle getLeftBounds() {
        return new Rectangle(
                (int) getSolidArea().x,
                (int) (getSolidArea().y + UIManager.getInstance().getTileSize() / 10),
                UIManager.getInstance().getTileSize() / 4,
                4 * UIManager.getInstance().getTileSize() / 5
        );
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public double getDefaultSpeed() {
        return defaultSpeed;
    }

    public void setDefaultSpeed(double defaultSpeed) {
        this.defaultSpeed = defaultSpeed;
    }

    public double getGravity() {
        return gravity;
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    @JsonIgnore
    public abstract BufferedImage getImage();

    public abstract void kill();

    @Override
    public EnemyObject clone() {
        try {
            EnemyObject clone = (EnemyObject) super.clone();
            if (clone.y == 9)
                clone.y = 0;
            else
                clone.y = 10 - clone.y;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

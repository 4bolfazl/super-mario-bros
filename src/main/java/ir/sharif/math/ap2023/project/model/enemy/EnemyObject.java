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
public abstract class EnemyObject {
    int x, y;
    EnemyType type;
    @JsonIgnore
    int frame = 0;
    @JsonIgnore
    Rectangle solidArea = new Rectangle();
    @JsonIgnore
    double gravity = 0.9;
    @JsonIgnore
    boolean falling = false;
    @JsonIgnore
    boolean jumping = false;
    @JsonIgnore
    boolean toRight = true;
    @JsonIgnore
    double speedX = 2, speedY = 10;

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

    @JsonIgnore
    public int getFrame() {
        return frame;
    }

    @JsonIgnore
    public void setFrame(int frame) {
        this.frame = frame;
    }

    @JsonIgnore
    public Rectangle getSolidArea() {
        return solidArea;
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
    public boolean isJumping() {
        return jumping;
    }

    @JsonIgnore
    public void setJumping(boolean jumping) {
        this.jumping = jumping;
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

    public void updateLocation() {
        if (jumping && speedY <= 0) {
            jumping = false;
            falling = true;
        } else if (jumping) {
            speedY -= gravity;
//            y -= speedY;
            getSolidArea().y -= speedY;
        }

        if (falling) {
//            y += speedY;
            getSolidArea().y += speedY;
            speedY += gravity;
        }

//        x += speedX;
        getSolidArea().x += speedX;

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

    public abstract BufferedImage getImage();
}

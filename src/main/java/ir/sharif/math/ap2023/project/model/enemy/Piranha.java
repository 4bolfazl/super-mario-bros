package ir.sharif.math.ap2023.project.model.enemy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sharif.math.ap2023.project.view.UIManager;

import java.awt.*;

public class Piranha {
    private int x, y;
    private boolean alive = true;
    private int coolDown = 0;
    private int outTime = 0;
    private double speedY = 1;
    private int frame = 0;

    public void updateLocation() {
        coolDown++;
        if (coolDown >= 90) {
            y -= speedY;
            outTime++;
            if (outTime >= 90) {
                outTime = 0;
                reverseSpeed();
            }
            if (coolDown >= 270)
                coolDown = 0;
        }
    }

    public void reverseSpeed() {
        speedY *= -1;
    }

    @JsonIgnore
    public Rectangle getSolidArea() {
        return new Rectangle(
                x,
                y,
                UIManager.getInstance().getTileSize(),
                77
        );
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
        this.y = y + 9;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public int getCoolDown() {
        return coolDown;
    }

    public void setCoolDown(int coolDown) {
        this.coolDown = coolDown;
    }

    public void addCoolDown() {
        this.coolDown++;
    }

    public int getOutTime() {
        return outTime;
    }

    public void setOutTime(int outTime) {
        this.outTime = outTime;
    }

    public void addOutTime() {
        this.outTime++;
    }

    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    public void addFrame() {
        this.frame++;
    }

    public double getSpeedY() {
        return speedY;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }
}

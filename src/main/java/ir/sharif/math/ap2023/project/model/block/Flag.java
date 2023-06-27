package ir.sharif.math.ap2023.project.model.block;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sharif.math.ap2023.project.controller.GameEngine;
import ir.sharif.math.ap2023.project.controller.GameState;
import ir.sharif.math.ap2023.project.view.ImageLoader;
import ir.sharif.math.ap2023.project.view.UIManager;

import java.awt.image.BufferedImage;

public class Flag {
    public int frame = 0;
    int x, y;
    int speedY = 2;
    boolean finished = false;
    boolean triggered = false;
    FlagRod flagRod;

    public Flag() {
        flagRod = new FlagRod();
    }

    @JsonIgnore
    public BufferedImage getImage() {
        return ImageLoader.getInstance().flag[frame / 15];
    }

    public void updateLocation() {
        if (triggered) {
            if (!finished) {
                y += speedY;
                if (y >= 9 * UIManager.getInstance().getTileSize()) {
                    finished = true;
                    GameEngine.getInstance().getPlayer().nextLevel();
                    GameEngine.getInstance().setGameState(GameState.PLAYING);
                }
            }
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isTriggered() {
        return triggered;
    }

    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x * UIManager.getInstance().getTileSize();
        flagRod.setX(this.x);
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = (9 - y) * UIManager.getInstance().getTileSize();
        flagRod.setY(this.y);
        this.y += 48;
    }

    public FlagRod getFlagRod() {
        return flagRod;
    }

    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    public int getSpeedY() {
        return speedY;
    }

    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void setFlagRod(FlagRod flagRod) {
        this.flagRod = flagRod;
    }
}

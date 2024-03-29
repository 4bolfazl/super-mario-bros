package ir.sharif.math.ap2023.project.model.enemy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sharif.math.ap2023.project.controller.GameEngine;
import ir.sharif.math.ap2023.project.controller.sound.SoundEffectType;
import ir.sharif.math.ap2023.project.controller.sound.SoundManager;
import ir.sharif.math.ap2023.project.view.ImageLoader;
import ir.sharif.math.ap2023.project.view.UIManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Koopa extends EnemyObject {
    boolean freeze = false;
    int freezeFrame = 0;
    @JsonIgnore
    BufferedImage[] images = ImageLoader.getInstance().getEnemyImages(EnemyType.KOOPA);

    public Koopa(int x, int y, EnemyType type) {
        super(x, y, type);
    }

    public Koopa() {
        solidArea.setSize(UIManager.getInstance().getTileSize(), 72);
    }

    @JsonIgnore
    public int getFreezeFrame() {
        return freezeFrame;
    }

    @JsonIgnore
    public void setFreezeFrame(int freezeFrame) {
        this.freezeFrame = freezeFrame;
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        solidArea.x = x * UIManager.getInstance().getTileSize();
    }

    @Override
    public void setY(int y) {
        super.setY(y);
        solidArea.y = this.y * UIManager.getInstance().getTileSize() - 24;
    }

    @Override
    public BufferedImage getImage() {
        if (isFreeze()) {
            if (!isDead()) {
                freezeFrame++;
                if (freezeFrame >= 180) {
                    setFreeze(false);
                    freezeFrame = 0;
                    setSpeedX(defaultSpeed);
                    setToRight(true);
                    frame = 0;
                    return images[2];
                }
                return images[4];
            } else {
                return images[5];
            }
        }
        addFrame();
        if (frame >= 30) {
            frame = 0;
        }
        return images[((isToRight()) ? 2 : 0) + frame / 15];
    }

    @Override
    public void kill() {
        SoundManager.getInstance().playSoundEffect(SoundEffectType.SQUISH);
        if (!isFreeze()) {
            setSpeedX((GameEngine.getInstance().randomGenerator.nextBoolean() ? -7 : 7));
            setSpeedY(0);
            setFreeze(true);
        } else {
            GameEngine.getInstance().getPlayer().addPoints(2);
            setSpeedX(0);
            setSpeedY(0);
            setDead(true);
        }
    }

    @Override
    public Rectangle getBottomBounds() {
        return new Rectangle(
                (int) (getSolidArea().x + UIManager.getInstance().getTileSize() / 4),
                (int) (getSolidArea().y + 24 + UIManager.getInstance().getTileSize() / 2),
                UIManager.getInstance().getTileSize() / 2,
                UIManager.getInstance().getTileSize() / 2
        );
    }

    public boolean isFreeze() {
        return freeze;
    }

    public void setFreeze(boolean freeze) {
        this.freeze = freeze;
    }
}

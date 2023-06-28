package ir.sharif.math.ap2023.project.model.enemy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sharif.math.ap2023.project.controller.sound.SoundEffectType;
import ir.sharif.math.ap2023.project.controller.sound.SoundManager;
import ir.sharif.math.ap2023.project.model.item.Bomb;
import ir.sharif.math.ap2023.project.view.ImageLoader;
import ir.sharif.math.ap2023.project.view.UIManager;

import java.awt.image.BufferedImage;

public class NukeBird extends EnemyObject {
    @JsonIgnore
    BufferedImage[] images = ImageLoader.getInstance().getEnemyImages(EnemyType.NUKEBIRD);
    int bombTimer = 0;
    @JsonIgnore
    Bomb bomb;

    public NukeBird(int x, int y, EnemyType type) {
        super(x, y, type);
    }

    public NukeBird() {
        solidArea.setSize(42, 36);
        gravity = 0;
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        solidArea.x = x * UIManager.getInstance().getTileSize();
    }

    @Override
    public void setY(int y) {
        super.setY(y);
        solidArea.y = this.y * UIManager.getInstance().getTileSize();
    }

    @Override
    public BufferedImage getImage() {
        addFrame();
        if (frame >= 30) {
            frame = 0;
        }
        bombTimer++;
        if (bombTimer >= 250) {
            throwBomb();
            bombTimer = 0;
        }
        return images[((isToRight()) ? 2 : 0) + frame / 15];
    }

    @Override
    public void kill() {
        SoundManager.getInstance().playSoundEffect(SoundEffectType.SQUISH);
    }

    public int getBombTimer() {
        return bombTimer;
    }

    public void setBombTimer(int bombTimer) {
        this.bombTimer = bombTimer;
    }

    @JsonIgnore
    public Bomb getBomb() {
        return bomb;
    }

    @JsonIgnore
    public void setBomb(Bomb bomb) {
        this.bomb = bomb;
    }

    private void throwBomb() {
        bomb = new Bomb(getSolidArea().x, getSolidArea().y);
    }
}

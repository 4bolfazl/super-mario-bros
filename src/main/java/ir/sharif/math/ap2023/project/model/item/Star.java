package ir.sharif.math.ap2023.project.model.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sharif.math.ap2023.project.controller.sound.BackgroundMusicType;
import ir.sharif.math.ap2023.project.controller.sound.SoundManager;
import ir.sharif.math.ap2023.project.model.player.Player;
import ir.sharif.math.ap2023.project.view.ImageLoader;

import java.awt.image.BufferedImage;

public class Star extends Item {
    int jumpTime = 0;
    @JsonIgnore
    BufferedImage image = ImageLoader.getInstance().getItemImage(ItemType.STAR);

    public Star() {
    }

    public Star(double x, double y) {
        super(x, y);
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }

    @Override
    public void acquired(Player player) {
        player.addPoints(40);
        player.setInvincible(true);
        SoundManager soundManager = SoundManager.getInstance();
        soundManager.pauseMusic();
        soundManager.playBackgroundMusic(BackgroundMusicType.INVINCIBLE);
        player.upgradeState();
        player.updateSolidArea();
    }

    public int getJumpTime() {
        return jumpTime;
    }

    public void setJumpTime(int jumpTime) {
        this.jumpTime = jumpTime;
    }

    public void addJumpTime() {
        jumpTime++;
    }
}

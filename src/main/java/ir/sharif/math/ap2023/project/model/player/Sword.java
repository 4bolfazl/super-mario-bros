package ir.sharif.math.ap2023.project.model.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sharif.math.ap2023.project.view.UIManager;

import java.awt.*;

public class Sword extends Fireball {
    public boolean released = false;
    @JsonIgnore
    Player player;
    boolean onBack = false;

    public Sword(Player player) {
        super();
        this.player = player;
        this.speedX = 0;
    }

    public Sword() {
    }

    @JsonIgnore
    public Rectangle getBounds(Player player) {
        return new Rectangle(
                (int) x, (int) player.getY() - 12,
                96, 36
        );
    }

    @Override
    public void updateLocation() {
        if (released) {
            x += speedX;
            distanceTraveled += 3;
            if (!onBack) {
                if (distanceTraveled >= 3.4 * UIManager.getInstance().getTileSize()) {
                    speedX *= -1;
                    distanceTraveled = 0;
                    onBack = true;

                }
            } else {
                if (speedX > 0) {
                    if (x + 96 >= player.getX())
                        destroyed = true;
                } else {
                    if (player.getX() + 48 >= x) {
                        destroyed = true;
                    }
                }
            }
        }
    }

    public boolean isReleased() {
        return released;
    }

    public void setReleased(boolean released) {
        this.released = released;
    }

    public boolean isOnBack() {
        return onBack;
    }

    public void setOnBack(boolean onBack) {
        this.onBack = onBack;
    }
}

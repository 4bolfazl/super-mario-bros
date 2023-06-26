package ir.sharif.math.ap2023.project.model.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sharif.math.ap2023.project.view.UIManager;

import java.awt.*;

public class Sword extends Fireball {
    @JsonIgnore
    public boolean released = false;
    Player player;
    @JsonIgnore
    boolean onBack = false;

    public Sword(Player player) {
        super();
        this.player = player;
        this.speedX = 0;
    }

    public Rectangle getBounds(Player player) {
        return new Rectangle(
                (int) x, (int) player.getY()-12,
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
}

package ir.sharif.math.ap2023.project.model.enemy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sharif.math.ap2023.project.controller.GameEngine;
import ir.sharif.math.ap2023.project.model.player.Player;
import ir.sharif.math.ap2023.project.view.ImageLoader;
import ir.sharif.math.ap2023.project.view.UIManager;

import java.awt.image.BufferedImage;

public class Spiny extends EnemyObject {
    @JsonIgnore
    float acceleration = 0.05f;
    @JsonIgnore
    BufferedImage[] images = ImageLoader.getInstance().getEnemyImages(EnemyType.SPINY);

    public Spiny(int x, int y, EnemyType type) {
        super(x, y, type);
    }

    public Spiny() {
        solidArea.setSize(UIManager.getInstance().getTileSize(), UIManager.getInstance().getTileSize());
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
        if (isDead()) {
            return images[4];
        }
        addFrame();
        if (frame >= 30) {
            frame = 0;
        }
        return images[((isToRight()) ? 2 : 0) + frame / 15];
    }

    @Override
    public void updateLocation() {
        Player player = GameEngine.getInstance().getPlayer();
        double distance = Math.sqrt(Math.pow(player.getX() - solidArea.x, 2) + Math.pow(player.getY() - solidArea.y, 2));
        if (distance > 5 * UIManager.getInstance().getTileSize()) {
            speedX = isToRight() ? defaultSpeed : -defaultSpeed;
        } else if (Math.abs(solidArea.y - (player.getY() + (player.getCharacterState() > 0 ? UIManager.getInstance().getTileSize() : 0))) <= 5) {
            if (player.getX() > getSolidArea().x) {
                setToRight(true);
                if (speedX > 0) {
                    speedX += acceleration;
                } else {
                    speedX = defaultSpeed;
                    speedX += acceleration;
                }
            } else {
                setToRight(false);
                if (speedX < 0) {
                    speedX -= acceleration;
                } else {
                    speedX = -defaultSpeed;
                    speedX -= acceleration;
                }
            }
        }
        super.updateLocation();
//        if (jumping && speedY <= 0) {
//            jumping = false;
//            falling = true;
//        } else if (jumping) {
//            speedY -= gravity;
//            getSolidArea().y -= speedY;
//        }
//
//        if (falling) {
//            getSolidArea().y += speedY;
//            speedY += gravity;
//        }
//
//        getSolidArea().x += speedX;
    }

    @Override
    public void kill() {
        GameEngine.getInstance().getPlayer().addPoints(1);
        setSpeedX(0);
        setSpeedY(0);
        setDead(true);
    }
}

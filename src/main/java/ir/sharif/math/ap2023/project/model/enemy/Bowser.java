package ir.sharif.math.ap2023.project.model.enemy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sharif.math.ap2023.project.controller.GameEngine;
import ir.sharif.math.ap2023.project.controller.GameLoader;
import ir.sharif.math.ap2023.project.model.block.BlockType;
import ir.sharif.math.ap2023.project.model.block.EmptyBlockObject;
import ir.sharif.math.ap2023.project.model.game.SectionObject;
import ir.sharif.math.ap2023.project.model.player.Fireball;
import ir.sharif.math.ap2023.project.model.player.Player;
import ir.sharif.math.ap2023.project.view.ImageLoader;
import ir.sharif.math.ap2023.project.view.UIManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bowser extends EnemyObject {
    @JsonIgnore
    int grabCoolDown = 4 * 60;
    @JsonIgnore
    int jumpCoolDown = 3 * 60;
    @JsonIgnore
    int fireballCoolDown = 2 * 60;
    @JsonIgnore
    int nukeCoolDown = 3 * 60;
    @JsonIgnore
    int HP = 20;
    @JsonIgnore
    int waitOnAir = 0;
    @JsonIgnore
    boolean triggered = false;
    @JsonIgnore
    BufferedImage[] images = ImageLoader.getInstance().getEnemyImages(EnemyType.BOWSER);
    @JsonIgnore
    double gravity = 1.1;

    public Bowser(int x, int y, EnemyType type) {
        super(x, y, type);
    }

    public Bowser() {
        solidArea.setSize(5 * UIManager.getInstance().getTileSize(), 5 * UIManager.getInstance().getTileSize());
        setToRight(false);
        speedX = 0;
    }

    @Override
    public void updateLocation() {
        if (!triggered) {
            if (distanceFromMario() <= 7 * UIManager.getInstance().getTileSize()) {
                Player player = GameEngine.getInstance().getPlayer();
                SectionObject sectionObject = GameLoader.getInstance("config.json").getGame().getLevels().get(player.getLevel() - 1).getSections().get(player.getSection() - 1);
                for (int i = 0; i < 10; i++) {
                    sectionObject.getBlocks().add(new EmptyBlockObject(0, i, BlockType.EMPTY));
                    sectionObject.getBlocks().add(new EmptyBlockObject(25, i, BlockType.EMPTY));
                }
                triggered = true;
            }
        } else {
            if (!isDead()) {
                Player player = GameEngine.getInstance().getPlayer();
                for (Fireball fireball : player.getFireballs()) {
                    if (waitOnAir == 0 && solidArea.y >= 240) {
                        if (fireball.isToRight()) {
                            if (solidArea.x - fireball.getX() <= 96) {
                                if (getSpeedY() == 0) {
                                    setSpeedY(16);
                                    setJumping(true);
                                }
                            }
                        } else {
                            if (fireball.getX() - (solidArea.x + 5 * UIManager.getInstance().getTileSize()) <= 96) {
                                if (getSpeedY() == 0) {
                                    setSpeedY(16);
                                    setJumping(true);
                                }
                            }
                        }
                    }
                }
                if (jumping && speedY <= 0) {
                    if (waitOnAir == 0)
                        speedY = 0;
                    waitOnAir++;
                    if (waitOnAir >= 60) {
                        waitOnAir = 0;
                        jumping = false;
                        falling = true;
                    }
                } else if (jumping) {
                    speedY -= gravity;
                    getSolidArea().y -= speedY;
                }

                if (falling) {
                    getSolidArea().y += speedY;
                    speedY += gravity;
                }

                getSolidArea().x += speedX;
            }
        }
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        solidArea.x = x * UIManager.getInstance().getTileSize();
    }

    @Override
    public void setY(int y) {
        super.setY(y);
        solidArea.y = (this.y - 4) * UIManager.getInstance().getTileSize();
    }

    @Override
    public Rectangle getBottomBounds() {
        return new Rectangle(
                solidArea.x + UIManager.getInstance().getTileSize() / 8,
                solidArea.y + 19 * UIManager.getInstance().getTileSize() / 4,
                19 * UIManager.getInstance().getTileSize() / 4,
                UIManager.getInstance().getTileSize() / 4
        );
    }

    @Override
    public Rectangle getTopBounds() {
        return new Rectangle(
                getSolidArea().x + UIManager.getInstance().getTileSize() / 8,
                getSolidArea().y,
                19 * UIManager.getInstance().getTileSize() / 4,
                UIManager.getInstance().getTileSize() / 4
        );
    }

    @Override
    public Rectangle getRightBounds() {
        return new Rectangle(
                getSolidArea().x + 19 * UIManager.getInstance().getTileSize() / 4,
                getSolidArea().y + UIManager.getInstance().getTileSize() / 10,
                UIManager.getInstance().getTileSize() / 4,
                24 * UIManager.getInstance().getTileSize() / 5
        );
    }

    @Override
    public Rectangle getLeftBounds() {
        return new Rectangle(
                getSolidArea().x,
                getSolidArea().y + UIManager.getInstance().getTileSize() / 10,
                UIManager.getInstance().getTileSize() / 4,
                24 * UIManager.getInstance().getTileSize() / 5
        );
    }

    @Override
    public BufferedImage getImage() {
        if (triggered)
            addFrame();
        if (frame >= 30) {
            frame = 0;
        }
        return images[((isToRight()) ? 9 : 0) + frame / 15];
    }

    public void decreaseHP(int damage) {
        HP -= damage;
    }

    @JsonIgnore
    public int getHP() {
        return HP;
    }

    @JsonIgnore
    public void setHP(int HP) {
        this.HP = HP;
    }

    public BufferedImage[] getImages() {
        return images;
    }

    public void setImages(BufferedImage[] images) {
        this.images = images;
    }

    public boolean isTriggered() {
        return triggered;
    }

    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }

    public double distanceFromMario() {
        Player player = GameEngine.getInstance().getPlayer();
        return Math.sqrt(Math.pow(player.getX() - solidArea.x, 2) + Math.pow(player.getY() - solidArea.y, 2));
    }

    public void resetGrabCoolDown() {
        grabCoolDown = 4 * 60;
    }

    public void resetJumpCoolDown() {
        jumpCoolDown = 3 * 60;
    }

    public void resetFireballCoolDown() {
        fireballCoolDown = 2 * 60;
    }

    public void resetNukeCoolDown() {
        nukeCoolDown = 3 * 60;
    }

    public void decreaseGrabAttack() {
        grabCoolDown--;
    }

    public void decreaseJumpAttack() {
        jumpCoolDown--;
    }

    public void decreaseFireballAttack() {
        fireballCoolDown--;
    }

    public void decreaseNukeAttack() {
        nukeCoolDown--;
    }

    @Override
    public void kill() {

    }
}

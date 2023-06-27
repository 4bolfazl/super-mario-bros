package ir.sharif.math.ap2023.project.model.enemy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sharif.math.ap2023.project.controller.GameEngine;
import ir.sharif.math.ap2023.project.controller.GameLoader;
import ir.sharif.math.ap2023.project.controller.GameState;
import ir.sharif.math.ap2023.project.controller.sound.BackgroundMusicType;
import ir.sharif.math.ap2023.project.controller.sound.SoundManager;
import ir.sharif.math.ap2023.project.model.block.BlockType;
import ir.sharif.math.ap2023.project.model.block.EmptyBlockObject;
import ir.sharif.math.ap2023.project.model.game.SectionObject;
import ir.sharif.math.ap2023.project.model.item.Bomb;
import ir.sharif.math.ap2023.project.model.player.Fireball;
import ir.sharif.math.ap2023.project.model.player.Player;
import ir.sharif.math.ap2023.project.view.ImageLoader;
import ir.sharif.math.ap2023.project.view.UIManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bowser extends EnemyObject {
    public int lastKey = 0;
    public int pressedTimes = 0;
    public boolean grabAttacking, jumpAttacking, fireballAttacking, nukeAttacking, grabAttackingStarted;
    public boolean grabCoolDownStart, jumpCoolDownStart, fireballCoolDownStart, nukeCoolDownStart;
    public int fireballCoolDown = 0;
    public int grabCoolDown = 0;
    public int jumpCoolDown = 0;
    public int nukeCoolDown = 0;
    public int jumpAttackTimer = 0;
    public boolean jumpAttackStarted = false;
    public int playersFireballs = 0;
    public boolean nukeAttackStarted = false;
    public int nukePressTimer = 0;
    public Bomb bomb;
    public boolean phase2 = false;
    int fireDelay = 120;
    int fireballsShot = 0;
    int HP = 20;
    int waitOnAir = 0;
    boolean triggered = false;
    boolean freeze = false;
    int freezeTimer = 0;
    int playerOnTheGroundTime = 0;
    @JsonIgnore
    BufferedImage[] images = ImageLoader.getInstance().getEnemyImages(EnemyType.BOWSER);
    double gravity = 3;
    List<BowserFireball> fireballs = Collections.synchronizedList(new ArrayList<>());
    int grabDelay = 0;
    int grabTime = 0;

    public Bowser(int x, int y, EnemyType type) {
        super(x, y, type);
    }

    public Bowser() {
        solidArea.setSize(5 * UIManager.getInstance().getTileSize(), 5 * UIManager.getInstance().getTileSize());
        setToRight(false);
        speedX = 0;
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
        if (GameEngine.getInstance().scene)
            return images[toRight ? 15 : 6];
        if (falling && jumpAttackStarted) {
            return images[toRight ? 19 : 18];
        }
        if (nukeAttackStarted) {
            return images[(toRight ? 16 : 7) + frame / 15];
        }
        if (fireballAttacking) {
            return images[(toRight ? 11 : 2) + frame / 15];
        } else if (grabAttackingStarted) {
            return images[toRight ? 15 : 6];
        } else if (grabAttacking) {
            return images[(toRight ? 13 : 4) + frame / 15];
        }
        return images[(toRight ? 9 : 0) + frame / 15];
    }

    public void decreaseHP(int damage) {
        HP -= damage;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public boolean isTriggered() {
        return triggered;
    }

    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }

    public double distanceFromMario() {
        Player player = GameEngine.getInstance().getPlayer();
        if (player.getX() > solidArea.x)
            return ((player.getX() - (solidArea.x + 5 * UIManager.getInstance().getTileSize()))) / UIManager.getInstance().getTileSize();
        return (solidArea.x - (player.getX() + UIManager.getInstance().getTileSize())) / UIManager.getInstance().getTileSize();
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

    public void jumpToAvoidFireballs() {
        Player player = GameEngine.getInstance().getPlayer();
        toRight = player.getX() > solidArea.x;
        for (Fireball fireball : player.getFireballs()) {
            if (waitOnAir == 0 && solidArea.y >= 240) {
                if (fireball.isToRight()) {
                    if (solidArea.x - fireball.getX() <= 112) {
                        if (!fireball.isDetermined()) {
                            fireball.setDetermined(true);
                            double distance = (solidArea.x - fireball.getStartPosition()) / UIManager.getInstance().getTileSize();
                            if (GameEngine.getInstance().randomGenerator.nextInt(8) < distance) {
                                if (getSpeedY() == 0) {
                                    setSpeedY(25);
                                    setJumping(true);
                                }
                            }
                        }
                    }
                } else {
                    if (fireball.getX() - (solidArea.x + 5 * UIManager.getInstance().getTileSize()) <= 112) {
                        if (!fireball.isDetermined()) {
                            fireball.setDetermined(true);
                            double distance = (solidArea.x + 5 * UIManager.getInstance().getTileSize() - fireball.getStartPosition()) / UIManager.getInstance().getTileSize();
                            if (GameEngine.getInstance().randomGenerator.nextInt(8) < distance) {
                                if (getSpeedY() == 0) {
                                    setSpeedY(25);
                                    setJumping(true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean isFreeze() {
        return freeze;
    }

    public void setFreeze(boolean freeze) {
        this.freeze = freeze;
    }

    public List<BowserFireball> getFireballs() {
        return fireballs;
    }

    public void setFireballs(List<BowserFireball> fireballs) {
        this.fireballs = fireballs;
    }

    @Override
    public void updateLocation() {
        if (!triggered) {
            if (distanceFromMario() <= 8) {
                Player player = GameEngine.getInstance().getPlayer();
                SectionObject sectionObject = GameLoader.getInstance("config.json").getGame().getLevels().get(player.getLevel() - 1).getSections().get(player.getSection() - 1);
                for (int i = 0; i < 10; i++) {
                    sectionObject.getBlocks().add(new EmptyBlockObject(0, i, BlockType.EMPTY));
                    sectionObject.getBlocks().add(new EmptyBlockObject(25, i, BlockType.EMPTY));
                }
                triggered = true;
                SoundManager.getInstance().pauseMusic();
                SoundManager.getInstance().playBackgroundMusic(BackgroundMusicType.BOSS);
            }
        } else {
            updateTime();
            if (!isDead() && !isFreeze()) {
                jumpToAvoidFireballs();
                double distanceFromMario = distanceFromMario();
                if (distanceFromMario >= 8 && speedX == 0) {
                    setSpeedX(toRight ? 3 : -3);
                } else if ((distanceFromMario >= 6 && distanceFromMario <= 10 && fireballCoolDown == 0) || fireballAttacking) {
                    speedX = 0;
                    fireballAttack();
                } else if ((distanceFromMario <= 2 && distanceFromMario >= 0 && GameEngine.getInstance().getPlayer().getY() >= solidArea.y && grabCoolDown == 0) || grabAttacking || grabAttackingStarted) {
                    speedX = 0;
                    grabAttack();
                } else if ((playerOnTheGroundTime >= 200 && jumpCoolDown == 0)) {
                    speedX = 0;
                    playerOnTheGroundTime = 0;
                    jumpAttack();
                } else if (playersFireballs >= 5 && phase2) {
                    speedX = 0;
                    playersFireballs = 0;
                    nukeAttack();
                } else {
                    setSpeedX(GameEngine.getInstance().randomGenerator.nextBoolean() ? -1.5 : 1.5);
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

                if (distanceFromMario <= 3)
                    speedX = 0;
                getSolidArea().x += speedX;
            }
        }
    }

    public void updateTime() {
        if (freeze) {
            freezeTimer++;
            if (freezeTimer >= 50) {
                freeze = false;
                freezeTimer = 0;
            }
        }
        if (nukeAttackStarted) {
            nukePressTimer++;
            if (nukePressTimer >= 120) {
                nukePressTimer = 0;
                nukeAttackStarted = false;
            }
        }
        if (jumpAttacking) {
            jumpAttackTimer++;
            if (jumpAttackTimer >= 250) {
                jumpAttacking = false;
                jumpAttackTimer = 0;
                jumpCoolDownStart = true;
            }
        }
        if (GameEngine.getInstance().getPlayer().isOnTheGround()) {
            playerOnTheGroundTime++;
        } else {
            playerOnTheGroundTime = 0;
        }
        if (grabCoolDownStart) {
            grabCoolDown++;
            if (grabCoolDown >= 240) {
                grabCoolDownStart = false;
                grabCoolDown = 0;
            }
        }
        if (jumpCoolDownStart) {
            jumpCoolDown++;
            if (jumpCoolDown >= 180) {
                jumpCoolDownStart = false;
                jumpCoolDown = 0;
            }
        }
        if (fireballCoolDownStart) {
            fireballCoolDown++;
            if (fireballCoolDown >= 240) {
                fireballCoolDownStart = false;
                fireballCoolDown = 0;
            }
        }
        if (nukeCoolDownStart) {
            nukeCoolDown++;
            if (nukeCoolDown >= 240) {
                nukeCoolDownStart = false;
                nukeCoolDown = 0;
            }
        }
    }

    public void fireballAttack() {
        fireballAttacking = true;

        fireDelay++;
        if (fireDelay >= 120 && fireballsShot < 3) {
            double x, y;
            if (toRight) {
                x = solidArea.x + 5 * UIManager.getInstance().getTileSize();
            } else {
                x = solidArea.x - UIManager.getInstance().getTileSize();
            }
            if (GameEngine.getInstance().randomGenerator.nextBoolean()) {
                y = 8 * UIManager.getInstance().getTileSize();
            } else {
                y = 6.1 * UIManager.getInstance().getTileSize();
            }

            fireballs.add(new BowserFireball(x, y, toRight));
            fireballsShot++;
            fireDelay = 0;
        }

        if (fireballsShot == 3) {
            fireballAttacking = false;
            fireballCoolDownStart = true;
            fireballsShot = 0;
            fireDelay = 120;
        }
    }

    public void grabAttack() {
        if (!grabAttacking) {
            grabAttackingStarted = true;
            grabDelay++;
            if (grabDelay >= 80) {
                grabAttackingStarted = false;
                grabDelay = 0;
                if (distanceFromMario() <= 2) {
                    grabAttacking = true;
                }
            }
        } else {
            grabTime++;
            if (grabTime >= 250) {
                throwPlayer();
                GameEngine.getInstance().getPlayer().decreaseHeartHit();
            } else if (pressedTimes >= 10) {
                throwPlayer();
            }
        }
    }

    public void jumpAttack() {
        jumpAttackStarted = true;
        setFalling(false);
        setJumping(true);
        setSpeedY(25);
    }

    public void nukeAttack() {
        nukeAttackStarted = true;
        bomb = new Bomb(UIManager.getInstance().getTileSize() * (GameEngine.getInstance().randomGenerator.nextInt(24) + 1), -48);
    }

    private void throwPlayer() {
        Player player = GameEngine.getInstance().getPlayer();
        player.setEnemyInvincible(true);
        if (player.getX() > solidArea.x) {
            player.setSpeedX(-6);
        } else {
            player.setSpeedX(6);
        }
        player.setSpeedY(16);
        player.setJumping(true);
        player.setFalling(false);
        grabAttacking = false;
        grabCoolDownStart = true;
        grabTime = 0;
        pressedTimes = 0;
    }

    @Override
    public void kill() {

    }

    public void kill(int damage) {
        decreaseHP(damage);
        if (getHP() <= 10 && !phase2) {
            GameEngine.getInstance().setGameState(GameState.SCENE);
            GameEngine.getInstance().scene = true;
        }
        freeze = true;
        if (getHP() <= 0) {
            setDead(true);
            GameEngine.getInstance().setGameState(GameState.BOSS_DEAD);
        }
    }

    public int getLastKey() {
        return lastKey;
    }

    public void setLastKey(int lastKey) {
        this.lastKey = lastKey;
    }

    public int getPressedTimes() {
        return pressedTimes;
    }

    public void setPressedTimes(int pressedTimes) {
        this.pressedTimes = pressedTimes;
    }

    public boolean isGrabAttacking() {
        return grabAttacking;
    }

    public void setGrabAttacking(boolean grabAttacking) {
        this.grabAttacking = grabAttacking;
    }

    public boolean isJumpAttacking() {
        return jumpAttacking;
    }

    public void setJumpAttacking(boolean jumpAttacking) {
        this.jumpAttacking = jumpAttacking;
    }

    public boolean isFireballAttacking() {
        return fireballAttacking;
    }

    public void setFireballAttacking(boolean fireballAttacking) {
        this.fireballAttacking = fireballAttacking;
    }

    public boolean isNukeAttacking() {
        return nukeAttacking;
    }

    public void setNukeAttacking(boolean nukeAttacking) {
        this.nukeAttacking = nukeAttacking;
    }

    public boolean isGrabAttackingStarted() {
        return grabAttackingStarted;
    }

    public void setGrabAttackingStarted(boolean grabAttackingStarted) {
        this.grabAttackingStarted = grabAttackingStarted;
    }

    public boolean isGrabCoolDownStart() {
        return grabCoolDownStart;
    }

    public void setGrabCoolDownStart(boolean grabCoolDownStart) {
        this.grabCoolDownStart = grabCoolDownStart;
    }

    public boolean isJumpCoolDownStart() {
        return jumpCoolDownStart;
    }

    public void setJumpCoolDownStart(boolean jumpCoolDownStart) {
        this.jumpCoolDownStart = jumpCoolDownStart;
    }

    public boolean isFireballCoolDownStart() {
        return fireballCoolDownStart;
    }

    public void setFireballCoolDownStart(boolean fireballCoolDownStart) {
        this.fireballCoolDownStart = fireballCoolDownStart;
    }

    public boolean isNukeCoolDownStart() {
        return nukeCoolDownStart;
    }

    public void setNukeCoolDownStart(boolean nukeCoolDownStart) {
        this.nukeCoolDownStart = nukeCoolDownStart;
    }

    public int getFireballCoolDown() {
        return fireballCoolDown;
    }

    public void setFireballCoolDown(int fireballCoolDown) {
        this.fireballCoolDown = fireballCoolDown;
    }

    public int getGrabCoolDown() {
        return grabCoolDown;
    }

    public void setGrabCoolDown(int grabCoolDown) {
        this.grabCoolDown = grabCoolDown;
    }

    public int getJumpCoolDown() {
        return jumpCoolDown;
    }

    public void setJumpCoolDown(int jumpCoolDown) {
        this.jumpCoolDown = jumpCoolDown;
    }

    public int getNukeCoolDown() {
        return nukeCoolDown;
    }

    public void setNukeCoolDown(int nukeCoolDown) {
        this.nukeCoolDown = nukeCoolDown;
    }

    public int getJumpAttackTimer() {
        return jumpAttackTimer;
    }

    public void setJumpAttackTimer(int jumpAttackTimer) {
        this.jumpAttackTimer = jumpAttackTimer;
    }

    public boolean isJumpAttackStarted() {
        return jumpAttackStarted;
    }

    public void setJumpAttackStarted(boolean jumpAttackStarted) {
        this.jumpAttackStarted = jumpAttackStarted;
    }

    public int getPlayersFireballs() {
        return playersFireballs;
    }

    public void setPlayersFireballs(int playersFireballs) {
        this.playersFireballs = playersFireballs;
    }

    public boolean isNukeAttackStarted() {
        return nukeAttackStarted;
    }

    public void setNukeAttackStarted(boolean nukeAttackStarted) {
        this.nukeAttackStarted = nukeAttackStarted;
    }

    public int getNukePressTimer() {
        return nukePressTimer;
    }

    public void setNukePressTimer(int nukePressTimer) {
        this.nukePressTimer = nukePressTimer;
    }

    public Bomb getBomb() {
        return bomb;
    }

    public void setBomb(Bomb bomb) {
        this.bomb = bomb;
    }

    public boolean isPhase2() {
        return phase2;
    }

    public void setPhase2(boolean phase2) {
        this.phase2 = phase2;
    }

    public int getFireDelay() {
        return fireDelay;
    }

    public void setFireDelay(int fireDelay) {
        this.fireDelay = fireDelay;
    }

    public int getFireballsShot() {
        return fireballsShot;
    }

    public void setFireballsShot(int fireballsShot) {
        this.fireballsShot = fireballsShot;
    }

    public int getWaitOnAir() {
        return waitOnAir;
    }

    public void setWaitOnAir(int waitOnAir) {
        this.waitOnAir = waitOnAir;
    }

    public int getFreezeTimer() {
        return freezeTimer;
    }

    public void setFreezeTimer(int freezeTimer) {
        this.freezeTimer = freezeTimer;
    }

    public int getPlayerOnTheGroundTime() {
        return playerOnTheGroundTime;
    }

    public void setPlayerOnTheGroundTime(int playerOnTheGroundTime) {
        this.playerOnTheGroundTime = playerOnTheGroundTime;
    }

    @Override
    public double getGravity() {
        return gravity;
    }

    @Override
    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    public int getGrabDelay() {
        return grabDelay;
    }

    public void setGrabDelay(int grabDelay) {
        this.grabDelay = grabDelay;
    }

    public int getGrabTime() {
        return grabTime;
    }

    public void setGrabTime(int grabTime) {
        this.grabTime = grabTime;
    }
}

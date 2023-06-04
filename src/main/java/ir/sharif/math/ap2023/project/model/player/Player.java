package ir.sharif.math.ap2023.project.model.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sharif.math.ap2023.project.controller.Camera;
import ir.sharif.math.ap2023.project.controller.sound.BackgroundMusicType;
import ir.sharif.math.ap2023.project.controller.sound.SoundManager;
import ir.sharif.math.ap2023.project.view.ImageLoader;
import ir.sharif.math.ap2023.project.view.UIManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player {
    private String username;
    private String password;
    private double x = 2 * UIManager.getInstance().getTileSize();
    private double y = UIManager.getInstance().getScreenHeight() - UIManager.getInstance().getTileSize() * 7;
    private Camera camera;
    private int time;
    private int level = 1, section = 1;
    private int coins = 0;
    private int hearts = 3;
    private Character character = Character.MARIO;
    private int characterState = 0;
    private int score;
    private PlayerDirection direction = PlayerDirection.IDLE_RIGHT;
    private double speedX, speedY;
    private double gravity = 0.38;
    private boolean jumping = false;
    private boolean falling = false;
    private Difficulty difficulty;
    private int saveSlot;
    private Player[] savedGames = new Player[3];
    private int frame = 0;
    private boolean isCrouching = false;
    private boolean invincible = false;
    private int invincibleTime = 0;

    private Rectangle solidArea = new Rectangle((int) x, (int) y, UIManager.getInstance().getTileSize(), UIManager.getInstance().getTileSize());

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Player(String username, String password, double x, double y, Camera camera, int time, int level, int section, int coins, int hearts, Character character, int characterState, int score, PlayerDirection direction, double speedX, double speedY, double gravity, boolean jumping, boolean falling, Difficulty difficulty, Player[] savedGames, int saveSlot, int sprites, Rectangle solidArea) {
        this.username = username;
        this.password = password;
        this.x = x;
        this.y = y;
        this.camera = camera;
        this.time = time;
        this.level = level;
        this.section = section;
        this.coins = coins;
        this.hearts = hearts;
        this.character = character;
        this.characterState = characterState;
        this.score = score;
        this.direction = direction;
        this.speedX = speedX;
        this.speedY = speedY;
        this.gravity = gravity;
        this.jumping = jumping;
        this.falling = falling;
        this.difficulty = difficulty;
        this.savedGames = savedGames;
        this.saveSlot = saveSlot;
        this.frame = sprites;
        this.solidArea = solidArea;
    }

    public Player() {
    }

    public boolean isCrouching() {
        return isCrouching;
    }

    public void setCrouching(boolean crouching) {
        isCrouching = crouching;
    }

    public void addPoints(int points) {
        this.score += points;
    }

    public void updateLocation() {
        if (jumping && speedY <= 0) {
            jumping = false;
            falling = true;
        } else if (jumping) {
            speedY -= gravity;
            y -= speedY;
        }

        if (falling) {
            y += speedY;
            speedY += gravity;
        }

        x += speedX;
    }

    @JsonIgnore
    public BufferedImage getImage() {
        BufferedImage[] images = ImageLoader.getInstance().getCharacterSprites(character)[characterState];
        if (characterState == 0) {
            if (isFalling()) {
                switch (direction) {
                    case JUMP_RIGHT, JUMP_IDLE_RIGHT, RIGHT, IDLE_RIGHT -> {
                        return images[4];
                    }
                    case JUMP_LEFT, JUMP_IDLE_LEFT, LEFT, IDLE_LEFT -> {
                        return images[10];
                    }
                    case DEAD -> {
                        return images[5];
                    }
                }
            } else {
                switch (direction) {
                    case IDLE_RIGHT -> {
                        return images[0];
                    }
                    case IDLE_LEFT -> {
                        return images[6];
                    }
                    case RIGHT -> {
                        frame++;
                        if (frame >= 27)
                            frame = 0;
                        return images[1 + frame / 9]; //changes
                    }
                    case LEFT -> {
                        frame++;
                        if (frame >= 27)
                            frame = 0;
                        return images[7 + frame / 9]; //changes
                    }
                    case JUMP_RIGHT, JUMP_IDLE_RIGHT -> {
                        return images[4];
                    }
                    case JUMP_LEFT, JUMP_IDLE_LEFT -> {
                        return images[10];
                    }
                    case DEAD -> {
                        return images[5];
                    }
                }
            }
        } else {
            if (isFalling()) {
                switch (direction) {
                    case JUMP_RIGHT, JUMP_IDLE_RIGHT, RIGHT, IDLE_RIGHT, CROUCH_RIGHT, CROUCH_RIGHT_IDLE -> {
                        return images[6];
                    }
                    case JUMP_LEFT, JUMP_IDLE_LEFT, LEFT, IDLE_LEFT, CROUCH_LEFT, CROUCH_LEFT_IDLE -> {
                        return images[14];
                    }
                    case DEAD -> {
                        return ImageLoader.getInstance().getCharacterSprites(character)[0][5];
                    }
                }
            } else {
                switch (direction) {
                    case IDLE_RIGHT -> {
                        return images[0];
                    }
                    case IDLE_LEFT -> {
                        return images[8];
                    }
                    case RIGHT -> {
                        frame++;
                        if (frame >= 27)
                            frame = 0;
                        return images[1 + frame / 9]; //changes
                    }
                    case LEFT -> {
                        frame++;
                        if (frame >= 27)
                            frame = 0;
                        return images[9 + frame / 9]; //changes
                    }
                    case JUMP_RIGHT, JUMP_IDLE_RIGHT -> {
                        return images[6];
                    }
                    case JUMP_LEFT, JUMP_IDLE_LEFT -> {
                        return images[14];
                    }
                    case CROUCH_RIGHT_IDLE -> {
                        return images[4];
                    }
                    case CROUCH_LEFT_IDLE -> {
                        return images[12];
                    }
                    case CROUCH_RIGHT -> {
                        frame++;
                        if (frame >= 18)
                            frame = 0;
                        return images[4 + frame / 9]; //changes
                    }
                    case CROUCH_LEFT -> {
                        frame++;
                        if (frame >= 18)
                            frame = 0;
                        return images[12 + frame / 9]; //changes
                    }
                    case SHRINK_RIGHT -> {
                        return images[7];
                    }
                    case SHRINK_LEFT -> {
                        return images[15];
                    }
                    case DEAD -> {
                        return ImageLoader.getInstance().getCharacterSprites(character)[0][5];
                    }
                }
            }
        }
        return null;
    }

    @JsonIgnore
    public Rectangle getTopBounds() {
        return new Rectangle(
                (int) x + solidArea.width / 6,
                (int) y,
                2 * solidArea.width / 3,
                solidArea.height / 2
        );
    }

    @JsonIgnore
    public Rectangle getBottomBounds() {
        return new Rectangle(
                (int) x + solidArea.width / 6,
                (int) y + solidArea.height / 2,
                2 * solidArea.width / 3,
                solidArea.height / 2
        );
    }

    @JsonIgnore
    public Rectangle getRightBounds() {
        return new Rectangle(
                (int) x + 3 * solidArea.width / 4,
                (int) y + solidArea.height / 4,
                solidArea.width / 4,
                solidArea.height / 2
        );
    }

    @JsonIgnore
    public Rectangle getLeftBounds() {
        return new Rectangle(
                (int) x,
                (int) y + solidArea.height / 4,
                solidArea.width / 4,
                solidArea.height / 2
        );
    }

    public void updateSolidArea() {
        if (characterState == 0) {
            solidArea.setBounds(
                    (int) x,
                    (int) y,
                    UIManager.getInstance().getTileSize(),
                    UIManager.getInstance().getTileSize()
            );
        } else {
            solidArea.setBounds(
                    (int) x,
                    (int) y + (isCrouching ? UIManager.getInstance().getTileSize() : 0),
                    UIManager.getInstance().getTileSize(),
                    UIManager.getInstance().getTileSize() * (isCrouching ? 1 : 2)
            );
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    public Rectangle getSolidArea() {
        return solidArea;
    }

    public void setSolidArea(Rectangle solidArea) {
        this.solidArea = solidArea;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getHearts() {
        return hearts;
    }

    public void setHearts(int hearts) {
        this.hearts = hearts;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public int getCharacterState() {
        return characterState;
    }

    public void setCharacterState(int characterState) {
        this.characterState = characterState;
    }

    public void upgradeState() {
        if (characterState != 2)
            characterState++;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public PlayerDirection getDirection() {
        return direction;
    }

    public void setDirection(PlayerDirection direction) {
        this.direction = direction;
    }

    public double getSpeedX() {
        return speedX;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    public double getGravity() {
        return gravity;
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    public boolean isFalling() {
        return falling;
    }

    public void setFalling(boolean falling) {
        this.falling = falling;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Player[] getSavedGames() {
        return savedGames;
    }

    public void setSavedGames(Player[] savedGames) {
        this.savedGames = savedGames;
    }

    public int getSaveSlot() {
        return saveSlot;
    }

    public void setSaveSlot(int saveSlot) {
        this.saveSlot = saveSlot;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    public int getInvincibleTime() {
        return invincibleTime;
    }

    public void setInvincibleTime(int invincibleTime) {
        this.invincibleTime = invincibleTime;
    }

    public void addInvincibleTime() {
        invincibleTime++;
    }

    public void checkInvincibility() {
        if (invincible) {
            invincibleTime++;
            if (invincibleTime >= 15 * 60) {
                invincibleTime = 0;
                invincible = false;
                SoundManager soundManager = SoundManager.getInstance();
                soundManager.pauseMusic();
                soundManager.playBackgroundMusic(BackgroundMusicType.OVERWORLD);
            }
        }
    }
}

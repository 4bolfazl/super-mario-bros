package ir.sharif.math.ap2023.project.model.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sharif.math.ap2023.project.controller.GameEngine;
import ir.sharif.math.ap2023.project.controller.GameLoader;
import ir.sharif.math.ap2023.project.controller.GameState;
import ir.sharif.math.ap2023.project.controller.sound.BackgroundMusicType;
import ir.sharif.math.ap2023.project.controller.sound.SoundEffectType;
import ir.sharif.math.ap2023.project.controller.sound.SoundManager;
import ir.sharif.math.ap2023.project.model.block.BlockType;
import ir.sharif.math.ap2023.project.model.block.EmptyBlockObject;
import ir.sharif.math.ap2023.project.model.enemy.Bowser;
import ir.sharif.math.ap2023.project.model.game.Game;
import ir.sharif.math.ap2023.project.model.game.SectionObject;
import ir.sharif.math.ap2023.project.model.pipe.ExitPipe;
import ir.sharif.math.ap2023.project.model.pipe.PipeObject;
import ir.sharif.math.ap2023.project.view.ImageLoader;
import ir.sharif.math.ap2023.project.view.UIManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Player {
    @JsonIgnore
    public boolean hasSword = false;
    public Sword sword = new Sword(this);
    public int swordCoolDownTimer = 0;
    public boolean swordCoolDownStart = false;
    private String username;
    private String password;
    private double x = 2 * UIManager.getInstance().getTileSize();
    private double y = UIManager.getInstance().getScreenHeight() - UIManager.getInstance().getTileSize() * 7;
    private int time;
    private int level = 1, section = 1;
    private int coins = 0;
    private int hearts = 3;
    private Character character = Character.MARIO;
    private int characterState = 0;
    private int score = 0;
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
    private boolean enemyInvincible = false;
    private int invincibleTime = 0;
    private int enemyInvincibleTime = 0;
    private int enemyInvincibleFrame = 0;
    private List<Fireball> fireballs = new ArrayList<>();
    @JsonIgnore
    private Rectangle solidArea = new Rectangle((int) x, (int) y, UIManager.getInstance().getTileSize(), UIManager.getInstance().getTileSize());
    private PipeObject pipeUnder;
    private SectionObject tempSection;
    private PipeObject tempPipe;

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
        init();
    }

    public Player(String username, String password, double x, double y, int time, int level, int section, int coins, int hearts, Character character, int characterState, int score, PlayerDirection direction, double speedX, double speedY, double gravity, boolean jumping, boolean falling, Difficulty difficulty, Player[] savedGames, int saveSlot, int sprites, Rectangle solidArea) {
        this.username = username;
        this.password = password;
        this.x = x;
        this.y = y;
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
        init();
    }

    @JsonIgnore
    public boolean isEnemyInvincible() {
        return enemyInvincible;
    }

    @JsonIgnore
    public void setEnemyInvincible(boolean enemyInvincible) {
        this.enemyInvincible = enemyInvincible;
    }

    private void init() {
        Game game = GameLoader.getInstance("config.json").getGame();

        setHearts(game.getHearts());
        setCharacterState(game.getMarioState());
        setTime(game.getLevels().get(level - 1).getSections().get(section - 1).getTime());
    }

    public boolean isOnTheGround() {
        int height = (characterState > 0 && !isCrouching) ? 96 : 48;
        return y + height >= 480;
    }

    public void decreaseTime() {
        time--;
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

    public void addCoins(int coin) {
        coins += coin;
    }

    public void updateLocation() {
        if (y + getSolidArea().height >= 530 && GameEngine.getInstance().getGameState() != GameState.SCENE) {
            setCharacterState(0);
            decreaseHeartHit();
        }
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
        if (enemyInvincible) {
            enemyInvincibleFrame++;
            if (enemyInvincibleFrame >= 5) {
                enemyInvincibleFrame = 0;
                return null;
            }
        }
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

    public void decreaseHeartHit() {
        if (getCharacterState() > 0) {
            setCharacterState(0);
            setEnemyInvincible(true);
        } else {
            decreaseHearts();
            GameEngine.getInstance().setGameState(GameState.SCENE);
            setDirection(PlayerDirection.DEAD);
            setCharacterState(0);
            SoundManager soundManager = SoundManager.getInstance();
            soundManager.pauseMusic();
            soundManager.playSoundEffect(SoundEffectType.GAME_OVER);
            setSpeedX(0);
            setSpeedY(5);
            setFalling(false);
            setJumping(true);
        }
    }

    public void nextSection() {
        GameEngine.getInstance().getItems().clear();
        section++;
        if (GameLoader.getInstance("config.json").getGame().getLevels().get(level - 1).getSections().get(section - 1).isBossSection()) {
            SoundManager soundManager = SoundManager.getInstance();
            soundManager.pauseMusic();
            soundManager.playBackgroundMusic(BackgroundMusicType.CASTLE);
            GameEngine.getInstance().boss = (Bowser) GameLoader.getInstance("config.json").getGame().getLevels().get(level - 1).getSections().get(section - 1).getEnemies().get(0);
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

    @JsonIgnore
    public Rectangle getSolidArea() {
        return solidArea;
    }

    @JsonIgnore
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

    public PipeObject getPipeUnder() {
        return pipeUnder;
    }

    public void setPipeUnder(PipeObject pipeUnder) {
        this.pipeUnder = pipeUnder;
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

    public void decreaseHearts() {
        hearts--;
    }

    public void checkInvincibility() {
        if (invincible) {
            invincibleTime++;
            if (invincibleTime >= 15 * 60) {
                invincibleTime = 0;
                invincible = false;
                SoundManager soundManager = SoundManager.getInstance();
                soundManager.pauseMusic();
                if (GameLoader.getInstance("config.json").getGame().getLevels().get(level - 1).getSections().get(section - 1).isBossSection()) {
                    if (GameEngine.getInstance().boss.isTriggered())
                        soundManager.playBackgroundMusic(BackgroundMusicType.BOSS);
                    else
                        soundManager.playBackgroundMusic(BackgroundMusicType.CASTLE);
                } else
                    soundManager.playBackgroundMusic(BackgroundMusicType.OVERWORLD);
            }
        }
        if (enemyInvincible) {
            enemyInvincibleTime++;
            if (enemyInvincibleTime >= 120) {
                enemyInvincibleTime = 0;
                enemyInvincible = false;
            }
        }
    }

    @JsonIgnore
    public int getEnemyInvincibleTime() {
        return enemyInvincibleTime;
    }

    @JsonIgnore
    public void setEnemyInvincibleTime(int enemyInvincibleTime) {
        this.enemyInvincibleTime = enemyInvincibleTime;
    }

    public void addEnemyInvincibleTime() {
        enemyInvincibleTime++;
    }

    public int getEnemyInvincibleFrame() {
        return enemyInvincibleFrame;
    }

    public void setEnemyInvincibleFrame(int enemyInvincibleFrame) {
        this.enemyInvincibleFrame = enemyInvincibleFrame;
    }

    public void addEnemyInvincibleFrame() {
        enemyInvincibleFrame++;
    }

    public List<Fireball> getFireballs() {
        return fireballs;
    }

    public void setFireballs(List<Fireball> fireballs) {
        this.fireballs = fireballs;
    }

    public void addFireBall(Fireball fireball) {
        this.fireballs.add(fireball);
    }

    public void enterSecretPipe() {
        tempPipe = pipeUnder;
        tempSection = GameLoader.getInstance("config.json").getGame().getLevels().get(level - 1).getSections().get(section - 1);
        GameLoader.getInstance("config.json").getGame().getLevels().get(level - 1).getSections().set(section - 1, pipeUnder.getSection());
        pipeUnder.getSection().getPipes().add(pipeUnder.getSection().getSpawnPipe());
        pipeUnder.getSection().getPipes().add(new ExitPipe());
        for (int i = 0; i < 10; i++) {
            pipeUnder.getSection().getBlocks().add(new EmptyBlockObject(26, i, BlockType.EMPTY));
        }
        setX(pipeUnder.getSection().getSpawnPipe().getX() * UIManager.getInstance().getTileSize());
        setY((pipeUnder.getSection().getSpawnPipe().getY() - 2) * UIManager.getInstance().getTileSize());
        setSpeedX(0);
        setSpeedY(12.5);
        setFalling(false);
        setJumping(true);
    }

    public void exitSecretPipe() {
        GameLoader.getInstance("config.json").getGame().getLevels().get(level - 1).getSections().set(section - 1, tempSection);
        setX(tempPipe.getX() * UIManager.getInstance().getTileSize());
        setY((tempPipe.getY() - 2) * UIManager.getInstance().getTileSize());
        setSpeedX(0);
        setSpeedY(12.5);
        setFalling(false);
        setJumping(true);
    }

    public void shootSword() {
        boolean isToRight = getDirection() == PlayerDirection.CROUCH_RIGHT || getDirection() == PlayerDirection.CROUCH_RIGHT_IDLE || getDirection() == PlayerDirection.RIGHT || getDirection() == PlayerDirection.IDLE_RIGHT || getDirection() == PlayerDirection.JUMP_IDLE_RIGHT || getDirection() == PlayerDirection.JUMP_RIGHT;
        sword.startPosition = (int) ((isToRight) ? getX() + 48 : getX() - 96);
        sword.x = sword.startPosition;
        sword.speedX = isToRight ? 4 : -4;
        sword.released = true;
    }

    public void activateSword() {
        if (!hasSword && swordCoolDownTimer == 0) {
            if (coins >= 3) {
                coins -= 3;
                hasSword = true;
            }
        }
    }

    public void nextLevel() {
        GameEngine.getInstance().getItems().clear();
        section = 1;
        level++;
        resetLocation();
    }

    public void resetLocation() {
        x = 2 * UIManager.getInstance().getTileSize();
        y = UIManager.getInstance().getScreenHeight() - UIManager.getInstance().getTileSize() * 7;
    }
}

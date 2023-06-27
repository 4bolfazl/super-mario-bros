package ir.sharif.math.ap2023.project.model.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sharif.math.ap2023.project.controller.sound.GameEngineCopy;
import ir.sharif.math.ap2023.project.model.game.Game;
import ir.sharif.math.ap2023.project.model.game.SectionObject;
import ir.sharif.math.ap2023.project.model.pipe.PipeObject;

import java.util.List;

public class PlayerToSave {
    public boolean hasSword;
    public Sword sword;
    public int swordCoolDownTimer;
    public boolean swordCoolDownStart;
    @JsonIgnore
    private String username;
    @JsonIgnore
    private String password;
    private double x;
    private double y;
    private int time;
    private int level, section;
    private int coins;
    private int hearts;
    private Character character;
    private int characterState;
    private int score;
    private PlayerDirection direction;
    private double speedX, speedY;
    private double gravity;
    private boolean jumping;
    private boolean falling;
    private Difficulty difficulty;
    @JsonIgnore
    private int saveSlot;
    @JsonIgnore
    private boolean continuing = false;
    @JsonIgnore
    private Player[] savedPlayer;
    @JsonIgnore
    private GameEngineCopy[] savedGameEngineCopy;
    @JsonIgnore
    private Game[] savedGame;
    private int frame;
    private boolean crouching;
    private boolean invincible;
    private boolean enemyInvincible;
    private int invincibleTime;
    private int enemyInvincibleTime;
    private int enemyInvincibleFrame;
    private List<Fireball> fireballs;
    private PipeObject pipeUnder;
    private SectionObject tempSection;
    private PipeObject tempPipe;

    public PlayerToSave() {
    }

    public PlayerToSave(Player player){
        this.hasSword = player.hasSword;
        this.sword = player.sword;
        this.swordCoolDownTimer = player.swordCoolDownTimer;
        this.swordCoolDownStart = player.swordCoolDownStart;
        this.x = player.getX();
        this.y = player.getY();
        this.time = player.getTime();
        this.level = player.getLevel();
        this.section = player.getSection();
        this.hearts = player.getHearts();
        this.coins = player.getCoins();
        this.character = player.getCharacter();
        this.characterState = player.getCharacterState();
        this.score = player.getScore();
        this.speedX = player.getSpeedX();
        this.speedY = player.getSpeedY();
        this.gravity = player.getGravity();
        this.direction = player.getDirection();
        this.jumping = player.isJumping();
        this.falling = player.isFalling();
        this.difficulty = player.getDifficulty();
        this.frame = player.getFrame();
        this.crouching = player.isCrouching();
        this.invincible = player.isInvincible();
        this.enemyInvincible = player.isEnemyInvincible();
        this.invincibleTime = player.getInvincibleTime();
        this.enemyInvincibleFrame = player.getEnemyInvincibleFrame();
        this.enemyInvincibleTime = player.getEnemyInvincibleTime();
        this.fireballs = player.getFireballs();
        this.pipeUnder = player.getPipeUnder();
        this.tempSection = player.getTempSection();
        this.tempPipe = player.getTempPipe();
    }

    public boolean isHasSword() {
        return hasSword;
    }

    public void setHasSword(boolean hasSword) {
        this.hasSword = hasSword;
    }

    public Sword getSword() {
        return sword;
    }

    public void setSword(Sword sword) {
        this.sword = sword;
    }

    public int getSwordCoolDownTimer() {
        return swordCoolDownTimer;
    }

    public void setSwordCoolDownTimer(int swordCoolDownTimer) {
        this.swordCoolDownTimer = swordCoolDownTimer;
    }

    public boolean isSwordCoolDownStart() {
        return swordCoolDownStart;
    }

    public void setSwordCoolDownStart(boolean swordCoolDownStart) {
        this.swordCoolDownStart = swordCoolDownStart;
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

    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    public boolean isCrouching() {
        return crouching;
    }

    public void setCrouching(boolean crouching) {
        this.crouching = crouching;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    public boolean isEnemyInvincible() {
        return enemyInvincible;
    }

    public void setEnemyInvincible(boolean enemyInvincible) {
        this.enemyInvincible = enemyInvincible;
    }

    public int getInvincibleTime() {
        return invincibleTime;
    }

    public void setInvincibleTime(int invincibleTime) {
        this.invincibleTime = invincibleTime;
    }

    public int getEnemyInvincibleTime() {
        return enemyInvincibleTime;
    }

    public void setEnemyInvincibleTime(int enemyInvincibleTime) {
        this.enemyInvincibleTime = enemyInvincibleTime;
    }

    public int getEnemyInvincibleFrame() {
        return enemyInvincibleFrame;
    }

    public void setEnemyInvincibleFrame(int enemyInvincibleFrame) {
        this.enemyInvincibleFrame = enemyInvincibleFrame;
    }

    public List<Fireball> getFireballs() {
        return fireballs;
    }

    public void setFireballs(List<Fireball> fireballs) {
        this.fireballs = fireballs;
    }

    public PipeObject getPipeUnder() {
        return pipeUnder;
    }

    public void setPipeUnder(PipeObject pipeUnder) {
        this.pipeUnder = pipeUnder;
    }

    public SectionObject getTempSection() {
        return tempSection;
    }

    public void setTempSection(SectionObject tempSection) {
        this.tempSection = tempSection;
    }

    public PipeObject getTempPipe() {
        return tempPipe;
    }

    public void setTempPipe(PipeObject tempPipe) {
        this.tempPipe = tempPipe;
    }
}

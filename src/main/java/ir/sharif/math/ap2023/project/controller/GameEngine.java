package ir.sharif.math.ap2023.project.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sharif.math.ap2023.project.controller.inputManager.KeyboardHandler;
import ir.sharif.math.ap2023.project.controller.sound.BackgroundMusicType;
import ir.sharif.math.ap2023.project.controller.sound.GameEngineCopy;
import ir.sharif.math.ap2023.project.controller.sound.SoundEffectType;
import ir.sharif.math.ap2023.project.controller.sound.SoundManager;
import ir.sharif.math.ap2023.project.model.CollisionChecker;
import ir.sharif.math.ap2023.project.model.block.Flag;
import ir.sharif.math.ap2023.project.model.enemy.Bowser;
import ir.sharif.math.ap2023.project.model.enemy.BowserFireball;
import ir.sharif.math.ap2023.project.model.enemy.EnemyObject;
import ir.sharif.math.ap2023.project.model.game.SectionObject;
import ir.sharif.math.ap2023.project.model.item.Coin;
import ir.sharif.math.ap2023.project.model.item.Item;
import ir.sharif.math.ap2023.project.model.player.Fireball;
import ir.sharif.math.ap2023.project.model.player.Player;
import ir.sharif.math.ap2023.project.model.player.PlayerDirection;
import ir.sharif.math.ap2023.project.model.player.Sword;
import ir.sharif.math.ap2023.project.view.UIManager;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class GameEngine implements Runnable {
    private static GameEngine instance;
    @JsonIgnore
    private final double FPS = 60;
    @JsonIgnore
    private final double nextFrameInterval = 1000000000 / FPS;
    @JsonIgnore
    public JFrame window;
    @JsonIgnore
    public Random randomGenerator = new Random();
    @JsonIgnore
    public Bowser boss;
    public int swordPressTimer = 0;
    public int sceneTimer1 = 0;
    public boolean scene = false;
    @JsonIgnore
    private Thread gameThread;
    private GameState gameState = GameState.LOGIN_MENU;
    @JsonIgnore
    private Player player;
    @JsonIgnore
    private CollisionChecker collisionChecker;
    private List<Item> items = new ArrayList<>();
    private int deathTimer = 0;

    private GameEngine() {
        init();
    }

    @JsonIgnore
    public static GameEngine getInstance() {
        if (instance == null) {
            instance = new GameEngine();
        }
        return instance;
    }

    @JsonIgnore
    public static void setInstance(GameEngine instance) {
        GameEngine.instance = instance;
    }

    private void init() {
        UIManager uiManager = UIManager.getInstance();
        KeyboardHandler keyboardHandler = KeyboardHandler.getInstance();
        collisionChecker = CollisionChecker.getInstance();
        SoundManager soundManager = SoundManager.getInstance();

        soundManager.playBackgroundMusic(BackgroundMusicType.OVERWORLD);

        gameState = GameState.LOGIN_MENU;
//        setPlayer(new Player());
//        setPlayer(Database.getInstance().getCurrentUser());

        JFrame frame = new JFrame("Super Mario Bros.");
        window = frame;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setIconImage(new ImageIcon("src/main/resources/icon.png").getImage());

        frame.setContentPane(uiManager);
        frame.addKeyListener(keyboardHandler);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public synchronized void startGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / nextFrameInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                if (gameState != GameState.LOGIN_MENU) {
                    updateGame();
                    render();
                }
                delta = 0;
            }
        }
    }

    private void updateGame() {
        player.updateSolidArea();
        player.checkInvincibility();
        player.updateLocation();
        collisionChecker.checkCollisions();
        updateItems();
        updateEnemies();
        updateFireballs();
        updateKeyDelay();
        updateSword();
        removeTempCoins();
        if (scene)
            updateCutScene();
        if (GameLoader.getInstance("config.json").getGame().getLevels().get(player.getLevel() - 1).getSections().get(player.getSection() - 1).getFlag() != null)
            updateFlag();
        if (player.getDirection() == PlayerDirection.DEAD)
            updateDeathDelay();
    }

    private void updateDeathDelay() {
        deathTimer++;
        if (deathTimer >= 220) {
            deathTimer = 0;
            player.placeAfterDeath();
        }
    }

    private void updateFlag() {
        Flag flag = GameLoader.getInstance("config.json").getGame().getLevels().get(player.getLevel() - 1).getSections().get(player.getSection() - 1).getFlag();
        flag.updateLocation();
    }

    private void removeTempCoins() {
        List<Item> TBR = new ArrayList<>();
        for (Item item : items) {
            if (item instanceof Coin) {
                if (((Coin) item).temp) {
                    ((Coin) item).timer++;
                    if (((Coin) item).timer >= 40) {
                        TBR.add(item);
                    }
                }
            }
        }
        for (Item item : TBR) {
            items.remove(item);
        }
    }

    private void updateCutScene() {
        sceneTimer1++;
        if (sceneTimer1 >= 120) {
            sceneTimer1 = 0;
            player.setCharacterState(2);
            boss.phase2 = true;
            scene = false;
            setGameState(GameState.PLAYING);
        }
    }

    private void updateSword() {
        player.sword.updateLocation();
        if (player.swordCoolDownStart) {
            player.swordCoolDownTimer++;
            if (player.swordCoolDownTimer >= 270) {
                player.swordCoolDownTimer = 0;
            }
        }
        if (player.sword.isDestroyed()) {
            player.hasSword = false;
            player.sword = new Sword(player);
            player.swordCoolDownStart = true;
        }
    }

    private void updateKeyDelay() {
        KeyboardHandler keyboardHandler = KeyboardHandler.getInstance();
        if (!keyboardHandler.upPressed && !keyboardHandler.downPressed)
            swordPressTimer = 0;
        if (keyboardHandler.upPressed && keyboardHandler.upDelay >= 2) {
            keyboardHandler.upDelay = 0;
            if (!keyboardHandler.downPressed) {
                swordPressTimer = 0;
                if (!player.isJumping() && !player.isFalling()) {
                    SoundManager soundManager = SoundManager.getInstance();
                    soundManager.playSoundEffect(SoundEffectType.JUMP);
                    switch (player.getDirection()) {
                        case IDLE_RIGHT -> {
                            player.setDirection(PlayerDirection.JUMP_IDLE_RIGHT);
                            player.setSpeedY(12.5);
                            player.setJumping(true);
                        }
                        case IDLE_LEFT -> {
                            player.setDirection(PlayerDirection.JUMP_IDLE_LEFT);
                            player.setSpeedY(12.5);
                            player.setJumping(true);
                        }
                        case RIGHT -> {
                            player.setDirection(PlayerDirection.JUMP_RIGHT);
                            player.setSpeedY(12.5);
                            player.setSpeedX(4);
                            player.setJumping(true);
                        }
                        case LEFT -> {
                            player.setDirection(PlayerDirection.JUMP_LEFT);
                            player.setSpeedY(12.5);
                            player.setSpeedX(-4);
                            player.setJumping(true);
                        }
                    }
                }
            } else {
                swordPressTimer++;
                if (swordPressTimer >= 40) {
                    player.activateSword();
                }
            }
        } else if (KeyboardHandler.getInstance().upPressed) {
            keyboardHandler.upDelay++;
        }
        if (keyboardHandler.downPressed && keyboardHandler.downDelay >= 2) {
            keyboardHandler.downDelay = 0;
            if (!keyboardHandler.upPressed) {
                swordPressTimer = 0;
                if (player.getPipeUnder() != null) {
                    player.enterSecretPipe();
                }
                if (player.getCharacterState() > 0) {
                    player.setCrouching(true);
                    switch (player.getDirection()) {
                        case IDLE_RIGHT -> player.setDirection(PlayerDirection.CROUCH_RIGHT_IDLE);
                        case RIGHT -> player.setDirection(PlayerDirection.CROUCH_RIGHT);
                        case IDLE_LEFT -> player.setDirection(PlayerDirection.CROUCH_LEFT_IDLE);
                        case LEFT -> player.setDirection(PlayerDirection.CROUCH_LEFT);
                    }
                }
            } else {
                swordPressTimer++;
                if (swordPressTimer >= 40)
                    player.activateSword();
            }
        } else if (keyboardHandler.downPressed) {
            keyboardHandler.downDelay++;
        }
    }

    private void updateFireballs() {
        List<Fireball> toBeRemoved = new ArrayList<>();
        for (Fireball fireball : player.getFireballs()) {
            fireball.updateLocation();
            if (fireball.isDestroyed())
                toBeRemoved.add(fireball);
        }
        for (Fireball fireball : toBeRemoved) {
            player.getFireballs().remove(fireball);
        }
        if (boss != null) {
            for (BowserFireball fireball : boss.getFireballs()) {
                fireball.updateLocation();
                if (fireball.isDestroyed()) {
                    toBeRemoved.add(fireball);
                }
            }
            for (Fireball fireball : toBeRemoved) {
                boss.getFireballs().remove(fireball);
            }
            if (boss.bomb != null) {
                boss.bomb.updateLocation();
            }
        }
    }

    private void updateEnemies() {
        SectionObject sectionObject = GameLoader.getInstance("config.json").getGame().getLevels().get(player.getLevel() - 1).getSections().get(player.getSection() - 1);
        for (EnemyObject enemy : sectionObject.getEnemies()) {
            if (gameState == GameState.PLAYING)
                enemy.updateLocation();
        }
    }

    private void updateItems() {
        for (Item item : items) {
            item.updateLocation();
        }
    }

    private void render() {
        UIManager uiManager = UIManager.getInstance();
        uiManager.repaint();
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    @JsonIgnore
    public Player getPlayer() {
        return player;
    }

    @JsonIgnore
    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

    public int getSwordPressTimer() {
        return swordPressTimer;
    }

    public void setSwordPressTimer(int swordPressTimer) {
        this.swordPressTimer = swordPressTimer;
    }

    public int getSceneTimer1() {
        return sceneTimer1;
    }

    public void setSceneTimer1(int sceneTimer1) {
        this.sceneTimer1 = sceneTimer1;
    }

    public boolean isScene() {
        return scene;
    }

    public void setScene(boolean scene) {
        this.scene = scene;
    }

    public GameEngineCopy copy() {
        return new GameEngineCopy(
                this.swordPressTimer,
                this.sceneTimer1,
                this.scene,
                this.gameState,
                this.items,
                this.deathTimer
        );
    }

    public void reset(int hearts) {
        player.reset(hearts);
        swordPressTimer = 0;
        sceneTimer1 = 0;
        scene = false;
        items = new ArrayList<>();
        boss = null;
        deathTimer = 0;
    }

    public void loadGameEngine(GameEngineCopy gameEngineCopy) {
        this.swordPressTimer = gameEngineCopy.swordPressTimer;
        this.sceneTimer1 = gameEngineCopy.getSceneTimer1();
        this.scene = gameEngineCopy.isScene();
        this.gameState = gameEngineCopy.getGameState();
        this.items = gameEngineCopy.getItems();
        this.deathTimer = 0;
    }
}

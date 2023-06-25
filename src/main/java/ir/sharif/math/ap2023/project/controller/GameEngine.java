package ir.sharif.math.ap2023.project.controller;

import ir.sharif.math.ap2023.project.controller.inputManager.KeyboardHandler;
import ir.sharif.math.ap2023.project.controller.sound.BackgroundMusicType;
import ir.sharif.math.ap2023.project.controller.sound.SoundManager;
import ir.sharif.math.ap2023.project.model.CollisionChecker;
import ir.sharif.math.ap2023.project.model.enemy.Bowser;
import ir.sharif.math.ap2023.project.model.enemy.BowserFireball;
import ir.sharif.math.ap2023.project.model.enemy.EnemyObject;
import ir.sharif.math.ap2023.project.model.game.SectionObject;
import ir.sharif.math.ap2023.project.model.item.Item;
import ir.sharif.math.ap2023.project.model.player.Fireball;
import ir.sharif.math.ap2023.project.model.player.Player;
import ir.sharif.math.ap2023.project.view.ImageLoader;
import ir.sharif.math.ap2023.project.view.UIManager;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class GameEngine implements Runnable {
    private static GameEngine instance;
    private final double FPS = 60;
    private final double nextFrameInterval = 1000000000 / FPS;
    public JFrame window;
    public Random randomGenerator = new Random();
    public Bowser boss;
    private Thread gameThread;
    private GameState gameState = GameState.LOGIN_MENU;
    private Player player;
    private CollisionChecker collisionChecker;
    private List<Item> items = new ArrayList<>();

    private GameEngine() {
        init();
    }

    public static GameEngine getInstance() {
        if (instance == null) {
            instance = new GameEngine();
        }
        return instance;
    }

    private void init() {
        UIManager uiManager = UIManager.getInstance();
        KeyboardHandler keyboardHandler = KeyboardHandler.getInstance();
        collisionChecker = CollisionChecker.getInstance();
        ImageLoader imageLoader = ImageLoader.getInstance();
        SoundManager soundManager = SoundManager.getInstance();

        soundManager.playBackgroundMusic(BackgroundMusicType.OVERWORLD);

        gameState = GameState.LOGIN_MENU;
        setPlayer(new Player());

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

//        SwingUtilities.invokeLater(() -> frame.setVisible(true));
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

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        this.items.add(item);
    }
}

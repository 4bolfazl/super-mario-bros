package ir.sharif.math.ap2023.project.controller;

import ir.sharif.math.ap2023.project.controller.inputManager.KeyboardHandler;
import ir.sharif.math.ap2023.project.controller.sound.BackgroundMusicType;
import ir.sharif.math.ap2023.project.controller.sound.SoundManager;
import ir.sharif.math.ap2023.project.model.CollisionChecker;
import ir.sharif.math.ap2023.project.model.enemy.EnemyObject;
import ir.sharif.math.ap2023.project.model.enemy.EnemyType;
import ir.sharif.math.ap2023.project.model.enemy.Piranha;
import ir.sharif.math.ap2023.project.model.item.Item;
import ir.sharif.math.ap2023.project.model.player.Player;
import ir.sharif.math.ap2023.project.view.ImageLoader;
import ir.sharif.math.ap2023.project.view.UIManager;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public final class GameEngine implements Runnable {
    private static GameEngine instance;
    private final double FPS = 60;
    private final double nextFrameInterval = 1000000000 / FPS;
    public JFrame window;
    private Thread gameThread;
    private GameState gameState = GameState.LOGIN_MENU;
    private Player player;
    private CollisionChecker collisionChecker;
    private List<Item> items = new ArrayList<>();
    private List<EnemyObject> enemies = new ArrayList<>();

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

    public List<EnemyObject> getEnemies() {
        return enemies;
    }

    public void addEnemy(EnemyObject enemy) {
        this.enemies.add(enemy);
    }
}

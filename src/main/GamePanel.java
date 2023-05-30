package main;

import entity.Player;
import tile.BreakSetter;
import tile.PipeSetter;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GamePanel extends JPanel implements Runnable {
    // SCREEN SETTINGS
    public final int tileSize = 48;
    public final int screenCol = 16;
    public final int screenRow = 12;
    public final int screenWidth = tileSize * screenCol;
    public final int screenHeight = tileSize * screenRow;
    public final int loginState = 1;
    public final int titleState = 2;
    public final int playState = 3;
    public final int rankListState = 4;
    public final int shopState = 5;
    public final int profileState = 6;
    public final int animationState = 7;
    public final int chooseDifficultyState = 8; // 1: EASY, 2: MEDIUM, 3: HARD
    public final int chooseCharacterState = 9;
    public final int pauseState = 10;
    public final int chooseWhereToSaveState = 11;
    public final int loadMenuState = 12;
    // FPS SETTINGS
    final int FPS = 60;
    // GAME STATES
    public int gameState;
    public boolean levelFinished = false;
    public int difficulty = 1;
    // PLAYERS DATA
    public java.util.List<String> usernames = new ArrayList<>();
    public java.util.List<String> passwords = new ArrayList<>();
    // PLAYER SETTINGS
    public Player player = new Player(this, 48, 416);
    public Map<String, Game[]> savedGamesMap = new HashMap<>();
    public Map<String, Integer> scoresMap = new HashMap<>();
    public UI ui = new UI(this);
    public KeyboardHandler keyboardHandler = new KeyboardHandler(this);
    public MouseHandler mouseHandler = new MouseHandler(this);
    public TileManager tileManager = new TileManager(this);
    public CollisionDetector collisionDetector = new CollisionDetector(this);
    public BreakSetter breakSetter = new BreakSetter(this);
    public PipeSetter pipeSetter = new PipeSetter(this);
    public CoinSetter coinSetter = new CoinSetter(this);
    // SCENE SETTINGS
    public int timer = 60;
    double drawInterval = 1000000000D / FPS;
    // SYSTEM SETTINGS
    Thread thread;

    GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);

        this.addKeyListener(keyboardHandler);
        this.addMouseListener(mouseHandler);
    }

    public void setupGame() {
        gameState = loginState;
        loadPlayers();
    }

    public void playGame(int x, int y) {
        this.player.x = x;
        this.player.y = y;
    }

    public void loadPlayers() {
        InputStream is = Objects.requireNonNull(getClass().getResourceAsStream("/account data/data.txt"));
        InputStream is2 = Objects.requireNonNull(getClass().getResourceAsStream("/account data/load game.txt"));
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
        // lv - se - sc - time - hearts - score - coins
        try {
            while (true) {
                String line = br.readLine();
                if (line != null) {
                    String[] array = line.split(" - ");
                    usernames.add(array[0]);
                    passwords.add(array[1]);
                } else {
                    break;
                }
            }
            br.close();
            is.close();
            while (true) {
                String line = br2.readLine();
                if (line == null) {
                    break;
                }
                String username = line.split(" - ")[0];
                int score = Integer.parseInt(line.split(" - ")[1]);
                Game[] gamesArray = new Game[3];
                for (int i = 0; i < 3; i++) {
                    String nextLine = br2.readLine();
                    if (!nextLine.equals("null")) {
                        gamesArray[i] = new Game(nextLine.split(" - "));
                    }
                }
                savedGamesMap.put(username, gamesArray);
                scoresMap.put(username, score);
            }
            br2.close();
            is2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void signPlayer(String username, String password) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("res/account data/data.txt", true));
            bw.newLine();
            bw.write(username + " - " + password);
            bw.close();
            BufferedWriter bw2 = new BufferedWriter(new FileWriter("res/account data/load game.txt", true));
            bw2.newLine();
            bw2.write(String.format("%s - 0%nnull%nnull%nnull", username));
            bw2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startGameThread() {
        thread = new Thread(this);
        thread.start();
    }

    public void saveGame(boolean hasLost) {
        keyboardHandler.jumpPressed = false;
        keyboardHandler.leftPressed = false;
        keyboardHandler.rightPressed = false;
        InputStream is = Objects.requireNonNull(getClass().getResourceAsStream("/account data/load game.txt"));
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        int playerLine = 0;
        try {
            while (true) {
                String username = br.readLine().split(" - ")[0];
                if (username.contentEquals(ui.username)) {
                    break;
                }
                playerLine++;
            }

            Path path = Paths.get("res/account data/load game.txt");
            java.util.List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            String toWrite;
            if (!hasLost) {
                toWrite = String.format("%s - %s - %s - %s - %s - %s - %s - %s - %s", player.level, player.section, 1, timer, player.currentLife, player.score, player.coins, player.character, difficulty);
                savedGamesMap.get(ui.username.toString())[ui.saveSlot] = new Game(toWrite.split(" - "));
            } else {
                toWrite = "null";
                savedGamesMap.get(ui.username.toString())[ui.saveSlot] = null;
            }
            lines.set(playerLine + ui.saveSlot + 1, toWrite);
            Files.write(path, lines, StandardCharsets.UTF_8);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addScoreToFile() {
        InputStream is = Objects.requireNonNull(getClass().getResourceAsStream("/account data/load game.txt"));
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        int playerLine = 0;
        try {
            while (true) {
                String username = br.readLine();
                if (username.contentEquals(ui.username)) {
                    break;
                }
                playerLine++;
            }
            Path path = Paths.get("res/account data/load game.txt");
            java.util.List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            String toWrite = ui.username + " - " + scoresMap.get(ui.username.toString());
            lines.set(playerLine, toWrite);
            Files.write(path, lines, StandardCharsets.UTF_8);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (thread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                // UPDATE INFORMATION:
                update();

                // DRAW UPDATED INFORMATION:
                repaint();

                delta = 0;
            }
        }
    }


    public void update() {
        if (gameState == playState || gameState == animationState) {
            player.update();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;

        // LOGIN STATE
        if (gameState == loginState || gameState == titleState) {
            ui.paint(g2D);
        } else if (gameState == playState || gameState == animationState || gameState == chooseCharacterState || gameState == chooseDifficultyState || gameState == pauseState || gameState == chooseWhereToSaveState || gameState == loadMenuState || gameState == profileState || gameState == rankListState) {
            // TILES
            tileManager.paint(g2D);
            breakSetter.paint(g2D);
            pipeSetter.paint(g2D);
            coinSetter.paint(g2D);

            // PLAYER
            player.paint(g2D);

            // UI
            ui.paint(g2D);
        }

        g2D.dispose();
    }
}

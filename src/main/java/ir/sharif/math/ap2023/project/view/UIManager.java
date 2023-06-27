package ir.sharif.math.ap2023.project.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sharif.math.ap2023.project.controller.GameEngine;
import ir.sharif.math.ap2023.project.controller.GameLoader;
import ir.sharif.math.ap2023.project.controller.GameState;
import ir.sharif.math.ap2023.project.controller.sound.BackgroundMusicType;
import ir.sharif.math.ap2023.project.controller.sound.SoundEffectType;
import ir.sharif.math.ap2023.project.controller.sound.SoundManager;
import ir.sharif.math.ap2023.project.model.Database;
import ir.sharif.math.ap2023.project.model.block.BlockObject;
import ir.sharif.math.ap2023.project.model.block.Flag;
import ir.sharif.math.ap2023.project.model.block.GroundBlockObject;
import ir.sharif.math.ap2023.project.model.block.QuestionBlockObject;
import ir.sharif.math.ap2023.project.model.checkpoint.Checkpoint;
import ir.sharif.math.ap2023.project.model.enemy.Bowser;
import ir.sharif.math.ap2023.project.model.enemy.BowserFireball;
import ir.sharif.math.ap2023.project.model.enemy.EnemyObject;
import ir.sharif.math.ap2023.project.model.enemy.Piranha;
import ir.sharif.math.ap2023.project.model.game.SectionObject;
import ir.sharif.math.ap2023.project.model.item.Coin;
import ir.sharif.math.ap2023.project.model.item.Item;
import ir.sharif.math.ap2023.project.model.pipe.*;
import ir.sharif.math.ap2023.project.model.player.Difficulty;
import ir.sharif.math.ap2023.project.model.player.Fireball;
import ir.sharif.math.ap2023.project.model.player.Player;
import ir.sharif.math.ap2023.project.model.player.PlayerDirection;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class UIManager extends JPanel {
    private static UIManager instance;
    private final int screenCol = 26;
    private final int screenRow = 14;
    private final int tileSize = 48;
    private final int screenWidth = screenCol * tileSize;
    private final int screenHeight = screenRow * tileSize;
    private final Color transparentBlack = new Color(0, 0, 0, 175);
    public MainMenuItem mainMenuItem = MainMenuItem.NEW_GAME;
    public Difficulty difficultyOption = Difficulty.EASY;
    public int saveOption = 1;
    @JsonIgnore
    public int congratsTimer = 0;
    private Font font;
    private ImageLoader imageLoader;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton signInButton, signUpButton;
    private int timer = 0;

    private UIManager() {
        imageLoader = ImageLoader.getInstance();
        Database database = Database.getInstance();

        setDoubleBuffered(true);
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setLayout(null);

        try (InputStream is = Objects.requireNonNull(getClass().getResourceAsStream("/Super Mario Bros.NES.ttf"))) {
            font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(12f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        usernameField = new JTextField();
        usernameField.setBounds(screenWidth / 2 + tileSize, screenHeight / 2 - 3 * tileSize, 10 * tileSize, 3 * tileSize / 4);
        usernameField.setFont(font);
        usernameField.setBorder(BorderFactory.createTitledBorder("Username:"));
        usernameField.setOpaque(true);
        passwordField = new JPasswordField();
        passwordField.setBounds(screenWidth / 2 + tileSize, screenHeight / 2 - 3 * tileSize / 2, 10 * tileSize, 3 * tileSize / 4);
        passwordField.setFont(font);
        passwordField.setBorder(BorderFactory.createTitledBorder("Password:"));
        passwordField.setOpaque(true);
        add(usernameField);
        add(passwordField);

        signInButton = new JButton("Sign In");
        signInButton.setBounds(usernameField.getX(), screenHeight / 2, 4 * tileSize, tileSize);
        signInButton.setFont(font);
        signInButton.addActionListener(e -> {
            String username = usernameField.getText();
            Player player = database.findUserByUsername(username);
            if (player == null) {
                JOptionPane.showMessageDialog(null, "Username doesn't exist!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                String password = new String(passwordField.getPassword());
                if (!player.getPassword().equals(password)) {
                    JOptionPane.showMessageDialog(null, "Wrong password!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    passwordField.setText("");
                    passwordField.setVisible(false);
                    usernameField.setText("");
                    usernameField.setVisible(false);
                    signUpButton.setVisible(false);
                    signInButton.setVisible(false);
                    GameEngine.getInstance().setGameState(GameState.MAIN_MENU);
                    GameEngine.getInstance().setPlayer(player);
                    GameEngine.getInstance().startGame();
                }
            }
        });
        signUpButton = new JButton("Sign Up");
        signUpButton.setBounds(usernameField.getX() + 6 * tileSize, screenHeight / 2, 4 * tileSize, tileSize);
        signUpButton.setFont(font);
        signUpButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (username == null || username.equals("")) {
                JOptionPane.showMessageDialog(null, "Username field is empty!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (password.equals("")) {
                JOptionPane.showMessageDialog(null, "Password field is empty!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (database.findUserByUsername(username) != null) {
                    JOptionPane.showMessageDialog(null, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    Player user = new Player(username, password);
                    database.signUpUser(user);
                    passwordField.setText("");
                    passwordField.setVisible(false);
                    usernameField.setText("");
                    usernameField.setVisible(false);
                    signUpButton.setVisible(false);
                    signInButton.setVisible(false);
                    GameEngine.getInstance().setGameState(GameState.MAIN_MENU);
                    GameEngine.getInstance().setPlayer(user);
                    GameEngine.getInstance().startGame();
                }
            }
        });
        add(signInButton);
        add(signUpButton);
    }

    public static UIManager getInstance() {
        if (instance == null) {
            instance = new UIManager();
        }
        return instance;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        GameEngine gameEngine = GameEngine.getInstance();

        Graphics2D g2D = (Graphics2D) g;

        switch (gameEngine.getGameState()) {
            case LOGIN_MENU -> drawLoginMenu(g2D);
            case MAIN_MENU -> drawMainMenu(g2D);
            case SELECT_SAVE_SLOT -> drawSelectSaveSlot(g2D);
            case SELECT_DIFFICULTY -> drawSelectDifficulty(g2D);
            case PLAYING, SCENE, CHECKPOINT -> {
                g2D.drawImage(imageLoader.gameBackground, 0, 0, 26 * tileSize, 10 * tileSize, null);
                drawItems(g2D);
                drawPipes(g2D);
                drawMap(g2D);
                drawEnemies(g2D);
                drawPlayer(g2D);
                drawInfo(g2D);
                drawCheckpointPanel(g2D);
            }
            case BOSS_DEAD -> drawCongrats(g2D);
        }

        g2D.dispose();
    }

    private void drawCheckpointPanel(Graphics2D g2D) {
        if (GameEngine.getInstance().getGameState() == GameState.CHECKPOINT) {
            g2D.setColor(transparentBlack);
            g2D.fillRect(0, 0, screenWidth, screenHeight);
            Player player = GameEngine.getInstance().getPlayer();
            List<SectionObject> sections = GameLoader.getInstance("config.json").getGame().getLevels().get(player.getLevel() - 1).getSections();
            double fullLength = 0;
            double progressedLength = 0;
            for (int i = 0; i < sections.size(); i++) {
                fullLength += sections.get(i).getLength();
                if (player.getSection() - 1 > i)
                    progressedLength += sections.get(i).getLength();
                else if (player.getSection() - 1 == i)
                    progressedLength += player.getX() / tileSize;
            }
            int progressRisk = ((int) ((progressedLength / fullLength) * player.getCoins()));
            Checkpoint checkpoint = GameLoader.getInstance("config.json").getGame().getLevels().get(player.getLevel() - 1).getSections().get(player.getSection() - 1).getCheckpoint();
            checkpoint.setProgressRisk(progressRisk);

            g2D.setFont(font.deriveFont(Font.BOLD, 26F));
            g2D.setColor(Color.WHITE);

            String text = "Press [Enter] to save for " + progressRisk + " coins!";
            int x = getXOfCenteredText(text, g2D);
            g2D.drawString(
                    text,
                    x,
                    264
            );
            text = "Press [Esc] to skip and  receive " + progressRisk / 4 + " coins";
            x = getXOfCenteredText(text, g2D);
            g2D.drawString(
                    text,
                    x,
                    408
            );
        }
    }

    private void drawEnemies(Graphics2D g2D) {
        GameLoader gameLoader = GameLoader.getInstance("config.json");
        Player player = GameEngine.getInstance().getPlayer();
        SectionObject sectionObject = gameLoader.getGame().getLevels().get(player.getLevel() - 1).getSections().get(player.getSection() - 1);

        List<EnemyObject> toBeRemoved = new ArrayList<>();

        for (EnemyObject enemy : sectionObject.getEnemies()) {
            g2D.drawImage(
                    enemy.getImage(),
                    (int) enemy.getSolidArea().getX(),
                    (int) enemy.getSolidArea().getY(),
                    (int) enemy.getSolidArea().getWidth(),
                    (int) enemy.getSolidArea().getHeight(),
                    null
            );
            if (enemy instanceof Bowser) {
                g2D.setFont(g2D.getFont().deriveFont(42f));
                g2D.drawString(
                        String.valueOf(((Bowser) enemy).jumpCoolDown),
                        48,
                        192
                );
                for (BowserFireball fireball : ((Bowser) enemy).getFireballs()) {
                    g2D.drawImage(
                            imageLoader.fireballImages[2],
                            (int) fireball.getX(),
                            (int) fireball.getY(),
                            2 * tileSize,
                            2 * tileSize,
                            null
                    );
                }
                g2D.fillRect(
                        516,
                        567,
                        281 * ((Bowser) enemy).getHP() / 20,
                        66
                );
                g2D.drawImage(
                        ImageLoader.getInstance().healthBar,
                        444,
                        552,
                        360,
                        96,
                        null
                );
                if (((Bowser) enemy).bomb != null) {
                    g2D.drawImage(
                            imageLoader.bomb,
                            (int) ((Bowser) enemy).bomb.getX(),
                            (int) ((Bowser) enemy).bomb.getY(),
                            tileSize,
                            tileSize,
                            null
                    );
                }
            }
            if (enemy.isDead()) {
                enemy.addFrame();
                if (enemy.getFrame() >= 75) {
                    toBeRemoved.add(enemy);
                }
            }
        }

        for (EnemyObject enemyObject : toBeRemoved) {
            sectionObject.getEnemies().remove(enemyObject);
        }
        toBeRemoved.clear();
    }

    private void drawInfo(Graphics2D g2D) {
        g2D.setFont(font.deriveFont(Font.PLAIN, 26F));
        g2D.setColor(Color.BLACK);

        Player player = GameEngine.getInstance().getPlayer();

        timer++;
        if (timer >= 60) {
            player.decreaseTime();
            timer = 0;
        }

        int x = tileSize / 2;
        int y = tileSize;
        int y2 = tileSize * 7 / 4;

        g2D.drawString("SCORE", x, y);
        g2D.drawString(String.valueOf(player.getScore()), x, y2);
        x += 5.5 * tileSize;
        g2D.drawString("COINS", x, y);
        g2D.drawString(String.valueOf(player.getCoins()), x, y2);
        x += 5.5 * tileSize;
        g2D.drawString("WORLD", x, y);
        g2D.drawString(player.getLevel() + " - " + player.getSection(), x, y2);
        x += 5.5 * tileSize;
        g2D.drawString("TIME", x, y);
        g2D.drawString(String.valueOf(player.getTime()), x, y2);
        x += 5.5 * tileSize;
        g2D.drawString("LIVES", x, y);
        g2D.drawString(String.valueOf(player.getHearts()), x, y2);
    }

    private void drawPipes(Graphics2D g2D) {
        GameLoader gameLoader = GameLoader.getInstance("config.json");
        Player player = GameEngine.getInstance().getPlayer();
        SectionObject sectionObject = gameLoader.getGame().getLevels().get(player.getLevel() - 1).getSections().get(player.getSection() - 1);

        for (PipeObject pipe : sectionObject.getPipes()) {
            if ((pipe instanceof PiranhaTrapPipe || pipe instanceof TelePiranhaPipe) && ((PiranhaTrapPipe) pipe).getPiranha() != null) {
                Piranha piranha = ((PiranhaTrapPipe) pipe).getPiranha();
                if (piranha.isAlive()) {
                    for (Fireball fireball : player.getFireballs()) {
                        if (fireball.getRightBounds().intersects(piranha.getSolidArea())) {
                            ((PiranhaTrapPipe) pipe).killPiranha();
                            fireball.setDestroyed(true);
                        }
                    }
                    if (player.hasSword) {
                        if (player.sword.getBounds(player).intersects(piranha.getSolidArea())) {
                            ((PiranhaTrapPipe) pipe).killPiranha();
                            player.sword.setDestroyed(true);
                        }
                    }
                    if (piranha.getSolidArea().intersects(player.getSolidArea()) && player.getDirection() != PlayerDirection.DEAD) {
                        if (player.isInvincible()) {
                            ((PiranhaTrapPipe) pipe).killPiranha();
                        } else if (!player.isEnemyInvincible()) {
                            if (player.getCharacterState() > 0) {
                                player.setCharacterState(0);
                                player.setEnemyInvincible(true);
                            } else {
                                player.decreaseHearts();
                                GameEngine.getInstance().setGameState(GameState.SCENE);
                                player.setDirection(PlayerDirection.DEAD);
                                player.setCharacterState(0);
                                SoundManager soundManager = SoundManager.getInstance();
                                soundManager.pauseMusic();
                                soundManager.playSoundEffect(SoundEffectType.GAME_OVER);
                                player.setSpeedX(0);
                                player.setSpeedY(5);
                                player.setFalling(false);
                                player.setJumping(true);
                            }
                        }
                    }
                    piranha.updateLocation();
                    piranha.addFrame();
                    if (piranha.getFrame() >= 32)
                        piranha.setFrame(0);
                    g2D.drawImage(
                            imageLoader.piranhaImages[piranha.getFrame() / 16],
                            piranha.getX(),
                            piranha.getY(),
                            tileSize,
                            77,
                            null
                    );
                }
            }
            g2D.drawImage(
                    pipe instanceof ExitPipe ? imageLoader.getPipeImage(2) : imageLoader.getPipeImage(1),
                    pipe.getX() * tileSize,
                    pipe.getY() * tileSize,
                    2 * tileSize,
                    2 * tileSize,
                    null
            );
        }

        for (TrunkPipe trunkPipe : sectionObject.trunkPipes) {
            g2D.drawImage(
                    imageLoader.getPipeImage(0),
                    trunkPipe.getX() * tileSize,
                    trunkPipe.getY() * tileSize,
                    2 * tileSize,
                    2 * tileSize,
                    null
            );
        }
    }

    private void drawItems(Graphics2D g2D) {
        List<Item> items = GameEngine.getInstance().getItems();

        for (Item item : items) {
            if (item instanceof Coin) {
                ((Coin) item).frame++;
                if (((Coin) item).frame >= 32)
                    ((Coin) item).frame = 0;
            }
            g2D.drawImage(
                    item.getImage(),
                    (int) item.getX(),
                    (int) item.getY(),
                    tileSize,
                    tileSize,
                    null
            );
        }
    }

    private void drawPlayer(Graphics2D g2D) {
        Player player = GameEngine.getInstance().getPlayer();
        BufferedImage image = player.getImage();

        g2D.drawImage(
                image,
                (int) player.getX(),
                (int) player.getY(),
                tileSize,
                player.getSolidArea().height,
                null
        );

        if (player.hasSword) {
            boolean isToRight = player.getDirection() == PlayerDirection.CROUCH_RIGHT || player.getDirection() == PlayerDirection.CROUCH_RIGHT_IDLE || player.getDirection() == PlayerDirection.RIGHT || player.getDirection() == PlayerDirection.IDLE_RIGHT || player.getDirection() == PlayerDirection.JUMP_IDLE_RIGHT || player.getDirection() == PlayerDirection.JUMP_RIGHT;
            if (!player.sword.released) {
                g2D.drawImage(
                        (isToRight) ? imageLoader.sword[1] : imageLoader.sword[0],
                        (int) ((isToRight) ? player.getX() + tileSize : player.getX() - 2 * tileSize),
                        (int) player.getY(),
                        2 * tileSize,
                        tileSize,
                        null
                );
            } else {
                g2D.drawImage(
                        (player.sword.speedX > 0) ? imageLoader.sword[1] : imageLoader.sword[0],
                        (int) player.sword.getX(),
                        (int) player.getY(),
                        2 * tileSize,
                        tileSize,
                        null
                );
            }
        }

        for (Fireball fireball : player.getFireballs()) {
            g2D.drawImage(
                    fireball.getImage(),
                    (int) fireball.getX(),
                    (int) fireball.getY(),
                    tileSize,
                    tileSize,
                    null
            );
        }
    }

    private void drawMap(Graphics2D g2D) {
        GameLoader gameLoader = GameLoader.getInstance("config.json");
        Player player = GameEngine.getInstance().getPlayer();

        int level = player.getLevel();
        int section = player.getSection();
        SectionObject sectionObject = gameLoader.getGame().getLevels().get(level - 1).getSections().get(section - 1);

        for (BlockObject blockObject : sectionObject.getBlocks()) {
            if (blockObject instanceof QuestionBlockObject) {
                ((QuestionBlockObject) blockObject).frame++;
                if (((QuestionBlockObject) blockObject).frame >= 45) {
                    ((QuestionBlockObject) blockObject).frame = 0;
                }
            }
            g2D.drawImage(
                    blockObject.getImage(),
                    blockObject.getX() * tileSize,
                    blockObject.getY() * tileSize,
                    tileSize,
                    (blockObject instanceof GroundBlockObject) ? 4 * tileSize : tileSize,
                    null
            );
        }
        if (sectionObject.getCheckpoint() != null) {
            Checkpoint checkpoint = sectionObject.getCheckpoint();
            g2D.drawImage(
                    ImageLoader.getInstance().checkpointFlag,
                    checkpoint.getX(),
                    checkpoint.getY(),
                    2 * tileSize,
                    2 * tileSize,
                    null
            );
        }
        if (sectionObject.getFlag() != null) {
            Flag flag = sectionObject.getFlag();
            flag.frame++;
            if (flag.frame >= 45)
                flag.frame = 0;
            g2D.drawImage(
                    flag.getFlagRod().getImage(),
                    flag.getFlagRod().getX(),
                    flag.getFlagRod().getY(),
                    tileSize,
                    8 * tileSize,
                    null
            );
            g2D.drawImage(
                    flag.getImage(),
                    flag.getX(),
                    flag.getY(),
                    tileSize,
                    tileSize,
                    null
            );
        }
    }

    private void drawCongrats(Graphics2D g2D) {
        congratsTimer++;
        if (congratsTimer >= 250) {
            congratsTimer = 0;
            GameEngine.getInstance().setGameState(GameState.MAIN_MENU);
            SoundManager.getInstance().pauseMusic();
            SoundManager.getInstance().playBackgroundMusic(BackgroundMusicType.OVERWORLD);
        } else {
            g2D.drawImage(imageLoader.menuScreen, 0, 0, null);

            g2D.setFont(font.deriveFont(Font.BOLD, 26F));

            int x, y;
            String text;

            text = "CONGRATS!";
            x = screenWidth / 5 + getXOfCenteredText(text, g2D);
            y = 6 * tileSize;
            g2D.setColor(Color.WHITE);
            g2D.drawString(text, x, y);
        }
    }

    private void drawSelectDifficulty(Graphics2D g2D) {
        g2D.drawImage(imageLoader.menuScreen, 0, 0, null);

        g2D.setFont(font.deriveFont(Font.BOLD, 26F));

        int x, y;
        String text;

        text = "EASY";
        x = screenWidth / 5 + getXOfCenteredText(text, g2D);
        y = 5 * tileSize;
        g2D.setColor(Color.WHITE);
        g2D.drawString(text, x, y);
        if (difficultyOption == Difficulty.EASY) {
            g2D.setColor(Color.GREEN);
            g2D.drawString(text, x, y);
            g2D.drawString(">", x - tileSize, y);
        }

        text = "MEDIUM";
        x = screenWidth / 5 + getXOfCenteredText(text, g2D);
        y += tileSize;
        g2D.setColor(Color.WHITE);
        g2D.drawString(text, x, y);
        if (difficultyOption == Difficulty.MEDIUM) {
            g2D.setColor(Color.GRAY);
            g2D.drawString(text, x, y);
            g2D.drawString(">", x - tileSize, y);
        }

        text = "HARD";
        x = screenWidth / 5 + getXOfCenteredText(text, g2D);
        y += tileSize;
        g2D.setColor(Color.WHITE);
        g2D.drawString(text, x, y);
        if (difficultyOption == Difficulty.HARD) {
            g2D.setColor(Color.RED);
            g2D.drawString(text, x, y);
            g2D.drawString(">", x - tileSize, y);
        }
    }

    private void drawSelectSaveSlot(Graphics2D g2D) {
        Player player = GameEngine.getInstance().getPlayer();

        g2D.drawImage(imageLoader.menuScreen, 0, 0, null);

        g2D.setFont(font.deriveFont(Font.BOLD, 26F));
        g2D.setColor(Color.WHITE);

        int x, y;
        String text;

        text = (player.getSavedPlayer()[0] == null) ? "1. EMPTY" : "1. SAVED GAME";
        x = screenWidth / 5 + getXOfCenteredText(text, g2D);
        y = 5 * tileSize;
        g2D.drawString(text, x, y);
        if (saveOption == 1) {
            g2D.drawString(">", x - tileSize, y);
        }

        text = (player.getSavedPlayer()[1] == null) ? "2. EMPTY" : "2. SAVED GAME";
        x = screenWidth / 5 + getXOfCenteredText(text, g2D);
        y += tileSize;
        g2D.drawString(text, x, y);
        if (saveOption == 2) {
            g2D.drawString(">", x - tileSize, y);
        }

        text = (player.getSavedPlayer()[2] == null) ? "3. EMPTY" : "3. SAVED GAME";
        x = screenWidth / 5 + getXOfCenteredText(text, g2D);
        y += tileSize;
        g2D.drawString(text, x, y);
        if (saveOption == 3) {
            g2D.drawString(">", x - tileSize, y);
        }
    }

    private void drawMainMenu(Graphics2D g2D) {
        GameEngine gameEngine = GameEngine.getInstance();
        gameEngine.window.requestFocusInWindow();
        g2D.drawImage(imageLoader.menuScreen, 0, 0, null);

        g2D.setFont(font.deriveFont(Font.BOLD, 26F));
        g2D.setColor(Color.WHITE);

        int x, y;
        String text;

        text = "Start New Game";
        x = screenWidth / 5 + getXOfCenteredText(text, g2D);
        y = 5 * tileSize;
        g2D.drawString(text, x, y);
        if (mainMenuItem == MainMenuItem.NEW_GAME) {
            g2D.drawString(">", x - tileSize, y);
        }

        text = "Continue";
        x = screenWidth / 5 + getXOfCenteredText(text, g2D);
        y += tileSize;
        g2D.drawString(text, x, y);
        if (mainMenuItem == MainMenuItem.CONTINUE) {
            g2D.drawString(">", x - tileSize, y);
        }

        text = "Highest Scores";
        x = screenWidth / 5 + getXOfCenteredText(text, g2D);
        y += tileSize;
        g2D.drawString(text, x, y);
        if (mainMenuItem == MainMenuItem.HIGHEST_SCORES) {
            g2D.drawString(">", x - tileSize, y);
        }

        text = "Shop";
        x = screenWidth / 5 + getXOfCenteredText(text, g2D);
        y += tileSize;
        g2D.drawString(text, x, y);
        if (mainMenuItem == MainMenuItem.SHOP) {
            g2D.drawString(">", x - tileSize, y);
        }

        text = "Profile";
        x = screenWidth / 5 + getXOfCenteredText(text, g2D);
        y += tileSize;
        g2D.drawString(text, x, y);
        if (mainMenuItem == MainMenuItem.PROFILE) {
            g2D.drawString(">", x - tileSize, y);
        }
    }

    private void drawLoginMenu(Graphics2D g2D) {
        g2D.drawImage(imageLoader.menuScreen, 0, 0, null);
        usernameField.repaint();
        passwordField.repaint();
        signInButton.repaint();
        signUpButton.repaint();
    }

    public int getXOfCenteredText(String text, Graphics2D g2D) {
        int length = (int) g2D.getFontMetrics().getStringBounds(text, g2D).getWidth();
        return (screenWidth - length) / 2;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getTileSize() {
        return tileSize;
    }
}

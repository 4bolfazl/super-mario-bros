package ir.sharif.math.ap2023.project.controller.inputManager;

import ir.sharif.math.ap2023.project.controller.GameEngine;
import ir.sharif.math.ap2023.project.controller.GameState;
import ir.sharif.math.ap2023.project.model.enemy.Bowser;
import ir.sharif.math.ap2023.project.model.player.Difficulty;
import ir.sharif.math.ap2023.project.model.player.Fireball;
import ir.sharif.math.ap2023.project.model.player.Player;
import ir.sharif.math.ap2023.project.model.player.PlayerDirection;
import ir.sharif.math.ap2023.project.view.MainMenuItem;
import ir.sharif.math.ap2023.project.view.UIManager;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public final class KeyboardHandler implements KeyListener {
    private static KeyboardHandler instance;
    public boolean downPressed = false, upPressed = false;
    public int downDelay = 0, upDelay = 0;

    private KeyboardHandler() {
    }

    public static KeyboardHandler getInstance() {
        if (instance == null) {
            instance = new KeyboardHandler();
        }
        return instance;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        GameEngine gameEngine = GameEngine.getInstance();
        int code = e.getKeyCode();

        switch (gameEngine.getGameState()) {
            case MAIN_MENU -> mainMenuHandler(code);
            case SELECT_SAVE_SLOT -> selectSaveSlotHandler(code);
            case SELECT_DIFFICULTY -> selectDifficultyHandler(code);
            case PLAYING -> playingHandler(code);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        GameEngine gameEngine = GameEngine.getInstance();
        Player player = gameEngine.getPlayer();
        int code = e.getKeyCode();

        if (gameEngine.getGameState() == GameState.PLAYING) {
            if (GameEngine.getInstance().boss != null && GameEngine.getInstance().boss.jumpAttacking) {
                switch (code) {
                    case KeyEvent.VK_DOWN -> {
                        if (player.isCrouching())
                            player.setDirection(PlayerDirection.CROUCH_RIGHT_IDLE);
                        else
                            player.setDirection(PlayerDirection.IDLE_RIGHT);
                        player.setSpeedX(0);
                    }
                    case KeyEvent.VK_UP -> {
                        if (player.isCrouching())
                            player.setDirection(PlayerDirection.CROUCH_LEFT_IDLE);
                        else
                            player.setDirection(PlayerDirection.IDLE_LEFT);
                        player.setSpeedX(0);
                    }
                    case KeyEvent.VK_RIGHT -> {
                        downPressed = false;
                        downDelay = 0;
                        if (player.getCharacterState() > 0) {
                            player.setCrouching(false);
                            switch (player.getDirection()) {
                                case CROUCH_LEFT -> player.setDirection(PlayerDirection.LEFT);
                                case CROUCH_LEFT_IDLE -> player.setDirection(PlayerDirection.IDLE_LEFT);
                                case CROUCH_RIGHT -> player.setDirection(PlayerDirection.RIGHT);
                                case CROUCH_RIGHT_IDLE -> player.setDirection(PlayerDirection.IDLE_RIGHT);
                            }
                        }
                    }
                    case KeyEvent.VK_CONTROL -> {
                        upPressed = false;
                        upDelay = 0;
                    }
                }
            } else if (GameEngine.getInstance().boss == null || (GameEngine.getInstance().boss != null && !GameEngine.getInstance().boss.jumpAttacking)) {
                switch (code) {
                    case KeyEvent.VK_RIGHT -> {
                        if (player.isCrouching())
                            player.setDirection(PlayerDirection.CROUCH_RIGHT_IDLE);
                        else
                            player.setDirection(PlayerDirection.IDLE_RIGHT);
                        player.setSpeedX(0);
                    }
                    case KeyEvent.VK_LEFT -> {
                        if (player.isCrouching())
                            player.setDirection(PlayerDirection.CROUCH_LEFT_IDLE);
                        else
                            player.setDirection(PlayerDirection.IDLE_LEFT);
                        player.setSpeedX(0);
                    }
                    case KeyEvent.VK_DOWN -> {
                        downPressed = false;
                        downDelay = 0;
                        if (player.getCharacterState() > 0) {
                            player.setCrouching(false);
                            switch (player.getDirection()) {
                                case CROUCH_LEFT -> player.setDirection(PlayerDirection.LEFT);
                                case CROUCH_LEFT_IDLE -> player.setDirection(PlayerDirection.IDLE_LEFT);
                                case CROUCH_RIGHT -> player.setDirection(PlayerDirection.RIGHT);
                                case CROUCH_RIGHT_IDLE -> player.setDirection(PlayerDirection.IDLE_RIGHT);
                            }
                        }
                    }
                    case KeyEvent.VK_UP -> {
                        upPressed = false;
                        upDelay = 0;
                    }
                }
            }
        }
    }

    private void playingHandler(int code) {
        Player player = GameEngine.getInstance().getPlayer();
        if (GameEngine.getInstance().boss == null || (!GameEngine.getInstance().boss.grabAttacking && !GameEngine.getInstance().boss.jumpAttacking)) {
            switch (code) {
                case KeyEvent.VK_RIGHT -> {
                    if (player.isCrouching())
                        player.setDirection(PlayerDirection.CROUCH_RIGHT);
                    else
                        player.setDirection(PlayerDirection.RIGHT);
                    player.setSpeedX(4);
                }
                case KeyEvent.VK_LEFT -> {
                    if (player.isCrouching())
                        player.setDirection(PlayerDirection.CROUCH_LEFT);
                    else
                        player.setDirection(PlayerDirection.LEFT);
                    player.setSpeedX(-4);
                }
                case KeyEvent.VK_DOWN -> {
                    downPressed = true;
                }
                case KeyEvent.VK_UP -> {
                    upPressed = true;
                }
                case KeyEvent.VK_CONTROL -> {
                    if (player.getCharacterState() == 2) {
                        if (player.getDirection() == PlayerDirection.IDLE_RIGHT || player.getDirection() == PlayerDirection.RIGHT || player.getDirection() == PlayerDirection.CROUCH_RIGHT || player.getDirection() == PlayerDirection.CROUCH_RIGHT_IDLE) {
                            if (player.getSpeedY() == 0) {
                                player.getFireballs().add(new Fireball((int) player.getX() + UIManager.getInstance().getTileSize(), (int) player.getY(), true));
                                if (GameEngine.getInstance().boss != null) {
                                    if (GameEngine.getInstance().boss.phase2 && !GameEngine.getInstance().boss.nukeAttackStarted && GameEngine.getInstance().boss.nukeCoolDown == 0)
                                        GameEngine.getInstance().boss.playersFireballs++;
                                }
                            }
                        } else if (player.getDirection() == PlayerDirection.IDLE_LEFT || player.getDirection() == PlayerDirection.LEFT || player.getDirection() == PlayerDirection.CROUCH_LEFT || player.getDirection() == PlayerDirection.CROUCH_LEFT_IDLE) {
                            if (player.getSpeedY() == 0) {
                                player.getFireballs().add(new Fireball((int) player.getX() - 2 * UIManager.getInstance().getTileSize() + UIManager.getInstance().getTileSize(), (int) player.getY(), false));
                                if (GameEngine.getInstance().boss != null) {
                                    if (GameEngine.getInstance().boss.phase2 && !GameEngine.getInstance().boss.nukeAttackStarted && GameEngine.getInstance().boss.nukeCoolDown == 0)
                                        GameEngine.getInstance().boss.playersFireballs++;
                                }
                            }
                        }
                    }
                }
                case KeyEvent.VK_SPACE -> {
                    if (player.hasSword) {
                        if (!player.sword.released)
                            player.shootSword();
                    }
                }
            }
        } else if (GameEngine.getInstance().boss != null && GameEngine.getInstance().boss.grabAttacking) {
            Bowser boss = GameEngine.getInstance().boss;
            switch (code) {
                case KeyEvent.VK_RIGHT -> {
                    if (boss.lastKey == 0)
                        boss.lastKey = KeyEvent.VK_RIGHT;
                    else if (boss.lastKey == KeyEvent.VK_LEFT) {
                        boss.pressedTimes++;
                        boss.lastKey = KeyEvent.VK_RIGHT;
                    }
                }
                case KeyEvent.VK_LEFT -> {
                    if (boss.lastKey == 0)
                        boss.lastKey = KeyEvent.VK_LEFT;
                    else if (boss.lastKey == KeyEvent.VK_RIGHT) {
                        boss.pressedTimes++;
                        boss.lastKey = KeyEvent.VK_LEFT;
                    }
                }
            }
        } else if (GameEngine.getInstance().boss != null && GameEngine.getInstance().boss.jumpAttacking) {
            switch (code) {
                case KeyEvent.VK_DOWN -> {
                    if (player.isCrouching())
                        player.setDirection(PlayerDirection.CROUCH_RIGHT);
                    else
                        player.setDirection(PlayerDirection.RIGHT);
                    player.setSpeedX(4);
                }
                case KeyEvent.VK_UP -> {
                    if (player.isCrouching())
                        player.setDirection(PlayerDirection.CROUCH_LEFT);
                    else
                        player.setDirection(PlayerDirection.LEFT);
                    player.setSpeedX(-4);
                }
                case KeyEvent.VK_RIGHT -> {
                    downPressed = true;
                }
                case KeyEvent.VK_CONTROL -> {
                    upPressed = true;
                }
                case KeyEvent.VK_LEFT -> {
                    if (player.getCharacterState() == 2) {
                        if (player.getDirection() == PlayerDirection.IDLE_RIGHT || player.getDirection() == PlayerDirection.RIGHT || player.getDirection() == PlayerDirection.CROUCH_RIGHT || player.getDirection() == PlayerDirection.CROUCH_RIGHT_IDLE) {
                            if (player.getSpeedY() == 0) {
                                player.getFireballs().add(new Fireball((int) player.getX() + UIManager.getInstance().getTileSize(), (int) player.getY(), true));
                                if (GameEngine.getInstance().boss != null) {
                                    if (GameEngine.getInstance().boss.phase2 && !GameEngine.getInstance().boss.nukeAttackStarted && GameEngine.getInstance().boss.nukeCoolDown == 0)
                                        GameEngine.getInstance().boss.playersFireballs++;
                                }
                            }
                        } else if (player.getDirection() == PlayerDirection.IDLE_LEFT || player.getDirection() == PlayerDirection.LEFT || player.getDirection() == PlayerDirection.CROUCH_LEFT || player.getDirection() == PlayerDirection.CROUCH_LEFT_IDLE) {
                            if (player.getSpeedY() == 0) {
                                player.getFireballs().add(new Fireball((int) player.getX() - 2 * UIManager.getInstance().getTileSize() + UIManager.getInstance().getTileSize(), (int) player.getY(), false));
                                if (GameEngine.getInstance().boss != null) {
                                    if (GameEngine.getInstance().boss.phase2 && !GameEngine.getInstance().boss.nukeAttackStarted && GameEngine.getInstance().boss.nukeCoolDown == 0)
                                        GameEngine.getInstance().boss.playersFireballs++;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void selectDifficultyHandler(int code) {
        UIManager uiManager = UIManager.getInstance();
        GameEngine gameEngine = GameEngine.getInstance();

        if (code == KeyEvent.VK_UP) {
            if (uiManager.difficultyOption.getCode() == 0) {
                uiManager.difficultyOption = Difficulty.getByCode(2);
            } else {
                uiManager.difficultyOption = Difficulty.getByCode(uiManager.difficultyOption.getCode() - 1);
            }
        } else if (code == KeyEvent.VK_DOWN) {
            if (uiManager.difficultyOption.getCode() == 2) {
                uiManager.difficultyOption = Difficulty.getByCode(0);
            } else {
                uiManager.difficultyOption = Difficulty.getByCode(uiManager.difficultyOption.getCode() + 1);
            }
        } else if (code == KeyEvent.VK_ENTER) {
            gameEngine.getPlayer().setDifficulty(uiManager.difficultyOption);
            gameEngine.setGameState(GameState.PLAYING);
        }
    }

    private void selectSaveSlotHandler(int code) {
        UIManager uiManager = UIManager.getInstance();
        GameEngine gameEngine = GameEngine.getInstance();

        if (code == KeyEvent.VK_UP) {
            if (uiManager.saveOption == 1) {
                uiManager.saveOption = 3;
            } else {
                uiManager.saveOption -= 1;
            }
        } else if (code == KeyEvent.VK_DOWN) {
            if (uiManager.saveOption == 3) {
                uiManager.saveOption = 1;
            } else {
                uiManager.saveOption += 1;
            }
        } else if (code == KeyEvent.VK_ENTER) {
            gameEngine.setGameState(GameState.SELECT_DIFFICULTY);
        }
    }

    private void mainMenuHandler(int code) {
        UIManager uiManager = UIManager.getInstance();
        GameEngine gameEngine = GameEngine.getInstance();

        if (code == KeyEvent.VK_UP) {
            if (uiManager.mainMenuItem.getCode() == 1) {
                uiManager.mainMenuItem = MainMenuItem.getByCode(5);
            } else {
                uiManager.mainMenuItem = MainMenuItem.getByCode(uiManager.mainMenuItem.getCode() - 1);
            }
        } else if (code == KeyEvent.VK_DOWN) {
            if (uiManager.mainMenuItem.getCode() == 5) {
                uiManager.mainMenuItem = MainMenuItem.getByCode(1);
            } else {
                uiManager.mainMenuItem = MainMenuItem.getByCode(uiManager.mainMenuItem.getCode() + 1);
            }
        } else if (code == KeyEvent.VK_ENTER) {
            switch (uiManager.mainMenuItem) {
                case NEW_GAME, CONTINUE -> gameEngine.setGameState(GameState.SELECT_SAVE_SLOT);
                case HIGHEST_SCORES -> gameEngine.setGameState(GameState.RANKING);
                case SHOP -> gameEngine.setGameState(GameState.SHOP);
                case PROFILE -> gameEngine.setGameState(GameState.PROFILE);
            }
        }
    }
}

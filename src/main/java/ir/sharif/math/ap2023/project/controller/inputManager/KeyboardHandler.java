package ir.sharif.math.ap2023.project.controller.inputManager;

import ir.sharif.math.ap2023.project.controller.GameEngine;
import ir.sharif.math.ap2023.project.controller.GameState;
import ir.sharif.math.ap2023.project.controller.sound.SoundEffectType;
import ir.sharif.math.ap2023.project.controller.sound.SoundManager;
import ir.sharif.math.ap2023.project.model.player.Difficulty;
import ir.sharif.math.ap2023.project.model.player.Player;
import ir.sharif.math.ap2023.project.model.player.PlayerDirection;
import ir.sharif.math.ap2023.project.view.MainMenuItem;
import ir.sharif.math.ap2023.project.view.UIManager;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public final class KeyboardHandler implements KeyListener {
    private static KeyboardHandler instance;

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
            }
        }
    }

    private void playingHandler(int code) {
        Player player = GameEngine.getInstance().getPlayer();
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
                if (player.getCharacterState() > 0) {
                    player.setCrouching(true);
                    switch (player.getDirection()) {
                        case IDLE_RIGHT -> player.setDirection(PlayerDirection.CROUCH_RIGHT_IDLE);
                        case RIGHT -> player.setDirection(PlayerDirection.CROUCH_RIGHT);
                        case IDLE_LEFT -> player.setDirection(PlayerDirection.CROUCH_LEFT_IDLE);
                        case LEFT -> player.setDirection(PlayerDirection.CROUCH_LEFT);
                    }
                }
            }
            case KeyEvent.VK_UP -> {
                if (!player.isJumping() && !player.isFalling()) {
                    SoundManager soundManager = SoundManager.getInstance();
                    soundManager.playSoundEffect(SoundEffectType.JUMP);
                    switch (player.getDirection()) {
                        case IDLE_RIGHT -> {
                            player.setDirection(PlayerDirection.JUMP_IDLE_RIGHT);
                            player.setSpeedY(13);
                            player.setJumping(true);
                        }
                        case IDLE_LEFT -> {
                            player.setDirection(PlayerDirection.JUMP_IDLE_LEFT);
                            player.setSpeedY(13);
                            player.setJumping(true);
                        }
                        case RIGHT -> {
                            player.setDirection(PlayerDirection.JUMP_RIGHT);
                            player.setSpeedY(13);
                            player.setSpeedX(4);
                            player.setJumping(true);
                        }
                        case LEFT -> {
                            player.setDirection(PlayerDirection.JUMP_LEFT);
                            player.setSpeedY(13);
                            player.setSpeedX(-4);
                            player.setJumping(true);
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

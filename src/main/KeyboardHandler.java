package main;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardHandler implements KeyListener {
    public boolean jumpPressed, rightPressed, leftPressed;

    GamePanel gp;

    public KeyboardHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // EMPTY
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (gp.gameState == gp.loginState) {
            loginStateKeyHandler(e);
        } else if (gp.gameState == gp.titleState) {
            titleStateKeyHandler(e);
        } else if (gp.gameState == gp.chooseDifficultyState) {
            difficultyHandler(e);
        } else if (gp.gameState == gp.chooseCharacterState) {
            characterChoosing(e);
        } else if (gp.gameState == gp.pauseState) {
            pauseHandler(e);
        } else if (gp.gameState == gp.loadMenuState) {
            loadMenu(e);
        } else if (gp.gameState == gp.profileState) {
            profile(e);
        } else if (gp.gameState == gp.chooseWhereToSaveState) {
            whereToSave(e);
        } else if (gp.gameState == gp.playState) {
            if (code == KeyEvent.VK_RIGHT) {
                rightPressed = true;
            }
            if (code == KeyEvent.VK_LEFT) {
                leftPressed = true;
            }
            if (code == KeyEvent.VK_UP) {
                jumpPressed = true;
            }
            if (code == KeyEvent.VK_ESCAPE) {
                gp.gameState = gp.pauseState;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (gp.gameState == gp.playState) {
            if (code == KeyEvent.VK_RIGHT) {
                rightPressed = false;
            }
            if (code == KeyEvent.VK_LEFT) {
                leftPressed = false;
            }
        }
    }

    public void profile(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_RIGHT) {
            if (gp.ui.charNumber == gp.player.chars.length - 1) {
                gp.ui.charNumber = 0;
            } else {
                gp.ui.charNumber++;
            }
        } else if (code == KeyEvent.VK_LEFT) {
            if (gp.ui.charNumber == 0) {
                gp.ui.charNumber = gp.player.chars.length - 1;
            } else {
                gp.ui.charNumber--;
            }
        } else if (code == KeyEvent.VK_ESCAPE) {
            gp.player.character = gp.player.chars[gp.ui.charNumber];
            gp.gameState = gp.titleState;
        }
    }

    public void loadMenu(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_ENTER) {
            if (gp.savedGamesMap.get(gp.ui.username.toString())[gp.ui.saveSlot] == null) {
                JOptionPane.showMessageDialog(null, "This slot is empty", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                Game game = gp.savedGamesMap.get(gp.ui.username.toString())[gp.ui.saveSlot];
                gp.player.level = game.lv;
                gp.player.section = game.se;
                gp.player.scene = game.sc;
                gp.timer = 60;
                gp.player.currentLife = game.hearts;
                gp.player.score = game.score;
                gp.player.coins = game.coins;
                gp.player.character = game.character;
                gp.player.getImages(gp.player.character);
                gp.player.x = 48;
                gp.player.y = 416;
                gp.tileManager.worldShift = (gp.player.section - 1) * gp.tileSize * 16;
                gp.difficulty = game.difficulty;
                gp.gameState = gp.playState;
            }
        } else if (code == KeyEvent.VK_DOWN) {
            if (gp.ui.loadSlot == 2) {
                gp.ui.loadSlot = 0;
            } else {
                gp.ui.loadSlot++;
            }
        } else if (code == KeyEvent.VK_UP) {
            if (gp.ui.loadSlot == 0) {
                gp.ui.loadSlot = 2;
            } else {
                gp.ui.loadSlot--;
            }
        }
    }

    public void whereToSave(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_ENTER) {
            gp.gameState = gp.chooseDifficultyState;
        } else if (code == KeyEvent.VK_DOWN) {
            if (gp.ui.saveSlot == 2) {
                gp.ui.saveSlot = 0;
            } else {
                gp.ui.saveSlot++;
            }
        } else if (code == KeyEvent.VK_UP) {
            if (gp.ui.saveSlot == 0) {
                gp.ui.saveSlot = 2;
            } else {
                gp.ui.saveSlot--;
            }
        }
    }

    public void pauseHandler(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_ENTER) {
            gp.saveGame(false);
            gp.gameState = gp.titleState;
        } else if (code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.playState;
        }
    }

    public void characterChoosing(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_RIGHT) {
            if (gp.ui.charNumber == gp.player.chars.length - 1) {
                gp.ui.charNumber = 0;
            } else {
                gp.ui.charNumber++;
            }
        } else if (code == KeyEvent.VK_LEFT) {
            if (gp.ui.charNumber == 0) {
                gp.ui.charNumber = gp.player.chars.length - 1;
            } else {
                gp.ui.charNumber--;
            }
        } else if (code == KeyEvent.VK_ENTER) {
            gp.player.character = gp.player.chars[gp.ui.charNumber];
            gp.playGame(48, 416);
            gp.gameState = gp.playState;
        }
    }

    public void difficultyHandler(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_UP) {
            if (gp.ui.commandNumber2 == 1) {
                gp.ui.commandNumber2 = 3;
            } else {
                gp.ui.commandNumber2--;
            }
        } else if (code == KeyEvent.VK_DOWN) {
            if (gp.ui.commandNumber2 == 3) {
                gp.ui.commandNumber2 = 1;
            } else {
                gp.ui.commandNumber2++;
            }
        } else if (code == KeyEvent.VK_ENTER) {
            gp.difficulty = gp.ui.commandNumber2;
            gp.gameState = gp.chooseCharacterState;
        }
    }

    public void titleStateKeyHandler(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_UP) {
            if (gp.ui.commandNumber == 0) {
                gp.ui.commandNumber = 4;
            } else {
                gp.ui.commandNumber--;
            }
        } else if (code == KeyEvent.VK_DOWN) {
            if (gp.ui.commandNumber == 4) {
                gp.ui.commandNumber = 0;
            } else {
                gp.ui.commandNumber++;
            }
        } else if (code == KeyEvent.VK_ENTER) {
            if (gp.ui.commandNumber == 0) {
                // New Game
                gp.timer = 60;
                gp.player.x = 48;
                gp.player.y = 416;
                gp.tileManager.worldShift = 0;
                gp.player.scene = 1;
                gp.player.section = 1;
                gp.player.currentLife = 3;
                gp.player.coins = 0;
                gp.player.direction = "right";
                gp.keyboardHandler.jumpPressed = false;
                gp.keyboardHandler.leftPressed = false;
                gp.keyboardHandler.rightPressed = false;
                gp.gameState = gp.chooseWhereToSaveState;
            } else if (gp.ui.commandNumber == 1) {
                // Load Game
                gp.gameState = gp.loadMenuState;
            } else if (gp.ui.commandNumber == 2) {
                gp.gameState = gp.rankListState;
            } else if (gp.ui.commandNumber == 3) {
                gp.gameState = gp.shopState;
            } else if (gp.ui.commandNumber == 4) {
                gp.gameState = gp.profileState;
            }
        }
    }

    public void loginStateKeyHandler(KeyEvent e) {
        char keyChar = e.getKeyChar();

        if ((keyChar >= 65 && keyChar <= 90) || (keyChar >= 97 && keyChar <= 122) || (keyChar >= 48 && keyChar <= 57)) {
            if (gp.ui.fieldSelected == 0) {
                if (gp.ui.username.length() < 23) {
                    gp.ui.username.append(keyChar);
                } else {
                    JOptionPane.showMessageDialog(null, "The username must be no longer than 23 characters!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                if (gp.ui.password.length() < 23) {
                    gp.ui.password.append(keyChar);
                } else {
                    JOptionPane.showMessageDialog(null, "The password must be no longer than 23 characters!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (keyChar == KeyEvent.VK_BACK_SPACE) {
            if (gp.ui.fieldSelected == 0) {
                if (gp.ui.username.length() != 0) {
                    gp.ui.username.deleteCharAt(gp.ui.username.length() - 1);
                }
            } else {
                if (gp.ui.password.length() != 0) {
                    gp.ui.password.deleteCharAt(gp.ui.password.length() - 1);
                }
            }
        } else if (keyChar == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }
}

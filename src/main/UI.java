package main;

import object.Coin;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class UI {
    public int fieldSelected = 0; // 0: username, 1: password
    public int commandNumber = 0;
    public int commandNumber2 = 1;
    public int saveSlot = 0;
    public int loadSlot = 0;
    public int charNumber = 0;
    public int timerCounter = 0;
    public int finishedCounter = 0;
    public Font maruMonica;
    public StringBuilder username = new StringBuilder(), password = new StringBuilder();
    GamePanel gp;
    BufferedImage logo, backgroundLoginState, textFieldBorders;

    public UI(GamePanel gp) {
        this.gp = gp;

        InputStream is = Objects.requireNonNull(getClass().getResourceAsStream("/fonts/MaruMonica.ttf"));
        try {
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
            logo = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/icon/icon.png")));
            backgroundLoginState = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/background/login state.jpg")));
            textFieldBorders = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/borders/text field.png")));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    public void paint(Graphics2D g2D) {
        g2D.setFont(maruMonica);

        if (gp.gameState == gp.loginState) {
            paintLoginScreen(g2D);
        } else if (gp.gameState == gp.titleState) {
            paintTitleScreen(g2D);
        } else if (gp.gameState == gp.chooseDifficultyState) {
            paintDifficultyState(g2D);
        } else if (gp.gameState == gp.rankListState) {
            paintRankList(g2D);
        } else if (gp.gameState == gp.chooseCharacterState) {
            paintCharacterChoosing(g2D);
        } else if (gp.gameState == gp.loadMenuState) {
            paintLoadMenu(g2D);
        } else if (gp.gameState == gp.chooseWhereToSaveState) {
            paintWhereToSave(g2D);
        } else if (gp.gameState == gp.profileState) {
            paintProfile(g2D);
        } else if (gp.gameState == gp.playState || gp.gameState == gp.animationState || gp.gameState == gp.pauseState) {
            if (gp.gameState == gp.pauseState) {
                g2D.setColor(new Color(0, 0, 0, 220));
                g2D.fillRoundRect(gp.tileSize * 5, gp.tileSize * 4, gp.tileSize * 6, gp.tileSize * 4, 5, 5);
                g2D.setColor(Color.WHITE);
                g2D.setFont(maruMonica.deriveFont(32F));
                g2D.drawString("Press Enter to exit.", getXOfCenteredText("Press Enter to exit.", g2D), 6 * gp.tileSize);
            }
            g2D.setFont(maruMonica.deriveFont(45F));
            g2D.setColor(Color.WHITE);
            if (gp.levelFinished) {
                g2D.setFont(g2D.getFont().deriveFont(Font.BOLD, 44F));
                g2D.drawString("Level Finished", getXOfCenteredText("Level Finished", g2D), gp.screenHeight / 2);
                g2D.drawString("Score: " + gp.player.score, getXOfCenteredText("Score: " + gp.player.scene, g2D), gp.screenHeight / 2 + 2 * gp.tileSize);
                finishedCounter++;
                if (finishedCounter >= 180) {
                    gp.gameState = gp.titleState;
                    finishedCounter = 0;
                    gp.player.score = 0;
                    gp.levelFinished = false;
                    for (Coin coin : gp.coinSetter.coins) {
                        coin.visible = true;
                    }
                }
            } else {
                if (gp.timer > 0) {
                    if (timerCounter >= 60) {
                        gp.timer--;
                        timerCounter = 0;
                    }
                    g2D.drawString("Time:", gp.tileSize, gp.tileSize);
                    g2D.drawString(String.valueOf(gp.timer), gp.tileSize, 2 * gp.tileSize);
                    if (gp.gameState != gp.pauseState) {
                        timerCounter++;
                    }
                } else {
                    gp.player.currentLife--;
                    gp.timer = 60;
                    gp.player.x = 48;
                    gp.player.y = 416;
                    gp.player.scene = 1;
                    gp.player.section = 1;
                    gp.tileManager.worldShift = 0;
                    gp.player.direction = "right";
                }
                g2D.drawString("Hearts:", 4 * gp.tileSize, gp.tileSize);
                g2D.drawString(String.valueOf(gp.player.currentLife), 4 * gp.tileSize, 2 * gp.tileSize);
                g2D.drawString("Score: ", 8 * gp.tileSize, gp.tileSize);
                g2D.drawString(String.valueOf(gp.player.score), 8 * gp.tileSize, 2 * gp.tileSize);
                g2D.drawString("Lv-Se-Sc:", 12 * gp.tileSize, gp.tileSize);
                g2D.drawString(String.format("%s-%s-%s", gp.player.level, gp.player.section, gp.player.scene), 12 * gp.tileSize, 2 * gp.tileSize);
                g2D.drawImage(gp.coinSetter.image, gp.tileSize - 15, 117, gp.tileSize, gp.tileSize, null);
                g2D.drawString("x " + gp.player.coins, 90, 3 * gp.tileSize + 16);
            }
        }
    }

    public void paintRankList(Graphics2D g2D) {
        g2D.drawImage(backgroundLoginState, 0, 0, gp.screenWidth, gp.screenHeight, null);
        g2D.setFont(g2D.getFont().deriveFont(Font.BOLD, 36F));

        ArrayList<Integer> list = new ArrayList<>(gp.scoresMap.values());
        list.sort(Comparator.comparingInt(value -> -value));

        int i = 1;
        for (Integer integer : list) {
            for (String user : gp.scoresMap.keySet()) {
                if (gp.scoresMap.get(user) == integer) {
                    g2D.drawString(user + ": " + integer, getXOfCenteredText(user + ": " + integer, g2D), i*gp.tileSize);
                    i++;
                    break;
                }
            }
            if (i == 4) {
                break;
            }
        }
    }

    public void paintProfile(Graphics2D g2D) {
        g2D.drawImage(backgroundLoginState, 0, 0, gp.screenWidth, gp.screenHeight, null);

        g2D.setFont(g2D.getFont().deriveFont(Font.BOLD, 36F));

        g2D.drawString(gp.ui.username.toString(), getXOfCenteredText(gp.ui.username.toString(), g2D), 10 * gp.tileSize);
        g2D.drawString(String.valueOf(gp.scoresMap.get(gp.ui.username.toString())), getXOfCenteredText(String.valueOf(gp.scoresMap.get(gp.ui.username.toString())), g2D), 11 * gp.tileSize);

        gp.player.getImages(gp.player.chars[charNumber]);
        g2D.drawImage(gp.player.standRight, 7 * gp.tileSize, 2 * gp.tileSize, 2 * gp.tileSize, 4 * gp.tileSize, null);
        g2D.drawString(gp.player.chars[charNumber], getXOfCenteredText(gp.player.chars[charNumber], g2D), 8 * gp.tileSize);
        g2D.drawString("<", gp.tileSize * 6, gp.tileSize * 4);
        g2D.drawString(">", gp.tileSize * 10, gp.tileSize * 4);
    }

    public void paintLoadMenu(Graphics2D g2D) {
        g2D.drawImage(backgroundLoginState, 0, 0, gp.screenWidth, gp.screenHeight, null);

        g2D.setFont(g2D.getFont().deriveFont(Font.BOLD, 36F));
        int x, y = gp.tileSize * 4;
        ;
        String text;

        int i = 1;
        for (int j = 0; j < 3; j++) {
            if (gp.savedGamesMap.get(gp.ui.username.toString())[j] != null) {
                text = "Saved Game " + i;
                i++;
                x = getXOfCenteredText(text, g2D);
                y += gp.tileSize;
                g2D.drawString(text, x, y);
                if (loadSlot == j) {
                    g2D.drawString(">", x - gp.tileSize, y);
                }
            } else {
                text = "Empty";
                x = getXOfCenteredText(text, g2D);
                y += gp.tileSize;
                g2D.drawString(text, x, y);
                if (loadSlot == j) {
                    g2D.drawString(">", x - gp.tileSize, y);
                }
            }
        }
    }

    public void paintWhereToSave(Graphics2D g2D) {
        g2D.drawImage(backgroundLoginState, 0, 0, gp.screenWidth, gp.screenHeight, null);

        g2D.setFont(g2D.getFont().deriveFont(Font.BOLD, 36F));
        int x, y = gp.tileSize * 4;
        ;
        String text;

        int i = 1;
        for (int j = 0; j < 3; j++) {
            if (gp.savedGamesMap.get(gp.ui.username.toString())[j] != null) {
                text = "Saved Game " + i;
                i++;
                x = getXOfCenteredText(text, g2D);
                y += gp.tileSize;
                g2D.drawString(text, x, y);
                if (saveSlot == j) {
                    g2D.drawString(">", x - gp.tileSize, y);
                }
            } else {
                text = "Empty";
                x = getXOfCenteredText(text, g2D);
                y += gp.tileSize;
                g2D.drawString(text, x, y);
                if (saveSlot == j) {
                    g2D.drawString(">", x - gp.tileSize, y);
                }
            }
        }
    }

    public void paintCharacterChoosing(Graphics2D g2D) {
        g2D.drawImage(backgroundLoginState, 0, 0, gp.screenWidth, gp.screenHeight, null);

        g2D.setFont(g2D.getFont().deriveFont(Font.BOLD, 36F));
        gp.player.getImages(gp.player.chars[charNumber]);
        g2D.drawImage(gp.player.standRight, 7 * gp.tileSize, 2 * gp.tileSize, 2 * gp.tileSize, 4 * gp.tileSize, null);
        g2D.drawString(gp.player.chars[charNumber], getXOfCenteredText(gp.player.chars[charNumber], g2D), 8 * gp.tileSize);
        g2D.drawString("<", gp.tileSize * 6, gp.tileSize * 4);
        g2D.drawString(">", gp.tileSize * 10, gp.tileSize * 4);
    }

    public void paintDifficultyState(Graphics2D g2D) {
        g2D.drawImage(backgroundLoginState, 0, 0, gp.screenWidth, gp.screenHeight, null);

        g2D.setFont(g2D.getFont().deriveFont(Font.BOLD, 36F));
        int x, y;
        String text;

        text = "Easy";
        x = getXOfCenteredText(text, g2D);
        y = gp.tileSize * 7;
        g2D.drawString(text, x, y);
        if (commandNumber2 == 1) {
            g2D.drawString(">", x - gp.tileSize, y);
        }

        text = "Medium";
        x = getXOfCenteredText(text, g2D);
        y += gp.tileSize;
        g2D.drawString(text, x, y);
        if (commandNumber2 == 2) {
            g2D.drawString(">", x - gp.tileSize, y);
        }

        text = "Iran Mode";
        x = getXOfCenteredText(text, g2D);
        y += gp.tileSize;
        g2D.drawString(text, x, y);
        if (commandNumber2 == 3) {
            g2D.drawString(">", x - gp.tileSize, y);
        }
    }

    public void paintTitleScreen(Graphics2D g2D) {
        // BACKGROUND
        g2D.drawImage(backgroundLoginState, 0, 0, gp.screenWidth, gp.screenHeight, null);
        g2D.drawImage(logo, 240, 36, 288, 135, null);

        // OPTIONS
        g2D.setFont(g2D.getFont().deriveFont(Font.BOLD, 36F));
        int x, y;
        String text;

        text = "New Game";
        x = getXOfCenteredText(text, g2D);
        y = gp.tileSize * 5;
        g2D.drawString(text, x, y);
        if (commandNumber == 0) {
            g2D.drawString(">", x - gp.tileSize, y);
        }

        text = "Continue";
        x = getXOfCenteredText(text, g2D);
        y += gp.tileSize;
        g2D.drawString(text, x, y);
        if (commandNumber == 1) {
            g2D.drawString(">", x - gp.tileSize, y);
        }

        text = "Rank List";
        x = getXOfCenteredText(text, g2D);
        y += gp.tileSize;
        g2D.drawString(text, x, y);
        if (commandNumber == 2) {
            g2D.drawString(">", x - gp.tileSize, y);
        }

        text = "Shop";
        x = getXOfCenteredText(text, g2D);
        y += gp.tileSize;
        g2D.drawString(text, x, y);
        if (commandNumber == 3) {
            g2D.drawString(">", x - gp.tileSize, y);
        }

        text = "Profile";
        x = getXOfCenteredText(text, g2D);
        y += gp.tileSize;
        g2D.drawString(text, x, y);
        if (commandNumber == 4) {
            g2D.drawString(">", x - gp.tileSize, y);
        }
    }

    public void paintLoginScreen(Graphics2D g2D) {
        // BACKGROUND
        g2D.drawImage(backgroundLoginState, 0, 0, gp.screenWidth, gp.screenHeight, null);
        g2D.drawImage(logo, 240, 36, 288, 135, null);

        // FIELDS AND BUTTONS
        g2D.drawImage(textFieldBorders, 3 * gp.tileSize, 4 * gp.tileSize, 10 * gp.tileSize, 72, null);
        g2D.drawImage(textFieldBorders, 3 * gp.tileSize, 6 * gp.tileSize, 10 * gp.tileSize, 72, null);
        g2D.drawImage(textFieldBorders, 5 * gp.tileSize, 8 * gp.tileSize, 2 * gp.tileSize, 48, null);
        g2D.drawImage(textFieldBorders, 9 * gp.tileSize, 8 * gp.tileSize, 2 * gp.tileSize, 48, null);
        g2D.setColor(Color.BLACK);
        g2D.setFont(maruMonica.deriveFont(Font.BOLD, 32F));
        g2D.drawString("Login", 256, 416);
        g2D.drawString("Signup", 440, 416);

        // HINT TO EXIT
        g2D.setFont(maruMonica.deriveFont(Font.BOLD, 22F));
        g2D.drawString("Press Esc to exit.", getXOfCenteredText("Press Esc to exit.", g2D), 470);

        // USERNAME AND PASSWORD
        g2D.setFont(maruMonica.deriveFont(40F));
        g2D.drawString(username.toString(), 175, 240);
        StringBuilder hiddenPassword = new StringBuilder();
        for (int i = 0; i < password.length(); i++) {
            hiddenPassword.append("*");
        }
        g2D.drawString(hiddenPassword.toString(), 175, 347);
    }

    public int getXOfCenteredText(String text, Graphics2D g2D) {
        int length = (int) g2D.getFontMetrics().getStringBounds(text, g2D).getWidth();
        return (gp.screenWidth - length) / 2;
    }
}

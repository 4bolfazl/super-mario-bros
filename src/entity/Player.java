package entity;

import main.GamePanel;
import object.Coin;
import object.Pipe;
import tile.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Player extends Entity {
    public int level = 1, section = 1;
    public int scene = 1;
    public boolean breakUnder = true;
    public boolean falling = false;
    public boolean jumping = false;
    public int coins = 0;
    public int score = 0;
    public String character = "mario";
    public String[] chars = new String[]{"mario", "vampire1", "countes", "vampire", "ghost"};
    int targetCounter = 40;

    public Player(GamePanel gp, int x, int y) {
        super(gp);
        this.x = x;
        this.y = y;
        this.speed = 3;
        this.maxLife = 3;
        this.currentLife = maxLife;

        getImages(this.character);
    }

    public void getImages(String name) {
        try {
            standRight = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/" + name + "/stand_right.png")));
            standLeft = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/" + name + "/stand_left.png")));
            runRight1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/" + name + "/run_right_1.png")));
            runRight2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/" + name + "/run_right_2.png")));
            runRight3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/" + name + "/run_right_3.png")));
            runLeft1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/" + name + "/run_left_1.png")));
            runLeft2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/" + name + "/run_left_2.png")));
            runLeft3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/" + name + "/run_left_3.png")));
            jumpRight = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/" + name + "/jump_right.png")));
            jumpLeft = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/" + name + "/jump_left.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        // CHECK FALLING
        gp.collisionDetector.checkFalling(this);

        // CHECK HEARTS
        if (this.currentLife == 0) {
            countScore();
            gp.saveGame(true);
            gp.timer = 60;
            gp.player.x = 48;
            gp.player.y = 416;
            gp.tileManager.worldShift = 0;
            scene = 1;
            section = 1;
            currentLife = 3;
            coins = 0;
            gp.player.direction = "right";
            gp.keyboardHandler.jumpPressed = false;
            gp.keyboardHandler.leftPressed = false;
            gp.keyboardHandler.rightPressed = false;
            gp.gameState = gp.animationState;
            gp.levelFinished = true;
        }

        fall();
        plantHit();

        if (gp.keyboardHandler.rightPressed || gp.keyboardHandler.leftPressed || gp.keyboardHandler.jumpPressed) {
            hitCoins();
            if (gp.keyboardHandler.rightPressed) {
                direction = "right";
            }
            if (gp.keyboardHandler.leftPressed) {
                direction = "left";
            }
            if (gp.keyboardHandler.jumpPressed) {
                switch (direction) {
                    case "right":
                        direction = "jump right";
                        break;
                    case "left":
                        direction = "jump left";
                        break;
                    case "still right":
                        direction = "jump right still";
                        break;
                    case "still left":
                        direction = "jump left still";
                        break;
                }
            }

            // CHECK SCENE
            if (gp.tileManager.worldShift + speed >= 5762) {
                countScore();
                gp.scoresMap.put(gp.ui.username.toString(), gp.scoresMap.get(gp.ui.username.toString()) + gp.player.score);
                gp.addScoreToFile();
                gp.saveGame(true);
                gp.timer = 60;
                coins = 0;
                currentLife = 3;
                scene = 1;
                section = 1;
                gp.player.score = 0;
                gp.tileManager.worldShift = 0;
                gp.keyboardHandler.jumpPressed = false;
                gp.keyboardHandler.rightPressed = false;
                gp.keyboardHandler.leftPressed = false;
                gp.gameState = gp.animationState;
                gp.levelFinished = true;
            }
            if (((x + gp.tileManager.worldShift) / gp.tileSize) % 16 == 0) {
                if (((x + gp.tileManager.worldShift) / gp.tileSize) / 16 == ((section - 1) * 4) + scene) {
                    if (scene == 4) {
                        if (section == 2) {
                            section = 1;
                            level++;
                        } else {
                            countScore();
                            gp.timer = 60;
                            coins = 0;
                            section++;
                        }
                        scene = 1;
                    } else {
                        scene++;
                    }
                }
            }

            // CHECK OBJECT COLLISION
            hasCollided = false;

            // CHECK MONSTER COLLISION

            if (!hasCollided) {
                switch (direction) {
                    case "right":
                        if (x + speed >= gp.screenWidth / 2 - 32) {
                            if (gp.tileManager.worldShift + speed <= 5762) {
                                boolean yN = false;
                                for (Pipe pipe : gp.pipeSetter.pipes) {
                                    if (Math.abs((x + speed + 25) - (pipe.x - gp.tileManager.worldShift)) <= 3) {
                                        yN = true;
                                        break;
                                    }
                                }
                                if (!yN) {
                                    gp.tileManager.worldShift += speed;
                                }
                            }
                        } else {
                            boolean yN = false;
                            for (Pipe pipe : gp.pipeSetter.pipes) {
                                if (Math.abs((x + speed + 25) - (pipe.x - gp.tileManager.worldShift)) <= 3) {
                                    yN = true;
                                    break;
                                }
                            }
                            if (!yN) {
                                x += speed;
                            }
                        }
                        break;
                    case "left":
                        if (x - speed > 0) {
                            boolean yN2 = false;
                            for (Pipe pipe : gp.pipeSetter.pipes) {
                                if (Math.abs((x - speed + 5) - (pipe.x + 96 - gp.tileManager.worldShift)) <= 3) {
                                    yN2 = true;
                                    break;
                                }
                            }
                            if (!yN2) {
                                x -= speed;
                            }
                        }
                        break;
                    case "jump right":
                        jumping = true;
                        boolean yN = false;
                        if (x + speed >= gp.screenWidth / 2 - 32) {
                            if (gp.tileManager.worldShift + speed <= 5762) {
                                for (Tile tileR : gp.breakSetter.breaks) {
                                    if (x + (speed * 2 / 3) == tileR.x - 30 - gp.tileManager.worldShift) {
                                        if (!(y + 64 < tileR.y || y > tileR.y + gp.tileSize / 2)) {
                                            yN = true;
                                            break;
                                        }
                                    }
                                }
                                for (Pipe pipe : gp.pipeSetter.pipes) {
                                    if (Math.abs((x + (speed * 2 / 3)) - (pipe.x - 30 - gp.tileManager.worldShift)) <= 3) {
                                        if (y + 64 > (10 - pipe.height) * gp.tileSize) {
                                            yN = true;
                                            break;
                                        }
                                    }
                                }
                                if (!yN) {
                                    gp.tileManager.worldShift += speed;
                                }
                            }
                        } else {
                            for (Tile tileR : gp.breakSetter.breaks) {
                                if (x + (speed * 2 / 3) == tileR.x - 30 - gp.tileManager.worldShift) {
                                    if (!(y + 64 < tileR.y || y > tileR.y + gp.tileSize / 2)) {
                                        yN = true;
                                        break;
                                    }
                                }
                            }
                            for (Pipe pipe : gp.pipeSetter.pipes) {
                                if (Math.abs((x + (speed * 2 / 3)) - (pipe.x - 30 - gp.tileManager.worldShift)) <= 3) {
                                    if (y + 64 > (10 - pipe.height) * gp.tileSize) {
                                        yN = true;
                                        break;
                                    }
                                }
                            }
                            if (!yN) {
                                x += speed * 2 / 3;
                            }
                        }
                        if (jumpCounter <= targetCounter * 2) {
                            gp.collisionDetector.checkBreak(this);
                            if (jumpCounter <= targetCounter) {
                                y -= speed;
                                for (Tile tileR1 : gp.breakSetter.breaks) {
                                    if (y + 64 > tileR1.y) {
                                        if (y - speed <= tileR1.y + 24) {
                                            if ((x + 16 + gp.tileManager.worldShift) / gp.tileSize == tileR1.x / gp.tileSize) {
                                                targetCounter = jumpCounter;
                                            }
                                        }
                                    }
                                }
                            } else {
                                if (hasCollidedFalling) {
                                    jumpCounter = 0;
                                    direction = "right";
                                    gp.keyboardHandler.jumpPressed = false;
                                    hasCollidedFalling = false;
                                    jumping = false;
                                    targetCounter = 40;
                                } else {
                                    y += speed;
                                }
                            }
                        } else {
                            jumpCounter = 0;
                            direction = "right";
//                            y = 416;
                            gp.keyboardHandler.jumpPressed = false;
                            jumping = false;
                            targetCounter = 40;
                        }
                        jumpCounter++;
                        break;
                    case "jump left":
                        jumping = true;
                        boolean yN2 = false;
                        if (x - speed * 2 / 3 > 0) {
                            for (Tile tileL : gp.breakSetter.breaks) {
                                if (x - (speed * 2 / 3) == tileL.x + 48 - gp.tileManager.worldShift) {
                                    if (!(y + 64 < tileL.y || y > tileL.y + gp.tileSize / 2)) {
                                        yN2 = true;
                                        break;
                                    }
                                }
                            }
                            for (Pipe pipe : gp.pipeSetter.pipes) {
                                if (Math.abs((x - (speed * 2 / 3)) - (pipe.x + 96 - gp.tileManager.worldShift)) <= 3) {
                                    if (y + 64 > (10 - pipe.height) * gp.tileSize) {
                                        yN2 = true;
                                        break;
                                    }
                                }
                            }
                            if (!yN2) {
                                x -= speed * 2 / 3;
                            }
                        }
                        if (jumpCounter <= targetCounter * 2) {
                            gp.collisionDetector.checkBreak(this);
                            if (jumpCounter <= targetCounter) {
                                y -= speed;
                                for (Tile tileL1 : gp.breakSetter.breaks) {
                                    if (y + 64 > tileL1.y) {
                                        if (y - speed <= tileL1.y + 24) {
                                            if ((x + 16 + gp.tileManager.worldShift) / gp.tileSize == tileL1.x / gp.tileSize) {
                                                targetCounter = jumpCounter;
                                            }
                                        }
                                    }
                                }
                            } else {
                                if (hasCollidedFalling) {
                                    jumpCounter = 0;
                                    direction = "left";
                                    gp.keyboardHandler.jumpPressed = false;
                                    hasCollidedFalling = false;
                                    jumping = false;
                                    targetCounter = 40;
                                } else {
                                    y += speed;
                                }
                            }
                        } else {
                            jumpCounter = 0;
                            direction = "left";
//                            y = 416;
                            gp.keyboardHandler.jumpPressed = false;
                            hasCollidedFalling = false;
                            jumping = false;
                            targetCounter = 40;
                        }
                        jumpCounter++;
                        break;
                    case "jump right still":
                        jumping = true;
                        if (jumpCounter <= targetCounter * 2) {
                            if (jumpCounter <= targetCounter) {
                                y -= speed;
                                for (Tile tileL1 : gp.breakSetter.breaks) {
                                    if (y + 64 > tileL1.y) {
                                        if (y - speed <= tileL1.y + 24) {
                                            if ((x + 16 + gp.tileManager.worldShift) / gp.tileSize == tileL1.x / gp.tileSize) {
                                                targetCounter = jumpCounter;
                                            }
                                        }
                                    }
                                }
                            } else {
                                y += speed;
                            }
                        } else {
                            jumpCounter = 0;
                            direction = "right";
//                            y = 416;
                            gp.keyboardHandler.jumpPressed = false;
                            jumping = false;
                            targetCounter = 40;
                        }
                        jumpCounter++;
                        break;
                    case "jump left still":
                        jumping = true;
                        if (jumpCounter <= targetCounter * 2) {
                            if (jumpCounter <= targetCounter) {
                                y -= speed;
                                for (Tile tileL1 : gp.breakSetter.breaks) {
                                    if (y + 64 > tileL1.y) {
                                        if (y - speed <= tileL1.y + 24) {
                                            if ((x + 16 + gp.tileManager.worldShift) / gp.tileSize == tileL1.x / gp.tileSize) {
                                                targetCounter = jumpCounter;
                                            }
                                        }
                                    }
                                }
                            } else {
                                y += speed;
                            }
                        } else {
                            jumpCounter = 0;
                            direction = "left";
//                            y = 416;
                            gp.keyboardHandler.jumpPressed = false;
                            jumping = false;
                            targetCounter = 40;
                        }
                        jumpCounter++;
                        break;
                }
            }

            spritesCounter++;
            if (spritesCounter > 8) {
                spriteNum = (spriteNum == 3) ? 1 : spriteNum + 1;
                spritesCounter = 0;
            }
        } else {
            if (direction.equals("right")) {
                direction = "still right";
            } else if (direction.equals("left")) {
                direction = "still left";
            }
        }
    }

    public void paint(Graphics2D g2D) {
        BufferedImage image = null;

        switch (direction) {
            case "right":
                if (falling) {
                    image = jumpRight;
                } else {
                    if (spriteNum == 1) {
                        image = runRight1;
                    } else if (spriteNum == 2) {
                        image = runRight2;
                    } else {
                        image = runRight3;
                    }
                }
                break;
            case "left":
                if (falling) {
                    image = jumpLeft;
                } else {
                    if (spriteNum == 1) {
                        image = runLeft1;
                    } else if (spriteNum == 2) {
                        image = runLeft2;
                    } else {
                        image = runLeft3;
                    }
                }
                break;
            case "jump right":
            case "jump right still":
                image = jumpRight;
                break;
            case "jump left":
            case "jump left still":
                image = jumpLeft;
                break;
            case "still right":
                if (falling) {
                    image = jumpRight;
                } else {
                    image = standRight;
                }
                break;
            case "still left":
                if (falling) {
                    image = jumpLeft;
                } else {
                    image = standLeft;
                }
                break;
        }
        g2D.drawImage(image, x, y, 32, 64, null);
    }

    public void fall() {
        if (!jumping && y < 416) {
            breakUnder = gp.collisionDetector.checkBreak(this);
            hasCollidedFalling = false;
            if (!breakUnder) {
                falling = true;
                y += 3;
                if (y >= 416) {
                    y = 416;
                    falling = false;
                }
            }
        }
    }

    public void plantHit() {
        for (Pipe pipe : gp.pipeSetter.pipes) {
            if (pipe.hasPlant) {
                if (x + 16 > pipe.x - gp.tileManager.worldShift && x + 16 < pipe.x + 96 - gp.tileSize - gp.tileManager.worldShift) {
                    if (pipe.isOut()) {
                        gp.player.currentLife--;
                        if (this.currentLife == 0) {
                            countScore();
                            gp.saveGame(true);
                            gp.timer = 60;
                            gp.player.x = 48;
                            gp.player.y = 416;
                            gp.tileManager.worldShift = 0;
                            scene = 1;
                            section = 1;
                            currentLife = 3;
                            coins = 0;
                            gp.player.direction = "right";
                            gp.keyboardHandler.jumpPressed = false;
                            gp.keyboardHandler.leftPressed = false;
                            gp.keyboardHandler.rightPressed = false;
                            gp.gameState = gp.animationState;
                            gp.levelFinished = true;
                        } else {
                            gp.timer = 60;
                            gp.player.x = 48;
                            gp.player.y = 416;
                            gp.tileManager.worldShift = (gp.player.section - 1) * gp.tileSize * 16;
                            gp.player.direction = "right";
                        }
                    }
                }
            }
        }
    }

    public void hitCoins() {
        for (Coin coin : gp.coinSetter.coins) {
            if (coin.visible) {
                Rectangle coinRect = new Rectangle(coin.x - gp.tileManager.worldShift, coin.y, gp.tileSize, gp.tileSize);
                Rectangle playerRect = new Rectangle(x, y, 32, 64);
                if (playerRect.intersects(coinRect)) {
                    coin.visible = false;
                    this.coins++;
                }
            }
        }
    }

    public void countScore() {
        score += 10 * coins;
        score += currentLife * 20;
        score += gp.timer;
    }
}

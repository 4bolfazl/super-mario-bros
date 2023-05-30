package main;

import entity.Player;
import object.Pipe;
import tile.Tile;

public class CollisionDetector {
    public int fallingCounter = 0;
    GamePanel gp;

    public CollisionDetector(GamePanel gp) {
        this.gp = gp;
    }

    public boolean checkBreak(Player player) {
        for (Tile tile : gp.breakSetter.breaks) {
            if (player.y + 64 == tile.y) {
                if ((player.x + 16 + gp.tileManager.worldShift) / gp.tileSize == tile.x / gp.tileSize) {
                    player.hasCollidedFalling = true;
                    return true;
                }
            }
        }
        for (Pipe pipe : gp.pipeSetter.pipes) {
            if (player.y + 64 == (10-pipe.height)*gp.tileSize){
                if ((player.x + 16 + gp.tileManager.worldShift) / gp.tileSize == pipe.x / gp.tileSize || (player.x + 16 + gp.tileManager.worldShift) / gp.tileSize == (pipe.x+gp.tileSize) / gp.tileSize) {
                    player.hasCollidedFalling = true;
                    return true;
                }
            }
        }
        return false;
    }

    public void checkFalling(Player player) {
        if (gp.tileManager.tilesMap[0][(player.x + 16 + gp.tileManager.worldShift) / gp.tileSize] == -1) {
            if (player.y >= 416) {
                gp.keyboardHandler.jumpPressed = false;
                gp.keyboardHandler.rightPressed = false;
                gp.keyboardHandler.leftPressed = false;
                gp.gameState = gp.animationState;
                fallingCounter++;
                player.y += 3;
                if (fallingCounter >= 120) {
                    fallingCounter = 0;
                    player.currentLife--;
                    if (player.currentLife == 0) {
                        player.countScore();
                        gp.timer = 60;
                        gp.player.x = 48;
                        gp.player.y = 416;
                        gp.tileManager.worldShift = 0;
                        player.scene = 1;
                        player.section = 1;
                        player.currentLife = 3;
                        player.coins = 0;
                        gp.player.direction = "right";
                        gp.keyboardHandler.jumpPressed = false;
                        gp.keyboardHandler.leftPressed = false;
                        gp.keyboardHandler.rightPressed = false;
                        gp.gameState = gp.animationState;
                        gp.levelFinished = true;
                    } else {
                    gp.timer = 60;
                    player.x = 48;
                    player.y = 416;
                    gp.tileManager.worldShift = (player.section - 1) * gp.tileSize * 16;
                    player.direction = "right";
                    gp.gameState = gp.playState;}
                }
            }
        }
    }
}

package entity;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
    public int x, y;
    public int speed;
    public String name;
    public BufferedImage standRight, standLeft, runRight1, runRight2, runRight3, runLeft1, runLeft2, runLeft3, jumpRight, jumpLeft;
    public String direction = "still right";
    public int spritesCounter = 0;
    public int spriteNum = 1;
    public Rectangle solidArea;
    public boolean hasCollidedFalling = false, hasCollided = false;
    public int jumpCounter = 0;
    public String jumpDirection = "up";
    //    public boolean invincible = false;
//    public int invincibilityCounter = 0;
    public int maxLife;
    public int currentLife;
    GamePanel gp;

    public Entity(GamePanel gp) {
        this.gp = gp;
    }
}

package main;

import object.Coin;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class CoinSetter {
    public BufferedImage image;
    public java.util.List<Coin> coins = new ArrayList<>();
    GamePanel gp;

    public CoinSetter(GamePanel gp) {
        this.gp = gp;

        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/coin.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setCoins();
    }

    public void setCoins() {
        Coin coin1 = new Coin();
        coin1.x = 54 * gp.tileSize;
        coin1.y = 9 * gp.tileSize;
        coins.add(coin1);

        Coin coin2 = new Coin();
        coin2.x = 55 * gp.tileSize;
        coin2.y = 9 * gp.tileSize;
        coins.add(coin2);

        Coin coin3 = new Coin();
        coin3.x = 56 * gp.tileSize;
        coin3.y = 9 * gp.tileSize;
        coins.add(coin3);
    }

    public void paint(Graphics2D g2D) {
        for (Coin coin : coins) {
            if (coin.visible) {
                g2D.drawImage(image, coin.x - gp.tileManager.worldShift, coin.y, gp.tileSize, gp.tileSize, null);
            }
        }
    }
}

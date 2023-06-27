package ir.sharif.math.ap2023.project.model.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sharif.math.ap2023.project.model.player.Player;
import ir.sharif.math.ap2023.project.view.ImageLoader;

import java.awt.image.BufferedImage;

public class Coin extends Item {
    @JsonIgnore
    public int frame = 0;
    @JsonIgnore
    public int timer = 0;
    @JsonIgnore
    public boolean temp = false;

    @JsonIgnore
    BufferedImage[] images = {
            ImageLoader.getInstance().getItemImage(ItemType.COIN),
            ImageLoader.getInstance().getItemImage(ItemType.COIN1),
            ImageLoader.getInstance().getItemImage(ItemType.COIN2),
            ImageLoader.getInstance().getItemImage(ItemType.COIN3)
    };

    public Coin(double x, double y) {
        super(x, y);
    }

    public Coin() {
    }

    @Override
    public BufferedImage getImage() {
        return images[frame / 8];
    }

    @Override
    public void acquired(Player player) {
        player.addPoints(10);
        player.addCoins(1);
    }
}

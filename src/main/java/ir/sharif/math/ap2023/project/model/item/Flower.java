package ir.sharif.math.ap2023.project.model.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sharif.math.ap2023.project.model.player.Player;
import ir.sharif.math.ap2023.project.view.ImageLoader;

import java.awt.image.BufferedImage;

public class Flower extends Item {
    @JsonIgnore
    BufferedImage image = ImageLoader.getInstance().getItemImage(ItemType.FLOWER);

    public Flower() {
    }

    public Flower(double x, double y) {
        super(x, y);
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }

    @Override
    public void acquired(Player player) {
        player.addPoints(20);
        player.upgradeState();
    }
}

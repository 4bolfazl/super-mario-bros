package ir.sharif.math.ap2023.project.model.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sharif.math.ap2023.project.model.player.Player;
import ir.sharif.math.ap2023.project.view.ImageLoader;

import java.awt.image.BufferedImage;

public class Mushroom extends Item {
    @JsonIgnore
    BufferedImage image = ImageLoader.getInstance().getItemImage(ItemType.MUSHROOM);

    public Mushroom() {
    }

    public Mushroom(double x, double y) {
        super(x, y);
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }

    @Override
    public void acquired(Player player) {
        player.addPoints(30);
        player.upgradeState();
    }
}

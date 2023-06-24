package ir.sharif.math.ap2023.project.model.block;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sharif.math.ap2023.project.controller.GameEngine;
import ir.sharif.math.ap2023.project.controller.GameLoader;
import ir.sharif.math.ap2023.project.model.game.SectionObject;
import ir.sharif.math.ap2023.project.model.item.ItemType;
import ir.sharif.math.ap2023.project.model.player.Player;
import ir.sharif.math.ap2023.project.view.ImageLoader;

import java.awt.image.BufferedImage;

public class SimpleBlockObject extends BlockObject {
    @JsonIgnore
    public BufferedImage image = ImageLoader.getInstance().getBlockImage(BlockType.SIMPLE);

    public SimpleBlockObject(int x, int y, BlockType blockType, ItemType item) {
        super(x, y, blockType, item);
    }

    public SimpleBlockObject(int x, int y, BlockType blockType) {
        super(x, y, blockType);
    }

    public SimpleBlockObject() {
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }

    @Override
    public void gotHit() {
        super.gotHit();
//        Player player = GameEngine.getInstance().getPlayer();
    }
}

package ir.sharif.math.ap2023.project.model.block;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sharif.math.ap2023.project.controller.GameEngine;
import ir.sharif.math.ap2023.project.model.item.ItemType;
import ir.sharif.math.ap2023.project.view.ImageLoader;

import java.awt.image.BufferedImage;

public class GroundBlockObject extends BlockObject {
    @JsonIgnore
    public BufferedImage image1 = ImageLoader.getInstance().getBlockImage(BlockType.GROUND);
    @JsonIgnore
    public BufferedImage image2 = ImageLoader.getInstance().getBlockImage(BlockType.FIRE_GROUND);

    public GroundBlockObject(int x, int y, BlockType blockType, ItemType item) {
        super(x, y, blockType, item);
    }

    public GroundBlockObject(int x, int y, BlockType blockType) {
        super(x, y, blockType);
    }

    public GroundBlockObject() {
    }

    @Override
    public BufferedImage getImage() {
        if (GameEngine.getInstance().boss != null) {
            if (GameEngine.getInstance().boss.phase2)
                return image2;
        }
        return image1;
    }
}

package ir.sharif.math.ap2023.project.model.block;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sharif.math.ap2023.project.model.item.ItemType;
import ir.sharif.math.ap2023.project.view.ImageLoader;
import ir.sharif.math.ap2023.project.view.UIManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GroundBlockObject extends BlockObject {
    @JsonIgnore
    public BufferedImage image = ImageLoader.getInstance().getBlockImage(BlockType.GROUND);

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
        return image;
    }
}

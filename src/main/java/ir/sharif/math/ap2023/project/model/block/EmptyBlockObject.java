package ir.sharif.math.ap2023.project.model.block;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sharif.math.ap2023.project.model.item.ItemType;
import ir.sharif.math.ap2023.project.view.ImageLoader;

import java.awt.image.BufferedImage;

public class EmptyBlockObject extends BlockObject {
    @JsonIgnore
    public BufferedImage image = ImageLoader.getInstance().getBlockImage(BlockType.EMPTY);

    public EmptyBlockObject(int x, int y, BlockType blockType, ItemType item) {
        super(x, y, blockType, item);
    }

    public EmptyBlockObject(int x, int y, BlockType blockType) {
        super(x, y, blockType);
    }

    public EmptyBlockObject() {
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }
}

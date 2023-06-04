package ir.sharif.math.ap2023.project.model.block;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sharif.math.ap2023.project.model.item.ItemType;
import ir.sharif.math.ap2023.project.view.ImageLoader;

import java.awt.image.BufferedImage;

public class CoinsBlockObject extends BlockObject {
    @JsonIgnore
    public BufferedImage image = ImageLoader.getInstance().getBlockImage(BlockType.SIMPLE);

    public CoinsBlockObject(int x, int y, BlockType blockType, ItemType item) {
        super(x, y, blockType, item);
    }

    public CoinsBlockObject(int x, int y, BlockType blockType) {
        super(x, y, blockType);
    }

    public CoinsBlockObject() {
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }
}

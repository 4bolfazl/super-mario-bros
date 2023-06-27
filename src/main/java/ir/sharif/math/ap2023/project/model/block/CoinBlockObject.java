package ir.sharif.math.ap2023.project.model.block;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sharif.math.ap2023.project.controller.GameEngine;
import ir.sharif.math.ap2023.project.model.item.Coin;
import ir.sharif.math.ap2023.project.model.item.Item;
import ir.sharif.math.ap2023.project.model.item.ItemType;
import ir.sharif.math.ap2023.project.view.ImageLoader;
import ir.sharif.math.ap2023.project.view.UIManager;

import java.awt.image.BufferedImage;

public class CoinBlockObject extends BlockObject {
    @JsonIgnore
    public BufferedImage image = ImageLoader.getInstance().getBlockImage(BlockType.SIMPLE);

    public CoinBlockObject(int x, int y, BlockType blockType, ItemType item) {
        super(x, y, blockType, item);
    }

    public CoinBlockObject(int x, int y, BlockType blockType) {
        super(x, y, blockType);
    }

    public CoinBlockObject() {
    }

    @Override
    public BufferedImage getImage() {
        if (type == BlockType.EMPTY) {
            return ImageLoader.getInstance().getBlockImage(BlockType.EMPTY);
        }
        return image;
    }

    public void revealItem() {
        Item prize = new Coin(x * UIManager.getInstance().getTileSize(), y * UIManager.getInstance().getTileSize());
        GameEngine gameEngine = GameEngine.getInstance();
        gameEngine.addItem(prize);
    }
}

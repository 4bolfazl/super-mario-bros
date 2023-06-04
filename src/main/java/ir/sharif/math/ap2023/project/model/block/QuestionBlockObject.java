package ir.sharif.math.ap2023.project.model.block;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sharif.math.ap2023.project.controller.GameEngine;
import ir.sharif.math.ap2023.project.model.item.*;
import ir.sharif.math.ap2023.project.view.ImageLoader;
import ir.sharif.math.ap2023.project.view.UIManager;

import java.awt.image.BufferedImage;

public class QuestionBlockObject extends BlockObject {
    @JsonIgnore
    public BufferedImage[] images = {
            ImageLoader.getInstance().getBlockImage(BlockType.QUESTION),
            ImageLoader.getInstance().getBlockImage(BlockType.QUESTION1),
            ImageLoader.getInstance().getBlockImage(BlockType.QUESTION2)
    };

    @JsonIgnore
    public int frame = 0;


    public QuestionBlockObject(int x, int y, BlockType blockType, ItemType item) {
        super(x, y, blockType, item);
    }

    public QuestionBlockObject(int x, int y, BlockType blockType) {
        super(x, y, blockType);
    }

    public QuestionBlockObject() {
    }

    @Override
    public BufferedImage getImage() {
        if (type == BlockType.EMPTY) {
            return ImageLoader.getInstance().getBlockImage(BlockType.EMPTY);
        }
        return images[frame / 15];
    }

    public void revealItem() {
        Item prize = switch (item) {
            case MUSHROOM -> new Mushroom(x * UIManager.getInstance().getTileSize(), y * UIManager.getInstance().getTileSize());
            case STAR -> new Star(x * UIManager.getInstance().getTileSize(), y * UIManager.getInstance().getTileSize());
            case FLOWER -> new Flower(x * UIManager.getInstance().getTileSize(), y * UIManager.getInstance().getTileSize());
            case COIN -> new Coin(x * UIManager.getInstance().getTileSize(), y * UIManager.getInstance().getTileSize());
            default -> null;
        };
        GameEngine gameEngine = GameEngine.getInstance();
        gameEngine.addItem(prize);
    }
}

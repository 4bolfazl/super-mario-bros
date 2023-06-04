package ir.sharif.math.ap2023.project.view;

import ir.sharif.math.ap2023.project.model.block.BlockType;
import ir.sharif.math.ap2023.project.model.item.ItemType;
import ir.sharif.math.ap2023.project.model.player.Character;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public final class ImageLoader {
    private static ImageLoader instance;
    public final BufferedImage menuScreen;
    public BufferedImage gameBackground;
    private HashMap<Character, BufferedImage[][]> sprites = new HashMap<>();
    private HashMap<BlockType, BufferedImage> blocks = new HashMap<>();
    private BufferedImage[] pipes = new BufferedImage[3];
    private HashMap<ItemType, BufferedImage> items = new HashMap<>();
    public BufferedImage[] piranhaImages = new BufferedImage[2];

    private ImageLoader() {
        menuScreen = loadImage("/screens/menu.png");
        gameBackground = loadImage("/backgrounds/0.png");

        Thread loadSpritesThread = new Thread(this::loadSprites);
        loadSpritesThread.start();

        loadBlocks();
        loadPipes();
        loadItems();
        loadEnemies();
    }

    private void loadEnemies() {
        piranhaImages[0] = loadImage("/enemies/piranha/piranha0.png");
        piranhaImages[1] = loadImage("/enemies/piranha/piranha1.png");
    }

    public static ImageLoader getInstance() {
        if (instance == null) {
            instance = new ImageLoader();
        }
        return instance;
    }

    private void loadItems() {
        items.put(ItemType.STAR, loadImage("/items/star.png"));
        items.put(ItemType.MUSHROOM, loadImage("/items/mushroom.png"));
        items.put(ItemType.FLOWER, loadImage("/items/flower.png"));
        items.put(ItemType.COIN, loadImage("/coins/coin0.png"));
        items.put(ItemType.COIN1, loadImage("/coins/coin1.png"));
        items.put(ItemType.COIN2, loadImage("/coins/coin2.png"));
        items.put(ItemType.COIN3, loadImage("/coins/coin3.png"));
    }

    private void loadPipes() {
        for (int i = 0; i < 3; i++) {
            pipes[i] = loadImage("/pipes/" + (i + 1) + ".png");
        }
    }

    private void loadBlocks() {
        blocks.put(BlockType.EMPTY, loadImage("/blocks/empty.png"));
        blocks.put(BlockType.SIMPLE, loadImage("/blocks/simple.png"));
        blocks.put(BlockType.SLIME, loadImage("/blocks/slime.png"));
        blocks.put(BlockType.QUESTION, loadImage("/blocks/question0.png"));
        blocks.put(BlockType.QUESTION1, loadImage("/blocks/question1.png"));
        blocks.put(BlockType.QUESTION2, loadImage("/blocks/question2.png"));
        blocks.put(BlockType.GROUND, loadImage("/blocks/ground.png"));
    }

    private void loadSprites() {
        for (Character character : Character.values()) {
            BufferedImage[][] spritesArray = new BufferedImage[3][16];
            for (int i = 0; i <= 7; i++) {
                spritesArray[1][i] = loadImage("/sprites/" + character.toString().toLowerCase() + "/mega-" + character.toString().toLowerCase() + i + "-right.png");
                spritesArray[1][i + 8] = loadImage("/sprites/" + character.toString().toLowerCase() + "/mega-" + character.toString().toLowerCase() + i + "-left.png");
                spritesArray[2][i] = loadImage("/sprites/" + character.toString().toLowerCase() + "/fire-" + character.toString().toLowerCase() + i + "-right.png");
                spritesArray[2][i + 8] = loadImage("/sprites/" + character.toString().toLowerCase() + "/fire-" + character.toString().toLowerCase() + i + "-left.png");
            }
            for (int i = 0; i <= 5; i++) {
                spritesArray[0][i] = loadImage("/sprites/" + character.toString().toLowerCase() + "/mini-" + character.toString().toLowerCase() + i + "-right.png");
                spritesArray[0][i + 6] = loadImage("/sprites/" + character.toString().toLowerCase() + "/mini-" + character.toString().toLowerCase() + i + "-left.png");
            }
            sprites.put(character, spritesArray);
        }
    }

    private BufferedImage loadImage(String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public BufferedImage[][] getCharacterSprites(Character character) {
        return sprites.get(character);
    }

    public BufferedImage getBlockImage(BlockType blockType) {
        return blocks.get(blockType);
    }

    public BufferedImage getPipeImage(int height) {
        return pipes[height - 1];
    }

    public BufferedImage getItemImage(ItemType itemType) {
        return items.get(itemType);
    }
}

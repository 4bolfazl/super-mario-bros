package ir.sharif.math.ap2023.project.model.enemy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sharif.math.ap2023.project.view.ImageLoader;
import ir.sharif.math.ap2023.project.view.UIManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Koopa extends EnemyObject {
    @JsonIgnore
    BufferedImage[] images = ImageLoader.getInstance().getEnemyImages(EnemyType.KOOPA);

    public Koopa(int x, int y, EnemyType type) {
        super(x, y, type);
    }

    public Koopa() {
        solidArea.setSize(UIManager.getInstance().getTileSize(), 72);
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        solidArea.x = x * UIManager.getInstance().getTileSize();
    }

    @Override
    public void setY(int y) {
        super.setY(y);
        solidArea.y = this.y * UIManager.getInstance().getTileSize() - 24;
    }

    @Override
    public BufferedImage getImage() {
        addFrame();
        if (frame >= 30) {
            frame = 0;
        }
        return images[((isToRight()) ? 2 : 0) + frame / 15];
    }

    @Override
    public Rectangle getBottomBounds() {
        return new Rectangle(
                (int) (getSolidArea().x + UIManager.getInstance().getTileSize() / 4),
                (int) (getSolidArea().y + 24 + UIManager.getInstance().getTileSize() / 2),
                UIManager.getInstance().getTileSize() / 2,
                UIManager.getInstance().getTileSize() / 2
        );
    }
}

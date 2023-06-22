package ir.sharif.math.ap2023.project.model.enemy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sharif.math.ap2023.project.view.ImageLoader;
import ir.sharif.math.ap2023.project.view.UIManager;

import java.awt.image.BufferedImage;

public class Goompa extends EnemyObject {
    @JsonIgnore
    BufferedImage[] images = ImageLoader.getInstance().getEnemyImages(EnemyType.GOOMPA);

    public Goompa(int x, int y, EnemyType type) {
        super(x, y, type);
    }

    public Goompa() {
        solidArea.setSize(UIManager.getInstance().getTileSize(), UIManager.getInstance().getTileSize());
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        solidArea.x = x * UIManager.getInstance().getTileSize();
    }

    @Override
    public void setY(int y) {
        super.setY(y);
        solidArea.y = this.y * UIManager.getInstance().getTileSize();
    }

    @Override
    public BufferedImage getImage() {
        addFrame();
        if (frame >= 30) {
            frame = 0;
        }
        return images[frame / 15];
    }
}

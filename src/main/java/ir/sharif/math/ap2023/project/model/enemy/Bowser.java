package ir.sharif.math.ap2023.project.model.enemy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sharif.math.ap2023.project.view.ImageLoader;
import ir.sharif.math.ap2023.project.view.UIManager;

import java.awt.image.BufferedImage;

public class Bowser extends EnemyObject {
    @JsonIgnore
    BufferedImage[] images = ImageLoader.getInstance().getEnemyImages(EnemyType.BOWSER);

    public Bowser(int x, int y, EnemyType type) {
        super(x, y, type);
    }

    public Bowser() {
        solidArea.setSize(2 * UIManager.getInstance().getTileSize(), 2 * UIManager.getInstance().getTileSize());
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        solidArea.x = x * UIManager.getInstance().getTileSize();
    }

    @Override
    public void setY(int y) {
        this.y = 9 - y;
        solidArea.y = this.y * UIManager.getInstance().getTileSize() - 48;
    }

    @Override
    public BufferedImage getImage() {
        return images[0]; // TODO: TO BE MODIFIED
    }

    @Override
    public void kill() {

    }
}

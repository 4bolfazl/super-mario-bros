package ir.sharif.math.ap2023.project.model.block;

import java.awt.image.BufferedImage;

public class NothingBlockObject extends BlockObject {
    public NothingBlockObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public BufferedImage getImage() {
        return null;
    }
}

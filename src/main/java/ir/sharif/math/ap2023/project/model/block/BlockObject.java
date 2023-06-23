package ir.sharif.math.ap2023.project.model.block;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ir.sharif.math.ap2023.project.model.enemy.EnemyObject;
import ir.sharif.math.ap2023.project.model.enemy.Koopa;
import ir.sharif.math.ap2023.project.model.item.ItemType;
import ir.sharif.math.ap2023.project.view.UIManager;

import java.awt.*;
import java.awt.image.BufferedImage;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "subclass")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CoinBlockObject.class, name = "coinBlock"),
        @JsonSubTypes.Type(value = CoinsBlockObject.class, name = "coinsBlock"),
        @JsonSubTypes.Type(value = EmptyBlockObject.class, name = "emptyBlock"),
        @JsonSubTypes.Type(value = GroundBlockObject.class, name = "groundBlock"),
        @JsonSubTypes.Type(value = QuestionBlockObject.class, name = "questionBlock"),
        @JsonSubTypes.Type(value = SimpleBlockObject.class, name = "simpleBlock"),
        @JsonSubTypes.Type(value = SlimeBlockObject.class, name = "slimeBlock"),
})
public abstract class BlockObject {
    int x, y;
    BlockType type;
    ItemType item;
    @JsonIgnore
    EnemyObject enemyOnIt = null;

    public BlockObject(int x, int y, BlockType type, ItemType item) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.item = item;
    }

    public BlockObject(int x, int y, BlockType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public BlockObject() {
    }

    @JsonIgnore
    public Rectangle getTopBounds() {
        return new Rectangle(
                x * UIManager.getInstance().getTileSize(),
                y * UIManager.getInstance().getTileSize(),
                UIManager.getInstance().getTileSize(),
                UIManager.getInstance().getTileSize() / 2
        );
    }

    @JsonIgnore
    public Rectangle getBottomBounds() {
        return new Rectangle(
                x * UIManager.getInstance().getTileSize(),
                y * UIManager.getInstance().getTileSize() + UIManager.getInstance().getTileSize() / 2,
                UIManager.getInstance().getTileSize(),
                UIManager.getInstance().getTileSize() / 2
        );
    }

    @JsonIgnore
    public Rectangle getRightBounds() {
        return new Rectangle(
                x * UIManager.getInstance().getTileSize() + 3 * UIManager.getInstance().getTileSize() / 4,
                y * UIManager.getInstance().getTileSize(),
                UIManager.getInstance().getTileSize() / 4,
                UIManager.getInstance().getTileSize()

        );
    }

    @JsonIgnore
    public Rectangle getLeftBounds() {
        return new Rectangle(
                x * UIManager.getInstance().getTileSize(),
                y * UIManager.getInstance().getTileSize(),
                UIManager.getInstance().getTileSize() / 4,
                UIManager.getInstance().getTileSize()
        );
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = 10 - y;
    }

    public BlockType getType() {
        return type;
    }

    public void setType(BlockType type) {
        this.type = type;
    }

    public ItemType getItem() {
        return item;
    }

    public void setItem(ItemType item) {
        this.item = item;
    }

    public EnemyObject getEnemyOnIt() {
        return enemyOnIt;
    }

    public void setEnemyOnIt(EnemyObject enemyOnIt) {
        this.enemyOnIt = enemyOnIt;
    }

    public abstract BufferedImage getImage();

    public void gotHit() {
        if (enemyOnIt != null) {
            if (enemyOnIt instanceof Koopa)
                ((Koopa) enemyOnIt).setFreeze(true);
            enemyOnIt.kill();
            enemyOnIt = null;
        }
    }
}

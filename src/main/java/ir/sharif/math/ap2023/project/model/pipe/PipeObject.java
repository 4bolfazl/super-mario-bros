package ir.sharif.math.ap2023.project.model.pipe;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ir.sharif.math.ap2023.project.model.game.SectionObject;
import ir.sharif.math.ap2023.project.view.UIManager;

import java.awt.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "subclass")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DeceitPipe.class, name = "deceitPipe"),
        @JsonSubTypes.Type(value = PiranhaTrapPipe.class, name = "piranhaTrapPipe"),
        @JsonSubTypes.Type(value = SimplePipe.class, name = "simplePipe"),
        @JsonSubTypes.Type(value = TelePiranhaPipe.class, name = "telePiranhaPipe"),
        @JsonSubTypes.Type(value = TeleSimplePipe.class, name = "teleSimplePipe")
})
public abstract class PipeObject {
    int x, y;
    PipeType type;
    SectionObject section;
    boolean activated;

    public PipeObject(int x, int y, PipeType type, SectionObject section, boolean activated) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.section = section;
        this.activated = activated;
    }

    public PipeObject(int x, int y, PipeType type, SectionObject section) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.section = section;
    }

    public PipeObject(int x, int y, PipeType type, boolean activated) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.activated = activated;
    }

    public PipeObject(int x, int y, PipeType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public PipeObject() {
    }

    @JsonIgnore
    public Rectangle getTopBounds() {
        return new Rectangle(
                x * UIManager.getInstance().getTileSize(),
                y * UIManager.getInstance().getTileSize(),
                UIManager.getInstance().getTileSize() * 2,
                UIManager.getInstance().getTileSize() / 2
        );
    }

    @JsonIgnore
    public Rectangle getRightBounds() {
        return new Rectangle(
                x * UIManager.getInstance().getTileSize() + 7 * UIManager.getInstance().getTileSize() / 4,
                y * UIManager.getInstance().getTileSize(),
                UIManager.getInstance().getTileSize() / 4,
                UIManager.getInstance().getTileSize() * 2

        );
    }

    @JsonIgnore
    public Rectangle getLeftBounds() {
        return new Rectangle(
                x * UIManager.getInstance().getTileSize(),
                y * UIManager.getInstance().getTileSize(),
                UIManager.getInstance().getTileSize() / 4,
                UIManager.getInstance().getTileSize() * 2
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
        this.y = 8 - y;
    }

    public PipeType getType() {
        return type;
    }

    public void setType(PipeType type) {
        this.type = type;
    }

    public SectionObject getSection() {
        return section;
    }

    public void setSection(SectionObject section) {
        this.section = section;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}

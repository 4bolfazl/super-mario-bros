package ir.sharif.math.ap2023.project.model.pipe;

import ir.sharif.math.ap2023.project.model.enemy.Piranha;
import ir.sharif.math.ap2023.project.model.game.SectionObject;
import ir.sharif.math.ap2023.project.view.UIManager;

public class TelePiranhaPipe extends PiranhaTrapPipe {
    Piranha piranha = new Piranha();

    public TelePiranhaPipe(int x, int y, PipeType type, SectionObject section, boolean activated) {
        super(x, y, type, section, activated);
    }

    public TelePiranhaPipe(int x, int y, PipeType type, SectionObject section) {
        super(x, y, type, section);
    }

    public TelePiranhaPipe(int x, int y, PipeType type, boolean activated) {
        super(x, y, type, activated);
    }

    public TelePiranhaPipe(int x, int y, PipeType type) {
        super(x, y, type);
    }

    public TelePiranhaPipe() {
    }

    @Override
    public Piranha getPiranha() {
        return piranha;
    }

    @Override
    public void setPiranha(Piranha piranha) {
        this.piranha = piranha;
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        piranha.setX((x * UIManager.getInstance().getTileSize() + UIManager.getInstance().getTileSize() / 2));
    }

    @Override
    public void setY(int y) {
        super.setY(y);
        piranha.setY((8 - y) * UIManager.getInstance().getTileSize());
    }

    public void killPiranha() {
        piranha = null;
    }
}

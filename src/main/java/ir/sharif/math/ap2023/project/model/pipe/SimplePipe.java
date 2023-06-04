package ir.sharif.math.ap2023.project.model.pipe;

import ir.sharif.math.ap2023.project.model.game.SectionObject;

public class SimplePipe extends PipeObject {
    public SimplePipe(int x, int y, PipeType type, SectionObject section, boolean activated) {
        super(x, y, type, section, activated);
    }

    public SimplePipe(int x, int y, PipeType type, SectionObject section) {
        super(x, y, type, section);
    }

    public SimplePipe(int x, int y, PipeType type, boolean activated) {
        super(x, y, type, activated);
    }

    public SimplePipe(int x, int y, PipeType type) {
        super(x, y, type);
    }

    public SimplePipe() {
    }
}

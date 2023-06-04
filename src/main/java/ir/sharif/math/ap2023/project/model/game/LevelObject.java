package ir.sharif.math.ap2023.project.model.game;

import java.util.ArrayList;
import java.util.List;

public class LevelObject {
    List<SectionObject> sections = new ArrayList<>();

    public LevelObject(List<SectionObject> sections) {
        this.sections = sections;
    }

    public LevelObject() {
    }

    public List<SectionObject> getSections() {
        return sections;
    }

    public void setSections(List<SectionObject> sections) {
        this.sections = sections;
    }
}

package ir.sharif.math.ap2023.project.model.game;

import java.util.ArrayList;
import java.util.List;

public class LevelObject implements Cloneable {
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

    @Override
    public LevelObject clone() {
        try {
            LevelObject clone = (LevelObject) super.clone();
            clone.sections = new ArrayList<>();
            for (SectionObject section : sections) {
                clone.sections.add(section.clone());
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

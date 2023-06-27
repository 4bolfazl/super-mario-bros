package ir.sharif.math.ap2023.project.model.game;

import java.util.ArrayList;
import java.util.List;

public class Game implements Cloneable {
    List<LevelObject> levels = new ArrayList<>();
    int hearts;
    int marioState;

    public Game(List<LevelObject> levels, int hearts, int marioState) {
        this.levels = levels;
        this.hearts = hearts;
        this.marioState = marioState;
    }

    public Game() {
    }

    public List<LevelObject> getLevels() {
        return levels;
    }

    public void setLevels(List<LevelObject> levels) {
        this.levels = levels;
    }

    public int getHearts() {
        return hearts;
    }

    public void setHearts(int hearts) {
        this.hearts = hearts;
    }

    public int getMarioState() {
        return marioState;
    }

    public void setMarioState(int marioState) {
        this.marioState = marioState;
    }

    @Override
    public Game clone() {
        try {
            Game clone = (Game) super.clone();
            clone.levels = new ArrayList<>();
            for (LevelObject level : levels) {
                clone.levels.add(level.clone());
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

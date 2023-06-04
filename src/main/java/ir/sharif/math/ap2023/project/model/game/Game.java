package ir.sharif.math.ap2023.project.model.game;

import java.util.ArrayList;
import java.util.List;

public class Game {
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
}

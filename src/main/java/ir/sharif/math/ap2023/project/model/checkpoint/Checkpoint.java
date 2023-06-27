package ir.sharif.math.ap2023.project.model.checkpoint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sharif.math.ap2023.project.controller.GameEngine;
import ir.sharif.math.ap2023.project.controller.GameLoader;
import ir.sharif.math.ap2023.project.controller.GameState;
import ir.sharif.math.ap2023.project.model.Database;
import ir.sharif.math.ap2023.project.model.player.Player;
import ir.sharif.math.ap2023.project.view.UIManager;

import java.awt.*;

public class Checkpoint {
    int x, y;
    private int progressRisk;
    private boolean triggered = false;

    public Checkpoint() {
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x * UIManager.getInstance().getTileSize();
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = (8 - y) * UIManager.getInstance().getTileSize();
    }

    @JsonIgnore
    public Rectangle getBounds() {
        return new Rectangle(x, y, 96, 96);
    }

    public int getProgressRisk() {
        return progressRisk;
    }

    public void setProgressRisk(int progressRisk) {
        this.progressRisk = progressRisk;
    }

    public boolean isTriggered() {
        return triggered;
    }

    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }

    public void save() {
        setTriggered(true);
        GameEngine.getInstance().getPlayer().addCoins(-progressRisk);
        Database.getInstance().saveGame();
        GameEngine.getInstance().setGameState(GameState.PLAYING);
    }

    public void pay() {
        Player player = GameEngine.getInstance().getPlayer();
        player.addCoins(progressRisk / 4);
        GameLoader.getInstance("config.json").getGame().getLevels().get(player.getLevel() - 1).getSections().get(player.getSection() - 1).setCheckpoint(null);
        GameEngine.getInstance().setGameState(GameState.PLAYING);
    }
}

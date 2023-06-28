package ir.sharif.math.ap2023.project.controller.sound;

import ir.sharif.math.ap2023.project.controller.GameState;
import ir.sharif.math.ap2023.project.model.item.Item;

import java.util.List;

public class GameEngineCopy {
    public int swordPressTimer;
    public int sceneTimer1;
    public boolean scene;
    private int deathTimer;
    private GameState gameState;
    private List<Item> items;

    public GameEngineCopy(int swordPressTimer, int sceneTimer1, boolean scene, GameState gameState, List<Item> items, int deathTimer) {
        this.swordPressTimer = swordPressTimer;
        this.sceneTimer1 = sceneTimer1;
        this.scene = scene;
        this.gameState = gameState;
        this.items = items;
        this.deathTimer = deathTimer;
    }

    public GameEngineCopy() {
    }

    public int getSwordPressTimer() {
        return swordPressTimer;
    }

    public void setSwordPressTimer(int swordPressTimer) {
        this.swordPressTimer = swordPressTimer;
    }

    public int getSceneTimer1() {
        return sceneTimer1;
    }

    public void setSceneTimer1(int sceneTimer1) {
        this.sceneTimer1 = sceneTimer1;
    }

    public boolean isScene() {
        return scene;
    }

    public void setScene(boolean scene) {
        this.scene = scene;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public int getDeathTimer() {
        return deathTimer;
    }

    public void setDeathTimer(int deathTimer) {
        this.deathTimer = deathTimer;
    }
}

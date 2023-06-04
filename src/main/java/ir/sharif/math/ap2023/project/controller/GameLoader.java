package ir.sharif.math.ap2023.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.sharif.math.ap2023.project.model.game.Game;

import java.io.File;
import java.io.IOException;

public final class GameLoader {
    private static GameLoader instance;
    private Game game;

    private GameLoader(String configFile) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            game = mapper.readValue(new File("src/main/resources/game-config/" + configFile), Game.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GameLoader getInstance(String configFile) {
        if (instance == null) {
            instance = new GameLoader(configFile);
        }
        return instance;
    }

    public Game getGame() {
        return game;
    }
}

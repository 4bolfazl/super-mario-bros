package ir.sharif.math.ap2023.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ir.sharif.math.ap2023.project.controller.GameEngine;
import ir.sharif.math.ap2023.project.controller.GameLoader;
import ir.sharif.math.ap2023.project.controller.GameState;
import ir.sharif.math.ap2023.project.controller.sound.GameEngineCopy;
import ir.sharif.math.ap2023.project.model.player.Player;
import ir.sharif.math.ap2023.project.model.player.PlayerToSave;
import ir.sharif.math.ap2023.project.view.UIManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class Database {
    private static Database instance;
    List<Player> users = new ArrayList<>();
    @JsonIgnore
    private Player currentUser;

    private Database(List<Player> users) {
        this.users = users;
    }

    private Database() {
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
            ObjectMapper mapper = new ObjectMapper();
            try {
                instance = mapper.readValue(getJsonFile(), Database.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public static void setInstance(Database instance) {
        Database.instance = instance;
    }

    private static File getJsonFile() {
        File file = new File("src/main/resources/database/database.json");
        if (!file.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public Player getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Player currentUser) {
        this.currentUser = currentUser;
    }

    public List<Player> getUsers() {
        return users;
    }

    public void setUsers(List<Player> users) {
        this.users = users;
    }

    public Player findUserByUsername(String username, Database instance) {
        for (Player user : instance.users) {
            if (user.getUsername().equals(username)) {
                instance.currentUser = user;
                return user;
            }
        }
        instance.currentUser = null;
        return null;
    }

    public void signUpUser(Player user) {
        users.add(user);

        write();
    }

    public void reload() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            instance = mapper.readValue(getJsonFile(), Database.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        findUserByUsername(GameEngine.getInstance().getPlayer().getUsername(), instance);
    }

    public void write() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        try {
            mapper.writeValue(getJsonFile(), getInstance());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveGame() {
        Player player = GameEngine.getInstance().getPlayer();

        PlayerToSave tempPlayer = new PlayerToSave(player);

        GameEngineCopy gameEngineCopy = GameEngine.getInstance().copy();
        gameEngineCopy.setGameState(GameState.PLAYING);

        player.getSavedPlayer()[UIManager.getInstance().saveOption - 1] = tempPlayer;
        player.getSavedGameEngineCopy()[UIManager.getInstance().saveOption - 1] = gameEngineCopy;
        player.getSavedGame()[UIManager.getInstance().saveOption - 1] = GameLoader.getInstance("config.json").getGame().clone();

        users.set(users.indexOf(findUserByUsername(player.getUsername(), instance)), player);
        write();
    }
}

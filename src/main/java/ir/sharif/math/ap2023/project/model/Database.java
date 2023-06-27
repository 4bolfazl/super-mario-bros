package ir.sharif.math.ap2023.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ir.sharif.math.ap2023.project.controller.GameEngine;
import ir.sharif.math.ap2023.project.controller.GameLoader;
import ir.sharif.math.ap2023.project.model.player.Player;

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

    public Player findUserByUsername(String username) {
        for (Player user : users) {
            if (user.getUsername().equals(username)) {
                currentUser = user;
                return user;
            }
        }
        currentUser = null;
        return null;
    }

    public void signUpUser(Player user) {
        users.add(user);

        write();
    }

    private void write() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        try {
            mapper.writeValue(getJsonFile(), getInstance());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveGame() {
        System.out.println("saving game...");
        Player player = GameEngine.getInstance().getPlayer();

        Player tempPlayer = player.clone();

        Object[][] tempSavedGames = tempPlayer.getSavedGames();

        player.getSavedGames()[player.getSaveSlot()][0] = tempPlayer;
        player.getSavedGames()[player.getSaveSlot()][1] = GameEngine.getInstance();
        player.getSavedGames()[player.getSaveSlot()][2] = GameLoader.getInstance("config.json").getGame();

        users.set(users.indexOf(findUserByUsername(player.getUsername())), player);
        write();

        player.setSavedGames(tempSavedGames);
    }
}

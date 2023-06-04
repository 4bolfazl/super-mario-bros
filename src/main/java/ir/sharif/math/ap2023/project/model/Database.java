package ir.sharif.math.ap2023.project.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ir.sharif.math.ap2023.project.model.player.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class Database {
    private static Database instance;
    List<Player> users = new ArrayList<>();

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

    public List<Player> getUsers() {
        return users;
    }

    public void setUsers(List<Player> users) {
        this.users = users;
    }

    public Player findUserByUsername(String username) {
        for (Player user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public void signUpUser(Player user) {
        users.add(user);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        try {
            mapper.writeValue(getJsonFile(), getInstance());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}

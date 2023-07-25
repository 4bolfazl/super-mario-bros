package ir.sharif.math.ap2023.project;

import ir.sharif.math.ap2023.project.controller.GameEngine;
import ir.sharif.math.ap2023.project.model.database.Database;

public class Main {
    public static void main(String[] args) {
        Database.getInstance();
        GameEngine.getInstance();
    }
}
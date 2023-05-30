package main;

public class Game {
    public int lv, se, sc;
    public int time;
    public int hearts;
    public int score;
    public int coins;
    public String character;
    public int difficulty;

    public Game(String[] array) {
        this.lv = Integer.parseInt(array[0]);
        this.se = Integer.parseInt(array[1]);
        this.sc = Integer.parseInt(array[2]);
        this.time = Integer.parseInt(array[3]);
        this.hearts = Integer.parseInt(array[4]);
        this.score = Integer.parseInt(array[5]);
        this.coins = Integer.parseInt(array[6]);
        this.character = array[7];
        this.difficulty = Integer.parseInt(array[8]);
    }
}

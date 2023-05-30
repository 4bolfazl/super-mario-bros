package main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("Super Mario");
        window.setIconImage(new ImageIcon("res/icon/icon.png").getImage());
        window.setResizable(false);

        GamePanel gp = new GamePanel();
        window.add(gp);

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.setupGame();
        gp.startGameThread();
    }
}
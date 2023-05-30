package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseHandler implements MouseListener {
    GamePanel gp;

    public MouseHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point point = e.getPoint();

        if ((point.x >= 168 && point.x <= 600) && (point.y >= 199 && point.y <= 257)) {
            gp.ui.fieldSelected = 0;
        } else if ((point.x >= 168 && point.x <= 600) && (point.y >= 294 && point.y <= 351)) {
            gp.ui.fieldSelected = 1;
        } else if (point.y >= 388 && point.y <= 426) {
            if (point.x >= 245 && point.x <= 330) {
                // LOGIN
                int userIndex = gp.usernames.indexOf(gp.ui.username.toString());
                if (userIndex != -1) {
                    if (gp.passwords.get(userIndex).contentEquals(gp.ui.password)) {
                        gp.gameState = gp.titleState;
                    } else {
                        JOptionPane.showMessageDialog(null, "Password wrong. Recheck your password.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Username doesn't exist. Use signup button or recheck your username.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else if (point.x >= 437 && point.x <= 522) {
                // SIGNUP
                if (gp.usernames.contains(gp.ui.username.toString())) {
                    JOptionPane.showMessageDialog(null, "Username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    if (gp.ui.username.toString().equals("")) {
                        JOptionPane.showMessageDialog(null, "Please fill the username field.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (gp.ui.password.toString().equals("")) {
                        JOptionPane.showMessageDialog(null, "Please fill the password field.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        gp.usernames.add(gp.ui.username.toString());
                        gp.passwords.add(gp.ui.password.toString());
                        gp.signPlayer(gp.ui.username.toString(), gp.ui.password.toString());
                        gp.gameState = gp.titleState;
                    }
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // EMPTY
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // EMPTY
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // EMPTY
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // EMPTY
    }
}

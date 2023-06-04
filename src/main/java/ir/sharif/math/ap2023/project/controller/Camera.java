package ir.sharif.math.ap2023.project.controller;

public class Camera {
    private double x, y;

    public Camera() {
        x = 0;
        y = 0;
    }

    public void moveCamera(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}

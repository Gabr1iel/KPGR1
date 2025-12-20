package cz.algone.model;

import cz.algone.util.color.ColorPair;

public class Point implements Model {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public ColorPair getColors() {
        return null;
    }
}

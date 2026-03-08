package cz.algone.model.models2D;

import cz.algone.util.color.ColorPair;

public class Line implements Model {
    private final int x1;
    private int X2;
    private final int y1;
    private int y2;
    private final ColorPair colors;

    public Line(int x1, int y1, int x2, int y2, ColorPair colors) {
        this.x1 = x1;
        this.y1 = y1;
        this.X2 = x2;
        this.y2 = y2;
        this.colors = colors;
    }

    public Line(Point p1, Point p2, ColorPair colors) {
        this.x1 = p1.x();
        this.y1 = p1.y();
        this.X2 = p2.x();
        this.y2 = p2.y();
        this.colors = colors;
    }

    public int getX1() {
        return x1;
    }

    public int getX2() {
        return X2;
    }

    public int getY1() {
        return y1;
    }

    public int getY2() {
        return y2;
    }

    @Override
    public ColorPair getColors() {
        return colors;
    }

    public void setX2(int x2) {this.X2 = x2;}
    public void setY2(int y2) {this.y2 = y2;}
}

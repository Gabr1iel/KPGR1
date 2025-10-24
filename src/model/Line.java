package model;

public class Line implements Model {
    private int x1, X2, y1, y2;

    public Line(int x1, int x2, int y1, int y2) {
        this.x1 = x1;
        this.X2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    public Line(Point p1, Point p2) {
        this.x1 = p1.getX();
        this.X2 = p2.getX();
        this.y1 = p1.getY();
        this.y2 = p2.getY();
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
}

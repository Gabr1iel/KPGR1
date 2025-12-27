package cz.algone.model;

import cz.algone.util.color.ColorPair;

public class Circle implements Model{
    private Point center;
    private int radius;
    private ColorPair colors;

    public Circle(Point center, int radius, ColorPair colors) {
        this.center = center;
        this.radius = radius;
        this.colors = colors;
    }

    @Override
    public ColorPair getColors() {
        return colors;
    }

    public Point getCenter() {
        return center;
    }

    public int getRadius() {
        return radius;
    }
}

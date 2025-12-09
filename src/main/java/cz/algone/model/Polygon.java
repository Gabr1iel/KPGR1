package cz.algone.model;

import cz.algone.util.color.ColorPair;

import java.util.ArrayList;

public class Polygon implements Model {
    private ColorPair colors;
    private ArrayList<Point> points;
    private Point previewPoint = null;

    public Polygon(ColorPair colors) {
        this.colors = colors;
        points = new ArrayList<>();
    }

    public int getNearestPoint(int x, int y) {
        int nearestPointIndex = -1;
        int nearestDistance = Integer.MAX_VALUE;

        for (int i = 0; i < points.size(); i++) {
            int lengthX = points.get(i).getX() - x;
            int lengthY = points.get(i).getY() - y;
            int distance = lengthX * lengthX + lengthY * lengthY;

            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestPointIndex = i;
            }
        }
        return nearestPointIndex;
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public ColorPair getColors() {
        return colors;
    }

    public Point getPointByIndex(int index) {
        return points.get(index);
    }

    public int getSize() {
        return points.size();
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public Point getPreviewPoint() {
        return previewPoint;
    }

    public void setColors(ColorPair colors) {
        this.colors = colors;
    }

    public void setPointByIndex(int index, int x, int y) {
        points.set(index, new Point(x, y));
    }

    public void setPreviewPoint(Point previewPoint) {
        this.previewPoint = previewPoint;
    }
}

package cz.algone.polygon;

import cz.algone.model.Model;
import cz.algone.model.Point;

import java.util.ArrayList;

public class Polygon implements Model {
    private ArrayList<Point> points;
    private Point previewPoint = null;

    public Polygon() {
        points = new ArrayList<>();
    }

    public void addPoint(Point point) {
        points.add(point);
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

    public void setPreviewPoint(Point previewPoint) {
        this.previewPoint = previewPoint;
    }
}

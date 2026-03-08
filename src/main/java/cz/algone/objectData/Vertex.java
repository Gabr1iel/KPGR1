package cz.algone.objectData;

import cz.algone.transforms.Col;
import cz.algone.transforms.Point3D;

public class Vertex implements Vectorizable<Vertex> {
    private final Col DEFAULT_COLOR = new Col(0xff0000);

    private final Point3D position;
    private final Col color;

    public Vertex(Point3D position, Col color) {
        this.position = position;
        this.color = color;
    }

    public Vertex(Point3D position) {
        this.position = position;
        this.color = DEFAULT_COLOR;
    }

    public Vertex(double x, double y, double z, Col color) {
        this.position = new Point3D(x, y, z);
        this.color = color;
    }

    @Override
    public Vertex mul(double d) {
        return new Vertex(this.position.mul(d), this.color.mul(d));
    }

    @Override
    public Vertex add(Vertex vertex) {
        return new Vertex(this.position.add(vertex.getPosition()), this.color.add(vertex.getColor()));
    }

    public Point3D getPosition() {return position;}
    public Col getColor() {return color;}
    public double getX() {return position.getX();}
    public double getY() {return position.getY();}
    public double getZ() {return position.getZ();}
}

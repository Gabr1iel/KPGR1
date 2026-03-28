package cz.algone.objectData;

import cz.algone.transforms.Col;
import cz.algone.transforms.Point3D;

public class Vertex implements Vectorizable<Vertex> {
    private final Col DEFAULT_COLOR = new Col(0xff0000);

    private final Point3D position;
    private final Col color;
    private final double u, v;
    private final double w; // 1/clipW pro perspektivně korektní interpolaci
    private final double nx, ny, nz; // normála (object-space nebo world-space dle fáze)
    private final double intensity;  // osvětlení (Gouraud), default 1.0 = plně osvětlený

    public Vertex(Point3D position, Col color) {
        this.position = position;
        this.color = color;
        this.u = 0; this.v = 0; this.w = 1;
        this.nx = 0; this.ny = 0; this.nz = 0;
        this.intensity = 1.0;
    }

    public Vertex(Point3D position) {
        this.position = position;
        this.color = DEFAULT_COLOR;
        this.u = 0; this.v = 0; this.w = 1;
        this.nx = 0; this.ny = 0; this.nz = 0;
        this.intensity = 1.0;
    }

    public Vertex(double x, double y, double z, Col color) {
        this.position = new Point3D(x, y, z);
        this.color = color;
        this.u = 0; this.v = 0; this.w = 1;
        this.nx = 0; this.ny = 0; this.nz = 0;
        this.intensity = 1.0;
    }

    public Vertex(double x, double y, double z, Col color, double u, double v) {
        this.position = new Point3D(x, y, z);
        this.color = color;
        this.u = u;
        this.v = v;
        this.w = 1;
        this.nx = 0; this.ny = 0; this.nz = 0;
        this.intensity = 1.0;
    }

    public Vertex(double x, double y, double z, Col color, double u, double v,
                  double nx, double ny, double nz) {
        this.position = new Point3D(x, y, z);
        this.color = color;
        this.u = u; this.v = v; this.w = 1;
        this.nx = nx; this.ny = ny; this.nz = nz;
        this.intensity = 1.0;
    }

    public Vertex(Point3D position, Col color, double u, double v) {
        this.position = position;
        this.color = color;
        this.u = u;
        this.v = v;
        this.w = 1;
        this.nx = 0; this.ny = 0; this.nz = 0;
        this.intensity = 1.0;
    }

    public Vertex(Point3D position, Col color, double u, double v, double w) {
        this.position = position;
        this.color = color;
        this.u = u;
        this.v = v;
        this.w = w;
        this.nx = 0; this.ny = 0; this.nz = 0;
        this.intensity = 1.0;
    }

    public Vertex(Point3D position, Col color, double u, double v, double w,
                  double nx, double ny, double nz, double intensity) {
        this.position = position;
        this.color = color;
        this.u = u; this.v = v; this.w = w;
        this.nx = nx; this.ny = ny; this.nz = nz;
        this.intensity = intensity;
    }

    @Override
    public Vertex mul(double d) {
        return new Vertex(this.position.mul(d), this.color.mul(d),
                this.u * d, this.v * d, this.w * d,
                this.nx * d, this.ny * d, this.nz * d, this.intensity * d);
    }

    @Override
    public Vertex add(Vertex vertex) {
        return new Vertex(this.position.add(vertex.position), this.color.add(vertex.color),
                this.u + vertex.u, this.v + vertex.v, this.w + vertex.w,
                this.nx + vertex.nx, this.ny + vertex.ny, this.nz + vertex.nz,
                this.intensity + vertex.intensity);
    }

    public Point3D getPosition() { return position; }
    public Col getColor() { return color; }
    public double getX() { return position.getX(); }
    public double getY() { return position.getY(); }
    public double getZ() { return position.getZ(); }
    public double getU() { return u; }
    public double getV() { return v; }
    public double getW() { return w; }
    public double getNx() { return nx; }
    public double getNy() { return ny; }
    public double getNz() { return nz; }
    public double getIntensity() { return intensity; }

    /** Vrátí nový Vertex s nastavenou intensity (wither pattern). */
    public Vertex withIntensity(double intensity) {
        return new Vertex(position, color, u, v, w, 0, 0, 0, intensity);
    }
}

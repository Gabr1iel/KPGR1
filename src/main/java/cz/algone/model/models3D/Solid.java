package cz.algone.model.models3D;

import cz.algone.transforms.Mat4;
import cz.algone.transforms.Mat4Identity;
import cz.algone.transforms.Point3D;
import cz.algone.transforms.Vec3D;
import cz.algone.util.color.ColorPair;
import cz.algone.util.color.ColorUtils;

import java.util.ArrayList;

public abstract class Solid {
    protected ColorPair highlightColor = ColorUtils.DEFAULT_HIGHLIGHT_COLOR;
    protected boolean selected = false;
    protected ArrayList<Point3D> vb = new ArrayList<>();
    protected ArrayList<Integer> ib = new ArrayList<>();
    protected Mat4 model = new Mat4Identity();
    protected ColorPair color;
    protected Vec3D position = new Vec3D(0,0,0);
    protected Vec3D pivot = new Vec3D(0.5, 0.5, 0.5);
    protected int angleX = 0;
    protected int angleY = 0;
    protected int angleZ = 0;
    protected double scale = 1.0;
    public ArrayList<Point3D> getVb() {
        return vb;
    }
    public ArrayList<Integer> getIb() {
        return ib;
    }

    public void resetTransform() {
        position = new Vec3D(0, 0, 0);
        angleX = 0;
        angleY = 0;
        angleZ = 0;
        scale = 1.0;
        model = new Mat4Identity();
    }

    public Mat4 getModel() {
        return model;
    }

    public void setModel(Mat4 model) {
        this.model = model;
    }

    public ColorPair getColor() {
        return color;
    }

    public Vec3D getPosition() {return position;}

    public void setPosition(Vec3D position) {this.position = position;}

    public int getAngleX() {return angleX;}

    public void setAngleX(int angle) {this.angleX = angle;}

    public int getAngleY() {return angleY;}

    public void setAngleY(int angle) {this.angleY = angle;}

    public int getAngleZ() {return angleZ;}

    public void setAngleZ(int angle) {this.angleZ = angle;}

    public double getScale() {return scale;}

    public void setScale(double scale) {this.scale = scale;}

    public boolean isSelected() {return selected;}

    public void setSelected(boolean selected) {this.selected = selected;}

    public ColorPair getHighlightColor() {return highlightColor;}

    public Vec3D getPivot() {return pivot;}

    public void setPivot(Vec3D pivot) {this.pivot = pivot;}
}

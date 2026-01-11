package cz.algone.model.solid;

import cz.algone.transforms.Mat4;
import cz.algone.transforms.Mat4Identity;
import cz.algone.transforms.Point3D;
import cz.algone.util.color.ColorPair;

import java.util.ArrayList;

public abstract class Solid {
    protected ArrayList<Point3D> vb = new ArrayList<>();
    protected ArrayList<Integer> ib = new ArrayList<>();
    protected Mat4 model = new Mat4Identity();
    protected ColorPair color;

    public ArrayList<Point3D> getVb() {
        return vb;
    }

    public ArrayList<Integer> getIb() {
        return ib;
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
}

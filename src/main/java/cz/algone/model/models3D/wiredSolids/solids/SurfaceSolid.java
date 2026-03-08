package cz.algone.model.models3D.wiredSolids.solids;

import cz.algone.model.models3D.wiredSolids.Solid;
import cz.algone.model.models3D.wiredSolids.cubic.IParametricSurface;
import cz.algone.transforms.Point3D;
import cz.algone.transforms.Vec3D;
import cz.algone.util.color.ColorPair;
import javafx.scene.paint.Color;

public class SurfaceSolid extends Solid {
    private IParametricSurface surface;
    private int uSteps = 25;
    private int vSteps = 25;

    public SurfaceSolid(IParametricSurface surface) {
        this.surface = surface;
        color = new ColorPair(Color.PURPLE, null);
        rebuild();
    }

    public void rebuild() {
        getVb().clear();
        getIb().clear();

        int cols = uSteps + 1;
        int rows = vSteps + 1;

        // VB
        for (int j = 0; j < rows; j++) {
            double v = j / (double) vSteps;
            for (int i = 0; i < cols; i++) {
                double u = i / (double) uSteps;
                Vec3D p = surface.evaluation(u, v);
                getVb().add(new Point3D(p.getX(), p.getY(), p.getZ(), 1.0));
            }
        }

        // IB - horizontální čáry (u směr)
        for (int j = 0; j < rows; j++) {
            int rowStart = j * cols;
            for (int i = 0; i < cols - 1; i++) {
                int a = rowStart + i;
                int b = rowStart + i + 1;
                getIb().add(a); getIb().add(b);
            }
        }

        // IB - vertikální čáry (v směr)
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows - 1; j++) {
                int a = j * cols + i;
                int b = (j + 1) * cols + i;
                getIb().add(a); getIb().add(b);
            }
        }
    }
}

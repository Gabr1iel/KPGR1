package cz.algone.model.models3D.solids;

import cz.algone.model.models3D.Solid;
import cz.algone.objectData.Part;
import cz.algone.objectData.TopologyType;
import cz.algone.objectData.Vertex;
import cz.algone.shader.ShaderConstant;
import cz.algone.transforms.Col;
import cz.algone.transforms.Point3D;
import cz.algone.transforms.Vec3D;
import cz.algone.util.color.ColorPair;

/** Vizualizace bodového světla jako malá koule.
 *  lightColor = barva světla = difuzní barva osvětlení. */
public class LightSolid extends Solid {
    private Col lightColor;

    public LightSolid() {
        this(new Col(255, 255, 200)); // teplá bílá
    }

    public LightSolid(Col lightColor) {
        this.lightColor = lightColor;
        pivot = new Vec3D(0, 0, 0);
        position = new Vec3D(2, 2, 2);
        color = new ColorPair(lightColor, null);
        highlightColor = new ColorPair(new Col(255, 255, 0), null);

        buildSphere(6, 8, 0.15);
        shader = new ShaderConstant(lightColor);
    }

    private void buildSphere(int stacks, int slices, double radius) {
        for (int i = 0; i <= stacks; i++) {
            double phi = Math.PI * i / stacks;
            for (int j = 0; j <= slices; j++) {
                double theta = 2 * Math.PI * j / slices;
                double x = radius * Math.sin(phi) * Math.cos(theta);
                double y = radius * Math.cos(phi);
                double z = radius * Math.sin(phi) * Math.sin(theta);
                vb.add(new Vertex(x, y, z, lightColor));
            }
        }

        int cols = slices + 1;

        // LINES
        int edgeStart = 0, edgeCount = 0;
        for (int i = 0; i <= stacks; i++) {
            for (int j = 0; j < slices; j++) {
                addEdge(i * cols + j, i * cols + j + 1);
                edgeCount++;
            }
        }
        for (int j = 0; j <= slices; j++) {
            for (int i = 0; i < stacks; i++) {
                addEdge(i * cols + j, (i + 1) * cols + j);
                edgeCount++;
            }
        }
        pb.add(new Part(TopologyType.LINES, edgeStart, edgeCount));

        // TRIANGLES
        int triStart = ib.size(), triCount = 0;
        for (int i = 0; i < stacks; i++) {
            for (int j = 0; j < slices; j++) {
                int a = i * cols + j;
                int b = i * cols + j + 1;
                int d = (i + 1) * cols + j;
                int e = (i + 1) * cols + j + 1;
                addTriangle(a, b, d);
                addTriangle(b, e, d);
                triCount += 2;
            }
        }
        pb.add(new Part(TopologyType.TRIANGLES, triStart, triCount));
    }

    public Col getLightColor() { return lightColor; }

    public void setLightColor(Col lightColor) {
        this.lightColor = lightColor;
        this.color = new ColorPair(lightColor, null);
        this.shader = new ShaderConstant(lightColor);
    }

    /** Vrací pozici světla ve world-space (střed koule po transformaci). */
    public Vec3D getWorldLightPosition() {
        Point3D world = new Point3D(0, 0, 0).mul(model);
        return new Vec3D(world.getX(), world.getY(), world.getZ());
    }
}

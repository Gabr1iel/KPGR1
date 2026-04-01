package cz.algone.model.models3D.solids;

import cz.algone.model.models3D.Solid;
import cz.algone.objectData.Part;
import cz.algone.objectData.TopologyType;
import cz.algone.objectData.Vertex;
import cz.algone.shader.ShaderConstant;
import cz.algone.transforms.Col;
import cz.algone.util.color.ColorPair;

/** Koule aproximovaná UV mřížkou.
 *  stacks = počet vrstev (latitude), slices = počet sloupců (longitude) */
public class Sphere extends Solid {
    public Sphere(int stacks, int slices) {
        color = new ColorPair(new Col(100, 149, 237), null); // cornflower blue
        Col c = color.primary();

        // VB – vrcholy UV mřížky (stacks+1 řádků × slices+1 sloupců)
        for (int i = 0; i <= stacks; i++) {
            double phi = Math.PI * i / stacks;           // 0 .. π
            for (int j = 0; j <= slices; j++) {
                double theta = 2 * Math.PI * j / slices; // 0 .. 2π
                double x = Math.sin(phi) * Math.cos(theta);
                double y = Math.cos(phi);
                double z = Math.sin(phi) * Math.sin(theta);
                double u = (double) j / slices;
                double v = (double) i / stacks;
                Col vc = posColor(x, y, z, 1.0);
                vb.add(new Vertex(x, y, z, vc, u, v, x, y, z));
            }
        }

        int cols = slices + 1;

        // LINES – horizontální a vertikální čáry mřížky
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

        // TRIANGLES – dva trojúhelníky na každý čtverec mřížky
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

        shader = new ShaderConstant(c);
    }
}

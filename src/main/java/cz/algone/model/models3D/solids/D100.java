package cz.algone.model.models3D.solids;

import cz.algone.model.models3D.Solid;
import cz.algone.objectData.Part;
import cz.algone.objectData.TopologyType;
import cz.algone.objectData.Vertex;
import cz.algone.shader.ShaderConstant;
import cz.algone.transforms.Col;
import cz.algone.transforms.Vec3D;
import cz.algone.util.color.ColorPair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** D100 – geodetický tetraedr s 100 trojúhelníkovými plochami na jednotkové sféře. */
public class D100 extends Solid {
    private static final int N = 5;

    public D100() {
        color = new ColorPair(new Col(153, 102, 204), null);
        Col c = color.primary();

        Vec3D vA = new Vec3D(0, 0, 1);
        Vec3D vB = new Vec3D(2 * Math.sqrt(2) / 3, 0, -1.0 / 3);
        Vec3D vC = new Vec3D(-Math.sqrt(2) / 3,  Math.sqrt(2.0 / 3), -1.0 / 3);
        Vec3D vD = new Vec3D(-Math.sqrt(2) / 3, -Math.sqrt(2.0 / 3), -1.0 / 3);

        List<int[]> triangles = new ArrayList<>();
        subdivideFace(vA, vB, vC, c, triangles);
        subdivideFace(vA, vC, vD, c, triangles);
        subdivideFace(vA, vD, vB, c, triangles);
        subdivideFace(vD, vC, vB, c, triangles);

        Set<Long> edgeSet = new HashSet<>();
        List<int[]> edges = new ArrayList<>();
        for (int[] tri : triangles) {
            for (int k = 0; k < 3; k++) {
                int a = tri[k], b = tri[(k + 1) % 3];
                long key = (long) Math.min(a, b) * 100000L + Math.max(a, b);
                if (edgeSet.add(key))
                    edges.add(new int[]{a, b});
            }
        }

        int edgeStart = 0;
        for (int[] e : edges) addEdge(e[0], e[1]);
        pb.add(new Part(TopologyType.LINES, edgeStart, edges.size()));

        int triStart = ib.size();
        for (int[] tri : triangles) addTriangle(tri[0], tri[1], tri[2]);
        pb.add(new Part(TopologyType.TRIANGLES, triStart, triangles.size()));

        shader = new ShaderConstant(c);
    }

    /** Rozdělí jednu stěnu tetraedru na NxN trojúhelníků a přidá vrcholy do vb, trojúhelníky do triangles. */
    private void subdivideFace(Vec3D A, Vec3D B, Vec3D C, Col c, List<int[]> triangles) {
        int[][] idx = new int[N + 1][N + 1];
        for (int i = 0; i <= N; i++) {
            for (int j = 0; j <= N - i; j++) {
                Vec3D p = A.mul(N - i - j).add(B.mul(i)).add(C.mul(j));
                p = p.mul(1.0 / p.length());
                idx[i][j] = vb.size();
                double u = (double) i / N;
                double v = (double) j / N;
                vb.add(new Vertex(p.getX(), p.getY(), p.getZ(), c, u, v));
            }
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N - i; j++) {
                triangles.add(new int[]{idx[i][j], idx[i + 1][j], idx[i][j + 1]});
                if (j + 1 < N - i)
                    triangles.add(new int[]{idx[i + 1][j], idx[i + 1][j + 1], idx[i][j + 1]});
            }
        }
    }
}

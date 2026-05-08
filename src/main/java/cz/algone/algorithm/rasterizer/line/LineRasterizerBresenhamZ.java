package cz.algone.algorithm.rasterizer.line;

import cz.algone.objectData.Vertex;
import cz.algone.raster.ZBuffer;

/** Rasterizér úseček algoritmem Bresenham se Z-testem proti {@link ZBuffer}u. */
public class LineRasterizerBresenhamZ {
    private ZBuffer zBuffer;

    public LineRasterizerBresenhamZ(ZBuffer zBuffer) {
        this.zBuffer = zBuffer;
    }

    public void setup(ZBuffer zBuffer) {
        this.zBuffer = zBuffer;
    }

    /** Rasterizuje úsečku mezi dvěma vrcholy ve window space s Z-interpolací. */
    public void rasterize(Vertex a, Vertex b) {
        if (zBuffer == null) return;
        int x1 = (int) Math.round(a.getX()), y1 = (int) Math.round(a.getY());
        int x2 = (int) Math.round(b.getX()), y2 = (int) Math.round(b.getY());

        int w = zBuffer.getWidth(), h = zBuffer.getHeight();
        if ((x1 < 0 && x2 < 0) || (x1 >= w && x2 >= w)) return;
        if ((y1 < 0 && y2 < 0) || (y1 >= h && y2 >= h)) return;

        double z1 = a.getZ(), z2 = b.getZ();
        int c1 = a.getColor().getRGB(), c2 = b.getColor().getRGB();

        int dx = Math.abs(x2 - x1), dy = Math.abs(y2 - y1);
        int steps = Math.max(dx, dy);

        if (steps == 0) {
            zBuffer.setPixelWithZTest(x1, y1, z1, c1);
            return;
        }

        int sx = Integer.compare(x2, x1), sy = Integer.compare(y2, y1);
        int x = x1, y = y1;

        if (dx > dy) {
            int p = 2 * dy - dx;
            for (int i = 0; i <= dx; i++) {
                double t = (double) i / steps;
                zBuffer.setPixelWithZTest(x, y, z1 + t * (z2 - z1), interpolate(c1, c2, t));
                x += sx;
                if (p >= 0) { y += sy; p += 2 * (dy - dx); }
                else { p += 2 * dy; }
            }
        } else {
            int p = 2 * dx - dy;
            for (int i = 0; i <= dy; i++) {
                double t = (double) i / steps;
                zBuffer.setPixelWithZTest(x, y, z1 + t * (z2 - z1), interpolate(c1, c2, t));
                y += sy;
                if (p >= 0) { x += sx; p += 2 * (dx - dy); }
                else { p += 2 * dx; }
            }
        }
    }

    private int interpolate(int c1, int c2, double t) {
        int r = (int) (((c1 >> 16) & 0xFF) + t * (((c2 >> 16) & 0xFF) - ((c1 >> 16) & 0xFF)));
        int g = (int) (((c1 >>  8) & 0xFF) + t * (((c2 >>  8) & 0xFF) - ((c1 >>  8) & 0xFF)));
        int b = (int) (( c1        & 0xFF) + t * (( c2        & 0xFF) - ( c1        & 0xFF)));
        return (r << 16) | (g << 8) | b;
    }
}

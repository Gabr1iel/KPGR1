package cz.algone.algorithm.rasterizer.triangle;

import cz.algone.objectData.Vertex;
import cz.algone.raster.ZBuffer;
import cz.algone.shader.Shader;
import cz.algone.util.Lerp;

public class TriangleRasterizer {
    private ZBuffer zBuffer;
    private final Lerp<Vertex> lerp = new Lerp<>();

    public TriangleRasterizer(ZBuffer zBuffer) {
        this.zBuffer = zBuffer;
    }

    public void setup(ZBuffer zBuffer) {
        this.zBuffer = zBuffer;
    }

    /** Rasterizuje trojúhelník (vrcholy jsou již v souřadnicích okna + mají z v NDC) */
    public void rasterize(Vertex a, Vertex b, Vertex c, Shader shader) {
        if (zBuffer == null) return;

        int screenW = zBuffer.getWidth();
        int screenH = zBuffer.getHeight();

        // 1. Seřadit vrcholy podle y (aY ≤ bY ≤ cY)
        if (a.getY() > b.getY()) { Vertex tmp = a; a = b; b = tmp; }
        if (b.getY() > c.getY()) { Vertex tmp = b; b = c; c = tmp; }
        if (a.getY() > b.getY()) { Vertex tmp = a; a = b; b = tmp; }

        int aY = (int) Math.round(a.getY());
        int bY = (int) Math.round(b.getY());
        int cY = (int) Math.round(c.getY());

        // Ořezání y na obrazovku – zabrání iteraci přes miliony mimoscénových řádků
        int yMin = Math.max(aY, 0);
        int yMid = Math.min(bY, screenH - 1);
        int yMax = Math.min(cY, screenH - 1);

        // 2. Horní polovina trojúhelníku (od aY do bY)
        for (int y = yMin; y <= Math.min(bY, yMid); y++) {
            double tAC = (cY == aY) ? 0 : (double)(y - aY) / (cY - aY);
            Vertex ac = lerp.lerp(a, c, tAC);

            double tAB = (bY == aY) ? 0 : (double)(y - aY) / (bY - aY);
            Vertex ab = lerp.lerp(a, b, tAB);

            drawScanline(y, ab, ac, shader, screenW);
        }

        // 3. Dolní polovina trojúhelníku (od bY do cY)
        for (int y = Math.max(bY, yMin); y <= yMax; y++) {
            double tAC = (cY == aY) ? 1 : (double)(y - aY) / (cY - aY);
            Vertex ac = lerp.lerp(a, c, tAC);

            double tBC = (cY == bY) ? 0 : (double)(y - bY) / (cY - bY);
            Vertex bc = lerp.lerp(b, c, tBC);

            drawScanline(y, bc, ac, shader, screenW);
        }
    }

    private void drawScanline(int y, Vertex left, Vertex right, Shader shader, int screenW) {
        // Zajistit, že left.x ≤ right.x
        if (left.getX() > right.getX()) { Vertex tmp = left; left = right; right = tmp; }

        int xLeft  = (int) Math.round(left.getX());
        int xRight = (int) Math.round(right.getX());

        // Ořezání x na obrazovku
        int xStart = Math.max(xLeft, 0);
        int xEnd   = Math.min(xRight, screenW - 1);

        for (int x = xStart; x <= xEnd; x++) {
            double t = (xRight == xLeft) ? 0 : (double)(x - xLeft) / (xRight - xLeft);
            Vertex pixel = lerp.lerp(left, right, t);

            double z = pixel.getZ();
            int color = shader.getColor(pixel).getRGB();
            zBuffer.setPixelWithZTest(x, y, z, color);
        }
    }
}

package cz.algone.line;

import cz.algone.raster.RasterCanvas;
import cz.algone.rasterize.Rasterizer;

public class LineRasterizerBresenham implements Rasterizer<Line> {
    private RasterCanvas raster;

    @Override
    public void setup(RasterCanvas raster) {
        this.raster = raster;
    }

    @Override
    public void rasterize(Line line) {
        rasterize(line.getX1(), line.getY1(), line.getX2(), line.getY2());
    }

    public void rasterize(int x1, int y1, int x2, int y2) {
        int lengthX = Math.abs(x2 - x1);
        int lengthY = Math.abs(y2 - y1);

        //Vrací jednu ze tří možností (x2 >= x1 (1),x2 == x1 (0),x2 <= x1 (-1))
        int incrementX = Integer.compare(x2, x1);
        int incrementY = Integer.compare(y2, y1);

        int x = x1;
        int y = y1;

        if (lengthX > lengthY) {
            int p = 2 * lengthY - lengthX;
            for (int i = 0; i < lengthX; i++) {
                x += incrementX;
                if (p >= 0) {
                    y += incrementY;
                    p += 2 * (lengthY - lengthX);
                } else {
                    p += 2 * lengthY;
                }
                raster.setPixel(x, y, 0xFFFF0000);
            }
        } else {
            int p = 2 * lengthX - lengthY;
            for (int i = 0; i < lengthY; i++) {
                y += incrementY;
                if (p >= 0) {
                    x += incrementX;
                    p += 2 * (lengthX - lengthY);
                } else {
                    p += 2 * lengthX;
                }
                raster.setPixel(x, y, 0xFFFF0000);
            }
        }
    }
}
package cz.algone.rasterize;

import cz.algone.model.Line;
import cz.algone.raster.RasterBufferedImage;

public class LineRasterizerTrivial implements Rasterizer<Line> {
    private final RasterBufferedImage raster;

    public LineRasterizerTrivial(RasterBufferedImage raster) {
        this.raster = raster;
    }

    @Override
    public void rasterize(Line line) {
        rasterize(line.getX1(), line.getY1(), line.getX2(), line.getY2());
    }

    public void rasterize(int x1, int y1, int x2, int y2) {
        //ošetření vykreslení vertikální čáry
        if (x1 == x2) {
            if (y1 > y2) {
                int temp = y1;
                y1 = y2;
                y2 = temp;
            }
            for (int y = y1; y <= y2; y++) {
                raster.setPixel(x1, y, 0xff0000);
            }
            return;
        }

        float k = (float) (y2 - y1) / (x2 - x1);
        float q = y1 - k * x1;

        //vykreslení pro 2 / 3 kvadrant
        if (k > 1) {
            if (y1 > y2) {
                int temp = y1;
                y1 = y2;
                y2 = temp;
            }
            for (int y = y1; y <= y2; y++) {
                float x = (y - q) / k;
                raster.setPixel(Math.round(x), y, 0xff0000);
            }
        } else { //vykreslení pro 1 a 4 kvadrant
            if (x1 > x2) {
                int temp = x1;
                x1 = x2;
                x2 = temp;
            }
            for (int x = x1; x <= x2; x++) {
                float y = k * x + q;
                raster.setPixel(x, Math.round(y), 0xff0000);
            }
        }
    }
}

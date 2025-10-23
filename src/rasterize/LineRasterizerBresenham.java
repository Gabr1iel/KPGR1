package rasterize;

import raster.RasterBufferedImage;

public class LineRasterizerBresenham extends LineRasterizer {

    public LineRasterizerBresenham(RasterBufferedImage raster) {
        super(raster);
    }

    @Override
    public void rasterize(int x1, int y1, int x2, int y2) {
        int lengthX = x2 - x1;
        int lengthY = y2 - y1;

        int sx = Integer.compare(x2, x1);
        int sy = Integer.compare(y2, y1);

        int x = x1;
        int y = y1;

        if (lengthX > lengthY) {
            int p = 2 * lengthY - lengthX;
            for (int i = 0; i < lengthX; i++) {
                x += 1;
                if (p >= 0) {
                    y += 1;
                    p += 2 * (lengthY - lengthX);
                } else {
                    p += 2 * lengthY;
                }
                raster.setPixel(x, y, 0xff0000);
            }
        } else {
            int p = 2 * lengthX - lengthY;
            for (int i = 0; i < lengthY; i++) {
                y += 1;
                if (p >= 0) {
                    x += 1;
                    p += 2 * (lengthX - lengthY);
                } else {
                    p += 2 * lengthX;
                }
                raster.setPixel(x, y, 0xff0000);
            }
        }
    }
}

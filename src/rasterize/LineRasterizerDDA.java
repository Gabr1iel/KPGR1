package rasterize;

import raster.RasterBufferedImage;

public class LineRasterizerDDA extends LineRasterizer{
    public LineRasterizerDDA(RasterBufferedImage raster) {
        super(raster);
    }

    @Override
    public void rasterize(int x1, int y1, int x2, int y2) {
        int lengthX = x2 - x1;
        int lengthY = y2 - y1;
        int biggerLength = Math.max(Math.abs(lengthX), Math.abs(lengthY));

        float xIncrement = (float) lengthX / biggerLength;
        float yIncrement = (float) lengthY / biggerLength;

        float x = x1;
        float y = y1;

        for (int i = 0; i < biggerLength; i++) {
            raster.setPixel(Math.round(x), Math.round(y), 0xff0000);
            x += xIncrement;
            y += yIncrement;
        }
    }
}

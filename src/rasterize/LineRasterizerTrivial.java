package rasterize;

import model.Line;
import raster.RasterBufferedImage;

import java.awt.*;

public class LineRasterizerTrivial extends LineRasterizer {
    public LineRasterizerTrivial(RasterBufferedImage raster) {
        super(raster);
    }

    @Override
    public void rasterize(Line line) {
        
    }

    @Override
    public void rasterize(int x1, int y1, int x2, int y2) {
        float k = (float) (y2 - y1) / (x2 - x1);
        float q = y1 - k * x1;

        //dodělat trivial (přehození z orientace x na y pro 2 a 3 kvadrant)
        if (k > 1) {

        } else {
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

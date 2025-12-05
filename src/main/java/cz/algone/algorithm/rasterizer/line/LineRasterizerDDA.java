package cz.algone.algorithm.rasterizer.line;

import cz.algone.model.Line;
import cz.algone.raster.RasterCanvas;
import cz.algone.algorithm.rasterizer.Rasterizer;
import cz.algone.util.color.ColorPair;
import cz.algone.util.color.ColorUtils;


public class LineRasterizerDDA implements Rasterizer<Line> {
    private RasterCanvas raster;

    @Override
    public void setup(RasterCanvas raster) {
        this.raster = raster;
    }

    @Override
    public void rasterize(Line line, ColorPair colors) {
        rasterize(line.getX1(), line.getY1(), line.getX2(), line.getY2(), colors);
    }

    public void rasterize(int x1, int y1, int x2, int y2, ColorPair colors) {
        int lengthX = x2 - x1;
        int lengthY = y2 - y1;
        int biggerLength = Math.max(Math.abs(lengthX), Math.abs(lengthY));

        float xIncrement = (float) lengthX / biggerLength;
        float yIncrement = (float) lengthY / biggerLength;

        float x = x1;
        float y = y1;

        for (int i = 0; i < biggerLength; i++) {
            float t = (float) i / biggerLength;
            int color = ColorUtils.interpolateColor(colors.primary(), colors.secondary(), t);
            raster.setPixel(Math.round(x), Math.round(y), color);
            x += xIncrement;
            y += yIncrement;
        }
    }
}

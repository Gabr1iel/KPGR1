package cz.algone.algorithm.rasterizer.line;

import cz.algone.model.models2D.Line;
import cz.algone.raster.ImageBuffer;
import cz.algone.algorithm.rasterizer.IRasterizer;
import cz.algone.util.color.ColorPair;
import cz.algone.util.color.ColorUtils;

public class LineRasterizerTrivial implements IRasterizer<Line> {
    private ImageBuffer imageBuffer;

    @Override
    public void setup(ImageBuffer raster) {
        this.imageBuffer = raster;
    }

    @Override
    public void rasterize(Line line) {
        rasterize(line.getX1(), line.getY1(), line.getX2(), line.getY2(), line.getColors());
    }

    public void rasterize(int x1, int y1, int x2, int y2, ColorPair colors) {
        int steps = x2 - x1;
        if (x1 == x2) {
            if (y1 > y2) {
                int temp = y1;
                y1 = y2;
                y2 = temp;
            }
            for (int y = y1; y <= y2; y++) {
                imageBuffer.setValue(x1, y, getColor(y, steps, colors));
            }
            return;
        }

        float k = (float) (y2 - y1) / (x2 - x1);
        float q = y1 - k * x1;

        if (k > 1) {
            if (y1 > y2) {
                int temp = y1;
                y1 = y2;
                y2 = temp;
            }
            for (int y = y1; y <= y2; y++) {
                float x = (y - q) / k;
                imageBuffer.setValue(Math.round(x), y, getColor(y, steps, colors));
            }
        } else {
            if (x1 > x2) {
                int temp = x1;
                x1 = x2;
                x2 = temp;
            }
            for (int x = x1; x <= x2; x++) {
                float y = k * x + q;
                imageBuffer.setValue(x, Math.round(y), getColor(x, steps, colors));
            }
        }
    }

    private int getColor(int currentStep, int steps, ColorPair colors) {
        float t = (float) currentStep / (float) steps;
        return ColorUtils.interpolateColor(colors.primary(), colors.secondary(), t);
    }
}

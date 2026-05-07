package cz.algone.algorithm.rasterizer.line;

import cz.algone.model.models2D.Line;
import cz.algone.raster.ImageBuffer;
import cz.algone.algorithm.rasterizer.IRasterizer;
import cz.algone.util.color.ColorPair;
import cz.algone.util.color.ColorUtils;

public class LineRasterizerBresenham implements IRasterizer<Line> {
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
        int lengthX = Math.abs(x2 - x1);
        int lengthY = Math.abs(y2 - y1);
        int steps = Math.max(lengthX, lengthY);

        int incrementX = Integer.compare(x2, x1);
        int incrementY = Integer.compare(y2, y1);

        int x = x1;
        int y = y1;

        if (lengthX > lengthY) {
            int p = 2 * lengthY - lengthX;
            for (int i = 0; i < lengthX; i++) {
                float t = (float) i / steps;
                int color = ColorUtils.interpolateColor(colors.primary(), colors.secondary(), t);
                x += incrementX;
                if (p >= 0) {
                    y += incrementY;
                    p += 2 * (lengthY - lengthX);
                } else {
                    p += 2 * lengthY;
                }
                imageBuffer.setValue(x, y, color);
            }
        } else {
            int p = 2 * lengthX - lengthY;
            for (int i = 0; i < lengthY; i++) {
                float t = (float) i / steps;
                int color = ColorUtils.interpolateColor(colors.primary(), colors.secondary(), t);
                y += incrementY;
                if (p >= 0) {
                    x += incrementX;
                    p += 2 * (lengthX - lengthY);
                } else {
                    p += 2 * lengthX;
                }
                imageBuffer.setValue(x, y, color);
            }
        }
    }
}
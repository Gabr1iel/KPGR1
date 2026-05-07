package cz.algone.algorithm.rasterizer.circle;

import cz.algone.algorithm.rasterizer.IRasterizer;
import cz.algone.model.models2D.Circle;
import cz.algone.model.models2D.Point;
import cz.algone.raster.ImageBuffer;
import cz.algone.util.color.ColorPair;
import cz.algone.util.color.ColorUtils;

public class CircleRasterizerMidpoint implements IRasterizer<Circle> {
    private ImageBuffer imageBuffer;

    @Override
    public void setup(ImageBuffer raster) {
        this.imageBuffer = raster;
    }

    @Override
    public void rasterize(Circle circle) {
        if (imageBuffer == null) throw new IllegalStateException("CircleRasterizerMidpoint: raster not set.");

        Point center = circle.getCenter();
        if (center == null) return;

        int r = circle.getRadius();
        if (r <= 0) {
            int color = ColorUtils.interpolateColor(circle.getColors().primary(), null, 0);
            imageBuffer.setValue(center.x(), center.y(), color);
            return;
        }

        int cx = center.x();
        int cy = center.y();

        int x = 0;
        int y = r;
        int d = 1 - r;

        plot8(cx, cy, x, y, circle.getColors());

        while (x < y) {
            x++;

            if (d < 0) {
                d += 2 * x + 1;
            } else {
                y--;
                d += 2 * (x - y) + 1;
            }

            plot8(cx, cy, x, y, circle.getColors());
        }
    }
    /** Zapíše 8 symetrických pixelů kolem středu pro daný oktant. */
    private void plot8(int cx, int cy, int x, int y, ColorPair colors) {
        plot(cx + x, cy + y, cx, cy, colors);
        plot(cx - x, cy + y, cx, cy, colors);
        plot(cx + x, cy - y, cx, cy, colors);
        plot(cx - x, cy - y, cx, cy, colors);

        plot(cx + y, cy + x, cx, cy, colors);
        plot(cx - y, cy + x, cx, cy, colors);
        plot(cx + y, cy - x, cx, cy, colors);
        plot(cx - y, cy - x, cx, cy, colors);
    }

    private void plot(int px, int py, int cx, int cy, ColorPair colors) {
        int color = ringColorAtAngle(px, py, cx, cy, colors);
        imageBuffer.setValue(px, py, color);
    }

    private int ringColorAtAngle(int px, int py, int cx, int cy, ColorPair colors) {
        if (colors.secondary() == null) {
            return ColorUtils.interpolateColor(colors.primary(), null, 0);
        }

        double angle = Math.atan2(py - cy, px - cx);
        double t = (angle + Math.PI) / (2.0 * Math.PI);

        return ColorUtils.interpolateColor(colors.primary(), colors.secondary(), (float) t);
    }
}

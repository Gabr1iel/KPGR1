package cz.algone.algorithm.fill.seed;

import cz.algone.algorithm.fill.FillMode;
import cz.algone.algorithm.fill.IFill;
import cz.algone.algorithm.fill.pattern.IPattern;
import cz.algone.model.Model;
import cz.algone.model.Point;
import cz.algone.raster.RasterCanvas;
import cz.algone.util.color.ColorPair;
import cz.algone.util.color.ColorUtils;

import java.util.ArrayDeque;
import java.util.Deque;

public class SeedFill implements IFill {
    private RasterCanvas raster;
    private final FillMode mode;
    private IPattern pattern = null;

    public SeedFill(FillMode mode) {
        this.mode = mode;
    }

    @Override
    public void setup(RasterCanvas raster) {
        this.raster = raster;
    }

    @Override
    public void fill(Model point, ColorPair colors, int borderColor) {
        Point seedPoint = (Point) point;
        int seedX = seedPoint.x();
        int seedY = seedPoint.y();

        int fillColor =  ColorUtils.interpolateColor(colors.primary(), null, 0);
        int seedColor = raster.getPixel(seedX, seedY);

        //Ošetření kliknutí přímo na hranici polygonu
        if (mode == FillMode.BORDER && seedColor == borderColor) return;

        int width = raster.getWidth();
        int height = raster.getHeight();
        if (seedX < 0 || seedX >= width || seedY < 0 || seedY >= height) return;

        boolean[] visited = new boolean[width * height];
        Deque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[]{seedX, seedY});

        while (!stack.isEmpty()) {
            int[] p = stack.pop();
            int px = p[0];
            int py = p[1];

            if (px < 0 || px >= width || py < 0 || py >= height) continue;

            int idx = py * width + px;
            if (visited[idx]) continue;
            visited[idx] = true;

            int currPixelColor = raster.getPixel(px, py);

            if (!shouldFill(currPixelColor, seedColor, borderColor, mode)) continue;

            int pixelColor = (pattern != null)
                    ? pattern.colorAt(px, py, colors)
                    : fillColor;

            raster.setPixel(px, py, pixelColor);

            stack.push(new int[]{px + 1, py});
            stack.push(new int[]{px - 1, py});
            stack.push(new int[]{px, py + 1});
            stack.push(new int[]{px, py - 1});
        }
    }
    /** Kontrola podmínek pro daný fill mod (Border, Background) */
    private boolean shouldFill(int current, int seedColor, int borderColor, FillMode mode) {
        return switch (mode) {
            case BACKGROUND -> current == seedColor;
            case BORDER -> current != borderColor;
        };
    }

    @Override
    public void setPattern(IPattern pattern) {
        this.pattern = pattern;
    }
}

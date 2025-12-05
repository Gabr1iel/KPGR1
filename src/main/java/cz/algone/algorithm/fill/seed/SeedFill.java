package cz.algone.algorithm.fill.seed;

import cz.algone.algorithm.fill.FillMode;
import cz.algone.algorithm.fill.IFill;
import cz.algone.raster.RasterCanvas;
import cz.algone.util.color.ColorPair;
import cz.algone.util.color.ColorUtils;
import javafx.scene.paint.Color;

import java.util.ArrayDeque;
import java.util.Deque;

public class SeedFill implements IFill {
    private RasterCanvas raster;
    private int borderColor;

    @Override
    public void setup(RasterCanvas raster) {
        this.raster = raster;
    }

    @Override
    public void fill(int x, int y, ColorPair color, FillMode mode) {
        int fillColor =  ColorUtils.interpolateColor(color.primary(), null, 0);
        int seedColor = raster.getPixel(x, y);

        if (seedColor == fillColor && seedColor != 0) return;

        int width = raster.getWidth();
        int height = raster.getHeight();

        Deque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[]{x, y});

        while (!stack.isEmpty()) {
            int[] p = stack.pop();
            int px = p[0];
            int py = p[1];

            if (px < 0 || px >= width || py < 0 || py >= height) continue;

            int current = raster.getPixel(px, py);

            if (!shoudFill(current, seedColor, mode)) continue;

            raster.setPixel(px, py, fillColor);

            stack.push(new int[]{px + 1, py});
            stack.push(new int[]{px - 1, py});
            stack.push(new int[]{px, py + 1});
            stack.push(new int[]{px, py - 1});
        }
    }

    private boolean shoudFill(int current, int seedColor, FillMode mode) {
        return switch (mode) {
            case BACKGROUND -> current == seedColor;
            case BORDER -> current != borderColor;
        };
    }

    public void setBorderColor(int color) {
        this.borderColor = borderColor;
    }
}

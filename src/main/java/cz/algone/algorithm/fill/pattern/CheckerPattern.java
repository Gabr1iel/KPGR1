package cz.algone.algorithm.fill.pattern;

import cz.algone.util.color.ColorPair;
import cz.algone.util.color.ColorUtils;

public class CheckerPattern implements IPattern {
    private final int size;

    public CheckerPattern(int size) {
        this.size = Math.max(1, size);
    }

    @Override
    public int colorAt(int x, int y, ColorPair color) {
        int color1 = ColorUtils.interpolateColor(color.primary(), null, 0);
        int color2 = (color.secondary() != null)
                ? ColorUtils.interpolateColor(color.secondary(), null, 0)
                : ColorUtils.darken(color.primary(), ColorUtils.DEFAULT_CHECKERPATTERN_DARKEN);

        int cellX = Math.floorDiv(x, size);
        int cellY = Math.floorDiv(y, size);

        boolean odd = ((cellX + cellY) & 1) == 1;
        return odd ? color2 : color1;
    }
}

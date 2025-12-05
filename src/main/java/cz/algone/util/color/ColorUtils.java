package cz.algone.util.color;

import javafx.scene.paint.Color;

public class ColorUtils {
    public static final ColorPair DEFAULT_COLORPICKER_COLOR = new ColorPair(Color.BLACK, null);

    public static int interpolateColor(Color primary, Color secondary, float t) {
        int r1 = (int) Math.round(primary.getRed() * 255);
        int g1 = (int) Math.round(primary.getGreen() * 255);
        int b1 = (int) Math.round(primary.getBlue() * 255);

        int r, g, b;

        if (!(secondary == null)) {
            int r2 = (int) Math.round(secondary.getRed() * 255);
            int g2 = (int) Math.round(secondary.getGreen() * 255);
            int b2 = (int) Math.round(secondary.getBlue() * 255);

            r = (int) (r1 + (r2 - r1) * t);
            g = (int) (g1 + (g2 - g1) * t);
            b = (int) (b1 + (b2 - b1) * t);
        } else {
            r = r1;
            g = g1;
            b = b1;
        }
        return (0xFF << 24) | (r << 16) | (g << 8) | b;
    }
}

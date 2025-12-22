package cz.algone.util.color;

import javafx.scene.paint.Color;

public class ColorUtils {
    public static final ColorPair DEFAULT_COLORPICKER_COLOR = new ColorPair(Color.BLACK, null);
    public static final double DEFAULT_CHECKERPATTERN_DARKEN = 0.25;

    public static int interpolateColor(Color primary, Color secondary, float t) {
        Rgb rgb1 = parseColorToInt(primary);
        int r, g, b;

        if (!(secondary == null)) {
            Rgb rgb2 = parseColorToInt(secondary);
            r = (int) (rgb1.r() + (rgb2.r() - rgb1.r()) * t);
            g = (int) (rgb1.g() + (rgb2.g() - rgb1.g()) * t);
            b = (int) (rgb1.b() + (rgb2.b() - rgb1.b()) * t);
        } else {
            r = rgb1.r();
            g = rgb1.g();
            b = rgb1.b();
        }
        return (0xFF << 24) | (r << 16) | (g << 8) | b;
    }

    public static int darken(Color color, double amount) {
        amount = Math.max(0, Math.min(1, amount));
        Rgb rgb1 = parseColorToInt(color);

        int r = (int) Math.round(rgb1.r() * (1.0 - amount));
        int g = (int) Math.round(rgb1.g() * (1.0 - amount));
        int b = (int) Math.round(rgb1.b() * (1.0 - amount));

        return (0xFF << 24) | (r << 16) | (g << 8) | b;
    }

    public static Rgb parseColorToInt(Color color) {
        int r = (int) Math.round(color.getRed() * 255);
        int g = (int) Math.round(color.getGreen() * 255);
        int b = (int) Math.round(color.getBlue() * 255);
        return new Rgb(r, g, b);
    }
}

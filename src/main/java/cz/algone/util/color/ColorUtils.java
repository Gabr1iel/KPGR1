package cz.algone.util.color;

import cz.algone.transforms.Col;
import javafx.scene.paint.Color;

public class ColorUtils {
    public static final ColorPair DEFAULT_COLORPICKER_COLOR = new ColorPair(new Col(0, 0, 0), null);
    public static final ColorPair DEFAULT_CLIP_COLOR = new ColorPair(new Col(0, 0, 255), null);
    public static final ColorPair DEFAULT_HIGHLIGHT_COLOR = new ColorPair(new Col(139, 0, 0), null);
    public static final ColorPair DEFAULT_HIGHLIGHT_BACKGROUND_COLOR = new ColorPair(new Col(255, 0, 0), null);
    public static final double DEFAULT_CHECKERPATTERN_DARKEN = 0.25;

    /** Lineárně interpoluje mezi primary a secondary v [0,1],
     *  pokud secondary == null vrátí packed int primary barvy.
     *  Vrací ARGB (alpha = 0xFF) aby byly nakreslené pixely odlišitelné od prázdného bufferu (0). */
    public static int interpolateColor(Col primary, Col secondary, float t) {
        if (secondary == null || t == 0) return primary.getARGB();
        return primary.mulna(1 - t).addna(secondary.mulna(t)).getARGB();
    }

    /** Ztmaví barvu o amount (0–1), vrací ARGB */
    public static int darken(Col color, double amount) {
        amount = Math.max(0, Math.min(1, amount));
        return color.mulna(1.0 - amount).getARGB();
    }

    /** Převede JavaFX {@link Color} na {@link Col} – použít pouze na UI hranici */
    public static Col toCol(Color color) {
        return new Col(color.getRed(), color.getGreen(), color.getBlue(), color.getOpacity());
    }
}

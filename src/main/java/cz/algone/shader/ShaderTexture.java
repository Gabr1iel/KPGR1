package cz.algone.shader;

import cz.algone.objectData.Vertex;
import cz.algone.transforms.Col;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class ShaderTexture implements Shader {
    private final Image image;
    private final PixelReader reader;
    private final int imgWidth;
    private final int imgHeight;

    /** Texturový shader vzorkující JavaFX obrázek podle UV. */
    public ShaderTexture(Image image) {
        this.image = image;
        this.reader = image.getPixelReader();
        this.imgWidth  = (int) image.getWidth();
        this.imgHeight = (int) image.getHeight();
    }

    /** Texturový shader generující procedurální šachovnici. */
    public ShaderTexture() {
        this.image = null;
        this.reader = null;
        this.imgWidth = 0;
        this.imgHeight = 0;
    }

    /** Vrací true pokud má shader nastavený obrázek. */
    public boolean hasImage() { return image != null; }

    @Override
    public Col getColor(Vertex pixel) {
        double invW = pixel.getW();
        double u, v;
        if (invW > 0) {
            u = pixel.getU() / invW;
            v = pixel.getV() / invW;
        } else {
            u = pixel.getU();
            v = pixel.getV();
        }
        u = Math.max(0, Math.min(1, u));
        v = Math.max(0, Math.min(1, v));

        if (image == null) {
            int cu = (int)(u * 8);
            int cv = (int)(v * 8);
            return ((cu + cv) % 2 == 0) ? new Col(240, 240, 240) : new Col(30, 30, 30);
        }

        int x = (int)(u * (imgWidth  - 1));
        int y = (int)(v * (imgHeight - 1));
        Color fx = reader.getColor(x, y);
        return new Col(
            (int)(fx.getRed()   * 255),
            (int)(fx.getGreen() * 255),
            (int)(fx.getBlue()  * 255)
        );
    }
}

package cz.algone.raster;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelWriter;

public class RasterCanvas implements Raster {

    private final Canvas canvas;
    private final PixelWriter pixelWriter;
    private final GraphicsContext graphicsContext;

    public RasterCanvas(Canvas canvas) {
        this.canvas = canvas;
        this.graphicsContext = canvas.getGraphicsContext2D();
        this.pixelWriter = graphicsContext.getPixelWriter();
    }

    @Override
    public void setPixel(int x, int y, int color) {
        //ošetření pixelů mimo rastr
        if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight())
            pixelWriter.setArgb(x, y, color);
    }

    @Override
    public int getPixel(int x, int y) {
        // TODO: druhá úloha
        return 0;
    }

    @Override
    public int getWidth() {
        return (int) canvas.getWidth();
    }

    @Override
    public int getHeight() {
        return (int) canvas.getHeight();
    }

    @Override
    public void clear() {
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public Canvas getCanvas() {
        return canvas;
    }
}

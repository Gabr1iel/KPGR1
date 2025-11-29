package cz.algone.raster;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class RasterCanvas implements Raster {
    private final Canvas canvas;
    private final PixelWriter pixelWriter;
    private final GraphicsContext graphicsContext;
    private int width;
    private int height;

    public RasterCanvas(Canvas canvas) {
        this.canvas = canvas;
        this.graphicsContext = canvas.getGraphicsContext2D();
        this.pixelWriter = graphicsContext.getPixelWriter();
        canvas.setWidth(width);
        canvas.setHeight(height);
    }

    @Override
    public void setPixel(int x, int y, int color) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color) & 0xFF;
        //ošetření pixelů mimo rastr
        if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight())
            pixelWriter.setColor(x, y, Color.rgb(r, g, b));
    }

    @Override
    public int getPixel(int x, int y) {
        // TODO: druhá úloha
        return 0;
    }

    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
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

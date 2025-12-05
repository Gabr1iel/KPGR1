package cz.algone.raster;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.Arrays;

public class RasterCanvas implements Raster {
    private final Canvas canvas;
    private final PixelWriter pixelWriter;
    private final GraphicsContext graphicsContext;

    private int width;
    private int height;

    private int[] pixels;

    public RasterCanvas(Canvas canvas) {
        this.canvas = canvas;
        this.graphicsContext = canvas.getGraphicsContext2D();
        this.pixelWriter = graphicsContext.getPixelWriter();

        this.width = (int) canvas.getWidth();
        this.height = (int) canvas.getHeight();

        this.pixels = new int[width * height];
    }

    @Override
    public void setPixel(int x, int y, int color) {
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) return;
        pixels[y * width + x] = color;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color) & 0xFF;

        pixelWriter.setColor(x, y, Color.rgb(r, g, b));
    }

    @Override
    public int getPixel(int x, int y) {
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) return 0;
        return pixels[y * width + x];
    }

    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new int[width * height];

        clear();
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void clear() {
        Arrays.fill(pixels, 0);
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void clearListeners() {
        canvas.setOnMouseClicked(null);
        canvas.setOnMousePressed(null);
        canvas.setOnMouseReleased(null);
        canvas.setOnMouseMoved(null);
        canvas.setOnMouseDragged(null);
    }

    public Canvas getCanvas() {
        return canvas;
    }
}

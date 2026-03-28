package cz.algone.raster;

import java.util.Arrays;

public class DepthBuffer implements Buffer<Double> {
    private final double DEFAULT_VALUE = 1.0;
    private int width;
    private int height;

    private double[] depthBuffer;

    public DepthBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        depthBuffer = new double[width * height];
        clear();
    }


    @Override
    public void setValue(int x, int y, Double value) {
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) return;
        depthBuffer[y * width + x] = value;
    }

    @Override
    public Double getValue(int x, int y) {
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) return 0.0;
        return depthBuffer[y * width + x];
    }

    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        depthBuffer = new double[width * height];
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
        Arrays.fill(depthBuffer, DEFAULT_VALUE);
    }
}

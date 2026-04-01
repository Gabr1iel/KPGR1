package cz.algone.raster;

public class ZBuffer {
    private final Buffer<Integer> imageBuffer;
    private final Buffer<Double> depthBuffer;

    public ZBuffer(Buffer<Integer> imageBuffer) {
        this.imageBuffer = imageBuffer;
        this.depthBuffer = new DepthBuffer(imageBuffer.getWidth(), imageBuffer.getHeight());
    }

    public void setPixelWithZTest(int x, int y, double z, int color) {
        if (z > depthBuffer.getValue(x, y)) return;
        depthBuffer.setValue(x, y, z);
        imageBuffer.setValue(x, y, color);
    }

    public void clear() {
        imageBuffer.clear();
        depthBuffer.clear();
    }

    public void resize(int width, int height) {
        depthBuffer.resize(width, height);
    }

    public int getWidth() {return imageBuffer.getWidth();}
    public int getHeight() {return imageBuffer.getHeight();}
}

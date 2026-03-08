package cz.algone.raster;

public interface Buffer<E> {
    void setValue(int x, int y, E value);
    E getValue(int x, int y);
    void resize(int width, int height);
    int getWidth();
    int getHeight();
    void clear();
}

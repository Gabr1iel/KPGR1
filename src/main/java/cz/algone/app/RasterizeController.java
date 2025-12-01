package cz.algone.app;

import cz.algone.model.Model;
import cz.algone.raster.RasterCanvas;
import cz.algone.rasterizer.Rasterizer;
import cz.algone.util.color.ColorPair;

public interface RasterizeController<T extends Model> {
    void initListeners();
    void drawScene();
    void clearRaster();
    void setup(RasterCanvas raster, Rasterizer<T> rasterizer);
    void setColors(ColorPair colors);
}

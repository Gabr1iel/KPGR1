package cz.algone.rasterizer;

import cz.algone.model.Model;
import cz.algone.raster.RasterCanvas;
import cz.algone.util.color.ColorPair;

public interface Rasterizer<T extends Model> {
    void rasterize(T model, ColorPair colors);
    void setup(RasterCanvas raster);
}

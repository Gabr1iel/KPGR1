package cz.algone.rasterizer;

import cz.algone.model.Model;
import cz.algone.raster.RasterCanvas;

public interface Rasterizer<T extends Model> {
    void rasterize(T model);
    void setup(RasterCanvas raster);
}

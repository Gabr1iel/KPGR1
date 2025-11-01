package cz.algone.controller;

import cz.algone.model.Model;
import cz.algone.raster.RasterCanvas;
import cz.algone.rasterize.Rasterizer;

public interface RasterizeController<T extends Model> {
    void initListeners();
    void drawScene();
    void clearRaster();
    void setup(RasterCanvas raster, Rasterizer<T> rasterizer);
}

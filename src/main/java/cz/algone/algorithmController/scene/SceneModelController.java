package cz.algone.algorithmController.scene;

import cz.algone.model.SceneModel;
import cz.algone.raster.RasterCanvas;

public record SceneModelController(RasterCanvas raster, SceneModel sceneModel) {
    public void clearRasterAndScene() {
        raster.clear();
        sceneModel.clear();
    }

    public void clearRaster() {
        raster.clear();
    }
}

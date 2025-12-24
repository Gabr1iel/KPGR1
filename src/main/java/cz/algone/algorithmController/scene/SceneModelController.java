package cz.algone.algorithmController.scene;

import cz.algone.model.SceneModel;
import cz.algone.raster.RasterCanvas;

public class SceneModelController {
    private final RasterCanvas raster;
    private final SceneModel sceneModel;

    public SceneModelController(RasterCanvas raster, SceneModel sceneModel) {
        this.raster = raster;
        this.sceneModel = sceneModel;
    }

    public void clearRasterAndScene() {
        raster.clear();
        sceneModel.clear();
    }

    public void clearRaster() {
        raster.clear();
    }

    public RasterCanvas getRaster() {
        return raster;
    }

    public SceneModel getSceneModel() {
        return sceneModel;
    }
}

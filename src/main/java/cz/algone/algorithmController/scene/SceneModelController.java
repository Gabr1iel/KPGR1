package cz.algone.algorithmController.scene;

import cz.algone.model.SceneModel;
import cz.algone.model.models3D.Solid;
import cz.algone.model.models3D.SolidToggleEvent;
import cz.algone.model.models3D.SolidsCollection;
import cz.algone.raster.RasterCanvas;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SceneModelController {
    private final RasterCanvas raster;
    private final SceneModel sceneModel;
    private final StringProperty rasterStatusText = new SimpleStringProperty("");
    private final SolidsCollection solidsCollection = new SolidsCollection();

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

    public void toggleSolids(SolidToggleEvent event) {
        if (event.enabled()) {
            sceneModel.getSolids().put(event.alias(), solidsCollection.solidsMap.get(event.alias()));
        } else {
            Solid solid = solidsCollection.solidsMap.get(event.alias());
            solid.resetTransform();
            sceneModel.getSolids().remove(event.alias());
        }
    }

    public SceneModel getSceneModel() {return sceneModel;}

    public RasterCanvas getRaster() {return raster;}

    public StringProperty getRasterStatus() {return rasterStatusText;}
    public String getRasterStatusText() {return rasterStatusText.get();}
    public void setRasterStatusText(String statusText) {this.rasterStatusText.set(statusText);}
}

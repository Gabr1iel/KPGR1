package cz.algone.algorithmController.scene;

import cz.algone.model.SceneModel;
import cz.algone.model.models3D.wiredSolids.Solid;
import cz.algone.model.models3D.SolidToggleEvent;
import cz.algone.model.models3D.wiredSolids.SolidsCollection;
import cz.algone.raster.ImageBuffer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SceneModelController {
    private final ImageBuffer imageBuffer;
    private final SceneModel sceneModel;
    private final StringProperty rasterStatusText = new SimpleStringProperty("");
    private final SolidsCollection solidsCollection = new SolidsCollection();

    public SceneModelController(ImageBuffer imageBuffer, SceneModel sceneModel) {
        this.imageBuffer = imageBuffer;
        this.sceneModel = sceneModel;
    }

    public void clearRasterAndScene() {
        imageBuffer.clear();
        sceneModel.clear();
    }
    public void clearRaster() {
        imageBuffer.clear();
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

    public ImageBuffer getImageBuffer() {return imageBuffer;}

    public StringProperty getRasterStatus() {return rasterStatusText;}
    public String getRasterStatusText() {return rasterStatusText.get();}
    public void setRasterStatusText(String statusText) {this.rasterStatusText.set(statusText);}
}

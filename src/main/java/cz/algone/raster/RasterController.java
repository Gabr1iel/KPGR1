package cz.algone.raster;

import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithmController.IAlgorithmController;
import cz.algone.algorithmController.shape.ShapeController;
import cz.algone.algorithmController.scene.SceneModelController;
import cz.algone.model.SceneModel;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;

public class RasterController {
    @FXML
    private Canvas canvas;
    @FXML
    private StackPane stackPane;
    private RasterCanvas raster;
    private IAlgorithmController algorithmController;

    private final SceneModel sceneModel = new SceneModel();
    private SceneModelController sceneModelController;

    @FXML
    private void initialize() {
        raster = new RasterCanvas(canvas);
        sceneModelController = new SceneModelController(raster, sceneModel);
        //Velikost rasteru se určí podle velikosti StackPane
        canvas.widthProperty().bind(stackPane.widthProperty());
        canvas.heightProperty().bind(stackPane.heightProperty());
        //Přepočítání rasteru při změně velikosti
        canvas.widthProperty().addListener((obs, oldValue, newValue) -> resizeRaster());
        canvas.heightProperty().addListener((obs, oldValue, newValue) -> resizeRaster());
    }

    //Přepínání algoritmů, nahrazení konstruktorů s parametry metodou setup()
    public void setAlgorithmController(IAlgorithmController algorithmController, IAlgorithm algorithm) {
        raster.clearListeners();
        algorithm.setup(raster);
        algorithmController.setup(raster, algorithm, sceneModelController);
        algorithmController.initListeners();
        this.algorithmController = algorithmController;
    }

    public void resizeRaster() {
        if (raster == null) return;

        raster.resize((int) canvas.getWidth(), (int) canvas.getHeight());
        if (algorithmController instanceof ShapeController<?> shapeController)
            shapeController.drawScene();
    }

    public SceneModelController getSceneModelController() {
        return sceneModelController;
    }
}

package cz.algone.raster;

import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithmController.IAlgorithmController;
import cz.algone.algorithmController.controller3D.Controller3D;
import cz.algone.algorithmController.shape.ShapeController;
import cz.algone.algorithmController.scene.SceneModelController;
import cz.algone.model.SceneModel;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class RasterController {
    @FXML private Canvas canvas;
    @FXML private StackPane stackPane;
    @FXML private Label statusLabel;

    private RasterCanvas raster;
    private boolean resizePending = false;
    private IAlgorithmController algorithmController;

    private final SceneModel sceneModel = new SceneModel();
    private SceneModelController sceneModelController;

    @FXML
    private void initialize() {
        raster = new RasterCanvas(canvas);
        stackPane.setMinSize(0, 0);
        sceneModelController = new SceneModelController(raster, sceneModel);
        statusLabel.textProperty().bind(sceneModelController.getRasterStatus());
        //Velikost rasteru se určí podle velikosti StackPane
        canvas.widthProperty().bind(stackPane.widthProperty());
        canvas.heightProperty().bind(stackPane.heightProperty());
        //Přepočítání rasteru při změně velikosti
        canvas.widthProperty().addListener((obs, oldValue, newValue) -> resizeRaster());
        canvas.heightProperty().addListener((obs, oldValue, newValue) -> resizeRaster());
    }

    /** Nastaví {@link IAlgorithmController} a {@link IAlgorithm} rasteru,
     *  zároveň jim předává i raster a sceneModel */
    public void setAlgorithmController(IAlgorithmController algorithmController, IAlgorithm algorithm) {
        raster.clearListeners();
        algorithm.setup(raster);
        algorithmController.setup(raster, algorithm, sceneModelController);
        algorithmController.initListeners();
        this.algorithmController = algorithmController;
    }

    public void resizeRaster() {
        if (raster == null || resizePending) return;
        resizePending = true;

        javafx.application.Platform.runLater(() -> {
            resizePending = false;

            int w = (int) Math.floor(canvas.getWidth());
            int h = (int) Math.floor(canvas.getHeight());
            if (w < 2 || h < 2) return;

            raster.resize(w, h);

            if (algorithmController instanceof ShapeController shapeController)
                shapeController.drawScene();
            else if (algorithmController instanceof Controller3D controller3D)
                controller3D.create3DSpace();
        });
    }

    public void showRasterLabel(boolean show) {
        statusLabel.setVisible(show);
        statusLabel.setManaged(show);
    }

    public SceneModelController getSceneModelController() {
        return sceneModelController;
    }
}

package cz.algone.raster;

import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithmController.IAlgorithmController;
import cz.algone.algorithmController.shape.ShapeController;
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

    @FXML
    private void initialize() {
        raster = new RasterCanvas(canvas);
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
        algorithmController.setup(raster, algorithm);
        algorithmController.initListeners();
        this.algorithmController = algorithmController;
    }

    public void resizeRaster() {
        if (raster == null) return;

        raster.resize((int) canvas.getWidth(), (int) canvas.getHeight());
        if (algorithmController instanceof ShapeController<?> shapeController)
            shapeController.drawScene();
    }
}

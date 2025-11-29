package cz.algone.raster;

import cz.algone.app.RasterizeController;
import cz.algone.rasterizer.Rasterizer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;

public class RasterController {
    @FXML
    private Canvas canvas;
    @FXML
    private StackPane stackPane;
    private RasterCanvas raster;
    private RasterizeController rasterizeController;

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
    public void setAlgorithmController(RasterizeController rasterizeController, Rasterizer rasterizer) {
        rasterizer.setup(raster);
        rasterizeController.setup(raster, rasterizer);
        rasterizeController.initListeners();
        this.rasterizeController = rasterizeController;
    }

    public void resizeRaster() {
        if (raster == null) return;

        raster.resize((int) canvas.getWidth(), (int) canvas.getHeight());
        rasterizeController.drawScene();
    }
}

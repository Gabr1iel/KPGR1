package cz.algone.raster;

import cz.algone.app.RasterizeController;
import cz.algone.rasterizer.Rasterizer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;

public class RasterController {
    @FXML
    private Canvas canvas;
    private RasterCanvas raster;

    @FXML
    private void initialize() {
        raster = new RasterCanvas(canvas);
    }

    //Přepínání algoritmů, nahrazení konstruktorů s parametry metodou setup()
    public void setAlgorithmController(RasterizeController rasterizeController, Rasterizer rasterizer) {
        rasterizer.setup(raster);
        rasterizeController.setup(raster, rasterizer);
        rasterizeController.initListeners();
    }
}

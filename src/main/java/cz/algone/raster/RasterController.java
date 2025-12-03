package cz.algone.raster;

import cz.algone.app.ShapeController;
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
    private ShapeController shapeController;

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
    public void setAlgorithmController(ShapeController shapeController, Rasterizer rasterizer) {
        rasterizer.setup(raster);
        shapeController.setup(raster, rasterizer);
        shapeController.initListeners();
        this.shapeController = shapeController;
    }

    public void resizeRaster() {
        if (raster == null) return;

        raster.resize((int) canvas.getWidth(), (int) canvas.getHeight());
        shapeController.drawScene();
    }
}

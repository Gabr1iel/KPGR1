package cz.algone.controller;

import cz.algone.raster.RasterController;
import cz.algone.rasterize.LineRasterizerBresenham;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

//Hlavní controller, zajištění přepínání algoritmů
public class UIController {
    @FXML
    private StackPane raster;
    @FXML
    private RasterController rasterController;

    private LineRasterizeController lineRasterizeController = new LineRasterizeController();
    private LineRasterizerBresenham bresenham = new LineRasterizerBresenham();

    @FXML
    private void initialize() {
        rasterController.setAlgorithmController(lineRasterizeController, bresenham);
    }
}

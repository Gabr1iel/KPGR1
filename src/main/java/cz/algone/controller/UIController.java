package cz.algone.controller;

import cz.algone.polygon.PolygonRasterizerController;
import cz.algone.raster.RasterController;
import cz.algone.rasterize.RasterizerCollection;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

//Hlavní controller, zajištění přepínání algoritmů
public class UIController {
    @FXML
    private StackPane raster;
    @FXML
    private RasterController rasterController;

    private final LineRasterizeController lineRasterizeController = new LineRasterizeController();
    private final PolygonRasterizerController polygonRasterizerController = new PolygonRasterizerController();
    private final RasterizerCollection rasterizerCollection = new RasterizerCollection();

    @FXML
    private void initialize() {
        rasterController.setAlgorithmController(polygonRasterizerController, rasterizerCollection.lineRasterizerBresenham);
    }
}

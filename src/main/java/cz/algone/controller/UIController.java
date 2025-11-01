package cz.algone.controller;

import cz.algone.line.LineRasterizeController;
import cz.algone.polygon.PolygonRasterizeController;
import cz.algone.raster.RasterController;
import cz.algone.rasterize.RasterizerCollection;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

//Hlavní controller, zajištění přepínání algoritmů
public class UIController {
    @FXML
    private BorderPane root;
    @FXML
    private StackPane raster;
    @FXML
    private RasterController rasterController;

    private RasterizeController currentRasterizeController;
    private final LineRasterizeController lineRasterizeController = new LineRasterizeController();
    private final PolygonRasterizeController polygonRasterizeController = new PolygonRasterizeController();
    private final RasterizerCollection rasterizerCollection = new RasterizerCollection();

    @FXML
    private void initialize() {
        currentRasterizeController = lineRasterizeController;
        rasterController.setAlgorithmController(currentRasterizeController, rasterizerCollection.lineRasterizerBresenham);
        root.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case C -> currentRasterizeController.clearRaster();
            }
        });
    }
}

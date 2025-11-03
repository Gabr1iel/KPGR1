package cz.algone.controller;

import cz.algone.line.Line;
import cz.algone.line.LineRasterizeController;
import cz.algone.polygon.PolygonRasterizeController;
import cz.algone.raster.RasterController;
import cz.algone.rasterize.Rasterizer;
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
    private Rasterizer<Line> currentRasterizer;
    private final LineRasterizeController lineRasterizeController = new LineRasterizeController();
    private final PolygonRasterizeController polygonRasterizeController = new PolygonRasterizeController();
    private final RasterizerCollection rasterizerCollection = new RasterizerCollection();

    @FXML
    private void initialize() {
        currentRasterizeController = polygonRasterizeController;
        currentRasterizer = rasterizerCollection.lineRasterizerBresenham;
        rasterController.setAlgorithmController(currentRasterizeController, currentRasterizer);
        root.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case C -> currentRasterizeController.clearRaster();
            }
        });
    }
}

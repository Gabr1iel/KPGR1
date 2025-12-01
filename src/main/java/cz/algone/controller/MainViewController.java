package cz.algone.controller;

import cz.algone.app.RasterizeController;
import cz.algone.app.line.LineRasterizeController;
import cz.algone.app.polygon.PolygonRasterizeController;
import cz.algone.raster.RasterController;
import cz.algone.rasterizer.RasterizerAlias;
import cz.algone.rasterizer.Rasterizer;
import cz.algone.rasterizer.RasterizerCollection;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

//Hlavní controller, zajištění přepínání algoritmů
public class MainViewController {
    @FXML
    private BorderPane root;
    @FXML
    private RasterController rasterController;
    @FXML
    private SidebarController sidebarPaneController;
    @FXML
    private ToolbarController toolbarPaneController;

    private RasterizeController currentRasterizeController;
    private Rasterizer currentRasterizer;

    private final LineRasterizeController lineRasterizeController = new LineRasterizeController();
    private final PolygonRasterizeController polygonRasterizeController = new PolygonRasterizeController();
    private final RasterizerCollection rasterizerCollection = new RasterizerCollection();

    @FXML
    private void initialize() {
        currentRasterizeController = polygonRasterizeController;
        currentRasterizer = rasterizerCollection.lineRasterizerBresenham;
        rasterController.setAlgorithmController(currentRasterizeController, currentRasterizer);

        //Získání eunum pro nastavení rasterizéru ze SidebarControlleru
        sidebarPaneController.setOnRasterizerChange(this::setRasterizer);
        toolbarPaneController.setOnColorChanged((colorPair) -> currentRasterizeController.setColors(colorPair));

        root.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case C -> currentRasterizeController.clearRaster();
            }
        });
    }

    private void setRasterizer(RasterizerAlias alias) {
        currentRasterizeController = lineRasterizeController;
        if (alias == RasterizerAlias.POLYGON)
            currentRasterizeController = polygonRasterizeController;

        currentRasterizer = rasterizerCollection.rasterizerMap.get(alias);
        rasterController.setAlgorithmController(currentRasterizeController, currentRasterizer);
    }
}

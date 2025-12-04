package cz.algone.ui.main;

import cz.algone.app.ShapeAlias;
import cz.algone.app.ShapeCollection;
import cz.algone.app.ShapeController;
import cz.algone.raster.RasterController;
import cz.algone.rasterizer.RasterizerAlias;
import cz.algone.rasterizer.Rasterizer;
import cz.algone.rasterizer.RasterizerCollection;
import cz.algone.ui.sidebar.SidebarController;
import cz.algone.ui.toolbar.ToolbarController;
import cz.algone.util.color.ColorPair;
import cz.algone.util.color.ColorUtils;
import cz.algone.util.map.HashMapUtils;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

//Hlavní controller, zajištění přepínání algoritmů
public class MainViewController {
    @FXML private BorderPane root;
    @FXML private RasterController rasterController;
    @FXML private SidebarController sidebarPaneController;
    @FXML private ToolbarController toolbarPaneController;

    private ShapeController currentShapeController;
    private Rasterizer currentRasterizer;
    private ColorPair currentColor = ColorUtils.DEFAULT_COLORPICKER_COLOR;

    private final ShapeCollection shapeCollection = new ShapeCollection();
    private final RasterizerCollection rasterizerCollection = new RasterizerCollection();

    @FXML
    private void initialize() {
        initRaster();

        //Získání eunum pro nastavení rasterizéru ze SidebarControlleru
        sidebarPaneController.setOnRasterizerChange(this::setRasterizer);
        toolbarPaneController.setOnShapeChanged(this::setShapeController);
        toolbarPaneController.setOnColorChanged((colorPair) -> {
            currentColor = colorPair;
            currentShapeController.setColors(currentColor);
        });

        root.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case C -> {
                    if (e.isShiftDown())
                        toolbarPaneController.resetPalette();
                    else
                        currentShapeController.clearRaster();
                }
            }
        });
    }

    private void setShapeController(ShapeAlias alias) {
        currentShapeController = shapeCollection.shapeMap.get(alias);
        currentShapeController.setColors(currentColor);
        sidebarPaneController.showOptionsFor(alias);
        currentRasterizer = rasterizerCollection.rasterizerMap.get(RasterizerAlias.valueOf(alias.name()));
        sidebarPaneController.setSelectedRasterizer(HashMapUtils.getKeyByValue(rasterizerCollection.rasterizerMap, currentRasterizer));
        rasterController.setAlgorithmController(currentShapeController, currentRasterizer);
    }

    private void setRasterizer(RasterizerAlias alias) {
        currentRasterizer = rasterizerCollection.rasterizerMap.get(alias);
        rasterController.setAlgorithmController(currentShapeController, currentRasterizer);
    }

    private void initRaster() {
        currentShapeController = shapeCollection.lineShapeController;
        currentShapeController.setColors(ColorUtils.DEFAULT_COLORPICKER_COLOR);
        currentRasterizer = rasterizerCollection.lineRasterizerBresenham;
        sidebarPaneController.showOptionsFor(ShapeAlias.LINE);
        rasterController.setAlgorithmController(currentShapeController, currentRasterizer);
    }
}

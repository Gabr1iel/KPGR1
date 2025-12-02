package cz.algone.ui.main;

import cz.algone.app.RasterizeController;
import cz.algone.app.line.LineRasterizeController;
import cz.algone.app.polygon.PolygonRasterizeController;
import cz.algone.raster.RasterController;
import cz.algone.rasterizer.RasterizerAlias;
import cz.algone.rasterizer.Rasterizer;
import cz.algone.rasterizer.RasterizerCollection;
import cz.algone.ui.colorPalette.ColorPaletteController;
import cz.algone.ui.sidebar.SidebarController;
import cz.algone.ui.toolbar.ToolbarController;
import cz.algone.util.color.ColorPair;
import cz.algone.util.color.ColorUtils;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

//Hlavní controller, zajištění přepínání algoritmů
public class MainViewController {
    @FXML private BorderPane root;
    @FXML private RasterController rasterController;
    @FXML private SidebarController sidebarPaneController;
    @FXML private ToolbarController toolbarPaneController;

    private RasterizeController currentRasterizeController;
    private Rasterizer currentRasterizer;
    private ColorPair currentColor = ColorUtils.DEFAULT_COLORPICKER_COLOR;

    private final LineRasterizeController lineRasterizeController = new LineRasterizeController();
    private final PolygonRasterizeController polygonRasterizeController = new PolygonRasterizeController();
    private final RasterizerCollection rasterizerCollection = new RasterizerCollection();

    @FXML
    private void initialize() {
        setupRaster();

        //Získání eunum pro nastavení rasterizéru ze SidebarControlleru
        sidebarPaneController.setOnRasterizerChange(this::setRasterizer);
        toolbarPaneController.setOnColorChanged((colorPair) -> {
            currentColor = colorPair;
            currentRasterizeController.setColors(currentColor);
        });

        root.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case C -> {
                    if (e.isShiftDown())
                        toolbarPaneController.resetPalette();
                    else
                        currentRasterizeController.clearRaster();
                }
            }
        });
    }

    private void setRasterizer(RasterizerAlias alias) {
        currentRasterizeController = lineRasterizeController;
        if (alias == RasterizerAlias.POLYGON)
            currentRasterizeController = polygonRasterizeController;

        currentRasterizeController.setColors(currentColor);
        currentRasterizer = rasterizerCollection.rasterizerMap.get(alias);

        rasterController.setAlgorithmController(currentRasterizeController, currentRasterizer);
    }

    private void setupRaster() {
        currentRasterizeController = polygonRasterizeController;
        currentRasterizeController.setColors(ColorUtils.DEFAULT_COLORPICKER_COLOR);
        currentRasterizer = rasterizerCollection.polygonRasterizer;
        rasterController.setAlgorithmController(currentRasterizeController, currentRasterizer);
    }
}

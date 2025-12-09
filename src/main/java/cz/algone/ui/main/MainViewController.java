package cz.algone.ui.main;

import cz.algone.algorithm.AlgorithmAlias;
import cz.algone.algorithm.AlgorithmCollection;
import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithmController.AlgorithmControllerAlias;
import cz.algone.algorithmController.AlgorithmControllerCollection;
import cz.algone.algorithmController.IAlgorithmController;
import cz.algone.algorithmController.shape.ShapeController;
import cz.algone.raster.RasterController;
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

    private IAlgorithmController currentAlgorithmController;
    private IAlgorithm currentAlgorithm;
    private ColorPair currentColor = ColorUtils.DEFAULT_COLORPICKER_COLOR;

    private final AlgorithmControllerCollection algorithmControllerCollection = new AlgorithmControllerCollection();
    private final AlgorithmCollection algorithmCollection = new AlgorithmCollection();

    @FXML
    private void initialize() {
        initRaster();

        //Získání eunum pro nastavení rasterizéru ze SidebarControlleru
        sidebarPaneController.setOnRasterizerChange(this::setAlgorithm);
        toolbarPaneController.setOnShapeChanged(this::setAlgorithmController);
        toolbarPaneController.setOnToolsChanged(this::setAlgorithmController);
        toolbarPaneController.setOnColorChanged((colorPair) -> {
            currentColor = colorPair;
            currentAlgorithmController.setColors(currentColor);
        });

        root.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case C -> {
                    if (e.isShiftDown())
                        toolbarPaneController.resetPalette();
                    else
                        if (currentAlgorithmController instanceof ShapeController<?> shapeController)
                            shapeController.clearRaster();
                }
            }
        });
    }

    private void setAlgorithmController(AlgorithmControllerAlias alias) {
        currentAlgorithmController = algorithmControllerCollection.algorithmControllerMap.get(alias);
        currentAlgorithmController.setColors(currentColor);
        currentAlgorithm = algorithmCollection.algorithmMap.get(currentAlgorithmController.getDefaultAlgorithm());

        updateUIComponents(alias);

        rasterController.setAlgorithmController(currentAlgorithmController, currentAlgorithm);
    }

    private void setAlgorithm(AlgorithmAlias alias) {
        currentAlgorithm = algorithmCollection.algorithmMap.get(alias);
        rasterController.setAlgorithmController(currentAlgorithmController, currentAlgorithm);
    }

    private void initRaster() {
        currentAlgorithmController = algorithmControllerCollection.lineShapeController;
        currentAlgorithmController.setColors(ColorUtils.DEFAULT_COLORPICKER_COLOR);
        currentAlgorithm = algorithmCollection.lineRasterizerBresenham;
        sidebarPaneController.showOptionsFor(AlgorithmControllerAlias.LINE);

        rasterController.setAlgorithmController(currentAlgorithmController, currentAlgorithm);
    }

    private void updateUIComponents(AlgorithmControllerAlias alias) {
        toolbarPaneController.setSelectedButton(alias);
        sidebarPaneController.showOptionsFor(alias);
        sidebarPaneController.setSelectedRasterizer(HashMapUtils.getKeyByValue(algorithmCollection.algorithmMap, currentAlgorithm));
    }
}

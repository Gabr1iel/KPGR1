package cz.algone.ui.main;

import cz.algone.algorithm.AlgorithmAlias;
import cz.algone.algorithm.AlgorithmCollection;
import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithm.fill.IFill;
import cz.algone.algorithm.fill.pattern.IPattern;
import cz.algone.algorithm.fill.pattern.PatternCollection;
import cz.algone.algorithmController.AlgorithmControllerAlias;
import cz.algone.algorithmController.AlgorithmControllerCollection;
import cz.algone.algorithmController.IAlgorithmController;
import cz.algone.algorithmController.scene.SceneModelController;
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
    private IPattern currentPattern  = null;
    private SceneModelController sceneModelController;

    private final AlgorithmControllerCollection algorithmControllerCollection = new AlgorithmControllerCollection();
    private final AlgorithmCollection algorithmCollection = new AlgorithmCollection();
    private final PatternCollection patternCollection = new PatternCollection();

    @FXML
    private void initialize() {
        initRaster();
        sceneModelController = rasterController.getSceneModelController();

        //Získání eunum pro nastavení rasterizéru ze SidebarControlleru
        sidebarPaneController.setOnRasterizerChange(this::setAlgorithm);
        sidebarPaneController.setOnPatternChanged(patternAlias -> {
            currentPattern = patternCollection.patternMap.get(patternAlias);
            setPattern(currentPattern);
        });
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
                        sceneModelController.clearRaster();
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

    private void setPattern(IPattern pattern) {
        System.out.println(pattern);
        if (currentAlgorithm instanceof IFill) {
            ((IFill) currentAlgorithm).setPattern(pattern);
        }
    }

    private void initRaster() {
        currentAlgorithmController = algorithmControllerCollection.lineShapeController;
        currentAlgorithmController.setColors(ColorUtils.DEFAULT_COLORPICKER_COLOR);
        currentAlgorithm = algorithmCollection.lineRasterizerBresenham;

        updateUIComponents(HashMapUtils.getKeyByValue(algorithmControllerCollection.algorithmControllerMap, currentAlgorithmController));

        rasterController.setAlgorithmController(currentAlgorithmController, currentAlgorithm);
    }

    private void updateUIComponents(AlgorithmControllerAlias alias) {
        toolbarPaneController.setSelectedButton(alias);
        sidebarPaneController.showOptionsFor(alias);
        sidebarPaneController.setSelectedRasterizer(HashMapUtils.getKeyByValue(algorithmCollection.algorithmMap, currentAlgorithm));
    }
}

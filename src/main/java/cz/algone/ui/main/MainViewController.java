package cz.algone.ui.main;

import cz.algone.common.enumAlias.*;
import cz.algone.algorithm.AlgorithmCollection;
import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithm.fill.IFill;
import cz.algone.algorithm.fill.pattern.IPattern;
import cz.algone.algorithm.fill.pattern.PatternCollection;
import cz.algone.algorithmController.AlgorithmControllerCollection;
import cz.algone.algorithmController.IAlgorithmController;
import cz.algone.algorithmController.clip.ClipPolygonController;
import cz.algone.algorithmController.controller3D.Controller3D;
import cz.algone.algorithmController.scene.SceneModelController;
import cz.algone.raster.RasterController;
import cz.algone.ui.sidebar.SidebarController;
import cz.algone.ui.toolbar.ToolbarController;
import cz.algone.util.color.ColorPair;
import cz.algone.util.color.ColorUtils;
import cz.algone.util.keyControll.KeyControllable;
import cz.algone.util.map.HashMapUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

//Hlavní controller, zajištění přepínání algoritmů
public class MainViewController {
    @FXML private BorderPane root;
    @FXML private RasterController rasterController;
    @FXML private SidebarController sidebarPaneController;
    @FXML private ToolbarController toolbarPaneController;

    private IAlgorithmController currentAlgorithmController;
    private AlgorithmControllerAlias currentAlgorithmControllerAlias;
    private IAlgorithm currentAlgorithm;
    private ColorPair currentColor = ColorUtils.DEFAULT_COLORPICKER_COLOR;
    private SceneAlias currentScene = SceneAlias.SCENE_2D;
    private IPattern currentPattern  = null;
    private SceneModelController sceneModelController;

    private final AlgorithmCollection algorithmCollection = new AlgorithmCollection();
    private final AlgorithmControllerCollection algorithmControllerCollection = new AlgorithmControllerCollection();
    private final PatternCollection patternCollection = new PatternCollection();

    @FXML
    private void initialize() {
        sceneModelController = rasterController.getSceneModelController();

        //Získání eunum pro nastavení rasterizéru ze SidebarControlleru
        sidebarPaneController.setOnRasterizerChange(this::setAlgorithm);
        sidebarPaneController.setOnPolygonOrientationChanged(this::setClipOrientation);
        sidebarPaneController.setOnSceneChanged(this::setCurrentScene);
        sidebarPaneController.setOnClip3DChanged(this::setClip3D);
        sidebarPaneController.setOnAnimationChanged(this::setAnimation);
        sidebarPaneController.setOnProjMatChanged(this::setProjMat);
        sidebarPaneController.setOnCubicChanged(this::setCubic);
        sidebarPaneController.setOnPatternChanged(patternAlias -> {
            currentPattern = patternCollection.patternMap.get(patternAlias);
            setPattern(currentPattern);
        });
        toolbarPaneController.setOnShapeChanged(this::setAlgorithmController);
        toolbarPaneController.setOnToolsChanged(this::setAlgorithmController);
        toolbarPaneController.setOnSolidsChanged(event -> {
            if (currentAlgorithmController instanceof Controller3D controller3D) {
                controller3D.addSolid(event);
            }
        });
        toolbarPaneController.setOnColorChanged((colorPair) -> {
            currentColor = colorPair;
            currentAlgorithmController.setColors(currentColor);
        });

        Platform.runLater(() -> {
            root.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
                if (e.getCode() == KeyCode.C) {
                    if (e.isShiftDown()) {
                        toolbarPaneController.resetPalette();
                    }
                    else {
                        sceneModelController.clearRasterAndScene();
                        if (currentAlgorithmController instanceof Controller3D controller3D) {
                            reset3DController(controller3D);
                            controller3D.create3DSpace();
                            controller3D.renderScene();
                        }
                    }
                    e.consume();
                    return;
                }
                if (currentAlgorithmController instanceof KeyControllable kc) {
                    kc.onKeyPressed(e);
                }
            });
        });
        initRaster();
    }
    /** Přijímá {@link AlgorithmControllerAlias} a následně získá daný controller z
     * {@link AlgorithmControllerCollection}, poté získá default algorithm pro daný
     * controller a oboje uloží do current proměných*/
    private void setAlgorithmController(AlgorithmControllerAlias alias) {
        currentAlgorithmController = algorithmControllerCollection.algorithmControllerMap.get(alias);
        currentAlgorithmControllerAlias = alias;
        currentAlgorithmController.setColors(currentColor);
        currentAlgorithm = algorithmCollection.algorithmMap.get(currentAlgorithmController.getDefaultAlgorithm());

        updateUIComponents(alias);

        rasterController.setAlgorithmController(alias, currentAlgorithmController, currentAlgorithm);
    }
    /** Pomocí {@link AlgorithmAlias} získá konkrétní algoritmus a uloží*/
    private void setAlgorithm(AlgorithmAlias alias) {
        currentAlgorithm = algorithmCollection.algorithmMap.get(alias);
        rasterController.setAlgorithmController(currentAlgorithmControllerAlias, currentAlgorithmController, currentAlgorithm);
    }
    /** Pokud currentAlgorithm -> IFill, nastaví pattern */
    private void setPattern(IPattern pattern) {
        if (currentAlgorithm instanceof IFill) {
            ((IFill) currentAlgorithm).setPattern(pattern);
        }
    }

    private void setClipOrientation(PolygonOrientation orientation) {
        if (currentAlgorithmController instanceof ClipPolygonController)
            ((ClipPolygonController) currentAlgorithmController).setOrientationMode(orientation);
    }
    private void setCurrentScene(SceneAlias alias) {
        this.currentScene = alias;
        toolbarPaneController.showOptionsFor(currentScene);
        switchDimension();
    }
    private void setClip3D(EnabledAlias alias) {
        if (currentAlgorithmController instanceof Controller3D)
            ((Controller3D) currentAlgorithmController).setEnabledClip(alias);
    }
    private void setAnimation(EnabledAlias alias) {
        if (currentAlgorithmController instanceof Controller3D)
            ((Controller3D) currentAlgorithmController).setAnimation(alias);
    }
    private void setProjMat(ProjMatAlias alias) {
        if (currentAlgorithmController instanceof Controller3D)
            ((Controller3D) currentAlgorithmController).setProjMat(alias);
    }
    private void setCubic(CubicAlias alias) {
        if (currentAlgorithmController instanceof Controller3D)
            ((Controller3D) currentAlgorithmController).setCubic(alias);
    }
    /** Nastaví základní hodnoty rasteru */
    private void initRaster() {
        currentAlgorithmController = algorithmControllerCollection.lineShapeController;
        currentAlgorithmControllerAlias = HashMapUtils.getKeyByValue(algorithmControllerCollection.algorithmControllerMap, currentAlgorithmController);
        currentAlgorithmController.setColors(ColorUtils.DEFAULT_COLORPICKER_COLOR);
        currentAlgorithm = algorithmCollection.lineRasterizerBresenham;

        updateUIComponents(currentAlgorithmControllerAlias);

        rasterController.setAlgorithmController(currentAlgorithmControllerAlias, currentAlgorithmController, currentAlgorithm);
    }
    /** Updatuje UI aby reagovalo správně na změny {@link IAlgorithmController} */
    private void updateUIComponents(AlgorithmControllerAlias alias) {
        toolbarPaneController.setSelectedButton(alias);
        sidebarPaneController.showSidebarSections(alias, HashMapUtils.getKeyByValue(algorithmCollection.algorithmMap, currentAlgorithm));
        sidebarPaneController.setSelectedRasterizer(HashMapUtils.getKeyByValue(algorithmCollection.algorithmMap, currentAlgorithm));
        sidebarPaneController.setSelectedScene(currentScene);
    }
    /** Resetuje nastavení {@link Controller3D} a updatuje UI */
    private void reset3DController(Controller3D controller) {
        controller.clear();
        sidebarPaneController.reset3DSettings();
        toolbarPaneController.resetSolids();
    }
    /** Přepíná mezi 2D a 3D scénou pomocí {@link SceneAlias} */
    private void switchDimension() {
        if (currentAlgorithmController instanceof Controller3D controller) {
            reset3DController(controller);
        }
        sceneModelController.clearRasterAndScene();
        if (currentScene == SceneAlias.SCENE_2D) {
            root.setStyle("-fx-background-color: #e9eef5;");
            initRaster();
        } else if (currentScene == SceneAlias.SCENE_3D) {
            root.setStyle("-fx-background-color: #000000;");
            setAlgorithmController(AlgorithmControllerAlias.CONTROLLER_3D);
        }
        rasterController.showRasterLabel(currentScene == SceneAlias.SCENE_3D);
    }
}

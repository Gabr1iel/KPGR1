package cz.algone.ui.main;

import cz.algone.common.enumAlias.*;
import cz.algone.algorithm.AlgorithmCollection;
import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithm.fill.IFill;
import cz.algone.algorithm.fill.pattern.PatternCollection;
import cz.algone.algorithmController.AlgorithmControllerCollection;
import cz.algone.algorithmController.IAlgorithmController;
import cz.algone.algorithmController.clip.ClipPolygonController;
import cz.algone.algorithmController.controller3D.Controller3D;
import cz.algone.algorithmController.scene.SceneContext;
import cz.algone.raster.RasterController;
import cz.algone.ui.sidebar.SidebarController;
import cz.algone.ui.toolbar.ToolbarController;
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
    private SceneContext sceneContext;

    private final AlgorithmCollection algorithmCollection = new AlgorithmCollection();
    private final AlgorithmControllerCollection algorithmControllerCollection = new AlgorithmControllerCollection();
    private final PatternCollection patternCollection = new PatternCollection();

    @FXML
    private void initialize() {
        sceneContext = new SceneContext();
        rasterController.initSceneContext(sceneContext);
        sidebarPaneController.initSceneContext(sceneContext);
        toolbarPaneController.initSceneContext(sceneContext);

        // === Property listenery na SceneContext ===
        sceneContext.controllerAliasProperty().addListener((obs, old, newVal) -> {
            if (newVal != null) setAlgorithmController(newVal);
        });
        sceneContext.algorithmAliasProperty().addListener((obs, old, newVal) -> {
            if (newVal != null) setAlgorithm(newVal);
        });
        sceneContext.sceneProperty().addListener((obs, old, newVal) -> {
            if (newVal != null) switchDimension();
        });
        sceneContext.colorsProperty().addListener((obs, old, newVal) -> {
            if (newVal != null) currentAlgorithmController.setColors(newVal);
        });

        // === Property listenery druhé vrstvy (nastavení algoritmů) ===
        sceneContext.patternAliasProperty().addListener((obs, old, newVal) -> {
            if (currentAlgorithm instanceof IFill fill)
                fill.setPattern(newVal != null ? patternCollection.patternMap.get(newVal) : null);
        });
        sceneContext.polygonOrientationProperty().addListener((obs, old, newVal) -> {
            if (newVal != null && currentAlgorithmController instanceof ClipPolygonController clip)
                clip.setOrientationMode(newVal);
        });
        sceneContext.clip3DEnabledProperty().addListener((obs, old, newVal) -> {
            if (newVal != null && currentAlgorithmController instanceof Controller3D c3d)
                c3d.setEnabledClip(newVal);
        });
        sceneContext.animationEnabledProperty().addListener((obs, old, newVal) -> {
            if (newVal != null && currentAlgorithmController instanceof Controller3D c3d)
                c3d.setAnimation(newVal);
        });
        sceneContext.projMatProperty().addListener((obs, old, newVal) -> {
            if (newVal != null && currentAlgorithmController instanceof Controller3D c3d)
                c3d.setProjMat(newVal);
        });
        sceneContext.cubicAliasProperty().addListener((obs, old, newVal) -> {
            if (newVal != null && currentAlgorithmController instanceof Controller3D c3d)
                c3d.setCubic(newVal);
        });
        toolbarPaneController.setOnSolidsChanged(event -> {
            if (currentAlgorithmController instanceof Controller3D controller3D) {
                controller3D.addSolid(event);
            }
        });

        Platform.runLater(() -> {
            root.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
                if (e.getCode() == KeyCode.C) {
                    if (e.isShiftDown()) {
                        toolbarPaneController.resetPalette();
                    }
                    else {
                        sceneContext.clearRasterAndScene();
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
        sceneContext.setControllerAlias(AlgorithmControllerAlias.LINE);
    }
    /** Přijímá {@link AlgorithmControllerAlias} a následně získá daný controller z
     * {@link AlgorithmControllerCollection}, poté získá default algorithm pro daný
     * controller a oboje uloží do current proměných*/
    private void setAlgorithmController(AlgorithmControllerAlias alias) {
        currentAlgorithmController = algorithmControllerCollection.algorithmControllerMap.get(alias);
        currentAlgorithmControllerAlias = alias;
        currentAlgorithmController.setColors(sceneContext.getColors());
        currentAlgorithm = algorithmCollection.algorithmMap.get(currentAlgorithmController.getDefaultAlgorithm());

        updateUIComponents(alias);

        rasterController.setAlgorithmController(alias, currentAlgorithmController, currentAlgorithm);
    }
    /** Pomocí {@link AlgorithmAlias} získá konkrétní algoritmus a uloží*/
    private void setAlgorithm(AlgorithmAlias alias) {
        currentAlgorithm = algorithmCollection.algorithmMap.get(alias);
        rasterController.setAlgorithmController(currentAlgorithmControllerAlias, currentAlgorithmController, currentAlgorithm);
    }
    /** Updatuje UI aby reagovalo správně na změny {@link IAlgorithmController} */
    private void updateUIComponents(AlgorithmControllerAlias alias) {
        sidebarPaneController.showSidebarSections(alias, HashMapUtils.getKeyByValue(algorithmCollection.algorithmMap, currentAlgorithm));
        sidebarPaneController.setSelectedRasterizer(HashMapUtils.getKeyByValue(algorithmCollection.algorithmMap, currentAlgorithm));
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
        sceneContext.clearRasterAndScene();
        SceneAlias scene = sceneContext.getScene();
        if (scene == SceneAlias.SCENE_2D) {
            root.setStyle("-fx-background-color: #e9eef5;");
            sceneContext.setControllerAlias(AlgorithmControllerAlias.LINE);
        } else if (scene == SceneAlias.SCENE_3D) {
            root.setStyle("-fx-background-color: #000000;");
            sceneContext.setControllerAlias(AlgorithmControllerAlias.CONTROLLER_3D);
        }
        rasterController.showRasterLabel(scene == SceneAlias.SCENE_3D);
    }
}

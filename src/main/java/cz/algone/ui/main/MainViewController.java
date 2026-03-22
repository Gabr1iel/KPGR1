package cz.algone.ui.main;

import cz.algone.algorithmController.MainAlgorithmController;
import cz.algone.common.enumAlias.*;
import cz.algone.algorithmController.IAlgorithmController;
import cz.algone.algorithmController.controller3D.Controller3D;
import cz.algone.algorithmController.scene.SceneContext;
import cz.algone.raster.RasterController;
import cz.algone.ui.sidebar.SidebarController;
import cz.algone.ui.toolbar.ToolbarControllerMain;
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
    @FXML private ToolbarControllerMain toolbarPaneController;

    private MainAlgorithmController mainAlgorithmController;

    private IAlgorithmController currentAlgorithmController;
    private SceneContext sceneContext;

    @FXML
    private void initialize() {
        sceneContext = new SceneContext();
        mainAlgorithmController = new MainAlgorithmController(sceneContext);
        rasterController.initSceneContext(sceneContext);
        sidebarPaneController.initSceneContext(sceneContext);
        toolbarPaneController.initSceneContext(sceneContext);

        // === Property listenery na SceneContext ===
        sceneContext.sceneProperty().addListener((obs, old, newVal) -> {
            if (newVal != null) switchDimension();
        });
        sceneContext.controllerProperty().addListener((obs, old, newVal) -> {this.currentAlgorithmController = newVal;});
        toolbarPaneController.setOnSolidsChanged(event -> {
            if (currentAlgorithmController instanceof Controller3D controller3D)
                controller3D.addSolid(event);
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

package cz.algone.ui.main;

import cz.algone.algorithmController.MainAlgorithmController;
import cz.algone.common.enumAlias.*;
import cz.algone.algorithmController.controller3D.Controller3D;
import cz.algone.model.SceneContext;
import cz.algone.raster.RasterController;
import cz.algone.ui.sidebar.SidebarController;
import cz.algone.ui.toolbar.ToolbarController;
import cz.algone.util.keyControll.KeyControllable;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

/** Kořenový controller spojující UI komponenty se {@link SceneContext}em a vstupními událostmi. */
public class MainViewController {
    @FXML private BorderPane root;
    @FXML private RasterController rasterController;
    @FXML private SidebarController sidebarPaneController;
    @FXML private ToolbarController toolbarPaneController;

    private SceneContext sceneContext;

    @FXML
    private void initialize() {
        sceneContext = new SceneContext();
        new MainAlgorithmController(sceneContext);
        rasterController.initSceneContext(sceneContext);
        sidebarPaneController.initSceneContext(sceneContext);
        toolbarPaneController.initSceneContext(sceneContext);

        sceneContext.sceneProperty().addListener((obs, old, newVal) -> {
            if (newVal != null) switchDimension();
        });
        Platform.runLater(() -> {
            root.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
                if (e.getCode() == KeyCode.C) {
                    if (e.isShiftDown()) {
                        toolbarPaneController.resetPalette();
                    }
                    else {
                        sceneContext.clearRasterAndScene();
                        if (sceneContext.getCurrentAlgorithmController() instanceof Controller3D controller3D) {
                            reset3DController(controller3D);
                            controller3D.create3DSpace();
                            controller3D.renderScene();
                        }
                    }
                    e.consume();
                    return;
                }
                if (sceneContext.getCurrentAlgorithmController() instanceof KeyControllable kc) {
                    kc.onKeyPressed(e);
                }
            });
        });
        sceneContext.setControllerAlias(AlgorithmControllerAlias.LINE);
    }
    /** Resetuje stav {@link Controller3D}u a synchronizuje s ním UI. */
    private void reset3DController(Controller3D controller) {
        controller.clear();
        sidebarPaneController.reset3DSettings();
        toolbarPaneController.resetSolids();
    }
    /** Přepne UI a controller podle aktuálního {@link SceneAlias}u. */
    private void switchDimension() {
        if (sceneContext.getCurrentAlgorithmController() instanceof Controller3D controller) {
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
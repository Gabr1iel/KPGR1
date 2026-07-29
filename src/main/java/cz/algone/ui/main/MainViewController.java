package cz.algone.ui.main;

import cz.algone.algorithmController.MainAlgorithmController;
import cz.algone.algorithmController.controller3D.Controller3D;
import cz.algone.common.enumAlias.*;
import cz.algone.model.SceneContext;
import cz.algone.raster.RasterController;
import cz.algone.ui.panel.LeftPanelController;
import cz.algone.ui.panel.RightPanelController;
import cz.algone.ui.rail.RailController;
import cz.algone.ui.topbar.TopBarController;
import cz.algone.util.color.ColorUtils;
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
    @FXML private TopBarController topbarController;
    @FXML private RailController railController;
    @FXML private LeftPanelController leftPanelController;
    @FXML private RightPanelController rightPanelController;

    private SceneContext sceneContext;
    /** Prostor, ve kterém se naposledy kreslilo — Obecné scénu nemění. */
    private SceneAlias lastDrawingScene = SceneAlias.SCENE_2D;

    @FXML
    private void initialize() {
        sceneContext = new SceneContext();
        new MainAlgorithmController(sceneContext);

        rasterController.initSceneContext(sceneContext);
        topbarController.initSceneContext(sceneContext);
        railController.initSceneContext(sceneContext);
        leftPanelController.initSceneContext(sceneContext);
        rightPanelController.initSceneContext(sceneContext);

        topbarController.setOnToggleControls(rasterController::toggleControls);

        sceneContext.sceneProperty().addListener((obs, old, scene) -> {
            if (scene != null) switchWorkspace(scene);
        });
        sceneContext.categoryProperty().addListener((obs, old, category) -> {
            if (category != null) switchCategory(category);
        });

        Platform.runLater(this::registerKeyFilter);

        sceneContext.setCategory(CategoryAlias.firstImplemented(sceneContext.getScene()));
    }

    /** Přepne pracovní prostor a nabídne jeho výchozí kategorii.
     *  Scéna se vyčistí jen při skutečné změně dimenze — odbočka do Obecného ji zachová. */
    private void switchWorkspace(SceneAlias scene) {
        if (scene != SceneAlias.GENERAL) {
            if (scene != lastDrawingScene) {
                if (sceneContext.getCurrentAlgorithmController() instanceof Controller3D controller)
                    controller.clear();

                sceneContext.clearRasterAndScene();
                lastDrawingScene = scene;
            }
            rasterController.showRasterLabel(scene == SceneAlias.SCENE_3D);
        }
        sceneContext.setCategory(CategoryAlias.firstImplemented(scene));
    }

    /** Kategorie railu určuje, který {@link cz.algone.algorithmController.IAlgorithmController} je aktivní. */
    private void switchCategory(CategoryAlias category) {
        AlgorithmControllerAlias controller = category.getDefaultController();
        if (controller != null) sceneContext.setControllerAlias(controller);
    }

    private void registerKeyFilter() {
        root.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.C) {
                if (e.isShiftDown()) sceneContext.setColors(ColorUtils.DEFAULT_COLORPICKER_COLOR);
                else clearScene();
                e.consume();
                return;
            }
            if (sceneContext.getCurrentAlgorithmController() instanceof KeyControllable controllable)
                controllable.onKeyPressed(e);
        });
    }

    /** Vyčistí raster i scénu a 3D prostor postaví znovu. */
    private void clearScene() {
        sceneContext.clearRasterAndScene();
        if (sceneContext.getCurrentAlgorithmController() instanceof Controller3D controller) {
            controller.clear();
            controller.create3DSpace();
            controller.renderScene();
        }
    }
}

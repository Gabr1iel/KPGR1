package cz.algone.ui.toolbar;

import cz.algone.ui.MainUIController;
import cz.algone.common.enumAlias.AlgorithmControllerAlias;
import cz.algone.model.models3D.SolidToggleEvent;
import cz.algone.ui.toolbar.colorPalette.ColorPaletteControllerMain;
import cz.algone.ui.toolbar.shapes.ShapesController;
import cz.algone.ui.toolbar.solids.SolidsController;
import cz.algone.ui.toolbar.tools.ToolsController;
import cz.algone.common.enumAlias.SceneAlias;
import javafx.fxml.FXML;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ToolbarControllerMain extends MainUIController {
    @FXML private ColorPaletteControllerMain colorPaletteController;
    @FXML private ShapesController shapesController;
    @FXML private ToolsController toolsController;
    @FXML private SolidsController solidsController;
    @FXML private VBox tools;
    @FXML private VBox shapes;
    @FXML private VBox solids;

    private Consumer<SolidToggleEvent> onSolidsChanged;

    private final Map<AlgorithmControllerAlias, ToggleButton> shapesToggleBtnMap = new HashMap<>();
    private final Map<AlgorithmControllerAlias, ToggleButton> toolsToggleBtnMap = new HashMap<>();

    @FXML
    private void initialize() {
        solidsController.setOnToggle(solidToggleEvent -> onSolidsChanged.accept(solidToggleEvent));
        fillMap(shapesController.getToggleBtns(), shapesToggleBtnMap);
        fillMap(toolsController.getToggleBtns(), toolsToggleBtnMap);
        bindManagedProperties();
    }

    @Override
    protected void onSceneContextReady() {
        colorPaletteController.initSceneContext(sceneContext);
        shapesController.setOnShapeChange(alias -> sceneContext.setControllerAlias(alias));
        toolsController.setOnToolsChange(alias -> sceneContext.setControllerAlias(alias));

        sceneContext.sceneProperty().addListener((obs, old, newVal) -> showOptionsFor(newVal));
        sceneContext.controllerAliasProperty().addListener((obs, old, newVal) -> {
            if (newVal != null) setSelectedButton(newVal);
        });
    }
    /** Namapuje toggle btns z příslušné toggleGroup,
     * Key -> {@link AlgorithmControllerAlias},
     * Value -> ToggleButton*/
    private void fillMap(ToggleGroup group, Map<AlgorithmControllerAlias, ToggleButton> map) {
        for (Toggle toggle : group.getToggles()) {
            AlgorithmControllerAlias alias = AlgorithmControllerAlias.valueOf(toggle.getUserData().toString());
            map.put(alias, (ToggleButton) toggle);
        }
    }
    /** Přijímá {@link AlgorithmControllerAlias} a následně pro všechny mapované
     * toggleGroup přepíná selected Button, zajišťuje že pouze jeden button je
     * selected i když jsou v jiné ToggleGroup*/
    public void setSelectedButton(AlgorithmControllerAlias alias) {
        ToggleButton shapeBtn = shapesToggleBtnMap.get(alias);
        ToggleButton toolBtn = toolsToggleBtnMap.get(alias);

        shapesController.getToggleBtns().selectToggle(shapeBtn);
        toolsController.getToggleBtns().selectToggle(toolBtn);
    }
    /** Přepíná viditelné sekce podle {@link SceneAlias}*/
    public void showOptionsFor(SceneAlias alias) {
        tools.setVisible(alias == SceneAlias.SCENE_2D);
        shapes.setVisible(alias == SceneAlias.SCENE_2D);
        solids.setVisible(alias == SceneAlias.SCENE_3D);
    }

    public void resetPalette() {
        colorPaletteController.clearColorPicker();
    }

    public void resetSolids() {
        solidsController.unselectAllButtons();
    }

    private void bindManagedProperties() {
        tools.managedProperty().bind(tools.visibleProperty());
        shapes.managedProperty().bind(shapes.visibleProperty());
        solids.managedProperty().bind(solids.visibleProperty());
    }

    public void setOnSolidsChanged(Consumer<SolidToggleEvent> onSolidsChanged) {this.onSolidsChanged = onSolidsChanged;}
}

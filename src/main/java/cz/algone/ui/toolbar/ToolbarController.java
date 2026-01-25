package cz.algone.ui.toolbar;

import cz.algone.common.enumAlias.AlgorithmControllerAlias;
import cz.algone.model.models3D.SolidToggleEvent;
import cz.algone.ui.colorPalette.ColorPaletteController;
import cz.algone.ui.shapes.ShapesController;
import cz.algone.ui.solids.SolidsController;
import cz.algone.ui.tools.ToolsController;
import cz.algone.util.color.ColorPair;
import cz.algone.common.enumAlias.SceneAlias;
import javafx.fxml.FXML;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ToolbarController {
    @FXML private ColorPaletteController colorPaletteController;
    @FXML private ShapesController shapesController;
    @FXML private ToolsController toolsController;
    @FXML private SolidsController solidsController;
    @FXML private VBox tools;
    @FXML private VBox shapes;
    @FXML private VBox solids;

    private Consumer<ColorPair> onColorChanged;
    private Consumer<AlgorithmControllerAlias> onShapeChanged;
    private Consumer<AlgorithmControllerAlias> onToolsChanged;
    private Consumer<SolidToggleEvent> onSolidsChanged;

    private final Map<AlgorithmControllerAlias, ToggleButton> shapesToggleBtnMap = new HashMap<>();
    private final Map<AlgorithmControllerAlias, ToggleButton> toolsToggleBtnMap = new HashMap<>();

    @FXML
    private void initialize() {
        colorPaletteController.setOnColorChanged((colorPair) -> onColorChanged.accept(colorPair));
        shapesController.setOnShapeChange((algorithmControllerAlias) -> onShapeChanged.accept(algorithmControllerAlias));
        toolsController.setOnToolsChange((algorithmControllerAlias) -> onToolsChanged.accept(algorithmControllerAlias));
        solidsController.setOnToggle((solidToggleEvent) -> {
            onSolidsChanged.accept(solidToggleEvent);
        });
        fillMap(shapesController.getToggleBtns(), shapesToggleBtnMap);
        fillMap(toolsController.getToggleBtns(), toolsToggleBtnMap);
        bindManagedProperties();
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

    public void setOnColorChanged(Consumer<ColorPair> onColorChanged) {
        this.onColorChanged = onColorChanged;
    }
    public  void setOnShapeChanged(Consumer<AlgorithmControllerAlias> onShapeChange) {this.onShapeChanged = onShapeChange;}
    public void setOnToolsChanged(Consumer<AlgorithmControllerAlias> onToolsChanged) {this.onToolsChanged = onToolsChanged;}
    public void setOnSolidsChanged(Consumer<SolidToggleEvent> onSolidsChanged) {this.onSolidsChanged = onSolidsChanged;}
}

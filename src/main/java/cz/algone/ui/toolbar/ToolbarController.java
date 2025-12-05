package cz.algone.ui.toolbar;

import cz.algone.algorithmController.AlgorithmControllerAlias;
import cz.algone.ui.colorPalette.ColorPaletteController;
import cz.algone.ui.shapes.ShapesController;
import cz.algone.ui.tools.ToolsController;
import cz.algone.util.color.ColorPair;
import javafx.fxml.FXML;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ToolbarController {
    @FXML private ColorPaletteController colorPaletteController;
    @FXML private ShapesController shapesController;
    @FXML private ToolsController toolsController;

    private Consumer<ColorPair> onColorChanged;
    private Consumer<AlgorithmControllerAlias> onShapeChanged;
    private Consumer<AlgorithmControllerAlias> onToolsChanged;

    private final Map<AlgorithmControllerAlias, ToggleButton> shapesToggleBtnMap = new HashMap<>();
    private final Map<AlgorithmControllerAlias, ToggleButton> toolsToggleBtnMap = new HashMap<>();

    @FXML
    private void initialize() {
        colorPaletteController.setOnColorChanged((colorPair) -> {onColorChanged.accept(colorPair);});
        shapesController.setOnShapeChange((algorithmControllerAlias) -> {onShapeChanged.accept(algorithmControllerAlias);});
        toolsController.setOnToolsChange((algorithmControllerAlias) -> {onToolsChanged.accept(algorithmControllerAlias);});
        fillMap(shapesController.getToggleBtns(), shapesToggleBtnMap);
        fillMap(toolsController.getToggleBtns(), toolsToggleBtnMap);
    }

    private void fillMap(ToggleGroup group, Map<AlgorithmControllerAlias, ToggleButton> map) {
        for (Toggle toggle : group.getToggles()) {
            AlgorithmControllerAlias alias = AlgorithmControllerAlias.valueOf(toggle.getUserData().toString());
            map.put(alias, (ToggleButton) toggle);
        }
    }

    public void setSelectedButton(AlgorithmControllerAlias alias) {
        ToggleButton shapeBtn = shapesToggleBtnMap.get(alias);
        ToggleButton toolBtn = toolsToggleBtnMap.get(alias);

        shapesController.getToggleBtns().selectToggle(shapeBtn);
        toolsController.getToggleBtns().selectToggle(toolBtn);
    }

    public void resetPalette() {
        colorPaletteController.clearColorPicker();
    }

    public void setOnColorChanged(Consumer<ColorPair> onColorChanged) {
        this.onColorChanged = onColorChanged;
    }

    public  void setOnShapeChanged(Consumer<AlgorithmControllerAlias> onShapeChange) {this.onShapeChanged = onShapeChange;}

    public void setOnToolsChanged(Consumer<AlgorithmControllerAlias> onToolsChanged) {this.onToolsChanged = onToolsChanged;}
}

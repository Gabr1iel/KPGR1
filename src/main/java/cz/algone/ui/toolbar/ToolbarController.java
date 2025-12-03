package cz.algone.ui.toolbar;

import cz.algone.app.ShapeAlias;
import cz.algone.ui.colorPalette.ColorPaletteController;
import cz.algone.ui.shapes.ShapesController;
import cz.algone.util.color.ColorPair;
import javafx.fxml.FXML;

import java.util.function.Consumer;

public class ToolbarController {
    @FXML private ColorPaletteController colorPaletteController;
    @FXML private ShapesController shapesController;

    private Consumer<ColorPair> onColorChanged;
    private Consumer<ShapeAlias> onShapeChanged;

    @FXML
    private void initialize() {
        colorPaletteController.setOnColorChanged((colorPair) -> {onColorChanged.accept(colorPair);});
        shapesController.setOnShapeChange((shapeAlias) -> {onShapeChanged.accept(shapeAlias);});
    }

    public void resetPalette() {
        colorPaletteController.clearColorPicker();
    }

    public void setOnColorChanged(Consumer<ColorPair> onColorChanged) {
        this.onColorChanged = onColorChanged;
    }

    public  void setOnShapeChanged(Consumer<ShapeAlias> onShapeChange) {
        this.onShapeChanged = onShapeChange;
    }
}

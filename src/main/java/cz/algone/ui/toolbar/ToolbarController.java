package cz.algone.ui.toolbar;

import cz.algone.ui.colorPalette.ColorPaletteController;
import cz.algone.util.color.ColorPair;
import javafx.fxml.FXML;

import java.util.function.Consumer;

public class ToolbarController {
    @FXML private ColorPaletteController colorPaletteController;

    private Consumer<ColorPair> onColorChanged;

    @FXML
    private void initialize() {
        colorPaletteController.setOnColorChanged((colorPair) -> {onColorChanged.accept(colorPair);});
    }

    public void resetPalette() {
        colorPaletteController.clearColorPicker();
    }

    public void setOnColorChanged(Consumer<ColorPair> onColorChanged) {
        this.onColorChanged = onColorChanged;
    }
}

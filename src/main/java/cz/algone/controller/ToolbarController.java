package cz.algone.controller;

import cz.algone.util.color.ColorPair;
import javafx.fxml.FXML;

import java.util.function.Consumer;

public class ToolbarController {
    @FXML private ColorPaletteController colorPalletteController;

    private Consumer<ColorPair> onColorChanged;

    @FXML
    private void initialize() {
        colorPalletteController.setOnColorChanged((colorPair) -> {onColorChanged.accept(colorPair);});
    }

    public void setOnColorChanged(Consumer<ColorPair> onColorChanged) {
        this.onColorChanged = onColorChanged;
    }
}

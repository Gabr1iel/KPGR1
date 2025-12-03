package cz.algone.ui.shapes;

import cz.algone.app.ShapeAlias;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import java.util.function.Consumer;

public class ShapesController {
    @FXML private ToggleGroup toggleBtns;

    private Consumer<ShapeAlias> onShapeChange;

    @FXML
    public void initialize() {
        toggleBtns.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
           if (newValue == null) return;
           ToggleButton toggleButton = (ToggleButton) newValue;
           onShapeChange.accept(ShapeAlias.valueOf((String) toggleButton.getUserData()));
        });
    }

    public void setOnShapeChange(Consumer<ShapeAlias> onShapeChange) {
        this.onShapeChange = onShapeChange;
    }
}

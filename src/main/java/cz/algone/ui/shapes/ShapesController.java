package cz.algone.ui.shapes;

import cz.algone.algorithmController.AlgorithmControllerAlias;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import java.util.function.Consumer;

public class ShapesController {
    @FXML private ToggleGroup toggleBtns;

    private Consumer<AlgorithmControllerAlias> onShapeChange;

    @FXML
    public void initialize() {
        toggleBtns.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
           if (newValue == null) return;
           ToggleButton toggleButton = (ToggleButton) newValue;
           onShapeChange.accept(AlgorithmControllerAlias.valueOf((String) toggleButton.getUserData()));
        });
    }

    public ToggleGroup getToggleBtns() {
        return toggleBtns;
    }

    public void setOnShapeChange(Consumer<AlgorithmControllerAlias> onShapeChange) {
        this.onShapeChange = onShapeChange;
    }
}

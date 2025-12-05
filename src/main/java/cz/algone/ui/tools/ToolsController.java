package cz.algone.ui.tools;

import cz.algone.algorithmController.AlgorithmControllerAlias;
import javafx.fxml.FXML;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

import java.util.function.Consumer;

public class ToolsController {
    @FXML
    private ToggleGroup toggleBtns;

    private Consumer<AlgorithmControllerAlias> onToolsChange;

    @FXML
    public void initialize() {
        toggleBtns.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            ToggleButton toggleButton = (ToggleButton) newValue;
            onToolsChange.accept(AlgorithmControllerAlias.valueOf((String) toggleButton.getUserData()));
        });
    }

    public ToggleGroup getToggleBtns() {
        return toggleBtns;
    }

    public void setOnToolsChange(Consumer<AlgorithmControllerAlias> onShapeChange) {
        this.onToolsChange = onShapeChange;
    }
}

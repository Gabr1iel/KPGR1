package cz.algone.ui.toolbar.tools;

import cz.algone.common.enumAlias.AlgorithmControllerAlias;
import cz.algone.ui.MainUIController;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import java.util.function.Consumer;

public class ToolsController extends MainUIController {
    @FXML
    private ToggleGroup toggleBtns;

    @FXML
    public void initialize() {
        toggleBtns.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            ToggleButton toggleButton = (ToggleButton) newValue;
            sceneContext.setControllerAlias(AlgorithmControllerAlias.valueOf((String) toggleButton.getUserData()));
        });
    }

    public ToggleGroup getToggleBtns() {
        return toggleBtns;
    }
}

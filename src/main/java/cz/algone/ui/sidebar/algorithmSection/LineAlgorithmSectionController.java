package cz.algone.ui.sidebar.algorithmSection;

import cz.algone.ui.sidebar.ISidebarSectionController;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;

public class LineAlgorithmSectionController implements ISidebarSectionController {
    @FXML private ToggleGroup algorithmToggle;

    @Override
    public ToggleGroup getToggleGroup() {
        return algorithmToggle;
    }
}

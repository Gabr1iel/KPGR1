package cz.algone.ui.sidebar.settingsSection;

import cz.algone.ui.sidebar.ISidebarSectionController;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;

public class ClipSettingsSectionController implements ISidebarSectionController {
    @FXML private ToggleGroup orientationToggle;

    @Override
    public ToggleGroup getToggleGroup() {return orientationToggle;}
}

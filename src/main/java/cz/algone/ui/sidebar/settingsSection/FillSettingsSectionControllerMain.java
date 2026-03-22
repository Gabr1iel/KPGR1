package cz.algone.ui.sidebar.settingsSection;

import cz.algone.common.enumAlias.PatternAlias;
import cz.algone.ui.MainUIController;
import cz.algone.ui.sidebar.ISidebarSectionController;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

public class FillSettingsSectionControllerMain extends MainUIController implements ISidebarSectionController {
    @FXML private ToggleButton btnTogglePattern;

    @FXML
    public void togglePattern() {
        boolean selected = btnTogglePattern.isSelected();
        if (selected)
            btnTogglePattern.setText("ON");
        else
            btnTogglePattern.setText("OFF");
        sceneContext.setPatternAlias(selected ? PatternAlias.CHECKER : null);
    }

    @Override
    public ToggleGroup getToggleGroup() {return null;}
}

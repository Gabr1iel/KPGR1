package cz.algone.ui.sidebar.settingsSection;

import cz.algone.common.enumAlias.PatternAlias;
import cz.algone.ui.sidebar.ISidebarSectionController;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

import java.util.function.Consumer;

public class FillSettingsSectionController implements ISidebarSectionController {
    @FXML private ToggleButton btnTogglePattern;

    private Consumer<PatternAlias> onPatternChanged;

    @FXML
    public void togglePattern() {
        boolean selected = btnTogglePattern.isSelected();
        if (selected)
            btnTogglePattern.setText("ON");
        else
            btnTogglePattern.setText("OFF");
        onPatternChanged.accept(selected ? PatternAlias.CHECKER : null);
    }

    public void setOnPatternChanged(Consumer<PatternAlias> listener) {this.onPatternChanged = listener;}

    @Override
    public ToggleGroup getToggleGroup() {return null;}
}

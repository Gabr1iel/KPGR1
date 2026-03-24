package cz.algone.ui.sidebar.settingsSection;

import cz.algone.common.enumAlias.PolygonOrientation;
import cz.algone.ui.MainUIController;
import cz.algone.ui.sidebar.ISidebarSectionController;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

public class ClipSettingsSectionController extends MainUIController implements ISidebarSectionController {
    @FXML private ToggleGroup orientationToggle;

    @Override
    protected void onSceneContextReady() {
        if (orientationToggle == null) return;
        orientationToggle.selectedToggleProperty().addListener((obs, old, newToggle) -> {
            if (newToggle instanceof ToggleButton btn && btn.getUserData() != null) {
                try {
                    PolygonOrientation o = PolygonOrientation.valueOf(btn.getUserData().toString());
                    sceneContext.setPolygonOrientation(o);
                } catch (IllegalArgumentException ignored) {}
            }
        });
    }

    @Override
    public ToggleGroup getToggleGroup() { return orientationToggle; }
}

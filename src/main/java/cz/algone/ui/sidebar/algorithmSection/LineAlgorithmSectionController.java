package cz.algone.ui.sidebar.algorithmSection;

import cz.algone.common.enumAlias.AlgorithmAlias;
import cz.algone.ui.MainUIController;
import cz.algone.ui.sidebar.ISidebarSectionController;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

public class LineAlgorithmSectionController extends MainUIController implements ISidebarSectionController {
    @FXML private ToggleGroup algorithmToggle;

    @Override
    protected void onSceneContextReady() {
        if (algorithmToggle == null) return;
        algorithmToggle.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle instanceof ToggleButton btn && btn.getUserData() != null) {
                try {
                    AlgorithmAlias alias = AlgorithmAlias.valueOf(btn.getUserData().toString());
                    sceneContext.setAlgorithmAlias(alias);
                } catch (IllegalArgumentException ignored) {}
            }
        });
    }

    @Override
    public ToggleGroup getToggleGroup() {
        return algorithmToggle;
    }
}

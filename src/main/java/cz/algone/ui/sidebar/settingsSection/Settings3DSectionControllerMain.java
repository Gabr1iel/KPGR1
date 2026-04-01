package cz.algone.ui.sidebar.settingsSection;

import cz.algone.common.enumAlias.EnabledAlias;
import cz.algone.common.enumAlias.ProjMatAlias;
import cz.algone.ui.MainUIController;
import cz.algone.ui.sidebar.ISidebarSectionController;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

public class Settings3DSectionControllerMain extends MainUIController implements ISidebarSectionController {
    @FXML private ToggleButton perspBtn;
    @FXML private ToggleButton orthoBtn;
    @FXML private ToggleButton btnToggleClip;
    @FXML private ToggleButton btnToggleAnimation;
    @FXML private ToggleGroup projectionToggle;

    @Override
    protected void onSceneContextReady() {
        projectionToggle.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue instanceof ToggleButton btn) {
                Object data = btn.getUserData();
                if (data != null) {
                    try {
                        ProjMatAlias alias = ProjMatAlias.valueOf(data.toString());
                        sceneContext.setProjMat(alias);
                    } catch (IllegalArgumentException ignored) {}
                }
            }
        });
    }

    // Pozn.: toto tlačítko nastavuje pouze NONE/FAST. Klávesa V cykluje NONE/FAST/ANALYTICAL,
    // takže při použití V je stav tlačítka a renderer.getClipMode() mimo synchronizaci.
    @FXML
    public void toggleClip3D() {
        boolean selected = btnToggleClip.isSelected();
        if (selected)
            btnToggleClip.setText("ON");
        else
            btnToggleClip.setText("OFF");
        sceneContext.setClip3DEnabled(selected ? EnabledAlias.ENABLED : EnabledAlias.DISABLED);
    }

    @FXML
    public void toggleAnimation() {
        boolean selected = btnToggleAnimation.isSelected();
        if (selected)
            btnToggleAnimation.setText("ON");
        else
            btnToggleAnimation.setText("OFF");
        sceneContext.setAnimationEnabled(selected ? EnabledAlias.ENABLED : EnabledAlias.DISABLED);
    }

    public void resetSettings() {
        btnToggleClip.setSelected(false);
        btnToggleAnimation.setSelected(false);
        perspBtn.setSelected(true);
        orthoBtn.setSelected(false);
    }

    @Override
    public ToggleGroup getToggleGroup() {return null;}
}

package cz.algone.ui.sidebar.algorithmSection;

import cz.algone.common.enumAlias.CubicAlias;
import cz.algone.ui.UIController;
import cz.algone.ui.sidebar.ISidebarSectionController;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

public class Algorithm3DSectionController extends UIController implements ISidebarSectionController {
    @FXML private ToggleGroup algorithmToggle;
    @FXML private ToggleButton bezierBtn;

    @Override
    protected void onSceneContextReady() {
        algorithmToggle.selectedToggleProperty().addListener((observable, oldValue, newToggle) -> {
           if (newToggle == null) return;
           if (newToggle instanceof ToggleButton btn) {
               Object data = btn.getUserData();
               if (data != null) {
                   try {
                       CubicAlias alias = CubicAlias.valueOf(data.toString());
                       sceneContext.setCubicAlias(alias);
                   } catch (Exception ignored) {}
               }
           }
        });
    }

    public void resetCubic() {
        bezierBtn.setSelected(true);
    }

    @Override
    public ToggleGroup getToggleGroup() {return null;}
}

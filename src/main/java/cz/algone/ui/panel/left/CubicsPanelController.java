package cz.algone.ui.panel.left;

import cz.algone.common.enumAlias.CubicAlias;
import cz.algone.ui.panel.common.SolidToggleController;
import cz.algone.ui.panel.common.ToggleBinding;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;

/** Panel kategorie Kubiky — křivka a plocha plus volba typu parametrické kubiky. */
public class CubicsPanelController extends SolidToggleController {
    @FXML private ToggleGroup cubicToggle;

    @Override
    protected void onSolidsReady() {
        ToggleBinding.bindGroup(cubicToggle, CubicAlias.class, sceneContext.cubicAliasProperty());
    }
}

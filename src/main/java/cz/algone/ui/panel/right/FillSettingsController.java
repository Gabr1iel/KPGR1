package cz.algone.ui.panel.right;

import cz.algone.common.enumAlias.AlgorithmAlias;
import cz.algone.common.enumAlias.PatternAlias;
import cz.algone.ui.MainUIController;
import cz.algone.ui.panel.common.ToggleBinding;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

/** Nastavení vyplňovacích algoritmů — pattern a u seed fillu i varianta ukončení. */
public class FillSettingsController extends MainUIController {
    @FXML private ToggleButton patternBtn;
    @FXML private ToggleGroup variantToggle;

    @Override
    protected void onSceneContextReady() {
        ToggleBinding.bindOptional(patternBtn, sceneContext.patternAliasProperty(), PatternAlias.CHECKER, null);
        ToggleBinding.bindGroup(variantToggle, AlgorithmAlias.class, sceneContext.algorithmAliasProperty());
    }
}

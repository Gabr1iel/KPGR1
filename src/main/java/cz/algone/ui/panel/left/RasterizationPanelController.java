package cz.algone.ui.panel.left;

import cz.algone.common.enumAlias.AlgorithmAlias;
import cz.algone.common.enumAlias.AlgorithmControllerAlias;
import cz.algone.ui.panel.common.ControllerAliasToggleController;
import cz.algone.ui.panel.common.ToggleBinding;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

/** Panel kategorie Rasterizace — volba tvaru a u čáry i konkrétního rasterizačního algoritmu. */
public class RasterizationPanelController extends ControllerAliasToggleController {
    @FXML private ToggleGroup algorithmToggle;
    @FXML private VBox lineAlgorithms;

    @Override
    protected void onBindingReady() {
        ToggleBinding.bindGroup(algorithmToggle, AlgorithmAlias.class, sceneContext.algorithmAliasProperty());

        lineAlgorithms.managedProperty().bind(lineAlgorithms.visibleProperty());
        sceneContext.controllerAliasProperty().addListener((obs, old, alias) -> showLineAlgorithms(alias));
        showLineAlgorithms(sceneContext.getControllerAlias());
    }

    /** Varianty rasterizace nabízí jen čára; ostatní tvary mají algoritmus jediný. */
    private void showLineAlgorithms(AlgorithmControllerAlias alias) {
        lineAlgorithms.setVisible(alias == AlgorithmControllerAlias.LINE);
    }
}

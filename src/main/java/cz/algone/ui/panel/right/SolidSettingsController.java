package cz.algone.ui.panel.right;

import cz.algone.algorithmController.controller3D.Controller3D;
import cz.algone.common.enumAlias.EnabledAlias;
import cz.algone.common.enumAlias.ShaderMode;
import cz.algone.model.models3D.Solid;
import cz.algone.ui.panel.common.EnumToggleController;
import cz.algone.ui.panel.common.ToggleBinding;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;

/** Nastavení vybraného tělesa — povrch, textura a rotační animace. */
public class SolidSettingsController extends EnumToggleController<ShaderMode> {
    @FXML private Label solidName;
    @FXML private VBox solidSettings;
    @FXML private Label emptyHint;
    @FXML private ToggleButton animationBtn;

    @Override
    protected Class<ShaderMode> enumType() {
        return ShaderMode.class;
    }

    @Override
    protected ObjectProperty<ShaderMode> boundProperty() {
        return sceneContext.solidShaderModeProperty();
    }

    @Override
    protected void onBindingReady() {
        ToggleBinding.bindOptional(animationBtn, sceneContext.animationEnabledProperty(),
                EnabledAlias.ENABLED, EnabledAlias.DISABLED);

        solidSettings.managedProperty().bind(solidSettings.visibleProperty());
        emptyHint.managedProperty().bind(emptyHint.visibleProperty());

        sceneContext.selectedSolidProperty().addListener((obs, old, solid) -> showSolid(solid));
        showSolid(sceneContext.getSelectedSolid());
    }

    /** Zobrazí nastavení pro vybrané těleso, nebo výzvu k jeho výběru. */
    private void showSolid(Solid solid) {
        solidSettings.setVisible(solid != null);
        emptyHint.setVisible(solid == null);
        solidName.setText(solid == null ? "" : solid.getClass().getSimpleName());
    }

    @FXML
    private void loadTexture() {
        if (sceneContext.getCurrentAlgorithmController() instanceof Controller3D controller)
            controller.loadTexture();
    }
}

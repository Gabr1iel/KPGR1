package cz.algone.ui.panel.right;

import cz.algone.algorithmController.controller3D.Controller3D;
import cz.algone.common.enumAlias.RenderMode;
import cz.algone.ui.panel.common.EnumToggleController;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;

/** Nastavení 3D scény — režim vykreslení a pohled kamery. */
public class Scene3DSettingsController extends EnumToggleController<RenderMode> {

    @Override
    protected Class<RenderMode> enumType() {
        return RenderMode.class;
    }

    @Override
    protected ObjectProperty<RenderMode> boundProperty() {
        return sceneContext.renderModeProperty();
    }

    @FXML
    private void resetCamera() {
        if (sceneContext.getCurrentAlgorithmController() instanceof Controller3D controller)
            controller.resetCamera();
    }
}

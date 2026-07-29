package cz.algone.ui.panel.common;

import cz.algone.ui.MainUIController;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;

/** Panel, jehož hlavní ToggleGroup je svázaná s enum property ve {@link cz.algone.model.SceneContext}u. */
public abstract class EnumToggleController<T extends Enum<T>> extends MainUIController {
    @FXML protected ToggleGroup toggleGroup;

    protected abstract Class<T> enumType();
    protected abstract ObjectProperty<T> boundProperty();

    @Override
    protected void onSceneContextReady() {
        ToggleBinding.bindGroup(toggleGroup, enumType(), boundProperty());
        onBindingReady();
    }

    /** Hook pro potomky, kteří potřebují navázat další prvky panelu. */
    protected void onBindingReady() {}
}

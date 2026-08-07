package cz.algone.ui.panel.left;

import cz.algone.common.enumAlias.ClipMode;
import cz.algone.ui.panel.common.EnumToggleController;
import javafx.beans.property.ObjectProperty;

/** Panel kategorie Ořezání ve 3D — režim ořezání ve frustum testu. */
public class Clip3DPanelController extends EnumToggleController<ClipMode> {

    @Override
    protected Class<ClipMode> enumType() {
        return ClipMode.class;
    }

    @Override
    protected ObjectProperty<ClipMode> boundProperty() {
        return sceneContext.clipMode3DProperty();
    }
}

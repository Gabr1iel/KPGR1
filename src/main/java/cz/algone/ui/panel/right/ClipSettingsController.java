package cz.algone.ui.panel.right;

import cz.algone.common.enumAlias.PolygonOrientation;
import cz.algone.ui.panel.common.EnumToggleController;
import javafx.beans.property.ObjectProperty;

/** Nastavení ořezávacího algoritmu — orientace polygonu. */
public class ClipSettingsController extends EnumToggleController<PolygonOrientation> {

    @Override
    protected Class<PolygonOrientation> enumType() {
        return PolygonOrientation.class;
    }

    @Override
    protected ObjectProperty<PolygonOrientation> boundProperty() {
        return sceneContext.polygonOrientationProperty();
    }
}

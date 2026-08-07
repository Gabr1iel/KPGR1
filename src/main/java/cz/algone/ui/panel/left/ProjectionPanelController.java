package cz.algone.ui.panel.left;

import cz.algone.common.enumAlias.ProjMatAlias;
import cz.algone.ui.panel.common.EnumToggleController;
import javafx.beans.property.ObjectProperty;

/** Panel kategorie Projekce — volba projekční matice. */
public class ProjectionPanelController extends EnumToggleController<ProjMatAlias> {

    @Override
    protected Class<ProjMatAlias> enumType() {
        return ProjMatAlias.class;
    }

    @Override
    protected ObjectProperty<ProjMatAlias> boundProperty() {
        return sceneContext.projMatProperty();
    }
}

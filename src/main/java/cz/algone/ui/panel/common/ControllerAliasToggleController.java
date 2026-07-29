package cz.algone.ui.panel.common;

import cz.algone.common.enumAlias.AlgorithmControllerAlias;
import javafx.beans.property.ObjectProperty;

/** Volba aktivního {@link AlgorithmControllerAlias}u (tvar / druh vyplnění / ořezání). */
public class ControllerAliasToggleController extends EnumToggleController<AlgorithmControllerAlias> {

    @Override
    protected Class<AlgorithmControllerAlias> enumType() {
        return AlgorithmControllerAlias.class;
    }

    @Override
    protected ObjectProperty<AlgorithmControllerAlias> boundProperty() {
        return sceneContext.controllerAliasProperty();
    }
}

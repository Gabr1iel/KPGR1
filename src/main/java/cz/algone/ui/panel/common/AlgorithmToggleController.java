package cz.algone.ui.panel.common;

import cz.algone.common.enumAlias.AlgorithmAlias;
import javafx.beans.property.ObjectProperty;

/** Volba konkrétního algoritmu v rámci aktivního controlleru (např. DDA / Bresenham / Trivial). */
public class AlgorithmToggleController extends EnumToggleController<AlgorithmAlias> {

    @Override
    protected Class<AlgorithmAlias> enumType() {
        return AlgorithmAlias.class;
    }

    @Override
    protected ObjectProperty<AlgorithmAlias> boundProperty() {
        return sceneContext.algorithmAliasProperty();
    }
}

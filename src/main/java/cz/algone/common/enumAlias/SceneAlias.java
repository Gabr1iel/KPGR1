package cz.algone.common.enumAlias;

/** Pracovní prostor aplikace. Určuje, které kategorie nabízí rail. */
public enum SceneAlias implements IAlias {
    /** Témata společná pro 2D i 3D (barvy, tloušťka, antialiasing). */
    GENERAL,
    SCENE_2D,
    SCENE_3D;

    @Override
    public IAlias getAlias(String alias) {
        return SceneAlias.valueOf(alias.toUpperCase());
    }
}

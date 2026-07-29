package cz.algone.common.enumAlias;

/** Záložka pravého panelu. */
public enum RightTabAlias implements IAlias {
    /** Nastavení scény, která nemají vlastní kategorii v railu. */
    SCENE,
    /** Nastavení aktuálně aktivní věci — ve 2D zvoleného algoritmu, ve 3D vybraného tělesa. */
    ALGORITHM;

    @Override
    public IAlias getAlias(String alias) {
        return RightTabAlias.valueOf(alias.toUpperCase());
    }
}

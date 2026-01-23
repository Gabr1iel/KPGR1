package cz.algone.common.enumAlias;

public enum PolygonOrientation implements IAlias {
    AUTO,
    CW,
    CCW;

    @Override
    public IAlias getAlias(String alias) {
        return PolygonOrientation.valueOf(alias.toUpperCase());
    }
}

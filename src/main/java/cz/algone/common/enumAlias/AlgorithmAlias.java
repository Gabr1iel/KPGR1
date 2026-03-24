package cz.algone.common.enumAlias;

public enum AlgorithmAlias implements IAlias {
    DDA,
    BRESENHAM,
    TRIVIAL,
    POLYGON,
    RECTANGLE,
    TRIANGLE,
    CIRCLE,

    SEED_FILL_BACKGROUND,
    SEED_FILL_BORDER,
    SCANLINE_FILL,

    SUTHERLAND_CLIP,
    CLIP_SERVICE,

    RENDERER;


    @Override
    public IAlias getAlias(String alias) {
        return AlgorithmAlias.valueOf(alias.toUpperCase());
    }
}

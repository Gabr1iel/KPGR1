package cz.algone.common.enumAlias;

public enum AlgorithmAlias implements IAlias {
    DDA,
    BRESENHAM,
    TRIVIAL,
    POLYGON,
    CIRCLE,

    SEED_FILL_BACKGROUND,
    SEED_FILL_BORDER,
    SCANLINE_FILL,

    SUTHERLAND_CLIP,
    CLIP_SERVICE;


    @Override
    public IAlias getAlias(String alias) {
        return AlgorithmAlias.valueOf(alias.toUpperCase());
    }
}

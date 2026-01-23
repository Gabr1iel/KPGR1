package cz.algone.common.enumAlias;

public enum SceneAlias implements IAlias {
    SCENE_2D,
    SCENE_3D;

    @Override
    public IAlias getAlias(String alias) {
        return SceneAlias.valueOf(alias.toUpperCase());
    }
}

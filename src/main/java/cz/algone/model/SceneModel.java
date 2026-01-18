package cz.algone.model;

import cz.algone.model.models3D.Solid;
import cz.algone.model.models3D.SolidAlias;

import java.util.HashMap;
import java.util.Map;

public class SceneModel {
    private final Map<ModelType, Model> models = new HashMap<>();
    private final Map<SolidAlias, Solid> solids = new HashMap<>();

    /** Vyčistí mapu modelů a solidů, nijak nepracuje s {@link cz.algone.raster.RasterCanvas} */
    public void clear() {models.clear(); solids.clear();}

    public Map<ModelType, Model> getModels() {
        return models;
    }
    public Map<SolidAlias, Solid> getSolids() {return solids;}
}

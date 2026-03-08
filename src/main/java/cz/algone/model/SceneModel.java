package cz.algone.model;

import cz.algone.common.enumAlias.ModelType;
import cz.algone.model.models2D.Model;
import cz.algone.model.models3D.wiredSolids.Solid;
import cz.algone.common.enumAlias.SolidAlias;
import cz.algone.raster.ImageBuffer;

import java.util.HashMap;
import java.util.Map;

public class SceneModel {
    private final Map<ModelType, Model> models = new HashMap<>();
    private final Map<SolidAlias, Solid> solids = new HashMap<>();

    /** Vyčistí mapu modelů a solidů, u solidů resetuje původní nastavení,
     *  nijak nepracuje s {@link ImageBuffer} */
    public void clear() {
        models.clear();
        for (Solid solid : solids.values()) {
            solid.resetTransform();
        }
        solids.clear();
    }

    public Map<ModelType, Model> getModels() {
        return models;
    }
    public Map<SolidAlias, Solid> getSolids() {return solids;}
}

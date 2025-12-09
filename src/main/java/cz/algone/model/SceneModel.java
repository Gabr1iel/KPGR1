package cz.algone.model;

import java.util.HashMap;
import java.util.Map;

public class SceneModel {
    private final Map<ModelType, Model> models = new HashMap<>();

    public void clear() {
        models.clear();
    }

    public Map<ModelType, Model> getModels() {
        return models;
    }
}

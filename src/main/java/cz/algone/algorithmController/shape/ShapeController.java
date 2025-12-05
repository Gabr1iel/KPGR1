package cz.algone.algorithmController.shape;

import cz.algone.algorithmController.IAlgorithmController;
import cz.algone.model.Model;

public interface ShapeController<T extends Model> extends IAlgorithmController {
    void drawScene();
    void clearRaster();

}

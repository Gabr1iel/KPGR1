package cz.algone.algorithmController.shape;

import cz.algone.algorithmController.IAlgorithmController;
import cz.algone.model.models2D.Model;
import cz.algone.model.SceneModel;

public interface ShapeController extends IAlgorithmController {
    void drawScene();
    /** Uloží model do {@link SceneModel} a následně uloženou hodnotu vrátí */
    Model updateModel();
}

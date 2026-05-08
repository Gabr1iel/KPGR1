package cz.algone.algorithmController.shape;

import cz.algone.algorithmController.IAlgorithmController;
import cz.algone.model.models2D.Model;

public interface ShapeController extends IAlgorithmController {
    void drawScene();
    /** Uloží aktuální model do scény a vrátí jej. */
    Model updateModel();
}

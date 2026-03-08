package cz.algone.algorithmController;

import cz.algone.common.enumAlias.AlgorithmAlias;
import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithmController.scene.SceneModelController;
import cz.algone.raster.ImageBuffer;
import cz.algone.util.color.ColorPair;

public interface IAlgorithmController {
    void initListeners();
    void setup(IAlgorithm algorithm, SceneModelController sceneModelController);
    void setColors(ColorPair colors);
    AlgorithmAlias getDefaultAlgorithm();
}

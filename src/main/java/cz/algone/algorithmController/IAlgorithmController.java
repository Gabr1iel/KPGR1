package cz.algone.algorithmController;

import cz.algone.common.enumAlias.AlgorithmAlias;
import cz.algone.algorithm.IAlgorithm;
import cz.algone.model.SceneContext;
import cz.algone.util.color.ColorPair;

public interface IAlgorithmController {
    void initListeners();
    void setup(IAlgorithm algorithm, SceneContext sceneContext);
    void setColors(ColorPair colors);
    AlgorithmAlias getDefaultAlgorithm();
}

package cz.algone.algorithmController;

import cz.algone.algorithm.AlgorithmAlias;
import cz.algone.algorithm.IAlgorithm;
import cz.algone.raster.RasterCanvas;
import cz.algone.util.color.ColorPair;

public interface IAlgorithmController {
    void initListeners();
    void setup(RasterCanvas canvas, IAlgorithm algorithm);
    void setColors(ColorPair colors);
    AlgorithmAlias getDefaultAlgorithm();
}

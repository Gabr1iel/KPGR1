package cz.algone.algorithm.fill;

import cz.algone.algorithm.IAlgorithm;
import cz.algone.util.color.ColorPair;

public interface IFill extends IAlgorithm {
    void fill(int x, int y, ColorPair color, int borderColor);
}

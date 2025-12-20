package cz.algone.algorithm.fill;

import cz.algone.algorithm.IAlgorithm;
import cz.algone.model.Model;
import cz.algone.util.color.ColorPair;

public interface IFill<T extends Model> extends IAlgorithm {
    void fill(T model, ColorPair color, int borderColor);
}

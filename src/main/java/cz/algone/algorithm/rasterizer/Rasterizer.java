package cz.algone.algorithm.rasterizer;

import cz.algone.algorithm.IAlgorithm;
import cz.algone.model.Model;
import cz.algone.util.color.ColorPair;

public interface Rasterizer<T extends Model> extends IAlgorithm {
    void rasterize(T model);
}

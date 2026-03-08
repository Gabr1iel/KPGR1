package cz.algone.algorithm.rasterizer;

import cz.algone.algorithm.IAlgorithm;
import cz.algone.model.models2D.Model;

public interface IRasterizer<T extends Model> extends IAlgorithm {
    void rasterize(T model);
}

package cz.algone.rasterize;

import cz.algone.model.Model;

public interface Rasterizer<T extends Model> {
    void rasterize(T model);
}

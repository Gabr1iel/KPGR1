package rasterize;

import model.Model;

public interface Rasterizer<T extends Model> {
    void rasterize(T model);
}

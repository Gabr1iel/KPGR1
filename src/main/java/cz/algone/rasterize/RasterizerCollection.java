package cz.algone.rasterize;

import cz.algone.model.Line;
import cz.algone.polygon.Polygon;
import cz.algone.polygon.PolygonRasterizer;

public class RasterizerCollection {
    public final Rasterizer<Line> lineRasterizerBresenham;
    public final Rasterizer<Line> lineRasterizerDDA;
    public final Rasterizer<Line> lineRasterizerTrivial;

    public RasterizerCollection() {
        lineRasterizerBresenham = new LineRasterizerBresenham();
        lineRasterizerDDA = new LineRasterizerDDA();
        lineRasterizerTrivial = new LineRasterizerTrivial();
    }
}

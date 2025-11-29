package cz.algone.rasterizer;

import cz.algone.model.Line;
import cz.algone.model.Polygon;
import cz.algone.rasterizer.line.LineRasterizerBresenham;
import cz.algone.rasterizer.line.LineRasterizerDDA;
import cz.algone.rasterizer.line.LineRasterizerTrivial;
import cz.algone.rasterizer.polygon.PolygonRasterizer;

import java.util.HashMap;
import java.util.Map;

public class RasterizerCollection {
    public final Map<RasterizerAlias, Rasterizer> rasterizerMap = new HashMap<>();
    public final Rasterizer<Polygon> polygonRasterizer;
    public final Rasterizer<Line> lineRasterizerBresenham;
    public final Rasterizer<Line> lineRasterizerDDA;
    public final Rasterizer<Line> lineRasterizerTrivial;

    public RasterizerCollection() {
        polygonRasterizer = new PolygonRasterizer();
        lineRasterizerBresenham = new LineRasterizerBresenham();
        lineRasterizerDDA = new LineRasterizerDDA();
        lineRasterizerTrivial = new LineRasterizerTrivial();
        setRasterizerAlias();
    }

    private void setRasterizerAlias() {
        rasterizerMap.put(RasterizerAlias.POLYGON, lineRasterizerBresenham);
        rasterizerMap.put(RasterizerAlias.BRESENHAM, lineRasterizerBresenham);
        rasterizerMap.put(RasterizerAlias.DDA, lineRasterizerDDA);
        rasterizerMap.put(RasterizerAlias.TRIVIAL, lineRasterizerTrivial);
    }
}

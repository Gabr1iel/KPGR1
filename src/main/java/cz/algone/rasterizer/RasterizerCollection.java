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
    public final PolygonRasterizer polygonRasterizer = new PolygonRasterizer();
    public final Rasterizer<Line> lineRasterizerBresenham;
    public final Rasterizer<Line> lineRasterizerDDA;
    public final Rasterizer<Line> lineRasterizerTrivial;

    public RasterizerCollection() {
        lineRasterizerBresenham = new LineRasterizerBresenham();
        lineRasterizerDDA = new LineRasterizerDDA();
        lineRasterizerTrivial = new LineRasterizerTrivial();
        polygonRasterizer.setupPolygonRasterizer(lineRasterizerBresenham);
        setRasterizerAlias();
    }

    private void setRasterizerAlias() {
        rasterizerMap.put(RasterizerAlias.POLYGON, polygonRasterizer);
        rasterizerMap.put(RasterizerAlias.BRESENHAM, lineRasterizerBresenham);
        rasterizerMap.put(RasterizerAlias.LINE, lineRasterizerBresenham);
        rasterizerMap.put(RasterizerAlias.DDA, lineRasterizerDDA);
        rasterizerMap.put(RasterizerAlias.TRIVIAL, lineRasterizerTrivial);
    }
}

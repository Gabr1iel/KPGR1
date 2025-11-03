package cz.algone.rasterizer;

import cz.algone.model.Line;
import cz.algone.rasterizer.line.LineRasterizerBresenham;
import cz.algone.rasterizer.line.LineRasterizerDDA;
import cz.algone.rasterizer.line.LineRasterizerTrivial;

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

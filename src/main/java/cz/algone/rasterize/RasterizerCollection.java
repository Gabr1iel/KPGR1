package cz.algone.rasterize;

import cz.algone.line.Line;
import cz.algone.line.LineRasterizerBresenham;
import cz.algone.line.LineRasterizerDDA;
import cz.algone.line.LineRasterizerTrivial;

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

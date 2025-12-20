package cz.algone.algorithm;

import cz.algone.algorithm.fill.FillMode;
import cz.algone.algorithm.fill.scanline.ScanlineFill;
import cz.algone.algorithm.fill.seed.SeedFill;
import cz.algone.algorithm.rasterizer.Rasterizer;
import cz.algone.algorithm.rasterizer.line.LineRasterizerBresenham;
import cz.algone.algorithm.rasterizer.line.LineRasterizerDDA;
import cz.algone.algorithm.rasterizer.line.LineRasterizerTrivial;
import cz.algone.algorithm.rasterizer.polygon.PolygonRasterizer;
import cz.algone.model.Line;
import java.util.HashMap;
import java.util.Map;

public class AlgorithmCollection {
    public final Map<AlgorithmAlias, IAlgorithm> algorithmMap = new HashMap<>();
    public final PolygonRasterizer polygonRasterizer = new PolygonRasterizer();
    public final Rasterizer<Line> lineRasterizerBresenham;
    public final Rasterizer<Line> lineRasterizerDDA;
    public final Rasterizer<Line> lineRasterizerTrivial;
    public final SeedFill seedFillBorder;
    public final SeedFill seedFillBackground;
    public final ScanlineFill scanlineFill;

    public AlgorithmCollection() {
        this.lineRasterizerBresenham = new LineRasterizerBresenham();
        this.lineRasterizerDDA = new LineRasterizerDDA();
        this.lineRasterizerTrivial = new LineRasterizerTrivial();
        polygonRasterizer.setupPolygonRasterizer(lineRasterizerBresenham);

        this.seedFillBorder = new SeedFill(FillMode.BORDER);
        this.seedFillBackground = new SeedFill(FillMode.BACKGROUND);
        this.scanlineFill = new ScanlineFill();
        setupAlgorithmAlias();
    }

    private void setupAlgorithmAlias() {
        algorithmMap.put(AlgorithmAlias.POLYGON, polygonRasterizer);
        algorithmMap.put(AlgorithmAlias.BRESENHAM, lineRasterizerBresenham);
        algorithmMap.put(AlgorithmAlias.DDA, lineRasterizerDDA);
        algorithmMap.put(AlgorithmAlias.TRIVIAL, lineRasterizerTrivial);

        algorithmMap.put(AlgorithmAlias.SEED_FILL_BORDER, seedFillBorder);
        algorithmMap.put(AlgorithmAlias.SEED_FILL_BACKGROUND, seedFillBackground);
        algorithmMap.put(AlgorithmAlias.SCANLINE_FILL, scanlineFill);
    }
}

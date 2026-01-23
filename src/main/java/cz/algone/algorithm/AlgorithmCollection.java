package cz.algone.algorithm;

import cz.algone.algorithm.clip.ClipService;
import cz.algone.algorithm.clip.SutherlandHodgmanClipper;
import cz.algone.common.enumAlias.FillMode;
import cz.algone.algorithm.fill.scanline.ScanlineFill;
import cz.algone.algorithm.fill.seed.SeedFill;
import cz.algone.algorithm.rasterizer.IRasterizer;
import cz.algone.algorithm.rasterizer.circle.CircleRasterizerMidpoint;
import cz.algone.algorithm.rasterizer.line.LineRasterizerBresenham;
import cz.algone.algorithm.rasterizer.line.LineRasterizerDDA;
import cz.algone.algorithm.rasterizer.line.LineRasterizerTrivial;
import cz.algone.algorithm.rasterizer.polygon.PolygonRasterizer;
import cz.algone.common.enumAlias.AlgorithmAlias;
import cz.algone.model.Circle;
import cz.algone.model.Line;
import java.util.HashMap;
import java.util.Map;

public class AlgorithmCollection {
    public final Map<AlgorithmAlias, IAlgorithm> algorithmMap = new HashMap<>();
    public final PolygonRasterizer polygonRasterizer;
    public final IRasterizer<Line> lineRasterizerBresenham;
    public final IRasterizer<Line> lineRasterizerDDA;
    public final IRasterizer<Line> lineRasterizerTrivial;
    public final IRasterizer<Circle> circleRasterizer;

    public final SeedFill seedFillBorder;
    public final SeedFill seedFillBackground;
    public final ScanlineFill scanlineFill;

    public final SutherlandHodgmanClipper sutherlandHodgmanClipper;
    public final ClipService clipService;

    public AlgorithmCollection() {
        this.lineRasterizerBresenham = new LineRasterizerBresenham();
        this.lineRasterizerDDA = new LineRasterizerDDA();
        this.lineRasterizerTrivial = new LineRasterizerTrivial();
        this.circleRasterizer = new CircleRasterizerMidpoint();
        this.polygonRasterizer = new PolygonRasterizer(lineRasterizerBresenham);

        this.seedFillBorder = new SeedFill(FillMode.BORDER);
        this.seedFillBackground = new SeedFill(FillMode.BACKGROUND);
        this.scanlineFill = new ScanlineFill(polygonRasterizer);

        this.sutherlandHodgmanClipper = new SutherlandHodgmanClipper();
        this.clipService = new ClipService(sutherlandHodgmanClipper, scanlineFill, polygonRasterizer);
        setupAlgorithmAlias();
    }

    private void setupAlgorithmAlias() {
        algorithmMap.put(AlgorithmAlias.POLYGON, polygonRasterizer);
        algorithmMap.put(AlgorithmAlias.BRESENHAM, lineRasterizerBresenham);
        algorithmMap.put(AlgorithmAlias.DDA, lineRasterizerDDA);
        algorithmMap.put(AlgorithmAlias.TRIVIAL, lineRasterizerTrivial);
        algorithmMap.put(AlgorithmAlias.CIRCLE,  circleRasterizer);

        algorithmMap.put(AlgorithmAlias.SEED_FILL_BORDER, seedFillBorder);
        algorithmMap.put(AlgorithmAlias.SEED_FILL_BACKGROUND, seedFillBackground);
        algorithmMap.put(AlgorithmAlias.SCANLINE_FILL, scanlineFill);

        algorithmMap.put(AlgorithmAlias.SUTHERLAND_CLIP, sutherlandHodgmanClipper);
        algorithmMap.put(AlgorithmAlias.CLIP_SERVICE, clipService);
    }
}

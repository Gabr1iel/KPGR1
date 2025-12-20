package cz.algone.algorithmController;

import cz.algone.algorithmController.fill.ScanlineFillController;
import cz.algone.algorithmController.fill.SeedFillController;
import cz.algone.algorithmController.shape.line.LineShapeController;
import cz.algone.algorithmController.shape.polygon.PolygonShapeController;
import cz.algone.algorithmController.shape.rectangle.RectangleShapeController;

import java.util.HashMap;
import java.util.Map;

public class AlgorithmControllerCollection {
    public final Map<AlgorithmControllerAlias, IAlgorithmController> algorithmControllerMap = new HashMap<>();
    public final LineShapeController lineShapeController;
    public final PolygonShapeController polygonShapeController;
    public final RectangleShapeController rectangleShapeController;

    public final SeedFillController seedFillController;
    public final ScanlineFillController scanlineFillController;

    public AlgorithmControllerCollection() {
        this.lineShapeController = new LineShapeController();
        this.polygonShapeController = new PolygonShapeController();
        this.rectangleShapeController = new RectangleShapeController();

        this.seedFillController = new SeedFillController();
        this.scanlineFillController = new ScanlineFillController();

        setupShapesAlias();
    }

    private void setupShapesAlias() {
        algorithmControllerMap.put(AlgorithmControllerAlias.LINE, lineShapeController);
        algorithmControllerMap.put(AlgorithmControllerAlias.POLYGON, polygonShapeController);
        algorithmControllerMap.put(AlgorithmControllerAlias.RECTANGLE, rectangleShapeController);
        algorithmControllerMap.put(AlgorithmControllerAlias.SEED_FILL, seedFillController);
        algorithmControllerMap.put(AlgorithmControllerAlias.SCANLINE_FILL,scanlineFillController);
    }
}

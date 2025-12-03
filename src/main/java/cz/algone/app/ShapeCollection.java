package cz.algone.app;

import cz.algone.app.line.LineShapeController;
import cz.algone.app.polygon.PolygonShapeController;
import java.util.HashMap;
import java.util.Map;

public class ShapeCollection {
    public final Map<ShapeAlias, ShapeController> shapeMap = new HashMap<>();
    public final LineShapeController lineShapeController;
    public final PolygonShapeController polygonShapeController;

    public ShapeCollection() {
        this.lineShapeController = new LineShapeController();
        this.polygonShapeController = new PolygonShapeController();
        setupShapesAlias();
    }

    private void setupShapesAlias() {
        shapeMap.put(ShapeAlias.LINE, lineShapeController);
        shapeMap.put(ShapeAlias.POLYGON, polygonShapeController);
    }
}

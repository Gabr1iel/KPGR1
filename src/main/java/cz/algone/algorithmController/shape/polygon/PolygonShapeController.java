package cz.algone.algorithmController.shape.polygon;

import cz.algone.common.enumAlias.AlgorithmAlias;
import cz.algone.algorithm.IAlgorithm;
import cz.algone.model.SceneContext;
import cz.algone.algorithmController.shape.ShapeController;
import cz.algone.common.enumAlias.ModelType;
import cz.algone.model.models2D.Model;
import cz.algone.model.models2D.Point;
import cz.algone.model.models2D.Polygon;
import cz.algone.algorithm.rasterizer.IRasterizer;
import cz.algone.util.color.ColorPair;
import cz.algone.util.geometry.GeometryUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;

import java.util.List;

public class PolygonShapeController implements ShapeController {
    private final AlgorithmAlias DEFAULT_ALGORITHM = AlgorithmAlias.POLYGON;
    private final ModelType DEFAULT_MODELTYPE = ModelType.POLYGON;
    private Canvas canvas;
    private SceneContext sceneContext;
    private Polygon polygon;
    private ColorPair colors;
    private int nearestPointIndex = -1;
    private IRasterizer<Polygon> polygonRasterizer;

    @Override
    public void setup(IAlgorithm polygonRasterizer, SceneContext sceneContext) {
        this.canvas = sceneContext.getImageBuffer().getCanvas();
        this.polygonRasterizer = (IRasterizer<Polygon>) polygonRasterizer;
        this.sceneContext = sceneContext;
        if (sceneContext.getModels().get(DEFAULT_MODELTYPE) == null)
            polygon = new Polygon(colors);
        else
            polygon = (Polygon) sceneContext.getModels().get(DEFAULT_MODELTYPE);
        updateModel();
    }

    @Override
    public void initListeners() {
        canvas.setOnMousePressed(e -> {
            polygon = (Polygon) sceneContext.getModels().get(DEFAULT_MODELTYPE);
            if (polygon == null)
                polygon = new Polygon(colors);
            if (e.getButton() == MouseButton.SECONDARY) {
                nearestPointIndex = polygon.getNearestPoint((int) e.getX(), (int) e.getY());
                if (e.isAltDown())
                    polygon.getPoints().remove(nearestPointIndex);
                if (e.isShiftDown())
                    insertPointIntoNearestEdge((int) e.getX(), (int) e.getY());
            }
            drawScene();
        });
        canvas.setOnMouseDragged(e -> {
            if (!e.isSecondaryButtonDown())
                polygon.setPreviewPoint(new Point((int) e.getX(), (int) e.getY()));
            else
                polygon.setPointByIndex(nearestPointIndex, (int) e.getX(), (int) e.getY());
            drawScene();
        });
        canvas.setOnMouseReleased(e -> {
            if (e.getButton() == MouseButton.PRIMARY)
                polygon.addPoint(new Point((int) e.getX(), (int) e.getY()));
            polygon.setPreviewPoint(null);
            nearestPointIndex = -1;
            drawScene();
        });
    }

    @Override
    public void drawScene() {
        sceneContext.clearRasterAndScene();
        polygonRasterizer.rasterize((Polygon) updateModel());
    }

    @Override
    public Model updateModel() {
        polygon.setColors(colors);
        sceneContext.getModels().put(DEFAULT_MODELTYPE, polygon);
        return polygon;
    }

    @Override
    public AlgorithmAlias getDefaultAlgorithm() {
        return DEFAULT_ALGORITHM;
    }

    @Override
    public void setColors(ColorPair colors) {
        this.colors = colors;
    }

    private void insertPointIntoNearestEdge(int x, int y) {
        List<Point> points = polygon.getPoints();
        if (points.size() < 2) return;

        double bestDist = Double.MAX_VALUE;
        int insertIndex = -1;

        for (int i = 0; i < points.size() - 1; i++) {
            Point a = points.get(i);
            Point b = points.get(i + 1);

            double dist = GeometryUtils.distancePointToSegment(x, y, a, b);
            if (dist < bestDist) {
                bestDist = dist;
                insertIndex = i + 1;
            }
        }

        Point last = points.getLast();
        Point first = points.getFirst();
        double distClosing = GeometryUtils.distancePointToSegment(x, y, last, first);
        if (distClosing < bestDist) {
            bestDist = distClosing;
            insertIndex = points.size();
        }

        double threshold = 8.0;
        if (insertIndex != -1 && bestDist <= threshold) {
            points.add(insertIndex, new Point(x, y));
        }
    }
}

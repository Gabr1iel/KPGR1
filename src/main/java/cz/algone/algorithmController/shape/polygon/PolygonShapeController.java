package cz.algone.algorithmController.shape.polygon;

import cz.algone.algorithm.AlgorithmAlias;
import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithmController.scene.SceneModelController;
import cz.algone.algorithmController.shape.ShapeController;
import cz.algone.model.*;
import cz.algone.raster.RasterCanvas;
import cz.algone.algorithm.rasterizer.Rasterizer;
import cz.algone.util.color.ColorPair;
import cz.algone.util.geometry.GeometryUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;

import java.util.List;

public class PolygonShapeController implements ShapeController {
    private final AlgorithmAlias DEFAULT_ALGORITHM = AlgorithmAlias.POLYGON;
    private final ModelType DEFAULT_MODELTYPE = ModelType.POLYGON;
    private RasterCanvas raster;
    private Canvas canvas;
    private SceneModelController sceneModelController;
    private SceneModel sceneModel;
    private Polygon polygon;
    private ColorPair colors;
    private int nearestPointIndex = -1;
    private Rasterizer polygonRasterizer;

    @Override
    public void setup(RasterCanvas raster, IAlgorithm polygonRasterizer, SceneModelController sceneModelController) {
        this.raster = raster;
        this.canvas = raster.getCanvas();
        this.polygonRasterizer = (Rasterizer) polygonRasterizer;
        this.sceneModelController = sceneModelController;
        this.sceneModel = sceneModelController.getSceneModel();
        polygon = new Polygon(colors);
        updateModel();
    }

    @Override
    public void initListeners() {
        canvas.setOnMousePressed(e -> {
            polygon = (Polygon) sceneModel.getModels().get(DEFAULT_MODELTYPE);
            if (polygon == null)
                polygon = new Polygon(colors);
            if (e.getButton() == MouseButton.SECONDARY) { //Získání indexu nebližšího bodu
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
            else //Update nejbližšího bodu
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
        sceneModelController.clearRaster();
        polygonRasterizer.rasterize(updateModel());
    }

    @Override
    public Model updateModel() {
        polygon.setColors(colors);
        sceneModel.getModels().put(DEFAULT_MODELTYPE, polygon);
        return sceneModel.getModels().get(DEFAULT_MODELTYPE);
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

        // Prochází všechny hrany (Pi, P(i+1))
        for (int i = 0; i < points.size() - 1; i++) {
            Point a = points.get(i);
            Point b = points.get(i + 1);

            double dist = GeometryUtils.distancePointToSegment(x, y, a, b);
            if (dist < bestDist) {
                bestDist = dist;
                insertIndex = i + 1; // nový bod se vloží mezi a a b → na pozici i+1
            }
        }

        //Kontrola prvního a posledního bodu
        Point last = points.get(points.size() - 1);
        Point first = points.get(0);
        double distClosing = GeometryUtils.distancePointToSegment(x, y, last, first);
        if (distClosing < bestDist) {
            bestDist = distClosing;
            insertIndex = points.size();
        }


        // Omezení vzdálenosti pro přidání do hrany
        double threshold = 8.0;
        if (insertIndex != -1 && bestDist <= threshold) {
            points.add(insertIndex, new Point(x, y));
        }
    }
}

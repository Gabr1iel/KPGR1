package cz.algone.algorithmController.shape.triangle;

import cz.algone.common.enumAlias.AlgorithmAlias;
import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithm.rasterizer.IRasterizer;
import cz.algone.algorithmController.scene.SceneModelController;
import cz.algone.algorithmController.shape.ShapeController;
import cz.algone.common.enumAlias.ModelType;
import cz.algone.model.*;
import cz.algone.model.models2D.Model;
import cz.algone.model.models2D.Point;
import cz.algone.model.models2D.Polygon;
import cz.algone.raster.ImageBuffer;
import cz.algone.util.color.ColorPair;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;

public class TriangleShapeController implements ShapeController {
    private final AlgorithmAlias DEFAULT_ALGORITHM = AlgorithmAlias.POLYGON;
    private final ModelType DEFAULT_MODELTYPE = ModelType.POLYGON;

    private Point point1;
    private Canvas canvas;
    private SceneModel sceneModel;
    private SceneModelController sceneModelController;
    private Polygon polygon;
    private ColorPair colors;
    private IRasterizer polygonRasterizer;

    @Override
    public void setup(IAlgorithm polygonRasterizer, SceneModelController sceneModelController) {
        this.canvas = sceneModelController.getImageBuffer().getCanvas();
        this.polygonRasterizer = (IRasterizer) polygonRasterizer;
        this.sceneModelController = sceneModelController;
        this.sceneModel = sceneModelController.getSceneModel();
        polygon = new Polygon(colors);
    }

    @Override
    public void initListeners() {
        canvas.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                point1 = new Point((int) event.getX(), (int) event.getY());
            }
        });
        canvas.setOnMouseDragged(event -> {
            if (point1 == null) return;
            Point point3 = new Point((int) event.getX(), (int) event.getY());

            createTriangle(point1, point3);
            drawScene();
        });
        canvas.setOnMouseReleased(event -> {
            if (point1 == null) return;
            Point point3 = new Point((int) event.getX(), (int) event.getY());

            createTriangle(point1, point3);
            drawScene();
        });
    }

    private void createTriangle(Point point1, Point point2) {
        polygon = new Polygon(colors);
        polygon.addPoint(point1);
        point2 = ensureOddLength(point1, point2);

        polygon.addPoint(new Point(point2.x(), point1.y()));
        polygon.addPoint(new Point(findCenter(point1.x(), point2.x()), point2.y()));
    }

    private int findCenter(int x1, int x2) {
        return x2 - ((x2 - x1) / 2);
    }

    /** Zkontroluje vzdálenost x mezi body, pokud je sudá tak od point2 odečte 1 */
    private Point ensureOddLength(Point p1, Point p2) {
        int dx = Math.abs(p1.x() - p2.x());
        if (dx % 2 == 0)
            return new Point(p2.x() - 1, p2.y());
        else
            return p2;
    }

    @Override
    public void drawScene() {
        sceneModelController.clearRasterAndScene();
        polygonRasterizer.rasterize(updateModel());
    }

    @Override
    public Model updateModel() {
        sceneModel.getModels().put(DEFAULT_MODELTYPE, polygon);
        return sceneModel.getModels().get(DEFAULT_MODELTYPE);
    }

    @Override
    public void setColors(ColorPair colors) {this.colors = colors;}

    @Override
    public AlgorithmAlias getDefaultAlgorithm() {return DEFAULT_ALGORITHM;}
}
package cz.algone.algorithmController.shape.rectangle;

import cz.algone.common.enumAlias.AlgorithmAlias;
import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithm.rasterizer.IRasterizer;
import cz.algone.model.SceneContext;
import cz.algone.algorithmController.shape.ShapeController;
import cz.algone.common.enumAlias.ModelType;
import cz.algone.model.models2D.Model;
import cz.algone.model.models2D.Point;
import cz.algone.model.models2D.Polygon;
import cz.algone.util.color.ColorPair;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;

public class RectangleShapeController implements ShapeController {
    private final AlgorithmAlias DEFAULT_ALGORITHM = AlgorithmAlias.RECTANGLE;
    private final ModelType DEFAULT_MODELTYPE = ModelType.POLYGON;
    private Point point1;
    private Canvas canvas;
    private SceneContext sceneContext;
    private Polygon polygon;
    private ColorPair colors;
    private IRasterizer<Polygon> polygonRasterizer;

    @Override
    public void setup(IAlgorithm polygonRasterizer, SceneContext sceneContext) {
        this.canvas = sceneContext.getImageBuffer().getCanvas();
        this.polygonRasterizer = (IRasterizer<Polygon>) polygonRasterizer;
        this.sceneContext = sceneContext;
        polygon = null;
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
            if (event.isShiftDown())
                point3 = makeSquare(point1, point3);

            createRectangle(point1, point3);
            drawScene();
        });
        canvas.setOnMouseReleased(event -> {
            if (point1 == null) return;
            Point point3 = new Point((int) event.getX(), (int) event.getY());
            if (event.isShiftDown())
                point3 = makeSquare(point1, point3);

            createRectangle(point1, point3);
            drawScene();
        });
    }

    private void createRectangle(Point point1, Point point3) {
        polygon = new Polygon(colors);
        polygon.addPoint(point1);
        polygon.addPoint(new Point(point3.x(), point1.y()));
        polygon.addPoint(point3);
        polygon.addPoint(new Point(point1.x(), point3.y()));
    }
    /** spočítá délku dy a dx, vezme menší a následně vypočítá souřadnice
     * třetího bodu v závisloti na dané délce */
    private Point makeSquare(Point p1, Point p3) {
        int dx = p3.x() - p1.x();
        int dy = p3.y() - p1.y();

        int side = Math.min(Math.abs(dx), Math.abs(dy));

        int signX = dx >= 0 ? 1 : -1;
        int signY = dy >= 0 ? 1 : -1;

        int x2 = p1.x() + signX * side;
        int y2 = p1.y() + signY * side;

        return new Point(x2, y2);
    }

    @Override
    public void drawScene() {
        sceneContext.clearRasterAndScene();
        polygonRasterizer.rasterize((Polygon) updateModel());
    }

    @Override
    public Model updateModel() {
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
}

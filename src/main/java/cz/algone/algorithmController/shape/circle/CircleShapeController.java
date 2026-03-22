package cz.algone.algorithmController.shape.circle;

import cz.algone.common.enumAlias.AlgorithmAlias;
import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithm.rasterizer.IRasterizer;
import cz.algone.algorithmController.scene.SceneContext;
import cz.algone.algorithmController.shape.ShapeController;
import cz.algone.common.enumAlias.ModelType;
import cz.algone.model.*;
import cz.algone.model.models2D.Circle;
import cz.algone.model.models2D.Model;
import cz.algone.model.models2D.Point;
import cz.algone.util.color.ColorPair;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;

public class CircleShapeController implements ShapeController {
    private static final AlgorithmAlias DEFAULT_ALGORITHM = AlgorithmAlias.CIRCLE;
    private static final ModelType DEFAULT_MODELTYPE = ModelType.CIRCLE;

    private Canvas canvas;
    private SceneContext sceneContext;
    private SceneModel sceneModel;
    private IRasterizer circleRasterizer;
    private ColorPair colors;
    private Circle circle;
    private Point center;

    @Override
    public void setup(IAlgorithm algorithm, SceneContext sceneContext) {
        this.canvas = sceneContext.getImageBuffer().getCanvas();
        this.circleRasterizer = (IRasterizer) algorithm;

        this.sceneContext = sceneContext;
        this.sceneModel = sceneContext.getSceneModel();
    }

    @Override
    public void initListeners() {
        canvas.setOnMousePressed(e -> {
            if (e.getButton() != MouseButton.PRIMARY) return;

            center = new Point((int) e.getX(), (int) e.getY());
            circle = new Circle(center, 0, colors);
            drawScene();
        });
        canvas.setOnMouseDragged(e -> {
            if (center == null) return;
            if (!e.isPrimaryButtonDown()) return;

            int x = (int) e.getX();
            int y = (int) e.getY();

            int radius = radius(center.x(), center.y(), x, y);

            circle = new Circle(center, radius, colors);
            drawScene();
        });
        canvas.setOnMouseReleased(e -> {
            if (e.getButton() != MouseButton.PRIMARY) return;
            if (center == null) return;

            int x = (int) e.getX();
            int y = (int) e.getY();

            int radius = radius(center.x(), center.y(), x, y);

            circle = new Circle(center, radius, colors);
            drawScene();
            center = null;
        });
    }

    private int radius(int x1, int y1, int x2, int y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return (int) Math.round(Math.sqrt(dx * dx + dy * dy));
    }

    @Override
    public void drawScene() {
        sceneContext.clearRasterAndScene();
        circleRasterizer.rasterize(updateModel());
    }

    @Override
    public Model updateModel() {
        sceneModel.getModels().put(DEFAULT_MODELTYPE, circle);
        return sceneModel.getModels().get(DEFAULT_MODELTYPE);
    }

    @Override
    public void setColors(ColorPair colors) {
        this.colors = colors;
    }

    @Override
    public AlgorithmAlias getDefaultAlgorithm() {
        return DEFAULT_ALGORITHM;
    }
}

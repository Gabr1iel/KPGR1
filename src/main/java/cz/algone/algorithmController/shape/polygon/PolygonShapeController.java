package cz.algone.algorithmController.shape.polygon;

import cz.algone.algorithm.AlgorithmAlias;
import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithmController.scene.SceneModelController;
import cz.algone.algorithmController.shape.ShapeController;
import cz.algone.model.*;
import cz.algone.raster.RasterCanvas;
import cz.algone.algorithm.rasterizer.Rasterizer;
import cz.algone.util.color.ColorPair;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;

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
    }

    @Override
    public void initListeners() {
        canvas.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.SECONDARY) //Získání indexu nebližšího bodu
                nearestPointIndex = polygon.getNearestPoint((int) e.getX(), (int) e.getY());
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
        updateModel();
        polygonRasterizer.rasterize(polygon);
    }

    @Override
    public void updateModel() {
        polygon.setColors(colors);
        sceneModel.getModels().put(DEFAULT_MODELTYPE, polygon);
        polygon = (Polygon) sceneModel.getModels().get(DEFAULT_MODELTYPE);
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

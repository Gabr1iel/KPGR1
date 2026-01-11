package cz.algone.algorithmController.controller3D;

import cz.algone.algorithm.AlgorithmAlias;
import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithm.algorithm3D.Renderer;
import cz.algone.algorithm.rasterizer.line.LineRasterizerBresenham;
import cz.algone.algorithmController.IAlgorithmController;
import cz.algone.algorithmController.scene.SceneModelController;
import cz.algone.model.SceneModel;
import cz.algone.model.solid.*;
import cz.algone.raster.RasterCanvas;
import cz.algone.transforms.*;
import cz.algone.util.color.ColorPair;
import cz.algone.util.keyControll.KeyControllable;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;

public class Controller3D implements IAlgorithmController, KeyControllable {
    private final AlgorithmAlias DEFAULT_ALGORITHM = AlgorithmAlias.BRESENHAM; //změní se na Renderer/se nechá pro získání line algoritmu a renderer se tu vytvoří
    private Canvas canvas;
    private SceneModelController sceneModelController;
    private SceneModel sceneModel;
    private LineRasterizerBresenham rasterizer;
    private ColorPair colors;

    private Renderer renderer;
    private Camera camera;
    private Mat4PerspRH proj;

    private Solid arrow, axisX, axisY, axisZ;
    private float angle = 0;

    @Override
    public void setup(RasterCanvas raster, IAlgorithm algorithm, SceneModelController sceneModelController) {
        this.rasterizer = (LineRasterizerBresenham) algorithm;
        this.canvas = raster.getCanvas();
        this.sceneModelController = sceneModelController;
        this.sceneModel = sceneModelController.sceneModel();
        initialize3DObjects();
        update();
    }

    @Override
    public void initListeners() {
        canvas.widthProperty().addListener((obs, oldValue, newValue) -> initialize3DObjects());
        canvas.heightProperty().addListener((obs, oldValue, newValue) -> initialize3DObjects());
        canvas.setFocusTraversable(true);
        canvas.requestFocus();
    }

    @Override
    public void onKeyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case UP -> {
                angle += 10;
                e.consume();
                update();
            }
            case A -> {
                camera = camera.left(0.5);
                update();
                e.consume();
            }
            case D -> {
                camera = camera.right(0.5);
                e.consume();
                update();
            }
        }
    }

    @Override
    public void setColors(ColorPair colors) {
        this.colors = colors;
    }

    @Override
    public AlgorithmAlias getDefaultAlgorithm() {
        return DEFAULT_ALGORITHM;
    }

    public void initialize3DObjects() {
        renderer = new Renderer(
                rasterizer,
                (int) canvas.getWidth(),
                (int) canvas.getHeight()
        );
        camera = new Camera()
                .withPosition(new Vec3D(1, -2, 1.5))
                .withAzimuth(Math.toRadians(110))
                .withZenith(Math.toRadians(-25))
                .withFirstPerson(true);
        proj = new Mat4PerspRH(
                Math.toRadians(70),
                canvas.getHeight() / (float) canvas.getWidth(),
                0.01,
                200
        );

        renderer.setProj(proj);

        // Solids
        arrow = new Arrow();
        axisX = new AxisX();
        axisY = new AxisY();
        axisZ = new AxisZ();
    }

    public void update() {
        sceneModelController.clearRaster();

        renderer.setView(camera.getViewMatrix());

        Mat4 model = new Mat4Transl(-0.5, 0, 0)
                .mul(new Mat4RotZ(Math.toRadians(angle)))
                .mul(new Mat4Transl(0.5, 0, 0));

        arrow.setModel(model);
        renderer.render(arrow);
        renderer.render(axisX);
        renderer.render(axisZ);
        renderer.render(axisY);

        //co kurva s tímhle?
        //panel.repaint();
    }
}

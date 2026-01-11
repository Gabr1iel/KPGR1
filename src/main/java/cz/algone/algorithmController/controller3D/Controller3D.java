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

    private double lastMouseX;
    private double lastMouseY;
    private boolean dragging = false;

    private final double mouseSensitivity = 0.005; // rad/pixel (doladíš)
    private final double moveSpeed = 0.2;
    private final double fastMultiplier = 3.0;

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
        // Mouse pressed / released
        canvas.setOnMousePressed(e -> {
            dragging = true;
            lastMouseX = e.getX();
            lastMouseY = e.getY();
            canvas.requestFocus();
        });

        canvas.setOnMouseReleased(e -> dragging = false);

        // Mouse dragged -> look around
        canvas.setOnMouseDragged(e -> {
            if (!dragging) return;
            double x = e.getX();
            double y = e.getY();

            double dx = x - lastMouseX;
            double dy = y - lastMouseY;

            lastMouseX = x;
            lastMouseY = y;

            camera = camera.addAzimuth(dx * mouseSensitivity)
                    .addZenith(-dy * mouseSensitivity);

            update();
            e.consume();
        });

        // Scroll -> zoom
        canvas.setOnScroll(e -> {
            double delta = e.getDeltaY(); // kladné = scroll up
            double zoomFactor = (delta > 0) ? 0.9 : 1.1;

            // Doporučení: 3rd person pro čistý zoom
            camera = camera.withFirstPerson(false).mulRadius(zoomFactor);

            update();
            e.consume();
        });

        canvas.widthProperty().addListener((obs, oldValue, newValue) -> initialize3DObjects());
        canvas.heightProperty().addListener((obs, oldValue, newValue) -> initialize3DObjects());
        canvas.setFocusTraversable(true);
        canvas.requestFocus();
    }

    @Override
    public void onKeyPressed(KeyEvent e) {
        double speed = moveSpeed;
        if (e.isShiftDown()) speed *= fastMultiplier;

        switch (e.getCode()) {
            case W -> { camera = camera.forward(speed); update(); e.consume(); }
            case S -> { camera = camera.backward(speed); update(); e.consume(); }
            case A -> {camera = camera.left(speed);update();e.consume();}
            case D -> {camera = camera.right(speed);e.consume();update();}

            // výška
            case Q -> { camera = camera.up(speed); update(); e.consume(); }
            case E -> { camera = camera.down(speed); update(); e.consume(); }

            // rychlé přepnutí režimu
            case F -> { camera = camera.withFirstPerson(!camera.getFirstPerson()); update(); e.consume(); }

            case UP -> {angle += 10;e.consume();update();}
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
    /** Prvotní inicializace 3D objektů, {@link Camera}, {@link Renderer} */
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
    /** Překreslí scénu po pohybu/resize */
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
    }
}

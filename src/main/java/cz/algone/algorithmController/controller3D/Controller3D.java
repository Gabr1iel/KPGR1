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

import java.util.ArrayList;
import java.util.List;

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

    private final List<Solid> axis = new ArrayList<>();
    private final List<Solid> solids = new ArrayList<>();
    private Solid editableSolid;
    private int editableIndex = 0;
    private Axis editAxis = Axis.X;

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
        renderScene();
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

            renderScene();
            e.consume();
        });

        // Scroll -> zoom
        canvas.setOnScroll(e -> {
            double delta = e.getDeltaY(); // kladné = scroll up
            double zoomFactor = (delta > 0) ? 0.9 : 1.1;

            // Doporučení: 3rd person pro čistý zoom
            camera = camera.withFirstPerson(false).mulRadius(zoomFactor);

            renderScene();
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
            case W -> { camera = camera.forward(speed); renderScene(); e.consume(); }
            case S -> { camera = camera.backward(speed); renderScene(); e.consume(); }
            case A -> {camera = camera.left(speed);renderScene();e.consume();}
            case D -> {camera = camera.right(speed);e.consume();renderScene();}

            // výška
            case Q -> { camera = camera.up(speed); renderScene(); e.consume(); }
            case E -> { camera = camera.down(speed); renderScene(); e.consume(); }

            // Přepnutí edit Axis
            case F -> {
                if (editAxis == Axis.X) {
                    editAxis = Axis.Y;
                } else if (editAxis == Axis.Y) {
                    editAxis = Axis.Z;
                } else {
                    editAxis = Axis.X;
                }
                e.consume();
            }
            case R -> {
                editableIndex = (editableIndex + 1) % solids.size();
                editableSolid = solids.get(editableIndex);
                renderScene();
                e.consume(); }

            case UP -> {
                if (e.isShiftDown()) {
                    editableSolid.setScale(editableSolid.getScale() * 1.1);
                } else {
                    translateSelected(0.1);
                }
                updateSolid();
                e.consume();
            }
            case DOWN -> {
                if (e.isShiftDown()) {
                    editableSolid.setScale(editableSolid.getScale() / 1.1);
                } else {
                    translateSelected(-0.1);
                }
                updateSolid();
                e.consume();
            }
            case LEFT -> {
                editableSolid.setAngle(editableSolid.getAngle() - 10);
                updateSolid();e.consume();
            }
            case RIGHT -> {
                editableSolid.setAngle(editableSolid.getAngle() + 10);
                updateSolid();e.consume();
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
        solids.add(new Tetrahedron());
        solids.add(new Cuboid());
        solids.add(new Cylinder(12));
        axis.add(new AxisX());
        axis.add(new AxisY());
        axis.add(new AxisZ());
        editableSolid = solids.get(editableIndex);
    }
    /** Překreslí scénu po pohybu/resize */
    public void renderScene() {
        sceneModelController.clearRaster();

        renderer.setView(camera.getViewMatrix());
        renderer.renderSolids(solids);
        renderer.renderSolids(axis);
    }
    /** Transformace tělesa */
    private void updateSolid() {
        Mat4 rot = switch (editAxis) {
            case X -> new Mat4RotX(Math.toRadians(editableSolid.getAngle()));
            case Y -> new Mat4RotY(Math.toRadians(editableSolid.getAngle()));
            case Z -> new Mat4RotZ(Math.toRadians(editableSolid.getAngle()));
        };
        Mat4 model = new Mat4Transl(editableSolid.getPosition())
                .mul(new Mat4Transl(editableSolid.getPosition().mul(-1)))
                .mul(rot)
                .mul(new Mat4Scale(editableSolid.getScale()))
                .mul(new Mat4Transl(editableSolid.getPosition()));
        editableSolid.setModel(model);
        renderScene();
    }
    /** Zajišťuje pohyb po správné ose pomocí {@link Axis},
     *  podle osy přičte další krok k dané souřadnici v position */
    private void translateSelected(double step) {
        Vec3D pivot = editableSolid.getPosition();
        Vec3D newPivot = switch (editAxis) {
            case X -> new Vec3D(pivot.getX() + step, pivot.getY(), pivot.getZ());
            case Y -> new Vec3D(pivot.getX(), pivot.getY() + step, pivot.getZ());
            case Z -> new Vec3D(pivot.getX(), pivot.getY(), pivot.getZ() + step);
        };
        editableSolid.setPosition(newPivot);
    }

    public enum Axis { X, Y, Z }
}

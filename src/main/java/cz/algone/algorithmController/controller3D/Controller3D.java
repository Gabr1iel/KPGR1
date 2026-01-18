package cz.algone.algorithmController.controller3D;

import cz.algone.algorithm.AlgorithmAlias;
import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithm.algorithm3D.Renderer;
import cz.algone.algorithm.rasterizer.line.LineRasterizerBresenham;
import cz.algone.algorithmController.IAlgorithmController;
import cz.algone.algorithmController.scene.SceneModelController;
import cz.algone.model.SceneModel;
import cz.algone.model.models3D.*;
import cz.algone.model.models3D.axis.AxisX;
import cz.algone.model.models3D.axis.AxisY;
import cz.algone.model.models3D.axis.AxisZ;
import cz.algone.raster.RasterCanvas;
import cz.algone.transforms.*;
import cz.algone.util.color.ColorPair;
import cz.algone.util.keyControll.KeyControllable;
import javafx.application.Platform;
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
        this.sceneModel = sceneModelController.getSceneModel();

        axis.add(new AxisX());
        axis.add(new AxisY());
        axis.add(new AxisZ());

        create3DSpace();
        pushRasterStatus();
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
                pushRasterStatus();
                e.consume();
            }
            case R -> {
                setEditableSolid();
                pushRasterStatus();
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
                editAngleByAxis(-10);
                updateSolid();e.consume();
            }
            case RIGHT -> {
                editAngleByAxis(10);
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

    /** Vytvoření 3D scény, konrétně {@link Camera}, {@link Renderer}
     * a projekční matice */
    public void create3DSpace() {
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        renderer = new Renderer(
                rasterizer,
                (int) width,
                (int) height
        );
        if (camera == null) {
            camera = new Camera()
                    .withPosition(new Vec3D(1, -2, 1.5))
                    .withAzimuth(Math.toRadians(110))
                    .withZenith(Math.toRadians(-25))
                    .withFirstPerson(true);
        }
        proj = new Mat4PerspRH(
                Math.toRadians(70),
                height / (float) width,
                0.01,
                200
        );

        renderer.setProj(proj);
        renderScene();
    }
    /** Překreslí scénu a znovu vykreslí všechny aktuální solidy */
    public void renderScene() {
        sceneModelController.clearRaster();

        renderer.setView(camera.getViewMatrix());
        renderer.renderSolids(solids);
        renderer.renderSolids(axis);
    }
    /** Transformace tělesa, nejdříve vypočítá rotační matici,
     * následně spočítá novou modelovou matici aktuálního solidu */
    private void updateSolid() {
        if (editableSolid != null) {
            Mat4 rot = new Mat4RotX(Math.toRadians(editableSolid.getAngleX()))
                    .mul(new Mat4RotY(Math.toRadians(editableSolid.getAngleY())))
                    .mul(new Mat4RotZ(Math.toRadians(editableSolid.getAngleZ())));
            Mat4 model = new Mat4Transl(editableSolid.getPosition())
                    .mul(new Mat4Transl(editableSolid.getPivot()))
                    .mul(rot)
                    .mul(new Mat4Scale(editableSolid.getScale()))
                    .mul(new Mat4Transl(editableSolid.getPivot().mul(-1)));
            editableSolid.setModel(model);
        }
        renderScene();
    }
    /** Předá {@link SolidToggleEvent} do {@link SceneModel}, následně z něj vezme
     * mapu {@link Solid} a upraví podle ni seznam solidů, potom vyrenderuje scénu*/
    public void addSolid(SolidToggleEvent event) {
        sceneModelController.toggleSolids(event);
        solids.removeIf(s -> !sceneModel.getSolids().containsValue(s));
        for (Solid solid : sceneModel.getSolids().values()) {
            if (!solids.contains(solid)) {
                solids.add(solid);
            }
        }
        setEditableSolid();
        pushRasterStatus();
        updateSolid();
    }
    /** Zajišťuje pohyb po správné ose pomocí {@link Axis},
     *  podle osy přičte další krok k dané souřadnici v position */
    private void translateSelected(double step) {
        Vec3D position = editableSolid.getPosition();
        Vec3D newPosition = switch (editAxis) {
            case X -> new Vec3D(position.getX() + step, position.getY(), position.getZ());
            case Y -> new Vec3D(position.getX(), position.getY() + step, position.getZ());
            case Z -> new Vec3D(position.getX(), position.getY(), position.getZ() + step);
        };
        editableSolid.setPosition(newPosition);
    }
    /** Po přidání/přepnutí {@link Solid)} aktualizuje editableSolid a index*/
    private void setEditableSolid() {
        if (editableSolid != null)
            editableSolid.setSelected(false);
        if (solids.isEmpty()) {
            editableIndex = 0;
            editableSolid = null;
        } else {
            editableIndex = (editableIndex + 1) % solids.size();
            editableSolid = solids.get(editableIndex);
            editableSolid.setSelected(true);
        }
    }
    /** Incrementuje správný angel o parametr Int podle aktuální axis */
    private void editAngleByAxis(int increment) {
        switch (editAxis) {
            case X -> editableSolid.setAngleX(editableSolid.getAngleX() + increment);
            case Y -> editableSolid.setAngleY(editableSolid.getAngleY() + increment);
            case Z -> editableSolid.setAngleZ(editableSolid.getAngleZ() + increment);
        }
    }
    /** Updatuje rasterStatusText v {@link SceneModelController} */
    private void pushRasterStatus() {
        String objectName = (editableSolid == null) ? "Žádný" : editableSolid.getClass().getSimpleName();
        sceneModelController.setRasterStatusText("objekt: " + objectName + " | osa: " + editAxis);
    }

    public enum Axis { X, Y, Z }
}

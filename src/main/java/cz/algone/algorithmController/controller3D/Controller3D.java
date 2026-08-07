package cz.algone.algorithmController.controller3D;

import cz.algone.common.enumAlias.AlgorithmAlias;
import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithm.algorithm3D.Renderer;
import cz.algone.algorithmController.IAlgorithmController;
import cz.algone.model.SceneContext;
import cz.algone.common.enumAlias.ClipMode;
import cz.algone.common.enumAlias.CubicAlias;
import cz.algone.common.enumAlias.EnabledAlias;
import cz.algone.common.enumAlias.ProjMatAlias;
import cz.algone.common.enumAlias.RenderMode;
import cz.algone.common.enumAlias.ShaderMode;
import cz.algone.model.models3D.Solid;
import cz.algone.common.enumAlias.SolidAlias;
import cz.algone.model.models3D.axis.AxisX;
import cz.algone.model.models3D.axis.AxisY;
import cz.algone.model.models3D.axis.AxisZ;
import cz.algone.model.models3D.cubic.BezierCubic;
import cz.algone.model.models3D.cubic.CoonsCubic;
import cz.algone.model.models3D.cubic.HermiteFergusonCubic;
import cz.algone.model.models3D.cubic.IParametricCubic;
import cz.algone.model.models3D.solids.CurveSolid;
import cz.algone.model.models3D.solids.LightSolid;
import cz.algone.transforms.*;
import cz.algone.util.color.ColorPair;
import cz.algone.util.keyControll.KeyControllable;
import javafx.animation.AnimationTimer;
import javafx.collections.MapChangeListener;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Controller3D implements IAlgorithmController, KeyControllable {
    private final AlgorithmAlias DEFAULT_ALGORITHM = AlgorithmAlias.RENDERER;
    private CubicAlias currentCubic = CubicAlias.BEZIER;
    private Canvas canvas;
    private SceneContext sceneContext;

    private Renderer renderer;
    private Camera camera;
    private Mat4Identity proj;
    private ProjMatAlias projMat = ProjMatAlias.PERSP;

    private boolean enabledAnimation = false;
    private AnimationTimer animationTimer;

    private final List<Solid> axis = new ArrayList<>();
    private final List<Solid> solids = new ArrayList<>();
    private Solid editableSolid;
    private int editableIndex = 0;
    private Axis editAxis = Axis.X;

    private double lastMouseX;
    private double lastMouseY;
    private boolean dragging = false;

    private final double mouseSensitivity = 0.005;
    private final double moveSpeed = 0.2;
    private final double fastMultiplier = 3.0;

    @Override
    public void setup(IAlgorithm algorithm, SceneContext sceneContext) {
        this.renderer = (Renderer) algorithm;
        this.sceneContext = sceneContext;
        this.canvas = sceneContext.getImageBuffer().getCanvas();
        renderer.setZBuffer(sceneContext.getZBuffer());

        axis.add(new AxisX());
        axis.add(new AxisY());
        axis.add(new AxisZ());

        sceneContext.getSolids().addListener((MapChangeListener<SolidAlias, Solid>) change -> {
            if (change.wasAdded()) solids.add(change.getValueAdded());
            if (change.wasRemoved()) solids.remove(change.getValueRemoved());
            setEditableSolid();
            pushRasterStatus();
            updateSolid();
        });

        sceneContext.setRenderMode(renderer.getRenderMode());
        sceneContext.setClipMode3D(renderer.getClipMode());

        create3DSpace();
        pushRasterStatus();
        renderScene();
    }

    @Override
    public void initListeners() {
        canvas.setOnMousePressed(e -> {
            dragging = true;
            lastMouseX = e.getX();
            lastMouseY = e.getY();
            canvas.requestFocus();
        });

        canvas.setOnMouseReleased(e -> dragging = false);

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

        canvas.setOnScroll(e -> {
            double delta = e.getDeltaY();
            double zoomFactor = (delta > 0) ? 0.9 : 1.1;

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

            case Q -> { camera = camera.up(speed); renderScene(); e.consume(); }
            case E -> { camera = camera.down(speed); renderScene(); e.consume(); }

            case M -> {
                RenderMode next = renderer.getRenderMode() == RenderMode.WIREFRAME
                        ? RenderMode.FILLED : RenderMode.WIREFRAME;
                sceneContext.setRenderMode(next);
                e.consume();
            }

            case V -> {
                ClipMode[] modes = ClipMode.values();
                ClipMode next = modes[(renderer.getClipMode().ordinal() + 1) % modes.length];
                sceneContext.setClipMode3D(next);
                e.consume();
            }

            case CONTROL -> setCubicAccuracy(5);
            case ALT -> setCubicAccuracy(-5);

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

            case G -> {
                if (editableSolid == null) return;
                if (e.isShiftDown()) {
                    loadTexture();
                } else {
                    editableSolid.cycleShaderMode();
                    sceneContext.setSolidShaderMode(editableSolid.getShaderMode());
                    pushRasterStatus();
                    renderScene();
                }
                e.consume();
            }

        }
    }

    @Override
    public void setColors(ColorPair colors) {}

    @Override
    public AlgorithmAlias getDefaultAlgorithm() {
        return DEFAULT_ALGORITHM;
    }

    /** Vytvoří kameru a projekční matici pro 3D scénu a vykreslí ji. */
    public void create3DSpace() {
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        renderer.setWidth((int) width);
        renderer.setHeight((int) height);
        if (camera == null) {
            camera = new Camera()
                    .withPosition(new Vec3D(1, -2, 1.5))
                    .withAzimuth(Math.toRadians(110))
                    .withZenith(Math.toRadians(-25))
                    .withFirstPerson(true);
        }

        switchProjectionMat(projMat, height, (float) width);
        renderer.setProj(proj);
        renderScene();
    }
    /** Překreslí scénu se všemi tělesy, osami a aktuálním stavem světla. */
    public void renderScene() {
        sceneContext.getZBuffer().clear();
        renderer.setView(camera.getViewMatrix());
        renderer.setCameraEye(camera.getEye());

        LightSolid light = findLight();
        if (light != null) {
            renderer.setLight(light.getWorldLightPosition(), light.getLightColor());
        } else {
            renderer.clearLight();
        }

        for (Solid solid : solids) {
            if (solid instanceof LightSolid) {
                renderer.renderUnlit(solid);
            } else {
                renderer.render(solid);
            }
        }
        renderer.renderAxisSolids(axis);
    }

    /** Vrací LightSolid ve scéně, nebo null pokud neexistuje. */
    private LightSolid findLight() {
        for (Solid solid : solids) {
            if (solid instanceof LightSolid ls) return ls;
        }
        return null;
    }
    /** Sestaví modelovou matici editovaného tělesa z jeho transformací a překreslí scénu. */
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
    /** Posune editované těleso podél aktivní {@link Axis} o daný krok. */
    private void translateSelected(double step) {
        Vec3D position = editableSolid.getPosition();
        Vec3D newPosition = switch (editAxis) {
            case X -> new Vec3D(position.getX() + step, position.getY(), position.getZ());
            case Y -> new Vec3D(position.getX(), position.getY() + step, position.getZ());
            case Z -> new Vec3D(position.getX(), position.getY(), position.getZ() + step);
        };
        editableSolid.setPosition(newPosition);
    }
    /** Vybere další těleso jako editovatelné a aktualizuje jeho selected stav. */
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
        sceneContext.setSelectedSolid(editableSolid);
        sceneContext.setSolidShaderMode(editableSolid == null ? null : editableSolid.getShaderMode());
    }
    /** Přičte hodnotu (ve stupních) k úhlu rotace editovaného tělesa kolem aktivní osy. */
    private void editAngleByAxis(double increment) {
        switch (editAxis) {
            case X -> editableSolid.setAngleX(editableSolid.getAngleX() + increment);
            case Y -> editableSolid.setAngleY(editableSolid.getAngleY() + increment);
            case Z -> editableSolid.setAngleZ(editableSolid.getAngleZ() + increment);
        }
    }
    /** Nastaví projekční matici jako {@link Mat4PerspRH} nebo {@link Mat4OrthoRH}. */
    private void switchProjectionMat(ProjMatAlias mat, double height, float width) {
        double orthoScale = 3.0;
        if (mat == ProjMatAlias.PERSP) {
            proj = new Mat4PerspRH(
                    Math.toRadians(70),
                    height / width,
                    0.01,
                    200
            );
        } else if (mat == ProjMatAlias.ORTHO) {
            proj = new Mat4OrthoRH(
                    orthoScale,
                    orthoScale * (height / width),
                    0.01,
                    200
            );
        }
    }

    private void animate() {
        if (animationTimer != null) return;
        animationTimer = new javafx.animation.AnimationTimer() {
            long lastNs = 0;
            @Override
            public void handle(long now) {
                if (!enabledAnimation) return;
                if (editableSolid == null) return;

                if (lastNs == 0) {lastNs = now; return;}
                double dt = (now - lastNs) / 1_000_000_000.0;
                lastNs = now;

                double delta = 60 * dt;
                editAngleByAxis(delta);
                updateSolid();
            }
        };
        animationTimer.start();
    }
    /** Zastaví běžící animační timer, pokud existuje. */
    private void stopAnimation() {
        if (animationTimer == null) return;
        animationTimer.stop();
        animationTimer = null;
    }
    /** Otevře dialog pro výběr obrázku a nastaví jej jako texturu editovaného tělesa. */
    public void loadTexture() {
        if (editableSolid == null) return;
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Vyberte texturu");
        fileChooser.getExtensionFilters().add(
            new javafx.stage.FileChooser.ExtensionFilter("Obrázky", "*.png", "*.jpg", "*.jpeg", "*.bmp")
        );
        java.io.File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());
        if (file != null) {
            javafx.scene.image.Image img = new javafx.scene.image.Image(file.toURI().toString());
            editableSolid.setTexture(new cz.algone.shader.ShaderTexture(img));
            editableSolid.setShaderMode(cz.algone.common.enumAlias.ShaderMode.TEXTURED);
            sceneContext.setSolidShaderMode(ShaderMode.TEXTURED);
            pushRasterStatus();
            renderScene();
        }
    }

    /** Nastaví danou parametrickou kubiku editovanému {@link CurveSolid}u. */
    private void setCubicToSolid(IParametricCubic cubic) {
        if (editableSolid instanceof CurveSolid) {
            ((CurveSolid) editableSolid).setCurve(cubic);
            updateSolid();
            pushRasterStatus();
        }
    }
    private void setCubicAccuracy(int accuracy) {
        if (editableSolid instanceof CurveSolid) {
            ((CurveSolid) editableSolid).setSteps(((CurveSolid) editableSolid).getSteps() + accuracy);
            updateSolid();
            pushRasterStatus();
        }
    }
    
    /** Aktualizuje rasterStatusText v {@link SceneContext} podle aktuálního stavu scény. */
    private void pushRasterStatus() {
        String objectName = (editableSolid == null) ? "Žádný" : editableSolid.getClass().getSimpleName();
        String activeClip = switch (renderer.getClipMode()) {
            case NONE       -> "Žádný";
            case FAST       -> "Rychlý";
            case ANALYTICAL -> "Analytický";
        };
        String projection = (projMat == ProjMatAlias.PERSP) ? "Perspektivní" : "Pravoúhlá";
        String cubic = currentCubic.name().toLowerCase();
        int accuracy;
        if (editableSolid instanceof CurveSolid solid)
            accuracy = solid.getSteps();
        else
            accuracy = 0;

        String mode = renderer.getRenderMode() == RenderMode.WIREFRAME ? "Drátový" : "Vyplněný";
        String shaderMode = (editableSolid == null) ? "–" : switch (editableSolid.getShaderMode()) {
            case CONSTANT     -> "Jednobarevný";
            case INTERPOLATED -> "Interpolovaný";
            case TEXTURED     -> "Textura" + (editableSolid.hasTexture() ? "" : " (výchozí)");
        };
        String lightInfo = findLight() != null ? "Zapnuto" : "Vypnuto";
        sceneContext.setRasterStatusText("Objekt: " + objectName + " | Clip: " + activeClip + " | Projekce: " + projection + " | Osa: " + editAxis + " | Cubic: " + cubic + " | Přesnost: " + accuracy + " | Režim: " + mode + " | Povrch: " + shaderMode + " | Světlo: " + lightInfo);
    }

    /** Nastaví režim ořezání ve frustum testu. */
    public void setClipMode(ClipMode mode) {
        if (mode == null || renderer.getClipMode() == mode) return;
        renderer.setClipMode(mode);
        pushRasterStatus();
        renderScene();
    }
    /** Přepne mezi drátovým a vyplněným vykreslením. */
    public void setRenderMode(RenderMode mode) {
        if (mode == null || renderer.getRenderMode() == mode) return;
        renderer.setRenderMode(mode);
        pushRasterStatus();
        renderScene();
    }
    /** Nastaví režim povrchu editovanému tělesu. */
    public void setSolidShaderMode(ShaderMode mode) {
        if (mode == null || editableSolid == null) return;
        if (editableSolid.getShaderMode() == mode) return;
        editableSolid.setShaderMode(mode);
        pushRasterStatus();
        renderScene();
    }
    /** Vrátí kameru do výchozí pozice. */
    public void resetCamera() {
        camera = null;
        create3DSpace();
    }

    public void setAnimation(EnabledAlias alias) {
        this.enabledAnimation = alias == EnabledAlias.ENABLED;
        if (enabledAnimation)
            animate();
        else
            stopAnimation();
    }

    public void setProjMat(ProjMatAlias alias) {
        projMat = alias;
        pushRasterStatus();
        create3DSpace();
    }
    /** Přiřadí editovanému solidu kubiku zvolenou podle {@link CubicAlias}. */
    public void setCubic(CubicAlias cubic) {
        currentCubic = cubic;
        switch (cubic.name()) {
            case "BEZIER" -> setCubicToSolid(new BezierCubic());
            case "HERMITE" -> setCubicToSolid(new HermiteFergusonCubic());
            case "COONS" -> setCubicToSolid(new CoonsCubic());
        }
    }
    /** Resetuje stav controlleru do výchozího nastavení. */
    public void clear() {
        currentCubic = CubicAlias.BEZIER;
        projMat = ProjMatAlias.PERSP;
        editableSolid = null;
        enabledAnimation = false;
        stopAnimation();
        solids.clear();
        renderer.clearLight();
        resetSceneContextState();
    }
    /** Vrátí observable stav ve {@link SceneContext}u do výchozích hodnot, čímž se resetuje i UI.
     *  Režim vykreslení a ořezání zůstávají, jak si je uživatel nastavil — čištění scény je nemá měnit. */
    private void resetSceneContextState() {
        if (sceneContext == null) return;
        sceneContext.setSelectedSolid(null);
        sceneContext.setSolidShaderMode(null);
        sceneContext.setAnimationEnabled(EnabledAlias.DISABLED);
        sceneContext.setProjMat(ProjMatAlias.PERSP);
        sceneContext.setCubicAlias(CubicAlias.BEZIER);
        sceneContext.setClipMode3D(renderer.getClipMode());
        sceneContext.setRenderMode(renderer.getRenderMode());
    }

    public enum Axis { X, Y, Z }
}
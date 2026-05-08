package cz.algone.algorithmController.clip;

import cz.algone.common.enumAlias.AlgorithmAlias;
import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithm.clip.ClipService;
import cz.algone.algorithm.fill.scanline.ScanlineFill;
import cz.algone.algorithm.rasterizer.polygon.PolygonRasterizer;
import cz.algone.model.SceneContext;
import cz.algone.algorithmController.shape.ShapeController;
import cz.algone.common.enumAlias.ModelType;
import cz.algone.common.enumAlias.PolygonOrientation;
import cz.algone.model.models2D.Model;
import cz.algone.model.models2D.Point;
import cz.algone.model.models2D.Polygon;
import cz.algone.util.color.ColorPair;
import cz.algone.util.color.ColorUtils;
import cz.algone.util.geometry.Geometry2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import java.util.Collections;

public class ClipPolygonController implements ShapeController {
    private static final ModelType SUBJECT_TYPE = ModelType.POLYGON;
    private static final ModelType CLIP_TYPE = ModelType.CLIP_POLYGON;
    private static final ModelType RESULT_TYPE = ModelType.CLIPPED_POLYGON;
    private final AlgorithmAlias DEFAULT_ALGORITHM = AlgorithmAlias.CLIP_SERVICE;

    private ClipService clipService;
    private Canvas canvas;
    private SceneContext sceneContext;
    private PolygonRasterizer polygonRasterizer;
    private ScanlineFill scanlineFill;
    private Polygon currentPolygon;
    private ColorPair colors;

    private int selectedIndex = -1;
    private PolygonOrientation orientationMode = PolygonOrientation.AUTO;

    @Override
    public void setup(IAlgorithm algorithm, SceneContext sceneContext) {
        this.sceneContext = sceneContext;
        this.canvas = sceneContext.getImageBuffer().getCanvas();
        this.clipService = (ClipService) algorithm;
        polygonRasterizer = clipService.getPolygonRasterizer();
        scanlineFill = clipService.getScanlineFill();
        scanlineFill.setup(sceneContext.getImageBuffer());

        ensureClipPolygon();
        currentPolygon = getClipPolygon();
    }

    @Override
    public void initListeners() {
        canvas.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                if (getSubjectPolygonOrNull() == null || getSubjectPolygonOrNull().getPoints().size() < 3) {
                    createSubjectPolygon();
                    currentPolygon = getSubjectPolygonOrNull();
                    currentPolygon.addPoint(new Point((int) e.getX(), (int) e.getY()));
                    drawScene();
                    currentPolygon = getClipPolygon();
                    return;
                }
                currentPolygon.addPoint(new Point((int) e.getX(), (int) e.getY()));
                applyClip();
                drawScene();
                return;
            }

            if (e.getButton() == MouseButton.SECONDARY) {
                int idx = currentPolygon.getNearestPoint((int) e.getX(), (int) e.getY());
                selectedIndex = idx;

                if (e.isAltDown() && idx >= 0) {
                    currentPolygon.getPoints().remove(idx);
                    selectedIndex = -1;
                    applyClip();
                    drawScene();
                }
            }
        });

        canvas.setOnMouseDragged(e -> {
            if (selectedIndex < 0) return;
            if (currentPolygon.getPoints().isEmpty()) return;
            if (selectedIndex >= currentPolygon.getPoints().size()) {
                selectedIndex = -1;
                return;
            }

            currentPolygon.setPointByIndex(selectedIndex, (int) e.getX(), (int) e.getY());
            applyClip();
            drawScene();
        });

        canvas.setOnMouseReleased(e -> selectedIndex = -1);

        canvas.getScene().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                clearClipPolygon();
                drawScene();
            }
            if (e.getCode() == KeyCode.S) {
                switchCurrentPolygon();
            }
        });
    }

    @Override
    public void drawScene() {
        sceneContext.clearRaster();

        Polygon subject = getSubjectPolygonOrNull();
        Polygon clip = getClipPolygon();
        Polygon result = getResultPolygonOrNull();

        if (subject != null)
            polygonRasterizer.rasterize(subject);

        if (clip != null && clip.getPoints().size() >= 2) {
            polygonRasterizer.rasterize(clip);
        }

        if (result != null) {
            scanlineFill.fill(result, ColorUtils.DEFAULT_HIGHLIGHT_BACKGROUND_COLOR, ColorUtils.interpolateColor(ColorUtils.DEFAULT_HIGHLIGHT_COLOR.primary(), null, 0));
            polygonRasterizer.rasterize(result);
        }
    }

    @Override
    public Model updateModel() {
        return getClipPolygon();
    }

    @Override
    public AlgorithmAlias getDefaultAlgorithm() {
        return DEFAULT_ALGORITHM;
    }

    @Override
    public void setColors(ColorPair colors) {this.colors = colors;}

    /** Nastaví požadovanou orientaci clip polygonu. */
    public void setOrientationMode(PolygonOrientation mode) {
        this.orientationMode = (mode != null) ? mode : PolygonOrientation.AUTO;
    }

    /** Spustí ořezání subject polygonu aktuálním clip polygonem, pokud splňují podmínky. */
    public void applyClip() {
        Polygon subject = getSubjectPolygonOrNull();
        Polygon clip = getClipPolygon();

        if (subject == null || subject.getPoints().size() < 3) return;
        if (clip == null || clip.getPoints().size() < 5) return;
        if (!Geometry2D.isConvex(clip.getPoints())) return;

        enforceOrientationIfNeeded(clip);
        clipService.clip(sceneContext.getModels(), ColorUtils.DEFAULT_HIGHLIGHT_COLOR);
    }
    /** Smaže body clip polygonu i výsledku. */
    private void clearClipPolygon() {
        Polygon clip = getClipPolygon();
        clip.getPoints().clear();
        sceneContext.getModels().remove(RESULT_TYPE);
    }
    /** Otočí pořadí bodů clip polygonu, pokud neodpovídá nastavené orientaci. */
    private void enforceOrientationIfNeeded(Polygon clip) {
        if (clip.getPoints().size() < 3) return;

        boolean ccw = Geometry2D.isCCW(clip.getPoints());
        if (orientationMode == PolygonOrientation.CCW && !ccw) {
            Collections.reverse(clip.getPoints());
        } else if (orientationMode == PolygonOrientation.CW && ccw) {
            Collections.reverse(clip.getPoints());
        }
    }
    /** Vytvoří v scéně clip polygon, pokud ještě neexistuje. */
    private void ensureClipPolygon() {
        if (!sceneContext.getModels().containsKey(CLIP_TYPE)) {
            Polygon clip = new Polygon(ColorUtils.DEFAULT_CLIP_COLOR);
            sceneContext.getModels().put(CLIP_TYPE, clip);
        }
    }

    /** Vytvoří v scéně subject polygon, pokud ještě neexistuje. */
    private void createSubjectPolygon() {
        if (!sceneContext.getModels().containsKey(SUBJECT_TYPE)) {
            Polygon subject = new Polygon(colors);
            sceneContext.getModels().put(SUBJECT_TYPE, subject);
        }
    }

    private Polygon getSubjectPolygonOrNull() {
        Model m = sceneContext.getModels().get(SUBJECT_TYPE);
        return (m instanceof Polygon p) ? p : null;
    }

    private Polygon getResultPolygonOrNull() {
        Model m = sceneContext.getModels().get(RESULT_TYPE);
        return (m instanceof Polygon p) ? p : null;
    }

    private Polygon getClipPolygon() {
        ensureClipPolygon();
        return (Polygon) sceneContext.getModels().get(CLIP_TYPE);
    }

    private void switchCurrentPolygon() {
        if (currentPolygon == getClipPolygon())
            currentPolygon = getSubjectPolygonOrNull();
        else
            currentPolygon = getClipPolygon();
    }
}
package cz.algone.algorithmController.clip;

import cz.algone.algorithm.AlgorithmAlias;
import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithm.clip.ClipService;
import cz.algone.algorithm.fill.scanline.ScanlineFill;
import cz.algone.algorithm.rasterizer.polygon.PolygonRasterizer;
import cz.algone.algorithmController.scene.SceneModelController;
import cz.algone.algorithmController.shape.ShapeController;
import cz.algone.model.*;
import cz.algone.raster.RasterCanvas;
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
    private SceneModelController sceneModelController;
    private SceneModel sceneModel;
    private PolygonRasterizer polygonRasterizer;
    private ScanlineFill scanlineFill;

    // editace clip polygonu
    private int selectedIndex = -1;
    private PolygonOrientation orientationMode = PolygonOrientation.AUTO; // AUTO / FORCE_CW / FORCE_CCW

    @Override
    public void setup(RasterCanvas raster, IAlgorithm algorithm, SceneModelController sceneModelController) {
        this.canvas = raster.getCanvas();
        this.sceneModelController = sceneModelController;
        this.sceneModel = sceneModelController.getSceneModel();
        this.clipService = (ClipService) algorithm;
        polygonRasterizer = clipService.getPolygonRasterizer();
        scanlineFill = clipService.getScanlineFill();
        scanlineFill.setup(raster);

        //Vytvoření nové instance clip polygon
        ensureClipPolygon();
    }

    @Override
    public void initListeners() {
        canvas.setOnMousePressed(e -> {
            ensureClipPolygon();

            if (e.getButton() == MouseButton.PRIMARY) {
                addPointToClip((int) e.getX(), (int) e.getY());
                applyClip();
                drawScene();
                return;
            }

            if (e.getButton() == MouseButton.SECONDARY) {
                Polygon clip = getClipPolygon();
                if (clip.getPoints().isEmpty()) return;

                int idx = clip.getNearestPoint((int) e.getX(), (int) e.getY());
                selectedIndex = idx;

                if (e.isAltDown() && idx >= 0) {
                    clip.getPoints().remove(idx);
                    selectedIndex = -1;
                    drawScene();
                }
            }
        });

        canvas.setOnMouseDragged(e -> {
            if (selectedIndex < 0) return;
            Polygon clip = getClipPolygon();
            if (clip.getPoints().isEmpty()) return;
            if (selectedIndex >= clip.getPoints().size()) {
                selectedIndex = -1;
                return;
            }

            clip.setPointByIndex(selectedIndex, (int) e.getX(), (int) e.getY());
            applyClip();
            drawScene();
        });

        canvas.setOnMouseReleased(e -> selectedIndex = -1);

        // klávesy – Esc = clear clip polygon
        canvas.getScene().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                clearClipPolygon();
                drawScene();
            }
        });
    }

    @Override
    public void drawScene() {
        sceneModelController.clearRaster();

        Polygon subject = getSubjectPolygonOrNull();
        Polygon clip = getClipPolygon();
        Polygon result = getResultPolygonOrNull();

        // 1) Subject
        if (subject != null)
            polygonRasterizer.rasterize(subject);

        // 2) Clip polygon
        if (clip != null && clip.getPoints().size() >= 2) {
            polygonRasterizer.rasterize(clip);
        }

        // 3) Result
        if (result != null) {
            scanlineFill.fill(result, ColorUtils.DEFAULT_HIGHLIGHT_BACKGROUND_COLOR, ColorUtils.interpolateColor(ColorUtils.DEFAULT_HIGHLIGHT_COLOR.primary(), null, 0));
            polygonRasterizer.rasterize(result);
        }
    }

    //Model se aktualizuje průběžně v listenerech, není potřeba updatovat zvlášť
    @Override
    public Model updateModel() {
        return getClipPolygon();
    }

    @Override
    public AlgorithmAlias getDefaultAlgorithm() {
        return DEFAULT_ALGORITHM;
    }

    //Ignorovaná metoda, možnost rozšíření o custom barvy result polygonu
    @Override
    public void setColors(ColorPair colors) {}

    /** Přepínání orientace clip polygonu */
    public void setOrientationMode(PolygonOrientation mode) {
        this.orientationMode = (mode != null) ? mode : PolygonOrientation.AUTO;
    }

    /** Clip polygonu, zkontroluje podmínky pro subject a clip polygon,
     *  následně volá clipService */
    public void applyClip() {
        Polygon subject = getSubjectPolygonOrNull();
        Polygon clip = getClipPolygon();

        if (subject == null || subject.getPoints().size() < 3) return;
        if (clip == null || clip.getPoints().size() < 5) return;
        if (!Geometry2D.isConvex(clip.getPoints())) return;

        enforceOrientationIfNeeded(clip);
        clipService.clip(sceneModel, ColorUtils.DEFAULT_HIGHLIGHT_COLOR);
    }

    /** Přidání bodu do clip polygonu */
    private void addPointToClip(int x, int y) {
        Polygon clip = getClipPolygon();
        clip.addPoint(new Point(x, y));
        enforceOrientationIfNeeded(clip);
    }
    /** Smazaní clip polygonu, volání pomocí klávesy esc */
    private void clearClipPolygon() {
        Polygon clip = getClipPolygon();
        clip.getPoints().clear();
        sceneModel.getModels().remove(RESULT_TYPE);
    }
    /** Zkontroluje clip polygon a v případě že orientace neodpovídá,
     *tak otočí pořadí bodů */
    private void enforceOrientationIfNeeded(Polygon clip) {
        if (clip.getPoints().size() < 3) return;

        boolean ccw = Geometry2D.isCCW(clip.getPoints());
        if (orientationMode == PolygonOrientation.CCW && !ccw) {
            Collections.reverse(clip.getPoints());
        } else if (orientationMode == PolygonOrientation.CW && ccw) {
            Collections.reverse(clip.getPoints());
        }
    }
    /** Pokud v {@link SceneModel} neexistuje ClipPolygon tak vytovří novou instanci */
    private void ensureClipPolygon() {
        if (!sceneModel.getModels().containsKey(CLIP_TYPE)) {
            Polygon clip = new Polygon(ColorUtils.DEFAULT_CLIP_COLOR);
            sceneModel.getModels().put(CLIP_TYPE, clip);
        }
    }

    private Polygon getSubjectPolygonOrNull() {
        Model m = sceneModel.getModels().get(SUBJECT_TYPE);
        return (m instanceof Polygon p) ? p : null;
    }

    private Polygon getResultPolygonOrNull() {
        Model m = sceneModel.getModels().get(RESULT_TYPE);
        return (m instanceof Polygon p) ? p : null;
    }

    private Polygon getClipPolygon() {
        ensureClipPolygon();
        return (Polygon) sceneModel.getModels().get(CLIP_TYPE);
    }
}
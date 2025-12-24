package cz.algone.algorithmController.clip;

import cz.algone.algorithm.AlgorithmAlias;
import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithm.clip.ClipService;
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

    private final AlgorithmAlias DEFAULT_ALGORITHM = AlgorithmAlias.POLYGON; // kreslení hran (ne ořez)
    private final ClipService clipService = new ClipService();

    private RasterCanvas raster;
    private Canvas canvas;
    private SceneModelController sceneModelController;
    private SceneModel sceneModel;

    // tohle u tebe bude nejspíš Rasterizer<Polygon> / PolygonRasterizer
    private cz.algone.algorithm.rasterizer.Rasterizer polygonRasterizer;

    private ColorPair currentColors = ColorUtils.DEFAULT_COLORPICKER_COLOR;

    // editace clip polygonu
    private int selectedIndex = -1;
    private ClipOrientationMode orientationMode = ClipOrientationMode.AUTO; // AUTO / FORCE_CW / FORCE_CCW

    @Override
    public void setup(RasterCanvas raster, IAlgorithm algorithm, SceneModelController sceneModelController) {
        this.raster = raster;
        this.canvas = raster.getCanvas();
        this.sceneModelController = sceneModelController;
        this.sceneModel = sceneModelController.getSceneModel();

        // očekávám, že algoritmus je polygon rasterizer
        this.polygonRasterizer = (cz.algone.algorithm.rasterizer.Rasterizer) algorithm;

        // zajisti, že clip polygon existuje
        ensureClipPolygon();
    }

    @Override
    public void initListeners() {
        raster.clearListeners();

        canvas.setOnMousePressed(e -> {
            ensureClipPolygon();

            if (e.getButton() == MouseButton.PRIMARY) {
                addPointToClip((int) e.getX(), (int) e.getY());
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
            drawScene();
        });

        canvas.setOnMouseReleased(e -> selectedIndex = -1);

        // klávesy – Enter = apply clip, R = reverse orientation clip polygonu, Esc = clear clip polygon
        canvas.getScene().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                applyClip();
                drawScene();
            } else if (e.getCode() == KeyCode.R) {
                reverseClipPolygon();
                drawScene();
            } else if (e.getCode() == KeyCode.ESCAPE) {
                clearClipPolygon();
                drawScene();
            }
        });
    }

    @Override
    public void drawScene() {
        sceneModelController.clearRaster();

        Polygon subject = getSubjectPolygonOrNull();
        Polygon clip = getClipPolygonOrNull();
        Polygon result = getResultPolygonOrNull();

        // 1) Subject
        if (subject != null)
            polygonRasterizer.rasterize(subject);

        // 2) Clip polygon – doporučuju jinou barvu (viz níže)
        if (clip != null && clip.getPoints().size() >= 2) {
            // pokud nechceš měnit barvu v modelu, můžeš si dočasně vytvořit kopii s jinými colors
            polygonRasterizer.rasterize(clip);
        }

        // 3) Result
        if (result != null)
            polygonRasterizer.rasterize(result);
    }

    @Override
    public Model updateModel() {
        // v tomto controlleru model aktualizuješ průběžně v listeneru, tady stačí vrátit clip polygon
        return getClipPolygon();
    }

    @Override
    public AlgorithmAlias getDefaultAlgorithm() {
        return DEFAULT_ALGORITHM;
    }

    @Override
    public void setColors(ColorPair colors) {
        this.currentColors = colors;
        // nastavení barvy výsledného polygonu? možná nebude potřeba a nechá se taky statické
    }

    // ====== veřejné API pro napojení UI ======

    /** volitelné – napojíš na toggle v UI */
    public void setOrientationMode(ClipOrientationMode mode) {
        this.orientationMode = (mode != null) ? mode : ClipOrientationMode.AUTO;
    }

    /** zavolej třeba z tlačítka "Clip" */
    public void applyClip() {
        Polygon subject = getSubjectPolygonOrNull();
        Polygon clip = getClipPolygonOrNull();

        if (subject == null || subject.getPoints().size() < 3) return;
        if (clip == null || clip.getPoints().size() < 5) return;
        if (!Geometry2D.isConvex(clip.getPoints())) return;

        enforceOrientationIfNeeded(clip);

        // výsledek uloží do sceneModel jako CLIPPED_POLYGON
        Polygon result = clipService.clip(sceneModel, currentColors);
    }

    // ====== interní logika ======

    private void addPointToClip(int x, int y) {
        Polygon clip = getClipPolygon();
        clip.addPoint(new Point(x, y));
        enforceOrientationIfNeeded(clip);
    }

    private void clearClipPolygon() {
        Polygon clip = getClipPolygon();
        clip.getPoints().clear();
        sceneModel.getModels().remove(RESULT_TYPE);
    }

    private void reverseClipPolygon() {
        Polygon clip = getClipPolygon();
        Collections.reverse(clip.getPoints());
    }

    private void enforceOrientationIfNeeded(Polygon clip) {
        if (clip.getPoints().size() < 3) return;

        boolean ccw = Geometry2D.isCCW(clip.getPoints());
        if (orientationMode == ClipOrientationMode.FORCE_CCW && !ccw) {
            Collections.reverse(clip.getPoints());
        } else if (orientationMode == ClipOrientationMode.FORCE_CW && ccw) {
            Collections.reverse(clip.getPoints());
        }
    }

    private void ensureClipPolygon() {
        if (!sceneModel.getModels().containsKey(CLIP_TYPE)) {
            Polygon clip = new Polygon(currentColors);
            sceneModel.getModels().put(CLIP_TYPE, clip);
        }
    }

    private Polygon getSubjectPolygonOrNull() {
        Model m = sceneModel.getModels().get(SUBJECT_TYPE);
        return (m instanceof Polygon p) ? p : null;
    }

    private Polygon getClipPolygonOrNull() {
        Model m = sceneModel.getModels().get(CLIP_TYPE);
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

    // ====== enumy ======
    public enum ClipOrientationMode {
        AUTO, FORCE_CW, FORCE_CCW
    }
}
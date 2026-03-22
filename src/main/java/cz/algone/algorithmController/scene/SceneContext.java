package cz.algone.algorithmController.scene;

import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithmController.IAlgorithmController;
import cz.algone.common.enumAlias.*;
import cz.algone.model.SceneModel;
import cz.algone.model.models3D.SolidToggleEvent;
import cz.algone.model.models3D.wiredSolids.Solid;
import cz.algone.model.models3D.wiredSolids.SolidsCollection;
import cz.algone.raster.ImageBuffer;
import cz.algone.raster.ZBuffer;
import cz.algone.util.color.ColorPair;
import cz.algone.util.color.ColorUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/** Centrální observable stav aplikace.
 *  UI komponenty naslouchají Properties, algorithm controllery přistupují k rendering infrastruktuře. */
public class SceneContext {

    /**
     * === Algoritmy ===
     */
    private IAlgorithmController currentAlgorithmController;
    private IAlgorithm currentAlgorithm;
    /**
     * === Observable stav pro Algoritmy ===
     */
    private final ObjectProperty<IAlgorithmController> currentAlgorithmControllerProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<IAlgorithm> currentAlgorithmProperty = new SimpleObjectProperty<>();

    /**
     * === Rendering infrastruktura (nastaví RasterController) ===
     */
    private ImageBuffer imageBuffer;
    private ZBuffer zBuffer;
    private final SceneModel sceneModel = new SceneModel();
    private final SolidsCollection solidsCollection = new SolidsCollection();

    /**
     *  === Property gettery pro listenery ===
     */
    public ObjectProperty<IAlgorithmController> controllerProperty() {return currentAlgorithmControllerProperty;}
    public ObjectProperty<IAlgorithm> algorithmProperty() {return currentAlgorithmProperty;}
    public ObjectProperty<AlgorithmControllerAlias> controllerAliasProperty() { return controllerAlias; }
    public ObjectProperty<AlgorithmAlias> algorithmAliasProperty() { return algorithmAlias; }
    public ObjectProperty<SceneAlias> sceneProperty() { return scene; }
    public ObjectProperty<ColorPair> colorsProperty() { return colors; }
    public StringProperty rasterStatusProperty() { return rasterStatusText; }

    /**
     * === Value gettery/settery ===
     */
    public IAlgorithmController getCurrentAlgorithmController() {return currentAlgorithmController;}
    public void setCurrentAlgorithmController(IAlgorithmController controller) {currentAlgorithmController = controller;}

    public IAlgorithm getCurrentAlgorithm() {return currentAlgorithm;}
    public void setCurrentAlgorithm(IAlgorithm algorithm) {currentAlgorithm = algorithm;}

    public AlgorithmControllerAlias getControllerAlias() { return controllerAlias.get(); }
    public void setControllerAlias(AlgorithmControllerAlias alias) { controllerAlias.set(alias); }

    public AlgorithmAlias getAlgorithmAlias() { return algorithmAlias.get(); }
    public void setAlgorithmAlias(AlgorithmAlias alias) { algorithmAlias.set(alias); }

    public SceneAlias getScene() { return scene.get(); }
    public void setScene(SceneAlias alias) { scene.set(alias); }

    public ColorPair getColors() { return colors.get(); }
    public void setColors(ColorPair colorPair) { colors.set(colorPair); }

    public String getRasterStatusText() { return rasterStatusText.get(); }
    public void setRasterStatusText(String text) { rasterStatusText.set(text); }

    /**
     *  === Observable stav pro UI (první vrstva) ===
     */
    private final ObjectProperty<AlgorithmControllerAlias> controllerAlias = new SimpleObjectProperty<>();
    private final ObjectProperty<AlgorithmAlias> algorithmAlias = new SimpleObjectProperty<>();
    private final ObjectProperty<SceneAlias> scene = new SimpleObjectProperty<>(SceneAlias.SCENE_2D);
    private final ObjectProperty<ColorPair> colors = new SimpleObjectProperty<>(ColorUtils.DEFAULT_COLORPICKER_COLOR);
    private final StringProperty rasterStatusText = new SimpleStringProperty("");

    /**
     * === Observable stav pro UI (druhá vrstva - nastavení algoritmů) ===
     */
    private final ObjectProperty<PatternAlias> patternAlias = new SimpleObjectProperty<>();
    private final ObjectProperty<EnabledAlias> clip3DEnabled = new SimpleObjectProperty<>(EnabledAlias.DISABLED);
    private final ObjectProperty<EnabledAlias> animationEnabled = new SimpleObjectProperty<>(EnabledAlias.DISABLED);
    private final ObjectProperty<ProjMatAlias> projMat = new SimpleObjectProperty<>(ProjMatAlias.PERSP);
    private final ObjectProperty<CubicAlias> cubicAlias = new SimpleObjectProperty<>();
    private final ObjectProperty<PolygonOrientation> polygonOrientation = new SimpleObjectProperty<>();


    /**
     * === Druhá vrstva - property gettery ===
     */
    public ObjectProperty<PatternAlias> patternAliasProperty() { return patternAlias; }
    public ObjectProperty<EnabledAlias> clip3DEnabledProperty() { return clip3DEnabled; }
    public ObjectProperty<EnabledAlias> animationEnabledProperty() { return animationEnabled; }
    public ObjectProperty<ProjMatAlias> projMatProperty() { return projMat; }
    public ObjectProperty<CubicAlias> cubicAliasProperty() { return cubicAlias; }
    public ObjectProperty<PolygonOrientation> polygonOrientationProperty() { return polygonOrientation; }

    /**
     * === Druhá vrstva - value gettery/settery ===
     */
    public PatternAlias getPatternAlias() { return patternAlias.get(); }
    public void setPatternAlias(PatternAlias alias) { patternAlias.set(alias); }

    public EnabledAlias getClip3DEnabled() { return clip3DEnabled.get(); }
    public void setClip3DEnabled(EnabledAlias alias) { clip3DEnabled.set(alias); }

    public EnabledAlias getAnimationEnabled() { return animationEnabled.get(); }
    public void setAnimationEnabled(EnabledAlias alias) { animationEnabled.set(alias); }

    public ProjMatAlias getProjMat() { return projMat.get(); }
    public void setProjMat(ProjMatAlias alias) { projMat.set(alias); }

    public CubicAlias getCubicAlias() { return cubicAlias.get(); }
    public void setCubicAlias(CubicAlias alias) { cubicAlias.set(alias); }

    public PolygonOrientation getPolygonOrientation() { return polygonOrientation.get(); }
    public void setPolygonOrientation(PolygonOrientation orientation) { polygonOrientation.set(orientation); }

    /**
     * === Rendering infrastruktura (setter pro RasterController) ===
     */
    public void setRenderingInfra(ImageBuffer imageBuffer, ZBuffer zBuffer) {
        this.imageBuffer = imageBuffer;
        this.zBuffer = zBuffer;
    }

    public ImageBuffer getImageBuffer() { return imageBuffer; }
    public ZBuffer getZBuffer() { return zBuffer; }
    public SceneModel getSceneModel() { return sceneModel; }

    /**
     * === Operace nad scénou ===
     */
    public void clearRasterAndScene() {
        imageBuffer.clear();
        sceneModel.clear();
    }

    public void clearRaster() {
        imageBuffer.clear();
    }

    public void toggleSolids(SolidToggleEvent event) {
        if (event.enabled()) {
            sceneModel.getSolids().put(event.alias(), solidsCollection.solidsMap.get(event.alias()));
        } else {
            Solid solid = solidsCollection.solidsMap.get(event.alias());
            solid.resetTransform();
            sceneModel.getSolids().remove(event.alias());
        }
    }
}

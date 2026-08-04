package cz.algone.model;

import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithmController.IAlgorithmController;
import cz.algone.common.enumAlias.*;
import cz.algone.model.models2D.Model;
import cz.algone.model.models3D.Solid;
import cz.algone.model.models3D.SolidsCollection;
import cz.algone.raster.ImageBuffer;
import cz.algone.raster.ZBuffer;
import cz.algone.util.color.ColorPair;
import cz.algone.util.color.ColorUtils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.util.HashMap;
import java.util.Map;

/** Centrální observable stav aplikace.
 *  UI komponenty naslouchají Properties, algorithm controllery přistupují k rendering infrastruktuře. */
public class SceneContext {

    /**
     * === Algoritmy (observable) ===
     */
    private final ObjectProperty<IAlgorithmController> currentAlgorithmControllerProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<IAlgorithm> currentAlgorithmProperty = new SimpleObjectProperty<>();

    /**
     * === Rendering infrastruktura (nastaví RasterController) ===
     */
    private ImageBuffer imageBuffer;
    private ZBuffer zBuffer;
    private final Map<ModelType, Model> models = new HashMap<>();
    private final ObservableMap<SolidAlias, Solid> solids = FXCollections.observableHashMap();
    private final SolidsCollection solidsCollection = new SolidsCollection();

    /**
     *  === Property gettery pro listenery ===
     */
    public ObjectProperty<IAlgorithmController> controllerProperty() {return currentAlgorithmControllerProperty;}
    public ObjectProperty<IAlgorithm> algorithmProperty() {return currentAlgorithmProperty;}
    public ObjectProperty<AlgorithmControllerAlias> controllerAliasProperty() { return controllerAlias; }
    public ObjectProperty<AlgorithmAlias> algorithmAliasProperty() { return algorithmAlias; }
    public ObjectProperty<SceneAlias> sceneProperty() { return scene; }
    public ObjectProperty<CategoryAlias> categoryProperty() { return category; }
    public ObjectProperty<RightTabAlias> rightTabProperty() { return rightTab; }
    public BooleanProperty animationBarVisibleProperty() { return animationBarVisible; }
    public ObjectProperty<ColorPair> colorsProperty() { return colors; }
    public StringProperty rasterStatusProperty() { return rasterStatusText; }

    /**
     * === Value gettery/settery ===
     */
    public IAlgorithmController getCurrentAlgorithmController() { return currentAlgorithmControllerProperty.get(); }
    public void setCurrentAlgorithmController(IAlgorithmController controller) { currentAlgorithmControllerProperty.set(controller); }

    public IAlgorithm getCurrentAlgorithm() { return currentAlgorithmProperty.get(); }
    public void setCurrentAlgorithm(IAlgorithm algorithm) { currentAlgorithmProperty.set(algorithm); }

    public AlgorithmControllerAlias getControllerAlias() { return controllerAlias.get(); }
    public void setControllerAlias(AlgorithmControllerAlias alias) { controllerAlias.set(alias); }

    public AlgorithmAlias getAlgorithmAlias() { return algorithmAlias.get(); }
    public void setAlgorithmAlias(AlgorithmAlias alias) { algorithmAlias.set(alias); }

    public SceneAlias getScene() { return scene.get(); }
    public void setScene(SceneAlias alias) { scene.set(alias); }

    public CategoryAlias getCategory() { return category.get(); }
    public void setCategory(CategoryAlias alias) { category.set(alias); }

    public RightTabAlias getRightTab() { return rightTab.get(); }
    public void setRightTab(RightTabAlias tab) { rightTab.set(tab); }

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
    private final ObjectProperty<CategoryAlias> category = new SimpleObjectProperty<>();
    private final ObjectProperty<RightTabAlias> rightTab = new SimpleObjectProperty<>(RightTabAlias.SCENE);
    /** Zobrazení přehrávače animací pod plátnem. */
    private final BooleanProperty animationBarVisible = new SimpleBooleanProperty(false);
    private final ObjectProperty<ColorPair> colors = new SimpleObjectProperty<>(ColorUtils.DEFAULT_COLORPICKER_COLOR);
    private final StringProperty rasterStatusText = new SimpleStringProperty("");

    /**
     * === Observable stav pro UI (druhá vrstva - nastavení algoritmů) ===
     */
    private final ObjectProperty<PatternAlias> patternAlias = new SimpleObjectProperty<>();
    private final ObjectProperty<ClipMode> clipMode3D = new SimpleObjectProperty<>(ClipMode.NONE);
    private final ObjectProperty<EnabledAlias> animationEnabled = new SimpleObjectProperty<>(EnabledAlias.DISABLED);
    private final ObjectProperty<ProjMatAlias> projMat = new SimpleObjectProperty<>(ProjMatAlias.PERSP);
    private final ObjectProperty<CubicAlias> cubicAlias = new SimpleObjectProperty<>(CubicAlias.BEZIER);
    private final ObjectProperty<PolygonOrientation> polygonOrientation = new SimpleObjectProperty<>(PolygonOrientation.AUTO);
    private final ObjectProperty<RenderMode> renderMode = new SimpleObjectProperty<>(RenderMode.FILLED);
    private final ObjectProperty<ShaderMode> solidShaderMode = new SimpleObjectProperty<>();
    private final ObjectProperty<Solid> selectedSolid = new SimpleObjectProperty<>();


    /**
     * === Druhá vrstva - property gettery ===
     */
    public ObjectProperty<PatternAlias> patternAliasProperty() { return patternAlias; }
    public ObjectProperty<ClipMode> clipMode3DProperty() { return clipMode3D; }
    public ObjectProperty<EnabledAlias> animationEnabledProperty() { return animationEnabled; }
    public ObjectProperty<ProjMatAlias> projMatProperty() { return projMat; }
    public ObjectProperty<CubicAlias> cubicAliasProperty() { return cubicAlias; }
    public ObjectProperty<PolygonOrientation> polygonOrientationProperty() { return polygonOrientation; }
    public ObjectProperty<RenderMode> renderModeProperty() { return renderMode; }
    public ObjectProperty<ShaderMode> solidShaderModeProperty() { return solidShaderMode; }
    public ObjectProperty<Solid> selectedSolidProperty() { return selectedSolid; }

    /**
     * === Druhá vrstva - value gettery/settery ===
     */
    public PatternAlias getPatternAlias() { return patternAlias.get(); }
    public void setPatternAlias(PatternAlias alias) { patternAlias.set(alias); }

    public ClipMode getClipMode3D() { return clipMode3D.get(); }
    public void setClipMode3D(ClipMode mode) { clipMode3D.set(mode); }

    public EnabledAlias getAnimationEnabled() { return animationEnabled.get(); }
    public void setAnimationEnabled(EnabledAlias alias) { animationEnabled.set(alias); }

    public ProjMatAlias getProjMat() { return projMat.get(); }
    public void setProjMat(ProjMatAlias alias) { projMat.set(alias); }

    public CubicAlias getCubicAlias() { return cubicAlias.get(); }
    public void setCubicAlias(CubicAlias alias) { cubicAlias.set(alias); }

    public PolygonOrientation getPolygonOrientation() { return polygonOrientation.get(); }
    public void setPolygonOrientation(PolygonOrientation orientation) { polygonOrientation.set(orientation); }

    public RenderMode getRenderMode() { return renderMode.get(); }
    public void setRenderMode(RenderMode mode) { renderMode.set(mode); }

    public ShaderMode getSolidShaderMode() { return solidShaderMode.get(); }
    public void setSolidShaderMode(ShaderMode mode) { solidShaderMode.set(mode); }

    public Solid getSelectedSolid() { return selectedSolid.get(); }
    public void setSelectedSolid(Solid solid) { selectedSolid.set(solid); }

    /**
     * === Rendering infrastruktura (setter pro RasterController) ===
     */
    public void setRenderingInfra(ImageBuffer imageBuffer, ZBuffer zBuffer) {
        this.imageBuffer = imageBuffer;
        this.zBuffer = zBuffer;
    }

    public ImageBuffer getImageBuffer() { return imageBuffer; }
    public ZBuffer getZBuffer() { return zBuffer; }
    public Map<ModelType, Model> getModels() { return models; }
    public ObservableMap<SolidAlias, Solid> getSolids() { return solids; }

    /**
     * === Operace nad scénou ===
     */
    public void clearRasterAndScene() {
        imageBuffer.clear();
        models.clear();
        for (Solid solid : solids.values()) {
            solid.resetTransform();
        }
        solids.clear();
    }

    public void clearRaster() {
        imageBuffer.clear();
    }

    public void toggleSolids(SolidAlias alias, boolean enabled) {
        if (enabled) {
            solids.put(alias, solidsCollection.solidsMap.get(alias));
        } else {
            Solid solid = solidsCollection.solidsMap.get(alias);
            solid.resetTransform();
            solids.remove(alias);
        }
    }
}

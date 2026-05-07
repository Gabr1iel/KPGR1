package cz.algone.algorithmController;

import cz.algone.algorithm.AlgorithmCollection;
import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithm.fill.IFill;
import cz.algone.algorithm.fill.pattern.PatternCollection;
import cz.algone.algorithmController.clip.ClipPolygonController;
import cz.algone.algorithmController.controller3D.Controller3D;
import cz.algone.model.SceneContext;
import cz.algone.common.enumAlias.AlgorithmAlias;
import cz.algone.common.enumAlias.AlgorithmControllerAlias;

public class MainAlgorithmController {
    private final SceneContext sceneContext;

    private final AlgorithmCollection algorithmCollection = new AlgorithmCollection();
    private final AlgorithmControllerCollection algorithmControllerCollection = new AlgorithmControllerCollection();
    private final PatternCollection patternCollection = new PatternCollection();

    public MainAlgorithmController(SceneContext sceneContext) {
        this.sceneContext = sceneContext;

        sceneContext.controllerAliasProperty().addListener((observable, oldValue, newValue) -> {
            updateAlgorithmController(newValue);
        });
        sceneContext.algorithmAliasProperty().addListener((observable, oldValue, newValue) -> {
            setupAlgorithm(newValue);
        });

        sceneContext.colorsProperty().addListener((obs, old, newVal) -> {
            if (newVal != null && sceneContext.getCurrentAlgorithmController() != null)
                sceneContext.getCurrentAlgorithmController().setColors(newVal);
        });

        sceneContext.patternAliasProperty().addListener((obs, old, newVal) -> {
            if (sceneContext.getCurrentAlgorithm() instanceof IFill fill)
                fill.setPattern(newVal != null ? patternCollection.patternMap.get(newVal) : null);
        });
        sceneContext.polygonOrientationProperty().addListener((obs, old, newVal) -> {
            if (newVal != null && sceneContext.getCurrentAlgorithmController() instanceof ClipPolygonController clip)
                clip.setOrientationMode(newVal);
        });
        sceneContext.clip3DEnabledProperty().addListener((obs, old, newVal) -> {
            if (newVal != null && sceneContext.getCurrentAlgorithmController() instanceof Controller3D c3d)
                c3d.setEnabledClip(newVal);
        });
        sceneContext.animationEnabledProperty().addListener((obs, old, newVal) -> {
            if (newVal != null && sceneContext.getCurrentAlgorithmController() instanceof Controller3D c3d)
                c3d.setAnimation(newVal);
        });
        sceneContext.projMatProperty().addListener((obs, old, newVal) -> {
            if (newVal != null && sceneContext.getCurrentAlgorithmController() instanceof Controller3D c3d)
                c3d.setProjMat(newVal);
        });
        sceneContext.cubicAliasProperty().addListener((obs, old, newVal) -> {
            if (newVal != null && sceneContext.getCurrentAlgorithmController() instanceof Controller3D c3d)
                c3d.setCubic(newVal);
        });
    }

    /** Inicializuje aktuální {@link IAlgorithm} a {@link IAlgorithmController}. */
    private void setupAlgorithmController() {
        IAlgorithmController controller = sceneContext.getCurrentAlgorithmController();
        IAlgorithm algorithm = sceneContext.getCurrentAlgorithm();
        sceneContext.getImageBuffer().clearListeners();
        algorithm.setup(sceneContext.getImageBuffer());
        controller.setup(algorithm, sceneContext);
        controller.setColors(sceneContext.getColors());
        controller.initListeners();
    }

    /** Uloží do {@link SceneContext} algoritmus podle {@link AlgorithmAlias} a inicializuje ho. */
    private void setupAlgorithm(AlgorithmAlias alias) {
        sceneContext.setCurrentAlgorithm(algorithmCollection.algorithmMap.get(alias));
        setupAlgorithmController();
    }

    /** Uloží do {@link SceneContext} controller podle {@link AlgorithmControllerAlias} a jeho výchozí algoritmus. */
    private void updateAlgorithmController(AlgorithmControllerAlias alias) {
        IAlgorithmController controller = algorithmControllerCollection.algorithmControllerMap.get(alias);
        sceneContext.setCurrentAlgorithmController(controller);
        sceneContext.setAlgorithmAlias(controller.getDefaultAlgorithm());
    }
}
